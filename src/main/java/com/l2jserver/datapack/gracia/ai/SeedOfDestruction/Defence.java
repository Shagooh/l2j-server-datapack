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
package com.l2jserver.datapack.gracia.ai.SeedOfDestruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.instancemanager.GraciaSeedsManager;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.instancemanager.WalkingManager;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.NpcSay;
import com.l2jserver.gameserver.util.Util;

/**
 * Gracia - Seed of Destruction Defence Stage
 * @author Lomka
 */
public class Defence extends AbstractNpcAI {
    // Defence Invasion Cycles
    // 1 - Regular
    // 2 - Surprise
    // 3 - Surprise Final
    
    // Script values (Tiat, Mobs)
    // 0 - moving to throne, 1 - finished moving to throne
    private List<L2Npc> spawnList = new ArrayList<>();
    private Map<String, L2Npc> spawnerPortalListFortress = new HashMap<>();
    private Map<String, L2Npc> spawnerPortalListThrone = new HashMap<>();
    private Set<L2MonsterInstance> portalMobSpawnList = ConcurrentHashMap.newKeySet();
    private L2MonsterInstance tiatRet = null;
    private L2Npc leftDevice = null;
    private L2Npc rightDevice = null;
    private int cycle = -1;
    
    public static String ENERGY_SEEDS = "EnergySeeds";
    
    private static final int MAX_NPC = 100;
    private static final int MAX_NPC_FINAL = 300;
    private static final int REGULAR_BATTLE_TIME = 2800 * 1000; // ~ 47 minutes
    private static final int FINAL_BATTLE_TIME = 86400 * 1000; // 24 hours
    private static final int SPAWNER_PORTAL = 18702;
    private static final int PROTECTOR_CRYSTAL = 18775;
    private static final int TELEPORT_DEVICE = 32602;
    private static final int TIAT_RETURN = 29175;
    private static final int TIAT_FIGHTER = 29162;
    private static final int ALLENOS = 32526;
    private static final int TIAT_WEAPON = 13842;
    
    private static final Skill TIAT_TRANSFORM_SKILL = new SkillHolder(5974, 4).getSkill();
    private static final Skill TIAT_THUNDER_STORM_SKILL = new SkillHolder(5844, 4).getSkill();
    
    private static final String INV_ROUTE_LEFT = "invasion_left";
    private static final String INV_ROUTE_CENTER = "invasion_center";
    private static final String INV_ROUTE_RIGHT = "invasion_right";
    private static final String INV_ROUTE_LEFT_SURPRISE = "invasion_left_surprise";
    private static final String INV_ROUTE_CENTER_SURPRISE = "invasion_center_surprise";
    private static final String INV_ROUTE_RIGHT_SURPRISE = "invasion_right_surprise";
    
