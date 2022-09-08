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
package com.l2jserver.datapack.quests.Q00385_YokeOfThePast;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Yoke of the Past (385)
 * @author Pandragon
 */
public final class Q00385_YokeOfThePast extends Quest {
	// NPCs
	// @formatter:off
	private static final int[] ZIGGURATS = {
		31095, 31096, 31097, 31098, 31099, 31100, 31101,
		31102, 31103, 31104, 31105, 31106, 31107, 31108,
		31109, 31110, 31114, 31115, 31116, 31117, 31118,
		31119, 31120, 31121, 31122, 31123, 31124, 31125
	};
	// @formatter:on
	// Item
	private static final int SCROLL_OF_ANCIENT_MAGIC = 5902;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21144, SCROLL_OF_ANCIENT_MAGIC, 30.6) // Catacomb Shadow
			.addSingleDrop(21156, SCROLL_OF_ANCIENT_MAGIC, 99.4) // Purgatory Shadow
			.addSingleDrop(21208, SCROLL_OF_ANCIENT_MAGIC, 14.6) // Hallowed Watchman
			.addSingleDrop(21209, SCROLL_OF_ANCIENT_MAGIC, 16.6) // Hallowed Seer
			.addSingleDrop(21210, SCROLL_OF_ANCIENT_MAGIC, 20.2) // Vault Guardian
			.addSingleDrop(21211, SCROLL_OF_ANCIENT_MAGIC, 21.2) // Vault Seer
			.addSingleDrop(21213, SCROLL_OF_ANCIENT_MAGIC, 27.4) // Hallowed Monk
			.addSingleDrop(21214, SCROLL_OF_ANCIENT_MAGIC, 34.2) // Vault Sentinel
			.addSingleDrop(21215, SCROLL_OF_ANCIENT_MAGIC, 36.0) // Vault Monk
			.addSingleDrop(21217, SCROLL_OF_ANCIENT_MAGIC, 46.0) // Hallowed Priest
			.addSingleDrop(21218, SCROLL_OF_ANCIENT_MAGIC, 55.8) // Vault Overlord
			.addSingleDrop(21219, SCROLL_OF_ANCIENT_MAGIC, 57.8) // Vault Priest
			.addSingleDrop(21221, SCROLL_OF_ANCIENT_MAGIC, 71.0) // Sepulcher Inquisitor
			.addSingleDrop(21222, SCROLL_OF_ANCIENT_MAGIC, 84.2) // Sepulcher Archon
			.addSingleDrop(21223, SCROLL_OF_ANCIENT_MAGIC, 86.2) // Sepulcher Inquisitor
			.addSingleDrop(21224, SCROLL_OF_ANCIENT_MAGIC, 94.0) // Sepulcher Guardian
			.addSingleDrop(21225, SCROLL_OF_ANCIENT_MAGIC, 97.0) // Sepulcher Sage
			.addSingleDrop(21226, SCROLL_OF_ANCIENT_MAGIC, 20.2) // Sepulcher Guardian
			.addSingleDrop(21227, SCROLL_OF_ANCIENT_MAGIC, 29.0) // Sepulcher Sage
			.addSingleDrop(21228, SCROLL_OF_ANCIENT_MAGIC, 31.6) // Sepulcher Guard
			.addSingleDrop(21229, SCROLL_OF_ANCIENT_MAGIC, 42.6) // Sepulcher Preacher
			.addSingleDrop(21230, SCROLL_OF_ANCIENT_MAGIC, 64.6) // Sepulcher Guard
			.addSingleDrop(21231, SCROLL_OF_ANCIENT_MAGIC, 65.4) // Sepulcher Preacher
			.addSingleDrop(21236, SCROLL_OF_ANCIENT_MAGIC, 23.8) // Barrow Sentinel
			.addSingleDrop(21237, SCROLL_OF_ANCIENT_MAGIC, 27.4) // Barrow Monk
			.addSingleDrop(21238, SCROLL_OF_ANCIENT_MAGIC, 34.2) // Grave Sentinel
			.addSingleDrop(21239, SCROLL_OF_ANCIENT_MAGIC, 36.0) // Grave Monk
			.addSingleDrop(21240, SCROLL_OF_ANCIENT_MAGIC, 41.0) // Barrow Overlord
			.addSingleDrop(21241, SCROLL_OF_ANCIENT_MAGIC, 46.0) // Barrow Priest
			.addSingleDrop(21242, SCROLL_OF_ANCIENT_MAGIC, 55.8) // Grave Overlord
			.addSingleDrop(21243, SCROLL_OF_ANCIENT_MAGIC, 57.8) // Grave Priest
			.addSingleDrop(21244, SCROLL_OF_ANCIENT_MAGIC, 64.2) // Crypt Archon
			.addSingleDrop(21245, SCROLL_OF_ANCIENT_MAGIC, 70.0) // Crypt Inquisitor
			.addSingleDrop(21246, SCROLL_OF_ANCIENT_MAGIC, 84.2) // Tomb Archon
			.addSingleDrop(21247, SCROLL_OF_ANCIENT_MAGIC, 86.2) // Tomb Inquisitor
			.addSingleDrop(21248, SCROLL_OF_ANCIENT_MAGIC, 94.0) // Crypt Guardian
			.addSingleDrop(21249, SCROLL_OF_ANCIENT_MAGIC, 97.0) // Crypt Sage
			.addSingleDrop(21250, SCROLL_OF_ANCIENT_MAGIC, 79.8) // Tomb Guardian
			.addSingleDrop(21251, SCROLL_OF_ANCIENT_MAGIC, 71.0) // Tomb Sage
			.addSingleDrop(21252, SCROLL_OF_ANCIENT_MAGIC, 68.4) // Crypt Guard
			.addSingleDrop(21253, SCROLL_OF_ANCIENT_MAGIC, 57.4) // Crypt Preacher
			.addSingleDrop(21254, SCROLL_OF_ANCIENT_MAGIC, 35.4) // Tomb Guard
			.addSingleDrop(21255, SCROLL_OF_ANCIENT_MAGIC, 25.0) // Tomb Preacher
			.build();
	// Reward
	private static final int BLANK_SCROLL = 5965;
	// Misc
	private static final int MIN_LVL = 20;
	
	public Q00385_YokeOfThePast() {
		super(385, Q00385_YokeOfThePast.class.getSimpleName(), "Yoke of the Past");
		addStartNpc(ZIGGURATS);
		addTalkId(ZIGGURATS);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(SCROLL_OF_ANCIENT_MAGIC);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs != null) {
			switch (event) {
				case "ziggurat-03.htm":
				case "ziggurat-04.htm":
				case "ziggurat-06.htm":
				case "ziggurat-07.htm": {
					htmltext = event;
					break;
				}
				case "ziggurat-05.htm": {
					if (qs.isCreated()) {
						qs.startQuest();
						htmltext = event;
					}
					break;
				}
				case "ziggurat-10.html": {
					qs.exitQuest(true, true);
					htmltext = event;
					break;
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		final QuestState qs = getQuestState(talker, true);
		String htmltext = getNoQuestMsg(talker);
		switch (qs.getState()) {
			case State.CREATED: {
				htmltext = (talker.getLevel() >= MIN_LVL) ? "ziggurat-01.htm" : "ziggurat-02.htm";
				break;
			}
			case State.STARTED: {
				if (hasQuestItems(talker, SCROLL_OF_ANCIENT_MAGIC)) {
					rewardItems(talker, BLANK_SCROLL, getQuestItemsCount(talker, SCROLL_OF_ANCIENT_MAGIC));
					takeItems(talker, SCROLL_OF_ANCIENT_MAGIC, -1);
					htmltext = "ziggurat-09.html";
				} else {
					htmltext = "ziggurat-08.html";
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
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
}
