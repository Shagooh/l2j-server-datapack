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
package com.l2jserver.datapack.quests.Q00325_GrimCollector;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestDroplist;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.util.Util;

/**
 * Grim Collector (325)
 * @author ivantotov
 */
public final class Q00325_GrimCollector extends Quest {
	// NPCs
	private static final int GUARD_CURTIZ = 30336;
	private static final int VARSAK = 30342;
	private static final int SAMED = 30434;
	// Items
	private static final int ANATOMY_DIAGRAM = 1349;
	private static final int ZOMBIE_HEAD = 1350;
	private static final int ZOMBIE_HEART = 1351;
	private static final int ZOMBIE_LIVER = 1352;
	private static final int SKULL = 1353;
	private static final int RIB_BONE = 1354;
	private static final int SPINE = 1355;
	private static final int ARM_BONE = 1356;
	private static final int THIGH_BONE = 1357;
	private static final int COMPLETE_SKELETON = 1358;
	// Droplist
	private static final QuestDroplist DROPLIST = QuestDroplist.builder()
			.addGroupedDrop(20026, 75.0).withDropItem(ZOMBIE_HEAD, 40.0).withDropItem(ZOMBIE_HEART, 26.67).withDropItem(ZOMBIE_LIVER, 33.33).build()
			.addGroupedDrop(20029, 75.0).withDropItem(ZOMBIE_HEAD, 40.0).withDropItem(ZOMBIE_HEART, 29.33).withDropItem(ZOMBIE_LIVER, 30.67).build()
			.addGroupedDrop(20035, 79.0).withDropItem(SKULL, 6.33).withDropItem(RIB_BONE, 12.65).withDropItem(SPINE, 17.72).withDropItem(THIGH_BONE, 63.30).build()
			.addGroupedDrop(20042, 86.0).withDropItem(SKULL, 6.97).withDropItem(RIB_BONE, 15.12).withDropItem(ARM_BONE, 58.15).withDropItem(THIGH_BONE, 19.76).build()
			.addGroupedDrop(20045, 97.0).withDropItem(SKULL, 9.28).withDropItem(SPINE, 51.55).withDropItem(ARM_BONE, 18.55).withDropItem(THIGH_BONE, 20.62).build()
			.addGroupedDrop(20051, 100.0).withDropItem(SKULL, 9.0).withDropItem(RIB_BONE, 50.0).withDropItem(SPINE, 20.0).withDropItem(ARM_BONE, 21.0).build()
			.addGroupedDrop(20457, 80.0).withDropItem(ZOMBIE_HEAD, 50.0).withDropItem(ZOMBIE_HEART, 25.0).withDropItem(ZOMBIE_LIVER, 25.0).build()
			.addGroupedDrop(20458, 100.0).withDropItem(ZOMBIE_HEAD, 40.0).withDropItem(ZOMBIE_HEART, 30.0).withDropItem(ZOMBIE_LIVER, 30.0).build()
			.addGroupedDrop(20514, 64.0).withDropItem(SKULL, 9.37).withDropItem(RIB_BONE, 23.43).withDropItem(SPINE, 14.07).withDropItem(ARM_BONE, 1.57).withDropItem(THIGH_BONE, 51.56).build()
			.addGroupedDrop(20515, 69.0).withDropItem(SKULL, 7.24).withDropItem(RIB_BONE, 21.74).withDropItem(SPINE, 15.94).withDropItem(ARM_BONE, 2.90).withDropItem(THIGH_BONE, 52.18).build()
			.build();
	// Misc
	private static final int MIN_LEVEL = 15;
	
