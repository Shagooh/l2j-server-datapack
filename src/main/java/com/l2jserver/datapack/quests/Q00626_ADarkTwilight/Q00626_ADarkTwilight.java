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
package com.l2jserver.datapack.quests.Q00626_ADarkTwilight;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * A Dark Twilight (626)<br>
 * Original Jython script by disKret.
 * @author Citizen
 */
public class Q00626_ADarkTwilight extends Quest {
	// NPCs
	private static final int HIERARCH = 31517;
	// Items
	private static final QuestItemChanceHolder BLOOD_OF_SAINT = new QuestItemChanceHolder(7169, 300L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21520, BLOOD_OF_SAINT, 64.1) // Eye of Splendor
			.addSingleDrop(21523, BLOOD_OF_SAINT, 64.8) // Flash of Splendor
			.addSingleDrop(21524, BLOOD_OF_SAINT, 69.2) // Blade of Splendor
			.addSingleDrop(21525, BLOOD_OF_SAINT, 71.0) // Blade of Splendor
			.addSingleDrop(21526, BLOOD_OF_SAINT, 77.2) // Wisdom of Splendor
			.addSingleDrop(21529, BLOOD_OF_SAINT, 63.9) // Soul of Splendor
			.addSingleDrop(21530, BLOOD_OF_SAINT, 68.3) // Victory of Splendor
			.addSingleDrop(21531, BLOOD_OF_SAINT, 76.7) // Punishment of Splendor
			.addSingleDrop(21532, BLOOD_OF_SAINT, 79.5) // Shout of Splendor
			.addSingleDrop(21535, BLOOD_OF_SAINT, 80.2) // Signet of Splendor
			.addSingleDrop(21536, BLOOD_OF_SAINT, 77.4) // Crown of Splendor
			.addSingleDrop(21539, BLOOD_OF_SAINT, 84.8) // Wailing of Splendor
			.addSingleDrop(21540, BLOOD_OF_SAINT, 88.0) // Wailing of Splendor
			.addSingleDrop(21658, BLOOD_OF_SAINT, 79.0) // Punishment of Splendor
			.build();
	// Misc
	private static final int MIN_LEVEL_REQUIRED = 60;
	private static final int ITEMS_COUNT_REQUIRED = 300;
	// Rewards
	private static final int ADENA_COUNT = 100000;
	private static final int XP_COUNT = 162773;
	private static final int SP_COUNT = 12500;
	
	public Q00626_ADarkTwilight() {
		super(626, Q00626_ADarkTwilight.class.getSimpleName(), "A Dark Twilight");
		addStartNpc(HIERARCH);
		addTalkId(HIERARCH);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(BLOOD_OF_SAINT.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		String htmltext = event;
		switch (event) {
			case "31517-05.html":
				break;
			case "31517-02.htm":
				st.startQuest();
				break;
			case "Exp":
				if (!hasItemsAtLimit(st.getPlayer(), BLOOD_OF_SAINT)) {
					return "31517-06.html";
				}
				st.addExpAndSp(XP_COUNT, SP_COUNT);
				st.exitQuest(true, true);
				htmltext = "31517-07.html";
				break;
			case "Adena":
				if (!hasItemsAtLimit(st.getPlayer(), BLOOD_OF_SAINT)) {
					return "31517-06.html";
				}
				st.giveAdena(ADENA_COUNT, true);
				st.exitQuest(true, true);
				htmltext = "31517-07.html";
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
				htmltext = (player.getLevel() >= MIN_LEVEL_REQUIRED) ? "31517-01.htm" : "31517-00.htm";
				break;
			case State.STARTED:
				switch (st.getCond()) {
					case 1:
						htmltext = "31517-03.html";
						break;
					case 2:
						htmltext = "31517-04.html";
						break;
				}
				break;
		}
		return htmltext;
	}
}
