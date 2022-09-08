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
package com.l2jserver.datapack.quests.Q00134_TempleMissionary;

import java.util.HashMap;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Temple Missionary (134)
 * @author malyelfik
 */
public class Q00134_TempleMissionary extends Quest {
	// NPCs
	private static final int GLYVKA = 30067;
	private static final int ROUKE = 31418;
	// Monsters
	private static final int CRUMA_MARSHLANDS_TRAITOR = 27339;
	// Items
	private static final int GIANTS_EXPERIMENTAL_TOOL_FRAGMENT = 10335;
	private static final int GIANTS_EXPERIMENTAL_TOOL = 10336;
	private static final int GIANTS_TECHNOLOGY_REPORT = 10337;
	private static final int ROUKES_REPOT = 10338;
	private static final int BADGE_TEMPLE_MISSIONARY = 10339;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20157, GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, 78.0) // Marsh Stakato
			.addSingleDrop(20229, GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, 75.0) // Stinger Wasp
			.addSingleDrop(20230, GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, 86.0) // Marsh Stakato Worker
			.addSingleDrop(20231, GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, 83.0) // Toad Lord
			.addSingleDrop(20232, GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, 81.0) // Marsh Stakato Soldier
			.addSingleDrop(20233, GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, 95.0) // Marsh Spider
			.addSingleDrop(20234, GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, 96.0) // Marsh Stakato Drone
			.build();

	// Misc
	private static final int MIN_LEVEL = 35;
	private static final int MAX_REWARD_LEVEL = 41;
	private static final int FRAGMENT_COUNT = 10;
	private static final int REPORT_COUNT = 3;
	
	public Q00134_TempleMissionary() {
		super(134, Q00134_TempleMissionary.class.getSimpleName(), "Temple Missionary");
		addStartNpc(GLYVKA);
		addTalkId(GLYVKA, ROUKE);
		addKillId(CRUMA_MARSHLANDS_TRAITOR);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, GIANTS_EXPERIMENTAL_TOOL, GIANTS_TECHNOLOGY_REPORT, ROUKES_REPOT);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = event;
		switch (event) {
			case "30067-05.html":
			case "30067-09.html":
			case "31418-07.html":
				break;
			case "30067-03.htm":
				st.startQuest();
				break;
			case "30067-06.html":
				st.setCond(2, true);
				break;
			case "31418-03.html":
				st.setCond(3, true);
				break;
			case "31418-08.html":
				st.setCond(5, true);
				st.giveItems(ROUKES_REPOT, 1);
				st.unset("talk");
				break;
			case "30067-10.html":
				st.giveItems(BADGE_TEMPLE_MISSIONARY, 1);
				st.giveAdena(15100, true);
				if (player.getLevel() < MAX_REWARD_LEVEL) {
					st.addExpAndSp(30000, 2000);
				}
				st.exitQuest(false, true);
				break;
			default:
				htmltext = null;
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		final L2PcInstance member = getRandomPartyMember(player, 3);
		if (member == null) {
			return super.onKill(npc, player, isSummon);
		}
		final QuestState st = getQuestState(member, false);
		if (npc.getId() == CRUMA_MARSHLANDS_TRAITOR) {
			st.giveItems(GIANTS_TECHNOLOGY_REPORT, 1);
			if (st.getQuestItemsCount(GIANTS_TECHNOLOGY_REPORT) >= REPORT_COUNT) {
				st.setCond(4, true);
			} else {
				st.playSound(Sound.ITEMSOUND_QUEST_ITEMGET);
			}
		} else {
			if (st.hasQuestItems(GIANTS_EXPERIMENTAL_TOOL)) {
				st.takeItems(GIANTS_EXPERIMENTAL_TOOL, 1);
				if (getRandom(100) != 0) {
					addSpawn(CRUMA_MARSHLANDS_TRAITOR, npc.getX() + 20, npc.getY() + 20, npc.getZ(), npc.getHeading(), false, 60000);
				}
			} else {
				giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		switch (npc.getId()) {
			case GLYVKA:
				switch (st.getState()) {
					case State.CREATED:
						htmltext = (player.getLevel() >= MIN_LEVEL) ? "30067-01.htm" : "30067-02.htm";
						break;
					case State.STARTED:
						switch (st.getCond()) {
							case 1:
								htmltext = "30067-04.html";
								break;
							case 2:
							case 3:
							case 4:
								htmltext = "30067-07.html";
								break;
							case 5:
								if (st.isSet("talk")) {
									htmltext = "30067-09.html";
								} else {
									st.takeItems(ROUKES_REPOT, -1);
									st.set("talk", "1");
									htmltext = "30067-08.html";
								}
								break;
						}
						break;
					case State.COMPLETED:
						htmltext = getAlreadyCompletedMsg(player);
						break;
				}
				break;
			case ROUKE:
				if (st.isStarted()) {
					switch (st.getCond()) {
						case 1:
							htmltext = "31418-01.html";
							break;
						case 2:
							htmltext = "31418-02.html";
							break;
						case 3:
							if ((st.getQuestItemsCount(GIANTS_EXPERIMENTAL_TOOL_FRAGMENT) < FRAGMENT_COUNT) && (st.getQuestItemsCount(GIANTS_TECHNOLOGY_REPORT) < REPORT_COUNT)) {
								htmltext = "31418-04.html";
							} else if (st.getQuestItemsCount(GIANTS_EXPERIMENTAL_TOOL_FRAGMENT) >= FRAGMENT_COUNT) {
								final long count = st.getQuestItemsCount(GIANTS_EXPERIMENTAL_TOOL_FRAGMENT) / 10;
								st.takeItems(GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, count * 10);
								st.giveItems(GIANTS_EXPERIMENTAL_TOOL, count);
								htmltext = "31418-05.html";
							}
							break;
						case 4:
							if (st.isSet("talk")) {
								htmltext = "31418-07.html";
							} else if (st.getQuestItemsCount(GIANTS_TECHNOLOGY_REPORT) >= REPORT_COUNT) {
								st.takeItems(GIANTS_EXPERIMENTAL_TOOL_FRAGMENT, -1);
								st.takeItems(GIANTS_EXPERIMENTAL_TOOL, -1);
								st.takeItems(GIANTS_TECHNOLOGY_REPORT, -1);
								st.set("talk", "1");
								htmltext = "31418-06.html";
							}
							break;
						case 5:
							htmltext = "31418-09.html";
							break;
					}
				}
				break;
		}
		return htmltext;
	}
}