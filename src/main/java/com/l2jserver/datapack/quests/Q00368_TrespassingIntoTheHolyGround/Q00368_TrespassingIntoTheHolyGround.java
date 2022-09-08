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
package com.l2jserver.datapack.quests.Q00368_TrespassingIntoTheHolyGround;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Trespassing into the Holy Ground (368)
 * @author Adry_85
 */
public final class Q00368_TrespassingIntoTheHolyGround extends Quest {
	// NPC
	private static final int RESTINA = 30926;
	// Item
	private static final int BLADE_STAKATO_FANG = 5881;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20794, BLADE_STAKATO_FANG, 60.0) // blade_stakato
			.addSingleDrop(20795, BLADE_STAKATO_FANG, 57.0) // blade_stakato_worker
			.addSingleDrop(20796, BLADE_STAKATO_FANG, 61.0) // blade_stakato_soldier
			.addSingleDrop(20797, BLADE_STAKATO_FANG, 93.0) // blade_stakato_drone
			.build();
	// Misc
	private static final int MIN_LEVEL = 36;

	public Q00368_TrespassingIntoTheHolyGround() {
		super(368, Q00368_TrespassingIntoTheHolyGround.class.getSimpleName(), "Trespassing into the Holy Ground");
		addStartNpc(RESTINA);
		addTalkId(RESTINA);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(BLADE_STAKATO_FANG);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "30926-02.htm": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "30926-05.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
			case "30926-06.html": {
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final int i;
		switch (npc.getId()) {
			case 20795, 20797 -> i = 1;
			default -> i = 3;
		}
		
		final QuestState st = getRandomPartyMemberState(killer, -1, i, npc);
		if (st != null) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LEVEL) ? "30926-01.htm" : "30926-03.html");
		} else if (st.isStarted()) {
			if (hasQuestItems(player, BLADE_STAKATO_FANG)) {
				final long count = getQuestItemsCount(player, BLADE_STAKATO_FANG);
				final long bonus = (count >= 10 ? 9450 : 2000);
				giveAdena(player, (count * 250) + bonus, true);
				takeItems(player, BLADE_STAKATO_FANG, -1);
				htmltext = "30926-04.html";
			} else {
				htmltext = "30926-07.html";
			}
		}
		return htmltext;
	}
}
