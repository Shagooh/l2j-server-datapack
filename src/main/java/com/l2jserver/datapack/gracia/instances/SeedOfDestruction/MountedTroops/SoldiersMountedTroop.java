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
package com.l2jserver.datapack.gracia.instances.SeedOfDestruction.MountedTroops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l2jserver.datapack.instances.AbstractInstance;
import com.l2jserver.datapack.quests.Q00693_DefeatingDragonkinRemnants.Q00693_DefeatingDragonkinRemnants;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.L2Territory;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.instancezone.InstanceWorld;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * Gracia SoD Soldiers Mounted Troop Instance
 * @author Lomka
 */
public final class SoldiersMountedTroop extends AbstractInstance {
    protected class SoldiersMountedTroopWorld extends InstanceWorld {
        protected L2Attackable doorMan = null;
        protected L2Attackable remnantMachine = null;
        protected List<L2Npc> npcList = new ArrayList<>();
    }
    
    private static final int TEMPLATE_ID = 124;
    private static final int EXIT_TIME = 5;
    private static final int MAX_PLAYERS = 9;
    private static final int MIN_LEVEL = 75;
    private static final int EDRIC = 32527;
    
    private static final int DEFEATED_TROOPS_WHITE_DRAGON_LEADER = 18785;
    private static final int DEFEATED_TROOPS_INFANTRY = 18786;
    private static final int DEFEATED_TROOPS_MAGIC_SOLDIER = 18789;
    private static final int DOORMAN = DEFEATED_TROOPS_INFANTRY;
    
    private static final int REMNANT_MACHINE = 18703;
    private static final int SEED_CONTROLLER = 32602;
    
    private static final int[] DOORS = {
        12240001,
        12240002
    };
    
    private static Location ENTER_TELEPORT_LOC = new Location(-242754, 219982, -10011);
    private static final Location DOORMAN_SPAWN_LOC = new Location(-239504, 219984, -10112, 32767);
    private static final Location REMNANT_MACHINE_LOC = new Location(-238320, 219983, -10112, 0);
    
    private static final L2Territory _hallZone1 = new L2Territory(1);
    private static final L2Territory _hallZone2 = new L2Territory(2);
    
    private static final Map<Integer, Integer> HALL_1_SPAWNS = new HashMap<>();
    {
        HALL_1_SPAWNS.put(DEFEATED_TROOPS_WHITE_DRAGON_LEADER, 4);
        HALL_1_SPAWNS.put(DEFEATED_TROOPS_INFANTRY, 3);
        HALL_1_SPAWNS.put(DEFEATED_TROOPS_MAGIC_SOLDIER, 3);
    }
    
    private static final Map<Integer, Integer> HALL_2_SPAWNS = new HashMap<>();
    {
        HALL_2_SPAWNS.put(DEFEATED_TROOPS_INFANTRY, 4);
        HALL_2_SPAWNS.put(DEFEATED_TROOPS_MAGIC_SOLDIER, 3);
    }
    
    private static final int[][] HALL_ZONE_1_COORDINATES = {
        {
            -240766,
            219168,
            -10178,
            -9978
        },
        {
            -240755,
            220795,
            -10178,
            -9978
        },
        {
            -240667,
            220889,
            -10178,
            -9978
        },
        {
            -239595,
            220886,
            -10178,
            -9978
        },
        {
            -239487,
            220765,
            -10178,
            -9978
        },
        {
            -239490,
            219170,
            -10178,
            -9978
        },
        {
            -239583,
            219059,
            -10178,
            -9978
        },
        {
            -240665,
            219065,
            -10178,
            -9978
        },
    };
    
    private static final int[][] HALL_ZONE_2_COORDINATES = {
        {
            -238353,
            219165,
            -10175,
            -9975
        },
        {
            -238340,
            220788,
            -10175,
            -9975
        },
        {
            -238431,
            220888,
            -10175,
            -9975
        },
        {
            -238933,
            220890,
            -10175,
            -9975
        },
        {
            -239027,
            220789,
            -10175,
            -9975
        },
        {
            -239034,
            219195,
            -10175,
            -9975
        },
        {
            -238924,
            219067,
            -10175,
            -9975
        },
        {
            -238445,
            219065,
            -10175,
            -9975
        },
    };
    
