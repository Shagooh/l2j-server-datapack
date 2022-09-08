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
package com.l2jserver.datapack.quests.Q00372_LegacyOfInsolence;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.ListenerRegisterType;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TODO: Check NPC rewards (need to be able to stub AbstractScript#getRandom())
 * TODO: Check Mob drops (need to be able to stub AbstractScript#giveItemRandomly())
 * @author Noé Caratini aka Kita
 */
@ExtendWith(MockitoExtension.class)
public class Q00372LegacyOfInsolenceTest {
	private static final String QUEST_NAME = Q00372_LegacyOfInsolence.class.getSimpleName();
	// Misc
	private static final int MIN_LEVEL = 59;
	// NPCs
	private static final int WRONG_NPC = 1;
	private static final int TRADER_HOLLY = 30839;
	private static final int WAREHOUSE_KEEPER_WALDERAL = 30844;
	private static final int MAGISTER_DESMOND = 30855;
	private static final int ANTIQUE_DEALER_PATRIN = 30929;
	private static final int CLAUDIA_ATHEBALDT = 31001;
	// Monsters
	private static final int CORRUPT_SAGE = 20817;
	private static final int ERIN_EDIUNCE = 20821;
	private static final int HALLATES_INSPECTOR = 20825;
	private static final int PLATINUM_TRIBE_OVERLORD = 20829;
	private static final int MESSENGER_ANGEL = 21062;
	private static final int PLATINUM_GUARDIAN_PREFECT = 21069;
	// Items
	private static final List<Integer> REVELATION_OF_THE_SEALS = List.of(5972, 5973, 5974, 5975, 5976, 5977, 5978);
	private static final List<Integer> ANCIENT_EPIC = List.of(5979, 5980, 5981, 5982, 5983);
	private static final List<Integer> IMPERIAL_GENEALOGY = List.of(5984, 5985, 5986, 5987, 5988);
	private static final List<Integer> BLUEPRINTS = List.of(5989, 5990, 5991, 5992, 5993, 5994, 5995, 5996, 5997, 5998, 5999, 6000, 6001);
	
	@Mock
	private L2PcInstance player;
	@Mock
	private L2Npc npc;
	@Mock
	private PcInventory inventory;
	@Mock
	private QuestState qs;
	
	@Spy
	private Quest quest = new Q00372_LegacyOfInsolence();
	
	@BeforeEach
	void setUp() {
		lenient().when(player.getQuestState(QUEST_NAME)).thenReturn(qs);
		lenient().when(player.getInventory()).thenReturn(inventory);
	}
	
	@Test
	public void shouldInitQuestCorrectly() {
		assertThat(quest.getId()).isEqualTo(372);
		assertThat(quest.getRegisteredIds(ListenerRegisterType.NPC)).containsExactlyInAnyOrder(
				TRADER_HOLLY,
				WAREHOUSE_KEEPER_WALDERAL,
				MAGISTER_DESMOND,
				ANTIQUE_DEALER_PATRIN,
				CLAUDIA_ATHEBALDT,
				CORRUPT_SAGE,
				ERIN_EDIUNCE,
				HALLATES_INSPECTOR,
				PLATINUM_TRIBE_OVERLORD,
				MESSENGER_ANGEL,
				PLATINUM_GUARDIAN_PREFECT
		);
	}
	
	@Test
	public void onAdvEventWithNoQuestStateShouldReturnNull() {
		when(player.getQuestState(QUEST_NAME)).thenReturn(null);
		
		String result = quest.onAdvEvent("30844-04.htm", npc, player);
		
		assertThat(result).isNull();
	}
	
	@Test
	public void onAdvEventWithUnsupportedEventShouldReturnNull() {
		String event = "00001-01.htm";
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isNull();
	}
	
	@Test
	public void onAdvEventWalderalStartQuest() {
		String event = "30844-04.htm";
		when(qs.isCreated()).thenReturn(true);
		
		String result = quest.onAdvEvent(event, npc, player);
		
		verify(qs).startQuest();
		assertThat(result).isEqualTo(event);
	}
	
	@Test
	public void onAdvEventWalderalQuestAlreadyStarted() {
		String event = "30844-04.htm";
		when(qs.isCreated()).thenReturn(false);
		
		String result = quest.onAdvEvent(event, npc, player);
		
		verify(qs, never()).startQuest();
		assertThat(result).isNull();
	}
	
	@Test
	public void onAdvEventWalderalOtherBooks() {
		String event = "30844-05b.html";
		
		String result = quest.onAdvEvent(event, npc, player);
		
		verify(qs).setCond(2);
		assertThat(result).isEqualTo(event);
	}
	
	@Test
	public void onAdvEventWalderalNotAllBlueprints() {
		String event = "30844-07.html";
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo("30844-06.html");
	}
	
	@Test
	public void onAdvEventWalderalAllBlueprints() {
		String event = "30844-07.html";
		L2ItemInstance item = mock(L2ItemInstance.class);
		BLUEPRINTS.forEach(itemId -> when(inventory.getItemByItemId(itemId)).thenReturn(item));
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo(event);
	}
	
	@Test
	public void onAdvEventWalderalDarkCrystalAllBlueprints() {
		String event = "30844-07a.html";
		BLUEPRINTS.forEach(this::stubInventoryItem);
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo(event);
	}
	
	@Test
	public void onAdvEventWalderalDarkCrystalNotAllBlueprints() {
		String event = "30844-07a.html";
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo("30844-07e.html");
	}
	
	@Test
	public void onAdvEventWalderalTallumAllBlueprints() {
		String event = "30844-07b.html";
		BLUEPRINTS.forEach(this::stubInventoryItem);
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo(event);
	}
	
	@Test
	public void onAdvEventWalderalTallumNotAllBlueprints() {
		String event = "30844-07b.html";
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo("30844-07e.html");
	}
	
	@Test
	public void onAdvEventWalderalNightmareAllBlueprints() {
		String event = "30844-07c.html";
		BLUEPRINTS.forEach(this::stubInventoryItem);
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo(event);
	}
	
	@Test
	public void onAdvEventWalderalNightmareNotAllBlueprints() {
		String event = "30844-07c.html";
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo("30844-07e.html");
	}
	
	@Test
	public void onAdvEventWalderalMajesticAllBlueprints() {
		String event = "30844-07c.html";
		BLUEPRINTS.forEach(this::stubInventoryItem);
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo(event);
	}
	
	@Test
	public void onAdvEventWalderalMajesticNotAllBlueprints() {
		String event = "30844-07c.html";
		
		String result = quest.onAdvEvent(event, npc, player);
		
		assertThat(result).isEqualTo("30844-07e.html");
	}
	
	@Test
	public void onAdvEventWalderalAbortQuest() {
		String event = "30844-09.html";
		
		String result = quest.onAdvEvent(event, npc, player);
		
		verify(qs).exitQuest(true, true);
		assertThat(result).isEqualTo(event);
	}
	
	@Test
	public void onAdvEventOtherSupportedEvents() {
		List.of("30844-03.htm", "30844-05.html", "30844-05a.html", "30844-08.html", "30844-10.html", "30844-11.html")
				.forEach(event -> {
					String result = quest.onAdvEvent(event, npc, player);
					
					assertThat(result).isEqualTo(event);
				});
	}
	
	@Test
	public void onTalkShouldInitQuestStateForPlayer() {
		when(player.getQuestState(QUEST_NAME)).thenReturn(null);
		
		quest.onTalk(npc, player);
		
		verify(quest).newQuestState(player);
	}
	
	@Test
	public void onTalkToWrongNpcWithStateCreated() {
		when(qs.isCreated()).thenReturn(true);
		when(npc.getId()).thenReturn(WRONG_NPC);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(Quest.getNoQuestMsg(player));
	}
	
	@Test
	public void onTalkToWalderalWithStateCreatedAndLevelTooLow() {
		when(qs.isCreated()).thenReturn(true);
		when(npc.getId()).thenReturn(WAREHOUSE_KEEPER_WALDERAL);
		when(player.getLevel()).thenReturn(MIN_LEVEL - 1);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(WAREHOUSE_KEEPER_WALDERAL + "-01.htm");
	}
	
	@Test
	public void onTalkToWalderalWithStateCreated() {
		when(qs.isCreated()).thenReturn(true);
		when(npc.getId()).thenReturn(WAREHOUSE_KEEPER_WALDERAL);
		when(player.getLevel()).thenReturn(MIN_LEVEL);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(WAREHOUSE_KEEPER_WALDERAL + "-02.htm");
	}
	
	@Test
	public void onTalkToHollyWithStateCreated() {
		when(qs.isCreated()).thenReturn(true);
		when(npc.getId()).thenReturn(TRADER_HOLLY);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(Quest.getNoQuestMsg(player));
	}
	
	@Test
	public void onTalkToDesmondWithStateCreated() {
		when(qs.isCreated()).thenReturn(true);
		when(npc.getId()).thenReturn(MAGISTER_DESMOND);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(Quest.getNoQuestMsg(player));
	}
	
	@Test
	public void onTalkToPatrinWithStateCreated() {
		when(qs.isCreated()).thenReturn(true);
		when(npc.getId()).thenReturn(ANTIQUE_DEALER_PATRIN);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(Quest.getNoQuestMsg(player));
	}
	
	@Test
	public void onTalkToClaudiaWithStateCreated() {
		when(qs.isCreated()).thenReturn(true);
		when(npc.getId()).thenReturn(CLAUDIA_ATHEBALDT);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(Quest.getNoQuestMsg(player));
	}
	
	@Test
	public void onTalkToWrongNpcWithStateStarted() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(WRONG_NPC);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(Quest.getNoQuestMsg(player));
	}
	
	@Test
	public void onTalkToWalderalWithStateStarted() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(WAREHOUSE_KEEPER_WALDERAL);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(WAREHOUSE_KEEPER_WALDERAL + "-05.html");
	}
	
	@Test
	public void onTalkToHollyWithStateStarted() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(TRADER_HOLLY);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(TRADER_HOLLY + "-01.html");
	}
	
	@Test
	public void onTalkToHollyWithStateStartedAndAllItems() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(TRADER_HOLLY);
		IMPERIAL_GENEALOGY.forEach(this::stubInventoryItem);
		
		String result = quest.onTalk(npc, player);
		
		List<Integer> removedItemIds = verifyItemsRemoved(player, IMPERIAL_GENEALOGY);
		assertThat(removedItemIds).containsExactlyInAnyOrderElementsOf(IMPERIAL_GENEALOGY);
		assertThat(result).isEqualTo(TRADER_HOLLY + "-02.html");
	}
	
	@Test
	public void onTalkToDesmondWithStateStarted() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(MAGISTER_DESMOND);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(MAGISTER_DESMOND + "-01.html");
	}
	
	@Test
	public void onTalkToDesmondWithStateStartedAndAllItems() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(MAGISTER_DESMOND);
		REVELATION_OF_THE_SEALS.forEach(this::stubInventoryItem);
		
		String result = quest.onTalk(npc, player);
		
		List<Integer> removedItemIds = verifyItemsRemoved(player, REVELATION_OF_THE_SEALS);
		assertThat(removedItemIds).containsExactlyInAnyOrderElementsOf(REVELATION_OF_THE_SEALS);
		assertThat(result).isEqualTo(MAGISTER_DESMOND + "-02.html");
	}
	
	@Test
	public void onTalkToPatrinWithStateStarted() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(ANTIQUE_DEALER_PATRIN);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(ANTIQUE_DEALER_PATRIN + "-01.html");
	}
	
	@Test
	public void onTalkToPatrinWithStateStartedAndAllItems() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(ANTIQUE_DEALER_PATRIN);
		ANCIENT_EPIC.forEach(this::stubInventoryItem);
		
		String result = quest.onTalk(npc, player);
		
		List<Integer> removedItemIds = verifyItemsRemoved(player, ANCIENT_EPIC);
		assertThat(removedItemIds).containsExactlyInAnyOrderElementsOf(ANCIENT_EPIC);
		assertThat(result).isEqualTo(ANTIQUE_DEALER_PATRIN + "-02.html");
	}
	
	@Test
	public void onTalkToClaudiaWithStateStarted() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(CLAUDIA_ATHEBALDT);
		
		String result = quest.onTalk(npc, player);
		
		assertThat(result).isEqualTo(CLAUDIA_ATHEBALDT + "-01.html");
	}
	
	@Test
	public void onTalkToClaudiaWithStateStartedAndAllItems() {
		when(qs.isStarted()).thenReturn(true);
		when(npc.getId()).thenReturn(CLAUDIA_ATHEBALDT);
		REVELATION_OF_THE_SEALS.forEach(this::stubInventoryItem);
		
		String result = quest.onTalk(npc, player);
		
		List<Integer> removedItemIds = verifyItemsRemoved(player, REVELATION_OF_THE_SEALS);
		assertThat(removedItemIds).containsExactlyInAnyOrderElementsOf(REVELATION_OF_THE_SEALS);
		assertThat(result).isEqualTo(CLAUDIA_ATHEBALDT + "-02.html");
	}
	
	@Test
	public void onKillCorruptSageShouldGetQuestStateWithCorrectPlayerChance() {
		when(npc.getId()).thenReturn(CORRUPT_SAGE);
		
		quest.onKill(npc, player, false);
		
		verify(quest).getRandomPartyMemberState(player, -1, 1, npc);
	}
	
	@Test
	public void onKillErinEdiunceShouldGetQuestStateWithCorrectPlayerChance() {
		when(npc.getId()).thenReturn(ERIN_EDIUNCE);
		
		quest.onKill(npc, player, false);
		
		verify(quest).getRandomPartyMemberState(player, -1, 1, npc);
	}
	
	@Test
	public void onKillHallatesInspectorShouldGetQuestStateWithCorrectPlayerChance() {
		when(npc.getId()).thenReturn(HALLATES_INSPECTOR);
		
		quest.onKill(npc, player, false);
		
		verify(quest).getRandomPartyMemberState(player, -1, 3, npc);
	}
	
	@Test
	public void onKillPlatinumOverlordShouldGetQuestStateWithCorrectPlayerChance() {
		when(npc.getId()).thenReturn(PLATINUM_TRIBE_OVERLORD);
		
		quest.onKill(npc, player, false);
		
		verify(quest).getRandomPartyMemberState(player, -1, 3, npc);
	}
	
	@Test
	public void onKillMessengerAngelShouldGetQuestStateWithCorrectPlayerChance() {
		when(npc.getId()).thenReturn(MESSENGER_ANGEL);
		
		quest.onKill(npc, player, false);
		
		verify(quest).getRandomPartyMemberState(player, -1, 1, npc);
	}
	
	@Test
	public void onKillPlatinumPrefectShouldGetQuestStateWithCorrectPlayerChance() {
		when(npc.getId()).thenReturn(PLATINUM_GUARDIAN_PREFECT);
		
		quest.onKill(npc, player, false);
		
		verify(quest).getRandomPartyMemberState(player, -1, 1, npc);
	}
	
	private void stubInventoryItem(Integer itemId) {
		L2ItemInstance item = mock(L2ItemInstance.class);
		when(item.getId()).thenReturn(itemId);
		when(item.getCount()).thenReturn(2L);
		when(inventory.getItemByItemId(itemId)).thenReturn(item);
		when(inventory.getItemsByItemId(itemId)).thenReturn(List.of(item));
	}
	
	private List<Integer> verifyItemsRemoved(L2PcInstance player, List<Integer> itemIds) {
		ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(player, times(itemIds.size())).destroyItemByItemId(
				eq("Quest"),
				intCaptor.capture(),
				eq(1L),
				any(),
				anyBoolean());
		return intCaptor.getAllValues();
	}
}
