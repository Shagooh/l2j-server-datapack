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
package com.l2jserver.datapack.quests.Q00379_FantasyWine;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.util.Util;

/**
 * Fantasy Wine (379)
 * @author Adry_85
 */
public final class Q00379_FantasyWine extends Quest {
	// NPC
	private static final int HARLAN = 30074;
	// Monsters
	private static final int ENKU_ORC_CHAMPION = 20291;
	private static final int ENKU_ORC_SHAMAN = 20292;
	// Items
	private static final int OLD_WINE_15_YEAR = 5956;
	private static final int OLD_WINE_30_YEAR = 5957;
	private static final int OLD_WINE_60_YEAR = 5958;
	private static final QuestItemChanceHolder LEAF_OF_EUCALYPTUS = new QuestItemChanceHolder(5893, 80L);
	private static final QuestItemChanceHolder STONE_OF_CHILL = new QuestItemChanceHolder(5894, 100L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(ENKU_ORC_CHAMPION, LEAF_OF_EUCALYPTUS)
			.addSingleDrop(ENKU_ORC_SHAMAN, STONE_OF_CHILL)
			.build();
	// Misc
	private static final int MIN_LEVEL = 20;
	
	public Q00379_FantasyWine() {
		super(379, Q00379_FantasyWine.class.getSimpleName(), "Fantasy Wine");
		addStartNpc(HARLAN);
		addTalkId(HARLAN);
		addKillId(ENKU_ORC_CHAMPION, ENKU_ORC_SHAMAN);
		registerQuestItems(LEAF_OF_EUCALYPTUS.getId(), STONE_OF_CHILL.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if (qs == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30074-02.htm":
			case "30074-03.htm":
			case "30074-05.html": {
				htmltext = event;
				break;
			}
			case "30074-04.htm": {
				qs.startQuest();
				htmltext = event;
				break;
			}
			case "30074-11.html": {
				if (hasItemsAtLimit(player, LEAF_OF_EUCALYPTUS, STONE_OF_CHILL)) {
					final int random = getRandom(10);
					final int item;
					
					if (random < 3) {
						item = OLD_WINE_15_YEAR;
						htmltext = event;
					} else if (random < 9) {
						item = OLD_WINE_30_YEAR;
						htmltext = "30074-12.html";
					} else {
						item = OLD_WINE_60_YEAR;
						htmltext = "30074-13.html";
					}
					
					giveItems(player, item, 1);
					takeItems(player, -1, LEAF_OF_EUCALYPTUS.getId(), STONE_OF_CHILL.getId());
					qs.exitQuest(true, true);
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getQuestState(killer, false);
		
		if ((qs == null) || !Util.checkIfInRange(1500, npc, qs.getPlayer(), true)) {
			return null;
		}

		if (giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true) && hasItemsAtLimit(killer, LEAF_OF_EUCALYPTUS, STONE_OF_CHILL)) {
			qs.setCond(2);
		}

		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			htmltext = (player.getLevel() >= MIN_LEVEL) ? "30074-01.htm" : "30074-06.html";
		} else if (qs.isStarted()) {
			final boolean hasLeafOfEucalyptus = hasItemsAtLimit(player, LEAF_OF_EUCALYPTUS);
			final boolean hasStoneOfChill = hasItemsAtLimit(player, STONE_OF_CHILL);
			
			if (!hasLeafOfEucalyptus && !hasStoneOfChill) {
				htmltext = "30074-07.html";
			} else if (hasLeafOfEucalyptus && !hasStoneOfChill) {
				htmltext = "30074-08.html";
			} else if (!hasLeafOfEucalyptus && hasStoneOfChill) {
				htmltext = "30074-09.html";
			} else {
				htmltext = "30074-10.html";
			}
		}
		return htmltext;
	}
}
