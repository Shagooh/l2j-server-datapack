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
package com.l2jserver.datapack.quests.Q00372_LegacyOfInsolence;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

import java.util.Map;

/**
 * Legacy Of Insolence (372)
 * @author ivantotov
 * @author Noé Caratini aka Kita
 */
public final class Q00372_LegacyOfInsolence extends Quest {
	// Misc
	private static final int MIN_LEVEL = 59;
	// NPCs
	private static final int TRADER_HOLLY = 30839;
	private static final int WAREHOUSE_KEEPER_WALDERAL = 30844;
	private static final int MAGISTER_DESMOND = 30855;
	private static final int ANTIQUE_DEALER_PATRIN = 30929;
	private static final int CLAUDIA_ATHEBALDT = 31001;
	// Monsters
	private static final int CORRUPT_SAGE = 20817;
	private static final int ERIN_EDIUNCE = 20821;
	private static final int HALLATES_INSPECTOR = 20825;
	private static final int PLATINUM_TRIBE_OVERLORD = 20829;
	private static final int MESSENGER_ANGEL = 21062;
	private static final int PLATINUM_GUARDIAN_PREFECT = 21069;
	// Items
	private static final int ANCIENT_RED_PAPYRUS = 5966;
	private static final int ANCIENT_BLUE_PAPYRUS = 5967;
	private static final int ANCIENT_BLACK_PAPYRUS = 5968;
	private static final int ANCIENT_WHITE_PAPYRUS = 5969;
	private static final int[] REVELATION_OF_THE_SEALS = {5972, 5973, 5974, 5975, 5976, 5977, 5978};
	private static final int[] ANCIENT_EPIC = {5979, 5980, 5981, 5982, 5983};
	private static final int[] IMPERIAL_GENEALOGY = {5984, 5985, 5986, 5987, 5988};
	private static final int[] BLUEPRINTS = {5989, 5990, 5991, 5992, 5993, 5994, 5995, 5996, 5997, 5998, 5999, 6000, 6001};
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(CORRUPT_SAGE, ANCIENT_RED_PAPYRUS, 30.2)
			.addSingleDrop(ERIN_EDIUNCE, ANCIENT_RED_PAPYRUS, 41.0)
			.addSingleDrop(HALLATES_INSPECTOR, ANCIENT_RED_PAPYRUS, 44.7)
			.addSingleDrop(PLATINUM_TRIBE_OVERLORD, ANCIENT_BLUE_PAPYRUS, 45.1)
			.addSingleDrop(MESSENGER_ANGEL, ANCIENT_WHITE_PAPYRUS, 29.0)
			.addSingleDrop(PLATINUM_GUARDIAN_PREFECT, ANCIENT_BLACK_PAPYRUS, 28.0)
			.build();
	private static final Map<Integer, Integer> QS_KILLER_CHANCE = Map.of(
			CORRUPT_SAGE, 1,
			ERIN_EDIUNCE, 1,
			HALLATES_INSPECTOR, 3,
			PLATINUM_TRIBE_OVERLORD, 3,
			MESSENGER_ANGEL, 1,
			PLATINUM_GUARDIAN_PREFECT, 1
	);
	// Rewards
	private static final RewardsGroup REWARDS_DARK_CRYSTAL = new RewardsGroup(
			5496, // Sealed Dark Crystal Boots Lining
			5508, // Sealed Dark Crystal Gloves Design
			5525, // Sealed Dark Crystal Helmet Design
			5368, // Recipe: Sealed Dark Crystal Boots(60%)
			5392, // Recipe: Sealed Dark Crystal Gloves(60%)
			5426 // Recipe: Sealed Dark Crystal Helmet(60%)
	);
	private static final RewardsGroup REWARDS_TALLUM = new RewardsGroup(
			5497, // Sealed Tallum Boots Lining
			5509, // Sealed Tallum Gloves Design
			5526, // Sealed Tallum Helm Design
			5370, // Recipe: Sealed Tallum Boots(60%)
			5394, // Recipe: Sealed Tallum Gloves(60%)
			5428 // Recipe: Sealed Tallum Helmet(60%)
	);
	private static final RewardsGroup REWARDS_NIGHTMARE = new RewardsGroup(
			5502, // Sealed Boots of Nightmare Lining
			5514, // Sealed Gauntlets of Nightmare Design
			5527, // Sealed Helm of Nightmare Design
			5380, // Recipe: Sealed Boots of Nightmare(60%)
			5404, // Recipe: Sealed Gauntlets of Nightmare(60%)
			5430 // Recipe: Sealed Helm of Nightmare(60%)
	);
	private static final RewardsGroup REWARDS_MAJESTIC = new RewardsGroup(
			5503, // Sealed Majestic Boots Lining
			5515, // Sealed Majestic Gauntlets Design
			5528, // Sealed Majestic Circlet Design
			5382, // Recipe: Sealed Majestic Boots(60%)
			5406, // Recipe: Sealed Majestic Gauntlets(60%)
			5432 // Recipe: Sealed Majestic Circlet (60%)
	);
	private static final int[] WALDERAL_CHANCES_LOW_A = {10, 20, 30, 40, 51, 62, 79, 100};
	private static final int[] WALDERAL_CHANCES_HIGH_A = {17, 34, 49, 58, 70, 82, 92, 100};
	private static final int[] OTHER_CHANCES_LOW_A = {30, 60, 80, 90, 100};
	private static final int[] OTHER_CHANCES_HIGH_A = {31, 62, 75, 83, 100};
	
