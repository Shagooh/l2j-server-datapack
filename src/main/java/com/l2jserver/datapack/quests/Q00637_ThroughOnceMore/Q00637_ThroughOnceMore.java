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
package com.l2jserver.datapack.quests.Q00637_ThroughOnceMore;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Through the Gate Once More (637)<br>
 * Original Jython script by BiTi! and DrLecter.
 * @author DS
 */
public final class Q00637_ThroughOnceMore extends Quest {
	// NPCs
	private static final int FLAURON = 32010;
	// Monsters
	private static final int[] MOBS = {
		21565,
		21566,
		21567
	};
	// Items
	private static final int VISITOR_MARK = 8064;
	private static final int FADED_MARK = 8065;
	private static final int MARK = 8067;
	private static final QuestItemChanceHolder NECRO_HEART = new QuestItemChanceHolder(8066, 90.0, 10L);

	public Q00637_ThroughOnceMore() {
		super(637, Q00637_ThroughOnceMore.class.getSimpleName(), "Through the Gate Once More");
		addStartNpc(FLAURON);
		addTalkId(FLAURON);
		addKillId(MOBS);
		registerQuestItems(NECRO_HEART.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		if ("32010-03.htm".equals(event)) {
			st.startQuest();
		} else if ("32010-10.htm".equals(event)) {
			st.exitQuest(true);
		}
		return event;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		final QuestState st = getQuestState(player, false);
		if ((st != null) && st.isStarted() && giveItemRandomly(st.getPlayer(), npc, NECRO_HEART, true)) {
			st.setCond(2);
		}
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		if (st == null) {
			return getNoQuestMsg(player);
		}
		
		final int id = st.getState();
		if (id == State.CREATED) {
			if (player.getLevel() > 72) {
				if (st.hasQuestItems(FADED_MARK)) {
					return "32010-02.htm";
				}
				if (st.hasQuestItems(VISITOR_MARK)) {
					st.exitQuest(true);
					return "32010-01a.htm";
				}
				if (st.hasQuestItems(MARK)) {
					st.exitQuest(true);
					return "32010-0.htm";
				}
			}
			st.exitQuest(true);
			return "32010-01.htm";
		} else if (id == State.STARTED) {
			if ((st.isCond(2)) && hasItemsAtLimit(st.getPlayer(), NECRO_HEART)) {
				st.takeItems(NECRO_HEART.getId(), 10);
				st.takeItems(FADED_MARK, 1);
				st.giveItems(MARK, 1);
				st.giveItems(8273, 10);
				st.exitQuest(true, true);
				return "32010-05.htm";
			}
			return "32010-04.htm";
		}
		return getNoQuestMsg(player);
	}
}
