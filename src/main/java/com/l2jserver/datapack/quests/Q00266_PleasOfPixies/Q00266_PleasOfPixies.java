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
package com.l2jserver.datapack.quests.Q00266_PleasOfPixies;

import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

import java.util.List;
import java.util.Map;

/**
 * Pleas of Pixies (266)
 * @author xban1x
 */
public final class Q00266_PleasOfPixies extends Quest {
	// NPC
	private static final int PIXY_MURIKA = 31852;
	// Items
	private static final QuestItemChanceHolder PREDATORS_FANG = new QuestItemChanceHolder(1334, 100L);
	// Droplists
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20537, PREDATORS_FANG, 2) // Elder Red Keltir
			.addSingleDrop(20525, PREDATORS_FANG, 250.0) // Gray Wolf
			.addSingleDrop(20534, PREDATORS_FANG, 60.0) // Red Keltir
			.addSingleDrop(20530, PREDATORS_FANG, 80.0) // Young Red Keltir
			.build();
	// Rewards
	private static final Map<Integer, List<ItemHolder>> REWARDS = Map.of(
		0, List.of(new ItemHolder(1337, 1), new ItemHolder(3032, 1)), // Emerald, Recipe: Spiritshot D
		1, List.of(new ItemHolder(2176, 1), new ItemHolder(1338, 1)), // Recipe: Leather Boots, Blue Onyx
		2, List.of(new ItemHolder(1339, 1), new ItemHolder(1061, 1)), // Onyx, Greater Healing Potion
		3, List.of(new ItemHolder(1336, 1), new ItemHolder(1060, 1))); // Glass Shard, Lesser Healing Potion
	// Misc
	private static final int MIN_LVL = 3;
	
	public Q00266_PleasOfPixies() {
		super(266, Q00266_PleasOfPixies.class.getSimpleName(), "Pleas of Pixies");
		addStartNpc(PIXY_MURIKA);
		addTalkId(PIXY_MURIKA);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(PREDATORS_FANG.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if ((st != null) && event.equals("31852-04.htm")) {
			st.startQuest();
			return event;
		}
		return null;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState st = getQuestState(killer, false);
		if ((st != null) && st.isCond(1)) {
			if (giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true)) {
				st.setCond(2);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (st.getState()) {
			case State.CREATED: {
				if (player.getRace() != Race.ELF) {
					htmltext = "31852-01.htm";
				} else if (player.getLevel() < MIN_LVL) {
					htmltext = "31852-02.htm";
				} else {
					htmltext = "31852-03.htm";
				}
				break;
			}
			case State.STARTED: {
				switch (st.getCond()) {
					case 1: {
						htmltext = "31852-05.html";
						break;
					}
					case 2: {
						if (hasItemsAtLimit(player, PREDATORS_FANG)) {
							final int chance = getRandom(100);
							int reward;
							if (chance < 2) {
								reward = 0;
								st.playSound(Sound.ITEMSOUND_QUEST_JACKPOT);
							} else if (chance < 20) {
								reward = 1;
							} else if (chance < 45) {
								reward = 2;
							} else {
								reward = 3;
							}
							for (ItemHolder item : REWARDS.get(reward)) {
								st.rewardItems(item);
							}
							st.exitQuest(true, true);
							htmltext = "31852-06.html";
						}
						break;
					}
				}
				break;
			}
		}
		return htmltext;
	}
}