	public Q00372_LegacyOfInsolence() {
		super(372, Q00372_LegacyOfInsolence.class.getSimpleName(), "Legacy Of Insolence");
		addStartNpc(WAREHOUSE_KEEPER_WALDERAL);
		addTalkId(WAREHOUSE_KEEPER_WALDERAL, TRADER_HOLLY, MAGISTER_DESMOND, ANTIQUE_DEALER_PATRIN, CLAUDIA_ATHEBALDT);
		addKillId(DROPLIST.getNpcIds());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if (qs == null) {
			return super.onAdvEvent(event, npc, player);
		}
		
		switch (event) {
			case "30844-04.htm" -> {
				if (qs.isCreated()) {
					qs.startQuest();
				} else {
					return null;
				}
			}
			case "30844-05b.html" -> qs.setCond(2);
			case "30844-07.html" -> {
				if (!hasQuestItems(player, BLUEPRINTS)) {
					return "30844-06.html";
				}
			}
			case "30844-07a.html" -> {
				if (hasQuestItems(player, BLUEPRINTS)) {
					takeItems(player, 1, BLUEPRINTS);
					walderalGiveRewards(player, REWARDS_DARK_CRYSTAL, WALDERAL_CHANCES_LOW_A);
				} else {
					return "30844-07e.html";
				}
			}
			case "30844-07b.html" -> {
				if (hasQuestItems(player, BLUEPRINTS)) {
					takeItems(player, 1, BLUEPRINTS);
					walderalGiveRewards(player, REWARDS_TALLUM, WALDERAL_CHANCES_LOW_A);
				} else {
					return "30844-07e.html";
				}
			}
			case "30844-07c.html" -> {
				if (hasQuestItems(player, BLUEPRINTS)) {
					takeItems(player, 1, BLUEPRINTS);
					walderalGiveRewards(player, REWARDS_NIGHTMARE, WALDERAL_CHANCES_HIGH_A);
				} else {
					return "30844-07e.html";
				}
			}
			case "30844-07d.html" -> {
				if (hasQuestItems(player, BLUEPRINTS)) {
					takeItems(player, 1, BLUEPRINTS);
					walderalGiveRewards(player, REWARDS_MAJESTIC, WALDERAL_CHANCES_HIGH_A);
				} else {
					return "30844-07e.html";
				}
			}
			case "30844-09.html" -> qs.exitQuest(true, true);
			case "30844-03.htm", "30844-05.html", "30844-05a.html", "30844-08.html", "30844-10.html", "30844-11.html" -> {
				return event;
			}
			default -> {
				return null;
			}
		}
		return event;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, -1, QS_KILLER_CHANCE.get(npc.getId()), npc);
		if (qs != null) {
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			if (npc.getId() == WAREHOUSE_KEEPER_WALDERAL) {
				if (player.getLevel() < MIN_LEVEL) {
					htmltext = "30844-01.htm";
				} else {
					htmltext = "30844-02.htm";
				}
			}
		} else if (qs.isStarted()) {
			switch (npc.getId()) {
				case WAREHOUSE_KEEPER_WALDERAL -> htmltext = "30844-05.html";
				case TRADER_HOLLY -> {
					if (hasQuestItems(player, IMPERIAL_GENEALOGY)) {
						takeItems(player, 1, IMPERIAL_GENEALOGY);
						otherGiveRewards(player, REWARDS_DARK_CRYSTAL, OTHER_CHANCES_LOW_A);
						htmltext = "30839-02.html";
					} else {
						htmltext = "30839-01.html";
					}
				}
				case MAGISTER_DESMOND -> {
					if (hasQuestItems(player, REVELATION_OF_THE_SEALS)) {
						takeItems(player, 1, REVELATION_OF_THE_SEALS);
						otherGiveRewards(player, REWARDS_MAJESTIC, OTHER_CHANCES_HIGH_A);
						htmltext = "30855-02.html";
					} else {
						htmltext = "30855-01.html";
					}
				}
				case ANTIQUE_DEALER_PATRIN -> {
					if (hasQuestItems(player, ANCIENT_EPIC)) {
						takeItems(player, 1, ANCIENT_EPIC);
						otherGiveRewards(player, REWARDS_TALLUM, OTHER_CHANCES_LOW_A);
						htmltext = "30929-02.html";
					} else {
						htmltext = "30929-01.html";
					}
				}
				case CLAUDIA_ATHEBALDT -> {
					if (hasQuestItems(player, REVELATION_OF_THE_SEALS)) {
						takeItems(player, 1, REVELATION_OF_THE_SEALS);
						otherGiveRewards(player, REWARDS_NIGHTMARE, OTHER_CHANCES_HIGH_A);
						htmltext = "31001-02.html";
					} else {
						htmltext = "31001-01.html";
					}
				}
			}
		}
		return htmltext;
	}
	
	private void walderalGiveRewards(L2PcInstance player, RewardsGroup rewardsGroup, int[] chances) {
		final int chance = getRandom(100);
		
		if (chance < chances[0]) {
			giveItems(player, rewardsGroup.partBoots, 1);
		} else if (chance < chances[1]) {
			giveItems(player, rewardsGroup.partGloves, 1);
		} else if (chance < chances[2]) {
			giveItems(player, rewardsGroup.partHelmet, 1);
		} else if (chance < chances[3]) {
			giveItems(player, rewardsGroup.partBoots, 1);
			giveItems(player, rewardsGroup.partGloves, 1);
			giveItems(player, rewardsGroup.partHelmet, 1);
		} else if (chance < chances[4]) {
			giveItems(player, rewardsGroup.recipeBoots, 1);
		} else if (chance < chances[5]) {
			giveItems(player, rewardsGroup.recipeGloves, 1);
		} else if (chance < chances[6]) {
			giveItems(player, rewardsGroup.recipeHelmet, 1);
		} else if (chance < chances[7]) {
			giveItems(player, rewardsGroup.recipeBoots, 1);
			giveItems(player, rewardsGroup.recipeGloves, 1);
			giveItems(player, rewardsGroup.recipeHelmet, 1);
		}
	}
	
	private void otherGiveRewards(L2PcInstance player, RewardsGroup rewardsGroup, int[] chances) {
		final int chance = getRandom(100);
		
		if (chance < chances[0]) {
			giveItems(player, rewardsGroup.partBoots, 1);
		} else if (chance < chances[1]) {
			giveItems(player, rewardsGroup.partGloves, 1);
		} else if (chance < chances[2]) {
			giveItems(player, rewardsGroup.partHelmet, 1);
		} else if (chance < chances[3]) {
			giveItems(player, rewardsGroup.partBoots, 1);
			giveItems(player, rewardsGroup.partGloves, 1);
			giveItems(player, rewardsGroup.partHelmet, 1);
		} else if (chance < chances[4]) {
			giveAdena(player, 4000, true);
		}
	}
	
	private record RewardsGroup(
			int partBoots, int partGloves, int partHelmet,
			int recipeBoots, int recipeGloves, int recipeHelmet
	) {}
}
