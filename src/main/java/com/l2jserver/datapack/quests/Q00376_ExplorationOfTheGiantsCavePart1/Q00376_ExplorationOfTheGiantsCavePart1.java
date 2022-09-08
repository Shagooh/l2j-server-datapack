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
package com.l2jserver.datapack.quests.Q00376_ExplorationOfTheGiantsCavePart1;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Exploration of the Giants' Cave Part 1 (376)<br>
 * Original Jython script by Gnacik.
 * @author nonom
 */
public class Q00376_ExplorationOfTheGiantsCavePart1 extends Quest {
	// NPC
	private static final int SOBLING = 31147;
	// Items
	private static final int ANCIENT_PARCHMENT = 14841;
	private static final int BOOK1 = 14836;
	private static final int BOOK2 = 14837;
	private static final int BOOK3 = 14838;
	private static final int BOOK4 = 14839;
	private static final int BOOK5 = 14840;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22670, ANCIENT_PARCHMENT, 31.4) // const_lord
			.addSingleDrop(22671, ANCIENT_PARCHMENT, 30.2) // const_gaurdian
			.addSingleDrop(22672, ANCIENT_PARCHMENT, 30.0) // const_seer
			.addSingleDrop(22673, ANCIENT_PARCHMENT, 25.8) // hirokai
			.addSingleDrop(22674, ANCIENT_PARCHMENT, 24.8) // imagro
			.addSingleDrop(22675, ANCIENT_PARCHMENT, 26.4) // palite
			.addSingleDrop(22676, ANCIENT_PARCHMENT, 25.8) // hamrit
			.addSingleDrop(22677, ANCIENT_PARCHMENT, 26.6) // kranout
			.build();

	public Q00376_ExplorationOfTheGiantsCavePart1() {
		super(376, Q00376_ExplorationOfTheGiantsCavePart1.class.getSimpleName(), "Exploration of the Giants' Cave - Part 1");
		addStartNpc(SOBLING);
		addTalkId(SOBLING);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(ANCIENT_PARCHMENT);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs == null) {
			return htmltext;
		}
		
		switch (event) {
			case "31147-02.htm": {
				qs.startQuest();
				htmltext = event;
				break;
			}
			case "31147-04.html":
			case "31147-cont.html": {
				htmltext = event;
				break;
			}
			case "31147-quit.html": {
				qs.exitQuest(true, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(player, -1, 3, npc);
		if (qs != null) {
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			htmltext = ((player.getLevel() >= 79) ? "31147-01.htm" : "31147-00.html");
		} else if (qs.isStarted()) {
			htmltext = (hasQuestItems(player, BOOK1, BOOK2, BOOK3, BOOK4, BOOK5) ? "31147-03.html" : "31147-02a.html");
		}
		return htmltext;
	}
}
