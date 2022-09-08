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
package com.l2jserver.datapack.quests.Q00654_JourneyToASettlement;

import com.l2jserver.datapack.quests.Q00119_LastImperialPrince.Q00119_LastImperialPrince;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Journey to a Settlement (654)
 * @author Adry_85
 */
public final class Q00654_JourneyToASettlement extends Quest {
	// NPC
	private static final int NAMELESS_SPIRIT = 31453;
	// Items
	private static final int FRINTEZZAS_SCROLL = 8073;
	private static final QuestItemChanceHolder ANTELOPE_SKIN = new QuestItemChanceHolder(8072, 1L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21294, ANTELOPE_SKIN, 84.0) // Canyon Antelope
			.addSingleDrop(21295, ANTELOPE_SKIN, 89.3) // Canyon Antelope Slave
			.build();
	// Misc
	private static final int MIN_LEVEL = 74;


	public Q00654_JourneyToASettlement() {
		super(654, Q00654_JourneyToASettlement.class.getSimpleName(), "Journey to a Settlement");
		addStartNpc(NAMELESS_SPIRIT);
		addTalkId(NAMELESS_SPIRIT);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(ANTELOPE_SKIN.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "31453-02.htm": {
				st.startQuest();
				st.setMemoState(1);
				htmltext = event;
				break;
			}
			case "31453-03.html": {
				if (st.isMemoState(1)) {
					st.setMemoState(2);
					st.setCond(2, true);
					htmltext = event;
				}
			}
			case "31453-07.html": {
				if (st.isMemoState(2) && st.hasQuestItems(ANTELOPE_SKIN.getId())) {
					giveItems(player, FRINTEZZAS_SCROLL, 1);
					st.exitQuest(true, true);
					htmltext = event;
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(player, 2, 3, npc);
		if ((st != null) && giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true)) {
			st.setCond(3);
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LEVEL) && player.hasQuestCompleted(Q00119_LastImperialPrince.class.getSimpleName())) ? "31453-01.htm" : "31453-04.htm";
		} else if (st.isStarted()) {
			if (st.isMemoState(1)) {
				st.setMemoState(2);
				st.setCond(2, true);
				htmltext = "31453-03.html";
			} else if (st.isMemoState(2)) {
				htmltext = (hasQuestItems(player, ANTELOPE_SKIN.getId()) ? "31453-06.html" : "31453-05.html");
			}
		}
		return htmltext;
	}
}
