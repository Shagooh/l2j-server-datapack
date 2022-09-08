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
package com.l2jserver.datapack.quests.Q00628_HuntGoldenRam;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Hunt of the Golden Ram Mercenary Force (628)
 * @author netvirus, Zoey76
 */
public final class Q00628_HuntGoldenRam extends Quest {
	// Misc
	private static final int MIN_LVL = 66;
	private static final long REQUIRED_ITEM_COUNT = 100L;
	// NPCs
	private static final int KAHMAN = 31554;
	// Items
	private static final int GOLDEN_RAM_BADGE_RECRUIT = 7246;
	private static final int GOLDEN_RAM_BADGE_SOLDIER = 7247;
	private static final QuestItemChanceHolder SPLINTER_STAKATO_CHITIN = new QuestItemChanceHolder(7248, REQUIRED_ITEM_COUNT);
	private static final QuestItemChanceHolder NEEDLE_STAKATO_CHITIN = new QuestItemChanceHolder(7249, REQUIRED_ITEM_COUNT);
	// Droplists
	private static final QuestDroplist SPLINTER_DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21508, SPLINTER_STAKATO_CHITIN, 50.0) // splinter_stakato
			.addSingleDrop(21509, SPLINTER_STAKATO_CHITIN, 43.0) // splinter_stakato_worker
			.addSingleDrop(21510, SPLINTER_STAKATO_CHITIN, 52.1) // splinter_stakato_soldier
			.addSingleDrop(21511, SPLINTER_STAKATO_CHITIN, 57.5) // splinter_stakato_drone
			.addSingleDrop(21512, SPLINTER_STAKATO_CHITIN, 74.6) // splinter_stakato_drone_a
			.build();
	private static final QuestDroplist NEEDLE_DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21513, NEEDLE_STAKATO_CHITIN, 50.0) // needle_stakato
			.addSingleDrop(21514, NEEDLE_STAKATO_CHITIN, 43.0) // needle_stakato_worker
			.addSingleDrop(21515, NEEDLE_STAKATO_CHITIN, 52.0) // needle_stakato_soldier
			.addSingleDrop(21516, NEEDLE_STAKATO_CHITIN, 53.1) // needle_stakato_drone
			.addSingleDrop(21517, NEEDLE_STAKATO_CHITIN, 74.4) // needle_stakato_drone_a
			.build();
	
	public Q00628_HuntGoldenRam() {
		super(628, Q00628_HuntGoldenRam.class.getSimpleName(), "Hunt of the Golden Ram Mercenary Force");
		addStartNpc(KAHMAN);
		addTalkId(KAHMAN);
		addKillId(SPLINTER_DROPLIST.getNpcIds());
		addKillId(NEEDLE_DROPLIST.getNpcIds());
		registerQuestItems(SPLINTER_STAKATO_CHITIN.getId(), NEEDLE_STAKATO_CHITIN.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs == null) {
			return htmltext;
		}
		
		switch (event) {
			case "accept": {
				if (qs.isCreated() && (player.getLevel() >= MIN_LVL)) {
					qs.startQuest();
					if (hasQuestItems(player, GOLDEN_RAM_BADGE_SOLDIER)) {
						qs.setCond(3);
						htmltext = "31554-05.htm";
					} else if (hasQuestItems(player, GOLDEN_RAM_BADGE_RECRUIT)) {
						qs.setCond(2);
						htmltext = "31554-04.htm";
					} else {
						htmltext = "31554-03.htm";
					}
				}
				break;
			}
			case "31554-08.html": {
				if (hasItemsAtLimit(player, SPLINTER_STAKATO_CHITIN)) {
					giveItems(player, GOLDEN_RAM_BADGE_RECRUIT, 1);
					takeItems(player, SPLINTER_STAKATO_CHITIN.getId(), -1);
					qs.setCond(2, true);
					htmltext = event;
				}
				break;
			}
			case "31554-12.html":
			case "31554-13.html": {
				if (qs.isStarted()) {
					htmltext = event;
				}
				break;
			}
			case "31554-14.html": {
				if (qs.isStarted()) {
					qs.exitQuest(true, true);
					htmltext = event;
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, -1, 1, npc);
		if (qs != null && !qs.isCond(3)) {
			if (qs.getCond() >= 1) {
				giveItemRandomly(qs.getPlayer(), npc, SPLINTER_DROPLIST.get(npc), true);
				if (qs.isCond(2)) {
					giveItemRandomly(qs.getPlayer(), npc, NEEDLE_DROPLIST.get(npc), true);
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (qs.getState()) {
			case State.CREATED: {
				htmltext = ((player.getLevel() >= MIN_LVL) ? "31554-01.htm" : "31554-02.htm");
				break;
			}
			case State.STARTED: {
				final long itemCountSplinter = getQuestItemsCount(player, SPLINTER_STAKATO_CHITIN.getId());
				final long itemCountNeedle = getQuestItemsCount(player, NEEDLE_STAKATO_CHITIN.getId());
				switch (qs.getCond()) {
					case 1: {
						htmltext = ((itemCountSplinter >= REQUIRED_ITEM_COUNT) ? "31554-07.html" : "31554-06.html");
						break;
					}
					case 2: {
						if (hasQuestItems(player, GOLDEN_RAM_BADGE_RECRUIT)) {
							if ((itemCountSplinter >= REQUIRED_ITEM_COUNT) && (itemCountNeedle >= REQUIRED_ITEM_COUNT)) {
								takeItems(player, GOLDEN_RAM_BADGE_RECRUIT, -1);
								takeItems(player, SPLINTER_STAKATO_CHITIN.getId(), -1);
								takeItems(player, NEEDLE_STAKATO_CHITIN.getId(), -1);
								giveItems(player, GOLDEN_RAM_BADGE_SOLDIER, 1);
								qs.setCond(3, true);
								htmltext = "31554-10.html";
							} else {
								htmltext = "31554-09.html";
							}
						} else {
							qs.setCond(1);
							htmltext = ((itemCountSplinter >= REQUIRED_ITEM_COUNT) ? "31554-07.html" : "31554-06.html");
						}
						break;
					}
					case 3: {
						if (hasQuestItems(player, GOLDEN_RAM_BADGE_SOLDIER)) {
							htmltext = "31554-11.html";
						} else {
							qs.setCond(1);
							htmltext = ((itemCountSplinter >= REQUIRED_ITEM_COUNT) ? "31554-07.html" : "31554-06.html");
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
