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
package com.l2jserver.datapack.quests.Q00111_ElrokianHuntersProof;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;

/**
 * Elrokian Hunter's Proof (111)
 * @author Adry_85
 */
public final class Q00111_ElrokianHuntersProof extends Quest {
	// Misc
	private static final int MIN_LEVEL = 75;
	private static final int DINO_DROPS_LIMIT = 10;
	// NPCs
	private static final int MARQUEZ = 32113;
	private static final int MUSHIKA = 32114;
	private static final int ASAMAH = 32115;
	private static final int KIRIKACHIN = 32116;
	// Items
	private static final int ELROKIAN_TRAP = 8763;
	private static final int TRAP_STONE = 8764;
	private static final int EXPEDITION_MEMBERS_LETTER = 8769;
	private static final int PRACTICE_ELROKIAN_TRAP = 8773;
	private static final QuestItemChanceHolder DIARY_FRAGMENT = new QuestItemChanceHolder(8768, 50L);
	private static final QuestItemChanceHolder ORNITHOMINUS_CLAW = new QuestItemChanceHolder(8770, DINO_DROPS_LIMIT);
	private static final QuestItemChanceHolder DEINONYCHUS_BONE = new QuestItemChanceHolder(8771, DINO_DROPS_LIMIT);
	private static final QuestItemChanceHolder PACHYCEPHALOSAURUS_SKIN = new QuestItemChanceHolder(8772, DINO_DROPS_LIMIT);
	// Droplists
	private static final QuestDroplist DROPLIST_DIARY = QuestDroplist.builder()
			.addSingleDrop(22196, DIARY_FRAGMENT, 51.0) // velociraptor_leader
			.addSingleDrop(22197, DIARY_FRAGMENT, 51.0) // velociraptor
			.addSingleDrop(22198, DIARY_FRAGMENT, 51.0) // velociraptor_s
			.addSingleDrop(22218, DIARY_FRAGMENT, 25.0) // velociraptor_n
			.addSingleDrop(22223, DIARY_FRAGMENT, 26.0) // velociraptor_leader2
			.build();
	private static final QuestDroplist DROPLIST_DINO = QuestDroplist.builder()
			.addSingleDrop(22200, ORNITHOMINUS_CLAW, 66.0) // ornithomimus_leader
			.addSingleDrop(22201, ORNITHOMINUS_CLAW, 33.0) // ornithomimus
			.addSingleDrop(22202, ORNITHOMINUS_CLAW, 66.0) // ornithomimus_s
			.addSingleDrop(22219, ORNITHOMINUS_CLAW, 33.0) // ornithomimus_n
			.addSingleDrop(22224, ORNITHOMINUS_CLAW, 33.0) // ornithomimus_leader2
			.addSingleDrop(22203, DEINONYCHUS_BONE, 65.0) // deinonychus_leader
			.addSingleDrop(22204, DEINONYCHUS_BONE, 32.0) // deinonychus
			.addSingleDrop(22205, DEINONYCHUS_BONE, 66.0) // deinonychus_s
			.addSingleDrop(22220, DEINONYCHUS_BONE, 32.0) // deinonychus_n
			.addSingleDrop(22225, DEINONYCHUS_BONE, 32.0) // deinonychus_leader2
			.addSingleDrop(22208, PACHYCEPHALOSAURUS_SKIN, 50.0) // pachycephalosaurus_ldr
			.addSingleDrop(22209, PACHYCEPHALOSAURUS_SKIN, 50.0) // pachycephalosaurus
			.addSingleDrop(22210, PACHYCEPHALOSAURUS_SKIN, 50.0) // pachycephalosaurus_s
			.addSingleDrop(22221, PACHYCEPHALOSAURUS_SKIN, 49.0) // pachycephalosaurus_n
			.addSingleDrop(22226, PACHYCEPHALOSAURUS_SKIN, 50.0) // pachycephalosaurus_ldr2
			.build();
	
