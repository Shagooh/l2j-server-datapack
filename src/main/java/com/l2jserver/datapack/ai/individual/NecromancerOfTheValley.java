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
package com.l2jserver.datapack.ai.individual;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * Necromancer of the Valley AI.
 * @author Adry_85
 * @author Maneco2
 * @since 2.6.2.0
 */
public class NecromancerOfTheValley extends AbstractNpcAI {
	// NPCs
	private static final int EXPLODING_ORC_GHOST = 22818;
	private static final int WRATHFUL_ORC_GHOST = 22819;
	private static final int NECROMANCER_OF_THE_VALLEY = 22858;
	// Skill
	private static final SkillHolder SELF_DESTRUCTION = new SkillHolder(6850);
	// Variable
	private static final String MID_HP_FLAG = "MID_HP_FLAG";
	// Misc
	private static final double HP_PERCENTAGE = 0.60;
	
	public NecromancerOfTheValley() {
		super(NecromancerOfTheValley.class.getSimpleName(), "ai/individual");
		addAttackId(NECROMANCER_OF_THE_VALLEY);
		addSpawnId(EXPLODING_ORC_GHOST);
		addSpellFinishedId(EXPLODING_ORC_GHOST);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon) {
		if (npc.getCurrentHp() < (npc.getMaxHp() * HP_PERCENTAGE)) {
			if ((getRandom(100) < 10) && !npc.getVariables().getBoolean(MID_HP_FLAG, false)) {
				npc.getVariables().set(MID_HP_FLAG, true);
				addAttackDesire(addSpawn((getRandomBoolean() ? EXPLODING_ORC_GHOST : WRATHFUL_ORC_GHOST), npc, true), attacker);
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		for (L2Character obj : npc.getKnownList().getKnownCharactersInRadius(200)) {
			if (obj.isPlayer() && !obj.isDead()) {
				addSkillCastDesire(npc, obj, SELF_DESTRUCTION, 1000000L);
			}
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onSpellFinished(L2Npc npc, L2PcInstance player, Skill skill) {
		if ((skill == SELF_DESTRUCTION.getSkill()) && (npc != null) && !npc.isDead()) {
			npc.doDie(player);
		}
		return super.onSpellFinished(npc, player, skill);
	}
}
