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
package com.l2jserver.datapack.quests.Q00311_ExpulsionOfEvilSpirits;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Expulsion of Evil Spirits (311)
 * @author Zoey76
 */
public final class Q00311_ExpulsionOfEvilSpirits extends Quest {
	// NPC
	private static final int CHAIREN = 32655;
	// Items
	private static final int PROTECTION_SOULS_PENDANT = 14848;
	private static final int SOUL_CORE_CONTAINING_EVIL_SPIRIT = 14881;
	private static final int RAGNA_ORCS_AMULET = 14882;
	// Misc
	private static final int MIN_LEVEL = 80;
	private static final int SOUL_CORE_COUNT = 10;
	private static final int RAGNA_ORCS_KILLS_COUNT = 100;
	private static final int RAGNA_ORCS_AMULET_COUNT = 10;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22691, RAGNA_ORCS_AMULET, 69.4) // Ragna Orc
			.addSingleDrop(22692, RAGNA_ORCS_AMULET, 71.6) // Ragna Orc Warrior
			.addSingleDrop(22693, RAGNA_ORCS_AMULET, 73.6) // Ragna Orc Hero
			.addSingleDrop(22694, RAGNA_ORCS_AMULET, 71.2) // Ragna Orc Commander
			.addSingleDrop(22695, RAGNA_ORCS_AMULET, 69.8) // Ragna Orc Healer
			.addSingleDrop(22696, RAGNA_ORCS_AMULET, 69.2) // Ragna Orc Shaman
			.addSingleDrop(22697, RAGNA_ORCS_AMULET, 64.0) // Ragna Orc Seer
			.addSingleDrop(22698, RAGNA_ORCS_AMULET, 71.6) // Ragna Orc Archer
			.addSingleDrop(22699, RAGNA_ORCS_AMULET, 75.2) // Ragna Orc Sniper
			.addSingleDrop(22701, RAGNA_ORCS_AMULET, 71.6) // Varangka's Dre Vanul
			.addSingleDrop(22702, RAGNA_ORCS_AMULET, 66.2) // Varangka's Destroyer
			.build();
	
	public Q00311_ExpulsionOfEvilSpirits() {
		super(311, Q00311_ExpulsionOfEvilSpirits.class.getSimpleName(), "Expulsion of Evil Spirits");
		addStartNpc(CHAIREN);
		addTalkId(CHAIREN);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(SOUL_CORE_CONTAINING_EVIL_SPIRIT, RAGNA_ORCS_AMULET);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if (qs == null) {
			return null;
		}
		
		String htmltext = null;
		if (player.getLevel() < MIN_LEVEL) {
			return null;
		}
		
		switch (event) {
			case "32655-03.htm":
			case "32655-15.html": {
				htmltext = event;
				break;
			}
			case "32655-04.htm": {
				qs.startQuest();
				htmltext = event;
				break;
			}
			case "32655-11.html": {
				if (getQuestItemsCount(player, SOUL_CORE_CONTAINING_EVIL_SPIRIT) >= SOUL_CORE_COUNT) {
					takeItems(player, SOUL_CORE_CONTAINING_EVIL_SPIRIT, SOUL_CORE_COUNT);
					giveItems(player, PROTECTION_SOULS_PENDANT, 1);
					htmltext = event;
				} else {
					htmltext = "32655-12.html";
				}
				break;
			}
			case "32655-13.html": {
				if (!hasQuestItems(player, SOUL_CORE_CONTAINING_EVIL_SPIRIT) && (getQuestItemsCount(player, RAGNA_ORCS_AMULET) >= RAGNA_ORCS_AMULET_COUNT)) {
					qs.exitQuest(true, true);
					htmltext = event;
				} else {
					htmltext = "32655-14.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, 1, 2, npc);
		if (qs != null) {
			final int count = qs.getMemoStateEx(1) + 1;
			if ((count >= RAGNA_ORCS_KILLS_COUNT) && (getRandom(20) < ((count % 100) + 1))) {
				qs.setMemoStateEx(1, 0);
				qs.giveItems(SOUL_CORE_CONTAINING_EVIL_SPIRIT, 1);
				qs.playSound(Sound.ITEMSOUND_QUEST_ITEMGET);
			} else {
				qs.setMemoStateEx(1, count);
			}
			
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			htmltext = (player.getLevel() >= MIN_LEVEL) ? "32655-01.htm" : "32655-02.htm";
		} else if (qs.isStarted()) {
			htmltext = !hasQuestItems(player, SOUL_CORE_CONTAINING_EVIL_SPIRIT, RAGNA_ORCS_AMULET) ? "32655-05.html" : "32655-06.html";
		}
		return htmltext;
	}
}
