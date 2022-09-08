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
package com.l2jserver.datapack.quests.Q00638_SeekersOfTheHolyGrail;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestDroplist.QuestDropInfo;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Seekers Of The Holy Grail (638)
 * @author netvirus
 */
public final class Q00638_SeekersOfTheHolyGrail extends Quest {
	// NPC
	private static final int INNOCENTIN = 31328;
	// Items
	private static final int TOTEM = 8068;
	private static final int ANTEROOM_KEY = 8273;
	private static final int CHAPEL_KEY = 8274;
	private static final int KEY_OF_DARKNESS = 8275;
	// Misc
	private static final int MIN_LVL = 73;
	private static final int TOTEMS_REQUIRED_COUNT = 2000;
	// Rewards
	private static final int SCROLL_ENCHANT_W_S = 959;
	private static final int SCROLL_ENCHANT_A_S = 960;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22136, TOTEM, 55.0) // Gatekeeper Zombie
			.addSingleDrop(22137, TOTEM, 6.0) // Penance Guard
			.addSingleDrop(22138, TOTEM, 6.0) // Chapel Guard
			.addSingleDrop(22139, TOTEM, 54.0) // Old Aristocrat's Soldier
			.addSingleDrop(22140, TOTEM, 54.0) // Zombie Worker
			.addSingleDrop(22141, TOTEM, 55.0) // Forgotten Victim
			.addSingleDrop(22142, TOTEM, 54.0) // Triol's Layperson
			.addSingleDrop(22143, TOTEM, 62.0) // Triol's Believer
			.addSingleDrop(22144, TOTEM, 54.0) // Resurrected Temple Knight
			.addSingleDrop(22145, TOTEM, 53.0) // Ritual Sacrifice
			.addSingleDrop(22146, TOTEM, 54.0) // Triol's Priest
			.addSingleDrop(22147, TOTEM, 55.0) // Ritual Offering
			.addSingleDrop(22148, TOTEM, 45.0) // Triol's Believer
			.addSingleDrop(22149, TOTEM, 54.0) // Ritual Offering
			.addSingleDrop(22150, TOTEM, 46.0) // Triol's Believer
			.addSingleDrop(22151, TOTEM, 62.0) // Triol's Priest
			.addSingleDrop(22152, TOTEM, 55.0) // Temple Guard
			.addSingleDrop(22153, TOTEM, 54.0) // Temple Guard Captain
			.addSingleDrop(22154, TOTEM, 53.0) // Ritual Sacrifice
			.addSingleDrop(22155, TOTEM, 75.0) // Triol's High Priest
			.addSingleDrop(22156, TOTEM, 67.0) // Triol's Priest
			.addSingleDrop(22157, TOTEM, 66.0) // Triol's Priest
			.addSingleDrop(22158, TOTEM, 67.0) // Triol's Believer
			.addSingleDrop(22159, TOTEM, 75.0) // Triol's High Priest
			.addSingleDrop(22160, TOTEM, 67.0) // Triol's Priest
			.addSingleDrop(22161, TOTEM, 78.0) // Ritual Sacrifice
			.addSingleDrop(22162, TOTEM, 67.0) // Triol's Believer
			.addSingleDrop(22163, TOTEM, 87.0) // Triol's High Priest
			.addSingleDrop(22164, TOTEM, 67.0) // Triol's Believer
			.addSingleDrop(22165, TOTEM, 66.0) // Triol's Priest
			.addSingleDrop(22166, TOTEM, 66.0) // Triol's Believer
			.addSingleDrop(22167, TOTEM, 75.0) // Triol's High Priest
			.addSingleDrop(22168, TOTEM, 66.0) // Triol's Priest
			.addSingleDrop(22169, TOTEM, 78.0) // Ritual Sacrifice
			.addSingleDrop(22170, TOTEM, 67.0) // Triol's Believer
			.addSingleDrop(22171, TOTEM, 87.0) // Triol's High Priest
			.addSingleDrop(22172, TOTEM, 78.0) // Ritual Sacrifice
			.addSingleDrop(22173, TOTEM, 66.0) // Triol's Priest
			.addSingleDrop(22174, TOTEM, 67.0) // Triol's Priest
			.addSingleDrop(22175, TOTEM, 3.0) // Andreas' Captain of the Royal Guard
			.addSingleDrop(22176, TOTEM, 3.0) // Andreas' Royal Guards
			.addSingleDrop(22188, TOTEM, 3.0) // Andreas' Captain of the Royal Guard
			.addSingleDrop(22189, TOTEM, 3.0) // Andreas' Royal Guards
			.addSingleDrop(22190, TOTEM, 3.0) // Ritual Sacrifice
			.addSingleDrop(22191, TOTEM, 3.0) // Andreas' Captain of the Royal Guard
			.addSingleDrop(22192, TOTEM, 3.0) // Andreas' Royal Guards
			.addSingleDrop(22193, TOTEM, 3.0) // Andreas' Royal Guards
			.addSingleDrop(22194, TOTEM, 3.0) // Penance Guard
			.addSingleDrop(22195, TOTEM, 3.0) // Ritual Sacrifice
			.build();
	private static final QuestDroplist DROPLIST_KEYS = QuestDroplist.builder()
			.addSingleDrop(22143, CHAPEL_KEY, 1) // Triol's Believer
			.addSingleDrop(22146, KEY_OF_DARKNESS, 1, 10.0) // Triol's Priest
			.addSingleDrop(22149, ANTEROOM_KEY, 6) // Ritual Offering
			.addSingleDrop(22151, KEY_OF_DARKNESS, 1, 10.0) // Triol's Priest
			.build();
	
	public Q00638_SeekersOfTheHolyGrail() {
		super(638, Q00638_SeekersOfTheHolyGrail.class.getSimpleName(), "Seekers Of The Holy Grail");
		addStartNpc(INNOCENTIN);
		addTalkId(INNOCENTIN);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(TOTEM);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs == null) {
			return htmltext;
		}
		
		switch (event) {
			case "31328-03.htm": {
				if (qs.isCreated()) {
					qs.startQuest();
					htmltext = event;
				}
				break;
			}
			case "31328-06.html": {
				if (qs.isStarted()) {
					htmltext = event;
				}
				break;
			}
			case "reward": {
				if (qs.isStarted() && (getQuestItemsCount(player, TOTEM) >= TOTEMS_REQUIRED_COUNT)) {
					if (getRandom(100) < 80) {
						if (getRandomBoolean()) {
							rewardItems(player, SCROLL_ENCHANT_A_S, 1);
						} else {
							rewardItems(player, SCROLL_ENCHANT_W_S, 1);
						}
						htmltext = "31328-07.html";
					} else {
						giveAdena(player, 3576000, true);
						htmltext = "31328-08.html";
					}
					takeItems(player, TOTEM, 2000);
				}
				break;
			}
			case "31328-09.html": {
				if (qs.isStarted()) {
					qs.exitQuest(true, true);
					htmltext = "31328-09.html";
				}
			}
			
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, -1, 3, npc);
		if (qs != null) {
			if (giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true)) {
				QuestDropInfo keyDropInfo = DROPLIST_KEYS.get(npc);
				if (keyDropInfo != null) {
					ItemHolder keyItemHolder = keyDropInfo.drop().calculateDrops(npc, qs.getPlayer()).get(0);
					npc.dropItem(qs.getPlayer(), keyItemHolder.getId(), keyItemHolder.getCount());
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LVL) ? "31328-01.htm" : "31328-02.htm");
		} else if (qs.isStarted()) {
			htmltext = ((getQuestItemsCount(player, TOTEM) >= TOTEMS_REQUIRED_COUNT) ? "31328-04.html" : "31328-05.html");
		}
		return htmltext;
	}
}
