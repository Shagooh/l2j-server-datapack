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
package com.l2jserver.datapack.quests.Q00125_TheNameOfEvil1;

import com.l2jserver.datapack.quests.Q00124_MeetingTheElroki.Q00124_MeetingTheElroki;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;

/**
 * The Name of Evil - 1 (125)
 * @author Adry_85
 */
public class Q00125_TheNameOfEvil1 extends Quest {
	// NPCs
	private static final int MUSHIKA = 32114;
	private static final int KARAKAWEI = 32117;
	private static final int ULU_KAIMU = 32119;
	private static final int BALU_KAIMU = 32120;
	private static final int CHUTA_KAIMU = 32121;
	// Items
	private static final int EPITAPH_OF_WISDOM = 8781;
	private static final int GAZKH_FRAGMENT = 8782;
	private static final QuestItemChanceHolder ORNITHOMIMUS_CLAW = new QuestItemChanceHolder(8779, 2L);
	private static final QuestItemChanceHolder DEINONYCHUS_BONE = new QuestItemChanceHolder(8780, 2L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(22200, ORNITHOMIMUS_CLAW, 66.1)
			.addSingleDrop(22201, ORNITHOMIMUS_CLAW, 33.0)
			.addSingleDrop(22202, ORNITHOMIMUS_CLAW, 66.1)
			.addSingleDrop(22219, ORNITHOMIMUS_CLAW, 32.7)
			.addSingleDrop(22224, ORNITHOMIMUS_CLAW, 32.7)
			.addSingleDrop(22203, DEINONYCHUS_BONE, 65.1)
			.addSingleDrop(22204, DEINONYCHUS_BONE, 32.6)
			.addSingleDrop(22205, DEINONYCHUS_BONE, 65.1)
			.addSingleDrop(22220, DEINONYCHUS_BONE, 31.9)
			.addSingleDrop(22225, DEINONYCHUS_BONE, 31.9)
			.build();
	// Skills
	private static final int REPRESENTATION_ENTER_THE_SAILREN_NEST_QUEST_ID = 5089;
	
	public Q00125_TheNameOfEvil1() {
		super(125, Q00125_TheNameOfEvil1.class.getSimpleName(), "The Name of Evil - 1");
		addStartNpc(MUSHIKA);
		addTalkId(MUSHIKA, KARAKAWEI, ULU_KAIMU, BALU_KAIMU, CHUTA_KAIMU);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(ORNITHOMIMUS_CLAW.getId(), DEINONYCHUS_BONE.getId(), EPITAPH_OF_WISDOM, GAZKH_FRAGMENT);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		QuestState st = getQuestState(player, false);
		if (st == null) {
			return getNoQuestMsg(player);
		}
		
		String htmltext = event;
		switch (event) {
			case "32114-05.html":
				st.startQuest();
				break;
			case "32114-08.html":
				if (st.isCond(1)) {
					st.giveItems(GAZKH_FRAGMENT, 1);
					st.setCond(2, true);
				}
				break;
			case "32117-09.html":
				if (st.isCond(2)) {
					st.setCond(3, true);
				}
				break;
			case "32117-15.html":
				if (st.isCond(4)) {
					st.setCond(5, true);
				}
				break;
			case "T_One":
				st.set("T", "1");
				htmltext = "32119-04.html";
				break;
			case "E_One":
				st.set("E", "1");
				htmltext = "32119-05.html";
				break;
			case "P_One":
				st.set("P", "1");
				htmltext = "32119-06.html";
				break;
			case "U_One":
				st.set("U", "1");
				if (st.isCond(5) && (st.getInt("T") > 0) && (st.getInt("E") > 0) && (st.getInt("P") > 0) && (st.getInt("U") > 0)) {
					htmltext = "32119-08.html";
					st.set("Memo", "1");
				} else {
					htmltext = "32119-07.html";
				}
				st.unset("T");
				st.unset("E");
				st.unset("P");
				st.unset("U");
				break;
			case "32119-07.html":
				st.unset("T");
				st.unset("E");
				st.unset("P");
				st.unset("U");
				break;
			case "32119-18.html":
				if (st.isCond(5)) {
					st.setCond(6, true);
					st.unset("Memo");
				}
				break;
			case "T_Two":
				st.set("T", "1");
				htmltext = "32120-04.html";
				break;
			case "O_Two":
				st.set("O", "1");
				htmltext = "32120-05.html";
				break;
			case "O2_Two":
				st.set("O2", "1");
				htmltext = "32120-06.html";
				break;
			case "N_Two":
				st.set("N", "1");
				if (st.isCond(6) && (st.getInt("T") > 0) && (st.getInt("O") > 0) && (st.getInt("O2") > 0) && (st.getInt("N") > 0)) {
					htmltext = "32120-08.html";
					st.set("Memo", "1");
				} else {
					htmltext = "32120-07.html";
				}
				st.unset("T");
				st.unset("O");
				st.unset("O2");
				st.unset("N");
				break;
			case "32120-07.html":
				st.unset("T");
				st.unset("O");
				st.unset("O2");
				st.unset("N");
			case "32120-17.html":
				if (st.isCond(6)) {
					st.setCond(7, true);
					st.unset("Memo");
				}
				break;
			case "W_Three":
				st.set("W", "1");
				htmltext = "32121-04.html";
				break;
			case "A_Three":
				st.set("A", "1");
				htmltext = "32121-05.html";
				break;
			case "G_Three":
				st.set("G", "1");
				htmltext = "32121-06.html";
				break;
			case "U_Three":
				st.set("U", "1");
				if (st.isCond(7) && (st.getInt("W") > 0) && (st.getInt("A") > 0) && (st.getInt("G") > 0) && (st.getInt("U") > 0)) {
					htmltext = "32121-08.html";
					st.set("Memo", "1");
				} else {
					htmltext = "32121-07.html";
				}
				st.unset("W");
				st.unset("A");
				st.unset("G");
				st.unset("U");
				break;
			case "32121-07.html":
				st.unset("W");
				st.unset("A");
				st.unset("G");
				st.unset("U");
				break;
			case "32121-11.html":
				st.set("Memo", "2");
				break;
			case "32121-16.html":
				st.set("Memo", "3");
				break;
			case "32121-18.html":
				if (st.isCond(7) && st.hasQuestItems(GAZKH_FRAGMENT)) {
					st.giveItems(EPITAPH_OF_WISDOM, 1);
					st.takeItems(GAZKH_FRAGMENT, -1);
					st.setCond(8, true);
					st.unset("Memo");
				}
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		QuestState st = getRandomPartyMemberState(player, 3, 1, npc);
		if (st != null && giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), false)) {
			if (hasItemsAtLimit(st.getPlayer(), ORNITHOMIMUS_CLAW, DEINONYCHUS_BONE)) {
				st.setCond(4, true);
			} else {
				playSound(st.getPlayer(), Sound.ITEMSOUND_QUEST_ITEMGET);
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		QuestState st = getQuestState(player, true);
		switch (npc.getId()) {
			case MUSHIKA:
				switch (st.getState()) {
					case State.CREATED:
						if (player.getLevel() < 76) {
							htmltext = "32114-01a.htm";
						} else {
							htmltext = (player.hasQuestCompleted(Q00124_MeetingTheElroki.class.getSimpleName())) ? "32114-01.htm" : "32114-01b.htm";
						}
						break;
					case State.STARTED:
						switch (st.getCond()) {
							case 1:
								htmltext = "32114-09.html";
								break;
							case 2:
								htmltext = "32114-10.html";
								break;
							case 3:
							case 4:
							case 5:
							case 6:
							case 7:
								htmltext = "32114-11.html";
								break;
							case 8:
								if (st.hasQuestItems(EPITAPH_OF_WISDOM)) {
									htmltext = "32114-12.html";
									st.addExpAndSp(859195, 86603);
									st.exitQuest(false, true);
								}
								break;
						}
						break;
					case State.COMPLETED:
						htmltext = getAlreadyCompletedMsg(player);
						break;
				}
				break;
			case KARAKAWEI:
				if (st.isStarted()) {
					switch (st.getCond()) {
						case 1:
							htmltext = "32117-01.html";
							break;
						case 2:
							htmltext = "32117-02.html";
							break;
						case 3:
							htmltext = "32117-10.html";
							break;
						case 4:
							if (hasItemsAtLimit(st.getPlayer(), ORNITHOMIMUS_CLAW, DEINONYCHUS_BONE)) {
								st.takeItems(ORNITHOMIMUS_CLAW.getId(), -1);
								st.takeItems(DEINONYCHUS_BONE.getId(), -1);
								htmltext = "32117-11.html";
							}
							break;
						case 5:
							htmltext = "32117-16.html";
							break;
						case 6:
						case 7:
							htmltext = "32117-17.html";
							break;
						case 8:
							htmltext = "32117-18.html";
							break;
					}
				}
				break;
			case ULU_KAIMU:
				if (st.isStarted()) {
					switch (st.getCond()) {
						case 1:
						case 2:
						case 3:
						case 4: {
							htmltext = "32119-01.html";
							break;
						}
						case 5:
							if (st.get("Memo") == null) {
								npc.broadcastPacket(new MagicSkillUse(npc, player, REPRESENTATION_ENTER_THE_SAILREN_NEST_QUEST_ID, 1, 1000, 0));
								st.unset("T");
								st.unset("E");
								st.unset("P");
								st.unset("U");
								htmltext = "32119-02.html";
							} else {
								htmltext = "32119-09.html";
							}
							break;
						case 6: {
							htmltext = "32119-18.html";
							break;
						}
						default: {
							htmltext = "32119-19.html";
							break;
						}
					}
				}
				break;
			case BALU_KAIMU:
				if (st.isStarted()) {
					switch (st.getCond()) {
						case 1:
						case 2:
						case 3:
						case 4:
						case 5: {
							htmltext = "32120-01.html";
							break;
						}
						case 6: {
							if (st.get("Memo") == null) {
								npc.broadcastPacket(new MagicSkillUse(npc, player, REPRESENTATION_ENTER_THE_SAILREN_NEST_QUEST_ID, 1, 1000, 0));
								st.unset("T");
								st.unset("O");
								st.unset("O2");
								st.unset("N");
								htmltext = "32120-02.html";
							} else {
								htmltext = "32120-09.html";
							}
							break;
						}
						case 7: {
							htmltext = "32120-17.html";
							break;
						}
						default: {
							htmltext = "32119-18.html";
							break;
						}
					}
				}
				break;
			case CHUTA_KAIMU:
				if (st.isStarted()) {
					switch (st.getCond()) {
						case 1:
						case 2:
						case 3:
						case 4:
						case 5:
						case 6: {
							htmltext = "32121-01.html";
							break;
						}
						case 7: {
							if (st.get("Memo") == null) {
								npc.broadcastPacket(new MagicSkillUse(npc, player, REPRESENTATION_ENTER_THE_SAILREN_NEST_QUEST_ID, 1, 1000, 0));
								st.unset("W");
								st.unset("A");
								st.unset("G");
								st.unset("U");
								htmltext = "32121-02.html";
							} else {
								switch (st.getInt("Memo")) {
									case 1:
										htmltext = "32121-09.html";
										break;
									case 2:
										htmltext = "32121-19.html";
										break;
									case 3:
										htmltext = "32121-20.html";
										break;
								}
							}
							break;
						}
						case 8: {
							htmltext = "32121-21.html";
							break;
						}
					}
				}
				break;
		}
		return htmltext;
	}
}
