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
package com.l2jserver.datapack.quests.Q00366_SilverHairedShaman;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Silver Haired Shaman (366)
 * @author Adry_85, jurchiks
 */
public final class Q00366_SilverHairedShaman extends Quest {
	// NPC
	private static final int DIETER = 30111;
	// Item
	private static final int SAIRONS_SILVER_HAIR = 5874;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20986, SAIRONS_SILVER_HAIR, 80.0) // saitnn
			.addSingleDrop(20987, SAIRONS_SILVER_HAIR, 73.0) // saitnn_doll
			.addSingleDrop(20988, SAIRONS_SILVER_HAIR, 80.0) // saitnn_puppet
			.build();
	// Misc
	private static final int MIN_LEVEL = 48;

	public Q00366_SilverHairedShaman() {
		super(366, Q00366_SilverHairedShaman.class.getSimpleName(), "Silver Haired Shaman");
		addStartNpc(DIETER);
		addTalkId(DIETER);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(SAIRONS_SILVER_HAIR);
	}
	
	@Override
	public boolean checkPartyMember(L2PcInstance member, L2Npc npc) {
		final QuestState qs = getQuestState(member, false);
		return ((qs != null) && qs.isStarted());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30111-02.htm": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "30111-05.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
			case "30111-06.html": {
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		L2PcInstance luckyPlayer = getRandomPartyMember(killer, npc);
		if (luckyPlayer != null) {
			giveItemRandomly(luckyPlayer, npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			htmltext = (player.getLevel() >= MIN_LEVEL) ? "30111-01.htm" : "30111-03.html";
		} else if (st.isStarted()) {
			if (hasQuestItems(player, SAIRONS_SILVER_HAIR)) {
				final long itemCount = getQuestItemsCount(player, SAIRONS_SILVER_HAIR);
				giveAdena(player, (itemCount * 500) + 29000, true);
				takeItems(player, SAIRONS_SILVER_HAIR, -1);
				htmltext = "30111-04.html";
			} else {
				htmltext = "30111-07.html";
			}
		}
		return htmltext;
	}
}
