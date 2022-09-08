/*
 * Copyright (C) 2004-2020 L2J DataPack
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

import java.util.List;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.instancemanager.WalkingManager;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;

/**
 * Knoriks AI.
 * @author Maneco2
 * @version 2.6.2.0
 */
public class Knoriks extends AbstractNpcAI {
	// NPC
	private static final int KNORIKS = 22857;
	// Skills
	private static final SkillHolder DARK_WIND = new SkillHolder(6743); // Dark Wind
	private static final SkillHolder DARK_STORM = new SkillHolder(6744); // Dark Storm
	private static final SkillHolder DARK_BLADE = new SkillHolder(6747); // Dark Blade
	// Misc
	private static final String SHOUT_FLAG = "SHOUT_FLAG";
	private static final int MAX_CHASE_DIST = 3000;
	private static int SpawnCount = 0;
	
	public Knoriks() {
		super(Knoriks.class.getSimpleName(), "ai/individual");
		addAggroRangeEnterId(KNORIKS);
		addSkillSeeId(KNORIKS);
		addTeleportId(KNORIKS);
		addAttackId(KNORIKS);
		addSpawnId(KNORIKS);
		startQuestTimer("KNORIKS_SPAWN", 1800000, null, null);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		switch (event) {
			case "CORE_AI": {
				if (npc != null) {
					((L2Attackable) npc).clearAggroList();
					npc.disableCoreAI(false);
				}
				break;
			}
			case "CHECK_ROUTE": {
				WalkingManager.getInstance().onSpawn(npc);
				break;
			}
			case "KNORIKS_SPAWN": {
				if (SpawnCount < 3) {
					SpawnCount++;
					addSpawn(KNORIKS, 140641, 114525, -3755, 0, false, 0);
					addSpawn(KNORIKS, 143789, 110205, -3968, 0, false, 0);
					addSpawn(KNORIKS, 146466, 109789, -3440, 0, false, 0);
					addSpawn(KNORIKS, 145482, 120250, -3944, 0, false, 0);
					startQuestTimer("KNORIKS_SPAWN", 60000, null, null);
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon) {
		final L2Character mostHated = ((L2Attackable) npc).getMostHated();
		if ((mostHated != null) && (npc.isInsideRadius(attacker, 250, false, false))) {
			if ((getRandom(100) < 10) && (!npc.isCastingNow())) {
				npc.doCast(getRandomBoolean() ? DARK_STORM : DARK_BLADE);
			}
			
			for (L2Character obj : npc.getKnownList().getKnownCharactersInRadius(200)) {
				if ((obj != null) && (obj.isMonster())) {
					if ((getRandom(100) < 10) && (obj.isInCombat()) && (!obj.isCastingNow())) {
						obj.doCast(getRandomBoolean() ? DARK_STORM : DARK_BLADE);
					}
				}
			}
		}
		
		if ((npc.calculateDistance(npc.getSpawn().getLocation(), false, false) > MAX_CHASE_DIST) || (Math.abs(npc.getZ() - npc.getSpawn().getZ()) > 450)) {
			npc.disableCoreAI(true);
			npc.teleToLocation(npc.getSpawn().getLocation());
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance player, Skill skill, List<L2Object> targets, boolean isSummon) {
		if ((getRandom(100) < 10) && (!npc.isCastingNow()) && (!npc.isInsideRadius(player, 250, false, false))) {
			addSkillCastDesire(npc, player, DARK_WIND, 1000000L);
		}
		return super.onSkillSee(npc, player, skill, targets, isSummon);
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		final L2Spawn spawn = npc.getSpawn();
		spawn.setAmount(1);
		spawn.setRespawnDelay(1800);
		spawn.startRespawn();
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isSummon) {
		if ((getRandom(100) < 50) && (!npc.getVariables().getBoolean(SHOUT_FLAG, false))) {
			npc.getVariables().set(SHOUT_FLAG, true);
			broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.WHOS_THERE_IF_YOU_DISTURB_THE_TEMPER_OF_THE_GREAT_LAND_DRAGON_ANTHARAS_I_WILL_NEVER_FORGIVE_YOU);
		}
		return super.onAggroRangeEnter(npc, player, isSummon);
	}
	
	@Override
	protected void onTeleport(L2Npc npc) {
		WalkingManager.getInstance().cancelMoving(npc);
		startQuestTimer("CORE_AI", 100, npc, null);
		notifyEvent("CHECK_ROUTE", npc, null);
	}
}
