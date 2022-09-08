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
package com.l2jserver.datapack.quests.Q00359_ForASleeplessDeadman;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * For a Sleepless Deadman (359)
 * @author Adry_85
 */
public final class Q00359_ForASleeplessDeadman extends Quest {
	// NPC
	private static final int ORVEN = 30857;
	// Item
	private static final QuestItemChanceHolder REMAINS_OF_ADEN_RESIDENTS = new QuestItemChanceHolder(5869, 60L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21006, REMAINS_OF_ADEN_RESIDENTS, 36.5) // doom_servant
			.addSingleDrop(21007, REMAINS_OF_ADEN_RESIDENTS, 39.2) // doom_guard
			.addSingleDrop(21008, REMAINS_OF_ADEN_RESIDENTS, 50.3) // doom_archer
			.build();
	// Misc
	private static final int MIN_LEVEL = 60;
	// Rewards
	private static final int[] REWARDS = new int[] {
		5494, // Sealed Dark Crystal Shield Fragment
		5495, // Sealed Shield of Nightmare Fragment
		6341, // Sealed Phoenix Earring Gemstone
		6342, // Sealed Majestic Earring Gemstone
		6343, // Sealed Phoenix Necklace Beads
		6344, // Sealed Majestic Necklace Beads
		6345, // Sealed Phoenix Ring Gemstone
		6346, // Sealed Majestic Ring Gemstone
	};

	public Q00359_ForASleeplessDeadman() {
		super(359, Q00359_ForASleeplessDeadman.class.getSimpleName(), "For a Sleepless Deadman");
		addStartNpc(ORVEN);
		addTalkId(ORVEN);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(REMAINS_OF_ADEN_RESIDENTS.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30857-02.htm":
			case "30857-03.htm":
			case "30857-04.htm": {
				htmltext = event;
				break;
			}
			case "30857-05.htm": {
				st.setMemoState(1);
				st.startQuest();
				htmltext = event;
				break;
			}
			case "30857-10.html": {
				rewardItems(player, REWARDS[getRandom(REWARDS.length)], 4);
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(killer, 1, 3, npc);
		if ((st != null) && giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true)) {
			st.setCond(2);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LEVEL) ? "30857-01.htm" : "30857-06.html");
		} else if (st.isStarted()) {
			if (st.isMemoState(1)) {
				if (!hasItemsAtLimit(player, REMAINS_OF_ADEN_RESIDENTS)) {
					htmltext = "30857-07.html";
				} else {
					takeItems(player, REMAINS_OF_ADEN_RESIDENTS.getId(), -1);
					st.setMemoState(2);
					st.setCond(3, true);
					htmltext = "30857-08.html";
				}
			} else if (st.isMemoState(2)) {
				htmltext = "30857-09.html";
			}
		}
		return htmltext;
	}
}
