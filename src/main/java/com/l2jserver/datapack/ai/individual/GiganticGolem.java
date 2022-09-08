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
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.instancemanager.GlobalVariablesManager;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.SpecialCamera;

/**
 * Gigantic Golem AI.
 * @author Kerberos
 * @author Maneco2
 * @version 2.6.2.0
 */
public class GiganticGolem extends AbstractNpcAI {
	// NPCs
	private static final int DR_CHAOS = 32033;
	private static final int GIGANTIC_GOLEM = 25703;
	private static final int STRANGE_MACHINE = 32032;
	private static final int GIGANTIC_BOOM_GOLEM = 25705;
	// Skills
	private static final SkillHolder SMOKE = new SkillHolder(6265);
	private static final SkillHolder EMP_SHOCK = new SkillHolder(6263);
	private static final SkillHolder GOLEM_BOOM = new SkillHolder(6264);
	private static final SkillHolder NPC_EARTH_SHOT = new SkillHolder(6608);
	// Variables
	private L2Npc _raidBoss;
	private boolean _skillsAcess = false;
	private static long _lastAttack = 0;
	private static final int RESPAWN = 24;
	private static final int MAX_CHASE_DIST = 3000;
	private static final double MIN_HP_PERCENTAGE = 0.30;
	private static final String SPAWN_FLAG = "SPAWN_FLAG";
	private static final String ATTACK_FLAG = "ATTACK_FLAG";
	// Locations
	private static final Location PLAYER_TELEPORT = new Location(94832, -112624, -3304);
	private static final Location DR_CHAOS_LOC = new Location(96320, -110912, -3328, 8191);
	
