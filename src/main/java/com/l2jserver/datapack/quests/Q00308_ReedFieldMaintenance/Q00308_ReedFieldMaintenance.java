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
package com.l2jserver.datapack.quests.Q00308_ReedFieldMaintenance;

import com.l2jserver.datapack.quests.Q00238_SuccessFailureOfBusiness.Q00238_SuccessFailureOfBusiness;
import com.l2jserver.datapack.quests.Q00309_ForAGoodCause.Q00309_ForAGoodCause;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.serverpackets.RadarControl;
import com.l2jserver.gameserver.util.Util;

/**
 * Reed Field Maintenance (308)<br>
 * Original Jython script by Bloodshed.
 * @author Joxit
 */
public class Q00308_ReedFieldMaintenance extends Quest {
	// NPC
	private static final int KATENSA = 32646;
	// Items
	private static final int MUCROKIAN_HIDE = 14871;
	private static final int AWAKENED_MUCROKIAN_HIDE = 14872;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22650, MUCROKIAN_HIDE, 21.8) // Mucrokian Fanatic
			.addSingleDrop(22651, MUCROKIAN_HIDE, 25.8) // Mucrokian Ascetic
			.addSingleDrop(22652, MUCROKIAN_HIDE, 24.8) // Mucrokian Savior
			.addSingleDrop(22653, MUCROKIAN_HIDE, 29.0) // Mucrokian Preacher
			.addSingleDrop(22654, MUCROKIAN_HIDE, 22.0) // Contaminated Mucrokian
			.addSingleDrop(22655, AWAKENED_MUCROKIAN_HIDE, 12.4) // Awakened Mucrokian
			.build();
	// Rewards
	private static final int REC_DYNASTY_EARRINGS_70 = 9985;
	private static final int REC_DYNASTY_NECKLACE_70 = 9986;
	private static final int REC_DYNASTY_RING_70 = 9987;
	private static final int REC_DYNASTY_SIGIL_60 = 10115;
	
	private static final int[] MOIRAI_RECIPES = {
		15777, 15780, 15783, 15786, 15789, 15790, 15814, 15813, 15812
	};
	
	private static final int[] MOIRAI_PIECES = {
		15647, 15650, 15653, 15656, 15659, 15692, 15772, 15773, 15774
	};
	
	// Misc
	private static final int MIN_LEVEL = 82;
	
	public Q00308_ReedFieldMaintenance() {
		super(308, Q00308_ReedFieldMaintenance.class.getSimpleName(), "Reed Field Maintenance");
		addStartNpc(KATENSA);
		addTalkId(KATENSA);
		addKillId(DROPLIST.getNpcIds());
	}
	
	private boolean canGiveItem(QuestState st, int quanty) {
		long mucrokian = st.getQuestItemsCount(MUCROKIAN_HIDE);
		long awakened = st.getQuestItemsCount(AWAKENED_MUCROKIAN_HIDE);
		if (awakened > 0) {
			if (awakened >= (quanty / 2)) {
				st.takeItems(AWAKENED_MUCROKIAN_HIDE, (quanty / 2));
				return true;
			} else if (mucrokian >= (quanty - (awakened * 2))) {
				st.takeItems(AWAKENED_MUCROKIAN_HIDE, awakened);
				st.takeItems(MUCROKIAN_HIDE, (quanty - (awakened * 2)));
				return true;
			}
		} else if (mucrokian >= quanty) {
			st.takeItems(MUCROKIAN_HIDE, quanty);
			return true;
		}
		return false;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "32646-02.htm":
			case "32646-03.htm":
			case "32646-06.html":
			case "32646-07.html":
			case "32646-08.html":
			case "32646-10.html":
				htmltext = event;
				break;
			case "32646-04.html":
				st.startQuest();
				player.sendPacket(new RadarControl(0, 2, 77325, 205773, -3432));
				htmltext = event;
				break;
			case "claimreward":
				htmltext = (player.hasQuestCompleted(Q00238_SuccessFailureOfBusiness.class.getName())) ? "32646-09.html" : "32646-12.html";
				break;
			case "100":
			case "120":
				htmltext = onItemExchangeRequest(st, MOIRAI_PIECES[getRandom(MOIRAI_PIECES.length - 1)], Integer.parseInt(event));
				break;
			case "192":
			case "230":
				htmltext = onItemExchangeRequest(st, REC_DYNASTY_EARRINGS_70, Integer.parseInt(event));
				break;
			case "256":
			case "308":
				htmltext = onItemExchangeRequest(st, REC_DYNASTY_NECKLACE_70, Integer.parseInt(event));
				break;
			case "128":
			case "154":
				htmltext = onItemExchangeRequest(st, REC_DYNASTY_RING_70, Integer.parseInt(event));
				break;
			case "206":
			case "246":
				htmltext = onItemExchangeRequest(st, REC_DYNASTY_SIGIL_60, Integer.parseInt(event));
				break;
			case "180":
			case "216":
				htmltext = onItemExchangeRequest(st, MOIRAI_RECIPES[getRandom(MOIRAI_RECIPES.length - 1)], Integer.parseInt(event));
				break;
			case "32646-11.html":
				st.exitQuest(true, true);
				htmltext = event;
				break;
		}
		return htmltext;
	}
	
	private String onItemExchangeRequest(QuestState st, int item, int quanty) {
		String htmltext;
		if (canGiveItem(st, quanty)) {
			if (Util.contains(MOIRAI_PIECES, item)) {
				st.giveItems(item, getRandom(1, 4));
			} else {
				st.giveItems(item, 1);
			}
			st.playSound(Sound.ITEMSOUND_QUEST_FINISH);
			htmltext = "32646-14.html";
		} else {
			htmltext = "32646-13.html";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(killer, 1, 1, npc);
		if (st != null) {
			giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker) {
		String htmltext = getNoQuestMsg(talker);
		final QuestState st = getQuestState(talker, true);
		final QuestState q309 = talker.getQuestState(Q00309_ForAGoodCause.class.getSimpleName());
		if ((q309 != null) && q309.isStarted()) {
			htmltext = "32646-15.html";
		} else if (st.isStarted()) {
			htmltext = (st.hasQuestItems(MUCROKIAN_HIDE) || st.hasQuestItems(AWAKENED_MUCROKIAN_HIDE)) ? "32646-06.html" : "32646-05.html";
		} else {
			htmltext = (talker.getLevel() >= MIN_LEVEL) ? "32646-01.htm" : "32646-00.html";
		}
		return htmltext;
	}
}
