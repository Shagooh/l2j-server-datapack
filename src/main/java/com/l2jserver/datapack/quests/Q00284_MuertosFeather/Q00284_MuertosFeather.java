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
package com.l2jserver.datapack.quests.Q00284_MuertosFeather;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Muertos Feather (284).
 * @author xban1x
 */
public final class Q00284_MuertosFeather extends Quest {
	// NPC
	private static final int TREVOR = 32166;
	// Item
	private static final int MUERTOS_FEATHER = 9748;
	// Droplists
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22239, MUERTOS_FEATHER, 50.0)
			.addSingleDrop(22240, MUERTOS_FEATHER, 53.3)
			.addSingleDrop(22242, MUERTOS_FEATHER, 56.6)
			.addSingleDrop(22243, MUERTOS_FEATHER, 60.0)
			.addSingleDrop(22245, MUERTOS_FEATHER, 63.3)
			.addSingleDrop(22246, MUERTOS_FEATHER, 63.3)
			.build();
	// Misc
	private static final int MIN_LVL = 11;
	
	public Q00284_MuertosFeather() {
		super(284, Q00284_MuertosFeather.class.getSimpleName(), "Muertos Feather");
		addStartNpc(TREVOR);
		addTalkId(TREVOR);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(MUERTOS_FEATHER);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String html = null;
		if (qs == null) {
			return html;
		}
		switch (event) {
			case "32166-03.htm": {
				qs.startQuest();
				html = event;
				break;
			}
			case "32166-06.html": {
				html = event;
				break;
			}
			case "32166-08.html": {
				if (hasQuestItems(player, MUERTOS_FEATHER)) {
					giveAdena(player, getQuestItemsCount(player, MUERTOS_FEATHER) * 45, true);
					takeItems(player, MUERTOS_FEATHER, -1);
					html = event;
				} else {
					html = "32166-07.html";
				}
				break;
			}
			case "32166-09.html": {
				qs.exitQuest(true, true);
				html = event;
				break;
			}
		}
		return html;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, 1, 3, npc);
		if (qs != null) {
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String html = getNoQuestMsg(player);
		if (qs.isCreated()) {
			html = ((player.getLevel() >= MIN_LVL) ? "32166-01.htm" : "32166-02.htm");
		} else if (qs.isStarted()) {
			html = (hasQuestItems(player, MUERTOS_FEATHER) ? "32166-05.html" : "32166-04.html");
		}
		return html;
	}
}
