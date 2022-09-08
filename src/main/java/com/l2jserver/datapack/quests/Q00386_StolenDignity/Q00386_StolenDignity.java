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
package com.l2jserver.datapack.quests.Q00386_StolenDignity;

import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;

import java.util.Arrays;
import java.util.Collections;

/**
 * Stolen Dignity (386)
 * @author Zealar
 */
public final class Q00386_StolenDignity extends Quest {
	// NPCs
	private static final int WAREHOUSE_KEEPER_ROMP = 30843;
	// Monsters
	private static final int CRIMSON_DRAKE = 20670;
	private static final int KADIOS = 20671;
	private static final int HUNGRY_CORPSE = 20954;
	private static final int PAST_KNIGHT = 20956;
	private static final int BLADE_DEATH = 20958;
	private static final int DARK_GUARD = 20959;
	private static final int BLOODY_GHOST = 20960;
	private static final int BLOODY_LORD = 20963;
	private static final int PAST_CREATURE = 20967;
	private static final int GIANT_SHADOW = 20969;
	private static final int ANCIENTS_SOLDIER = 20970;
	private static final int ANCIENTS_WARRIOR = 20971;
	private static final int SPITE_SOUL_LEADER = 20974;
	private static final int SPITE_SOUL_WIZARD = 20975;
	private static final int WRECKED_ARCHER = 21001;
	private static final int FLOAT_OF_GRAVE = 21003;
	private static final int GRAVE_PREDATOR = 21005;
	private static final int FALLEN_ORC_SHAMAN = 21020;
	private static final int SHARP_TALON_TIGER = 21021;
	private static final int GLOW_WISP = 21108;
	private static final int MARSH_PREDATOR = 21110;
	private static final int HAMES_ORC_SNIPER = 21113;
	private static final int CURSED_GUARDIAN = 21114;
	private static final int HAMES_ORC_CHIEFTAIN = 21116;
	private static final int FALLEN_ORC_SHAMAN_TRANS = 21258;
	private static final int SHARP_TALON_TIGER_TRANS = 21259;
	// Items
	private static final int Q_STOLEN_INF_ORE = 6363;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(CRIMSON_DRAKE, Q_STOLEN_INF_ORE, 20.200001)
			.addSingleDrop(KADIOS, Q_STOLEN_INF_ORE, 21.1)
			.addSingleDrop(HUNGRY_CORPSE, Q_STOLEN_INF_ORE, 18.4)
			.addSingleDrop(PAST_KNIGHT, Q_STOLEN_INF_ORE, 21.6)
			.addSingleDrop(BLADE_DEATH, Q_STOLEN_INF_ORE, 17.0)
			.addSingleDrop(DARK_GUARD, Q_STOLEN_INF_ORE, 27.3)
			.addSingleDrop(BLOODY_GHOST, Q_STOLEN_INF_ORE, 14.9)
			.addSingleDrop(BLOODY_LORD, Q_STOLEN_INF_ORE, 19.9)
			.addSingleDrop(PAST_CREATURE, Q_STOLEN_INF_ORE, 25.7)
			.addSingleDrop(GIANT_SHADOW, Q_STOLEN_INF_ORE, 20.5)
			.addSingleDrop(ANCIENTS_SOLDIER, Q_STOLEN_INF_ORE, 20.8)
			.addSingleDrop(ANCIENTS_WARRIOR, Q_STOLEN_INF_ORE, 29.9)
			.addSingleDrop(SPITE_SOUL_LEADER, Q_STOLEN_INF_ORE, 44.0)
			.addSingleDrop(SPITE_SOUL_WIZARD, Q_STOLEN_INF_ORE, 39.0)
			.addSingleDrop(WRECKED_ARCHER, Q_STOLEN_INF_ORE, 21.4)
			.addSingleDrop(FLOAT_OF_GRAVE, Q_STOLEN_INF_ORE, 17.3)
			.addSingleDrop(GRAVE_PREDATOR, Q_STOLEN_INF_ORE, 21.1)
			.addSingleDrop(FALLEN_ORC_SHAMAN, Q_STOLEN_INF_ORE, 47.8)
			.addSingleDrop(SHARP_TALON_TIGER, Q_STOLEN_INF_ORE, 23.4)
			.addSingleDrop(GLOW_WISP, Q_STOLEN_INF_ORE, 24.5)
			.addSingleDrop(MARSH_PREDATOR, Q_STOLEN_INF_ORE, 26.0)
			.addSingleDrop(HAMES_ORC_SNIPER, Q_STOLEN_INF_ORE, 37.0)
			.addSingleDrop(CURSED_GUARDIAN, Q_STOLEN_INF_ORE, 35.2)
			.addSingleDrop(HAMES_ORC_CHIEFTAIN, Q_STOLEN_INF_ORE, 48.7)
			.addSingleDrop(FALLEN_ORC_SHAMAN_TRANS, Q_STOLEN_INF_ORE, 48.7)
			.addSingleDrop(SHARP_TALON_TIGER_TRANS, Q_STOLEN_INF_ORE, 48.7)
			.build();
	// Rewards
	private static final int[] REWARDS = {
		5529, // Dragon Slayer Edge
		5532, // Meteor Shower Head
		5533, // Elysian Head
		5534, // Soul Bow Shaft
		5535, // Carnium Bow Shaft
		5536, // Bloody Orchid Head
		5537, // Soul Separator Head
		5538, // Dragon Grinder Edge
		5539, // Blood Tornado Edge
		5541, // Tallum Glaive Edge
		5542, // Halbard Edge
		5543, // Dasparions Staff Head
		5544, // Worldtrees Branch Head
		5545, // Dark Legions Edge Edge
		5546, // Sword of Miracle Edge
		5547, // Elemental Sword Edge
		5548, // Tallum Blade Edge
		8331, // Inferno Master Blade
		8341, // Eye of Soul Piece
		8342, // Dragon Flame Head Piece
		8349, // Doom Crusher Head
		8346, // Hammer of Destroyer Piece
		8712, // Sirr Blade Blade
		8713, // Sword of Ipos Blade
		8714, // Barakiel Axe Piece
		8715, // Tuning Fork of Behemoth Piece
		8716, // Naga Storm Piece
		8717, // Tiphon Spear Edge
		8718, // Shyid Bow Shaft
		8719, // Sobekk Hurricane Edge
		8720, // Tongue of Themis Piece
		8721, // Hand of Cabrio Head
		8722 // Crystal of Deamon Piece
	};

