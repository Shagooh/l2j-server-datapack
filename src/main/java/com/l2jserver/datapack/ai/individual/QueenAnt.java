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
import java.util.concurrent.CopyOnWriteArrayList;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.enums.MountType;
import com.l2jserver.gameserver.enums.audio.Music;
import com.l2jserver.gameserver.instancemanager.GrandBossManager;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2GrandBossInstance;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.AttributeType;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.model.zone.type.L2BossZone;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;

/**
 * Queen Ant AI
 * @author Emperorc
 * @author Maneco2
 */
public final class QueenAnt extends AbstractNpcAI {
	// NPCs
	private static final int QUEEN = 29001;
	private static final int LARVA = 29002;
	private static final int NURSE = 29003;
	private static final int GUARD = 29004;
	private static final int ROYAL = 29005;
	
	private static final int[] MOBS = {
		QUEEN,
		LARVA,
		NURSE,
		GUARD,
		ROYAL
	};
	
	// Queen Ant Location
	private static final int QUEEN_X = -21610;
	private static final int QUEEN_Y = 181594;
	private static final int QUEEN_Z = -5734;
	// Larva Location
	private static final Location LARVA_LOCATION = new Location(-21600, 179482, -5846, getRandom(360));
	// Zones
	private static final int ZONE = 12012;
	private static final Location TELE_LOCATION_1 = new Location(-19480, 187344, -5600);
	private static final Location TELE_LOCATION_2 = new Location(-17928, 180912, -5520);
	private static final Location TELE_LOCATION_3 = new Location(-23808, 182368, -5600);
	// Skills
	private static final SkillHolder RAID_CURSE = new SkillHolder(4215); // Raid Curse - Silence
	private static final SkillHolder RAID_CURSE_2 = new SkillHolder(4515); // Raid Curse - Stone
	private static final SkillHolder QUEEN_ANT_BRANDISH = new SkillHolder(4017); // Queen Ant Brandish
	private static final SkillHolder DECREASE_SPEED_1 = new SkillHolder(4018); // Decrease Speed
	private static final SkillHolder DECREASE_SPEED_2 = new SkillHolder(4019); // Decrease Speed
	private static final SkillHolder HEAL_QUEEN_ANT_1 = new SkillHolder(4020); // Heal Queen Ant1
	private static final SkillHolder HEAL_QUEEN_ANT_2 = new SkillHolder(4024); // Heal Queen Ant2
	private static final SkillHolder ANTI_STRIDER = new SkillHolder(4258); // Hinder Strider
	// Variables
	private static L2BossZone _zone;
	private static final String ATTACK_FLAG = "ATTACK_FLAG";
	private L2MonsterInstance _queen = null;
	private L2MonsterInstance _larva = null;
	private final List<L2MonsterInstance> _nurses = new CopyOnWriteArrayList<>();
	// Status
	private static final int ALIVE = 0;
	private static final int DEAD = 1;
	
