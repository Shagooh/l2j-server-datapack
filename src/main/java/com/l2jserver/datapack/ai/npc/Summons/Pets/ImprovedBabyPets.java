/*
 * Copyright © 2004-2021 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.ai.npc.Summons.Pets;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.ListenerRegisterType;
import com.l2jserver.gameserver.model.events.annotations.RegisterEvent;
import com.l2jserver.gameserver.model.events.annotations.RegisterType;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogout;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.AbnormalType;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;

import java.util.Optional;

/**
 * Improved Baby Pets AI.
 * @author St3eT
 * @since 2.6.0.0
 */
public final class ImprovedBabyPets extends AbstractNpcAI {
	// NPCs
	private static final int[] IMPROVED_BABY_PETS = {
		16034, // Improved Baby Buffalo
		16035, // Improved Baby Kookaburra
		16036, // Improved Baby Cougar
	};
	
	// Skill
	private static final int BUFF_CONTROL = 5771;
	
	public ImprovedBabyPets() {
		super(ImprovedBabyPets.class.getSimpleName(), "ai/npc/Summons/Pets");
		addSummonSpawnId(IMPROVED_BABY_PETS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		if (player != null) {
			final L2PetInstance pet = (L2PetInstance) player.getSummon();
			if (pet == null) {
				cancelQuestTimer("CAST_BUFF", null, player);
				cancelQuestTimer("CAST_HEAL", null, player);
			} else if (event.equals("CAST_HEAL") && player.isInCombat() && !pet.isHungry()) {
				final double hpPer = (player.getCurrentHp() / player.getMaxHp()) * 100;
				final double mpPer = (player.getCurrentMp() / player.getMaxMp()) * 100;
				final int healStep = (int) Math.floor((pet.getLevel() / 5.) - 11);
				final int healType = pet.getTemplate().getParameters().getInt("heal_type", 0);
				
				switch (healType) {
					case 0 -> {
						if (hpPer < 30) {
							castHealSkill(pet, Util.constrain(healStep, 0, 3), 2);
						} else if (mpPer < 60) {
							castHealSkill(pet, Util.constrain(healStep, 0, 3), 1);
						}
					}
					case 1 -> {
						if ((hpPer >= 30) && (hpPer < 70)) {
							castHealSkill(pet, Util.constrain(healStep, 0, 3), 1);
						} else if (hpPer < 30) {
							castHealSkill(pet, Util.constrain(healStep, 0, 3), 2);
						}
					}
				}
			} else if (event.equals("CAST_BUFF") && !pet.isAffectedBySkill(BUFF_CONTROL) && !pet.isHungry()) {
				final int buffStep = (int) Util.constrain(Math.floor((pet.getLevel() / 5.) - 11), 0, 3);
				for (int i = 1; i <= (2 * (1 + buffStep)); i++) {
					castBuffSkill(pet, buffStep, i);
				}
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@RegisterEvent(EventType.ON_PLAYER_LOGOUT)
	@RegisterType(ListenerRegisterType.GLOBAL)
	public void OnPlayerLogout(OnPlayerLogout event) {
		cancelQuestTimer("CAST_BUFF", null, event.getActiveChar());
		cancelQuestTimer("CAST_HEAL", null, event.getActiveChar());
	}
	
	@Override
	public void onSummonSpawn(L2Summon summon) {
		startQuestTimer("CAST_BUFF", 10000, null, summon.getOwner(), true);
		startQuestTimer("CAST_HEAL", 3000, null, summon.getOwner(), true);
	}
	
	private boolean castBuffSkill(L2Summon summon, int stepNumber, int buffNumber) {
		final L2PcInstance owner = summon.getOwner();
		if ((owner == null) || owner.isDead() || owner.isInvul()) {
			return false;
		}
		
		final StatsSet parameters = summon.getTemplate().getParameters();
		
		final SkillHolder skill = Optional.ofNullable(parameters.getObject("step" + stepNumber + "_merged_buff0" + buffNumber, SkillHolder.class))
						.orElseGet(() -> parameters.getObject("step" + stepNumber + "_buff0" + buffNumber, SkillHolder.class));
		
		if (skill != null) {
			final int targetType = parameters.getInt("step" + stepNumber + "_buff_target0" + buffNumber, 0);
			if (!hasAbnormal(owner, skill.getSkill().getAbnormalType()) && summon.checkDoCastConditions(skill.getSkill())) {
				final boolean previousFollowStatus = summon.getFollowStatus();
				
				if (!previousFollowStatus && !summon.isInsideRadius(owner, skill.getSkill().getCastRange(), true, true)) {
					return false;
				}
				
				if ((targetType >= 0) && (targetType <= 2)) {
					summon.setTarget((targetType == 1) ? summon : owner);
					summon.doCast(skill.getSkill());
					summon.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_USES_S1).addSkillName(skill.getSkill()));
					
					if (previousFollowStatus != summon.getFollowStatus()) {
						summon.setFollowStatus(previousFollowStatus);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private void castHealSkill(L2Summon summon, int stepNumber, int healNumber) {
		final L2PcInstance owner = summon.getOwner();
		final StatsSet parameters = summon.getTemplate().getParameters();
		final SkillHolder skill = parameters.getObject("step" + stepNumber + "_heal0" + healNumber, SkillHolder.class);
		final int targetType = parameters.getInt("step" + stepNumber + "_heal_target0" + healNumber, 0);
		
		if ((skill != null) && (owner != null) && summon.checkDoCastConditions(skill.getSkill()) && !owner.isDead()) {
			final boolean previousFollowStatus = summon.getFollowStatus();
			
			if (!previousFollowStatus && !summon.isInsideRadius(owner, skill.getSkill().getCastRange(), true, true)) {
				return;
			}
			
			if (!hasAbnormal(owner, skill.getSkill().getAbnormalType())) {
				if ((targetType >= 0) && (targetType <= 2)) {
					summon.setTarget((targetType == 1) ? summon : owner);
					summon.doCast(skill.getSkill());
					summon.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_USES_S1).addSkillName(skill.getSkill()));
					
					if (previousFollowStatus != summon.getFollowStatus()) {
						summon.setFollowStatus(previousFollowStatus);
					}
				}
			}
		}
	}
	
	private static boolean hasAbnormal(L2PcInstance player, AbnormalType type) {
		return player.getEffectList().getBuffInfoByAbnormalType(type) != null;
	}
}