	public GiganticGolem() {
		super(GiganticGolem.class.getSimpleName(), "ai/individual");
		addFirstTalkId(DR_CHAOS);
		addKillId(GIGANTIC_GOLEM);
		addTeleportId(GIGANTIC_GOLEM);
		addMoveFinishedId(GIGANTIC_BOOM_GOLEM);
		addSpawnId(GIGANTIC_GOLEM, GIGANTIC_BOOM_GOLEM);
		addAttackId(GIGANTIC_GOLEM, GIGANTIC_BOOM_GOLEM);
		
		final long remain = GlobalVariablesManager.getInstance().getLong("GolemRespawn", 0) - System.currentTimeMillis();
		if (remain > 0) {
			startQuestTimer("CLEAR_STATUS", remain, null, null);
		} else {
			startQuestTimer("CLEAR_STATUS", 1000, null, null);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		switch (event) {
			case "ATTACK_MACHINE": {
				for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(STRANGE_MACHINE)) {
					final L2Npc obj = spawn.getLastSpawn();
					if (obj != null) {
						if (npc.getId() == DR_CHAOS) {
							npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, obj);
							npc.broadcastPacket(new SpecialCamera(npc, 1, -200, 15, 10000, 1000, 20000, 0, 0, 0, 0, 0));
						}
					}
				}
				startQuestTimer("ACTION_CAMERA", 10000, npc, player);
				break;
			}
			case "ACTION_CAMERA": {
				startQuestTimer("MOVE_SHOW", 2500, npc, player);
				npc.broadcastPacket(new SpecialCamera(npc, 1, -150, 10, 3000, 1000, 20000, 0, 0, 0, 0, 0));
				break;
			}
			case "MOVE_SHOW": {
				startQuestTimer("TELEPORT", 2000, npc, player);
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(96055, -110759, -3312, 0));
				broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.FOOLS_WHY_HAVENT_YOU_FLED_YET_PREPARE_TO_LEARN_A_LESSON);
				break;
			}
			case "TELEPORT": {
				if (player.isInParty()) {
					final L2Party party = player.getParty();
					final boolean isInCC = party.isInCommandChannel();
					final List<L2PcInstance> members = (isInCC) ? party.getCommandChannel().getMembers() : party.getMembers();
					for (L2PcInstance groupMembers : members) {
						if (groupMembers.isInsideRadius(npc, 2000, true, false)) {
							groupMembers.teleToLocation(PLAYER_TELEPORT, true);
						}
					}
				} else {
					player.teleToLocation(PLAYER_TELEPORT);
				}
				
				if ((npc != null) && (npc.getId() == DR_CHAOS)) {
					npc.deleteMe();
				}
				startQuestTimer("WAIT_CAMERA", 1000, npc, player);
				break;
			}
			case "WAIT_CAMERA": {
				startQuestTimer("SPAWN_RAID", 1000, npc, player);
				npc.broadcastPacket(new SpecialCamera(npc, 30, -200, 20, 6000, 700, 8000, 0, 0, 0, 0, 0));
				break;
			}
			case "SPAWN_RAID": {
				addSpawn(GIGANTIC_GOLEM, 94640, -112496, -3360, 0, false, 0);
				break;
			}
			case "FLAG": {
				npc.getVariables().set(SPAWN_FLAG, false);
				break;
			}
			case "CORE_AI": {
				if (npc != null) {
					((L2Attackable) npc).clearAggroList();
					npc.disableCoreAI(false);
				}
				break;
			}
			case "CLEAR_STATUS": {
				addSpawn(DR_CHAOS, DR_CHAOS_LOC, false, 0);
				GlobalVariablesManager.getInstance().set("GolemRespawn", 0);
				break;
			}
			case "SKILL_ATTACK": {
				addSkillCastDesire(npc, npc, SMOKE, 1000000L);
				if (!npc.getVariables().getBoolean(ATTACK_FLAG, false)) {
					npc.disableCoreAI(true);
					npc.getVariables().set(ATTACK_FLAG, true);
				}
				break;
			}
			case "MOVE_TIME": {
				if (npc != null) {
					for (L2Character obj : npc.getKnownList().getKnownCharactersInRadius(3000)) {
						if ((obj != null) && (obj.isRaid())) {
							addMoveToDesire(npc, new Location(obj.getX() + getRandom(-200, 200), obj.getY() + getRandom(-200, 200), obj.getZ() + 20, 0), 0);
						}
					}
				}
				break;
			}
			case "CHECK_ATTACK": {
				if ((_lastAttack + 1800000) < System.currentTimeMillis()) {
					if (npc != null) {
						npc.deleteMe();
						cancelQuestTimer("CHECK_ATTACK", npc, null);
						startQuestTimer("CLEAR_STATUS", 1000, null, null);
					}
				} else {
					startQuestTimer("CHECK_ATTACK", 60000, npc, null);
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		if (npc.getId() == DR_CHAOS) {
			startQuestTimer("ATTACK_MACHINE", 3000, npc, player);
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(96320, -110912, -3328, 0));
			broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.HOW_DARE_YOU_TRESPASS_INTO_MY_TERRITORY_HAVE_YOU_NO_FEAR);
		}
		return "";
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon) {
		if (npc.getId() == GIGANTIC_BOOM_GOLEM) {
			npc.doCast(GOLEM_BOOM);
		} else {
			_lastAttack = System.currentTimeMillis();
			
			if ((_raidBoss != null) && (!npc.isCastingNow())) {
				if (getRandom(100) < 5) {
					npc.doCast(NPC_EARTH_SHOT);
				} else if ((getRandom(100) < 1) && (npc.getCurrentHp() < (npc.getMaxHp() * MIN_HP_PERCENTAGE))) {
					if (_skillsAcess) {
						npc.doCast(EMP_SHOCK);
					} else {
						_skillsAcess = true;
						_raidBoss.enableSkill(EMP_SHOCK.getSkill());
					}
				} else if (_skillsAcess) {
					_skillsAcess = false;
					_raidBoss.disableSkill(EMP_SHOCK.getSkill(), -1);
				}
			}
			if (!npc.getVariables().getBoolean(SPAWN_FLAG, false)) {
				npc.getVariables().set(SPAWN_FLAG, true);
				int posX = npc.getX() + getRandom(-200, 200);
				int posY = npc.getY() + getRandom(-200, 200);
				addSpawn(GIGANTIC_BOOM_GOLEM, posX + getRandom(-200, 200), posY + getRandom(-200, 200), npc.getZ() + 20, 0, false, 0);
				addSpawn(GIGANTIC_BOOM_GOLEM, posX + getRandom(-200, 200), posY + getRandom(-200, 200), npc.getZ() + 20, 0, false, 0);
				addSpawn(GIGANTIC_BOOM_GOLEM, posX + getRandom(-200, 200), posY + getRandom(-200, 200), npc.getZ() + 20, 0, false, 0);
				addSpawn(GIGANTIC_BOOM_GOLEM, posX + getRandom(-200, 200), posY + getRandom(-200, 200), npc.getZ() + 20, 0, false, 0);
				addSpawn(GIGANTIC_BOOM_GOLEM, posX + getRandom(-200, 200), posY + getRandom(-200, 200), npc.getZ() + 20, 0, false, 0);
				addSpawn(GIGANTIC_BOOM_GOLEM, posX + getRandom(-200, 200), posY + getRandom(-200, 200), npc.getZ() + 20, 0, false, 0);
				startQuestTimer("FLAG", 360000, npc, null);
			}
			if (npc.calculateDistance(npc.getSpawn().getLocation(), false, false) > MAX_CHASE_DIST) {
				npc.disableCoreAI(true);
				npc.teleToLocation(npc.getSpawn().getLocation());
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final long respawnTime = RESPAWN * 3600000;
		GlobalVariablesManager.getInstance().set("GolemRespawn", System.currentTimeMillis() + respawnTime);
		startQuestTimer("CLEAR_STATUS", respawnTime, null, null);
		cancelQuestTimer("CHECK_ATTACK", npc, null);
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	protected void onTeleport(L2Npc npc) {
		startQuestTimer("CORE_AI", 100, npc, null);
	}
	
	@Override
	public void onMoveFinished(L2Npc npc) {
		startQuestTimer("SKILL_ATTACK", 1000, npc, null);
		startQuestTimer("MOVE_TIME", 3000, npc, null);
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		if (npc.getId() == GIGANTIC_BOOM_GOLEM) {
			npc.setIsRunning(true);
			npc.scheduleDespawn(360000);
			startQuestTimer("MOVE_TIME", 3000, npc, null);
			((L2Attackable) npc).setCanReturnToSpawnPoint(false);
		} else {
			_raidBoss = npc;
			_lastAttack = System.currentTimeMillis();
			_raidBoss.disableSkill(EMP_SHOCK.getSkill(), -1);
			startQuestTimer("CHECK_ATTACK", 300000, npc, null);
			broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.BWAH_HA_HA_YOUR_DOOM_IS_AT_HAND_BEHOLD_THE_ULTRA_SECRET_SUPER_WEAPON);
		}
		return super.onSpawn(npc);
	}
}
