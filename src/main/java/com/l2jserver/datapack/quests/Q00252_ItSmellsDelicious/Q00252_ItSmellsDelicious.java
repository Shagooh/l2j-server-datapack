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
package com.l2jserver.datapack.quests.Q00252_ItSmellsDelicious;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * It Smells Delicious! (252)<br>
 * Updated by corbin12, thanks VlLight for help.
 * @author Dumpster, jurchiks
 */
public class Q00252_ItSmellsDelicious extends Quest {
	// NPC
	public static final int STAN = 30200;
	// Items
	private static final QuestItemChanceHolder DIARY = new QuestItemChanceHolder(15500, 59.9, 10L);
	private static final QuestItemChanceHolder COOKBOOK_PAGE = new QuestItemChanceHolder(15501, 36L, 5L);
	// Monsters
	private static final int[] MOBS = {
		22786,
		22787,
		22788
	};
	private static final int CHEF = 18908;
	
	public Q00252_ItSmellsDelicious() {
		super(252, Q00252_ItSmellsDelicious.class.getSimpleName(), "It Smells Delicious!");
		addStartNpc(STAN);
		addTalkId(STAN);
		addKillId(CHEF);
		addKillId(MOBS);
		registerQuestItems(DIARY.getId(), COOKBOOK_PAGE.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs == null) {
			return htmltext;
		}
		
		switch (event) {
			case "30200-04.htm":
				htmltext = event;
				break;
			case "30200-05.htm":
				if (qs.isCreated()) {
					qs.startQuest();
					htmltext = event;
				}
				break;
			case "30200-08.html":
				if (qs.isCond(2)) {
					giveAdena(player, 147656, true);
					addExpAndSp(player, 716238, 78324);
					qs.exitQuest(false, true);
					htmltext = event;
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs;
		if (npc.getId() == CHEF) { // only the killer gets quest items from the chef
			qs = getQuestState(killer, false);
			if ((qs != null) && qs.isCond(1)) {
				if (giveItemRandomly(qs.getPlayer(), npc, COOKBOOK_PAGE, true) && hasItemsAtLimit(qs.getPlayer(), DIARY)) {
					qs.setCond(2);
				}
			}
		} else {
			qs = getRandomPartyMemberState(killer, 1, 3, npc);
			if (qs != null && giveItemRandomly(qs.getPlayer(), npc, DIARY, true) && hasItemsAtLimit(qs.getPlayer(), COOKBOOK_PAGE)) {
				qs.setCond(2);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public boolean checkPartyMember(QuestState qs, L2Npc npc) {
		return !hasItemsAtLimit(qs.getPlayer(), DIARY);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		
		if (qs.isCreated()) {
			htmltext = ((player.getLevel() >= 82) ? "30200-01.htm" : "30200-02.htm");
		} else if (qs.isStarted()) {
			switch (qs.getCond()) {
				case 1:
					htmltext = "30200-06.html";
					break;
				case 2:
					if (hasItemsAtLimit(qs.getPlayer(), DIARY, COOKBOOK_PAGE)) {
						htmltext = "30200-07.html";
					}
					break;
			}
		} else {
			htmltext = "30200-03.html";
		}
		return htmltext;
	}
}