	public Q00386_StolenDignity() {
		super(386, Q00386_StolenDignity.class.getSimpleName(), "Stolen Dignity");
		addStartNpc(WAREHOUSE_KEEPER_ROMP);
		addTalkId(WAREHOUSE_KEEPER_ROMP);
		addKillId(CRIMSON_DRAKE, KADIOS, HUNGRY_CORPSE, PAST_KNIGHT, BLADE_DEATH, DARK_GUARD, BLOODY_GHOST, BLOODY_LORD, PAST_CREATURE, GIANT_SHADOW, ANCIENTS_SOLDIER, ANCIENTS_WARRIOR, SPITE_SOUL_LEADER, SPITE_SOUL_WIZARD, WRECKED_ARCHER, FLOAT_OF_GRAVE, GRAVE_PREDATOR, FALLEN_ORC_SHAMAN, SHARP_TALON_TIGER, GLOW_WISP, MARSH_PREDATOR, HAMES_ORC_SNIPER, CURSED_GUARDIAN, HAMES_ORC_CHIEFTAIN, FALLEN_ORC_SHAMAN_TRANS, SHARP_TALON_TIGER_TRANS);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (npc.getId() == WAREHOUSE_KEEPER_ROMP) {
			if (qs.isCreated()) {
				if (player.getLevel() >= 58) {
					return "30843-01.htm";
				}
				return "30843-04.html";
			}
			if (qs.getQuestItemsCount(Q_STOLEN_INF_ORE) < 100) {
				return "30843-06.html";
			}
			return "30843-07.html";
		}
		return htmltext;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, false);
		if ((qs != null) && (npc.getId() == WAREHOUSE_KEEPER_ROMP)) {
			if (event.equals("QUEST_ACCEPTED")) {
				qs.playSound(Sound.ITEMSOUND_QUEST_ACCEPT);
				qs.setMemoState(386);
				qs.startQuest();
				qs.showQuestionMark(386);
				qs.playSound(Sound.ITEMSOUND_QUEST_MIDDLE);
				return "30843-05.htm";
			}
			if (event.contains(".html")) {
				return event;
			}
			int ask = Integer.parseInt(event);
			switch (ask) {
				case 3:
					return "30843-09a.html";
				case 5:
					return "30843-03.html";
				case 6: {
					qs.exitQuest(true);
					return "30843-08.html";
				}
				case 9:
					return "30843-09.htm";
				case 8: {
					if (qs.getQuestItemsCount(Q_STOLEN_INF_ORE) >= 100) {
						qs.takeItems(Q_STOLEN_INF_ORE, 100);
						createBingoBoard(qs);
						return "30843-12.html";
					}
					return "30843-11.html";
				}
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18: {
					selectBingoNumber(qs, (ask - 9));
					return fillBoard(player, qs, getHtm(player.getHtmlPrefix(), "30843-13.html"));
				}
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 25:
				case 26:
				case 27: {
					return takeHtml(player, qs, (ask - 18));
				}
				case 55:
				case 56:
				case 57:
				case 58:
				case 59:
				case 60:
				case 61:
				case 62:
				case 63: {
					return beforeReward(player, qs, (ask - 54));
				}
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	private String takeHtml(L2PcInstance player, QuestState qs, int num) {
		String html = null;
		int i3;
		if (!isSelectedBingoNumber(qs, num)) {
			selectBingoNumber(qs, num);
			i3 = getBingoSelectCount(qs);
			
			if (i3 == 2) {
				html = getHtm(player.getHtmlPrefix(), "30843-14.html");
			} else if (i3 == 3) {
				html = getHtm(player.getHtmlPrefix(), "30843-16.html");
			} else if (i3 == 4) {
				html = getHtm(player.getHtmlPrefix(), "30843-18.html");
			} else if (i3 == 5) {
				html = getHtm(player.getHtmlPrefix(), "30843-20.html");
			}
			return fillBoard(player, qs, html);
		}
		i3 = getBingoSelectCount(qs);
		if (i3 == 1) {
			html = getHtm(player.getHtmlPrefix(), "30843-15.html");
		} else if (i3 == 2) {
			html = getHtm(player.getHtmlPrefix(), "30843-17.html");
		} else if (i3 == 3) {
			html = getHtm(player.getHtmlPrefix(), "30843-19.html");
		} else if (i3 == 4) {
			html = getHtm(player.getHtmlPrefix(), "30843-21.html");
		}
		return fillBoard(player, qs, html);
	}
	
	private String fillBoard(L2PcInstance player, QuestState qs, String html) {
		for (int i0 = 0; i0 < 9; i0 = i0 + 1) {
			int i1 = getNumberFromBingoBoard(qs, i0);
			if (isSelectedBingoNumber(qs, i1)) {
				html = html.replace("<?Cell" + (i0 + 1) + "?>", i1 + "");
			} else {
				html = html.replace("<?Cell" + (i0 + 1) + "?>", "?");
			}
		}
		return html;
	}
	
	private String colorBoard(L2PcInstance player, QuestState qs, String html) {
		for (int i0 = 0; i0 < 9; i0 = i0 + 1) {
			int i1 = getNumberFromBingoBoard(qs, i0);
			html = html.replace("<?FontColor" + (i0 + 1) + "?>", (isSelectedBingoNumber(qs, i1)) ? "ff0000" : "ffffff");
			html = html.replace("<?Cell" + (i0 + 1) + "?>", i1 + "");
		}
		return html;
	}
	
	private String beforeReward(L2PcInstance player, QuestState qs, int num) {
		if (!isSelectedBingoNumber(qs, num)) {
			selectBingoNumber(qs, num);
			int i3 = getMatchedBingoLineCount(qs);
			String html;
			if ((i3 == 3) && ((getBingoSelectCount(qs)) == 6)) {
				reward(player, 4);
				html = getHtm(player.getHtmlPrefix(), "30843-22.html");
			} else if ((i3 == 0) && (getBingoSelectCount(qs) == 6)) {
				reward(player, 10);
				html = getHtm(player.getHtmlPrefix(), "30843-24.html");
			} else {
				html = getHtm(player.getHtmlPrefix(), "30843-23.html");
			}
			return colorBoard(player, qs, html);
		}
		return fillBoard(player, qs, getHtm(player.getHtmlPrefix(), "30843-25.html"));
	}
	
	private void reward(L2PcInstance player, int count) {
		int index = getRandom(33);
		giveItems(player, REWARDS[index], count);
	}

	private void createBingoBoard(QuestState qs) {
		//@formatter:off
		Integer[] arr = {1,2,3,4,5,6,7,8,9};
		//@formatter:on
		Collections.shuffle(Arrays.asList(arr));
		qs.set("numbers", Arrays.asList(arr).toString().replaceAll("[^\\d ]", ""));
		qs.set("selected", "? ? ? ? ? ? ? ? ?");
	}

	private int getMatchedBingoLineCount(QuestState qs) {
		String[] q = qs.get("selected").split(" ");
		int found = 0;
		// Horizontal
		if ((q[0] + q[1] + q[2]).matches("\\d+")) {
			found++;
		}
		if ((q[3] + q[4] + q[5]).matches("\\d+")) {
			found++;
		}
		if ((q[6] + q[7] + q[8]).matches("\\d+")) {
			found++;
		}
		// Vertical
		if ((q[0] + q[3] + q[6]).matches("\\d+")) {
			found++;
		}
		if ((q[1] + q[4] + q[7]).matches("\\d+")) {
			found++;
		}
		if ((q[2] + q[5] + q[8]).matches("\\d+")) {
			found++;
		}
		// Diagonal
		if ((q[0] + q[4] + q[8]).matches("\\d+")) {
			found++;
		}
		if ((q[2] + q[4] + q[6]).matches("\\d+")) {
			found++;
		}
		return found;
	}

	private void selectBingoNumber(QuestState qs, int num) {
		String[] numbers = qs.get("numbers").split(" ");
		int pos = 0;
		for (int i = 0; i < numbers.length; i++) {
			if (Integer.parseInt(numbers[i]) == num) {
				pos = i;
				break;
			}
		}
		String[] selected = qs.get("selected").split(" ");
		for (int i = 0; i < selected.length; i++) {
			if (i == pos) {
				selected[i] = num + "";
				continue;
			}
		}
		StringBuilder result = new StringBuilder(selected[0]);
		for (int i = 1; i < selected.length; i++) {
			result.append(" ").append(selected[i]);
		}
		qs.set("selected", result.toString());
	}
	
	private boolean isSelectedBingoNumber(QuestState qs, int num) {
		return qs.get("selected").contains(num + "");
	}
	
	private int getNumberFromBingoBoard(QuestState qs, int num) {
		return Integer.parseInt(qs.get("numbers").split(" ")[num]);
	}
	
	private int getBingoSelectCount(QuestState qs) {
		String current = qs.get("selected");
		return current.replaceAll("\\D", "").length();
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getRandomPartyMemberState(killer, -1, 2, npc);
		if (qs != null) {
			giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
		}
		return super.onKill(npc, killer, isSummon);
	}
}
