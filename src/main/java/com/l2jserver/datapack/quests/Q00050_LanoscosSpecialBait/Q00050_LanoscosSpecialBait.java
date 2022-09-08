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
package com.l2jserver.datapack.quests.Q00050_LanoscosSpecialBait;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Lanosco's Special Bait (50)<br>
 * Original Jython script by Kilkenny.
 * @author nonom
 */
public class Q00050_LanoscosSpecialBait extends Quest {
	// NPCs
	private static final int LANOSCO = 31570;
	private static final int SINGING_WIND = 21026;
	// Items
	private static final int WIND_FISHING_LURE = 7610;
	private static final QuestItemChanceHolder ESSENCE_OF_WIND = new QuestItemChanceHolder(7621, 33.0, 100L);

	public Q00050_LanoscosSpecialBait() {
		super(50, Q00050_LanoscosSpecialBait.class.getSimpleName(), "Lanosco's Special Bait");
		addStartNpc(LANOSCO);
		addTalkId(LANOSCO);
		addKillId(SINGING_WIND);
		registerQuestItems(ESSENCE_OF_WIND.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		QuestState st = getQuestState(player, false);
		if (st == null) {
			return getNoQuestMsg(player);
		}
		
		String htmltext = event;
		
		switch (event) {
			case "31570-03.htm":
				st.startQuest();
				break;
			case "31570-07.html":
				if ((st.isCond(2)) && hasItemsAtLimit(st.getPlayer(), ESSENCE_OF_WIND)) {
					htmltext = "31570-06.htm";
					st.giveItems(WIND_FISHING_LURE, 4);
					st.exitQuest(false, true);
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(player, 1, 1, npc);
		if (st != null && giveItemRandomly(st.getPlayer(), npc, ESSENCE_OF_WIND, true)) {
			st.setCond(2);
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		switch (st.getState()) {
			case State.COMPLETED:
				htmltext = getAlreadyCompletedMsg(player);
				break;
			case State.CREATED:
				htmltext = (player.getLevel() >= 27) ? "31570-01.htm" : "31570-02.html";
				break;
			case State.STARTED:
				htmltext = (st.isCond(1)) ? "31570-05.html" : "31570-04.html";
				break;
		}
		return htmltext;
	}
}
