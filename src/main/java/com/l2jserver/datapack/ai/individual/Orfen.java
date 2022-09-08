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

import static com.l2jserver.gameserver.config.Configuration.grandBoss;

import java.util.List;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.enums.audio.Music;
import com.l2jserver.gameserver.instancemanager.GrandBossManager;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2GrandBossInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.model.zone.type.L2BossZone;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;

/**
 * Orfen AI
 * @author Emperorc
 * @author Maneco2
 */
public final class Orfen extends AbstractNpcAI {
	// NPCs
	private static final int ORFEN = 29014;
	private static final int RAIKEL = 29015;
	private static final int RAIKEL_LEOS = 29016;
	private static final int RIBA_IREN = 29018;
	// Orfen Location
	private static final Location ORFEN_LOC_1 = new Location(43728, 17220, -4342, 0);
	private static final Location ORFEN_LOC_2 = new Location(55024, 17368, -5412, 0);
	private static final Location ORFEN_LOC_3 = new Location(53504, 21248, -5486, 0);
	private static final Location ORFEN_LOC_4 = new Location(53248, 24576, -5262, 0);
	// Zone
	private static final int ZONE = 12013;
	// Skills
	private static final SkillHolder ORFEN_STRIKE = new SkillHolder(4063);
	private static final SkillHolder ORFEN_DISPEL = new SkillHolder(4064);
	private static final SkillHolder S_NPC_BLOW4 = new SkillHolder(4067, 4);
	private static final SkillHolder S_ORFEN_HEAL = new SkillHolder(4516);
	// Variables
	private boolean isAttacked = false;
	private boolean isDefensive = false;
	private static L2BossZone _zone;
	private L2GrandBossInstance _orfen = null;
	private L2Npc _riba = null;
	// Status
	private static final int ALIVE = 0;
	private static final int DEAD = 1;
	