    public SoldiersMountedTroop() {
        super(SoldiersMountedTroop.class.getSimpleName(), "gracia/instances");
        addStartNpc(EDRIC);
        addTalkId(EDRIC);
        addKillId(DOORMAN, REMNANT_MACHINE);
        for (int[] coord : HALL_ZONE_1_COORDINATES) {
            _hallZone1.add(coord[0], coord[1], coord[2], coord[3], 0);
        }
        for (int[] coord : HALL_ZONE_2_COORDINATES) {
            _hallZone2.add(coord[0], coord[1], coord[2], coord[3], 0);
        }
    }
    
    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        switch (event) {
            case "DOORMAN_KILLED": {
                openDoor(DOORS[0], npc.getInstanceId());
                openDoor(DOORS[1], npc.getInstanceId());
                break;
            }
        }
        return "";
    }
    
    @Override
    protected boolean checkConditions(L2PcInstance player) {
        final L2Party party = player.getParty();
        
        if (party == null) {
            player.sendPacket(SystemMessageId.NOT_IN_PARTY_CANT_ENTER);
            return false;
        }
        
        if (party.getLeader() != player) {
            player.sendPacket(SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER);
            return false;
        }
        
        if (party.getMemberCount() > MAX_PLAYERS) {
            player.sendPacket(SystemMessageId.PARTY_EXCEEDED_THE_LIMIT_CANT_ENTER);
            return false;
        }
        for (L2PcInstance partyMember : party.getMembers()) {
            if (partyMember.getLevel() < MIN_LEVEL) {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_S_LEVEL_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED);
                sm.addPcName(partyMember);
                party.broadcastPacket(sm);
                return false;
            }
            if (!Util.checkIfInRange(1000, player, partyMember, true)) {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_IN_A_LOCATION_WHICH_CANNOT_BE_ENTERED_THEREFORE_IT_CANNOT_BE_PROCESSED);
                sm.addPcName(partyMember);
                party.broadcastPacket(sm);
                return false;
            }
            if (System.currentTimeMillis() < InstanceManager.getInstance().getInstanceTime(partyMember.getObjectId(), TEMPLATE_ID)) {
                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_MAY_NOT_RE_ENTER_YET);
                sm.addPcName(partyMember);
                party.broadcastPacket(sm);
                return false;
            }
            if (partyMember.isFlyingMounted()) {
                partyMember.sendPacket(SystemMessageId.YOU_CANNOT_ENTER_SEED_IN_FLYING_TRANSFORM);
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onEnterInstance(L2PcInstance player, InstanceWorld world, boolean firstEntrance) {
        if (firstEntrance) {
            final L2Party party = player.getParty();
            SoldiersMountedTroopWorld _world = (SoldiersMountedTroopWorld) world;
            _world.doorMan = (L2Attackable) addSpawn(DOORMAN, DOORMAN_SPAWN_LOC, false, 0, false, _world.getInstanceId());
            _world.remnantMachine = (L2Attackable) addSpawn(REMNANT_MACHINE, REMNANT_MACHINE_LOC, false, 0, false, _world.getInstanceId());
            _world.doorMan.setIsNoRndWalk(true);
            _world.remnantMachine.disableCoreAI(true);
            _world.remnantMachine.setIsImmobilized(true);
            // Spawn mobs in the 1st hall
            HALL_1_SPAWNS.entrySet().forEach(entry -> {
                for (int i = 0; i < entry.getValue(); i++) {
                    final Location location = _hallZone1.getRandomPoint();
                    location.setHeading(getRandom(65536));
                    L2Attackable spawn = (L2Attackable) addSpawn(entry.getKey(), location, false, 0, false, _world.getInstanceId());
                    spawn.setIsNoRndWalk(true);
                    _world.npcList.add(spawn);
                }
            });
            // Spawn mobs in the 2nd hall
            HALL_2_SPAWNS.entrySet().forEach(entry -> {
                for (int i = 0; i < entry.getValue(); i++) {
                    final Location location = _hallZone2.getRandomPoint();
                    location.setHeading(getRandom(65536));
                    L2Attackable spawn = (L2Attackable) addSpawn(entry.getKey(), location, false, 0, false, _world.getInstanceId());
                    spawn.setIsNoRndWalk(true);
                    _world.npcList.add(spawn);
                }
            });
            // Spawn temporary teleporter
            addSpawn(SEED_CONTROLLER, ENTER_TELEPORT_LOC, false, 0, false, _world.getInstanceId());
            closeDoor(DOORS[0], _world.getInstanceId());
            closeDoor(DOORS[1], _world.getInstanceId());
            
            if (party != null) {
                for (L2PcInstance players : party.getMembers()) {
                    QuestState st = players.getQuestState(Q00693_DefeatingDragonkinRemnants.class.getSimpleName());
                    if (st != null) {
                        st.set("difficulty", 2);
                        st.setMemoState(2);
                        st.set("members", party.getMemberCount());
                        _world.addAllowed(players.getObjectId());
                        teleportPlayer(players, ENTER_TELEPORT_LOC, _world.getInstanceId(), true);
                    }
                }
            }
        }
    }
    
    @Override
    public String onTalk(L2Npc npc, L2PcInstance player) {
        if (checkConditions(player)) {
            enterInstance(player, new SoldiersMountedTroopWorld(), "SoldiersMountedTroop.xml", TEMPLATE_ID);
            return "32527-entrance.html";
        }
        return "";
    }
    
    @Override
    public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
        final InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
        if (tmpworld instanceof SoldiersMountedTroopWorld) {
            final SoldiersMountedTroopWorld world = (SoldiersMountedTroopWorld) InstanceManager.getInstance().getWorld(npc.getInstanceId());
            if (npc == world.doorMan) {
                startQuestTimer("DOORMAN_KILLED", 2000, npc, null);
            }
            if (npc.getId() == REMNANT_MACHINE) {
                world.doorMan.deleteMe();
                for (L2Npc _npc : world.npcList) {
                    _npc.deleteMe();
                }
                world.npcList.clear();
                final L2Party party = killer.getParty();
                if (party != null) {
                    for (L2PcInstance players : party.getMembers()) {
                        QuestState st = players.getQuestState(Q00693_DefeatingDragonkinRemnants.class.getSimpleName());
                        if (st != null && players.getInstanceId() == world.getInstanceId()) {
                            st.setMemoState(3);
                        }
                    }
                }
                // finish Instance
                cancelQuestTimers("FAILED_IN_TIME");
                finishInstance(world, EXIT_TIME * 60000);
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