	public Q00111_ElrokianHuntersProof() {
		super(111, Q00111_ElrokianHuntersProof.class.getSimpleName(), "Elrokian Hunter's Proof");
		addStartNpc(MARQUEZ);
		addTalkId(MARQUEZ, MUSHIKA, ASAMAH, KIRIKACHIN);
		addKillId(DROPLIST_DIARY.getNpcIds());
		addKillId(DROPLIST_DINO.getNpcIds());
		registerQuestItems(DIARY_FRAGMENT.getId(), EXPEDITION_MEMBERS_LETTER, ORNITHOMINUS_CLAW.getId(), DEINONYCHUS_BONE.getId(), PACHYCEPHALOSAURUS_SKIN.getId(), PRACTICE_ELROKIAN_TRAP);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		String htmltext = null;
		if (qs == null) {
			return htmltext;
		}
		
		switch (event) {
			case "32113-02.htm":
			case "32113-05.htm":
			case "32113-04.html":
			case "32113-10.html":
			case "32113-11.html":
			case "32113-12.html":
			case "32113-13.html":
			case "32113-14.html":
			case "32113-18.html":
			case "32113-19.html":
			case "32113-20.html":
			case "32113-21.html":
			case "32113-22.html":
			case "32113-23.html":
			case "32113-24.html":
			case "32115-08.html":
			case "32116-03.html": {
				htmltext = event;
				break;
			}
			case "32113-03.html": {
				qs.startQuest();
				qs.setMemoState(1);
				htmltext = event;
				break;
			}
			case "32113-15.html": {
				if (qs.isMemoState(3)) {
					qs.setMemoState(4);
					qs.setCond(4, true);
					htmltext = event;
				}
				break;
			}
			case "32113-25.html": {
				if (qs.isMemoState(5)) {
					qs.setMemoState(6);
					qs.setCond(6, true);
					giveItems(player, EXPEDITION_MEMBERS_LETTER, 1);
					htmltext = event;
				}
				break;
			}
			case "32115-03.html": {
				if (qs.isMemoState(2)) {
					qs.setMemoState(3);
					qs.setCond(3, true);
					htmltext = event;
				}
				break;
			}
			case "32115-06.html": {
				if (qs.isMemoState(9)) {
					qs.setMemoState(10);
					qs.setCond(9);
					playSound(player, Sound.ETCSOUND_ELROKI_SONG_FULL);
					htmltext = event;
				}
				break;
			}
			case "32115-09.html": {
				if (qs.isMemoState(10)) {
					qs.setMemoState(11);
					qs.setCond(10, true);
					htmltext = event;
				}
				break;
			}
			case "32116-04.html": {
				if (qs.isMemoState(7)) {
					qs.setMemoState(8);
					playSound(player, Sound.ETCSOUND_ELROKI_SONG_FULL);
					htmltext = event;
				}
				break;
			}
			case "32116-07.html": {
				if (qs.isMemoState(8)) {
					qs.setMemoState(9);
					qs.setCond(8, true);
					htmltext = event;
				}
				break;
			}
			case "32116-10.html": {
				if (qs.isMemoState(12) && hasQuestItems(player, PRACTICE_ELROKIAN_TRAP)) {
					takeItems(player, PRACTICE_ELROKIAN_TRAP, -1);
					giveItems(player, ELROKIAN_TRAP, 1);
					giveItems(player, TRAP_STONE, 100);
					giveAdena(player, 1071691, true);
					addExpAndSp(player, 553524, 55538);
					qs.exitQuest(false, true);
					htmltext = event;
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
			if (qs.isMemoState(4) && qs.isCond(4)
					&& giveItemRandomly(qs.getPlayer(), npc, DROPLIST_DIARY.get(npc), true)) {
				qs.setCond(5);
			} else if (qs.isMemoState(11) && qs.isCond(10)) {
				if (giveItemRandomly(qs.getPlayer(), npc, DROPLIST_DINO.get(npc), true)
						&& hasItemsAtLimit(qs.getPlayer(), ORNITHOMINUS_CLAW, DEINONYCHUS_BONE, PACHYCEPHALOSAURUS_SKIN)) {
					qs.setCond(11);
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (qs.getState()) {
			case State.COMPLETED: {
				if (npc.getId() == MARQUEZ) {
					htmltext = getAlreadyCompletedMsg(player);
				}
				break;
			}
			case State.CREATED: {
				if (npc.getId() == MARQUEZ) {
					htmltext = ((player.getLevel() >= MIN_LEVEL) ? "32113-01.htm" : "32113-06.html");
				}
				break;
			}
			case State.STARTED: {
				switch (npc.getId()) {
					case MARQUEZ: {
						switch (qs.getMemoState()) {
							case 1: {
								htmltext = "32113-07.html";
								break;
							}
							case 2: {
								htmltext = "32113-08.html";
								break;
							}
							case 3: {
								htmltext = "32113-09.html";
								break;
							}
							case 4: {
								if (!hasItemsAtLimit(player, DIARY_FRAGMENT)) {
									htmltext = "32113-16.html";
								} else {
									takeItems(player, DIARY_FRAGMENT.getId(), -1);
									qs.setMemoState(5);
									htmltext = "32113-17.html";
								}
								break;
							}
							case 5: {
								htmltext = "32113-26.html";
								break;
							}
							case 6: {
								htmltext = "32113-27.html";
								break;
							}
							case 7:
							case 8: {
								htmltext = "32113-28.html";
								break;
							}
							case 9: {
								htmltext = "32113-29.html";
								break;
							}
							case 10:
							case 11:
							case 12: {
								htmltext = "32113-30.html";
								break;
							}
						}
						break;
					}
					case MUSHIKA: {
						if (qs.isMemoState(1)) {
							qs.setCond(2, true);
							qs.setMemoState(2);
							htmltext = "32114-01.html";
						} else if ((qs.getMemoState() > 1) && (qs.getMemoState() < 10)) {
							htmltext = "32114-02.html";
						} else {
							htmltext = "32114-03.html";
						}
						break;
					}
					case ASAMAH: {
						switch (qs.getMemoState()) {
							case 1: {
								htmltext = "32115-01.html";
								break;
							}
							case 2: {
								htmltext = "32115-02.html";
								break;
							}
							case 3:
							case 4:
							case 5:
							case 6:
							case 7:
							case 8: {
								htmltext = "32115-04.html";
								break;
							}
							case 9: {
								htmltext = "32115-05.html";
								break;
							}
							case 10: {
								htmltext = "32115-07.html";
								break;
							}
							case 11: {
								if (!hasItemsAtLimit(player, ORNITHOMINUS_CLAW, DEINONYCHUS_BONE, PACHYCEPHALOSAURUS_SKIN)) {
									htmltext = "32115-10.html";
								} else {
									qs.setMemoState(12);
									qs.setCond(12, true);
									giveItems(player, PRACTICE_ELROKIAN_TRAP, 1);
									takeItems(player, ORNITHOMINUS_CLAW.getId(), -1);
									takeItems(player, DEINONYCHUS_BONE.getId(), -1);
									takeItems(player, PACHYCEPHALOSAURUS_SKIN.getId(), -1);
									htmltext = "32115-11.html";
								}
								break;
							}
							case 12: {
								htmltext = "32115-12.html";
								break;
							}
						}
						break;
					}
					case KIRIKACHIN: {
						switch (qs.getMemoState()) {
							case 1:
							case 2:
							case 3:
							case 4:
							case 5: {
								htmltext = "32116-01.html";
								break;
							}
							case 6: {
								if (hasQuestItems(player, EXPEDITION_MEMBERS_LETTER)) {
									qs.setMemoState(7);
									qs.setCond(7, true);
									takeItems(player, EXPEDITION_MEMBERS_LETTER, -1);
									htmltext = "32116-02.html";
								}
								break;
							}
							case 7: {
								htmltext = "32116-05.html";
								break;
							}
							case 8: {
								htmltext = "32116-06.html";
								break;
							}
							case 9:
							case 10:
							case 11: {
								htmltext = "32116-08.html";
								break;
							}
							case 12: {
								htmltext = "32116-09.html";
								break;
							}
						}
						break;
					}
				}
				break;
			}
		}
		return htmltext;
	}
}
