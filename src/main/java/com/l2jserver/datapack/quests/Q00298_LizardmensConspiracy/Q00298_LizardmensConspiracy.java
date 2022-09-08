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
package com.l2jserver.datapack.quests.Q00298_LizardmensConspiracy;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * Lizardmen's Conspiracy (298)
 * @author xban1x
 */
public final class Q00298_LizardmensConspiracy extends Quest {
	// Misc
	private static final int MIN_LVL = 25;
	private static final long GEMS_LIMIT = 50L;
	// NPCs
	private static final int GUARD_PRAGA = 30333;
	private static final int MAGISTER_ROHMER = 30344;
	// Items
	private static final int PATROLS_REPORT = 7182;
	private static final QuestItemChanceHolder SHINING_GEM = new QuestItemChanceHolder(7183, GEMS_LIMIT);
	private static final QuestItemChanceHolder SHINING_RED_GEM = new QuestItemChanceHolder(7184, 54.0, GEMS_LIMIT);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(20922, SHINING_GEM, 70.0)
			.addSingleDrop(20924, SHINING_GEM, 75.0)
			.addSingleDrop(20926, SHINING_RED_GEM)
			.addSingleDrop(20927, SHINING_RED_GEM)
			.build();

	public Q00298_LizardmensConspiracy() {
		super(298, Q00298_LizardmensConspiracy.class.getSimpleName(), "Lizardmen's Conspiracy");
		addStartNpc(GUARD_PRAGA);
		addTalkId(GUARD_PRAGA, MAGISTER_ROHMER);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(PATROLS_REPORT, SHINING_GEM.getId(), SHINING_RED_GEM.getId());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String html = null;
		if (qs == null) {
			return html;
		}
		
		switch (event) {
			case "30333-03.htm": {
				if (qs.isCreated()) {
					qs.startQuest();
					giveItems(player, PATROLS_REPORT, 1);
					html = event;
				}
				break;
			}
			case "30344-04.html": {
				if (qs.isCond(1) && hasQuestItems(player, PATROLS_REPORT)) {
					takeItems(player, PATROLS_REPORT, -1);
					qs.setCond(2, true);
					html = event;
				}
				break;
			}
			case "30344-06.html": {
				if (qs.isStarted()) {
					if (qs.isCond(3)) {
						addExpAndSp(player, 0, 42000);
						qs.exitQuest(true, true);
						html = event;
					} else {
						html = "30344-07.html";
					}
				}
				break;
			}
		}
		return html;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, 2, 3, npc);
		if (qs != null) {
			if (giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true)
					&& hasItemsAtLimit(qs.getPlayer(), SHINING_GEM, SHINING_RED_GEM)) {
				qs.setCond(3);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		final QuestState qs = getQuestState(talker, true);
		String html = getNoQuestMsg(talker);
		if (qs.isCreated() && (npc.getId() == GUARD_PRAGA)) {
			html = (talker.getLevel() >= MIN_LVL) ? "30333-01.htm" : "30333-02.htm";
		} else if (qs.isStarted()) {
			if ((npc.getId() == GUARD_PRAGA) && hasQuestItems(talker, PATROLS_REPORT)) {
				html = "30333-04.html";
			} else if (npc.getId() == MAGISTER_ROHMER) {
				switch (qs.getCond()) {
					case 1: {
						html = "30344-01.html";
						break;
					}
					case 2: {
						html = "30344-02.html";
						break;
					}
					case 3: {
						html = "30344-03.html";
						break;
					}
				}
			}
		}
		return html;
	}
}