    private static final int[] POSSIBLE_MOB_FROM_PORTAL_LIST = {
        22569,
        22570,
        22571,
        22572,
        22573,
        22574,
        22575,
        22576,
        22577,
        22578,
        22579,
        22581,
    };
    // Spawner portals
    private static final Map<String, Location> SPAWNER_PORTAL_SPAWNS_FORTRESS = new HashMap<>();
    {
        SPAWNER_PORTAL_SPAWNS_FORTRESS.put(INV_ROUTE_LEFT, new Location(-252944, 218224, -12320, 0));
        SPAWNER_PORTAL_SPAWNS_FORTRESS.put(INV_ROUTE_CENTER, new Location(-251439, 218093, -12363, 0));
        SPAWNER_PORTAL_SPAWNS_FORTRESS.put(INV_ROUTE_RIGHT, new Location(-249936, 218224, -12320, 0));
    }
    private static final Map<String, Location> SPAWNER_PORTAL_SPAWNS_THRONE = new HashMap<>();
    {
        SPAWNER_PORTAL_SPAWNS_THRONE.put(INV_ROUTE_LEFT_SURPRISE, new Location(-251736, 210131, -11995, 0));
        SPAWNER_PORTAL_SPAWNS_THRONE.put(INV_ROUTE_CENTER_SURPRISE, new Location(-250402, 210131, -11952, 0));
        SPAWNER_PORTAL_SPAWNS_THRONE.put(INV_ROUTE_RIGHT_SURPRISE, new Location(-249012, 210131, -11995, 0));
    }
    private static final Location PROTECTOR_CRYSTAL_SPAWN_LEFT = new Location(-252022, 206875, -11995, 0);
    private static final Location PROTECTOR_CRYSTAL_SPAWN_RIGHT = new Location(-248782, 206875, -11995, 0);
    // Used in every cycle
    private static final Location TELEPORT_DEVICE_SPAWN_THRONE = new Location(-249977, 207047, -11968, 0);
    // ---------------------
    // Used only in cycle 1
    private static final Location TELEPORT_DEVICE_SPAWN_FORTRESS = new Location(-251489, 216737, -12248, 0);
    // ----------------------
    private static final Location TIAT_RETURN_SPAWN = new Location(-251439, 218093, -12363, 0);
    private static final Location TIAT_RETURN_FINAL_POINT = new Location(-250401, 207591, -11952, 16384);
    private static final Location SOD_EXIT_POINT = new Location(-248717, 250260, 4337);
    private static final int SOD_ZONE = 60009;
    
    public Defence() {
        super(Defence.class.getSimpleName(), "gracia/AI");
        addKillId(TIAT_RETURN, SPAWNER_PORTAL);
        addAttackId(TIAT_RETURN);
        addRouteFinishedId(TIAT_RETURN);
        addRouteFinishedId(POSSIBLE_MOB_FROM_PORTAL_LIST);
        addAggroRangeEnterId(TIAT_RETURN, TIAT_FIGHTER);
        addAggroRangeEnterId(POSSIBLE_MOB_FROM_PORTAL_LIST);
        startQuestTimer("start", 5000, null, null);
    }
    
