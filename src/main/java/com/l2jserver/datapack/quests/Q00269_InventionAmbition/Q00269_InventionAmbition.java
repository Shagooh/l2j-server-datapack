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
package com.l2jserver.datapack.quests.Q00269_InventionAmbition;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Invention Ambition (269)
 * @author xban1x
 */
public final class Q00269_InventionAmbition extends Quest {
	// NPC
	private static final int INVENTOR_MARU = 32486;
	// Items
	private static final int ENERGY_ORE = 10866;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21124, ENERGY_ORE, 46.0) // Red Eye Barbed Bat
			.addSingleDrop(21125, ENERGY_ORE, 48.0) // Northern Trimden
			.addSingleDrop(21126, ENERGY_ORE, 50.0) // Kerope Werewolf
			.addSingleDrop(21127, ENERGY_ORE, 64.0) // Northern Goblin
			.addSingleDrop(21128, ENERGY_ORE, 66.0) // Spine Golem
			.addSingleDrop(21129, ENERGY_ORE, 68.0) // Kerope Werewolf Chief
			.addSingleDrop(21130, ENERGY_ORE, 76.0) // Northern Goblin Leader
			.addSingleDrop(21131, ENERGY_ORE, 78.0) // Enchanted Spine Golem
			.build();
	// Misc
	private static final int MIN_LVL = 18;
	
	public Q00269_InventionAmbition() {
		super(269, Q00269_InventionAmbition.class.getSimpleName(), "Invention Ambition");
		addStartNpc(INVENTOR_MARU);
		addTalkId(INVENTOR_MARU);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(ENERGY_ORE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "32486-03.htm": {
				if (player.getLevel() >= MIN_LVL) {
					htmltext = event;
				}
				break;
			}
			case "32486-04.htm": {
				if (player.getLevel() >= MIN_LVL) {
					st.startQuest();
					htmltext = event;
				}
				break;
			}
			case "32486-07.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
			case "32486-08.html": {
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(killer, -1, 3, npc);
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
			htmltext = (player.getLevel() >= MIN_LVL) ? "32486-01.htm" : "32486-02.html";
		} else if (st.isStarted()) {
			if (st.hasQuestItems(ENERGY_ORE)) {
				final long count = getQuestItemsCount(player, ENERGY_ORE);
				giveAdena(player, (count * 50) + (count >= 10 ? 2044 : 0), true);
				takeItems(player, ENERGY_ORE, -1);
				htmltext = "32486-06.html";
			} else {
				htmltext = "32486-05.html";
			}
		}
		return htmltext;
	}
}
