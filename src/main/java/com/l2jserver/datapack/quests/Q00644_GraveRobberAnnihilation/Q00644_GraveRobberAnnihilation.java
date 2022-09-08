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
package com.l2jserver.datapack.quests.Q00644_GraveRobberAnnihilation;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Grave Robber Annihilation (644)
 * @author netvirus
 */
public final class Q00644_GraveRobberAnnihilation extends Quest {
	// NPC
	private static final int KARUDA = 32017;
	// Item
	private static final QuestItemChanceHolder ORC_GOODS = new QuestItemChanceHolder(8088, 120L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22003, ORC_GOODS, 71.4) // Grave Robber Scout
			.addSingleDrop(22004, ORC_GOODS, 84.1) // Grave Robber Lookout
			.addSingleDrop(22005, ORC_GOODS, 77.8) // Grave Robber Ranger
			.addSingleDrop(22006, ORC_GOODS, 74.6) // Grave Robber Guard
			.addSingleDrop(22008, ORC_GOODS, 81.0) // Grave Robber Fighter
			.build();
	// Misc
	private static final int MIN_LVL = 20;
	private static final int ORC_GOODS_REQUIRED_COUNT = 120;
	// Rewards
	private static final Map<String, ItemHolder> REWARDS = new HashMap<>();
	static {
		REWARDS.put("varnish", new ItemHolder(1865, 30)); // Varnish
		REWARDS.put("animalskin", new ItemHolder(1867, 40)); // Animal Skin
		REWARDS.put("animalbone", new ItemHolder(1872, 40)); // Animal Bone
		REWARDS.put("charcoal", new ItemHolder(1871, 30)); // Charcoal
		REWARDS.put("coal", new ItemHolder(1870, 30)); // Coal
		REWARDS.put("ironore", new ItemHolder(1869, 30)); // Iron Ore
	}
	
	public Q00644_GraveRobberAnnihilation() {
		super(644, Q00644_GraveRobberAnnihilation.class.getSimpleName(), "Grave Robber Annihilation");
		addStartNpc(KARUDA);
		addTalkId(KARUDA);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(ORC_GOODS.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		String htmltext = null;
		if (st == null) {
			return htmltext;
		}
		
		switch (event) {
			case "32017-03.htm": {
				if (st.isCreated()) {
					st.startQuest();
					htmltext = event;
				}
				break;
			}
			case "32017-06.html": {
				if (st.isCond(2) && hasItemsAtLimit(st.getPlayer(), ORC_GOODS)) {
					htmltext = event;
				}
				break;
			}
			case "varnish":
			case "animalskin":
			case "animalbone":
			case "charcoal":
			case "coal":
			case "ironore": {
				if (st.isCond(2)) {
					final ItemHolder reward = REWARDS.get(event);
					st.rewardItems(reward.getId(), reward.getCount());
					st.exitQuest(true, true);
					htmltext = "32017-07.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, 1, 3, npc);
		if ((qs != null) && giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true)) {
			qs.setCond(2);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (st.getState()) {
			case State.CREATED: {
				htmltext = ((player.getLevel() >= MIN_LVL) ? "32017-01.htm" : "32017-02.htm");
				break;
			}
			case State.STARTED: {
				if (st.isCond(2) && hasItemsAtLimit(st.getPlayer(), ORC_GOODS)) {
					htmltext = "32017-04.html";
				} else {
					htmltext = "32017-05.html";
				}
				break;
			}
		}
		return htmltext;
	}
}
