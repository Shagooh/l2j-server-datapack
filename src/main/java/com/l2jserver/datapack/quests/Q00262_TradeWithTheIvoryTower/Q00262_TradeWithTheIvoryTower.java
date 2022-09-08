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
package com.l2jserver.datapack.quests.Q00262_TradeWithTheIvoryTower;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Trade With The Ivory Tower (262)
 * @author ivantotov
 */
public final class Q00262_TradeWithTheIvoryTower extends Quest {
	// NPCs
	private static final int VOLLODOS = 30137;
	// Items
	private static final QuestItemChanceHolder SPORE_SAC = new QuestItemChanceHolder(707, 10L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20007, SPORE_SAC, 30.0) // Green Fungus
			.addSingleDrop(20400, SPORE_SAC, 40.0) // Blood Fungus
			.build();
	// Misc
	private static final int MIN_LEVEL = 8;
	private static final int REQUIRED_ITEM_COUNT = 10;

	public Q00262_TradeWithTheIvoryTower() {
		super(262, Q00262_TradeWithTheIvoryTower.class.getSimpleName(), "Trade With The Ivory Tower");
		addStartNpc(VOLLODOS);
		addTalkId(VOLLODOS);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(SPORE_SAC.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if ((st != null) && event.equalsIgnoreCase("30137-03.htm")) {
			st.startQuest();
			return event;
		}
		return null;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(player, 1, 1, npc);
		if (st != null && giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true)) {
			st.setCond(2);
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		switch (st.getState()) {
			case State.CREATED: {
				htmltext = player.getLevel() >= MIN_LEVEL ? "30137-02.htm" : "30137-01.htm";
				break;
			}
			case State.STARTED: {
				switch (st.getCond()) {
					case 1: {
						if (!hasItemsAtLimit(st.getPlayer(), SPORE_SAC)) {
							htmltext = "30137-04.html";
						}
						break;
					}
					case 2: {
						if (hasItemsAtLimit(st.getPlayer(), SPORE_SAC)) {
							htmltext = "30137-05.html";
							st.giveAdena(3000, true);
							st.exitQuest(true, true);
						}
						break;
					}
				}
				break;
			}
		}
		return htmltext;
	}
}
