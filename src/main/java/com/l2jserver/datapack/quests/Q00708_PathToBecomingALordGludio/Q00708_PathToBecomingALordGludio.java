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
package com.l2jserver.datapack.quests.Q00708_PathToBecomingALordGludio;

import static com.l2jserver.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;
import static com.l2jserver.gameserver.enums.audio.Sound.ITEMSOUND_QUEST_FINISH;
import static com.l2jserver.gameserver.enums.audio.Sound.ITEMSOUND_QUEST_MIDDLE;
import static com.l2jserver.gameserver.network.NpcStringId.DOES_MY_MISSION_TO_BLOCK_THE_SUPPLIES_END_HERE;
import static com.l2jserver.gameserver.network.NpcStringId.GO_FIND_SAIUS;
import static com.l2jserver.gameserver.network.NpcStringId.HAVE_YOU_COMPLETED_YOUR_PREPARATIONS_TO_BECOME_A_LORD;
import static com.l2jserver.gameserver.network.NpcStringId.LISTEN_YOU_VILLAGERS_OUR_LIEGE_WHO_WILL_SOON_BECOME_A_LORD_HAS_DEFEATED_THE_HEADLESS_KNIGHT_YOU_CAN_NOW_REST_EASY;
import static com.l2jserver.gameserver.network.NpcStringId.S1_DO_YOU_DARE_DEFY_MY_SUBORDINATES;
import static com.l2jserver.gameserver.network.NpcStringId.S1_HAS_BECOME_LORD_OF_THE_TOWN_OF_GLUDIO_LONG_MAY_HE_REIGN;
import static com.l2jserver.gameserver.network.NpcStringId.S1_NOW_DEPART;
import static com.l2jserver.gameserver.network.clientpackets.Say2.NPC_ALL;
import static com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage.NORMAL_SIZE;
import static com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage.TOP_CENTER;

import com.l2jserver.gameserver.instancemanager.FortManager;
import com.l2jserver.gameserver.instancemanager.TerritoryWarManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.serverpackets.NpcSay;
import com.l2jserver.gameserver.util.Util;

/**
 * Path To Becoming A Lord - Gludio (708)
 * @author IvanTotov
 * @author Zoey76
 */
public final class Q00708_PathToBecomingALordGludio extends Quest {
	
	// NPCs
	private static final int PINTER = 30298;
	private static final int BATHIS = 30332;
	private static final int SAYRES = 35100;
	// Items
	private static final int VARNISH = 1865;
	private static final int ANIMAL_SKIN = 1867;
	private static final int IRON_ORE = 1869;
	private static final int COKES = 1879;
	private static final int HEADLESS_ARMOR = 13848;
	// Monsters
	private static final int HEADLESS_KNIGHT = 27393;
	private static final int[] CASTLE_RUINS = {
		20035, // Skeleton Tracker
		20042, // Skeleton Tracker Leader
		20045, // Skeleton Scout
		20051, // Skeleton Bowman
		20054, // Ruin Spartoi
		20060, // Raging Spartoi
		20514, // Shield Skeleton
		20515, // Skeleton Infantryman
		HEADLESS_KNIGHT
	};
	// Dominion
	private static final int GLUDIO_ID = 81;
	private static final int SHANTY = 101;
	private static final int SOUTHERN = 102;
	// Misc
	private static final int MIN_TIME = 60000;
	
