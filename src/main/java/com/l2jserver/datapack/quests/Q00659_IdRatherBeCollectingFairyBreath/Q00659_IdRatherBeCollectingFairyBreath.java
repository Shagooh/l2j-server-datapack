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
package com.l2jserver.datapack.quests.Q00659_IdRatherBeCollectingFairyBreath;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * I'd Rather Be Collecting Fairy Breath (659)
 * @author Adry_85
 */
public final class Q00659_IdRatherBeCollectingFairyBreath extends Quest {
	// NPC
	private static final int GALATEA = 30634;
	// Item
	private static final int FAIRY_BREATH = 8286;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20078, FAIRY_BREATH, 98.0) // whispering_wind
			.addSingleDrop(21023, FAIRY_BREATH, 82.0) // sobing_wind
			.addSingleDrop(21024, FAIRY_BREATH, 86.0) // babbleing_wind
			.addSingleDrop(21025, FAIRY_BREATH, 90.0) // giggleing_wind
			.addSingleDrop(21026, FAIRY_BREATH, 96.0) // singing_wind
			.build();
	// Misc
	private static final int MIN_LEVEL = 26;

	public Q00659_IdRatherBeCollectingFairyBreath() {
		super(659, Q00659_IdRatherBeCollectingFairyBreath.class.getSimpleName(), "I'd Rather Be Collecting Fairy Breath");
		addStartNpc(GALATEA);
		addTalkId(GALATEA);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(FAIRY_BREATH);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30634-02.htm": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "REWARD": {
				if (hasQuestItems(player, FAIRY_BREATH)) {
					final long count = getQuestItemsCount(player, FAIRY_BREATH);
					final long bonus = ((count >= 10) ? 5365 : 0);
					st.takeItems(FAIRY_BREATH, -1);
					st.giveAdena((count * 50) + bonus, true);
					htmltext = "30634-05.html";
				} else {
					htmltext = "30634-06.html";
				}
				break;
			}
			case "30634-07.html": {
				htmltext = event;
				break;
			}
			case "30634-08.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(player, -1, 3, npc);
		if (st != null) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LEVEL) ? "30634-01.htm" : "30634-03.html");
		} else if (st.isStarted()) {
			htmltext = (hasQuestItems(player, FAIRY_BREATH) ? "30634-04.html" : "30634-09.html");
		}
		return htmltext;
	}
}
