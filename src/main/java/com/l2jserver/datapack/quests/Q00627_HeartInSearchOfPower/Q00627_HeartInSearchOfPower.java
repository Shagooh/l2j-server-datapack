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
package com.l2jserver.datapack.quests.Q00627_HeartInSearchOfPower;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Heart in Search of Power (627)
 * @author Citizen
 */
public class Q00627_HeartInSearchOfPower extends Quest {
	// NPCs
	private static final int MYSTERIOUS_NECROMANCER = 31518;
	private static final int ENFEUX = 31519;
	// Items
	private static final int SEAL_OF_LIGHT = 7170;
	private static final int GEM_OF_SAINTS = 7172;
	private static final QuestItemChanceHolder BEAD_OF_OBEDIENCE = new QuestItemChanceHolder(7171, 300L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21520, BEAD_OF_OBEDIENCE, 66.1) // Eye of Splendor
			.addSingleDrop(21523, BEAD_OF_OBEDIENCE, 66.8) // Flash of Splendor
			.addSingleDrop(21524, BEAD_OF_OBEDIENCE, 71.4) // Blade of Splendor
			.addSingleDrop(21525, BEAD_OF_OBEDIENCE, 71.4) // Blade of Splendor
			.addSingleDrop(21526, BEAD_OF_OBEDIENCE, 79.6) // Wisdom of Splendor
			.addSingleDrop(21529, BEAD_OF_OBEDIENCE, 65.9) // Soul of Splendor
			.addSingleDrop(21530, BEAD_OF_OBEDIENCE, 70.4) // Victory of Splendor
			.addSingleDrop(21531, BEAD_OF_OBEDIENCE, 79.1) // Punishment of Splendor
			.addSingleDrop(21532, BEAD_OF_OBEDIENCE, 82.0) // Shout of Splendor
			.addSingleDrop(21535, BEAD_OF_OBEDIENCE, 82.7) // Signet of Splendor
			.addSingleDrop(21536, BEAD_OF_OBEDIENCE, 79.8) // Crown of Splendor
			.addSingleDrop(21539, BEAD_OF_OBEDIENCE, 87.5) // Wailing of Splendor
			.addSingleDrop(21540, BEAD_OF_OBEDIENCE, 87.5) // Wailing of Splendor
			.addSingleDrop(21658, BEAD_OF_OBEDIENCE, 79.1) // Punishment of Splendor
			.build();
	// Misc
	private static final int MIN_LEVEL_REQUIRED = 60;
	private static final int BEAD_OF_OBEDIENCE_COUNT_REQUIRED = 300;
	// Rewards ID's
	private static final int ASOFE = 4043;
	private static final int THONS = 4044;
	private static final int ENRIA = 4042;
	private static final int MOLD_HARDENER = 4041;
	
	public Q00627_HeartInSearchOfPower() {
		super(627, Q00627_HeartInSearchOfPower.class.getSimpleName(), "Heart in Search of Power");
		addStartNpc(MYSTERIOUS_NECROMANCER);
		addTalkId(MYSTERIOUS_NECROMANCER, ENFEUX);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(SEAL_OF_LIGHT, BEAD_OF_OBEDIENCE.getId(), GEM_OF_SAINTS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		String htmltext = event;
		switch (event) {
			case "31518-02.htm":
				st.startQuest();
				break;
			case "31518-06.html":
				if (!hasItemsAtLimit(st.getPlayer(), BEAD_OF_OBEDIENCE)) {
					return "31518-05.html";
				}
				st.giveItems(SEAL_OF_LIGHT, 1);
				st.takeItems(BEAD_OF_OBEDIENCE.getId(), -1);
				st.setCond(3);
				break;
			case "Adena":
			case "Asofes":
			case "Thons":
			case "Enrias":
			case "Mold_Hardener":
				if (!st.hasQuestItems(GEM_OF_SAINTS)) {
					return "31518-11.html";
				}
				switch (event) {
					case "Adena":
						st.giveAdena(100000, true);
						break;
					case "Asofes":
						st.rewardItems(ASOFE, 13);
						st.giveAdena(6400, true);
						break;
					case "Thons":
						st.rewardItems(THONS, 13);
						st.giveAdena(6400, true);
						break;
					case "Enrias":
						st.rewardItems(ENRIA, 6);
						st.giveAdena(13600, true);
						break;
					case "Mold_Hardener":
						st.rewardItems(MOLD_HARDENER, 3);
						st.giveAdena(17200, true);
						break;
				}
				htmltext = "31518-10.html";
				st.exitQuest(true);
				break;
			case "31519-02.html":
				if (st.hasQuestItems(SEAL_OF_LIGHT) && st.isCond(3)) {
					st.giveItems(GEM_OF_SAINTS, 1);
					st.takeItems(SEAL_OF_LIGHT, -1);
					st.setCond(4);
				} else {
					htmltext = getNoQuestMsg(player);
				}
				break;
			case "31518-09.html":
				break;
			default:
				htmltext = null;
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(killer, 1, 1, npc);
		if (st != null && giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true)) {
			st.setCond(2);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		switch (st.getState()) {
			case State.CREATED:
				if (npc.getId() == MYSTERIOUS_NECROMANCER) {
					htmltext = (player.getLevel() >= MIN_LEVEL_REQUIRED) ? "31518-01.htm" : "31518-00.htm";
				}
				break;
			case State.STARTED:
				switch (npc.getId()) {
					case MYSTERIOUS_NECROMANCER:
						switch (st.getCond()) {
							case 1:
								htmltext = "31518-03.html";
								break;
							case 2:
								htmltext = "31518-04.html";
								break;
							case 3:
								htmltext = "31518-07.html";
								break;
							case 4:
								htmltext = "31518-08.html";
								break;
						}
						break;
					case ENFEUX:
						switch (st.getCond()) {
							case 3:
								htmltext = "31519-01.html";
								break;
							case 4:
								htmltext = "31519-03.html";
								break;
						}
						break;
				}
				break;
		}
		return htmltext;
	}
}
