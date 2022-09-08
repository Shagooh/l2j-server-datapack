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
package com.l2jserver.datapack.quests.Q00355_FamilyHonor;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.util.Util;

/**
 * Family Honor (355)
 * @author Adry_85
 */
public final class Q00355_FamilyHonor extends Quest {
	private static final class DropInfo {
		public final int _firstChance;
		public final int _secondChance;
		
		public DropInfo(int firstChance, int secondChance) {
			_firstChance = firstChance;
			_secondChance = secondChance;
		}
		
		public int getFirstChance() {
			return _firstChance;
		}
		
		public int getSecondChance() {
			return _secondChance;
		}
	}
	
	// NPCs
	private static final int GALIBREDO = 30181;
	private static final int PATRIN = 30929;
	// Items
	private static final int GALFREDO_ROMERS_BUST = 4252;
	private static final int SCULPTOR_BERONA = 4350;
	private static final int ANCIENT_STATUE_PROTOTYPE = 4351;
	private static final int ANCIENT_STATUE_ORIGINAL = 4352;
	private static final int ANCIENT_STATUE_REPLICA = 4353;
	private static final int ANCIENT_STATUE_FORGERY = 4354;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addGroupedDrop(20767, 68.4) // timak_orc_troop_leader
				.withDropItem(GALFREDO_ROMERS_BUST, 81.87)
				.withDropItem(SCULPTOR_BERONA, 18.13)
				.build()
			.addGroupedDrop(20768, 65.0) // timak_orc_troop_shaman
				.withDropItem(GALFREDO_ROMERS_BUST, 81.54)
				.withDropItem(SCULPTOR_BERONA, 18.46)
				.build()
			.addGroupedDrop(20769, 51.6) // timak_orc_troop_warrior
				.withDropItem(GALFREDO_ROMERS_BUST, 81.4)
				.withDropItem(SCULPTOR_BERONA, 18.6)
				.build()
			.addGroupedDrop(20770, 56.0) // timak_orc_troop_archer
				.withDropItem(GALFREDO_ROMERS_BUST, 78.57)
				.withDropItem(SCULPTOR_BERONA, 21.43)
				.build()
			.build();
	// Misc
	private static final int MIN_LEVEL = 36;
	
	public Q00355_FamilyHonor() {
		super(355, Q00355_FamilyHonor.class.getSimpleName(), "Family Honor");
		addStartNpc(GALIBREDO);
		addTalkId(GALIBREDO, PATRIN);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(GALFREDO_ROMERS_BUST);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if (qs == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30181-02.htm":
			case "30181-09.html":
			case "30929-01.html":
			case "30929-02.html": {
				htmltext = event;
				break;
			}
			case "30181-03.htm": {
				qs.startQuest();
				htmltext = event;
				break;
			}
			case "30181-06.html": {
				final long galfredoRomersBustCount = getQuestItemsCount(player, GALFREDO_ROMERS_BUST);
				
				if (galfredoRomersBustCount < 1) {
					htmltext = event;
				} else if (galfredoRomersBustCount >= 100) {
					giveAdena(player, (galfredoRomersBustCount * 120) + 7800, true);
					takeItems(player, GALFREDO_ROMERS_BUST, -1);
					htmltext = "30181-07.html";
				} else {
					giveAdena(player, (galfredoRomersBustCount * 120) + 2800, true);
					takeItems(player, GALFREDO_ROMERS_BUST, -1);
					htmltext = "30181-08.html";
				}
				break;
			}
			case "30181-10.html": {
				final long galfredoRomersBustCount = getQuestItemsCount(player, GALFREDO_ROMERS_BUST);
				
				if (galfredoRomersBustCount > 0) {
					giveAdena(player, galfredoRomersBustCount * 120, true);
				}
				
				takeItems(player, GALFREDO_ROMERS_BUST, -1);
				qs.exitQuest(true, true);
				htmltext = event;
				break;
			}
			case "30929-03.html": {
				final int random = getRandom(100);
				
				if (hasQuestItems(player, SCULPTOR_BERONA)) {
					if (random < 2) {
						giveItems(player, ANCIENT_STATUE_PROTOTYPE, 1);
						htmltext = event;
					} else if (random < 32) {
						giveItems(player, ANCIENT_STATUE_ORIGINAL, 1);
						htmltext = "30929-04.html";
					} else if (random < 62) {
						giveItems(player, ANCIENT_STATUE_REPLICA, 1);
						htmltext = "30929-05.html";
					} else if (random < 77) {
						giveItems(player, ANCIENT_STATUE_FORGERY, 1);
						htmltext = "30929-06.html";
					} else {
						htmltext = "30929-07.html";
					}
					
					takeItems(player, SCULPTOR_BERONA, 1);
				} else {
					htmltext = "30929-08.html";
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
			htmltext = (player.getLevel() >= MIN_LEVEL) ? "30181-01.htm" : "30181-04.html";
		} else if (qs.isStarted()) {
			if (npc.getId() == GALIBREDO) {
				if (hasQuestItems(player, SCULPTOR_BERONA)) {
					htmltext = "30181-11.html";
				} else {
					htmltext = "30181-05.html";
				}
			} else {
				htmltext = "30929-01.html";
			}
		}
		return htmltext;
	}
}
