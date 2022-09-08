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
package com.l2jserver.datapack.quests.Q00280_TheFoodChain;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.util.Util;

/**
 * The Food Chain (280)
 * @author xban1x
 */
public final class Q00280_TheFoodChain extends Quest {
	// Npc
	private static final int BIXON = 32175;
	// Items
	private static final int GREY_KELTIR_TOOTH = 9809;
	private static final QuestItemChanceHolder BLACK_WOLF_TOOTH = new QuestItemChanceHolder(9810, 3L, 0L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22229, GREY_KELTIR_TOOTH)
			.addSingleDrop(22230, GREY_KELTIR_TOOTH, 150.0)
			.addSingleDrop(22231, GREY_KELTIR_TOOTH, 2L)
			.addSingleDrop(22232, BLACK_WOLF_TOOTH)
			.addGroupedDropForSingleItem(22233, BLACK_WOLF_TOOTH, 100.0)
				.withAmount(4, 50.0).orElse(1)
			.build();
	// Rewards
	private static final int[] REWARDS = new int[] {
		28, 35, 41, 48, 116,
	};
	// Misc
	private static final int MIN_LVL = 3;
	private static final int TEETH_COUNT = 25;
	
	public Q00280_TheFoodChain() {
		super(280, Q00280_TheFoodChain.class.getSimpleName(), "The Food Chain");
		addStartNpc(BIXON);
		addTalkId(BIXON);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(GREY_KELTIR_TOOTH, BLACK_WOLF_TOOTH.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		String htmltext = null;
		if (st == null) {
			return htmltext;
		}
		
		switch (event) {
			case "32175-03.htm": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "32175-06.html": {
				if (hasAtLeastOneQuestItem(player, getRegisteredItemIds())) {
					final long greyTeeth = st.getQuestItemsCount(GREY_KELTIR_TOOTH);
					final long blackTeeth = st.getQuestItemsCount(BLACK_WOLF_TOOTH.getId());
					st.giveAdena(2 * (greyTeeth + blackTeeth), true);
					takeItems(player, -1, GREY_KELTIR_TOOTH, BLACK_WOLF_TOOTH.getId());
					htmltext = event;
				} else {
					htmltext = "32175-07.html";
				}
				break;
			}
			case "32175-08.html": {
				htmltext = event;
				break;
			}
			case "32175-09.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
			case "32175-11.html": {
				final long greyTeeth = st.getQuestItemsCount(GREY_KELTIR_TOOTH);
				final long blackTeeth = st.getQuestItemsCount(BLACK_WOLF_TOOTH.getId());
				if ((greyTeeth + blackTeeth) >= TEETH_COUNT) {
					if (greyTeeth >= TEETH_COUNT) {
						st.takeItems(GREY_KELTIR_TOOTH, TEETH_COUNT);
					} else {
						st.takeItems(GREY_KELTIR_TOOTH, greyTeeth);
						st.takeItems(BLACK_WOLF_TOOTH.getId(), TEETH_COUNT - greyTeeth);
					}
					st.rewardItems(REWARDS[getRandom(5)], 1);
					htmltext = event;
				} else {
					htmltext = "32175-10.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getQuestState(killer, false);
		if ((st != null) && Util.checkIfInRange(1500, npc, st.getPlayer(), true)) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		final QuestState st = getQuestState(talker, true);
		String htmltext = getNoQuestMsg(talker);
		switch (st.getState()) {
			case State.CREATED: {
				htmltext = (talker.getLevel() >= MIN_LVL) ? "32175-01.htm" : "32175-02.htm";
				break;
			}
			case State.STARTED: {
				if (hasAtLeastOneQuestItem(talker, getRegisteredItemIds())) {
					htmltext = "32175-05.html";
				} else {
					htmltext = "32175-04.html";
				}
				break;
			}
		}
		return htmltext;
	}
}
