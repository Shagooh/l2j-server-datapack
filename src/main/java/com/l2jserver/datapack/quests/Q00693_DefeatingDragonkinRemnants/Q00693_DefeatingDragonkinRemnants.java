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
package com.l2jserver.datapack.quests.Q00693_DefeatingDragonkinRemnants;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.instancemanager.GraciaSeedsManager;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.instancezone.InstanceWorld;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.SystemMessageId;

/**
 * Quest 693 - Defeating Dragonkin Remnants
 * @author Lomka
 */
public class Q00693_DefeatingDragonkinRemnants extends Quest {
    private static final int EDRIC = 32527;
    private static final int MIN_LEVEL = 75;
    private static Location ENTER_TELEPORT_LOC = new Location(-242754, 219982, -10011);
    
    public Q00693_DefeatingDragonkinRemnants() {
        super(693, Q00693_DefeatingDragonkinRemnants.class.getSimpleName(), "Defeating Dragonkin Remnants");
        addStartNpc(EDRIC);
        addFirstTalkId(EDRIC);
        addTalkId(EDRIC);
    }
    
    @Override
    public String onFirstTalk(L2Npc npc, L2PcInstance player) {
        if (GraciaSeedsManager.getInstance().getSoDState() == 2) {
            return "32527-00.html";
        }
        return "32527-00a.html";
    }
    
    @Override
    public String onTalk(L2Npc npc, L2PcInstance talker) {
        QuestState st = getQuestState(talker, true);
        String htmltext = getNoQuestMsg(talker);
        if (npc.getId() == EDRIC) {
            if (talker.getLevel() < 75) {
                htmltext = "32527-lowlevel.htm";
            } else if (st.getMemoState() < 1) {
                htmltext = "32527-01.htm";
            } else if (st.isStarted() && st.getMemoState() >= 1) {
                L2Party party = talker.getParty();
                if (st.getMemoState() >= 3) {
                    if (st.isMemoState(3) && rewardPlayer(st, st.getInt("difficulty"), st.getInt("members"))) {
                        htmltext = "32527-reward.html";
                    } else {
                        htmltext = "32527-noreward.html";
                    }
                    st.unset("difficulty");
                    st.unset("members");
                    playSound(talker, Sound.ITEMSOUND_QUEST_FINISH);
                    st.exitQuest(true);
                } else if (st.isMemoState(2)) {
                    htmltext = "32527-11.html";
                } else if (party == null) {
                    htmltext = "32527-noparty.html";
                } else if (!party.getLeader().equals(talker)) {
                    htmltext = getHtm(talker.getHtmlPrefix(), "32527-noleader.html");
                    htmltext = htmltext.replace("%leader%", party.getLeader().getName());
                } else {
                    for (L2PcInstance member : party.getMembers()) {
                        QuestState state = getQuestState(member, false);
                        if (state == null || (!state.isStarted()) || (!state.isMemoState(1))) {
                            htmltext = getHtm(talker.getHtmlPrefix(), "32527-noquest.html");
                            htmltext = htmltext.replace("%member%", member.getName());
                            return htmltext;
                        }
                    }
                    htmltext = "32527-06.htm";
                }
            }
        }
        return htmltext;
    }
    
    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        final QuestState st = getQuestState(player, false);
        String htmltext = event;
        if (st == null) {
            return null;
        }
        if (event.equalsIgnoreCase("32527-05.htm")) {
            if (checkInstances(player)) {
                playSound(player, Sound.ITEMSOUND_QUEST_ACCEPT);
                if (player.getLevel() >= MIN_LEVEL) {
                    st.startQuest(false);
                    st.setMemoState(1);
                    st.playSound(Sound.ITEMSOUND_QUEST_ACCEPT);
                    htmltext = event;
                }
            } else {
                htmltext = "32527-12.html";
            }
        }
        if (event.equalsIgnoreCase("reenter")) {
            htmltext = "";
            if (st.getInt("difficulty") >= 1) {
                InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
                // player already in the instance
                if (world != null) {
                    if (world.getTemplateId() >= 123 && world.getTemplateId() <= 126) {
                        teleportPlayer(player, ENTER_TELEPORT_LOC, world.getInstanceId(), true);
                    } else {
                        player.sendPacket(SystemMessageId.YOU_HAVE_ENTERED_ANOTHER_INSTANT_ZONE_THEREFORE_YOU_CANNOT_ENTER_CORRESPONDING_DUNGEON);
                    }
                } else {
                    htmltext = "32527-noreward.html";
                    st.unset("difficulty");
                    st.unset("members");
                    playSound(player, Sound.ITEMSOUND_QUEST_FINISH);
                    st.exitQuest(true);
                }
            }
        }
        return htmltext;
    }
    
    private boolean checkInstances(L2PcInstance talker) {
        if (System.currentTimeMillis() < InstanceManager.getInstance().getInstanceTime(talker.getObjectId(), 123) //
            || System.currentTimeMillis() < InstanceManager.getInstance().getInstanceTime(talker.getObjectId(), 124) //
            || System.currentTimeMillis() < InstanceManager.getInstance().getInstanceTime(talker.getObjectId(), 125) //
            || System.currentTimeMillis() < InstanceManager.getInstance().getInstanceTime(talker.getObjectId(), 126) //
        ) {
            return false;
        }
        return true;
    }
    
    private boolean rewardPlayer(QuestState st, int difficulty, int memberCount) {
        if (getRandom(1000) < (10000 / (memberCount * 10) * (1 + difficulty * 2))) {
            if (difficulty == 4) {
                st.giveItems(14638, 1L); // Best Quality Battle Reward Chest
            } else if (difficulty == 3) {
                st.giveItems(14637, 1L); // High-Grade Battle Reward Chest
            } else if (difficulty == 2) {
                st.giveItems(14636, 1L); // Middle Quality Battle Reward Chest
            } else if (difficulty == 1) {
                st.giveItems(14635, 1L); // Low Quality Battle Reward Chest
            }
            return true;
        }
        return false;
    }
    
}