	public Orfen() {
		super(Orfen.class.getSimpleName(), "ai/individual");
		addKillId(ORFEN);
		addSpawnId(ORFEN, RIBA_IREN);
		addExitZoneId(ZONE);
		addSkillSeeId(ORFEN);
		addTeleportId(ORFEN);
		addAttackId(ORFEN, RAIKEL_LEOS, RAIKEL, RIBA_IREN);
		_zone = GrandBossManager.getInstance().getZone(43728, 17220, -4342);
		StatsSet info = GrandBossManager.getInstance().getStatsSet(ORFEN);
		if (GrandBossManager.getInstance().getBossStatus(ORFEN) == DEAD) {
			final long remain = info.getLong("respawn_time") - System.currentTimeMillis();
			if (remain > 0) {
				startQuestTimer("ORFEN_UNLOCK", remain, null, null);
			} else {
				notifyEvent("ORFEN_UNLOCK", null, null);
			}
		} else {
			notifyEvent("ORFEN_UNLOCK", null, null);
		}
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
			case "ORFEN_UNLOCK": {
				StatsSet info = GrandBossManager.getInstance().getStatsSet(ORFEN);
				int hp = info.getInt("currentHP");
				int mp = info.getInt("currentMP");
				if (getRandom(100) < 40) {
					_orfen = (L2GrandBossInstance) addSpawn(ORFEN, 55024, 17368, -5412, 0, false, 0);
				} else if (getRandom(100) < 30) {
					_orfen = (L2GrandBossInstance) addSpawn(ORFEN, 53504, 21248, -5486, 0, false, 0);
				} else {
					_orfen = (L2GrandBossInstance) addSpawn(ORFEN, 53248, 24576, -5262, 0, false, 0);
				}
				_orfen.setCurrentHpMp(hp, mp);
				
				if (GrandBossManager.getInstance().getBossStatus(ORFEN) == DEAD) {
					GrandBossManager.getInstance().setBossStatus(ORFEN, ALIVE);
				}
				spawnBoss(_orfen);
				break;
			}
			case "CHECK_ZONE": {
				if (npc != null) {
					if (npc.getCurrentHp() > (npc.getMaxHp() * 0.95)) {
						if ((isDefensive) && (!isAttacked)) {
							isAttacked = true;
							npc.disableCoreAI(true);
							if (getRandom(100) < 33) {
								npc.teleToLocation(ORFEN_LOC_2);
								npc.getSpawn().setLocation(ORFEN_LOC_2);
							} else if (getRandom(100) < 66) {
								npc.teleToLocation(ORFEN_LOC_3);
								npc.getSpawn().setLocation(ORFEN_LOC_3);
							} else {
								npc.teleToLocation(ORFEN_LOC_4);
								npc.getSpawn().setLocation(ORFEN_LOC_4);
							}
							
							if (isDefensive) {
								isDefensive = false;
							}
						}
					} else {
						if ((npc.getCurrentHp()) < (npc.getMaxHp() / 2)) {
							if (!isDefensive) {
								isDefensive = true;
								npc.disableCoreAI(true);
								npc.teleToLocation(ORFEN_LOC_1);
								npc.getSpawn().setLocation(ORFEN_LOC_1);
								if (isAttacked) {
									isAttacked = false;
								}
							}
						}
					}
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon) {
		if ((npc != null) && (!npc.isCastingNow()) && (!npc.isCoreAIDisabled())) {
			switch (npc.getId()) {
				case ORFEN: {
					if ((npc.getCurrentHp() - damage) < (npc.getMaxHp() / 2)) {
						if (!isDefensive) {
							isDefensive = true;
							npc.disableCoreAI(true);
							npc.teleToLocation(ORFEN_LOC_1);
							npc.getSpawn().setLocation(ORFEN_LOC_1);
							if (isAttacked) {
								isAttacked = false;
							}
						}
						if (getRandom(100) < 90) {
							_riba.setTarget(attacker);
							_riba.doCast(S_ORFEN_HEAL);
						} else if (getRandom(100) < 10) {
							_riba.setTarget(attacker);
							_riba.doCast(S_ORFEN_HEAL);
						}
					}
					if ((npc.calculateDistance(attacker, false, false) > 300) && (npc.calculateDistance(attacker, false, false) < 1000) && (getRandom(100) < 10)) {
						if (getRandom(100) < 33) {
							broadcastNpcSay(npc, Say2.ALL, NpcStringId.S1_STOP_KIDDING_YOURSELF_ABOUT_YOUR_OWN_POWERLESSNESS);
						} else if (getRandom(100) < 66) {
							broadcastNpcSay(npc, Say2.ALL, NpcStringId.S1_ILL_MAKE_YOU_FEEL_WHAT_TRUE_FEAR_IS);
						} else {
							broadcastNpcSay(npc, Say2.ALL, NpcStringId.YOURE_REALLY_STUPID_TO_HAVE_CHALLENGED_ME_S1_GET_READY);
						}
						
						attacker.teleToLocation(npc.getLocation());
						addSkillCastDesire(npc, attacker, ORFEN_STRIKE, 1000000L);
					} else if (getRandom(100) < 20) {
						addSkillCastDesire(npc, attacker, ORFEN_DISPEL, 1000000L);
					}
				}
				case RAIKEL_LEOS: {
					if (getRandom(100) < 5) {
						addSkillCastDesire(npc, attacker, S_NPC_BLOW4, 1000000L);
					}
				}
				case RAIKEL:
				case RIBA_IREN: {
					if ((npc.getCurrentHp() - damage) < (npc.getMaxHp() / 2)) {
						npc.setTarget(npc);
						npc.doCast(S_ORFEN_HEAL);
					}
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, Skill skill, List<L2Object> targets, boolean isSummon) {
		if (npc.getId() == ORFEN) {
			final L2Character originalCaster = isSummon ? caster.getSummon() : caster;
			if ((skill.getEffectPoint() > 0) && (getRandom(100) < 20) && (npc.calculateDistance(originalCaster, false, false) < 1000)) {
				if (getRandom(100) < 25) {
					broadcastNpcSay(npc, Say2.ALL, NpcStringId.S1_STOP_KIDDING_YOURSELF_ABOUT_YOUR_OWN_POWERLESSNESS);
				} else if (getRandom(100) < 50) {
					broadcastNpcSay(npc, Say2.ALL, NpcStringId.S1_ILL_MAKE_YOU_FEEL_WHAT_TRUE_FEAR_IS);
				} else if (getRandom(100) < 75) {
					broadcastNpcSay(npc, Say2.ALL, NpcStringId.YOURE_REALLY_STUPID_TO_HAVE_CHALLENGED_ME_S1_GET_READY);
				} else {
					broadcastNpcSay(npc, Say2.ALL, NpcStringId.S1_DO_YOU_THINK_THATS_GOING_TO_WORK);
				}
				
				originalCaster.teleToLocation(npc.getLocation());
				addSkillCastDesire(npc, caster, ORFEN_DISPEL, 1000000L);
			}
		}
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		switch (npc.getId()) {
			case ORFEN: {
				notifyEvent("CHECK_ZONE", npc, null);
				startQuestTimer("CHECK_ZONE", 10000, npc, null, true);
			}
			case RIBA_IREN: {
				_riba = npc;
				break;
			}
		}
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		if (npc.getId() == ORFEN) {
			cancelQuestTimers("CHECK_ZONE");
			npc.broadcastPacket(Music.BS02_D_7000.getPacket());
			GrandBossManager.getInstance().setBossStatus(ORFEN, DEAD);
			final long respawnTime = (grandBoss().getIntervalOfOrfenSpawn() + getRandom(-grandBoss().getRandomOfOrfenSpawn(), grandBoss().getRandomOfOrfenSpawn())) * 3600000;
			GrandBossManager.getInstance().getStatsSet(ORFEN).set("respawn_time", (System.currentTimeMillis() + respawnTime));
			startQuestTimer("ORFEN_UNLOCK", respawnTime, null, null);
			_orfen = null;
			_riba = null;
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onExitZone(L2Character character, L2ZoneType zone) {
		if ((character.isAttackable()) && (character.isRaid()) && (!_zone.isInsideZone(_orfen)) && (_orfen != null)) {
			_orfen.disableCoreAI(true);
			_orfen.teleToLocation(_orfen.getSpawn().getLocation());
			startQuestTimer("CORE_AI", 100, _orfen, null);
		}
		return super.onExitZone(character, zone);
	}
	
	@Override
	protected void onTeleport(L2Npc npc) {
		startQuestTimer("CORE_AI", 100, npc, null);
	}
	
	public void spawnBoss(L2GrandBossInstance npc) {
		GrandBossManager.getInstance().addBoss(npc);
		npc.broadcastPacket(Music.BS01_A_7000.getPacket());
		_orfen = npc;
	}
}
