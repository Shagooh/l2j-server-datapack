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
package com.l2jserver.datapack.quests.Q00416_PathOfTheOrcShaman;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.NpcSay;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;

import java.util.Map;

/**
 * Path of the Orc Shaman (416)
 * @author Adry_85
 */
public final class Q00416_PathOfTheOrcShaman extends Quest {
	// NPCs
	private static final int UMOS = 30502;
	private static final int TATARU_ZU_HESTUI = 30585;
	private static final int HESTUI_TOTEM_SPIRIT = 30592;
	private static final int DUDA_MARA_TOTEM_SPIRIT = 30593;
	private static final int MOIRA = 31979;
	private static final int TOTEM_SPIRIT_OF_GANDI = 32057;
	private static final int DEAD_LEOPARDS_CARCASS = 32090;
	// Monsters
	private static final int GRIZZLY_BEAR = 20335;
	private static final int SCARLET_SALAMANDER = 20415;
	private static final int KASHA_BLADE_SPIDER = 20478;
	private static final int KASHA_BEAR = 20479;
	// Quest Monsters
	private static final int DURKA_SPIRIT = 27056;
	private static final int BLACK_LEOPARD = 27319;
	// Items
	private static final int FIRE_CHARM = 1616;
	private static final int HESTUI_MASK = 1620;
	private static final int SECOND_FIERY_EGG = 1621;
	private static final int TOTEM_SPIRIT_CLAW = 1622;
	private static final int TATARUS_LETTER = 1623;
	private static final int FLAME_CHARM = 1624;
	private static final int BLOOD_CAULDRON = 1626;
	private static final int SPIRIT_NET = 1627;
	private static final int BOUND_DURKA_SPIRIT = 1628;
	private static final int DURKA_PARASITE = 1629;
	private static final int TOTEM_SPIRIT_BLOOD = 1630;
	private static final int MASK_OF_MEDIUM = 1631;
	private static final QuestItemChanceHolder KASHA_BEAR_PELT = new QuestItemChanceHolder(1617, 1L);
	private static final QuestItemChanceHolder KASHA_BLADE_SPIDER_HUSK = new QuestItemChanceHolder(1618, 1L);
	private static final QuestItemChanceHolder FIRST_FIERY_EGG = new QuestItemChanceHolder(1619, 1L);
	private static final QuestItemChanceHolder GRIZZLY_BLOOD = new QuestItemChanceHolder(1625, 3L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(SCARLET_SALAMANDER, FIRST_FIERY_EGG)
			.addSingleDrop(KASHA_BLADE_SPIDER, KASHA_BLADE_SPIDER_HUSK)
			.addSingleDrop(KASHA_BEAR, KASHA_BEAR_PELT)
			.addSingleDrop(GRIZZLY_BEAR, GRIZZLY_BLOOD)
			.build();
	// Misc
	private static final int MIN_LEVEL = 18;
	// Mobs
	private static final Map<Integer, Integer> MOBS_CONDITIONS = Map.of(
			SCARLET_SALAMANDER, 1,
			KASHA_BLADE_SPIDER, 1,
			KASHA_BEAR, 1,
			GRIZZLY_BEAR, 6,
			20038, 9, // poison_spider
			20043, 9, // bind_poison_spider
			DURKA_SPIRIT, 9);

	public Q00416_PathOfTheOrcShaman() {
		super(416, Q00416_PathOfTheOrcShaman.class.getSimpleName(), "Path of the Orc Shaman");
		addStartNpc(TATARU_ZU_HESTUI);
		addTalkId(TATARU_ZU_HESTUI, UMOS, MOIRA, DEAD_LEOPARDS_CARCASS, DUDA_MARA_TOTEM_SPIRIT, HESTUI_TOTEM_SPIRIT, TOTEM_SPIRIT_OF_GANDI);
		addKillId(MOBS_CONDITIONS.keySet());
		addKillId(BLACK_LEOPARD);
		registerQuestItems(FIRE_CHARM, KASHA_BEAR_PELT.getId(), KASHA_BLADE_SPIDER_HUSK.getId(), FIRST_FIERY_EGG.getId(), HESTUI_MASK, SECOND_FIERY_EGG, TOTEM_SPIRIT_CLAW, TATARUS_LETTER, FLAME_CHARM, GRIZZLY_BLOOD.getId(), BLOOD_CAULDRON, SPIRIT_NET, BOUND_DURKA_SPIRIT, DURKA_PARASITE, TOTEM_SPIRIT_BLOOD);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		String htmltext = null;
		switch (event) {
			case "START": {
				if (player.getClassId() != ClassId.orcMage) {
					if (player.getClassId() == ClassId.orcShaman) {
						htmltext = "30585-02.htm";
					} else {
						htmltext = "30585-03.htm";
					}
				} else if (player.getLevel() < MIN_LEVEL) {
					htmltext = "30585-04.htm";
				} else if (hasQuestItems(player, MASK_OF_MEDIUM)) {
					htmltext = "30585-05.htm";
				} else {
					htmltext = "30585-06.htm";
				}
				break;
			}
			case "30585-07.htm": {
				st.startQuest();
				st.setMemoState(1);
				giveItems(player, FIRE_CHARM, 1);
				htmltext = event;
				break;
			}
			case "30585-12.html": {
				if (hasQuestItems(player, TOTEM_SPIRIT_CLAW)) {
					htmltext = event;
				}
				break;
			}
			case "30585-13.html": {
				if (hasQuestItems(player, TOTEM_SPIRIT_CLAW)) {
					takeItems(player, TOTEM_SPIRIT_CLAW, -1);
					giveItems(player, TATARUS_LETTER, 1);
					st.setCond(5, true);
					htmltext = event;
				}
				break;
			}
			case "30585-14.html": {
				if (hasQuestItems(player, TOTEM_SPIRIT_CLAW)) {
					takeItems(player, TOTEM_SPIRIT_CLAW, -1);
					st.setCond(12, true);
					st.setMemoState(100);
					htmltext = event;
				}
				break;
			}
			case "30502-07.html": {
				if (hasQuestItems(player, TOTEM_SPIRIT_BLOOD)) {
					takeItems(player, TOTEM_SPIRIT_BLOOD, -1);
					giveItems(player, MASK_OF_MEDIUM, 1);
					final int level = player.getLevel();
					if (level >= 20) {
						addExpAndSp(player, 320534, 22992);
					} else if (level >= 19) {
						addExpAndSp(player, 456128, 29690);
					} else {
						addExpAndSp(player, 591724, 36388);
					}
					giveAdena(player, 163800, true);
					st.exitQuest(false, true);
					player.sendPacket(new SocialAction(player.getObjectId(), 3));
					st.saveGlobalQuestVar("1ClassQuestFinished", "1");
					htmltext = event;
				}
				break;
			}
			case "32090-05.html": {
				if (st.isMemoState(106)) {
					htmltext = event;
				}
				break;
			}
			case "32090-06.html": {
				if (st.isMemoState(106)) {
					st.setMemoState(107);
					st.setCond(18, true);
					htmltext = event;
				}
				break;
			}
			case "30593-02.html": {
				if (hasQuestItems(player, BLOOD_CAULDRON)) {
					htmltext = event;
				}
				break;
			}
			case "30593-03.html": {
				if (hasQuestItems(player, BLOOD_CAULDRON)) {
					takeItems(player, BLOOD_CAULDRON, -1);
					giveItems(player, SPIRIT_NET, 1);
					st.setCond(9, true);
					htmltext = event;
				}
				break;
			}
			case "30592-02.html": {
				if (hasQuestItems(player, HESTUI_MASK, SECOND_FIERY_EGG)) {
					htmltext = event;
				}
				break;
			}
			case "30592-03.html": {
				if (hasQuestItems(player, HESTUI_MASK, SECOND_FIERY_EGG)) {
					takeItems(player, -1, HESTUI_MASK, SECOND_FIERY_EGG);
					giveItems(player, TOTEM_SPIRIT_CLAW, 1);
					st.setCond(4, true);
					htmltext = event;
				}
				break;
			}
			case "32057-02.html": {
				if (st.isMemoState(101)) {
					st.setMemoState(102);
					st.setCond(14, true);
					htmltext = event;
				}
				break;
			}
			case "32057-05.html": {
				if (st.isMemoState(109)) {
					st.setMemoState(110);
					st.setCond(21, true);
					htmltext = event;
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		final QuestState st = getRandomPartyMemberState(player, -1, 3, npc);
		if (st == null) {
			return super.onKill(npc, player, isSummon);
		}
		
		if (npc.getId() == BLACK_LEOPARD) {
			switch (st.getMemoState()) {
				case 102 -> st.setMemoState(103);
				case 103 -> {
					st.setMemoState(104);
					st.setCond(15, true);
					if (getRandom(100) < 66) {
						npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getId(), NpcStringId.MY_DEAR_FRIEND_OF_S1_WHO_HAS_GONE_ON_AHEAD_OF_ME).addStringParameter(st.getPlayer().getName()));
					}
				}
				case 105 -> {
					st.setMemoState(106);
					st.setCond(17, true);
					if (getRandom(100) < 66) {
						npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getId(), NpcStringId.LISTEN_TO_TEJAKAR_GANDI_YOUNG_OROKA_THE_SPIRIT_OF_THE_SLAIN_LEOPARD_IS_CALLING_YOU_S1).addStringParameter(st.getPlayer().getName()));
					}
				}
				case 107 -> {
					st.setMemoState(108);
					st.setCond(19, true);
				}
			}
			return super.onKill(npc, player, isSummon);
		}
		
		final int mobCond = MOBS_CONDITIONS.get(npc.getId());
		if (mobCond == st.getCond()) {
			if (st.isCond(1) && hasQuestItems(st.getPlayer(), FIRE_CHARM)) {
				if (giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true)
					&& hasQuestItems(st.getPlayer(), FIRST_FIERY_EGG.getId(), KASHA_BLADE_SPIDER_HUSK.getId(), KASHA_BEAR_PELT.getId())) {
					st.setCond(2);
				}
			} else if (st.isCond(6) && hasQuestItems(st.getPlayer(), FLAME_CHARM)) {
				if (giveItemRandomly(st.getPlayer(), npc, DROPLIST.get(npc), true)) {
					st.setCond(7);
				}
			} else if (st.isCond(9) && hasQuestItems(st.getPlayer(), SPIRIT_NET) //
				&& !hasQuestItems(st.getPlayer(), BOUND_DURKA_SPIRIT) //
				&& (getQuestItemsCount(st.getPlayer(), DURKA_PARASITE) <= 8)) {
				if ((npc.getId() == 20038) || (npc.getId() == 20043)) {
					final int random = getRandom(10);
					final long itemCount = getQuestItemsCount(st.getPlayer(), DURKA_PARASITE);
					if ((((itemCount == 5) && (random < 1))) //
						|| ((itemCount == 6) && (random < 2)) //
						|| ((itemCount == 7) && (random < 2)) //
						|| (itemCount >= 8)) {
						takeItems(player, DURKA_PARASITE, -1);
						addSpawn(DURKA_SPIRIT, npc.getX(), npc.getY(), npc.getZ(), 0, true, 0, false);
						playSound(st.getPlayer(), Sound.ITEMSOUND_QUEST_BEFORE_BATTLE);
					} else {
						giveItems(st.getPlayer(), DURKA_PARASITE, 1);
						playSound(st.getPlayer(), Sound.ITEMSOUND_QUEST_ITEMGET);
					}
				} else {
					giveItems(st.getPlayer(), BOUND_DURKA_SPIRIT, 1);
					takeItems(st.getPlayer(), -1, DURKA_PARASITE, SPIRIT_NET);
				}
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (st.isCreated()) {
			if (npc.getId() == TATARU_ZU_HESTUI) {
				htmltext = "30585-01.htm";
			}
		} else if (st.isStarted()) {
			switch (npc.getId()) {
				case TATARU_ZU_HESTUI: {
					if (st.isMemoState(1)) {
						if (hasQuestItems(player, FIRE_CHARM)) {
							if (!hasItemsAtLimit(player, KASHA_BEAR_PELT, KASHA_BLADE_SPIDER_HUSK, FIRST_FIERY_EGG)) {
								htmltext = "30585-08.html";
							} else {
								takeItems(player, -1, FIRE_CHARM, KASHA_BEAR_PELT.getId(), KASHA_BLADE_SPIDER_HUSK.getId(), FIRST_FIERY_EGG.getId());
								giveItems(player, HESTUI_MASK, 1);
								giveItems(player, SECOND_FIERY_EGG, 1);
								st.setCond(3, true);
								htmltext = "30585-09.html";
							}
						} else if (hasQuestItems(player, HESTUI_MASK, SECOND_FIERY_EGG)) {
							htmltext = "30585-10.html";
						} else if (hasQuestItems(player, TOTEM_SPIRIT_CLAW)) {
							htmltext = "30585-11.html";
						} else if (hasQuestItems(player, TATARUS_LETTER)) {
							htmltext = "30585-15.html";
						} else if (hasAtLeastOneQuestItem(player, GRIZZLY_BLOOD.getId(), FLAME_CHARM, BLOOD_CAULDRON, SPIRIT_NET, BOUND_DURKA_SPIRIT, TOTEM_SPIRIT_BLOOD)) {
							htmltext = "30585-16.html";
						}
					} else if (st.isMemoState(100)) {
						htmltext = "30585-14.html";
					}
					break;
				}
				case UMOS: {
					if (st.isMemoState(1)) {
						if (hasQuestItems(player, TATARUS_LETTER)) {
							giveItems(player, FLAME_CHARM, 1);
							takeItems(player, TATARUS_LETTER, -1);
							st.setCond(6, true);
							htmltext = "30502-01.html";
						} else if (hasQuestItems(player, FLAME_CHARM)) {
							if (!hasItemsAtLimit(player, GRIZZLY_BLOOD)) {
								htmltext = "30502-02.html";
							} else {
								takeItems(player, -1, FLAME_CHARM, GRIZZLY_BLOOD.getId());
								giveItems(player, BLOOD_CAULDRON, 1);
								st.setCond(8, true);
								htmltext = "30502-03.html";
							}
						} else if (hasQuestItems(player, BLOOD_CAULDRON)) {
							htmltext = "30502-04.html";
						} else if (hasAtLeastOneQuestItem(player, BOUND_DURKA_SPIRIT, SPIRIT_NET)) {
							htmltext = "30502-05.html";
						} else if (hasQuestItems(player, TOTEM_SPIRIT_BLOOD)) {
							htmltext = "30502-06.html";
						}
					}
					break;
				}
				case MOIRA: {
					final int memoState = st.getMemoState();
					if (memoState == 100) {
						st.setMemoState(101);
						st.setCond(13, true);
						htmltext = "31979-01.html";
					} else if ((memoState >= 101) && (memoState < 108)) {
						htmltext = "31979-02.html";
					} else if (memoState == 110) {
						giveItems(player, MASK_OF_MEDIUM, 1);
						final int level = player.getLevel();
						if (level >= 20) {
							addExpAndSp(player, 160267, 11496);
						} else if (level >= 19) {
							addExpAndSp(player, 228064, 14845);
						} else {
							addExpAndSp(player, 295862, 18194);
						}
						giveAdena(player, 81900, true);
						st.exitQuest(false, true);
						player.sendPacket(new SocialAction(player.getObjectId(), 3));
						st.saveGlobalQuestVar("1ClassQuestFinished", "1");
						htmltext = "31979-03.html";
					}
					break;
				}
				case DEAD_LEOPARDS_CARCASS: {
					switch (st.getMemoState()) {
						case 102:
						case 103: {
							htmltext = "32090-01.html";
							break;
						}
						case 104: {
							st.setMemoState(105);
							st.setCond(16, true);
							htmltext = "32090-03.html";
							break;
						}
						case 105: {
							htmltext = "32090-01.html";
							break;
						}
						case 106: {
							htmltext = "32090-04.html";
							break;
						}
						case 107: {
							htmltext = "32090-07.html";
							break;
						}
						case 108: {
							st.setMemoState(109);
							st.setCond(20, true);
							htmltext = "32090-08.html";
							break;
						}
					}
					break;
				}
				case DUDA_MARA_TOTEM_SPIRIT: {
					if (st.isMemoState(1)) {
						if (hasQuestItems(player, BLOOD_CAULDRON)) {
							htmltext = "30593-01.html";
						} else if (hasQuestItems(player, SPIRIT_NET) && !hasQuestItems(player, BOUND_DURKA_SPIRIT)) {
							htmltext = "30593-04.html";
						} else if (!hasQuestItems(player, SPIRIT_NET) && hasQuestItems(player, BOUND_DURKA_SPIRIT)) {
							takeItems(player, BOUND_DURKA_SPIRIT, -1);
							giveItems(player, TOTEM_SPIRIT_BLOOD, 1);
							st.setCond(11, true);
							htmltext = "30593-05.html";
						} else if (hasQuestItems(player, TOTEM_SPIRIT_BLOOD)) {
							htmltext = "30593-06.html";
						}
					}
					break;
				}
				case HESTUI_TOTEM_SPIRIT: {
					if (st.isMemoState(1)) {
						if (hasQuestItems(player, HESTUI_MASK, SECOND_FIERY_EGG)) {
							htmltext = "30592-01.html";
						} else if (hasQuestItems(player, TOTEM_SPIRIT_CLAW)) {
							htmltext = "30592-04.html";
						} else if (hasAtLeastOneQuestItem(player, GRIZZLY_BLOOD.getId(), FLAME_CHARM, BLOOD_CAULDRON, SPIRIT_NET, BOUND_DURKA_SPIRIT, TOTEM_SPIRIT_BLOOD, TATARUS_LETTER)) {
							htmltext = "30592-05.html";
						}
					}
					break;
				}
				case TOTEM_SPIRIT_OF_GANDI: {
					switch (st.getMemoState()) {
						case 101: {
							htmltext = "32057-01.html";
							break;
						}
						case 102: {
							htmltext = "32057-03.html";
							break;
						}
						case 109: {
							htmltext = "32057-04.html";
							break;
						}
					}
					break;
				}
			}
		}
		return htmltext;
	}
}
