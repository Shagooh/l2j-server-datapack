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
package com.l2jserver.datapack.quests.Q00695_DefendTheHallOfSuffering;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Defend the Hall of Suffering (695)
 * @author Adry_85
 * @since 2.6.0.0
 */
public class Q00695_DefendTheHallOfSuffering extends Quest {
	// NPC
	private static final int TEPIOS = 32603;
	// Misc
	private static final int MIN_LEVEL = 75;
	private static final int MAX_LEVEL = 82;
	
	public Q00695_DefendTheHallOfSuffering() {
		super(695, Q00695_DefendTheHallOfSuffering.class.getSimpleName(), "Defend the Hall of Suffering");
		addStartNpc(TEPIOS);
		addTalkId(TEPIOS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "32603-02.html": {
				htmltext = event;
				break;
			}
			case "32603-03.htm": {
				if (player.getLevel() >= MIN_LEVEL) {
					st.startQuest(false);
					st.setMemoState(2);
					st.playSound(Sound.ITEMSOUND_QUEST_MIDDLE);
					htmltext = event;
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			final int playerLevel = player.getLevel();
			if ((playerLevel >= MIN_LEVEL) && (playerLevel <= MAX_LEVEL)) {
				// TODO (Adry_85): This quest can only be carried out during the Seed of Infinity 4th period or Seed of Infinity 5th period.
				htmltext = "32603-01.htm";
			} else if (playerLevel < MIN_LEVEL) {
				htmltext = "32603-04.htm";
			} else {
				htmltext = "32603-05.html";
			}
		} else if (st.isStarted() && (st.isMemoState(2))) {
			htmltext = "32603-06.htm";
		}
		return htmltext;
	}
}
