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
package com.l2jserver.datapack.quests.Q00633_InTheForgottenVillage;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestDroplist.QuestDropInfo;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * In The Forgotten Village (633)
 * @author netvirus
 */
public final class Q00633_InTheForgottenVillage extends Quest {
	// NPC
	private static final int MINA = 31388;
	// Items
	private static final QuestItemChanceHolder RIB_BONE_OF_A_BLACK_MAGUS = new QuestItemChanceHolder(7544, 200L);
	private static final int ZOMBIES_LIVER = 7545;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21553, ZOMBIES_LIVER, 41.7) // Trampled Man
			.addSingleDrop(21554, ZOMBIES_LIVER, 41.7) // Trampled Man
			.addSingleDrop(21557, RIB_BONE_OF_A_BLACK_MAGUS, 39.4) // Bone Snatcher
			.addSingleDrop(21558, RIB_BONE_OF_A_BLACK_MAGUS, 39.4) // Bone Snatcher
			.addSingleDrop(21559, RIB_BONE_OF_A_BLACK_MAGUS, 43.6) // Bone Maker
			.addSingleDrop(21560, RIB_BONE_OF_A_BLACK_MAGUS, 43.0) // Bone Shaper
			.addSingleDrop(21561, ZOMBIES_LIVER, 53.8) // Sacrificed Man
			.addSingleDrop(21563, RIB_BONE_OF_A_BLACK_MAGUS, 43.6) // Bone Collector
			.addSingleDrop(21564, RIB_BONE_OF_A_BLACK_MAGUS, 41.4) // Skull Collector
			.addSingleDrop(21565, RIB_BONE_OF_A_BLACK_MAGUS, 42.0) // Bone Animator
			.addSingleDrop(21566, RIB_BONE_OF_A_BLACK_MAGUS, 46.0) // Skull Animator
			.addSingleDrop(21567, RIB_BONE_OF_A_BLACK_MAGUS, 54.9) // Bone Slayer
			.addSingleDrop(21570, ZOMBIES_LIVER, 50.8) // Ghost of Betrayer
			.addSingleDrop(21572, RIB_BONE_OF_A_BLACK_MAGUS, 46.5) // Bone Sweeper
			.addSingleDrop(21574, RIB_BONE_OF_A_BLACK_MAGUS, 58.6) // Bone Grinder
			.addSingleDrop(21575, RIB_BONE_OF_A_BLACK_MAGUS, 32.9) // Bone Grinder
			.addSingleDrop(21578, ZOMBIES_LIVER, 64.9) // Behemoth Zombie
			.addSingleDrop(21580, RIB_BONE_OF_A_BLACK_MAGUS, 46.2) // Bone Caster
			.addSingleDrop(21581, RIB_BONE_OF_A_BLACK_MAGUS, 50.5) // Bone Puppeteer
			.addSingleDrop(21583, RIB_BONE_OF_A_BLACK_MAGUS, 47.5) // Bone Scavenger
			.addSingleDrop(21584, RIB_BONE_OF_A_BLACK_MAGUS, 47.5) // Bone Scavenger
			.addSingleDrop(21596, RIB_BONE_OF_A_BLACK_MAGUS, 54.3) // Requiem Lord
			.addSingleDrop(21597, ZOMBIES_LIVER, 51.0) // Requiem Behemoth
			.addSingleDrop(21598, ZOMBIES_LIVER, 57.2) // Requiem Behemoth
			.addSingleDrop(21599, RIB_BONE_OF_A_BLACK_MAGUS, 58.0) // Requiem Priest
			.addSingleDrop(21600, ZOMBIES_LIVER, 56.1) // Requiem Behemoth
			.addSingleDrop(21601, RIB_BONE_OF_A_BLACK_MAGUS, 67.7) // Requiem Behemoth
			.build();
	// Misc
	private static final int MIN_LVL = 65;

	public Q00633_InTheForgottenVillage() {
		super(633, Q00633_InTheForgottenVillage.class.getSimpleName(), "In The Forgotten Village");
		addStartNpc(MINA);
		addTalkId(MINA);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(RIB_BONE_OF_A_BLACK_MAGUS.getId(), ZOMBIES_LIVER);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs == null) {
			return htmltext;
		}
		
		switch (event) {
			case "31388-03.htm": {
				if (qs.isCreated()) {
					qs.startQuest();
					htmltext = event;
				}
				break;
			}
			case "31388-04.html":
			case "31388-05.html":
			case "31388-06.html": {
				if (qs.isStarted()) {
					htmltext = event;
				}
				break;
			}
			case "31388-07.html": {
				if (qs.isCond(2)) {
					if (hasItemsAtLimit(player, RIB_BONE_OF_A_BLACK_MAGUS)) {
						giveAdena(player, 25000, true);
						addExpAndSp(player, 305235, 0);
						takeItems(player, RIB_BONE_OF_A_BLACK_MAGUS.getId(), -1);
						qs.setCond(1, true);
						htmltext = event;
					} else {
						htmltext = "31388-08.html";
					}
				}
				break;
			}
			case "31388-09.html": {
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
		final QuestState qs = getRandomPartyMemberState(killer, -1, 3, npc);
		if (qs != null) {
			QuestDropInfo dropInfo = DROPLIST.get(npc);
			if (RIB_BONE_OF_A_BLACK_MAGUS.getId() == dropInfo.item().getId() && qs.isCond(1)
					&& giveItemRandomly(qs.getPlayer(), npc, dropInfo, true)) {
				qs.setCond(2);
			} else {
				giveItemRandomly(qs.getPlayer(), npc, dropInfo, true);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LVL) ? "31388-01.htm" : "31388-02.htm");
		} else if (qs.isStarted()) {
			htmltext = hasItemsAtLimit(player, RIB_BONE_OF_A_BLACK_MAGUS) ? "31388-04.html" : "31388-05.html";
		}
		return htmltext;
	}
}
