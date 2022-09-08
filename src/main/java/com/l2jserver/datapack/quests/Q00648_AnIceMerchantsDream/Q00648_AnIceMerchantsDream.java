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
package com.l2jserver.datapack.quests.Q00648_AnIceMerchantsDream;

import com.l2jserver.datapack.quests.Q00115_TheOtherSideOfTruth.Q00115_TheOtherSideOfTruth;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestDroplist.QuestDropInfo;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * An Ice Merchant's Dream (648)
 * @author netvirus, Adry_85
 */
public final class Q00648_AnIceMerchantsDream extends Quest {
	// NPCs
	private static final int RAFFORTY = 32020;
	private static final int ICE_SHELF = 32023;
	// Items
	private static final int SILVER_HEMOCYTE = 8057;
	private static final int SILVER_ICE_CRYSTAL = 8077;
	private static final int BLACK_ICE_CRYSTAL = 8078;
	// Droplists
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22080, SILVER_ICE_CRYSTAL, 28.5).addSingleDrop(22080, SILVER_HEMOCYTE, 4.8) // Massive Maze Bandersnatch
			.addSingleDrop(22081, SILVER_ICE_CRYSTAL, 44.3) // Lost Watcher
			.addSingleDrop(22082, SILVER_ICE_CRYSTAL, 51.0) // Elder Lost Watcher
			.addSingleDrop(22083, SILVER_ICE_CRYSTAL, 47.7).addSingleDrop(22083, SILVER_HEMOCYTE, 4.9) // Baby Panthera
			.addSingleDrop(22084, SILVER_ICE_CRYSTAL, 47.7).addSingleDrop(22084, SILVER_HEMOCYTE, 4.9) // Panthera
			.addSingleDrop(22085, SILVER_ICE_CRYSTAL, 42.0).addSingleDrop(22085, SILVER_HEMOCYTE, 4.3) // Lost Gargoyle
			.addSingleDrop(22086, SILVER_ICE_CRYSTAL, 49.0).addSingleDrop(22086, SILVER_HEMOCYTE, 5.0) // Lost Gargoyle Youngling
			.addSingleDrop(22087, SILVER_ICE_CRYSTAL, 78.7).addSingleDrop(22087, SILVER_HEMOCYTE, 8.1) // Pronghorn Spirit
			.addSingleDrop(22088, SILVER_ICE_CRYSTAL, 48.0).addSingleDrop(22088, SILVER_HEMOCYTE, 4.9) // Pronghorn
			.addSingleDrop(22089, SILVER_ICE_CRYSTAL, 55.0).addSingleDrop(22089, SILVER_HEMOCYTE, 5.6) // Ice Tarantula
			.addSingleDrop(22090, SILVER_ICE_CRYSTAL, 57.0).addSingleDrop(22090, SILVER_HEMOCYTE, 5.8) // Frost Tarantula
			.addSingleDrop(22091, SILVER_ICE_CRYSTAL, 62.3) // Lost Iron Golem
			.addSingleDrop(22092, SILVER_ICE_CRYSTAL, 62.3) // Frost Iron Golem
			.addSingleDrop(22093, SILVER_ICE_CRYSTAL, 91.0).addSingleDrop(22093, SILVER_HEMOCYTE, 9.3) // Lost Buffalo
			.addSingleDrop(22094, SILVER_ICE_CRYSTAL, 55.3).addSingleDrop(22094, SILVER_HEMOCYTE, 5.7) // Frost Buffalo
			.addSingleDrop(22095, SILVER_ICE_CRYSTAL, 59.3).addSingleDrop(22095, SILVER_HEMOCYTE, 6.1) // Ursus Cub
			.addSingleDrop(22096, SILVER_ICE_CRYSTAL, 59.3).addSingleDrop(22096, SILVER_HEMOCYTE, 6.1) // Ursus
			.addSingleDrop(22097, SILVER_ICE_CRYSTAL, 69.3).addSingleDrop(22097, SILVER_HEMOCYTE, 7.1) // Lost Yeti
			.addSingleDrop(22098, SILVER_ICE_CRYSTAL, 71.7).addSingleDrop(22098, SILVER_HEMOCYTE, 7.4) // Frost Yeti
			.build();
	// Misc
	private static final int MIN_LVL = 53;

	public Q00648_AnIceMerchantsDream() {
		super(648, Q00648_AnIceMerchantsDream.class.getSimpleName(), "An Ice Merchants Dream");
		addStartNpc(RAFFORTY);
		addTalkId(RAFFORTY, ICE_SHELF);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(SILVER_HEMOCYTE, SILVER_ICE_CRYSTAL, BLACK_ICE_CRYSTAL);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		final QuestState q115 = player.getQuestState(Q00115_TheOtherSideOfTruth.class.getSimpleName());
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "ACCEPT" -> {
				st.startQuest();
				if ((q115 != null) && (q115.isCompleted())) {
					htmltext = "32020-04.htm";
				} else {
					st.setCond(2);
					htmltext = "32020-05.htm";
				}
			}
			case "ASK" -> {
				if (st.getCond() >= 1) {
					htmltext = ((q115 != null) && !q115.isCompleted()) ? "32020-14.html" : "32020-15.html";
				}
			}
			case "LATER" -> {
				if (st.getCond() >= 1) {
					htmltext = ((q115 != null) && !q115.isCompleted()) ? "32020-19.html" : "32020-20.html";
				}
			}
			case "REWARD" -> {
				if (st.getCond() >= 1) {
					final long silverCryCount = getQuestItemsCount(player, SILVER_ICE_CRYSTAL);
					final long blackCryCount = getQuestItemsCount(player, BLACK_ICE_CRYSTAL);
					if ((silverCryCount + blackCryCount) > 0) {
						giveAdena(player, (silverCryCount * 300) + (blackCryCount * 1200), true);
						takeItems(player, -1, SILVER_ICE_CRYSTAL, BLACK_ICE_CRYSTAL);
						htmltext = ((q115 != null) && !q115.isCompleted()) ? "32020-16.html" : "32020-17.html";
					} else {
						htmltext = "32020-18.html";
					}
				}
			}
			case "QUIT" -> {
				if (st.getCond() >= 1) {
					if ((q115 != null) && !q115.isCompleted()) {
						htmltext = "32020-21.html";
						st.exitQuest(true, true);
					} else {
						htmltext = "32020-22.html";
					}
				}
			}
			case "32020-06.html", "32020-07.html", "32020-08.html", "32020-09.html" -> {
				if (st.getCond() >= 1) {
					htmltext = event;
				}
			}
			case "32020-23.html" -> {
				if (st.getCond() >= 1) {
					st.exitQuest(true, true);
					htmltext = event;
				}
			}
			case "32023-04.html" -> {
				if ((st.getCond() >= 1) && hasQuestItems(player, SILVER_ICE_CRYSTAL) && (st.getInt("ex") == 0)) {
					st.set("ex", ((getRandom(4) + 1) * 10));
					htmltext = event;
				}
			}
			case "32023-05.html" -> {
				if ((st.getCond() >= 1) && hasQuestItems(player, SILVER_ICE_CRYSTAL) && (st.getInt("ex") > 0)) {
					takeItems(player, SILVER_ICE_CRYSTAL, 1);
					int val = (st.getInt("ex") + 1);
					st.set("ex", val);
					playSound(player, Sound.ITEMSOUND_BROKEN_KEY);
					htmltext = event;
				}
			}
			case "32023-06.html" -> {
				if ((st.getCond() >= 1) && hasQuestItems(player, SILVER_ICE_CRYSTAL) && (st.getInt("ex") > 0)) {
					takeItems(player, SILVER_ICE_CRYSTAL, 1);
					int val = (st.getInt("ex") + 2);
					st.set("ex", val);
					playSound(player, Sound.ITEMSOUND_BROKEN_KEY);
					htmltext = event;
				}
			}
			case "REPLY4" -> {
				if ((st.getCond() >= 1) && (st.getInt("ex") > 0)) {
					int ex = st.getInt("ex");
					int val1 = ex / 10;
					int val2 = ex - (val1 * 10);
					if (val1 == val2) {
						htmltext = "32023-07.html";
						giveItems(player, BLACK_ICE_CRYSTAL, 1);
						playSound(player, Sound.ITEMSOUND_ENCHANT_SUCCESS);
					} else {
						htmltext = "32023-08.html";
						playSound(player, Sound.ITEMSOUND_ENCHANT_FAILED);
					}
					st.set("ex", 0);
				}
			}
			case "REPLY5" -> {
				if ((st.getCond() >= 1) && (st.getInt("ex") > 0)) {
					int ex = st.getInt("ex");
					int val1 = ex / 10;
					int val2 = ((ex - (val1 * 10)) + 2);
					if (val1 == val2) {
						htmltext = "32023-07.html";
						giveItems(player, BLACK_ICE_CRYSTAL, 1);
						playSound(player, Sound.ITEMSOUND_ENCHANT_SUCCESS);
					} else {
						htmltext = "32023-08.html";
						playSound(player, Sound.ITEMSOUND_ENCHANT_FAILED);
					}
					st.set("ex", 0);
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(killer, -1, 3, npc);
		if (st != null) {
			if (st.getCond() >= 1) {
				giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
			}

			QuestDropInfo hemocyteDropInfo = DROPLIST.get(npc.getId(), SILVER_HEMOCYTE);
			if (hemocyteDropInfo != null && (st.getCond() >= 2)
					&& st.getPlayer().hasQuestCompleted(Q00115_TheOtherSideOfTruth.class.getSimpleName())) {
				giveItemRandomly(st.getPlayer(), npc, hemocyteDropInfo, true);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (npc.getId()) {
			case RAFFORTY -> {
				if (st.isCreated()) {
					if (player.getLevel() < MIN_LVL) {
						htmltext = "32020-01.htm";
					} else {
						htmltext = (player.hasQuestCompleted(Q00115_TheOtherSideOfTruth.class.getSimpleName())) ? "32020-02.htm" : "32020-03.htm";
					}
				} else if (st.isStarted()) {
					final long hasQuestItems = getQuestItemsCount(player, SILVER_ICE_CRYSTAL, BLACK_ICE_CRYSTAL);
					if (player.hasQuestCompleted(Q00115_TheOtherSideOfTruth.class.getSimpleName())) {
						htmltext = (hasQuestItems > 0) ? "32020-13.html" : "32020-11.html";
						if (st.isCond(1)) {
							st.setCond(2, true);
						}
					} else {
						htmltext = (hasQuestItems > 0) ? "32020-12.html" : "32020-10.html";
					}
				}
			}
			case ICE_SHELF -> {
				// TODO: In High Five this quest have an updated reward system.
				if (st.isStarted()) {
					if (hasQuestItems(player, SILVER_ICE_CRYSTAL)) {
						final int val = st.getInt("ex") % 10;
						if (!st.isSet("ex") || val == 0) {
							htmltext = "32023-03.html";
							st.set("ex", 0);
						} else {
							htmltext = "32023-09.html";
						}
					} else {
						htmltext = "32023-02.html";
					}
				} else {
					htmltext = "32023-01.html";
				}
			}
		}
		return htmltext;
	}
}