	public Q00708_PathToBecomingALordGludio() {
		super(708, Q00708_PathToBecomingALordGludio.class.getSimpleName(), "Path To Becoming A Lord - Gludio");
		addStartNpc(SAYRES);
		addTalkId(SAYRES, PINTER, BATHIS);
		registerQuestItems(HEADLESS_ARMOR);
		addKillId(CASTLE_RUINS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if (qs == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "ACCEPT": {
				if (qs.isCreated() && npc.isMyLord(player) && (npc.getCastle().getResidenceId() == GLUDIO_ID)) {
					qs.startQuest();
					playSound(player, ITEMSOUND_QUEST_MIDDLE);
					qs.setMemoState(1);
					qs.setMemoStateEx(1, System.currentTimeMillis());
					qs.setFlagJournal(1);
					startQuestTimer("70801", 60000, npc, player);
					htmltext = "35100-04.htm";
				}
				break;
			}
			case "70801": {
				npc.broadcastPacket(new NpcSay(npc, NPC_ALL, HAVE_YOU_COMPLETED_YOUR_PREPARATIONS_TO_BECOME_A_LORD));
				break;
			}
			case "35100-02.htm": {
				if (qs.hasMemoState() && npc.isMyLord(player) && !npc.isDominionOfLord(GLUDIO_ID)) {
					htmltext = event;
				}
				break;
			}
			case "35100-08.html": {
				if (qs.hasMemoState() && (qs.getMemoState() == 2) && !npc.isDominionOfLord(GLUDIO_ID)) {
					qs.setMemoState(3);
					qs.setFlagJournal(2);
					qs.playSound(ITEMSOUND_QUEST_MIDDLE);
					htmltext = event;
				}
				break;
			}
			case "35100-12.html": {
				final var qsLeader = getClanLeaderQuestState(player);
				if ((qsLeader != null) && (npc.calculateDistance(qsLeader.getPlayer(), false, false) <= 1500) && qsLeader.hasMemoState() && //
					qsLeader.isMemoState(3) && (npc.getCastle().getOwnerId() == player.getClanId()) && (player.getClanId() != 0)) {
					npc.broadcastPacket(new NpcSay(npc, NPC_ALL, S1_NOW_DEPART).addStringParameter(player.getName()));
					qsLeader.setMemoState(4);
					qsLeader.setFlagJournal(3);
					qsLeader.showQuestionMark(getId());
					qsLeader.playSound(ITEMSOUND_QUEST_MIDDLE);
					qsLeader.setMemoStateEx(1, player.getObjectId());
					htmltext = getHtm(player.getHtmlPrefix(), event).replace("%name%", player.getName());
				} else {
					htmltext = "35100-13.html";
				}
				break;
			}
			case "35100-23.html": {
				if (qs.hasMemoState() && qs.isMemoState(49)) {
					if ((qs.getDominionWarState(GLUDIO_ID) != 5) && npc.isMyLord(player)) {
						npc.broadcastPacket(new NpcSay(npc, NPC_ALL, S1_HAS_BECOME_LORD_OF_THE_TOWN_OF_GLUDIO_LONG_MAY_HE_REIGN).addStringParameter(player.getName()));
						TerritoryWarManager.getInstance().declareLord(GLUDIO_ID, player);
						qs.removeMemo();
						qs.playSound(ITEMSOUND_QUEST_FINISH);
						htmltext = event;
					}
				}
				break;
			}
			case "30298-04.html": {
				final var qsLeader = getClanLeaderQuestState(player);
				if (qsLeader != null) {
					if (qsLeader.hasMemoState() && (qsLeader.getMemoState() == 4)) {
						htmltext = event;
					}
				}
				break;
			}
			case "30298-05.html": {
				final var qsLeader = getClanLeaderQuestState(player);
				if (qsLeader != null) {
					if (qsLeader.hasMemoState() && (qsLeader.getMemoState() == 4)) {
						showOnScreenMsgFStr(player, TOP_CENTER, 0, NORMAL_SIZE, 0, 1, false, 5000, false, GO_FIND_SAIUS);
						qs.setMemoState(5);
						qs.setMemoStateEx(1, 0);
						qs.setFlagJournal(4);
						qs.playSound(ITEMSOUND_QUEST_MIDDLE);
						htmltext = event;
					}
				} else {
					htmltext = "30298-06.html";
				}
				break;
			}
			case "30298-09.html": {
				final var qsLeader = getClanLeaderQuestState(player);
				if (qsLeader != null) {
					if (qsLeader.hasMemoState() && ((qsLeader.getMemoState() % 10) == 5)) {
						final int i0 = (qs.getMemoState() / 10);
						if ((getQuestItemsCount(player, ANIMAL_SKIN) >= 100) && (getQuestItemsCount(player, VARNISH) >= 100) && //
							(getQuestItemsCount(player, IRON_ORE) >= 100) && (getQuestItemsCount(player, COKES) >= 50)) {
							takeItems(player, ANIMAL_SKIN, -100);
							takeItems(player, VARNISH, -100);
							takeItems(player, IRON_ORE, -100);
							takeItems(player, COKES, -50);
							qs.setMemoState(9 + i0 * 10);
							htmltext = event;
						} else {
							htmltext = "30298-10.html";
						}
					}
				} else {
					htmltext = "30298-11.html";
				}
				break;
			}
			case "30332-02.html": {
				if ((qs.getMemoState() / 10) == 1) {
					final int i0 = qs.getMemoState();
					qs.setMemoState(i0 + 10);
					qs.setFlagJournal(6);
					qs.playSound(ITEMSOUND_QUEST_MIDDLE);
					htmltext = event;
				}
				break;
			}
			case "30332-05.html": {
				if ((qs.getMemoState() / 10) == 3) {
					if ((qs.getMemoState() % 10) == 9) {
						takeItems(player, HEADLESS_ARMOR, -1);
						final int i0 = qs.getMemoState();
						qs.setMemoState(i0 + 10);
						qs.setFlagJournal(9);
						qs.playSound(ITEMSOUND_QUEST_MIDDLE);
						htmltext = event;
					} else {
						takeItems(player, HEADLESS_ARMOR, -1);
						final int i0 = qs.getMemoState();
						qs.setMemoState(i0 + 10);
						qs.setFlagJournal(8);
						qs.playSound(ITEMSOUND_QUEST_MIDDLE);
						htmltext = "30332-05.html";
					}
					npc.broadcastPacket(new NpcSay(npc, NPC_ALL, LISTEN_YOU_VILLAGERS_OUR_LIEGE_WHO_WILL_SOON_BECOME_A_LORD_HAS_DEFEATED_THE_HEADLESS_KNIGHT_YOU_CAN_NOW_REST_EASY));
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final var target = killer.isInParty() ? killer.getParty().getRandomPlayer() : killer;
		final var qs = target.getQuestState(getName());
		if ((qs != null) && Util.checkIfInRange(1500, npc, target, true)) {
			final var clan = target.getClan();
			if (clan != null) {
				final var clanLeader = clan.getLeader();
				if (clanLeader != null) {
					final var leader = clanLeader.getPlayerInstance();
					if (leader != null) {
						final var questStateLeader = leader.getQuestState(getName());
						if ((questStateLeader != null) && ((questStateLeader.getMemoState() / 10) == 2)) {
							if (npc.getId() == HEADLESS_KNIGHT) {
								if (Util.checkIfInRange(1500, npc, leader, true)) {
									giveItems(leader, HEADLESS_ARMOR, 1);
									questStateLeader.setMemoState(questStateLeader.getMemoState() + 10);
									questStateLeader.setFlagJournal(7);
									questStateLeader.showQuestionMark(getId());
									playSound(leader, ITEMSOUND_QUEST_MIDDLE);
									npc.broadcastPacket(new NpcSay(npc, NPC_ALL, DOES_MY_MISSION_TO_BLOCK_THE_SUPPLIES_END_HERE));
								}
							} else {
								if (getRandom(100) < questStateLeader.getMemoStateEx(1)) {
									final L2Npc headlessKnight = addSpawn(HEADLESS_KNIGHT, npc.getLocation(), false, 0);
									headlessKnight.setScriptValue(target.getObjectId());
									headlessKnight.getAI().setIntention(AI_INTENTION_ATTACK, target);
								} else {
									questStateLeader.setMemoStateEx(1, questStateLeader.getMemoStateEx(1) + 1);
								}
							}
						}
					}
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		if (npc.getId() == HEADLESS_KNIGHT) {
			final var target = L2World.getInstance().getPlayer(npc.getScriptValue());
			if (target != null) {
				final var message = new NpcSay(npc, NPC_ALL, S1_DO_YOU_DARE_DEFY_MY_SUBORDINATES);
				message.addStringParameter(target.getName());
				npc.broadcastPacket(message);
			}
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (qs.getState()) {
			case State.CREATED: {
				switch (npc.getId()) {
					case SAYRES: {
						if (npc.isMyLord(player)) {
							if (!npc.isDominionOfLord(GLUDIO_ID)) {
								htmltext = "35100-01.htm";
							} else {
								htmltext = "35100-03.htm";
							}
						}
						break;
					}
				}
				break;
			}
			case State.STARTED: {
				switch (npc.getId()) {
					case SAYRES: {
						if ((qs.hasMemoState() && (qs.getMemoState() == 1) && npc.isMyLord(player))) {
							if (System.currentTimeMillis() - qs.getMemoStateEx(1) < MIN_TIME) {
								htmltext = "35100-05.html";
							} else {
								qs.setMemoState(2);
								qs.setMemoStateEx(1, 0);
								htmltext = "35100-06.html";
							}
						} else if (qs.hasMemoState() && (qs.getMemoState() == 2)) {
							htmltext = "35100-07.html";
						} else if (!npc.isMyLord(player)) {
							if ((npc.getCastle().getOwnerId() == player.getClanId()) && (player.getClanId() != 0)) {
								final var leader = player.getClan().getLeader().getPlayerInstance();
								final var qsLeader = getQuestState(leader, false);
								if (qsLeader != null) {
									if (qsLeader.hasMemoState() && (qsLeader.getMemoState() == 3)) {
										if (npc.calculateDistance(leader, false, false) <= 1500) {
											htmltext = "35100-11.html";
										} else {
											htmltext = "35100-10.html";
										}
									} else if (qsLeader.hasMemoState() && (qsLeader.getMemoState() == 4)) {
										htmltext = "35100-13a.html";
									} else {
										htmltext = "35100-09.html";
									}
								} else {
									htmltext = "35100-09.html";
								}
							} else if (npc.getCastle().getOwnerId() != player.getClanId()) {
								htmltext = "35100-09.html";
							}
						} else if (qs.hasMemoState() && (qs.getMemoState() == 3)) {
							htmltext = "35100-14.html";
						} else if (qs.hasMemoState() && (qs.getMemoState() == 4)) {
							htmltext = "35100-15.html";
						} else if (qs.hasMemoState() && (qs.getMemoState() == 5)) {
							final int i0 = qs.getMemoState();
							qs.setMemoState(i0 + 10);
							qs.setFlagJournal(5);
							qs.playSound(ITEMSOUND_QUEST_MIDDLE);
							htmltext = "35100-16.html";
						} else if (qs.hasMemoState() && ((qs.getMemoState() / 10) <= 1) && ((qs.getMemoState() % 10) == 9)) {
							final int i0 = qs.getMemoState() / 10;
							final int i1 = qs.getMemoState();
							if (i0 == 0) {
								qs.setMemoState(i1 + 10);
								qs.setFlagJournal(5);
								qs.playSound(ITEMSOUND_QUEST_MIDDLE);
							}
							htmltext = "35100-17.html";
						} else if (qs.hasMemoState() && ((qs.getMemoState() / 10) == 1) && ((qs.getMemoState() % 10) != 9)) {
							htmltext = "35100-18.html";
						} else if (qs.hasMemoState() && ((qs.getMemoState() / 10) == 2) || ((qs.getMemoState() / 10) == 3) && ((qs.getMemoState() % 10) == 9)) {
							htmltext = "35100-19.html";
						} else if (qs.hasMemoState() && ((qs.getMemoState() / 10) == 2) || ((qs.getMemoState() / 10) == 3) && ((qs.getMemoState() % 10) == 9)) {
							htmltext = "35100-20.html";
						} else if (qs.hasMemoState() && ((qs.getMemoState() / 10) == 4) && ((qs.getMemoState() % 10) != 9)) {
							htmltext = "35100-21.html";
						} else if (qs.hasMemoState() && ((qs.getMemoState() / 10) == 4) && ((qs.getMemoState() % 10) == 9) && npc.isMyLord(player)) {
							if (TerritoryWarManager.getInstance().isTWInProgress() || npc.getCastle().getSiege().isInProgress()) {
								htmltext = "35100-22a.html";
							} else if ((FortManager.getInstance().getFortById(SHANTY).getFortState() == 0) || FortManager.getInstance().getFortById(SOUTHERN).getFortState() == 0) {
								htmltext = "35100-22b.html";
							} else {
								htmltext = "35100-22.html";
							}
						}
						break;
					}
					case PINTER: {
						if (!npc.isMyLord(player)) {
							final var leader = player.getClan().getLeader().getPlayerInstance();
							final var qsLeader = getQuestState(leader, false);
							if (qsLeader != null) {
								if (qsLeader.hasMemoState() && (qsLeader.getMemoState() <= 3)) {
									htmltext = "30298-02.html";
								} else if (qsLeader.getMemoState() == 4) {
									if (player.getObjectId() == qs.getMemoStateEx(1)) {
										htmltext = "30298-03.html";
									} else {
										htmltext = "30298-03a.html";
									}
								} else if ((qsLeader.getMemoState() % 10) == 5) {
									if ((getQuestItemsCount(player, ANIMAL_SKIN) >= 100) && (getQuestItemsCount(player, VARNISH) >= 100) && //
										(getQuestItemsCount(player, IRON_ORE) >= 100) && (getQuestItemsCount(player, COKES) >= 50)) {
										htmltext = "30298-08.html";
									} else {
										htmltext = "30298-07.html";
									}
								} else if ((qsLeader.getMemoState() % 10) == 9) {
									htmltext = "30298-12.html";
								}
							} else {
								htmltext = "30298-01.html";
							}
						} else if (npc.isMyLord(player)) {
							htmltext = "30298-13.html";
						}
						break;
					}
					case BATHIS: {
						if (qs.hasMemoState() && ((qs.getMemoState() / 10) == 1)) {
							htmltext = "30332-01.html";
						} else if (qs.hasMemoState() && ((qs.getMemoState() / 10) == 2)) {
							htmltext = "30332-03.html";
						} else if (qs.hasMemoState() && ((qs.getMemoState() / 10) == 3)) {
							htmltext = "30332-04.html";
						} else if (qs.hasMemoState() && ((qs.getMemoState() / 10) == 4)) {
							htmltext = "30332-06.html";
						}
						break;
					}
				}
				break;
			}
			case State.COMPLETED: {
				// TODO(Zoey76): Review this in the future.
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		return htmltext;
	}
	
	private QuestState getClanLeaderQuestState(L2PcInstance player) {
		final var clan = player.getClan();
		if (clan == null) {
			return null;
		}
		
		final var leader = player.getClan().getLeader().getPlayerInstance();
		if (leader == null) {
			return null;
		}
		return getQuestState(leader, false);
	}
}