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
package com.l2jserver.datapack.quests.Q00629_CleanUpTheSwampOfScreams;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Clean Up The Swamp Of Screams (629)
 * @author netvirus
 */
public final class Q00629_CleanUpTheSwampOfScreams extends Quest {
	// NPC
	private static final int PIERCE = 31553;
	// Items
	private static final int TALON_OF_STAKATO = 7250;
	private static final int GOLDEN_RAM_COIN = 7251;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21508, TALON_OF_STAKATO, 59.9) // splinter_stakato
			.addSingleDrop(21509, TALON_OF_STAKATO, 52.4) // splinter_stakato_worker
			.addSingleDrop(21510, TALON_OF_STAKATO, 64.0) // splinter_stakato_soldier
			.addSingleDrop(21511, TALON_OF_STAKATO, 83.0) // splinter_stakato_drone
			.addSingleDrop(21512, TALON_OF_STAKATO, 97.0) // splinter_stakato_drone_a
			.addSingleDrop(21513, TALON_OF_STAKATO, 68.2) // needle_stakato
			.addSingleDrop(21514, TALON_OF_STAKATO, 59.5) // needle_stakato_worker
			.addSingleDrop(21515, TALON_OF_STAKATO, 72.7) // needle_stakato_soldier
			.addSingleDrop(21516, TALON_OF_STAKATO, 87.9) // needle_stakato_drone
			.addSingleDrop(21517, TALON_OF_STAKATO, 99.9) // needle_stakato_drone_a
			.build();
	// Misc
	private static final int REQUIRED_TALON_COUNT = 100;
	private static final int MIN_LVL = 66;
	
	public Q00629_CleanUpTheSwampOfScreams() {
		super(629, Q00629_CleanUpTheSwampOfScreams.class.getSimpleName(), "Clean Up The Swamp Of Screams");
		addStartNpc(PIERCE);
		addTalkId(PIERCE);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(TALON_OF_STAKATO, GOLDEN_RAM_COIN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs == null) {
			return htmltext;
		}
		
		switch (event) {
			case "31553-03.htm": {
				if (qs.isCreated()) {
					qs.startQuest();
					htmltext = event;
				}
				break;
			}
			case "31553-04.html":
			case "31553-06.html": {
				if (qs.isStarted()) {
					htmltext = event;
				}
				break;
			}
			case "31553-07.html": {
				if (qs.isStarted() && (getQuestItemsCount(player, TALON_OF_STAKATO) >= REQUIRED_TALON_COUNT)) {
					rewardItems(player, GOLDEN_RAM_COIN, 20);
					takeItems(player, TALON_OF_STAKATO, 100);
					htmltext = event;
				} else {
					htmltext = "31553-08.html";
				}
				break;
			}
			case "31553-09.html": {
				if (qs.isStarted()) {
					qs.exitQuest(true, true);
					htmltext = event;
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, -1, 2, npc);
		if (qs != null) {
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LVL) ? "31553-01.htm" : "31553-02.htm");
		} else if (qs.isStarted()) {
			htmltext = ((getQuestItemsCount(player, TALON_OF_STAKATO) >= REQUIRED_TALON_COUNT) ? "31553-04.html" : "31553-05.html");
		}
		return htmltext;
	}
}
