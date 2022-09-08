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
package com.l2jserver.datapack.quests.Q00371_ShrieksOfGhosts;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.util.Util;

/**
 * Shrieks of Ghosts (371)
 * @author Adry_85
 */
public final class Q00371_ShrieksOfGhosts extends Quest {
	// NPCs
	private static final int REVA = 30867;
	private static final int PATRIN = 30929;
	// Items
	private static final int ANCIENT_ASH_URN = 5903;
	private static final int ANCIENT_PORCELAIN = 6002;
	private static final int ANCIENT_PORCELAIN_EXCELLENT = 6003;
	private static final int ANCIENT_PORCELAIN_HIGH_QUALITY = 6004;
	private static final int ANCIENT_PORCELAIN_LOW_QUALITY = 6005;
	private static final int ANCIENT_PORCELAIN_LOWEST_QUALITY = 6006;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addGroupedDrop(20818, 40.0) // hallates_warrior
				.withDropItem(ANCIENT_ASH_URN, 87.5)
				.withDropItem(ANCIENT_PORCELAIN, 12.5)
				.build()
			.addGroupedDrop(20820, 67.3) // hallates_knight
				.withDropItem(ANCIENT_ASH_URN, 86.63)
				.withDropItem(ANCIENT_PORCELAIN, 13.37)
				.build()
			.addGroupedDrop(20824, 53.8) // hallates_commander
				.withDropItem(ANCIENT_ASH_URN, 85.13)
				.withDropItem(ANCIENT_PORCELAIN, 14.87)
				.build()
			.build();
	// Misc
	private static final int MIN_LEVEL = 59;


	public Q00371_ShrieksOfGhosts() {
		super(371, Q00371_ShrieksOfGhosts.class.getSimpleName(), "Shrieks of Ghosts");
		addStartNpc(REVA);
		addTalkId(REVA, PATRIN);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(ANCIENT_ASH_URN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if (qs == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30867-02.htm": {
				qs.startQuest();
				htmltext = event;
				break;
			}
			case "30867-05.html": {
				final long ancientAshUrnCount = getQuestItemsCount(player, ANCIENT_ASH_URN);
				
				if (ancientAshUrnCount < 1) {
					htmltext = event;
				} else if (ancientAshUrnCount < 100) {
					giveAdena(player, (ancientAshUrnCount * 1000) + 15000, true);
					takeItems(player, ANCIENT_ASH_URN, -1);
					htmltext = "30867-06.html";
				} else {
					giveAdena(player, (ancientAshUrnCount * 1000) + 37700, true);
					takeItems(player, ANCIENT_ASH_URN, -1);
					htmltext = "30867-07.html";
				}
				break;
			}
			case "30867-08.html":
			case "30929-01.html":
			case "30929-02.html": {
				htmltext = event;
				break;
			}
			case "30867-09.html": {
				giveAdena(player, getQuestItemsCount(player, ANCIENT_ASH_URN) * 1000, true);
				qs.exitQuest(true, true);
				htmltext = "30867-09.html";
				break;
			}
			case "30929-03.html": {
				if (!hasQuestItems(player, ANCIENT_PORCELAIN)) {
					htmltext = event;
				} else {
					final int random = getRandom(100);
					
					if (random < 2) {
						giveItems(player, ANCIENT_PORCELAIN_EXCELLENT, 1);
						htmltext = "30929-04.html";
						
					} else if (random < 32) {
						giveItems(player, ANCIENT_PORCELAIN_HIGH_QUALITY, 1);
						htmltext = "30929-05.html";
					} else if (random < 62) {
						giveItems(player, ANCIENT_PORCELAIN_LOW_QUALITY, 1);
						htmltext = "30929-06.html";
					} else if (random < 77) {
						giveItems(player, ANCIENT_PORCELAIN_LOWEST_QUALITY, 1);
						htmltext = "30929-07.html";
					} else {
						htmltext = "30929-08.html";
					}
					
					takeItems(player, ANCIENT_PORCELAIN, 1);
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

		giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);

		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			htmltext = (player.getLevel() >= MIN_LEVEL) ? "30867-01.htm" : "30867-03.htm";
		} else if (qs.isStarted()) {
			if (npc.getId() == REVA) {
				htmltext = (hasQuestItems(player, ANCIENT_PORCELAIN)) ? "30867-04.html" : "30867-10.html";
			} else {
				htmltext = "30929-01.html";
			}
		}
		return htmltext;
	}
}
