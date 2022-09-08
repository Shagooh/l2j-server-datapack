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
package com.l2jserver.datapack.quests.Q00369_CollectorOfJewels;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Collector of Jewels (369)
 * @author Adry_85
 */
public final class Q00369_CollectorOfJewels extends Quest {
	// NPC
	private static final int NELL = 30376;
	// Items
	private static final int FLARE_SHARD = 5882;
	private static final int FREEZING_SHARD = 5883;
	// Mobs
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20609, FLARE_SHARD, 75.0) // salamander_lakin
			.addSingleDrop(20612, FLARE_SHARD, 91.0) // salamander_rowin
			.addSingleDrop(20749, FLARE_SHARD, 2L) // death_fire
			.addSingleDrop(20616, FREEZING_SHARD, 81.0) // undine_lakin
			.addSingleDrop(20619, FREEZING_SHARD, 87.0) // undine_rowin
			.addSingleDrop(20747, FREEZING_SHARD, 2L) // roxide
			.build();
	// Misc
	private static final int MIN_LEVEL = 25;
	private static final int FIRST_STEP_LIMIT = 50;
	private static final int SECOND_STEP_LIMIT = 200;

	public Q00369_CollectorOfJewels() {
		super(369, Q00369_CollectorOfJewels.class.getSimpleName(), "Collector of Jewels");
		addStartNpc(NELL);
		addTalkId(NELL);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(FLARE_SHARD, FREEZING_SHARD);
	}
	
	@Override
	public boolean checkPartyMember(L2PcInstance member, L2Npc npc) {
		final QuestState st = getQuestState(member, false);
		return ((st != null) && (st.isMemoState(1) || st.isMemoState(3)));
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30376-02.htm": {
				st.startQuest();
				st.setMemoState(1);
				htmltext = event;
				break;
			}
			case "30376-05.html": {
				htmltext = event;
				break;
			}
			case "30376-06.html": {
				if (st.isMemoState(2)) {
					st.setMemoState(3);
					st.setCond(3, true);
					htmltext = event;
				}
				break;
			}
			case "30376-07.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		L2PcInstance luckyPlayer = getRandomPartyMember(killer, npc);
		final QuestState st = getQuestState(luckyPlayer, false);

		final int itemLimit = (st.isMemoState(1) ? FIRST_STEP_LIMIT : SECOND_STEP_LIMIT);
		if (giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc).drop(), itemLimit, true)
			&& (getQuestItemsCount(luckyPlayer, FLARE_SHARD, FREEZING_SHARD) >= (itemLimit * 2))) {
			st.setCond((st.isMemoState(1) ? 2 : 4));
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			htmltext = (player.getLevel() >= MIN_LEVEL) ? "30376-01.htm" : "30376-03.html";
		} else if (st.isStarted()) {
			switch (st.getMemoState()) {
				case 1: {
					if (getQuestItemsCount(player, FLARE_SHARD, FREEZING_SHARD) >= FIRST_STEP_LIMIT * 2) {
						giveAdena(player, 31810, true);
						takeItems(player, -1, FLARE_SHARD, FREEZING_SHARD);
						st.setMemoState(2);
						htmltext = "30376-04.html";
					} else {
						htmltext = "30376-08.html";
					}
					break;
				}
				case 2: {
					htmltext = "30376-09.html";
					break;
				}
				case 3: {
					if (getQuestItemsCount(player, FLARE_SHARD, FREEZING_SHARD) >= SECOND_STEP_LIMIT * 2) {
						giveAdena(player, 84415, true);
						takeItems(player, -1, FLARE_SHARD, FREEZING_SHARD);
						st.exitQuest(true, true);
						htmltext = "30376-10.html";
					} else {
						htmltext = "30376-11.html";
					}
					break;
				}
			}
		}
		return htmltext;
	}
}
