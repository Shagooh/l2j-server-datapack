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
package com.l2jserver.datapack.quests.Q00141_ShadowFoxPart3;

import com.l2jserver.datapack.quests.Q00140_ShadowFoxPart2.Q00140_ShadowFoxPart2;
import com.l2jserver.datapack.quests.Q00998_FallenAngelSelect.Q00998_FallenAngelSelect;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Shadow Fox - 3 (141)
 * @author Nono
 */
public class Q00141_ShadowFoxPart3 extends Quest {
	// NPCs
	private static final int NATOOLS = 30894;
	// Items
	private static final QuestItemChanceHolder PREDECESSORS_REPORT = new QuestItemChanceHolder(10350, 30L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20135, PREDECESSORS_REPORT, 53.0) // Alligator
			.addSingleDrop(20791, PREDECESSORS_REPORT, 100.0) // Crokian Warrior
			.addSingleDrop(20792, PREDECESSORS_REPORT, 92.0) // Farhite
			.build();
	// Misc
	private static final int MIN_LEVEL = 37;
	private static final int MAX_REWARD_LEVEL = 42;

	public Q00141_ShadowFoxPart3() {
		super(141, Q00141_ShadowFoxPart3.class.getSimpleName(), "Shadow Fox - 3");
		addStartNpc(NATOOLS);
		addTalkId(NATOOLS);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(PREDECESSORS_REPORT.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = event;
		switch (event) {
			case "30894-05.html":
			case "30894-10.html":
			case "30894-11.html":
			case "30894-12.html":
			case "30894-13.html":
			case "30894-14.html":
			case "30894-16.html":
			case "30894-17.html":
			case "30894-19.html":
			case "30894-20.html":
				break;
			case "30894-03.htm":
				st.startQuest();
				break;
			case "30894-06.html":
				st.setCond(2, true);
				break;
			case "30894-15.html":
				st.set("talk", "2");
				break;
			case "30894-18.html":
				st.setCond(4, true);
				st.unset("talk");
				break;
			case "30894-21.html":
				st.giveAdena(88888, true);
				if (player.getLevel() <= MAX_REWARD_LEVEL) {
					st.addExpAndSp(278005, 17058);
				}
				st.exitQuest(false, true);
				
				final Quest q = QuestManager.getInstance().getQuest(Q00998_FallenAngelSelect.class.getSimpleName());
				if (q != null) {
					q.newQuestState(player).setState(State.STARTED);
				}
				break;
			default:
				htmltext = null;
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(player, 2, 1, npc);
		if (st != null && giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true)) {
			st.setCond(3);
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		switch (st.getState()) {
			case State.CREATED:
				htmltext = (player.getLevel() >= MIN_LEVEL) ? (player.hasQuestCompleted(Q00140_ShadowFoxPart2.class.getSimpleName())) ? "30894-01.htm" : "30894-00.html" : "30894-02.htm";
				break;
			case State.STARTED:
				switch (st.getCond()) {
					case 1:
						htmltext = "30894-04.html";
						break;
					case 2:
						htmltext = "30894-07.html";
						break;
					case 3:
						if (st.getInt("talk") == 1) {
							htmltext = "30894-09.html";
						} else if (st.getInt("talk") == 2) {
							htmltext = "30894-16.html";
						} else {
							htmltext = "30894-08.html";
							st.takeItems(PREDECESSORS_REPORT.getId(), -1);
							st.set("talk", "1");
						}
						break;
					case 4:
						htmltext = "30894-19.html";
						break;
				}
				break;
			case State.COMPLETED:
				htmltext = getAlreadyCompletedMsg(player);
				break;
		}
		return htmltext;
	}
}