	public QueenAnt() {
		super(QueenAnt.class.getSimpleName(), "ai/individual");
		addKillId(MOBS);
		addSpawnId(MOBS);
		addExitZoneId(ZONE);
		addEnterZoneId(ZONE);
		addSkillSeeId(QUEEN);
		addMoveFinishedId(QUEEN);
		addAttackId(QUEEN, NURSE, GUARD, ROYAL);
		
		_zone = GrandBossManager.getInstance().getZone(QUEEN_X, QUEEN_Y, QUEEN_Z);
		final StatsSet info = GrandBossManager.getInstance().getStatsSet(QUEEN);
		if (GrandBossManager.getInstance().getBossStatus(QUEEN) == DEAD) {
			final long remain = info.getLong("respawn_time") - System.currentTimeMillis();
			if (remain > 0) {
				startQuestTimer("QUEEN_UNLOCK", remain, null, null);
			} else {
				notifyEvent("QUEEN_UNLOCK", null, null);
			}
		} else {
			int hp = info.getInt("currentHP");
			int mp = info.getInt("currentMP");
			int loc_x = info.getInt("loc_x");
			int loc_y = info.getInt("loc_y");
			int loc_z = info.getInt("loc_z");
			int heading = info.getInt("heading");
			if (!_zone.isInsideZone(loc_x, loc_y, loc_z)) {
				loc_x = QUEEN_X;
				loc_y = QUEEN_Y;
				loc_z = QUEEN_Z;
			}
			L2GrandBossInstance queen = (L2GrandBossInstance) addSpawn(QUEEN, loc_x, loc_y, loc_z, heading, false, 0);
			queen.setCurrentHpMp(hp, mp);
			spawnBoss(queen);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		switch (event) {
			case "CHECK_ZONE": {
				if (npc != null) {
					addMoveToDesire(npc, new Location(-21610, 181594, -5734, 0), 0);
					if (_zone.isInsideZone(npc)) {
						cancelQuestTimers("CHECK_ZONE");
					}
				}
				break;
			}
			case "CORE_AI": {
				if ((npc != null) && (npc.isCoreAIDisabled()) && (_zone.isInsideZone(npc))) {
					((L2Attackable) npc).clearAggroList();
					npc.disableCoreAI(false);
					npc.setIsImmobilized(false);
					npc.getVariables().set(ATTACK_FLAG, false);
				}
				break;
			}
			case "CORE_MOVEMENT": {
				if (npc != null) {
					final L2Object obj = npc.getTarget();
					if ((obj != null) && (_queen != null) && (obj.isPlayer()) && (_zone.isInsideZone(obj))) {
						_queen.setIsImmobilized(false);
					}
					npc.getVariables().set(ATTACK_FLAG, false);
				}
				break;
			}
			case "HEAL": {
				if ((npc != null) && (!npc.isCastingNow())) {
					if ((_larva != null) && (_queen != null)) {
						if (_larva.getCurrentHp() < _larva.getMaxHp()) {
							if (npc.calculateDistance(_larva, false, false) < 2500) {
								addSkillCastDesire(npc, _larva, getRandomBoolean() ? HEAL_QUEEN_ANT_1 : HEAL_QUEEN_ANT_2, 100L);
							} else {
								addMoveToDesire(npc, LARVA_LOCATION, 0);
							}
						} else if (_queen.getCurrentHp() < _queen.getMaxHp()) {
							addSkillCastDesire(npc, _queen, HEAL_QUEEN_ANT_1, 1000000L);
						}
					}
					if ((!npc.isMoving()) && (npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ATTACK)) {
						((L2Attackable) npc).clearAggroList();
						npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
					}
				}
				break;
			}
			case "ACTION": {
				if ((getRandom(100) < 30) && (npc != null) && (!npc.isInCombat())) {
					if (getRandom(100) < 50) {
						npc.broadcastSocialAction(3);
					} else {
						npc.broadcastSocialAction(4);
					}
				}
				break;
			}
			case "QUEEN_UNLOCK": {
				L2GrandBossInstance queen = (L2GrandBossInstance) addSpawn(QUEEN, QUEEN_X, QUEEN_Y, QUEEN_Z, 0, false, 0);
				GrandBossManager.getInstance().setBossStatus(QUEEN, ALIVE);
				spawnBoss(queen);
				break;
			}
			case "RAID_CURSE": {
				if ((player != null) && (!_zone.isInsideZone(player))) {
					cancelQuestTimers("RAID_CURSE");
				} else if ((player != null) && (!player.isDead()) && (_zone.isInsideZone(player)) && (_queen != null) && (!_queen.isDead())) {
					if ((player.getLevel() - _queen.getLevel()) > 8) {
						if (player.isMageClass()) {
							if (!player.isAffectedBySkill(RAID_CURSE.getSkillId())) {
								_queen.broadcastPacket(new MagicSkillUse(_queen, player, RAID_CURSE.getSkillId(), 1, 300, 0));
								RAID_CURSE.getSkill().applyEffects(_queen, player);
							}
						} else {
							if (!player.isAffectedBySkill(RAID_CURSE_2.getSkillId())) {
								_queen.broadcastPacket(new MagicSkillUse(_queen, player, RAID_CURSE_2.getSkillId(), 1, 300, 0));
								RAID_CURSE_2.getSkill().applyEffects(_queen, player);
							}
						}
					}
				}
				break;
			}
			case "RESPAWN_QUEEN": {
				if (GrandBossManager.getInstance().getBossStatus(QUEEN) == DEAD) {
					GrandBossManager.getInstance().getStatsSet(QUEEN).set("respawn_time", 0);
					cancelQuestTimers("QUEEN_UNLOCK");
					notifyEvent("QUEEN_UNLOCK", null, null);
				} else {
					player.sendMessage(getClass().getSimpleName() + ": You cant respawn Queen Ant while Queen Ant is alive!");
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon, Skill skill) {
		if ((_zone.isInsideZone(npc)) && (npc != null) && (_queen != null) && (!npc.isCastingNow()) && (!npc.isCoreAIDisabled())) {
			switch (npc.getId()) {
				case QUEEN: {
					if ((attacker.getMountType() == MountType.STRIDER) && !attacker.isAffectedBySkill(ANTI_STRIDER.getSkillId())) {
						if (npc.checkDoCastConditions(ANTI_STRIDER.getSkill())) {
							addSkillCastDesire(npc, attacker, ANTI_STRIDER, 1000000L);
						}
					} else if ((skill != null) && (skill.getAttributeType() == AttributeType.FIRE) && (getRandom(100) < 70)) {
						addSkillCastDesire(npc, attacker, DECREASE_SPEED_1, 1000000L);
					} else if ((npc.calculateDistance(attacker, false, false) > 500) && (getRandom(100) < 10)) {
						addSkillCastDesire(npc, attacker, DECREASE_SPEED_2, 1000000L);
					} else if ((npc.calculateDistance(attacker, false, false) > 150) && (getRandom(100) < 10)) {
						if ((npc.calculateDistance(attacker, false, false) < 500) && (getRandom(100) < 80)) {
							addSkillCastDesire(npc, attacker, DECREASE_SPEED_1, 1000000L);
						} else {
							addSkillCastDesire(npc, attacker, DECREASE_SPEED_2, 1000000L);
						}
					} else if ((npc.calculateDistance(attacker, false, false) < 250) && (getRandom(100) < 5)) {
						addSkillCastDesire(npc, attacker, QUEEN_ANT_BRANDISH, 1000000L);
					}
					break;
				}
				case NURSE:
				case ROYAL: {
					if ((_queen.calculateDistance(attacker, false, false) > 500) && (getRandom(100) < 5)) {
						addSkillCastDesire(_queen, attacker, DECREASE_SPEED_2, 1000000L);
					} else if ((_queen.calculateDistance(attacker, false, false) > 150) && (getRandom(100) < 5)) {
						if (getRandom(100) < 80) {
							addSkillCastDesire(_queen, attacker, DECREASE_SPEED_1, 1000000L);
						} else {
							addSkillCastDesire(_queen, attacker, DECREASE_SPEED_2, 1000000L);
						}
					} else if ((_queen.calculateDistance(attacker, false, false) < 250) && (getRandom(100) < 2)) {
						addSkillCastDesire(_queen, attacker, QUEEN_ANT_BRANDISH, 1000000L);
					}
					break;
				}
				case GUARD: {
					if (attacker.isPlayer()) {
						if ((_queen.calculateDistance(attacker, false, false) > 500) && (getRandom(100) < 3)) {
							addSkillCastDesire(_queen, attacker, DECREASE_SPEED_2, 1000000L);
						} else if ((_queen.calculateDistance(attacker, false, false) > 150) && (getRandom(100) < 3)) {
							if (getRandom(100) < 80) {
								addSkillCastDesire(_queen, attacker, DECREASE_SPEED_1, 1000000L);
							} else {
								addSkillCastDesire(_queen, attacker, DECREASE_SPEED_2, 1000000L);
							}
						} else if ((_queen.calculateDistance(attacker, false, false) < 250) && (getRandom(100) < 2)) {
							addSkillCastDesire(_queen, attacker, QUEEN_ANT_BRANDISH, 1000000L);
						}
					}
					break;
				}
			}
		}
		if ((!npc.getVariables().getBoolean(ATTACK_FLAG, false)) && (_queen != null) && !npc.isCoreAIDisabled()) {
			if (npc.calculateDistance(attacker, false, false) > 150) {
				_queen.setIsImmobilized(true);
			}
			npc.getVariables().set(ATTACK_FLAG, true);
			startQuestTimer("CORE_MOVEMENT", 5000, npc, null);
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance player, Skill skill, List<L2Object> targets, boolean isSummon) {
		if ((skill.getEffectPoint() > 0) && (getRandom(100) < 15)) {
			if ((_zone.isInsideZone(npc)) && (npc != null) && (!npc.isCastingNow()) && (!npc.isCoreAIDisabled())) {
				addSkillCastDesire(npc, player, DECREASE_SPEED_1, 1000000L);
			}
		}
		return super.onSkillSee(npc, player, skill, targets, isSummon);
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		final L2MonsterInstance mob = (L2MonsterInstance) npc;
		switch (npc.getId()) {
			case QUEEN: {
				startQuestTimer("ACTION", 10000, npc, null, true);
				break;
			}
			case LARVA: {
				mob.setIsMortal(false);
				mob.setIsRaidMinion(true);
				mob.setIsImmobilized(true);
				break;
			}
			case NURSE: {
				_nurses.add(mob);
				mob.disableCoreAI(true);
				mob.setIsRaidMinion(true);
				startQuestTimer("HEAL", 2000, mob, null, true);
				break;
			}
			case ROYAL:
			case GUARD: {
				mob.setIsRaidMinion(true);
				break;
			}
		}
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onEnterZone(L2Character character, L2ZoneType zone) {
		final L2PcInstance player = character.getActingPlayer();
		notifyEvent("RAID_CURSE", _queen, player);
		startQuestTimer("RAID_CURSE", 3000, _queen, player, true);
		return super.onEnterZone(character, zone);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		if (npc.getId() == QUEEN) {
			cancelQuestTimers("HEAL");
			cancelQuestTimers("ACTION");
			npc.broadcastPacket(Music.BS02_D_10000.getPacket());
			GrandBossManager.getInstance().setBossStatus(QUEEN, DEAD);
			final long respawnTime = (grandBoss().getIntervalOfQueenAntSpawn() + getRandom(-grandBoss().getRandomOfQueenAntSpawn(), grandBoss().getRandomOfQueenAntSpawn())) * 3600000;
			GrandBossManager.getInstance().getStatsSet(QUEEN).set("respawn_time", (System.currentTimeMillis() + respawnTime));
			startQuestTimer("QUEEN_UNLOCK", respawnTime, null, null);
			_nurses.clear();
			_queen = null;
			if (_larva != null) {
				_larva.deleteMe();
				_larva = null;
			}
		} else if ((_queen != null) && !_queen.isAlikeDead()) {
			if (npc.getId() == ROYAL) {
				L2MonsterInstance mob = (L2MonsterInstance) npc;
				if (mob.getLeader() != null) {
					mob.getLeader().getMinionList().onMinionDie(mob, (280 + getRandom(40)) * 1000);
				}
			} else if (npc.getId() == NURSE) {
				L2MonsterInstance mob = (L2MonsterInstance) npc;
				_nurses.remove(mob);
				if (mob.getLeader() != null) {
					mob.getLeader().getMinionList().onMinionDie(mob, 10000);
				}
			}
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onExitZone(L2Character character, L2ZoneType zone) {
		if ((character.isAttackable()) && (character.isRaid()) && (!_zone.isInsideZone(_queen)) && (_queen != null)) {
			_queen.disableCoreAI(true);
			_queen.setIsImmobilized(false);
			startQuestTimer("CHECK_ZONE", 1000, _queen, null, true);
		}
		return super.onExitZone(character, zone);
	}
	
	@Override
	public void onMoveFinished(L2Npc npc) {
		startQuestTimer("CORE_AI", 100, npc, null);
	}
	
	private void spawnBoss(L2GrandBossInstance npc) {
		if (getRandom(100) < 33) {
			_zone.movePlayersTo(TELE_LOCATION_1);
		} else if (getRandom(100) < 50) {
			_zone.movePlayersTo(TELE_LOCATION_2);
		} else {
			_zone.movePlayersTo(TELE_LOCATION_3);
		}
		GrandBossManager.getInstance().addBoss(npc);
		npc.broadcastPacket(Music.BS01_A_10000.getPacket());
		_queen = npc;
		_larva = (L2MonsterInstance) addSpawn(LARVA, LARVA_LOCATION, false, 0);
	}
}
