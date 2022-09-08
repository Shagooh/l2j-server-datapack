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
package com.l2jserver.datapack.quests.Q00222_TestOfTheDuelist;

import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.gameserver.util.Util;

/**
 * Test Of The Duelist (222)
 * @author ivantotov
 */
public final class Q00222_TestOfTheDuelist extends Quest {
	// NPC
	private static final int DUELIST_KAIEN = 30623;
	// Monster
	private static final int PUNCHER = 20085;
	private static final int NOBLE_ANT_LEADER = 20090;
	private static final int DEAD_SEEKER = 20202;
	private static final int EXCURO = 20214;
	private static final int KRATOR = 20217;
	private static final int MARSH_STAKATO_DRONE = 20234;
	private static final int BREKA_ORC_OVERLORD = 20270;
	private static final int FETTERED_SOUL = 20552;
	private static final int GRANDIS = 20554;
	private static final int ENCHANTED_MONSTEREYE = 20564;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	private static final int TIMAK_ORC_OVERLORD = 20588;
	private static final int TAMLIN_ORC = 20601;
	private static final int TAMLIN_ORC_ARCHER = 20602;
	private static final int LAKIN = 20604;
	// Items
	private static final int ORDER_GLUDIO = 2763;
	private static final int ORDER_DION = 2764;
	private static final int ORDER_GIRAN = 2765;
	private static final int ORDER_OREN = 2766;
	private static final int ORDER_ADEN = 2767;
	private static final int FINAL_ORDER = 2778;
	private static final QuestItemChanceHolder PUNCHERS_SHARD = new QuestItemChanceHolder(2768, 10L);
	private static final QuestItemChanceHolder NOBLE_ANTS_FEELER = new QuestItemChanceHolder(2769, 10L);
	private static final QuestItemChanceHolder DRONES_CHITIN = new QuestItemChanceHolder(2770, 10L);
	private static final QuestItemChanceHolder DEAD_SEEKER_FANG = new QuestItemChanceHolder(2771, 10L);
	private static final QuestItemChanceHolder OVERLORD_NECKLACE = new QuestItemChanceHolder(2772, 10L);
	private static final QuestItemChanceHolder FETTERED_SOULS_CHAIN = new QuestItemChanceHolder(2773, 10L);
	private static final QuestItemChanceHolder CHIEFS_AMULET = new QuestItemChanceHolder(2774, 10L);
	private static final QuestItemChanceHolder ENCHANTED_EYE_MEAT = new QuestItemChanceHolder(2775, 10L);
	private static final QuestItemChanceHolder TAMLIN_ORCS_RING = new QuestItemChanceHolder(2776, 10L);
	private static final QuestItemChanceHolder TAMRIN_ORCS_ARROW = new QuestItemChanceHolder(2777, 10L);
	private static final QuestItemChanceHolder EXCUROS_SKIN = new QuestItemChanceHolder(2779, 3L);
	private static final QuestItemChanceHolder KRATORS_SHARD = new QuestItemChanceHolder(2780, 3L);
	private static final QuestItemChanceHolder GRANDIS_SKIN = new QuestItemChanceHolder(2781, 3L);
	private static final QuestItemChanceHolder TIMAK_ORCS_BELT = new QuestItemChanceHolder(2782, 3L);
	private static final QuestItemChanceHolder LAKINS_MACE = new QuestItemChanceHolder(2783, 3L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(PUNCHER, PUNCHERS_SHARD)
			.addSingleDrop(NOBLE_ANT_LEADER, NOBLE_ANTS_FEELER)
			.addSingleDrop(DEAD_SEEKER, DEAD_SEEKER_FANG)
			.addSingleDrop(EXCURO, EXCUROS_SKIN)
			.addSingleDrop(KRATOR, KRATORS_SHARD)
			.addSingleDrop(MARSH_STAKATO_DRONE, DRONES_CHITIN)
			.addSingleDrop(BREKA_ORC_OVERLORD, OVERLORD_NECKLACE)
			.addSingleDrop(FETTERED_SOUL, FETTERED_SOULS_CHAIN)
			.addSingleDrop(GRANDIS, GRANDIS_SKIN)
			.addSingleDrop(ENCHANTED_MONSTEREYE, ENCHANTED_EYE_MEAT)
			.addSingleDrop(LETO_LIZARDMAN_OVERLORD, CHIEFS_AMULET)
			.addSingleDrop(TIMAK_ORC_OVERLORD, TIMAK_ORCS_BELT)
			.addSingleDrop(TAMLIN_ORC, TAMLIN_ORCS_RING)
			.addSingleDrop(TAMLIN_ORC_ARCHER, TAMRIN_ORCS_ARROW)
			.addSingleDrop(LAKIN, LAKINS_MACE)
			.build();
	// Reward
	private static final int MARK_OF_DUELIST = 2762;
	private static final int DIMENSIONAL_DIAMOND = 7562;
	// Misc
	private static final int MIN_LEVEL = 39;
	
	public Q00222_TestOfTheDuelist() {
		super(222, Q00222_TestOfTheDuelist.class.getSimpleName(), "Test Of The Duelist");
		addStartNpc(DUELIST_KAIEN);
		addTalkId(DUELIST_KAIEN);
		addKillId(PUNCHER, NOBLE_ANT_LEADER, DEAD_SEEKER, EXCURO, KRATOR, MARSH_STAKATO_DRONE, BREKA_ORC_OVERLORD, FETTERED_SOUL, GRANDIS, ENCHANTED_MONSTEREYE, LETO_LIZARDMAN_OVERLORD, TIMAK_ORC_OVERLORD, TAMLIN_ORC, TAMLIN_ORC_ARCHER, LAKIN);
		registerQuestItems(ORDER_GLUDIO, ORDER_DION, ORDER_GIRAN, ORDER_OREN, ORDER_ADEN, PUNCHERS_SHARD.getId(), NOBLE_ANTS_FEELER.getId(), DRONES_CHITIN.getId(), DEAD_SEEKER_FANG.getId(), OVERLORD_NECKLACE.getId(), FETTERED_SOULS_CHAIN.getId(), CHIEFS_AMULET.getId(), ENCHANTED_EYE_MEAT.getId(), TAMLIN_ORCS_RING.getId(), TAMRIN_ORCS_ARROW.getId(), FINAL_ORDER, EXCUROS_SKIN.getId(), KRATORS_SHARD.getId(), GRANDIS_SKIN.getId(), TIMAK_ORCS_BELT.getId(), LAKINS_MACE.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if (qs == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "ACCEPT": {
				if (qs.isCreated()) {
					qs.startQuest();
					qs.setMemoState(1);
					giveItems(player, ORDER_GLUDIO, 1);
					giveItems(player, ORDER_DION, 1);
					giveItems(player, ORDER_GIRAN, 1);
					giveItems(player, ORDER_OREN, 1);
					giveItems(player, ORDER_ADEN, 1);
					playSound(player, Sound.ITEMSOUND_QUEST_MIDDLE);
					if (player.getVariables().getInt("2ND_CLASS_DIAMOND_REWARD", 0) == 0) {
						if (player.getClassId() == ClassId.palusKnight) {
							giveItems(player, DIMENSIONAL_DIAMOND, 104);
						} else {
							giveItems(player, DIMENSIONAL_DIAMOND, 72);
						}
						player.getVariables().set("2ND_CLASS_DIAMOND_REWARD", 1);
						htmltext = "30623-07a.htm";
					} else {
						htmltext = "30623-07.htm";
					}
				}
				break;
			}
			case "30623-04.htm": {
				if (player.getRace() != Race.ORC) {
					htmltext = event;
				} else {
					htmltext = "30623-05.htm";
				}
				break;
			}
			case "30623-06.htm":
			case "30623-07.html":
			case "30623-09.html":
			case "30623-10.html":
			case "30623-11.html":
			case "30623-12.html":
			case "30623-15.html": {
				htmltext = event;
				break;
			}
			case "30623-08.html": {
				qs.setCond(2, true);
				htmltext = event;
				break;
			}
			case "30623-16.html": {
				takeItems(player, PUNCHERS_SHARD.getId(), -1);
				takeItems(player, NOBLE_ANTS_FEELER.getId(), -1);
				takeItems(player, DEAD_SEEKER_FANG.getId(), -1);
				takeItems(player, DRONES_CHITIN.getId(), -1);
				takeItems(player, OVERLORD_NECKLACE.getId(), -1);
				takeItems(player, FETTERED_SOULS_CHAIN.getId(), -1);
				takeItems(player, CHIEFS_AMULET.getId(), -1);
				takeItems(player, ENCHANTED_EYE_MEAT.getId(), -1);
				takeItems(player, TAMLIN_ORCS_RING.getId(), -1);
				takeItems(player, TAMRIN_ORCS_ARROW.getId(), -1);
				takeItems(player, ORDER_GLUDIO, 1);
				takeItems(player, ORDER_DION, 1);
				takeItems(player, ORDER_GIRAN, 1);
				takeItems(player, ORDER_OREN, 1);
				takeItems(player, ORDER_ADEN, 1);
				giveItems(player, FINAL_ORDER, 1);
				qs.setMemoState(2);
				qs.setCond(4, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getQuestState(killer, false);
		if ((qs != null) && qs.isStarted() && Util.checkIfInRange(1500, npc, killer, true)) {
			switch (npc.getId()) {
				case PUNCHER, NOBLE_ANT_LEADER ->
						handleDropsAndQuestStateUpdate(qs, npc, 1, ORDER_GLUDIO, hasAllDrops(qs.getPlayer()), 9, 3);
				case DEAD_SEEKER, MARSH_STAKATO_DRONE ->
						handleDropsAndQuestStateUpdate(qs, npc, 1, ORDER_DION, hasAllDrops(qs.getPlayer()), 9, 3);
				case BREKA_ORC_OVERLORD, FETTERED_SOUL ->
						handleDropsAndQuestStateUpdate(qs, npc, 1, ORDER_GIRAN, hasAllDrops(qs.getPlayer()), 9, 3);
				case ENCHANTED_MONSTEREYE, LETO_LIZARDMAN_OVERLORD ->
						handleDropsAndQuestStateUpdate(qs, npc, 1, ORDER_OREN, hasAllDrops(qs.getPlayer()), 9, 3);
				case TAMLIN_ORC, TAMLIN_ORC_ARCHER ->
						handleDropsAndQuestStateUpdate(qs, npc, 1, ORDER_ADEN, hasAllDrops(qs.getPlayer()), 9, 3);
				case EXCURO, KRATOR, GRANDIS, TIMAK_ORC_OVERLORD, LAKIN ->
						handleDropsAndQuestStateUpdate(qs, npc, 2, FINAL_ORDER, hasAllFinalOrderDrops(qs.getPlayer()), 5, 5);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}

	private void handleDropsAndQuestStateUpdate(QuestState qs, L2Npc npc, int requiredMemoState, int requiredItemId, boolean hasAllDrops, int memoStateForUpdate, int condToSet) {
		if (qs.isMemoState(requiredMemoState) && hasQuestItems(qs.getPlayer(), requiredItemId)) {
			final int memoState = qs.getMemoStateEx(1);
			qs.setMemoStateEx(1, memoState + 1);
			if (giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true) && hasAllDrops) {
				if (memoState >= memoStateForUpdate) {
					qs.setCond(condToSet);
				}
				qs.setMemoStateEx(1, 0);
			}
		}
	}

	private boolean hasAllDrops(L2PcInstance player) {
		return hasItemsAtLimit(player, PUNCHERS_SHARD, NOBLE_ANTS_FEELER, DRONES_CHITIN, DEAD_SEEKER_FANG, OVERLORD_NECKLACE, FETTERED_SOULS_CHAIN, CHIEFS_AMULET, ENCHANTED_EYE_MEAT, TAMLIN_ORCS_RING, TAMRIN_ORCS_ARROW);
	}

	private boolean hasAllFinalOrderDrops(L2PcInstance player) {
		return hasItemsAtLimit(player, EXCUROS_SKIN, KRATORS_SHARD, LAKINS_MACE, GRANDIS_SKIN, TIMAK_ORCS_BELT);
	}

	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			if ((player.getClassId() == ClassId.warrior) || (player.getClassId() == ClassId.elvenKnight) || (player.getClassId() == ClassId.palusKnight) || (player.getClassId() == ClassId.orcMonk)) {
				if (player.getLevel() >= MIN_LEVEL) {
					htmltext = "30623-03.htm";
				} else {
					htmltext = "30623-01.html";
				}
			} else {
				htmltext = "30623-02.html";
			}
		} else if (qs.isStarted()) {
			if (hasQuestItems(player, ORDER_GLUDIO, ORDER_DION, ORDER_GIRAN, ORDER_OREN, ORDER_ADEN)) {
				if (hasAllDrops(player)) {
					htmltext = "30623-13.html";
				} else {
					htmltext = "30623-14.html";
				}
			} else if (hasQuestItems(player, FINAL_ORDER)) {
				if (hasAllFinalOrderDrops(player)) {
					giveAdena(player, 161806, true);
					giveItems(player, MARK_OF_DUELIST, 1);
					addExpAndSp(player, 894888, 61408);
					qs.exitQuest(false, true);
					player.sendPacket(new SocialAction(player.getObjectId(), 3));
					htmltext = "30623-18.html";
				} else {
					htmltext = "30623-17.html";
				}
			}
		} else if (qs.isCompleted()) {
			htmltext = getAlreadyCompletedMsg(player);
		}
		return htmltext;
	}
}
