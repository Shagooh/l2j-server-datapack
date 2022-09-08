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
package com.l2jserver.datapack.quests.Q00690_JudesRequest;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Jude's Request (690)
 * @author malyelfik
 */
public class Q00690_JudesRequest extends Quest {
	// NPCs
	private static final int JUDE = 32356;
	private static final int LESSER_EVIL = 22398;
	private static final int GREATER_EVIL = 22399;
	// Items
	private static final int EVIL_WEAPON = 10327;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(LESSER_EVIL, EVIL_WEAPON, 17.3)
			.addSingleDrop(GREATER_EVIL, EVIL_WEAPON, 24.6)
			.build();
	// Rewards
	private static final int[][] REWARDS = {
		{
			10373,
			10374,
			10375,
			10376,
			10377,
			10378,
			10379,
			10380,
			10381
		},
		{
			10397,
			10398,
			10399,
			10400,
			10401,
			10402,
			10403,
			10404,
			10405
		}
	};
	
	public Q00690_JudesRequest() {
		super(690, Q00690_JudesRequest.class.getSimpleName(), "Jude's Request");
		addStartNpc(JUDE);
		addTalkId(JUDE);
		addKillId(LESSER_EVIL, GREATER_EVIL);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		String htmltext = event;
		QuestState st = getQuestState(player, false);
		
		if (st == null) {
			return getNoQuestMsg(player);
		}
		
		if (event.equalsIgnoreCase("32356-03.htm")) {
			st.startQuest();
		} else if (event.equalsIgnoreCase("32356-07.htm")) {
			if (st.getQuestItemsCount(EVIL_WEAPON) >= 200) {
				st.giveItems(REWARDS[0][getRandom(REWARDS[0].length)], 1);
				st.takeItems(EVIL_WEAPON, 200);
				st.playSound(Sound.ITEMSOUND_QUEST_MIDDLE);
				htmltext = "32356-07.htm";
			} else {
				htmltext = "32356-07a.htm";
			}
		} else if (event.equalsIgnoreCase("32356-08.htm")) {
			st.takeItems(EVIL_WEAPON, -1);
			st.exitQuest(true, true);
		} else if (event.equalsIgnoreCase("32356-09.htm")) {
			if (st.getQuestItemsCount(EVIL_WEAPON) >= 5) {
				st.giveItems(REWARDS[1][getRandom(REWARDS[1].length)], 1);
				st.takeItems(EVIL_WEAPON, 5);
				st.playSound(Sound.ITEMSOUND_QUEST_MIDDLE);
				htmltext = "32356-09.htm";
			} else {
				htmltext = "32356-09a.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(player, 1, 1, npc);
		if (st != null) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		switch (st.getState()) {
			case State.CREATED:
				if (player.getLevel() >= 78) {
					htmltext = "32356-01.htm";
				} else {
					htmltext = "32356-02.htm";
				}
				break;
			case State.STARTED:
				if (st.getQuestItemsCount(EVIL_WEAPON) >= 200) {
					htmltext = "32356-04.htm";
				} else if (st.getQuestItemsCount(EVIL_WEAPON) < 5) {
					htmltext = "32356-05a.htm";
				} else {
					htmltext = "32356-05.htm";
				}
				break;
		}
		return htmltext;
	}
}
