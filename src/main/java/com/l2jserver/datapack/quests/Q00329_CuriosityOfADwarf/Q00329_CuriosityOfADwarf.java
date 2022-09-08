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
package com.l2jserver.datapack.quests.Q00329_CuriosityOfADwarf;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.util.Util;

/**
 * Curiosity Of A Dwarf (329)
 * @author ivantotov
 */
public final class Q00329_CuriosityOfADwarf extends Quest {
	// NPC
	private static final int TRADER_ROLENTO = 30437;
	// Items
	private static final int GOLEM_HEARTSTONE = 1346;
	private static final int BROKEN_HEARTSTONE = 1365;
	// Misc
	private static final int MIN_LEVEL = 33;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addGroupedDrop(20083, 54.0) // Granitic Golem
				.withDropItem(GOLEM_HEARTSTONE, 5.56)
				.withDropItem(BROKEN_HEARTSTONE, 94.44)
				.build()
			.addGroupedDrop(20085, 58.0) // Puncher
				.withDropItem(GOLEM_HEARTSTONE, 5.17)
				.withDropItem(BROKEN_HEARTSTONE, 94.83)
				.build()
			.build();
	
	public Q00329_CuriosityOfADwarf() {
		super(329, Q00329_CuriosityOfADwarf.class.getSimpleName(), "Curiosity Of A Dwarf");
		addStartNpc(TRADER_ROLENTO);
		addTalkId(TRADER_ROLENTO);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(GOLEM_HEARTSTONE, BROKEN_HEARTSTONE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		String htmltext = null;
		if (st == null) {
			return htmltext;
		}
		
		switch (event) {
			case "30437-03.htm": {
				if (st.isCreated()) {
					st.startQuest();
					htmltext = event;
				}
				break;
			}
			case "30437-06.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
			case "30437-07.html": {
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getQuestState(killer, false);
		if ((st != null) && Util.checkIfInRange(1500, npc, killer, true)) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		switch (st.getState()) {
			case State.CREATED: {
				htmltext = player.getLevel() >= MIN_LEVEL ? "30437-02.htm" : "30437-01.htm";
				break;
			}
			case State.STARTED: {
				if (hasAtLeastOneQuestItem(player, getRegisteredItemIds())) {
					final long broken = st.getQuestItemsCount(BROKEN_HEARTSTONE);
					final long golem = st.getQuestItemsCount(GOLEM_HEARTSTONE);
					st.giveAdena(((broken * 50) + (golem * 1000) + ((broken + golem) >= 10 ? 1183 : 0)), true);
					takeItems(player, -1, getRegisteredItemIds());
					htmltext = "30437-05.html";
				} else {
					htmltext = "30437-04.html";
				}
				break;
			}
		}
		return htmltext;
	}
}
