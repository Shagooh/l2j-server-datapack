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
package com.l2jserver.datapack.quests.Q00264_KeenClaws;

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
 * Keen Claws (264)
 * @author xban1x
 */
public final class Q00264_KeenClaws extends Quest {
	// Npc
	private static final int PAINT = 30136;
	// Item
	private static final QuestItemChanceHolder WOLF_CLAW = new QuestItemChanceHolder(1367, 50L);
	// Droplists
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addGroupedDropForSingleItem(20003, WOLF_CLAW, 50.0)
				.withAmount(2, 50.0).orElse(8)
			.addSingleDrop(20456, WOLF_CLAW, 125.0)
			.build();
	// Rewards
	private static final Map<Integer, List<ItemHolder>> REWARDS = Map.of(
			1, List.of(new ItemHolder(4633, 1)),
			2, List.of(new ItemHolder(57, 2000)),
			5, List.of(new ItemHolder(5140, 1)),
			8, List.of(new ItemHolder(735, 1), new ItemHolder(57, 50)),
			11, List.of(new ItemHolder(737, 1)),
			14, List.of(new ItemHolder(734, 1)),
			17, List.of(new ItemHolder(35, 1), new ItemHolder(57, 50))
	);
	// Misc
	private static final int MIN_LVL = 3;
	
	public Q00264_KeenClaws() {
		super(264, Q00264_KeenClaws.class.getSimpleName(), "Keen Claws");
		addStartNpc(PAINT);
		addTalkId(PAINT);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(WOLF_CLAW.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if ((st != null) && event.equals("30136-03.htm")) {
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
				htmltext = (player.getLevel() >= MIN_LVL) ? "30136-02.htm" : "30136-01.htm";
				break;
			}
			case State.STARTED: {
				switch (st.getCond()) {
					case 1: {
						htmltext = "30136-04.html";
						break;
					}
					case 2: {
						if (hasItemsAtLimit(st.getPlayer(), WOLF_CLAW)) {
							final int chance = getRandom(17);
							for (Map.Entry<Integer, List<ItemHolder>> reward : REWARDS.entrySet()) {
								if (chance < reward.getKey()) {
									for (ItemHolder item : reward.getValue()) {
										st.rewardItems(item);
									}
									if (chance == 0) {
										st.playSound(Sound.ITEMSOUND_QUEST_JACKPOT);
									}
									break;
								}
							}
							st.exitQuest(true, true);
							htmltext = "30136-05.html";
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
