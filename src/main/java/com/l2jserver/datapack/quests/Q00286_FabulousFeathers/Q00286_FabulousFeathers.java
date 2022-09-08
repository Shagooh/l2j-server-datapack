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
package com.l2jserver.datapack.quests.Q00286_FabulousFeathers;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Fabulous Feathers (286).
 * @author xban1x
 */
public final class Q00286_FabulousFeathers extends Quest {
	// NPC
	private static final int ERINU = 32164;
	// Item
	private static final QuestItemChanceHolder COMMANDERS_FEATHER = new QuestItemChanceHolder(9746, 80L);
	// Droplists
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22251, COMMANDERS_FEATHER, 74.8) // Shady Muertos Captain
			.addSingleDrop(22253, COMMANDERS_FEATHER, 77.2) // Shady Muertos Warrior
			.addSingleDrop(22254, COMMANDERS_FEATHER, 77.2) // Shady Muertos Archer
			.addSingleDrop(22255, COMMANDERS_FEATHER, 79.6) // Shady Muertos Commander
			.addSingleDrop(22256, COMMANDERS_FEATHER, 95.2) // Shady Muertos Wizard
			.build();
	// Misc
	private static final int MIN_LVL = 17;
	
	public Q00286_FabulousFeathers() {
		super(286, Q00286_FabulousFeathers.class.getSimpleName(), "Fabulous Feathers");
		addStartNpc(ERINU);
		addTalkId(ERINU);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(COMMANDERS_FEATHER.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String html = null;
		if (qs == null) {
			return html;
		}
		switch (event) {
			case "32164-03.htm": {
				qs.startQuest();
				html = event;
				break;
			}
			case "32164-06.html": {
				if (qs.isCond(2) && hasItemsAtLimit(player, COMMANDERS_FEATHER)) {
					takeItems(player, COMMANDERS_FEATHER.getId(), -1);
					giveAdena(player, 4160, true);
					qs.exitQuest(true, true);
					html = event;
				} else {
					html = "32164-07.html";
				}
				break;
			}
		}
		return html;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, 1, 3, npc);
		if (qs != null && giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true)) {
			qs.setCond(2);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String html = getNoQuestMsg(player);
		if (qs.isCreated()) {
			html = ((player.getLevel() >= MIN_LVL) ? "32164-01.htm" : "32164-02.htm");
		} else if (qs.isStarted()) {
			html = ((qs.isCond(2) && hasItemsAtLimit(player, COMMANDERS_FEATHER)) ? "32164-04.html" : "32164-05.html");
		}
		return html;
	}
}
