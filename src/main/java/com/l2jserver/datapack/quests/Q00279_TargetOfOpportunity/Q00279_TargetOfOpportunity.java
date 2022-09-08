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
package com.l2jserver.datapack.quests.Q00279_TargetOfOpportunity;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Target of Opportunity (279)
 * @author GKR
 */
public final class Q00279_TargetOfOpportunity extends Quest {
	// NPCs
	private static final int JERIAN = 32302;
	// Items
	private static final int SEAL_BREAKER_5TH_FLOOR = 15515;
	private static final int SEAL_BREAKER_10TH_FLOOR = 15516;
	private static final QuestItemChanceHolder SEAL_COMPONENTS_PART1 = new QuestItemChanceHolder(15517, 31.1, 1L);
	private static final QuestItemChanceHolder SEAL_COMPONENTS_PART2 = new QuestItemChanceHolder(15518, 31.1, 1L);
	private static final QuestItemChanceHolder SEAL_COMPONENTS_PART3 = new QuestItemChanceHolder(15519, 31.1, 1L);
	private static final QuestItemChanceHolder SEAL_COMPONENTS_PART4 = new QuestItemChanceHolder(15520, 31.1, 1L);
	// Droplist
	private static final QuestDroplist DROPLIST_SEAL_COMPONENTS = QuestDroplist.builder()
			.addSingleDrop(22373, SEAL_COMPONENTS_PART1) // Cosmic Scout
			.addSingleDrop(22374, SEAL_COMPONENTS_PART2) // Cosmic Watcher
			.addSingleDrop(22375, SEAL_COMPONENTS_PART3) // Cosmic Priest
			.addSingleDrop(22376, SEAL_COMPONENTS_PART4) // Cosmic Lord
			.build();

	public Q00279_TargetOfOpportunity() {
		super(279, Q00279_TargetOfOpportunity.class.getSimpleName(), "Target of Opportunity");
		addStartNpc(JERIAN);
		addTalkId(JERIAN);
		addKillId(DROPLIST_SEAL_COMPONENTS.getNpcIds());
		registerQuestItems(SEAL_COMPONENTS_PART1.getId(), SEAL_COMPONENTS_PART2.getId(), SEAL_COMPONENTS_PART3.getId(), SEAL_COMPONENTS_PART4.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if ((st == null) || (player.getLevel() < 82)) {
			return getNoQuestMsg(player);
		}
		
		if (event.equalsIgnoreCase("32302-05.html")) {
			st.startQuest();
			st.set("progress", "1");
		} else if (event.equalsIgnoreCase("32302-08.html") && (st.getInt("progress") == 1)
				&& hasQuestItems(st.getPlayer(), SEAL_COMPONENTS_PART1.getId(), SEAL_COMPONENTS_PART2.getId(), SEAL_COMPONENTS_PART3.getId(), SEAL_COMPONENTS_PART4.getId())) {
			st.giveItems(SEAL_BREAKER_5TH_FLOOR, 1);
			st.giveItems(SEAL_BREAKER_10TH_FLOOR, 1);
			st.exitQuest(true, true);
		}
		return event;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		L2PcInstance pl = getRandomPartyMember(player, "progress", "1");
		if (pl != null) {
			final QuestState st = getQuestState(pl, false);
			if (giveItemRandomly(st.getPlayer(), npc, DROPLIST_SEAL_COMPONENTS.get(npc), false)) {
				if (hasItemsAtLimit(st.getPlayer(), SEAL_COMPONENTS_PART1, SEAL_COMPONENTS_PART2, SEAL_COMPONENTS_PART3, SEAL_COMPONENTS_PART4)) {
					st.setCond(2, true);
				} else {
					playSound(st.getPlayer(), Sound.ITEMSOUND_QUEST_ITEMGET);
				}
			}
		}
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		if (st.getState() == State.CREATED) {
			htmltext = (player.getLevel() >= 82) ? "32302-01.htm" : "32302-02.html";
		} else if ((st.getState() == State.STARTED) && (st.getInt("progress") == 1)) {
			htmltext = hasQuestItems(st.getPlayer(), SEAL_COMPONENTS_PART1.getId(), SEAL_COMPONENTS_PART2.getId(), SEAL_COMPONENTS_PART3.getId(), SEAL_COMPONENTS_PART4.getId()) ? "32302-07.html" : "32302-06.html";
		}
		return htmltext;
	}
}
