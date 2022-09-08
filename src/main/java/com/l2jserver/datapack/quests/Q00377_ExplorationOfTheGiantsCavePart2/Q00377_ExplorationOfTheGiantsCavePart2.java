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
package com.l2jserver.datapack.quests.Q00377_ExplorationOfTheGiantsCavePart2;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Exploration of the Giants' Cave Part 2 (377)<br>
 * Original Jython script by Gnacik.
 * @author nonom
 */
public class Q00377_ExplorationOfTheGiantsCavePart2 extends Quest {
	// NPC
	private static final int SOBLING = 31147;
	// Items
	private static final int TITAN_ANCIENT_BOOK = 14847;
	private static final int BOOK1 = 14842;
	private static final int BOOK2 = 14843;
	private static final int BOOK3 = 14844;
	private static final int BOOK4 = 14845;
	private static final int BOOK5 = 14846;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22660, TITAN_ANCIENT_BOOK, 236.6) // lesser_giant_re
			.addSingleDrop(22661, TITAN_ANCIENT_BOOK, 242.4) // lesser_giant_soldier_re
			.addSingleDrop(22662, TITAN_ANCIENT_BOOK, 230.4) // lesser_giant_shooter_re
			.addSingleDrop(22663, TITAN_ANCIENT_BOOK, 230.4) // lesser_giant_scout_re
			.addSingleDrop(22664, TITAN_ANCIENT_BOOK, 235.4) // lesser_giant_mage_re
			.addSingleDrop(22665, TITAN_ANCIENT_BOOK, 232.4) // lesser_giant_elder_re
			.addSingleDrop(22666, TITAN_ANCIENT_BOOK, 27.6) // barif_re
			.addSingleDrop(22667, TITAN_ANCIENT_BOOK, 28.4) // barif_pet_re
			.addSingleDrop(22668, TITAN_ANCIENT_BOOK, 24.0) // gamlin_re
			.addSingleDrop(22669, TITAN_ANCIENT_BOOK, 24.0) // leogul_re
			.build();
	
	public Q00377_ExplorationOfTheGiantsCavePart2() {
		super(377, Q00377_ExplorationOfTheGiantsCavePart2.class.getSimpleName(), "Exploration of the Giants' Cave - Part 2");
		addStartNpc(SOBLING);
		addTalkId(SOBLING);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(TITAN_ANCIENT_BOOK);
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
