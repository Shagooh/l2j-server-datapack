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
package com.l2jserver.datapack.quests.Q00380_BringOutTheFlavorOfIngredients;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

import static com.l2jserver.gameserver.enums.audio.Sound.ITEMSOUND_QUEST_ITEMGET;

/**
 * Bring Out the Flavor of Ingredients! (380)
 * @author Pandragon
 */
public final class Q00380_BringOutTheFlavorOfIngredients extends Quest {
	// NPC
	private static final int ROLLAND = 30069;
	// Items
	private static final int ANTIDOTE = 1831;
	private static final QuestItemChanceHolder RITRON_FRUIT = new QuestItemChanceHolder(5895, 10.0, 4L);
	private static final QuestItemChanceHolder MOON_FLOWER = new QuestItemChanceHolder(5896, 50.0, 20L);
	private static final QuestItemChanceHolder LEECH_FLUIDS = new QuestItemChanceHolder(5897, 50.0, 10L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20205, RITRON_FRUIT) // Dire Wolf
			.addSingleDrop(20206, MOON_FLOWER) // Kadif Werewolf
			.addSingleDrop(20225, LEECH_FLUIDS) // Giant Mist Leech
			.build();
	// Rewards
	private static final int RITRON_RECIPE = 5959;
	private static final int RITRON_DESSERT = 5960;
	// Misc
	private static final int MIN_LVL = 24;
	
	public Q00380_BringOutTheFlavorOfIngredients() {
		super(380, Q00380_BringOutTheFlavorOfIngredients.class.getSimpleName(), "Bring Out the Flavor of Ingredients!");
		addStartNpc(ROLLAND);
		addTalkId(ROLLAND);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(RITRON_FRUIT.getId(), MOON_FLOWER.getId(), LEECH_FLUIDS.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs != null) {
			switch (event) {
				case "30069-03.htm":
				case "30069-04.htm":
				case "30069-06.html": {
					htmltext = event;
					break;
				}
				case "30069-05.htm": {
					if (qs.isCreated()) {
						qs.startQuest();
						htmltext = event;
					}
					break;
				}
				case "30069-13.html": {
					if (qs.isCond(9)) {
						rewardItems(player, RITRON_RECIPE, 1);
						qs.exitQuest(true, true);
						htmltext = event;
					}
					break;
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		final QuestState qs = getQuestState(talker, true);
		String htmltext = getNoQuestMsg(talker);
		switch (qs.getState()) {
			case State.CREATED: {
				htmltext = (talker.getLevel() >= MIN_LVL) ? "30069-02.htm" : "30069-01.htm";
				break;
			}
			case State.STARTED: {
				switch (qs.getCond()) {
					case 1:
					case 2:
					case 3:
					case 4: {
						if ((getQuestItemsCount(talker, ANTIDOTE) >= 2) && hasItemsAtLimit(talker, RITRON_FRUIT, MOON_FLOWER, LEECH_FLUIDS)) {
							takeItems(talker, ANTIDOTE, 2);
							takeItems(talker, -1, RITRON_FRUIT.getId(), MOON_FLOWER.getId(), LEECH_FLUIDS.getId());
							qs.setCond(5, true);
							htmltext = "30069-08.html";
						} else {
							htmltext = "30069-07.html";
						}
						break;
					}
					case 5: {
						qs.setCond(6, true);
						htmltext = "30069-09.html";
						break;
					}
					case 6: {
						qs.setCond(7, true);
						htmltext = "30069-10.html";
						break;
					}
					case 7: {
						qs.setCond(8, true);
						htmltext = "30069-11.html";
						break;
					}
					case 8: {
						rewardItems(talker, RITRON_DESSERT, 1);
						if (getRandom(100) < 56) {
							htmltext = "30069-15.html";
							qs.exitQuest(true, true);
						} else {
							qs.setCond(9, true);
							htmltext = "30069-12.html";
						}
						break;
					}
					case 9: {
						htmltext = "30069-12.html";
						break;
					}
				}
				break;
			}
			case State.COMPLETED: {
				htmltext = getAlreadyCompletedMsg(talker);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, -1, 3, npc);
		if ((qs != null) && (qs.getCond() < 4)) {
			if (giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), false)) {
				if (hasItemsAtLimit(killer, RITRON_FRUIT, MOON_FLOWER, LEECH_FLUIDS)) {
					qs.setCond(qs.getCond() + 1, true);
				} else {
					playSound(killer, ITEMSOUND_QUEST_ITEMGET);
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
}
