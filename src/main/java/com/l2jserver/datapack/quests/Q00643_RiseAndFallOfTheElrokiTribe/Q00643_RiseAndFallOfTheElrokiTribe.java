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
package com.l2jserver.datapack.quests.Q00643_RiseAndFallOfTheElrokiTribe;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Rise and Fall of the Elroki Tribe (643)
 * @author Adry_85
 */
public class Q00643_RiseAndFallOfTheElrokiTribe extends Quest {
	// NPCs
	private static final int SINGSING = 32106;
	private static final int KARAKAWEI = 32117;
	// Item
	private static final int BONES_OF_A_PLAINS_DINOSAUR = 8776;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.bulkAddSingleDrop(BONES_OF_A_PLAINS_DINOSAUR, 111.6)
				.withNpcs(22200, 22201, 22202, 22219, 22224) // Ornithomimus
				.withNpcs(22204, 22205, 22220, 22225) // Deinonychus
				.withNpcs(22208, 22209, 22210, 22221, 22226) // Pachycephalosaurus
				.withNpcs(22211, 22212, 22213, 22222, 22227).build() // Wild Strider
			.bulkAddSingleDrop(BONES_OF_A_PLAINS_DINOSAUR, 36.0)
				.withNpcs(22742, 22744) // Ornithomimus
				.withNpcs(22743, 22745).build() // Deinonychus
			.addSingleDrop(22203, BONES_OF_A_PLAINS_DINOSAUR, 55.8) // Deinonychus
			.build();
	// Misc
	private static final int MIN_LEVEL = 75;
	private boolean isFirstTalk = true;
	// Rewards
	private static final int[] PIECE = {
		8712, // Sirra's Blade Edge
		8713, // Sword of Ipos Blade
		8714, // Barakiel's Axe Piece
		8715, // Behemoth's Tuning Fork Piece
		8716, // Naga Storm Piece
		8717, // Tiphon's Spear Edge
		8718, // Shyeed's Bow Shaft
		8719, // Sobekk's Hurricane Edge
		8720, // Themis' Tongue Piece
		8721, // Cabrio's Hand Head
		8722, // Daimon Crystal Fragment
	};
	
	public Q00643_RiseAndFallOfTheElrokiTribe() {
		super(643, Q00643_RiseAndFallOfTheElrokiTribe.class.getSimpleName(), "Rise and Fall of the Elroki Tribe");
		addStartNpc(SINGSING);
		addTalkId(SINGSING, KARAKAWEI);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(BONES_OF_A_PLAINS_DINOSAUR);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "32106-02.htm":
			case "32106-04.htm":
			case "32106-05.html":
			case "32106-10.html":
			case "32106-13.html":
			case "32117-02.html":
			case "32117-06.html":
			case "32117-07.html": {
				htmltext = event;
				break;
			}
			case "quest_accept": {
				if (player.getLevel() >= MIN_LEVEL) {
					st.startQuest();
					htmltext = "32106-03.html";
				} else {
					htmltext = "32106-07.html";
				}
				break;
			}
			case "32106-09.html": {
				st.giveAdena(1374 * st.getQuestItemsCount(BONES_OF_A_PLAINS_DINOSAUR), true);
				st.takeItems(BONES_OF_A_PLAINS_DINOSAUR, -1);
				htmltext = event;
				break;
			}
			case "exit": {
				if (!st.hasQuestItems(BONES_OF_A_PLAINS_DINOSAUR)) {
					htmltext = "32106-11.html";
				} else {
					st.giveAdena(1374 * st.getQuestItemsCount(BONES_OF_A_PLAINS_DINOSAUR), true);
					htmltext = "32106-12.html";
				}
				st.exitQuest(true, true);
				break;
			}
			case "exchange": {
				if (st.getQuestItemsCount(BONES_OF_A_PLAINS_DINOSAUR) < 300) {
					htmltext = "32117-04.html";
				} else {
					st.rewardItems(PIECE[getRandom(PIECE.length)], 5);
					st.takeItems(BONES_OF_A_PLAINS_DINOSAUR, 300);
					st.playSound(Sound.ITEMSOUND_QUEST_MIDDLE);
					htmltext = "32117-05.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(player, 1, 1, npc);
		if (st != null) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (st.getState()) {
			case State.CREATED: {
				htmltext = (player.getLevel() >= MIN_LEVEL) ? "32106-01.htm" : "32106-06.html";
				break;
			}
			case State.STARTED: {
				if (npc.getId() == SINGSING) {
					htmltext = (st.hasQuestItems(BONES_OF_A_PLAINS_DINOSAUR)) ? "32106-08.html" : "32106-14.html";
				} else if (npc.getId() == KARAKAWEI) {
					if (isFirstTalk) {
						isFirstTalk = false;
						htmltext = "32117-01.html";
					} else {
						htmltext = "32117-03.html";
					}
				}
				break;
			}
		}
		return htmltext;
	}
}
