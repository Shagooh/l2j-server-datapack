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
package com.l2jserver.datapack.quests.Q00619_RelicsOfTheOldEmpire;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestDroplist.QuestDropInfo;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Relics of the Old Empire (619)
 * @author Adry_85, jurchiks
 */
public final class Q00619_RelicsOfTheOldEmpire extends Quest {
	// NPC
	private static final int GHOST_OF_ADVENTURER = 31538;
	// Monsters
	// @formatter:off
	private static final int[] ARCHON_OF_HALISHA = {
			18212, 18213, 18214, 18215, 18216, 18217, 18218, 18219
	};
	// @formatter:on
	// Items
	private static final QuestItemChanceHolder ENTRANCE_PASS_TO_THE_SEPULCHER = new QuestItemChanceHolder(7075, 3.333);
	private static final QuestItemChanceHolder BROKEN_RELIC_PART = new QuestItemChanceHolder(7254);
	// Misc
	private static final int MIN_LEVEL = 74;
	private static final int REQUIRED_RELIC_COUNT = 1000;
	// Reward
	private static final int[] RECIPES = {
		6881, // Recipe: Forgotten Blade (60%)
		6883, // Recipe: Basalt Battlehammer (60%)
		6885, // Recipe: Imperial Staff (60%)
		6887, // Recipe: Angel Slayer (60%)
		6891, // Recipe: Dragon Hunter Axe (60%)
		6893, // Recipe: Saint Spear (60%)
		6895, // Recipe: Demon Splinter (60%)
		6897, // Recipe: Heavens Divider (60%)
		6899, // Recipe: Arcana Mace (60%)
		7580, // Recipe: Draconic Bow (60%)
	};

	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(21396, BROKEN_RELIC_PART, 51.0).addSingleDrop(21396, ENTRANCE_PASS_TO_THE_SEPULCHER) // carrion_scarab
			.addSingleDrop(21397, BROKEN_RELIC_PART, 50.0).addSingleDrop(21396, ENTRANCE_PASS_TO_THE_SEPULCHER) // carrion_scarab_a
			.addSingleDrop(21398, BROKEN_RELIC_PART, 95.0).addSingleDrop(21398, ENTRANCE_PASS_TO_THE_SEPULCHER) // soldier_scarab
			.addSingleDrop(21399, BROKEN_RELIC_PART, 84.0).addSingleDrop(21399, ENTRANCE_PASS_TO_THE_SEPULCHER) // soldier_scarab_a
			.addSingleDrop(21400, BROKEN_RELIC_PART, 76.0).addSingleDrop(21400, ENTRANCE_PASS_TO_THE_SEPULCHER) // hexa_beetle
			.addSingleDrop(21401, BROKEN_RELIC_PART, 67.0).addSingleDrop(21401, ENTRANCE_PASS_TO_THE_SEPULCHER) // hexa_beetle_a
			.addSingleDrop(21402, BROKEN_RELIC_PART, 69.0).addSingleDrop(21402, ENTRANCE_PASS_TO_THE_SEPULCHER) // katraxith
			.addSingleDrop(21403, BROKEN_RELIC_PART, 80.0).addSingleDrop(21403, ENTRANCE_PASS_TO_THE_SEPULCHER) // katraxith_a
			.addSingleDrop(21404, BROKEN_RELIC_PART, 90.0).addSingleDrop(21404, ENTRANCE_PASS_TO_THE_SEPULCHER) // tera_beetle
			.addSingleDrop(21405, BROKEN_RELIC_PART, 64.0).addSingleDrop(21405, ENTRANCE_PASS_TO_THE_SEPULCHER) // tera_beetle_a
			.addSingleDrop(21406, BROKEN_RELIC_PART, 87.0).addSingleDrop(21406, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_knight
			.addSingleDrop(21407, BROKEN_RELIC_PART, 56.0).addSingleDrop(21407, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_knight_a
			.addSingleDrop(21408, BROKEN_RELIC_PART, 82.0).addSingleDrop(21408, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_guard
			.addSingleDrop(21409, BROKEN_RELIC_PART, 92.0).addSingleDrop(21409, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_guard_a
			.addSingleDrop(21410, BROKEN_RELIC_PART, 81.0).addSingleDrop(21410, ENTRANCE_PASS_TO_THE_SEPULCHER) // guardian_scarab
			.addSingleDrop(21411, BROKEN_RELIC_PART, 66.0).addSingleDrop(21411, ENTRANCE_PASS_TO_THE_SEPULCHER) // guardian_scarab_a
			.addSingleDrop(21412, BROKEN_RELIC_PART, 106.0).addSingleDrop(21412, ENTRANCE_PASS_TO_THE_SEPULCHER) // ustralith
			.addSingleDrop(21413, BROKEN_RELIC_PART, 81.0).addSingleDrop(21413, ENTRANCE_PASS_TO_THE_SEPULCHER) // ustralith_a
			.addSingleDrop(21414, BROKEN_RELIC_PART, 79.0).addSingleDrop(21414, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_assassin
			.addSingleDrop(21415, BROKEN_RELIC_PART, 80.0).addSingleDrop(21415, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_assassin_a
			.addSingleDrop(21416, BROKEN_RELIC_PART, 82.0).addSingleDrop(21416, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_warlord
			.addSingleDrop(21417, BROKEN_RELIC_PART, 127.0).addSingleDrop(21417, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_warlord_a
			.addSingleDrop(21418, BROKEN_RELIC_PART, 66.0).addSingleDrop(21418, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_highguard
			.addSingleDrop(21419, BROKEN_RELIC_PART, 67.0).addSingleDrop(21419, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_highguard_a
			.addSingleDrop(21420, BROKEN_RELIC_PART, 82.0).addSingleDrop(21420, ENTRANCE_PASS_TO_THE_SEPULCHER) // ashuras
			.addSingleDrop(21421, BROKEN_RELIC_PART, 77.0).addSingleDrop(21421, ENTRANCE_PASS_TO_THE_SEPULCHER) // ashuras_a
			.addSingleDrop(21422, BROKEN_RELIC_PART, 88.0).addSingleDrop(21422, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_dancer
			.addSingleDrop(21423, BROKEN_RELIC_PART, 94.0).addSingleDrop(21423, ENTRANCE_PASS_TO_THE_SEPULCHER) // imperial_dancer_a
			.addSingleDrop(21424, BROKEN_RELIC_PART, 119.0).addSingleDrop(21424, ENTRANCE_PASS_TO_THE_SEPULCHER) // ashikenas
			.addSingleDrop(21425, BROKEN_RELIC_PART, 121.0).addSingleDrop(21425, ENTRANCE_PASS_TO_THE_SEPULCHER) // ashikenas_a
			.addSingleDrop(21426, BROKEN_RELIC_PART, 108.0).addSingleDrop(21426, ENTRANCE_PASS_TO_THE_SEPULCHER) // abraxian
			.addSingleDrop(21427, BROKEN_RELIC_PART, 74.0).addSingleDrop(21427, ENTRANCE_PASS_TO_THE_SEPULCHER) // abraxian_a
			.addSingleDrop(21428, BROKEN_RELIC_PART, 76.0).addSingleDrop(21428, ENTRANCE_PASS_TO_THE_SEPULCHER) // hasturan
			.addSingleDrop(21429, BROKEN_RELIC_PART, 80.0).addSingleDrop(21429, ENTRANCE_PASS_TO_THE_SEPULCHER) // hasturan_a
			.addSingleDrop(21430, BROKEN_RELIC_PART, 110.0).addSingleDrop(21430, ENTRANCE_PASS_TO_THE_SEPULCHER) // ahrimanes
			.addSingleDrop(21431, BROKEN_RELIC_PART, 94.0).addSingleDrop(21431, ENTRANCE_PASS_TO_THE_SEPULCHER) // ahrimanes_a
			.addSingleDrop(21432, BROKEN_RELIC_PART, 134.0).addSingleDrop(21432, ENTRANCE_PASS_TO_THE_SEPULCHER) // chakram_beetle
			.addSingleDrop(21433, BROKEN_RELIC_PART, 134.0).addSingleDrop(21433, ENTRANCE_PASS_TO_THE_SEPULCHER) // jamadhr_beetle
			.addSingleDrop(21434, BROKEN_RELIC_PART, 190.0).addSingleDrop(21434, ENTRANCE_PASS_TO_THE_SEPULCHER) // priest_of_blood
			.addSingleDrop(21435, BROKEN_RELIC_PART, 160.0).addSingleDrop(21435, ENTRANCE_PASS_TO_THE_SEPULCHER) // sacrifice_guide
			.addSingleDrop(21436, BROKEN_RELIC_PART, 166.0).addSingleDrop(21436, ENTRANCE_PASS_TO_THE_SEPULCHER) // sacrifice_bearer
			.addSingleDrop(21437, BROKEN_RELIC_PART, 69.0).addSingleDrop(21437, ENTRANCE_PASS_TO_THE_SEPULCHER) // sacrifice_scarab
			.addSingleDrop(21798, BROKEN_RELIC_PART, 33.0).addSingleDrop(21798, ENTRANCE_PASS_TO_THE_SEPULCHER) // guard_skeleton_2d
			.addSingleDrop(21799, BROKEN_RELIC_PART, 61.0).addSingleDrop(21799, ENTRANCE_PASS_TO_THE_SEPULCHER) // guard_skeleton_3d
			.addSingleDrop(21800, BROKEN_RELIC_PART, 31.0).addSingleDrop(21800, ENTRANCE_PASS_TO_THE_SEPULCHER) // guard_undead
			.addSingleDrop(18120, BROKEN_RELIC_PART, 128.0) // r11_roomboss_strong
			.addSingleDrop(18121, BROKEN_RELIC_PART, 121.0) // r11_roomboss_weak
			.addSingleDrop(18122, BROKEN_RELIC_PART, 93.0) // r11_roomboss_teleport
			.addSingleDrop(18123, BROKEN_RELIC_PART, 128.0) // r12_roomboss_strong
			.addSingleDrop(18124, BROKEN_RELIC_PART, 121.0) // r12_roomboss_weak
			.addSingleDrop(18125, BROKEN_RELIC_PART, 93.0) // r12_roomboss_teleport
			.addSingleDrop(18126, BROKEN_RELIC_PART, 128.0) // r13_roomboss_strong
			.addSingleDrop(18127, BROKEN_RELIC_PART, 121.0) // r13_roomboss_weak
			.addSingleDrop(18128, BROKEN_RELIC_PART, 93.0) // r13_roomboss_teleport
			.addSingleDrop(18129, BROKEN_RELIC_PART, 128.0) // r14_roomboss_strong
			.addSingleDrop(18130, BROKEN_RELIC_PART, 121.0) // r14_roomboss_weak
			.addSingleDrop(18131, BROKEN_RELIC_PART, 93.0) // r14_roomboss_teleport
			.addSingleDrop(18132, BROKEN_RELIC_PART, 130.0) // r1_beatle_healer
			.addSingleDrop(18133, BROKEN_RELIC_PART, 120.0) // r1_scorpion_warrior
			.addSingleDrop(18134, BROKEN_RELIC_PART, 90.0) // r1_warrior_longatk1_h
			.addSingleDrop(18135, BROKEN_RELIC_PART, 120.0) // r1_warrior_longatk2
			.addSingleDrop(18136, BROKEN_RELIC_PART, 120.0) // r1_warrior_selfbuff
			.addSingleDrop(18137, BROKEN_RELIC_PART, 89.0) // r1_wizard_h
			.addSingleDrop(18138, BROKEN_RELIC_PART, 119.0) // r1_wizard_clanbuff
			.addSingleDrop(18139, BROKEN_RELIC_PART, 117.0) // r1_wizard_debuff
			.addSingleDrop(18140, BROKEN_RELIC_PART, 119.0) // r1_wizard_selfbuff
			.addSingleDrop(18141, BROKEN_RELIC_PART, 76.0) // r21_scarab_roombosss
			.addSingleDrop(18142, BROKEN_RELIC_PART, 76.0) // r22_scarab_roombosss
			.addSingleDrop(18143, BROKEN_RELIC_PART, 76.0) // r23_scarab_roombosss
			.addSingleDrop(18144, BROKEN_RELIC_PART, 76.0) // r24_scarab_roombosss
			.addSingleDrop(18145, BROKEN_RELIC_PART, 65.0) // r2_wizard_clanbuff
			.addSingleDrop(18146, BROKEN_RELIC_PART, 66.0) // r2_warrior_longatk2
			.addSingleDrop(18147, BROKEN_RELIC_PART, 62.0) // r2_wizard
			.addSingleDrop(18148, BROKEN_RELIC_PART, 72.0) // r2_warrior
			.addSingleDrop(18149, BROKEN_RELIC_PART, 63.0) // r2_bomb
			.addSingleDrop(18166, BROKEN_RELIC_PART, 92.0) // r3_warrior
			.addSingleDrop(18167, BROKEN_RELIC_PART, 92.0) // r3_warrior_longatk1_h
			.addSingleDrop(18168, BROKEN_RELIC_PART, 93.0) // r3_warrior_longatk2
			.addSingleDrop(18169, BROKEN_RELIC_PART, 90.0) // r3_warrior_selfbuff
			.addSingleDrop(18170, BROKEN_RELIC_PART, 90.0) // r3_wizard_h
			.addSingleDrop(18171, BROKEN_RELIC_PART, 94.0) // r3_wizard_clanbuff
			.addSingleDrop(18172, BROKEN_RELIC_PART, 89.0) // r3_wizard_selfbuff
			.addSingleDrop(18173, BROKEN_RELIC_PART, 99.0) // r41_roomboss_strong
			.addSingleDrop(18174, BROKEN_RELIC_PART, 122.0) // r41_roomboss_weak
			.addSingleDrop(18175, BROKEN_RELIC_PART, 93.0) // r41_roomboss_teleport
			.addSingleDrop(18176, BROKEN_RELIC_PART, 99.0) // r42_roomboss_strong
			.addSingleDrop(18177, BROKEN_RELIC_PART, 122.0) // r42_roomboss_weak
			.addSingleDrop(18178, BROKEN_RELIC_PART, 93.0) // r42_roomboss_teleport
			.addSingleDrop(18179, BROKEN_RELIC_PART, 99.0) // r43_roomboss_strong
			.addSingleDrop(18180, BROKEN_RELIC_PART, 122.0) // r43_roomboss_weak
			.addSingleDrop(18181, BROKEN_RELIC_PART, 93.0) // r43_roomboss_teleport
			.addSingleDrop(18182, BROKEN_RELIC_PART, 122.0) // r44_roomboss_weak
			.addSingleDrop(18183, BROKEN_RELIC_PART, 99.0) // r44_roomboss_strong
			.addSingleDrop(18184, BROKEN_RELIC_PART, 93.0) // r44_roomboss_teleport
			.addSingleDrop(18185, BROKEN_RELIC_PART, 123.0) // r4_healer_srddmagic
			.addSingleDrop(18186, BROKEN_RELIC_PART, 124.0) // r4_hearler_srdebuff
			.addSingleDrop(18187, BROKEN_RELIC_PART, 120.0) // r4_warrior
			.addSingleDrop(18188, BROKEN_RELIC_PART, 90.0) // r4_warrior_longatk1_h
			.addSingleDrop(18189, BROKEN_RELIC_PART, 120.0) // r4_warrior_longatk2
			.addSingleDrop(18190, BROKEN_RELIC_PART, 120.0) // r4_warrior_selfbuff
			.addSingleDrop(18191, BROKEN_RELIC_PART, 89.0) // r4_wizard_h
			.addSingleDrop(18192, BROKEN_RELIC_PART, 119.0) // r4_wizard_clanbuff
			.addSingleDrop(18193, BROKEN_RELIC_PART, 117.0) // r4_wizard_debuff
			.addSingleDrop(18194, BROKEN_RELIC_PART, 119.0) // r4_wizard_selfbuff
			.addSingleDrop(18195, BROKEN_RELIC_PART, 91.0) // r4_bomb
			.addSingleDrop(18220, BROKEN_RELIC_PART, 124.0) // r5_healer1
			.addSingleDrop(18221, BROKEN_RELIC_PART, 127.0) // r5_healer2
			.addSingleDrop(18222, BROKEN_RELIC_PART, 121.0) // r5_warrior
			.addSingleDrop(18223, BROKEN_RELIC_PART, 90.0) // r5_warrior_longatk1_h
			.addSingleDrop(18224, BROKEN_RELIC_PART, 122.0) // r5_warrior_longatk2
			.addSingleDrop(18225, BROKEN_RELIC_PART, 121.0) // r5_warrior_sbuff
			.addSingleDrop(18226, BROKEN_RELIC_PART, 89.0) // r5_wizard_h
			.addSingleDrop(18227, BROKEN_RELIC_PART, 153.0) // r5_wizard_clanbuff
			.addSingleDrop(18228, BROKEN_RELIC_PART, 115.0) // r5_wizard_debuff
			.addSingleDrop(18229, BROKEN_RELIC_PART, 119.0) // r5_wizard_slefbuff
			.addSingleDrop(18230, BROKEN_RELIC_PART, 49.0) // r5_bomb
			.bulkAddSingleDrop(BROKEN_RELIC_PART, 379.0).withNpcs(ARCHON_OF_HALISHA).build()
			.build();
	
	public Q00619_RelicsOfTheOldEmpire() {
		super(619, Q00619_RelicsOfTheOldEmpire.class.getSimpleName(), "Relics of the Old Empire");
		addStartNpc(GHOST_OF_ADVENTURER);
		addTalkId(GHOST_OF_ADVENTURER);
		addKillId(DROPLIST.getNpcIds());
		addKillId(ARCHON_OF_HALISHA);
		registerQuestItems(BROKEN_RELIC_PART.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "31538-02.htm": {
				st.startQuest();
				htmltext = event;
				break;
			}
			case "31538-05.html": {
				htmltext = event;
				break;
			}
			case "31538-06.html": {
				if (st.getQuestItemsCount(BROKEN_RELIC_PART.getId()) >= REQUIRED_RELIC_COUNT) {
					st.rewardItems(RECIPES[getRandom(RECIPES.length)], 1);
					st.takeItems(BROKEN_RELIC_PART.getId(), REQUIRED_RELIC_COUNT);
					htmltext = event;
				}
				break;
			}
			case "31538-08.html": {
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(player, -1, 3, npc);
		if (st != null) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);

			QuestDropInfo passDropInfo = DROPLIST.get(npc, ENTRANCE_PASS_TO_THE_SEPULCHER);
			if (passDropInfo != null) {
				giveItemRandomly(st.getPlayer(), npc, ENTRANCE_PASS_TO_THE_SEPULCHER, false);
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LEVEL) ? "31538-01.htm" : "31538-03.html");
		} else if (st.isStarted()) {
			htmltext = ((getQuestItemsCount(player, BROKEN_RELIC_PART.getId()) >= REQUIRED_RELIC_COUNT) ? "31538-04.html" : "31538-07.html");
		}
		return htmltext;
	}
}
