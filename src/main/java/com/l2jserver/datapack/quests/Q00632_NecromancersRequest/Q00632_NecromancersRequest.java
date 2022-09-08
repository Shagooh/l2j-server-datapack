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
package com.l2jserver.datapack.quests.Q00632_NecromancersRequest;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Necromancer's Request (632)
 * @author Zoey76
 */
public final class Q00632_NecromancersRequest extends Quest {
	// NPC
	private static final int MYSTERIOUS_WIZARD = 31522;
	// Items
	private static final int VAMPIRES_HEART = 7542;
	private static final int ZOMBIES_BRAIN = 7543;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21547, ZOMBIES_BRAIN, 56.5) // Corrupted Knight
			.addSingleDrop(21548, ZOMBIES_BRAIN, 48.4) // Resurrected Knight
			.addSingleDrop(21549, ZOMBIES_BRAIN, 58.5) // Corrupted Guard
			.addSingleDrop(21550, ZOMBIES_BRAIN, 59.7) // Corrupted Guard
			.addSingleDrop(21551, ZOMBIES_BRAIN, 67.3) // Resurrected Guard
			.addSingleDrop(21552, ZOMBIES_BRAIN, 63.7) // Resurrected Guard
			.addSingleDrop(21555, ZOMBIES_BRAIN, 57.5) // Slaughter Executioner
			.addSingleDrop(21556, ZOMBIES_BRAIN, 56.0) // Slaughter Executioner
			.addSingleDrop(21562, ZOMBIES_BRAIN, 63.1) // Guillotine's Ghost
			.addSingleDrop(21571, ZOMBIES_BRAIN, 75.8) // Ghost of Rebellion Soldier
			.addSingleDrop(21576, ZOMBIES_BRAIN, 64.7) // Ghost of Guillotine
			.addSingleDrop(21577, ZOMBIES_BRAIN, 62.5) // Ghost of Guillotine
			.addSingleDrop(21579, ZOMBIES_BRAIN, 76.6) // Ghost of Rebellion Leader
			.addSingleDrop(21568, VAMPIRES_HEART, 45.2) // Devil Bat
			.addSingleDrop(21569, VAMPIRES_HEART, 48.4) // Devil Bat
			.addSingleDrop(21573, VAMPIRES_HEART, 49.9) // Atrox
			.addSingleDrop(21582, VAMPIRES_HEART, 52.2) // Vampire Soldier
			.addSingleDrop(21585, VAMPIRES_HEART, 41.3) // Vampire Magician
			.addSingleDrop(21586, VAMPIRES_HEART, 49.6) // Vampire Adept
			.addSingleDrop(21587, VAMPIRES_HEART, 51.9) // Vampire Warrior
			.addSingleDrop(21588, VAMPIRES_HEART, 42.8) // Vampire Wizard
			.addSingleDrop(21589, VAMPIRES_HEART, 43.9) // Vampire Wizard
			.addSingleDrop(21590, VAMPIRES_HEART, 42.8) // Vampire Magister
			.addSingleDrop(21591, VAMPIRES_HEART, 50.2) // Vampire Magister
			.addSingleDrop(21592, VAMPIRES_HEART, 37.0) // Vampire Magister
			.addSingleDrop(21593, VAMPIRES_HEART, 59.2) // Vampire Warlord
			.addSingleDrop(21594, VAMPIRES_HEART, 55.4) // Vampire Warlord
			.addSingleDrop(21595, VAMPIRES_HEART, 39.2) // Vampire Warlord
			.build();
	// Misc
	private static final int MIN_LEVEL = 63;
	private static final int REQUIRED_ITEM_COUNT = 200;
	private static final int ADENA_REWARD = 120000;
	
	public Q00632_NecromancersRequest() {
		super(632, Q00632_NecromancersRequest.class.getSimpleName(), "Necromancer's Request");
		addStartNpc(MYSTERIOUS_WIZARD);
		addTalkId(MYSTERIOUS_WIZARD);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(VAMPIRES_HEART, ZOMBIES_BRAIN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if (qs == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "31522-104.htm": {
				if (player.getLevel() >= MIN_LEVEL) {
					qs.startQuest();
					qs.setMemoState(11);
					htmltext = event;
				}
				break;
			}
			case "31522-201.html": {
				htmltext = event;
				break;
			}
			case "31522-202.html": {
				if (getQuestItemsCount(player, VAMPIRES_HEART) >= REQUIRED_ITEM_COUNT) {
					takeItems(player, VAMPIRES_HEART, -1);
					giveAdena(player, ADENA_REWARD, true);
					qs.setMemoState(11);
					htmltext = event;
				} else {
					htmltext = "31522-203.html";
				}
				break;
			}
			case "31522-204.html": {
				takeItems(player, VAMPIRES_HEART, -1);
				qs.exitQuest(true, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(player, -1, 3, npc);
		if (qs != null) {
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
				
			if (getQuestItemsCount(player, VAMPIRES_HEART) >= REQUIRED_ITEM_COUNT) {
				qs.setCond(2);
				qs.setMemoState(12);
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			htmltext = player.getLevel() >= MIN_LEVEL ? "31522-101.htm" : "31522-103.htm";
		} else if (qs.isStarted()) {
			if (qs.isMemoState(11)) {
				htmltext = "31522-106.html";
			} else if (qs.isMemoState(12) && (getQuestItemsCount(player, VAMPIRES_HEART) >= REQUIRED_ITEM_COUNT)) {
				htmltext = "31522-105.html";
			}
		}
		return htmltext;
	}
}
