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
package com.l2jserver.datapack.quests.Q00354_ConquestOfAlligatorIsland;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Conquest of Alligator Island (354)
 * @author Adry_85
 */
public final class Q00354_ConquestOfAlligatorIsland extends Quest {
	// NPC
	private static final int KLUCK = 30895;
	// Items
	private static final int ALLIGATOR_TOOTH = 5863;
	private static final int PIRATES_TREASURE_MAP = 5915;
	private static final QuestItemChanceHolder MYSTERIOUS_MAP_PIECE = new QuestItemChanceHolder(5864, 10.0);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20804, ALLIGATOR_TOOTH, 84.0) // crokian_lad
			.addSingleDrop(20805, ALLIGATOR_TOOTH, 91.0) // dailaon_lad
			.addSingleDrop(20806, ALLIGATOR_TOOTH, 88.0) // crokian_lad_warrior
			.addSingleDrop(20807, ALLIGATOR_TOOTH, 92.0) // farhite_lad
			.addSingleDrop(22208, ALLIGATOR_TOOTH, 114.0) // nos_lad
			.addSingleDrop(20991, ALLIGATOR_TOOTH, 169.0) // tribe_of_swamp
			.build();
	// Misc
	private static final int MIN_LEVEL = 38;
	
	public Q00354_ConquestOfAlligatorIsland() {
		super(354, Q00354_ConquestOfAlligatorIsland.class.getSimpleName(), "Conquest of Alligator Island");
		addStartNpc(KLUCK);
		addTalkId(KLUCK);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(ALLIGATOR_TOOTH, MYSTERIOUS_MAP_PIECE.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30895-04.html":
			case "30895-05.html":
			case "30895-09.html": {
				htmltext = event;
				break;
			}
			case "30895-02.html": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "ADENA": {
				final long count = st.getQuestItemsCount(ALLIGATOR_TOOTH);
				if (count >= 100) {
					st.giveAdena((count * 220) + 10700, true);
					st.takeItems(ALLIGATOR_TOOTH, -1);
					htmltext = "30895-06.html";
				} else if (count > 0) {
					st.giveAdena((count * 220) + 3100, true);
					st.takeItems(ALLIGATOR_TOOTH, -1);
					htmltext = "30895-07.html";
				} else {
					htmltext = "30895-08.html";
				}
				break;
			}
			case "30895-10.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
			case "REWARD": {
				final long count = st.getQuestItemsCount(MYSTERIOUS_MAP_PIECE.getId());
				if (count >= 10) {
					st.giveItems(PIRATES_TREASURE_MAP, 1);
					st.takeItems(MYSTERIOUS_MAP_PIECE.getId(), 10);
					htmltext = "30895-13.html";
				} else if (count > 0) {
					htmltext = "30895-12.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(killer, -1, 3, npc);
		if (st != null) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
			giveItemRandomly(st.getPlayer(), npc, MYSTERIOUS_MAP_PIECE, false);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LEVEL) ? "30895-01.htm" : "30895-03.html");
		} else if (st.isStarted()) {
			htmltext = (st.hasQuestItems(MYSTERIOUS_MAP_PIECE.getId()) ? "30895-11.html" : "30895-04.html");
		}
		return htmltext;
	}
}
