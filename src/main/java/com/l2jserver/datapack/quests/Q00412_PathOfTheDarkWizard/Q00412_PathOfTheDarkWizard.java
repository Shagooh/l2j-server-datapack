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
package com.l2jserver.datapack.quests.Q00412_PathOfTheDarkWizard;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.holders.QuestItemChanceHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.gameserver.util.Util;

/**
 * Path Of The Dark Wizard (412)
 * @author ivantotov
 */
public final class Q00412_PathOfTheDarkWizard extends Quest {
	// NPCs
	private static final int CHARKEREN = 30415;
	private static final int ANNIKA = 30418;
	private static final int ARKENIA = 30419;
	private static final int VARIKA = 30421;
	// Monster
	private static final int MARSH_ZOMBIE = 20015;
	private static final int MISERY_SKELETON = 20022;
	private static final int SKELETON_SCOUT = 20045;
	private static final int SKELETON_HUNTER = 20517;
	private static final int SKELETON_HUNTER_ARCHER = 20518;
	// Items
	private static final int SEEDS_OF_ANGER = 1253;
	private static final int SEEDS_OF_DESPAIR = 1254;
	private static final int SEEDS_OF_HORROR = 1255;
	private static final int SEEDS_OF_LUNACY = 1256;
	private static final int LUCKY_KEY = 1277;
	private static final int CANDLE = 1278;
	private static final int HUB_SCENT = 1279;
	private static final QuestItemChanceHolder FAMILYS_REMAINS = new QuestItemChanceHolder(1257, 50.0, 3L);
	private static final QuestItemChanceHolder KNEE_BONE = new QuestItemChanceHolder(1259, 50.0, 2L);
	private static final QuestItemChanceHolder HEART_OF_LUNACY = new QuestItemChanceHolder(1260, 50.0, 3L);
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addSingleDrop(MARSH_ZOMBIE, FAMILYS_REMAINS).withRequiredItems(LUCKY_KEY)
			.bulkAddSingleDrop(KNEE_BONE).withNpcs(MISERY_SKELETON, SKELETON_HUNTER, SKELETON_HUNTER_ARCHER).withRequiredItems(CANDLE).build()
			.addSingleDrop(SKELETON_SCOUT, HEART_OF_LUNACY).withRequiredItems(HUB_SCENT)
			.build();
	// Reward
	private static final int JEWEL_OF_DARKNESS = 1261;
	// Misc
	private static final int MIN_LEVEL = 18;
	