    protected void setCycle(int cycleId) {
        if (cycleId > 0 && cycleId <= 3) {
            cycle = cycleId;
        }
    }
    
    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        if (event != "start" && event != "check_for_fail" && event != "defence_successful" //
            && ((leftDevice != null && rightDevice != null && leftDevice.isDead() && rightDevice.isDead()) //
                || (tiatRet != null && cycle == 1 && tiatRet.isDead()))) {
            return "";
        }
        switch (event) {
            case "start": {
                final int currentSoDState = GraciaSeedsManager.getInstance().getSoDState();
                if (currentSoDState == 3) {
                    initCycle(2);
                } else if (currentSoDState == 4) {
                    initCycle(3);
                } else if (currentSoDState == 5) {
                    initCycle(1);
                }
                break;
            }
            case "stop": {
                setCycle(-1);
                cancelQuestTimer("defence_successful", null, null);
                cleanUpTimers();
                cleanUpSpawns();
                break;
            }
            case "tiat_transform": {
                tiatRet.setIsImmobilized(false);
                tiatRet.disableCoreAI(false);
                final Skill tiatTransformSkill = TIAT_TRANSFORM_SKILL;
                tiatRet.doCast(tiatTransformSkill);
                break;
            }
            case "tiat_cast_thunder_storm": {
                if (tiatRet.getCurrentMp() > TIAT_THUNDER_STORM_SKILL.getMpConsume2() // Enough MP
                    && tiatRet.getCurrentHp() > TIAT_THUNDER_STORM_SKILL.getHpConsume() // Enough HP
                    && npc.getSkillRemainingReuseTime(TIAT_THUNDER_STORM_SKILL.getReuseHashCode()) <= 0) // Skill not on cd
                {
                    final int msgRnd = getRandom(3);
                    for (L2PcInstance ch : ZoneManager.getInstance().getZoneById(SOD_ZONE).getPlayersInside()) {
                        if (ch != null) {
                            if (msgRnd == 0) {
                                showOnScreenMsg(ch, NpcStringId.POOR_CREATURES_FEEL_THE_POWER_OF_DARKNESS, 2, 5000);
                            } else if (msgRnd == 1) {
                                showOnScreenMsg(ch, NpcStringId.WHOAAAAAA, 2, 5000);
                            } else {
                                showOnScreenMsg(ch, NpcStringId.YOULL_REGRET_CHALLENGING_ME, 2, 5000);
                            }
                        }
                    }
                    tiatRet.doCast(TIAT_THUNDER_STORM_SKILL);
                }
                break;
            }
            case "walk_tiat_to_throne": {
                WalkingManager.getInstance().startMoving(tiatRet, INV_ROUTE_CENTER);
                startQuestTimer("check_walk_tiat_to_throne", 3000, tiatRet, null);
                break;
            }
            case "check_walk_tiat_to_throne": {
                if (tiatRet.getScriptValue() == 0) {
                    final CtrlIntention tiatIntention = tiatRet.getAI().getIntention();
                    if (tiatIntention == CtrlIntention.AI_INTENTION_ACTIVE || tiatIntention == CtrlIntention.AI_INTENTION_IDLE) {
                        WalkingManager.getInstance().resumeMoving(tiatRet);
                    }
                    startQuestTimer("check_walk_tiat_to_throne", 3000, tiatRet, null);
                }
                break;
            }
            case "tiat_move_to_throne_point_desire": {
                final CtrlIntention tiatIntention = tiatRet.getAI().getIntention();
                if (tiatIntention == CtrlIntention.AI_INTENTION_ACTIVE || tiatIntention == CtrlIntention.AI_INTENTION_IDLE) {
                    addMoveToDesire(tiatRet, TIAT_RETURN_FINAL_POINT, 0);
                }
                startQuestTimer("tiat_move_to_throne_point_desire", 10000, tiatRet, null);
                break;
            }
            case "continue_route": {
                Iterator<L2MonsterInstance> it = portalMobSpawnList.iterator();
                while (it.hasNext()) {
                    L2MonsterInstance mob = it.next();
                    if (mob != null && mob.getScriptValue() == 0) {
                        final CtrlIntention mobIntention = mob.getAI().getIntention();
                        boolean shouldContinue = false;
                        if (mobIntention == CtrlIntention.AI_INTENTION_ACTIVE || mobIntention == CtrlIntention.AI_INTENTION_IDLE) {
                            shouldContinue = true;
                        } else if (mob.getMostHated() != null && !Util.checkIfInRange(mob.getAggroRange(), mob.getMostHated(), mob, false)) {
                            shouldContinue = true;
                        }
                        if (shouldContinue) {
                            mob.clearAggroList();
                            WalkingManager.getInstance().resumeMoving(mob);
                        }
                    }
                }
                startQuestTimer("continue_route", 4000, null, null);
                break;
            }
            case "do_spawn_cycle": {
                spawnInvasion(cycle);
                startQuestTimer("do_spawn_cycle", 10000, null, null);
                break;
            }
            case "start_moving_mob": {
                String mobRoute = npc.getVariables().getString("route_name");
                if (mobRoute != null) {
                    WalkingManager.getInstance().startMoving(npc, mobRoute);
                }
                break;
            }
            case "crystal_aggro_left": {
                if (leftDevice.isDead()) {
                    break;
                }
                doCrystalAggro(leftDevice, rightDevice);
                startQuestTimer("crystal_aggro_left", 2500 + (leftDevice.getScriptValue() != 1 ? -500 : 500), leftDevice, null);
                if (leftDevice.getScriptValue() != 1) {
                    leftDevice.setScriptValue(1);
                } else {
                    leftDevice.setScriptValue(0);
                }
                break;
            }
            case "crystal_aggro_right": {
                if (rightDevice.isDead()) {
                    break;
                }
                doCrystalAggro(rightDevice, leftDevice);
                startQuestTimer("crystal_aggro_right", 2500 + (rightDevice.getScriptValue() != 1 ? 500 : -500), null, null);
                if (rightDevice.getScriptValue() != 1) {
                    rightDevice.setScriptValue(1);
                } else {
                    rightDevice.setScriptValue(0);
                }
                break;
            }
            case "check_for_fail": {
                if (leftDevice.isDead() && rightDevice.isDead()) {
                    defenceFailed();
                } else {
                    startQuestTimer("check_for_fail", 2000, null, null);
                }
                break;
            }
            case "defence_successful": {
                defenceSuccessful();
                break;
            }
        }
        return "";
        
    }
    
    @Override
    public void onRouteFinished(L2Npc npc) {
        if (npc == tiatRet) {
            WalkingManager.getInstance().cancelMoving(npc);
            addMoveFinishedId(TIAT_RETURN);
            tiatRet.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, TIAT_RETURN_FINAL_POINT);
            cancelQuestTimer("check_walk_tiat_to_throne", npc, null);
            startQuestTimer("tiat_move_to_throne_point_desire", 10000, tiatRet, null);
            tiatRet.setScriptValue(1);
        } else {
            WalkingManager.getInstance().stopMoving(npc, true, true);
        }
    }
    
    @Override
    public void onMoveFinished(L2Npc npc) {
        if (npc == tiatRet) {
            tiatRet.removeListenerIf(EventType.ON_NPC_MOVE_FINISHED, listener -> listener.getOwner() == this);
        }
    }
    
    @Override
    public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isSummon) {
        npc.setRunning();
        addAttackDesire(npc, player);
        return super.onAggroRangeEnter(npc, player, isSummon);
    }
    
    @Override
    public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
        if (npc == tiatRet) {
            cancelQuestTimer("defence_successful", null, null);
            startQuestTimer("defence_successful", 2000, null, null);
        } else if (npc.getId() == SPAWNER_PORTAL) {
            int killedPortals = 0;
            if (cycle == 1) {
                if (!spawnerPortalListFortress.isEmpty()) {
                    for (Map.Entry<String, L2Npc> entry : spawnerPortalListFortress.entrySet()) {
                        if (entry.getValue().isDead()) {
                            killedPortals += 1;
                        }
                    }
                }
            } else {
                if (!spawnerPortalListThrone.isEmpty()) {
                    for (Map.Entry<String, L2Npc> entry : spawnerPortalListThrone.entrySet()) {
                        if (entry.getValue().isDead()) {
                            killedPortals += 1;
                        }
                    }
                }
            }
            if (killedPortals > 2) {
                cancelQuestTimer("defence_successful", null, null);
                startQuestTimer("defence_successful", 2000, null, null);
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
    
    @Override
    public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon) {
        if (npc == tiatRet) {
            if (getQuestTimer("tiat_cast_thunder_storm", npc, null) == null) {
                startQuestTimer("tiat_cast_thunder_storm", (getRandom(60) + 60) * 1000, npc, null);
            }
        }
        return super.onAttack(npc, attacker, damage, isSummon);
    }
    
    public void initCycle(int cycleToStart) {
        ////////////////////////////////////////////////////////////////////////////////
        // Get cycle duration.
        // Has low chance of skipping 3rd state and jumping straight to 4th state.
        // cycle 1 (stage 5) = 60 min defence duration.
        // cycle 2 (stage 3) = random 10, 20 or 30 min defence duration.
        // cycle 3 (stage 4) = 30 min defence duration.
        ////////////////////////////////////////////////////////////////////////////////
        setCycle(cycleToStart);
        int timeToDefend = 60 * 60 * 1000;
        if (cycle == 2) {
            int randNum = getRandom(100);
            if (randNum < 20) {
                timeToDefend = 10 * 60 * 1000;
            } else if (randNum < 70) {
                timeToDefend = 20 * 60 * 1000;
            } else {
                randNum = getRandom(100);
                if (randNum < 80) {
                    timeToDefend = 30 * 60 * 1000;
                } else {
                    GraciaSeedsManager.getInstance().setSoDState(4, false, false);
                    return;
                }
            }
        } else if (cycle == 3) {
            timeToDefend = 30 * 60 * 1000;
        }
        ////////////////////////////////////////////////
        // Cancel existing timers & clear spawns from other stages
        cleanUpTimers();
        cleanUpSpawns();
        ////////////////////////////////////////////////
        QuestManager.getInstance().getQuest(ENERGY_SEEDS).notifyEvent("SoDDefenceStarted", null, null);
        if (cycle == 1) {
            // Spawn Tiat and shout
            tiatRet = (L2MonsterInstance) addSpawn(TIAT_RETURN, TIAT_RETURN_SPAWN, false, 0, false);
            tiatRet.setRHandId(TIAT_WEAPON);
            for (int i = 0; i < 4; i++) {
                addMinion(tiatRet, TIAT_FIGHTER);
            }
            shoutMessageInSoD(tiatRet, NpcStringId.OH_FOLLOWERS_OF_THE_DRAGON_OF_DARKNESS_FIGHT_BY_MY_SIDE);
            tiatRet.setScriptValue(0);
            tiatRet.disableCoreAI(true);
            tiatRet.setIsImmobilized(true);
            spawnList.add(tiatRet);
            // Spawn portals in forttess
            SPAWNER_PORTAL_SPAWNS_FORTRESS.entrySet().forEach(entry -> {
                final L2Attackable spawn = (L2Attackable) addSpawn(SPAWNER_PORTAL, entry.getValue(), false, 0, true);
                spawn.disableCoreAI(true);
                spawn.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                spawn.setIsImmobilized(true);
                spawnerPortalListFortress.put(entry.getKey(), spawn);
            });
            // Spawn teleporter in fortress
            final L2Npc tpFort = addSpawn(TELEPORT_DEVICE, TELEPORT_DEVICE_SPAWN_FORTRESS, false, 0, false);
            spawnList.add(tpFort);
        } else {
            // Spawn portals in throne
            SPAWNER_PORTAL_SPAWNS_THRONE.entrySet().forEach(entry -> {
                final L2Attackable spawn = (L2Attackable) addSpawn(SPAWNER_PORTAL, entry.getValue(), false, 0, true);
                spawn.disableCoreAI(true);
                spawn.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
                spawn.setIsImmobilized(true);
                spawnerPortalListThrone.put(entry.getKey(), spawn);
            });
        }
        // Spawn teleporter in throne (all stages)
        final L2Npc tpThrone = addSpawn(TELEPORT_DEVICE, TELEPORT_DEVICE_SPAWN_THRONE, false, 0, false);
        spawnList.add(tpThrone);
        // Left device spawn
        leftDevice = addSpawn(PROTECTOR_CRYSTAL, PROTECTOR_CRYSTAL_SPAWN_LEFT, false, 0, false);
        leftDevice.disableCoreAI(true);
        leftDevice.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
        leftDevice.setIsImmobilized(true);
        leftDevice.setTalking(false);
        leftDevice.setScriptValue(0);
        shoutMessageInSoD(leftDevice, NpcStringId.THE_DRAGON_RACE_ARE_INVADING_PREPARE_FOR_BATTLE);
        spawnList.add(leftDevice);
        // Right device spawn
        rightDevice = addSpawn(PROTECTOR_CRYSTAL, PROTECTOR_CRYSTAL_SPAWN_RIGHT, false, 0, false);
        rightDevice.disableCoreAI(true);
        rightDevice.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
        rightDevice.setIsImmobilized(true);
        rightDevice.setTalking(false);
        rightDevice.setScriptValue(0);
        shoutMessageInSoD(rightDevice, NpcStringId.THE_DRAGON_RACE_ARE_INVADING_PREPARE_FOR_BATTLE);
        spawnList.add(rightDevice);
        // Statrt Quest Timers
        if (cycle == 1) {
            startQuestTimer("tiat_transform", 1000, tiatRet, null);
            startQuestTimer("walk_tiat_to_throne", 6000, tiatRet, null);
        }
        startQuestTimer("do_spawn_cycle", 10000, null, null);
        startQuestTimer("continue_route", 15000, null, null);
        startQuestTimer("crystal_aggro_left", 15000, null, null);
        startQuestTimer("crystal_aggro_right", 15000, null, null);
        startQuestTimer("check_for_fail", 2000, null, null);
        // After certain time (timeToDefend), if crystals are still alive, defence is successful.
        startQuestTimer("defence_successful", timeToDefend, null, null);
    }
    
    public void spawnInvasion(int cycle) {
        Map<String, Location> currentPortalSpawns = cycle == 1 ? SPAWNER_PORTAL_SPAWNS_FORTRESS : SPAWNER_PORTAL_SPAWNS_THRONE;
        Map<String, L2Npc> currentPortals = cycle == 1 ? spawnerPortalListFortress : spawnerPortalListThrone;
        if (((cycle == 1 || cycle == 2) && portalMobSpawnList.size() > MAX_NPC) || (cycle == 3 && portalMobSpawnList.size() > MAX_NPC_FINAL)) {
            return;
        }
        currentPortalSpawns.entrySet().forEach(entry -> {
            final String walkingRoute = entry.getKey();
            final Location spawnLocaction = entry.getValue();
            // Check if the portal is dead or not (mobs should not spawn when the portal is dead).
            if (currentPortals.get(walkingRoute).isDead()) {
                return;
            }
            // Spawn random monster from 12 available monsters.
            int randNpcId = POSSIBLE_MOB_FROM_PORTAL_LIST[getRandom(12)];
            final L2MonsterInstance spawn = (L2MonsterInstance) addSpawn(randNpcId, spawnLocaction, false, 0, false);
            spawn.setCanReturnToSpawnPoint(false);
            spawn.setScriptValue(0);
            spawn.getVariables().set("route_name", walkingRoute);
            portalMobSpawnList.add(spawn);
            // Make mob walk on a specific route 2 seconds after spawning.
            startQuestTimer("start_moving_mob", 2000, spawn, null);
        });
        
    }
    
    private void doCrystalAggro(L2Npc currentDevice, L2Npc oppositeDevice) {
        final Location currentDeviceSpawnLocation = new Location(currentDevice.getX(), currentDevice.getY() + 125, currentDevice.getZ());
        Iterator<L2MonsterInstance> it = portalMobSpawnList.iterator();
        while (it.hasNext()) {
            L2MonsterInstance mob = it.next();
            if (Util.checkIfInRange(oppositeDevice.isDead() ? 4000 : 2000, mob, currentDevice, true)) {
                if (mob.getScriptValue() == 0) {
                    mob.setScriptValue(1);
                    if (cycle == 1) {
                        WalkingManager.getInstance().stopMoving(mob, true, true);
                    } else {
                        mob.stopMove(null);
                    }
                }
                if (mob.isInsideRadius(currentDeviceSpawnLocation, 800, false, false)) {
                    {
                        mob.setRunning();
                        mob.setTarget(currentDevice);
                        mob.addDamageHate(currentDevice, 0, 5000);
                        mob.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, currentDevice);
                    }
                } else {
                    if (mob.getAI().getIntention() != CtrlIntention.AI_INTENTION_ATTACK) {
                        mob.setIsRunning(true);
                        mob.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, currentDeviceSpawnLocation);
                    }
                }
            } else if (!oppositeDevice.isDead() && !Util.checkIfInRange(2000, mob, oppositeDevice, true)) {
                if (mob.getScriptValue() == 1) {
                    mob.setScriptValue(0);
                }
            }
        }
    }
    
    private void defenceFailed() {
        cancelQuestTimer("defence_successful", null, null);
        cleanUpSpawns();
        for (L2PcInstance ch : ZoneManager.getInstance().getZoneById(SOD_ZONE).getPlayersInside()) {
            if (ch != null) {
                ch.teleToLocation(SOD_EXIT_POINT);
            }
        }
        GraciaSeedsManager.getInstance().setSoDState(1, true, true);
    }
    
    private void defenceSuccessful() {
        cancelQuestTimer("defence_successful", null, null);
        cleanUpTimers();
        cleanUpSpawns();
        GraciaSeedsManager.getInstance().setSoDState(2, true, false);
        if ((System.currentTimeMillis() - GraciaSeedsManager.getInstance().getSoDLastStateChangeDate().getTimeInMillis()) < REGULAR_BATTLE_TIME) {
            ThreadPoolManager.getInstance().scheduleGeneral(() -> {
                initCycle(1);
                GraciaSeedsManager.getInstance().setSoDState(5, false, false);
            }, 60000);
        } else if ((System.currentTimeMillis() - GraciaSeedsManager.getInstance().getSoDLastStateChangeDate().getTimeInMillis()) < FINAL_BATTLE_TIME) {
            for (L2Spawn npcs : SpawnTable.getInstance().getSpawns(ALLENOS)) {
                broadcastNpcSay(npcs.getLastSpawn(), Say2.NPC_SHOUT, NpcStringId.TIATS_FOLLOWERS_ARE_COMING_TO_RETAKE_THE_SEED_OF_DESTRUCTION_GET_READY_TO_STOP_THE_ENEMIES);
            }
            ThreadPoolManager.getInstance().scheduleGeneral(() -> {
                initCycle(2);
                GraciaSeedsManager.getInstance().setSoDState(3, false, false);
            }, 3600000);
        } else {
            ThreadPoolManager.getInstance().scheduleGeneral(() -> {
                initCycle(3);
                GraciaSeedsManager.getInstance().setSoDState(4, false, false);
            }, 1000);
        }
    }
    
    public void cleanUpTimers() {
        cancelQuestTimers("start_moving_left_fortress");
        cancelQuestTimers("start_moving_right_fortress");
        cancelQuestTimers("start_moving_center_fortress");
        cancelQuestTimers("start_moving_left_throne");
        cancelQuestTimers("start_moving_right_throne");
        cancelQuestTimers("start_moving_center_throne");
        cancelQuestTimers("do_spawn_cycle");
        cancelQuestTimers("check_for_fail");
        cancelQuestTimers("crystal_aggro_right");
        cancelQuestTimers("crystal_aggro_left");
        cancelQuestTimers("tiat_move_to_throne_point_desire");
        cancelQuestTimers("check_walk_tiat_to_throne");
        cancelQuestTimers("tiat_cast_thunder_storm");
    }
    
    private void cleanUpSpawns() {
        Iterator<L2MonsterInstance> it = portalMobSpawnList.iterator();
        while (it.hasNext()) {
            L2MonsterInstance spawns = it.next();
            WalkingManager.getInstance().cancelMoving(spawns);
            spawns.deleteMe();
        }
        for (L2Npc spawns : spawnList) {
            if (spawns != null) {
                WalkingManager.getInstance().cancelMoving(spawns);
                spawns.deleteMe();
            }
        }
        if (!spawnerPortalListFortress.isEmpty()) {
            for (Map.Entry<String, L2Npc> entry : spawnerPortalListFortress.entrySet()) {
                entry.getValue().deleteMe();
            }
            spawnerPortalListFortress.clear();
        }
        if (!spawnerPortalListThrone.isEmpty()) {
            for (Map.Entry<String, L2Npc> entry : spawnerPortalListThrone.entrySet()) {
                entry.getValue().deleteMe();
            }
            spawnerPortalListThrone.clear();
        }
        portalMobSpawnList.clear();
        spawnList.clear();
        if (tiatRet != null) {
            tiatRet.deleteMe();
        }
    }
    
    // Shouts to everyone who's inside the SoD zone.
    private void shoutMessageInSoD(L2Npc npc, NpcStringId stringId) {
        for (L2PcInstance ch : ZoneManager.getInstance().getZoneById(SOD_ZONE).getPlayersInside()) {
            if (ch != null) {
                ch.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_SHOUT, npc.getTemplate().getDisplayId(), stringId));
            }
        }
    }
}
