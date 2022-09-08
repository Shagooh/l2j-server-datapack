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
package com.l2jserver.datapack.quests.Q00642_APowerfulPrimevalCreature;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * A Powerful Primeval Creature (642)
 * @author Adry_85
 */
public class Q00642_APowerfulPrimevalCreature extends Quest {
	// NPC
	private static final int DINN = 32105;
	// Mobs
	private static final int ANCIENT_EGG = 18344;
	// Items
	private static final int DINOSAUR_TISSUE = 8774;
	private static final int DINOSAUR_EGG = 8775;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.bulkAddSingleDrop(DINOSAUR_TISSUE, 30.9)
				.withNpcs(22196, 22197, 22198, 22199, 22218, 22223).build() // Velociraptor, Pterosaur
			.bulkAddSingleDrop(DINOSAUR_TISSUE, 98.8).withNpcs(22215, 22216, 22217).build() // Tyrannosaurus
			.addSingleDrop(ANCIENT_EGG, DINOSAUR_EGG) // Ancient Egg
			.build();
	// Misc
	private static final int MIN_LEVEL = 75;

	public Q00642_APowerfulPrimevalCreature() {
		super(642, Q00642_APowerfulPrimevalCreature.class.getSimpleName(), "A Powerful Primeval Creature");
		addStartNpc(DINN);
		addTalkId(DINN);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(DINOSAUR_TISSUE, DINOSAUR_EGG);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if (qs == null) {
			return null;
		}
		
		String htmltext = event;
		switch (event) {
			case "32105-05.html": {
				qs.startQuest();
				break;
			}
			case "32105-06.htm": {
				qs.exitQuest(true);
				break;
			}
			case "32105-09.html": {
				if (hasQuestItems(player, DINOSAUR_TISSUE)) {
					giveAdena(player, 5000 * getQuestItemsCount(player, DINOSAUR_TISSUE), true);
					takeItems(player, DINOSAUR_TISSUE, -1);
				} else {
					htmltext = "32105-14.html";
				}
				break;
			}
			case "exit": {
				if (hasQuestItems(player, DINOSAUR_TISSUE)) {
					giveAdena(player, 5000 * getQuestItemsCount(player, DINOSAUR_TISSUE), true);
					qs.exitQuest(true, true);
					htmltext = "32105-12.html";
				} else {
					qs.exitQuest(true, true);
					htmltext = "32105-13.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, -1, 3, npc);
		
		if (qs != null) {
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
		}

		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			htmltext = player.getLevel() < MIN_LEVEL ? "32105-01.htm" : "32105-02.htm";
		} else if (qs.isStarted()) {
			htmltext = (hasAtLeastOneQuestItem(player, DINOSAUR_TISSUE, DINOSAUR_EGG)) ? "32105-08.html" : "32105-07.html";
		}
		return htmltext;
	}
}