	public Q00412_PathOfTheDarkWizard() {
		super(412, Q00412_PathOfTheDarkWizard.class.getSimpleName(), "Path Of The Dark Wizard");
		addStartNpc(VARIKA);
		addTalkId(VARIKA, CHARKEREN, ANNIKA, ARKENIA);
		addKillId(MARSH_ZOMBIE, MISERY_SKELETON, SKELETON_SCOUT, SKELETON_HUNTER, SKELETON_HUNTER_ARCHER);
		registerQuestItems(SEEDS_OF_ANGER, SEEDS_OF_DESPAIR, SEEDS_OF_HORROR, SEEDS_OF_LUNACY, FAMILYS_REMAINS.getId(), KNEE_BONE.getId(), HEART_OF_LUNACY.getId(), LUCKY_KEY, CANDLE, HUB_SCENT);
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
				if (player.getClassId() == ClassId.darkMage) {
					if (player.getLevel() >= MIN_LEVEL) {
						if (hasQuestItems(player, JEWEL_OF_DARKNESS)) {
							htmltext = "30421-04.htm";
						} else {
							qs.startQuest();
							giveItems(player, SEEDS_OF_DESPAIR, 1);
							htmltext = "30421-05.htm";
						}
					} else {
						htmltext = "30421-02.htm";
					}
				} else if (player.getClassId() == ClassId.darkWizard) {
					htmltext = "30421-02a.htm";
				} else {
					htmltext = "30421-03.htm";
				}
				break;
			}
			case "30421-06.html": {
				if (hasQuestItems(player, SEEDS_OF_ANGER)) {
					htmltext = event;
				} else {
					htmltext = "30421-07.html";
				}
				break;
			}
			case "30421-09.html": {
				if (hasQuestItems(player, SEEDS_OF_HORROR)) {
					htmltext = event;
				} else {
					htmltext = "30421-10.html";
				}
				break;
			}
			case "30421-11.html": {
				if (hasQuestItems(player, SEEDS_OF_LUNACY)) {
					htmltext = event;
				} else if (!hasQuestItems(player, SEEDS_OF_LUNACY) && hasQuestItems(player, SEEDS_OF_DESPAIR)) {
					htmltext = "30421-12.html";
				}
				break;
			}
			case "30421-08.html":
			case "30415-02.html": {
				htmltext = event;
				break;
			}
			case "30415-03.html": {
				giveItems(player, LUCKY_KEY, 1);
				htmltext = event;
				break;
			}
			case "30418-02.html": {
				giveItems(player, CANDLE, 1);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getQuestState(killer, false);
		if ((qs != null) && qs.isStarted() && Util.checkIfInRange(1500, npc, qs.getPlayer(), true)) {
			if (hasQuestItems(qs.getPlayer(), DROPLIST.get(npc).requiredItems())) {
				giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		if (qs.isCreated() || qs.isCompleted()) {
			if (npc.getId() == VARIKA) {
				if (!hasQuestItems(player, JEWEL_OF_DARKNESS)) {
					htmltext = "30421-01.htm";
				} else {
					htmltext = "30421-04.htm";
				}
			}
		} else if (qs.isStarted()) {
			switch (npc.getId()) {
				case VARIKA: {
					if (hasQuestItems(player, SEEDS_OF_DESPAIR, SEEDS_OF_HORROR, SEEDS_OF_LUNACY, SEEDS_OF_ANGER)) {
						giveAdena(player, 163800, true);
						giveItems(player, JEWEL_OF_DARKNESS, 1);
						final int level = player.getLevel();
						if (level >= 20) {
							addExpAndSp(player, 320534, 28630);
						} else if (level == 19) {
							addExpAndSp(player, 456128, 28630);
						} else {
							addExpAndSp(player, 591724, 35328);
						}
						qs.exitQuest(false, true);
						player.sendPacket(new SocialAction(player.getObjectId(), 3));
						qs.saveGlobalQuestVar("1ClassQuestFinished", "1");
						htmltext = "30421-13.html";
					} else if (hasQuestItems(player, SEEDS_OF_DESPAIR)) {
						if (!hasAtLeastOneQuestItem(player, FAMILYS_REMAINS.getId(), LUCKY_KEY, CANDLE, HUB_SCENT, KNEE_BONE.getId(), HEART_OF_LUNACY.getId())) {
							htmltext = "30421-14.html";
						} else if (!hasQuestItems(player, SEEDS_OF_ANGER)) {
							htmltext = "30421-08.html";
						} else if (!hasQuestItems(player, SEEDS_OF_HORROR)) {
							htmltext = "30421-15.html";
						} else if (!hasQuestItems(player, SEEDS_OF_LUNACY)) {
							htmltext = "30421-12.html";
						}
					}
					break;
				}
				case CHARKEREN: {
					if (!hasQuestItems(player, SEEDS_OF_ANGER) && hasQuestItems(player, SEEDS_OF_DESPAIR)) {
						if (!hasAtLeastOneQuestItem(player, FAMILYS_REMAINS.getId(), LUCKY_KEY)) {
							htmltext = "30415-01.html";
						} else if (hasQuestItems(player, LUCKY_KEY) && !hasItemsAtLimit(player, FAMILYS_REMAINS)) {
							htmltext = "30415-04.html";
						} else {
							giveItems(player, SEEDS_OF_ANGER, 1);
							takeItems(player, FAMILYS_REMAINS.getId(), -1);
							takeItems(player, LUCKY_KEY, 1);
							htmltext = "30415-05.html";
						}
					} else {
						htmltext = "30415-06.html";
					}
					break;
				}
				case ANNIKA: {
					if (!hasQuestItems(player, SEEDS_OF_HORROR) && hasQuestItems(player, SEEDS_OF_DESPAIR)) {
						if (!hasAtLeastOneQuestItem(player, CANDLE, KNEE_BONE.getId())) {
							htmltext = "30418-01.html";
						} else if (hasQuestItems(player, CANDLE) && !hasItemsAtLimit(player, KNEE_BONE)) {
							htmltext = "30418-03.html";
						} else {
							giveItems(player, SEEDS_OF_HORROR, 1);
							takeItems(player, KNEE_BONE.getId(), -1);
							takeItems(player, CANDLE, 1);
							htmltext = "30418-04.html";
						}
					}
					break;
				}
				case ARKENIA: {
					if (!hasQuestItems(player, SEEDS_OF_LUNACY)) {
						if (!hasAtLeastOneQuestItem(player, HUB_SCENT, HEART_OF_LUNACY.getId())) {
							giveItems(player, HUB_SCENT, 1);
							htmltext = "30419-01.html";
						} else if (hasQuestItems(player, HUB_SCENT) && !hasItemsAtLimit(player, HEART_OF_LUNACY)) {
							htmltext = "30419-02.html";
						} else {
							giveItems(player, SEEDS_OF_LUNACY, 1);
							takeItems(player, HEART_OF_LUNACY.getId(), -1);
							takeItems(player, HUB_SCENT, 1);
							htmltext = "30419-03.html";
						}
					}
					break;
				}
			}
		}
		return htmltext;
	}
}