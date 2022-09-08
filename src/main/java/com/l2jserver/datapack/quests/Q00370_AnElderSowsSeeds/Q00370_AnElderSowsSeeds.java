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
package com.l2jserver.datapack.quests.Q00370_AnElderSowsSeeds;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.util.Util;

/**
 * An Elder Sows Seeds (370)
 * @author Adry_85
 */
public final class Q00370_AnElderSowsSeeds extends Quest {
	// NPC
	private static final int CASIAN = 30612;
	// Items
	private static final int SPELLBOOK_PAGE = 5916;
	private static final int CHAPTER_OF_FIRE = 5917;
	private static final int CHAPTER_OF_WATER = 5918;
	private static final int CHAPTER_OF_WIND = 5919;
	private static final int CHAPTER_OF_EARTH = 5920;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20082, SPELLBOOK_PAGE, 9.0) // ant_recruit
			.addSingleDrop(20086, SPELLBOOK_PAGE, 9.0) // ant_guard
			.addSingleDrop(20090, SPELLBOOK_PAGE, 22.0) // noble_ant_leader
			.addSingleDrop(20084, SPELLBOOK_PAGE, 10.1) // ant_patrol
			.addSingleDrop(20089, SPELLBOOK_PAGE, 10.0) // noble_ant
			.build();
	// Misc
	private static final int MIN_LEVEL = 28;
	
	public Q00370_AnElderSowsSeeds() {
		super(370, Q00370_AnElderSowsSeeds.class.getSimpleName(), "An Elder Sows Seeds");
		addStartNpc(CASIAN);
		addTalkId(CASIAN);
		addKillId(DROPLIST.getNpcIds());
	}
	
	@Override
	public boolean checkPartyMember(L2PcInstance member, L2Npc npc) {
		final QuestState st = getQuestState(member, false);
		return ((st != null) && st.isStarted());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30612-02.htm":
			case "30612-03.htm":
			case "30612-06.html":
			case "30612-07.html":
			case "30612-09.html": {
				htmltext = event;
				break;
			}
			case "30612-04.htm": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "REWARD": {
				if (st.isStarted()) {
					if (exchangeChapters(player, false)) {
						htmltext = "30612-08.html";
					} else {
						htmltext = "30612-11.html";
					}
				}
				break;
			}
			case "30612-10.html": {
				if (st.isStarted()) {
					exchangeChapters(player, true);
					st.exitQuest(true, true);
					htmltext = event;
				}
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
			htmltext = (player.getLevel() >= MIN_LEVEL) ? "30612-01.htm" : "30612-05.html";
		} else if (st.isStarted()) {
			htmltext = "30612-06.html";
		}
		return htmltext;
	}
	
	private boolean exchangeChapters(L2PcInstance player, boolean takeAllItems) {
		final long waterChapters = getQuestItemsCount(player, CHAPTER_OF_WATER);
		final long earthChapters = getQuestItemsCount(player, CHAPTER_OF_EARTH);
		final long windChapters = getQuestItemsCount(player, CHAPTER_OF_WIND);
		final long fireChapters = getQuestItemsCount(player, CHAPTER_OF_FIRE);
		final long minCount = Util.min(waterChapters, earthChapters, windChapters, fireChapters);
		if (minCount > 0) {
			giveAdena(player, minCount * 3600, true);
		}
		final long countToTake = (takeAllItems ? -1 : minCount);
		takeItems(player, (int) countToTake, CHAPTER_OF_WATER, CHAPTER_OF_EARTH, CHAPTER_OF_WIND, CHAPTER_OF_FIRE);
		return (minCount > 0);
	}
}
