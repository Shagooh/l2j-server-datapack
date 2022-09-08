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
package com.l2jserver.datapack.quests.Q00382_KailsMagicCoin;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.util.Util;

/**
 * Kail's Magic Coin (382)
 * @author Sdw, jurchicks
 */
public final class Q00382_KailsMagicCoin extends Quest {
	// NPCs
	private static final int VERGARA = 30687;
	// Monsters
	private static final int FALLEN_ORC = 21017;
	private static final int FALLEN_ORC_ARCHER = 21019;
	private static final int FALLEN_ORC_SHAMAN = 21020;
	private static final int FALLEN_ORC_CAPTAIN = 21022;
	// Items
	private static final int ROYAL_MEMBERSHIP = 5898;
	private static final int KAILS_SILVER_BASILISK = 5961;
	private static final int KAILS_GOLD_GOLEM = 5962;
	private static final int KAILS_BLOOD_DRAGON = 5963;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addGroupedDrop(FALLEN_ORC_CAPTAIN, 6.9)
				.withDropItem(KAILS_SILVER_BASILISK, 33.333)
				.withDropItem(KAILS_GOLD_GOLEM, 33.333)
				.withDropItem(KAILS_BLOOD_DRAGON, 33.334).build()
			.addSingleDrop(FALLEN_ORC, KAILS_SILVER_BASILISK, 7.3)
			.addSingleDrop(FALLEN_ORC_ARCHER, KAILS_GOLD_GOLEM, 7.5)
			.addSingleDrop(FALLEN_ORC_SHAMAN, KAILS_BLOOD_DRAGON, 7.3)
			.build();
	// Misc
	private static final int MIN_LVL = 55;
	
	public Q00382_KailsMagicCoin() {
		super(382, Q00382_KailsMagicCoin.class.getSimpleName(), "Kail's Magic Coin");
		addStartNpc(VERGARA);
		addTalkId(VERGARA);
		addKillId(FALLEN_ORC, FALLEN_ORC_ARCHER, FALLEN_ORC_SHAMAN, FALLEN_ORC_CAPTAIN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs == null) {
			return htmltext;
		}
		
		switch (event) {
			case "30687-03.htm": {
				if (qs.isCreated()) {
					qs.startQuest();
					htmltext = event;
				}
				break;
			}
			case "30687-05.htm":
			case "30687-06.htm": {
				if (qs.isStarted()) {
					htmltext = event;
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		final QuestState qs = getQuestState(talker, true);
		String htmltext = getNoQuestMsg(talker);
		if (qs.isCreated()) {
			htmltext = (((talker.getLevel() >= MIN_LVL) && hasQuestItems(talker, ROYAL_MEMBERSHIP)) ? "30687-02.htm" : "30687-01.htm");
		} else if (qs.isStarted()) {
			htmltext = "30687-04.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getQuestState(killer, false);
		if ((qs != null) && hasQuestItems(qs.getPlayer(), ROYAL_MEMBERSHIP) && Util.checkIfInRange(1500, npc, qs.getPlayer(), true)) {
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
}
