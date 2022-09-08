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
package com.l2jserver.datapack.quests.Q00053_LinnaeusSpecialBait;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Linnaeus Special Bait (53)<br>
 * Original Jython script by Next and DooMita.
 * @author nonom
 */
public class Q00053_LinnaeusSpecialBait extends Quest {
	// NPCs
	private static final int LINNAEUS = 31577;
	private static final int CRIMSON_DRAKE = 20670;
	// Items
	private static final int FLAMING_FISHING_LURE = 7613;
	private static final QuestItemChanceHolder CRIMSON_DRAKE_HEART = new QuestItemChanceHolder(7624, 33.0, 100L);

	public Q00053_LinnaeusSpecialBait() {
		super(53, Q00053_LinnaeusSpecialBait.class.getSimpleName(), "Linnaeus Special Bait");
		addStartNpc(LINNAEUS);
		addTalkId(LINNAEUS);
		addKillId(CRIMSON_DRAKE);
		registerQuestItems(CRIMSON_DRAKE_HEART.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		QuestState st = getQuestState(player, false);
		if (st == null) {
			return getNoQuestMsg(player);
		}
		
		String htmltext = event;
		
		switch (event) {
			case "31577-1.htm":
				st.startQuest();
				break;
			case "31577-3.htm":
				if (st.isCond(2) && hasItemsAtLimit(st.getPlayer(), CRIMSON_DRAKE_HEART)) {
					st.giveItems(FLAMING_FISHING_LURE, 4);
					st.exitQuest(false, true);
				} else {
					htmltext = "31577-5.html";
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(player, 1, 1, npc);
		if (st != null && giveItemRandomly(st.getPlayer(), npc, CRIMSON_DRAKE_HEART, true)) {
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
				htmltext = (player.getLevel() > 59) ? "31577-0.htm" : "31577-0a.html";
				break;
			case State.STARTED:
				htmltext = (st.isCond(1)) ? "31577-4.html" : "31577-2.html";
				break;
		}
		return htmltext;
	}
}
