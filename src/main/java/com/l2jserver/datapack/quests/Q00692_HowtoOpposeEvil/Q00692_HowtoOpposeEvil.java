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
package com.l2jserver.datapack.quests.Q00692_HowtoOpposeEvil;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * How to Oppose Evil (692)
 * @author Gigiikun
 */
public final class Q00692_HowtoOpposeEvil extends Quest {
	// NPCs
	private static final int DILIOS = 32549;
	private static final int KIRKLAN = 32550;
	// Items
	private static final int LEKONS_CERTIFICATE = 13857;
	private static final int FREED_SOUL_FRAGMENT = 13863;
	private static final int DRAGONKIN_CHARM_FRAGMENT = 13865;
	private static final int SPIRIT_STONE_DUST = 15536;
	private static final int[] QUEST_ITEMS = {
			FREED_SOUL_FRAGMENT,
			13864, // Seal of Tiat
			DRAGONKIN_CHARM_FRAGMENT,
			13866, // Restless Soul
			13867, // Tiat Charm
			15535, // Concentrated Spirit Energy
			SPIRIT_STONE_DUST
	};
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.bulkAddSingleDrop(FREED_SOUL_FRAGMENT, 50.0) // Seed of Infinity
				.withNpcs(22509, 22510, 22511, 22512, 22513, 22514, 22515).build()
			.bulkAddSingleDrop(DRAGONKIN_CHARM_FRAGMENT, 25.0) // Seed of Destruction
				.withNpcs(22537, 22538, 22539, 22540, 22541, 22542, 22543, 22544, 22546)
				.withNpcs(22547, 22548, 22549, 22550, 22551, 22552, 22593, 22596, 22597).build()
			.bulkAddSingleDrop(SPIRIT_STONE_DUST, 12.5) // Seed of Annihilation
				.withNpcs(22746, 22747, 22748, 22749, 22750, 22751, 22752, 22753, 22754, 22755)
				.withNpcs(22756, 22757, 22758, 22759, 22760, 22761, 22762, 22763, 22764, 22765).build()
			.build();
	
	public Q00692_HowtoOpposeEvil() {
		super(692, Q00692_HowtoOpposeEvil.class.getSimpleName(), "How to Oppose Evil");
		addStartNpc(DILIOS);
		addTalkId(DILIOS, KIRKLAN);
		addKillId(DROPLIST.getNpcIds());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		QuestState st = getQuestState(player, false);
		if (st == null) {
			return getNoQuestMsg(player);
		}
		if (event.equalsIgnoreCase("32549-03.htm")) {
			st.startQuest();
		} else if (event.equalsIgnoreCase("32550-04.htm")) {
			st.setCond(3);
		} else if (event.equalsIgnoreCase("32550-07.htm")) {
			if (!giveReward(st, FREED_SOUL_FRAGMENT, 5, 13796, 1)) {
				return "32550-08.htm";
			}
		} else if (event.equalsIgnoreCase("32550-09.htm")) {
			if (!giveReward(st, 13798, 1, 57, 5000)) {
				return "32550-10.htm";
			}
		} else if (event.equalsIgnoreCase("32550-12.htm")) {
			if (!giveReward(st, DRAGONKIN_CHARM_FRAGMENT, 5, 13841, 1)) {
				return "32550-13.htm";
			}
		} else if (event.equalsIgnoreCase("32550-14.htm")) {
			if (!giveReward(st, 13867, 1, 57, 5000)) {
				return "32550-15.htm";
			}
		} else if (event.equalsIgnoreCase("32550-17.htm")) {
			if (!giveReward(st, SPIRIT_STONE_DUST, 5, 15486, 1)) {
				return "32550-18.htm";
			}
		} else if (event.equalsIgnoreCase("32550-19.htm")) {
			if (!giveReward(st, 15535, 1, 57, 5000)) {
				return "32550-20.htm";
			}
		}
		return event;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(player, 3, 1, npc);
		if (st != null) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		if (st.isCreated()) {
			htmltext = (player.getLevel() >= 75) ? "32549-01.htm" : "32549-00.htm";
		} else {
			if (npc.getId() == DILIOS) {
				if (st.isCond(1) && st.hasQuestItems(LEKONS_CERTIFICATE)) {
					htmltext = "32549-04.htm";
					st.takeItems(LEKONS_CERTIFICATE, -1);
					st.setCond(2);
				} else if (st.isCond(2)) {
					htmltext = "32549-05.htm";
				}
			} else {
				if (st.isCond(2)) {
					htmltext = "32550-01.htm";
				} else if (st.isCond(3)) {
					for (int i : QUEST_ITEMS) {
						if (st.getQuestItemsCount(i) > 0) {
							return "32550-05.htm";
						}
					}
					htmltext = "32550-04.htm";
				}
			}
		}
		return htmltext;
	}
	
	private static boolean giveReward(QuestState st, int itemId, int minCount, int rewardItemId, long rewardCount) {
		long count = st.getQuestItemsCount(itemId);
		if (count < minCount) {
			return false;
		}
		
		count = count / minCount;
		st.takeItems(itemId, count * minCount);
		st.rewardItems(rewardItemId, rewardCount * count);
		return true;
	}
}