	public Q00325_GrimCollector() {
		super(325, Q00325_GrimCollector.class.getSimpleName(), "Grim Collector");
		addStartNpc(GUARD_CURTIZ);
		addTalkId(GUARD_CURTIZ, VARSAK, SAMED);
		addKillId(DROPLIST.getNpcIds());
		registerQuestItems(ANATOMY_DIAGRAM, ZOMBIE_HEAD, ZOMBIE_HEART, ZOMBIE_LIVER, SKULL, RIB_BONE, SPINE, ARM_BONE, THIGH_BONE, COMPLETE_SKELETON);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		final QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		String htmltext = null;
		switch (event) {
			case "30336-03.htm": {
				if (st.isCreated()) {
					st.startQuest();
					htmltext = event;
				}
				break;
			}
			case "assembleSkeleton": {
				if (!hasQuestItems(player, SPINE, ARM_BONE, SKULL, RIB_BONE, THIGH_BONE)) {
					htmltext = "30342-02.html";
				} else {
					takeItems(player, 1, SPINE, ARM_BONE, SKULL, RIB_BONE, THIGH_BONE);
					
					if (getRandom(5) < 4) {
						giveItems(player, COMPLETE_SKELETON, 1);
						htmltext = "30342-03.html";
					} else {
						htmltext = "30342-04.html";
					}
				}
				break;
			}
			case "30434-02.htm": {
				htmltext = event;
				break;
			}
			case "30434-03.html": {
				st.giveItems(ANATOMY_DIAGRAM, 1);
				htmltext = event;
				break;
			}
			case "30434-06.html":
			case "30434-07.html": {
				final long head = st.getQuestItemsCount(ZOMBIE_HEAD);
				final long heart = st.getQuestItemsCount(ZOMBIE_HEART);
				final long liver = st.getQuestItemsCount(ZOMBIE_LIVER);
				final long skull = st.getQuestItemsCount(SKULL);
				final long rib = st.getQuestItemsCount(RIB_BONE);
				final long spine = st.getQuestItemsCount(SPINE);
				final long arm = st.getQuestItemsCount(ARM_BONE);
				final long thigh = st.getQuestItemsCount(THIGH_BONE);
				final long complete = st.getQuestItemsCount(COMPLETE_SKELETON);
				final long totalCount = (head + heart + liver + skull + rib + spine + arm + thigh + complete);
				if (totalCount > 0) {
					long sum = ((head * 30) + (heart * 20) + (liver * 20) + (skull * 100) + (rib * 40) + (spine * 14) + (arm * 14) + (thigh * 14));
					
					if (totalCount >= 10) {
						sum += 1629;
					}
					
					if (complete > 0) {
						sum += 543 + (complete * 341);
					}
					
					st.giveAdena(sum, true);
				}
				
				takeItems(player, -1, ZOMBIE_HEAD, ZOMBIE_HEART, ZOMBIE_LIVER, SKULL, RIB_BONE, SPINE, ARM_BONE, THIGH_BONE, COMPLETE_SKELETON);
				
				if (event.equals("30434-06.html")) {
					st.exitQuest(true, true);
				}
				
				htmltext = event;
				break;
			}
			case "30434-09.html": {
				final long complete = st.getQuestItemsCount(COMPLETE_SKELETON);
				if (complete > 0) {
					st.giveAdena(((complete * 341) + 543), true);
					st.takeItems(COMPLETE_SKELETON, -1);
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final QuestState qs = getQuestState(killer, false);
		
		if ((qs == null) || !qs.isStarted()) {
			return super.onKill(npc, killer, isSummon);
		}
		
		if (!Util.checkIfInRange(1500, killer, npc, true) || !qs.hasQuestItems(ANATOMY_DIAGRAM)) {
			return super.onKill(npc, killer, isSummon);
		}

		giveItemRandomly(qs.getPlayer(), npc, DROPLIST.get(npc), true);

		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		switch (npc.getId()) {
			case GUARD_CURTIZ: {
				switch (st.getState()) {
					case State.CREATED: {
						htmltext = (player.getLevel() >= MIN_LEVEL) ? "30336-02.htm" : "30336-01.htm";
						break;
					}
					case State.STARTED: {
						htmltext = st.hasQuestItems(ANATOMY_DIAGRAM) ? "30336-05.html" : "30336-04.html";
						break;
					}
				}
				break;
			}
			case VARSAK: {
				if (st.isStarted() && st.hasQuestItems(ANATOMY_DIAGRAM)) {
					htmltext = "30342-01.html";
				}
				break;
			}
			case SAMED: {
				if (st.isStarted()) {
					if (!st.hasQuestItems(ANATOMY_DIAGRAM)) {
						htmltext = "30434-01.html";
					} else {
						if (!hasAtLeastOneQuestItem(player, getRegisteredItemIds())) {
							htmltext = "30434-04.html";
						} else if (!st.hasQuestItems(COMPLETE_SKELETON)) {
							htmltext = "30434-05.html";
						} else {
							htmltext = "30434-08.html";
						}
					}
				}
				break;
			}
		}
		return htmltext;
	}
}
