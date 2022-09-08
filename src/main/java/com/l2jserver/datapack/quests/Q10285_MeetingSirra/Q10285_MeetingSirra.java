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
package com.l2jserver.datapack.quests.Q10285_MeetingSirra;

import com.l2jserver.datapack.quests.Q10284_AcquisitionOfDivineSword.Q10284_AcquisitionOfDivineSword;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.instancezone.InstanceWorld;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.NpcSay;

/**
 * Meeting Sirra (10285)
 * @author Adry_85
 * @since 2.6.0.0
 */
public final class Q10285_MeetingSirra extends Quest {
	// NPCs
	private static final int RAFFORTY = 32020;
	private static final int FREYAS_STEWARD = 32029;
	private static final int JINIA = 32760;
	private static final int KEGOR = 32761;
	private static final int SIRRA = 32762;
	private static final int JINIA2 = 32781;
	// Misc
	private static final int MIN_LEVEL = 82;
	// Locations
	private static final Location EXIT_LOC = new Location(113793, -109342, -845, 0);
	private static final Location FREYA_LOC = new Location(103045, -124361, -2768, 0);
	
	public Q10285_MeetingSirra() {
		super(10285, Q10285_MeetingSirra.class.getSimpleName(), "Meeting Sirra");
		addStartNpc(RAFFORTY);
		addTalkId(RAFFORTY, JINIA, KEGOR, SIRRA, JINIA2, FREYAS_STEWARD);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "32020-02.htm": {
				htmltext = event;
				break;
			}
			case "32020-03.htm": {
				st.startQuest();
				st.setMemoState(1);
				st.setMemoStateEx(1, 0);
				htmltext = event;
				break;
			}
			case "32760-02.html": {
				if (st.isMemoState(1) && st.isMemoStateEx(1, 0)) {
					st.setMemoStateEx(1, 1);
					st.setCond(3, true);
					htmltext = event;
				}
				break;
			}
			case "32760-05.html": {
				if (st.isMemoState(1) && st.isMemoStateEx(1, 2)) {
					htmltext = event;
				}
				break;
			}
			case "32760-06.html": {
				if (st.isMemoState(1) && st.isMemoStateEx(1, 2)) {
					final L2Npc sirra = addSpawn(SIRRA, -23905, -8790, -5384, 56238, false, 0, false, npc.getInstanceId());
					sirra.broadcastPacket(new NpcSay(sirra.getObjectId(), Say2.NPC_ALL, sirra.getId(), NpcStringId.THERES_NOTHING_YOU_CANT_SAY_I_CANT_LISTEN_TO_YOU_ANYMORE));
					st.setMemoStateEx(1, 3);
					st.setCond(5, true);
					htmltext = event;
				}
				break;
			}
			case "32760-09.html":
			case "32760-10.html":
			case "32760-11.html": {
				if (st.isMemoState(1) && st.isMemoStateEx(1, 4)) {
					htmltext = event;
				}
				break;
			}
			case "32760-12.html": {
				if (st.isMemoState(1) && st.isMemoStateEx(1, 4)) {
					st.setMemoStateEx(1, 5);
					st.setCond(7, true);
					htmltext = event;
				}
				break;
			}
			case "32760-13.html": {
				if (st.isMemoState(1) && st.isMemoStateEx(1, 5)) {
					st.setMemoStateEx(1, 0);
					st.setMemoState(2);
					final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
					world.removeAllowed(player.getObjectId());
					player.setInstanceId(0);
					htmltext = event;
				}
				break;
			}
			case "32760-14.html": {
				if (st.isMemoState(2)) {
					player.teleToLocation(EXIT_LOC, 0);
					htmltext = event;
				}
				break;
			}
			case "32761-02.html": {
				if (st.isMemoState(1) && st.isMemoStateEx(1, 1)) {
					st.setMemoStateEx(1, 2);
					st.setCond(4, true);
					htmltext = event;
				}
				break;
			}
			case "32762-02.html":
			case "32762-03.html":
			case "32762-04.html":
			case "32762-05.html":
			case "32762-06.html":
			case "32762-07.html": {
				if (st.isMemoState(1) && st.isMemoStateEx(1, 3)) {
					htmltext = event;
				}
				break;
			}
			case "32762-08.html": {
				if (st.isMemoState(1) && st.isMemoStateEx(1, 3)) {
					st.setMemoStateEx(1, 4);
					st.setCond(6, true);
					htmltext = event;
					npc.deleteMe();
				}
				break;
			}
			case "32781-02.html":
			case "32781-03.html": {
				if (st.isMemoState(2)) {
					htmltext = event;
				}
				break;
			}
			case "TELEPORT": {
				if (player.getLevel() >= MIN_LEVEL) {
					player.teleToLocation(FREYA_LOC, 0);
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCompleted()) {
			if (npc.getId() == RAFFORTY) {
				htmltext = "32020-05.htm";
			}
		} else if (st.isCreated()) {
			htmltext = ((player.getLevel() >= MIN_LEVEL) && player.hasQuestCompleted(Q10284_AcquisitionOfDivineSword.class.getSimpleName())) ? "32020-01.htm" : "32020-04.htm";
		} else if (st.isStarted()) {
			switch (npc.getId()) {
				case RAFFORTY: {
					switch (st.getMemoState()) {
						case 1: {
							htmltext = (player.getLevel() >= MIN_LEVEL) ? "32020-06.html" : "32020-09.html";
							break;
						}
						case 2: {
							htmltext = "32020-07.html";
							break;
						}
						case 3: {
							st.giveAdena(283425, true);
							st.addExpAndSp(939075, 83855);
							st.exitQuest(false, true);
							htmltext = "32020-08.html";
							break;
						}
					}
					break;
				}
				case JINIA: {
					if (st.isMemoState(1)) {
						switch (st.getMemoStateEx(1)) {
							case 0: {
								htmltext = "32760-01.html";
								break;
							}
							case 1: {
								htmltext = "32760-03.html";
								break;
							}
							case 2: {
								htmltext = "32760-04.html";
								break;
							}
							case 3: {
								htmltext = "32760-07.html";
								break;
							}
							case 4: {
								htmltext = "32760-08.html";
								break;
							}
							case 5: {
								htmltext = "32760-15.html";
								break;
							}
						}
					}
					break;
				}
				case KEGOR: {
					if (st.isMemoState(1)) {
						switch (st.getMemoStateEx(1)) {
							case 1: {
								htmltext = "32761-01.html";
								break;
							}
							case 2: {
								htmltext = "32761-03.html";
								break;
							}
							case 3: {
								htmltext = "32761-04.html";
								break;
							}
						}
					}
					break;
				}
				case SIRRA: {
					if (st.isMemoState(1)) {
						if (st.isMemoStateEx(1, 3)) {
							htmltext = "32762-01.html";
						} else if (st.isMemoStateEx(1, 4)) {
							htmltext = "32762-09.html";
						}
					}
					break;
				}
				case JINIA2: {
					if (st.isMemoState(2)) {
						htmltext = "32781-01.html";
					} else if (st.isMemoState(3)) {
						htmltext = "32781-04.html";
					}
					break;
				}
				case FREYAS_STEWARD: {
					if (st.isMemoState(2)) {
						htmltext = "32029-01.html";
						st.setCond(8, true);
					}
					break;
				}
			}
		}
		return htmltext;
	}
}
