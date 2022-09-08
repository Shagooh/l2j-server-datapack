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
package com.l2jserver.datapack.quests.Q00051_OFullesSpecialBait;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * O'Fulle's Special Bait (51)<br>
 * Original Jython script by Kilkenny.
 * @author nonom
 */
public class Q00051_OFullesSpecialBait extends Quest {
	// NPCs
	private static final int OFULLE = 31572;
	private static final int FETTERED_SOUL = 20552;
	// Items
	private static final QuestItemChanceHolder LOST_BAIT = new QuestItemChanceHolder(7622, 33.0, 100L);
	private static final int ICY_AIR_LURE = 7611;
	
	public Q00051_OFullesSpecialBait() {
		super(51, Q00051_OFullesSpecialBait.class.getSimpleName(), "O'Fulle's Special Bait");
		addStartNpc(OFULLE);
		addTalkId(OFULLE);
		addKillId(FETTERED_SOUL);
		registerQuestItems(LOST_BAIT.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		QuestState st = getQuestState(player, false);
		if (st == null) {
			return getNoQuestMsg(player);
		}
		
		String htmltext = event;
		switch (event) {
			case "31572-03.htm":
				st.startQuest();
				break;
			case "31572-07.html":
				if ((st.isCond(2)) && hasItemsAtLimit(st.getPlayer(), LOST_BAIT)) {
					htmltext = "31572-06.htm";
					st.giveItems(ICY_AIR_LURE, 4);
					st.exitQuest(false, true);
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(player, 1, 1, npc);
		if (st != null && giveItemRandomly(st.getPlayer(), npc, LOST_BAIT, true)) {
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
				htmltext = (player.getLevel() >= 36) ? "31572-01.htm" : "31572-02.html";
				break;
			case State.STARTED:
				htmltext = (st.isCond(1)) ? "31572-05.html" : "31572-04.html";
				break;
		}
		return htmltext;
	}
}
