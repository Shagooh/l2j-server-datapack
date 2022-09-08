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
package com.l2jserver.datapack.quests.Q10292_SevenSignsGirlOfDoubt;

import com.l2jserver.datapack.quests.Q00198_SevenSignsEmbryo.Q00198_SevenSignsEmbryo;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestDroplist.QuestDropInfo;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Seven Signs, Girl of Doubt (10292)
 * @author Adry_85
 * @since 2.6.0.0
 */
public final class Q10292_SevenSignsGirlOfDoubt extends Quest {
	// NPCs
	private static final int HARDIN = 30832;
	private static final int WOOD = 32593;
	private static final int FRANZ = 32597;
	private static final int ELCADIA = 32784;
	// Item
	private static final QuestItemChanceHolder ELCADIAS_MARK = new QuestItemChanceHolder(17226, 70.0, 10L);
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.bulkAddSingleDrop(ELCADIAS_MARK)
				.withNpcs(22801, 22802, 22803) // Cruel Pincer Golem
				.withNpcs(22804, 22805, 22806) // Horrifying Jackhammer Golem
				.build()
			.build();
	// Misc
	private static final int MIN_LEVEL = 81;
	// Variables
	private static final String I_QUEST1 = "I_QUEST1";
	// Monster
	private static final int CREATURE_OF_THE_DUSK1 = 27422;
	private static final int CREATURE_OF_THE_DUSK2 = 27424;

	public Q10292_SevenSignsGirlOfDoubt() {
		super(10292, Q10292_SevenSignsGirlOfDoubt.class.getSimpleName(), "Seven Signs, Girl of Doubt");
		addStartNpc(WOOD);
		addSpawnId(ELCADIA);
		addTalkId(WOOD, FRANZ, ELCADIA, HARDIN);
		addKillId(DROPLIST.getNpcIds());
		addKillId(CREATURE_OF_THE_DUSK1, CREATURE_OF_THE_DUSK2);
		registerQuestItems(ELCADIAS_MARK.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "32593-02.htm":
			case "32593-04.htm":
			case "32597-02.html":
			case "32597-04.html": {
				htmltext = event;
				break;
			}
			case "32593-03.htm": {
				st.startQuest();
				st.setMemoState(1);
				htmltext = event;
				break;
			}
			case "32597-03.html": {
				st.setMemoState(2);
				st.setCond(2, true);
				htmltext = event;
				break;
			}
			case "32784-02.html": {
				if (st.isMemoState(2)) {
					htmltext = event;
				}
				break;
			}
			case "32784-03.html": {
				if (st.isMemoState(2)) {
					st.setMemoState(3);
					st.setCond(3, true);
					htmltext = event;
				}
				break;
			}
			case "32784-05.html": {
				if (st.isMemoState(4)) {
					htmltext = event;
				}
				break;
			}
			case "32784-06.html": {
				if (st.isMemoState(4)) {
					st.setMemoState(5);
					st.setCond(5, true);
					htmltext = event;
				}
				break;
			}
			case "SPAWN": {
				if (!npc.getVariables().getBoolean(I_QUEST1, false)) {
					st.set("killCount", 0);
					npc.getVariables().set(I_QUEST1, true);
					addSpawn(CREATURE_OF_THE_DUSK1, 89440, -238016, -9632, getRandom(360), false, 0, false, player.getInstanceId());
					addSpawn(CREATURE_OF_THE_DUSK2, 89524, -238131, -9632, getRandom(360), false, 0, false, player.getInstanceId());
				} else {
					htmltext = "32784-07.html";
				}
				break;
			}
			case "32784-11.html":
			case "32784-12.html": {
				if (st.isMemoState(6)) {
					htmltext = event;
				}
				break;
			}
			case "32784-13.html": {
				if (st.isMemoState(6)) {
					st.setMemoState(7);
					st.setCond(7, true);
					htmltext = event;
				}
				break;
			}
			case "30832-02.html": {
				if (st.isMemoState(7)) {
					st.setMemoState(8);
					st.setCond(8, true);
					htmltext = event;
				}
				break;
			}
			case "30832-03.html": {
				if (st.isMemoState(8)) {
					htmltext = event;
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(player, -1, 3, npc);
		if (st != null) {
			QuestDropInfo markDropInfo = DROPLIST.get(npc);
			if (markDropInfo != null && giveItemRandomly(st.getPlayer(), npc, markDropInfo, true) && st.isMemoState(3)) {
				st.setCond(4);
			} else {
				int killCount = st.getInt("killCount");
				st.set("killCount", ++killCount);
				if (killCount == 2) {
					st.setMemoState(6);
					st.setCond(6);
				}
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		npc.getVariables().set(I_QUEST1, false);
		return super.onSpawn(npc);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCompleted()) {
			if (npc.getId() == WOOD) {
				htmltext = "32593-05.html";
			}
		} else if (st.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LEVEL) && player.hasQuestCompleted(Q00198_SevenSignsEmbryo.class.getSimpleName())) ? "32593-01.htm" : "32593-06.htm";
		} else if (st.isStarted()) {
			switch (npc.getId()) {
				case WOOD: {
					if ((st.getMemoState() > 0) && (st.getMemoState() < 9)) {
						htmltext = "32593-07.html";
					}
					break;
				}
				case FRANZ: {
					final int memoState = st.getMemoState();
					if (memoState == 1) {
						htmltext = "32597-01.html";
					} else if ((memoState >= 2) && (memoState < 7)) {
						htmltext = "32597-05.html";
					} else if (memoState == 7) {
						htmltext = "32597-06.html";
					}
					break;
				}
				case ELCADIA: {
					switch (st.getMemoState()) {
						case 2: {
							htmltext = "32784-01.html";
							break;
						}
						case 3: {
							if (!hasItemsAtLimit(player, ELCADIAS_MARK)) {
								htmltext = "32784-03.html";
							} else {
								takeItems(player, ELCADIAS_MARK.getId(), -1);
								st.setMemoState(4);
								st.setCond(4, true);
								htmltext = "32784-04.html";
							}
							break;
						}
						case 4: {
							htmltext = "32784-08.html";
							break;
						}
						case 5: {
							htmltext = "32784-09.html";
							break;
						}
						case 6: {
							htmltext = "32784-10.html";
							break;
						}
						case 7: {
							htmltext = "32784-14.html";
							break;
						}
						case 8: {
							if (player.isSubClassActive()) {
								htmltext = "32784-15.html";
							} else {
								addExpAndSp(player, 10000000, 1000000);
								st.exitQuest(false, true);
								htmltext = "32784-16.html";
							}
							break;
						}
					}
					break;
				}
				case HARDIN: {
					if (st.isMemoState(7)) {
						htmltext = "30832-01.html";
					} else if (st.isMemoState(8)) {
						htmltext = "30832-03.html";
					}
					break;
				}
			}
		}
		return htmltext;
	}
}
