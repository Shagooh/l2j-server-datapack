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
package com.l2jserver.datapack.quests.Q00299_GatherIngredientsForPie;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.ItemChanceHolder;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

import java.util.List;

/**
 * Gather Ingredients for Pie (299)
 * @author xban1x
 */
public final class Q00299_GatherIngredientsForPie extends Quest {
	// NPCs
	private static final int LARS = 30063;
	private static final int BRIGHT = 30466;
	private static final int EMILLY = 30620;
	// Items
	private static final int FRUIT_BASKET = 7136;
	private static final int AVELLAN_SPICE = 7137;
	private static final QuestItemChanceHolder HONEY_POUCH = new QuestItemChanceHolder(7138, 100L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20934, HONEY_POUCH, 1, 2, 70.0) // Wasp Worker
			.addSingleDrop(20935, HONEY_POUCH, 1, 2, 77.0) // Wasp Leader
			.build();
	// Rewards
	private static final List<ItemChanceHolder> REWARDS = List.of(
			new ItemChanceHolder(57, 400, 2500), // Adena
			new ItemChanceHolder(1865, 550, 50), // Varnish
			new ItemChanceHolder(1870, 700, 50), // Coal
			new ItemChanceHolder(1869, 850, 50), // Iron Ore
			new ItemChanceHolder(1871, 1000, 50) // Charcoal
	);
	// Misc
	private static final int MIN_LVL = 34;
	
	public Q00299_GatherIngredientsForPie() {
		super(299, Q00299_GatherIngredientsForPie.class.getSimpleName(), "Gather Ingredients for Pie");
		addStartNpc(EMILLY);
		addTalkId(LARS, BRIGHT, EMILLY);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(FRUIT_BASKET, HONEY_POUCH.getId(), AVELLAN_SPICE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String html = null;
		if (qs == null) {
			return html;
		}
		switch (event) {
			case "30063-02.html": {
				if (qs.isCond(3)) {
					giveItems(player, AVELLAN_SPICE, 1);
					qs.setCond(4, true);
					html = event;
				}
				break;
			}
			case "30466-02.html": {
				if (qs.isCond(5)) {
					giveItems(player, FRUIT_BASKET, 1);
					qs.setCond(6, true);
					html = event;
				}
				break;
			}
			case "30620-03.htm": {
				if (qs.isCreated()) {
					qs.startQuest();
					html = event;
				}
				break;
			}
			case "30620-06.html": {
				if (qs.isCond(2) && hasItemsAtLimit(player, HONEY_POUCH)) {
					takeItems(player, HONEY_POUCH.getId(), -1);
					qs.setCond(3, true);
					html = event;
				} else {
					html = "30620-07.html";
				}
				break;
			}
			case "30620-10.html": {
				if (qs.isCond(4) && hasQuestItems(player, AVELLAN_SPICE)) {
					takeItems(player, AVELLAN_SPICE, -1);
					qs.setCond(5, true);
					html = event;
				} else {
					html = "30620-11.html";
				}
				break;
			}
			case "30620-14.html": {
				if (qs.isCond(6) && hasQuestItems(player, FRUIT_BASKET)) {
					takeItems(player, FRUIT_BASKET, -1);
					final int chance = getRandom(1000);
					for (ItemChanceHolder holder : REWARDS) {
						if (holder.getChance() > chance) {
							rewardItems(player, holder);
							break;
						}
					}
					qs.exitQuest(true, true);
					html = event;
				} else {
					html = "30620-15.html";
				}
				break;
			}
		}
		return html;
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
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		final QuestState qs = getQuestState(talker, true);
		String html = getNoQuestMsg(talker);
		switch (npc.getId()) {
			case LARS: {
				switch (qs.getCond()) {
					case 3: {
						html = "30063-01.html";
						break;
					}
					case 4: {
						html = "30063-03.html";
						break;
					}
				}
				break;
			}
			case BRIGHT: {
				switch (qs.getCond()) {
					case 5: {
						html = "30466-01.html";
						break;
					}
					case 6: {
						html = "30466-03.html";
						break;
					}
				}
				break;
			}
			case EMILLY: {
				switch (qs.getState()) {
					case State.CREATED: {
						html = (talker.getLevel() >= MIN_LVL) ? "30620-01.htm" : "30620-02.htm";
						break;
					}
					case State.STARTED: {
						switch (qs.getCond()) {
							case 1: {
								html = "30620-05.html";
								break;
							}
							case 2: {
								if (hasItemsAtLimit(talker, HONEY_POUCH)) {
									html = "30620-04.html";
								}
								break;
							}
							case 3: {
								html = "30620-08.html";
								break;
							}
							case 4: {
								if (hasQuestItems(talker, AVELLAN_SPICE)) {
									html = "30620-09.html";
								}
								break;
							}
							case 5: {
								html = "30620-12.html";
								break;
							}
							case 6: {
								if (hasQuestItems(talker, FRUIT_BASKET)) {
									html = "30620-13.html";
								}
								break;
							}
						}
						break;
					}
				}
				break;
			}
		}
		return html;
	}
}
