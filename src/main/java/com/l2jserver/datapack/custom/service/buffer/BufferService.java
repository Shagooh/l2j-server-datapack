/*
 * Copyright © 2004-2022 L2J DataPack
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
package com.l2jserver.datapack.custom.service.buffer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.custom.service.base.CustomServiceScript;
import com.l2jserver.datapack.custom.service.base.util.CommandProcessor;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.buffer.model.entity.AbstractBuffer;
import com.l2jserver.datapack.custom.service.buffer.model.entity.BuffCategory;
import com.l2jserver.datapack.custom.service.buffer.model.entity.BuffSkill;
import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.handler.BypassHandler;
import com.l2jserver.gameserver.handler.ItemHandler;
import com.l2jserver.gameserver.handler.VoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Playable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.TvTEvent;
import com.l2jserver.gameserver.model.skills.BuffInfo;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.taskmanager.AttackStanceTaskManager;

/**
 * Buffer Service.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class BufferService extends CustomServiceScript {
	
	private static final Logger LOG = LoggerFactory.getLogger(BufferService.class);
	
	public static final String SCRIPT_NAME = "buffer";
	
	public static final Path SCRIPT_PATH = Paths.get(SCRIPT_COLLECTION, SCRIPT_NAME);
	
	public static void main(String[] args) {
		if (!Configuration.bufferService().enable()) {
			LOG.info("Disabled.");
			return;
		}
		
		LOG.info("Loading...");
		
		BufferServiceRepository.getInstance().getConfig().registerNpcs(getInstance());
	}
	
	private static final Map<Integer, Long> LAST_PLAYABLES_HEAL_TIME = new ConcurrentHashMap<>();
	
	private static final Map<Integer, String> ACTIVE_PLAYER_BUFFLISTS = new ConcurrentHashMap<>();
	private static final Map<Integer, String> ACTIVE_PLAYER_CATEGORIES = new ConcurrentHashMap<>();
	private static final Map<Integer, String> ACTIVE_PLAYER_TARGETS = new ConcurrentHashMap<>();
	
	BufferService() {
		super(SCRIPT_NAME);
		
		BypassHandler.getInstance().registerHandler(BufferServiceBypassHandler.getInstance());
		
		if (Configuration.bufferService().getVoicedEnable()) {
			VoicedCommandHandler.getInstance().registerHandler(BufferServiceVoicedCommandHandler.getInstance());
			ItemHandler.getInstance().registerHandler(BufferServiceItemHandler.getInstance());
		}
	}
	
	@Override
	public boolean unload() {
		BypassHandler.getInstance().removeHandler(BufferServiceBypassHandler.getInstance());
		if (Configuration.bufferService().getVoicedEnable()) {
			VoicedCommandHandler.getInstance().removeHandler(BufferServiceVoicedCommandHandler.getInstance());
			ItemHandler.getInstance().removeHandler(BufferServiceItemHandler.getInstance());
		}
		return super.unload();
	}
	
	private void castBuff(L2Playable playable, BuffSkill buff) {
		buff.getSkill().applyEffects(playable, playable);
	}
	
	private void showAdvancedHtml(L2PcInstance player, AbstractBuffer buffer, L2Npc npc, String htmlPath, Map<String, HTMLTemplatePlaceholder> placeholders) {
		HTMLTemplatePlaceholder ulistsPlaceholder = BufferServiceRepository.getInstance().getPlayersUListsPlaceholder(player.getObjectId());
		if (ulistsPlaceholder != null) {
			placeholders.put(ulistsPlaceholder.getName(), ulistsPlaceholder);
		}
		
		String activeUniqueName = ACTIVE_PLAYER_BUFFLISTS.get(player.getObjectId());
		if (activeUniqueName != null) {
			HTMLTemplatePlaceholder ulistPlaceholder = BufferServiceRepository.getInstance().getPlayersUListPlaceholder(player.getObjectId(), activeUniqueName);
			if (ulistPlaceholder != null) {
				placeholders.put("active_unique", ulistPlaceholder);
			}
		}

		String activeCategoryId = ACTIVE_PLAYER_CATEGORIES.get(player.getObjectId());
		if (activeCategoryId != null) {
			HTMLTemplatePlaceholder catPlaceholder = BufferServiceRepository.getInstance().getBuffCategoryPlaceholder(activeCategoryId);
			if (catPlaceholder != null) {
				placeholders.put("active_category", catPlaceholder);
			}
		}
		
		String activeTarget = ACTIVE_PLAYER_TARGETS.get(player.getObjectId());
		if (activeTarget != null) {
			placeholders.put("active_target", new HTMLTemplatePlaceholder("active_taget", activeTarget));
		}
		
		HTMLTemplatePlaceholder playerPlaceholder = new HTMLTemplatePlaceholder("player", null);
		playerPlaceholder.addChild("name", player.getName());
		playerPlaceholder.addChild("unique_max_buffs", String.valueOf(player.getStat().getMaxBuffCount()));
		playerPlaceholder.addChild("unique_max_songs_dances", String.valueOf(Configuration.character().getMaxDanceAmount()));
		
		placeholders.put(playerPlaceholder.getName(), playerPlaceholder);
		
		super.showAdvancedHtml(player, buffer, npc, htmlPath, placeholders);
	}
	
	private boolean htmlShowAny(L2PcInstance player, AbstractBuffer buffer, L2Npc npc, String html) {
		showAdvancedHtml(player, buffer, npc, html, new HashMap<>());
		return true;
	}
	
	private boolean htmlShowMain(L2PcInstance player, AbstractBuffer buffer, L2Npc npc) {
		showAdvancedHtml(player, buffer, npc, "main.html", new HashMap<>());
		return true;
	}
	
	private boolean htmlShowCategory(L2PcInstance player, AbstractBuffer buffer, L2Npc npc, String categoryIdent) {
		BuffCategory buffCat = buffer.getBuffCats().get(categoryIdent);
		if (buffCat == null) {
			debug(player, "Invalid buff category: " + categoryIdent);
			return false;
		}
		
		HashMap<String, HTMLTemplatePlaceholder> placeholders = new HashMap<>();
		
		placeholders.put("category", buffCat.getPlaceholder());
		
		showAdvancedHtml(player, buffer, npc, "category.html", placeholders);
		return true;
	}
	
	private boolean htmlShowBuff(L2PcInstance player, AbstractBuffer buffer, L2Npc npc, String categoryIdent, String buffIdent) {
		BuffCategory buffCat = buffer.getBuffCats().get(categoryIdent);
		if (buffCat == null) {
			debug(player, "Invalid buff category: " + categoryIdent);
			return false;
		}
		BuffSkill buff = buffCat.getBuff(buffIdent);
		if (buff == null) {
			debug(player, "Invalid buff skill: " + buffIdent);
			return false;
		}
		
		HashMap<String, HTMLTemplatePlaceholder> placeholders = new HashMap<>();
		
		placeholders.put("category", buffCat.getPlaceholder());
		placeholders.put("buff", buff.getPlaceholder());
		
		showAdvancedHtml(player, buffer, npc, "buff.html", placeholders);
		return true;
	}
	
	private boolean htmlShowPreset(L2PcInstance player, AbstractBuffer buffer, L2Npc npc, String presetBufflistIdent) {
		BuffCategory presetBufflist = buffer.getPresetBuffCats().get(presetBufflistIdent);
		if (presetBufflist == null) {
			debug(player, "Invalid preset buff category: " + presetBufflistIdent);
			return false;
		}
		
		var placeholders = new HashMap<String, HTMLTemplatePlaceholder>();
		
		placeholders.put("preset", presetBufflist.getPlaceholder());
		
		showAdvancedHtml(player, buffer, npc, "preset.html", placeholders);
		return true;
	}
	
	private boolean htmlShowUnique(L2PcInstance player, AbstractBuffer buffer, L2Npc npc, String uniqueName) {
		HTMLTemplatePlaceholder uniquePlaceholder = BufferServiceRepository.getInstance().getPlayersUListPlaceholder(player.getObjectId(), uniqueName);
		if (uniquePlaceholder == null) {
			// redirect to main html if uniqueName is not valid, will most likely happen when the player deletes a unique bufflist he is currently viewing
			executeHtmlCommand(player, npc, new CommandProcessor("main"));
			return false;
		}
		
		HashMap<String, HTMLTemplatePlaceholder> placeholders = new HashMap<>();
		
		placeholders.put(uniquePlaceholder.getName(), uniquePlaceholder);
		
		showAdvancedHtml(player, buffer, npc, "unique.html", placeholders);
		return true;
	}
	
	private void targetBuffBuff(L2PcInstance player, L2Playable target, AbstractBuffer buffer, String categoryIdent, String buffIdent) {
		BuffCategory bCat = buffer.getBuffCats().get(categoryIdent);
		if (bCat == null) {
			debug(player, "Invalid buff category: " + categoryIdent);
			return;
		}
		BuffSkill buff = bCat.getBuff(buffIdent);
		if (buff == null) {
			debug(player, "Invalid buff skill: " + buffIdent);
			return;
		}
		
		if (!buff.getItems().isEmpty()) {
			HashMap<Integer, Long> items = new HashMap<>();
			fillItemAmountMap(items, buff);
			
			for (Entry<Integer, Long> item : items.entrySet()) {
				if (player.getInventory().getInventoryItemCount(item.getKey(), 0, true) < item.getValue()) {
					player.sendMessage("Not enough items!");
					return;
				}
			}
			
			for (Entry<Integer, Long> item : items.entrySet()) {
				player.destroyItemByItemId("BufferService", item.getKey(), item.getValue(), player, true);
			}
		}
		
		castBuff(target, buff);
	}
	
	private void targetBuffUnique(L2PcInstance player, L2Playable target, AbstractBuffer buffer, String uniqueName) {
		List<BuffSkill> buffs = BufferServiceRepository.getInstance().getUniqueBufflist(player.getObjectId(), uniqueName);
		
		if (buffs != null) {
			HashMap<Integer, Long> items = null;
			for (BuffSkill buff : buffs) {
				if (!buff.getItems().isEmpty()) {
					if (items == null) {
						items = new HashMap<>();
					}
					fillItemAmountMap(items, buff);
				}
			}
			
			if (items != null) {
				for (var item : items.entrySet()) {
					if (player.getInventory().getInventoryItemCount(item.getKey(), 0, true) < item.getValue()) {
						player.sendMessage("Not enough items!");
						return;
					}
				}
				
				for (var item : items.entrySet()) {
					player.destroyItemByItemId("BufferService", item.getKey(), item.getValue(), player, true);
				}
			}
			
			for (BuffSkill buff : buffs) {
				castBuff(target, buff);
			}
		}
	}
	
	private void targetBuffPreset(L2PcInstance player, L2Playable target, AbstractBuffer buffer, String presetBufflistIdent) {
		BuffCategory presetBufflist = buffer.getPresetBuffCats().get(presetBufflistIdent);
		if (presetBufflist == null) {
			debug(player, "Invalid preset buff category: " + presetBufflistIdent);
			return;
		}
		
		Collection<BuffSkill> buffs = presetBufflist.getBuffs().values();
		
		if (buffs != null) {
			HashMap<Integer, Long> items = null;
			for (BuffSkill buff : buffs) {
				if (!buff.getItems().isEmpty()) {
					if (items == null) {
						items = new HashMap<>();
					}
					fillItemAmountMap(items, buff);
				}
			}
			
			if (items != null) {
				for (Entry<Integer, Long> item : items.entrySet()) {
					if (player.getInventory().getInventoryItemCount(item.getKey(), 0, true) < item.getValue()) {
						player.sendMessage("Not enough items!");
						return;
					}
				}
				
				for (Entry<Integer, Long> item : items.entrySet()) {
					player.destroyItemByItemId("BufferService", item.getKey(), item.getValue(), player, true);
				}
			}
			
			for (BuffSkill buff : buffs) {
				castBuff(target, buff);
			}
		}
	}
	
	private void targetHeal(L2PcInstance player, L2Playable target, AbstractBuffer buffer) {
		if (!buffer.getCanHeal()) {
			debug(player, "This buffer can not heal!");
			return;
		}
		
		// prevent heal spamming, process cooldown on heal target
		Long lastPlayableHealTime = LAST_PLAYABLES_HEAL_TIME.get(target.getObjectId());
		if (lastPlayableHealTime != null) {
			Long elapsedTime = System.currentTimeMillis() - lastPlayableHealTime;
			Long healCooldown = Configuration.bufferService().getHealCooldown().longValue();
			if (elapsedTime < healCooldown) {
				long remainingTime = healCooldown - elapsedTime;
				if (target == player) {
					player.sendMessage("You can heal yourself again in " + (remainingTime / 1000) + " seconds.");
				} else {
					player.sendMessage("You can heal your pet again in " + (remainingTime / 1000) + " seconds.");
				}
				return;
			}
		}
		
		LAST_PLAYABLES_HEAL_TIME.put(target.getObjectId(), System.currentTimeMillis());
		
		if (player == target) {
			player.setCurrentCp(player.getMaxCp());
		}
		target.setCurrentHp(target.getMaxHp());
		target.setCurrentMp(target.getMaxMp());
		target.broadcastStatusUpdate();
	}
	
	private void targetCancel(L2PcInstance player, L2Playable target, AbstractBuffer buffer) {
		if (!buffer.getCanCancel()) {
			debug(player, "This buffer can not cancel!");
			return;
		}
		target.stopAllEffectsExceptThoseThatLastThroughDeath();
	}
	
	private void targetSelect(L2PcInstance player, L2Playable target) {
		ACTIVE_PLAYER_TARGETS.put(player.getObjectId(), target.isSummon() ? "summon" : "player");
	}
	
	private void targetDeselect(L2PcInstance player) {
		ACTIVE_PLAYER_TARGETS.remove(player.getObjectId());
	}
	
	private void executeTargetCommand(L2PcInstance player, AbstractBuffer buffer, CommandProcessor command) {
		if (command.matchAndRemove("deselect", "des")) {
			targetDeselect(player);
			return;
		}
		
		// first determine the target
		L2Playable target;
		if (command.matchAndRemove("player ", "p ")) {
			target = player;
		} else if (command.matchAndRemove("summon ", "s ")) {
			target = player.getSummon();
			if (target == null) {
				debug(player, "No summon available!");
				return;
			}
		} else {
			debug(player, "Invalid target command target!");
			return;
		}
		
		// run the chosen action on the target
		if (command.matchAndRemove("buff ", "b ")) {
			String[] argsSplit = command.splitRemaining(" ");
			if (argsSplit.length != 2) {
				debug(player, "Missing arguments!");
				return;
			}
			targetBuffBuff(player, target, buffer, argsSplit[0], argsSplit[1]);
		} else if (command.matchAndRemove("unique ", "u ")) {
			targetBuffUnique(player, target, buffer, command.getRemaining());
		} else if (command.matchAndRemove("preset ", "p ")) {
			targetBuffPreset(player, target, buffer, command.getRemaining());
		} else if (command.matchAndRemove("heal", "h")) {
			targetHeal(player, target, buffer);
		} else if (command.matchAndRemove("cancel", "c")) {
			targetCancel(player, target, buffer);
		} else if (command.matchAndRemove("select", "s")) {
			targetSelect(player, target);
		}
	}
	
	private void categorySelect(L2PcInstance player, AbstractBuffer buffer, String categoryIdent) {
		BuffCategory cat = buffer.getBuffCats().get(categoryIdent);
		if (cat == null) {
			return;
		}
		
		ACTIVE_PLAYER_CATEGORIES.put(player.getObjectId(), categoryIdent);
	}
	
	private void categoryDeselect(L2PcInstance player) {
		ACTIVE_PLAYER_CATEGORIES.remove(player.getObjectId());
	}
	
	private void executeCategoryCommand(L2PcInstance player, AbstractBuffer buffer, CommandProcessor command) {
		if (command.matchAndRemove("select ", "s ")) {
			categorySelect(player, buffer, command.getRemaining());
		} else if (command.matchAndRemove("deselect ", "des ")) {
			categoryDeselect(player);
		}
	}
	
	private boolean uniqueCreate(L2PcInstance player, String uniqueName) {
		if (!BufferServiceRepository.getInstance().canHaveMoreBufflists(player)) {
			player.sendMessage("Maximum number of unique bufflists reached!");
			return false;
		}
		
		// only allow alpha numeric names because we use this name on the htmls
		if (!uniqueName.matches("[A-Za-z0-9]+")) {
			return false;
		}
		
		return BufferServiceRepository.getInstance().createUniqueBufflist(player.getObjectId(), uniqueName);
	}
	
	private void uniqueDelete(L2PcInstance player, String uniqueName) {
		BufferServiceRepository.getInstance().deleteUniqueBufflist(player.getObjectId(), uniqueName);
		// also remove from active buff list when it's the deleted
		String activeUniqueName = ACTIVE_PLAYER_BUFFLISTS.get(player.getObjectId());
		if ((activeUniqueName != null) && activeUniqueName.equals(uniqueName)) {
			ACTIVE_PLAYER_BUFFLISTS.remove(player.getObjectId());
		}
	}
	
	private void uniqueAdd(L2PcInstance player, AbstractBuffer buffer, String uniqueName, String categoryIdent, String buffIdent) {
		BuffCategory bCat = buffer.getBuffCats().get(categoryIdent);
		if (bCat == null) {
			return;
		}
		BuffSkill buff = bCat.getBuff(buffIdent);
		if (buff == null) {
			return;
		}
		
		BufferServiceRepository.getInstance().addToUniqueBufflist(player, uniqueName, buff);
	}
	
	private void uniqueRemove(L2PcInstance player, String uniqueName, String buffIdent) {
		BuffSkill buff = BufferServiceRepository.getInstance().getConfig().getGlobal().getBuff(buffIdent);
		if (buff == null) {
			return;
		}
		
		BufferServiceRepository.getInstance().removeFromUniqueBufflist(player.getObjectId(), uniqueName, buff);
	}
	
	private void uniqueSelect(L2PcInstance player, String uniqueName) {
		if (BufferServiceRepository.getInstance().hasUniqueBufflist(player.getObjectId(), uniqueName)) {
			ACTIVE_PLAYER_BUFFLISTS.put(player.getObjectId(), uniqueName);
		}
	}
	
	private void uniqueDeselect(L2PcInstance player) {
		ACTIVE_PLAYER_BUFFLISTS.remove(player.getObjectId());
	}
	
	private void executeUniqueCommand(L2PcInstance player, AbstractBuffer buffer, CommandProcessor command) {
		if (command.matchAndRemove("create ", "c ")) {
			uniqueCreate(player, command.getRemaining());
		} else if (command.matchAndRemove("create_from_effects ", "cfe ")) {
			String uniqueName = command.getRemaining();
			if (!uniqueCreate(player, uniqueName)) {
				return;
			}
			
			final List<BuffInfo> effects = player.getEffectList().getEffects();
			for (final BuffInfo effect : effects) {
				for (Entry<String, BuffCategory> buffCatEntry : buffer.getBuffCats().entrySet()) {
					boolean added = false;
					
					for (Entry<String, BuffSkill> buffEntry : buffCatEntry.getValue().getBuffs().entrySet()) {
						final BuffSkill buff = buffEntry.getValue();
						
						if (buff.getSkill().getId() == effect.getSkill().getId()) {
							uniqueAdd(player, buffer, uniqueName, buffCatEntry.getKey(), buff.getId());
							added = true;
							break;
						}
					}
					
					if (added) {
						break;
					}
				}
			}
		} else if (command.matchAndRemove("delete ", "del ")) {
			uniqueDelete(player, command.getRemaining());
		} else if (command.matchAndRemove("add ", "a ")) {
			String[] argsSplit = command.splitRemaining(" ");
			if (argsSplit.length != 3) {
				debug(player, "Missing arguments!");
				return;
			}
			uniqueAdd(player, buffer, argsSplit[0], argsSplit[1], argsSplit[2]);
		} else if (command.matchAndRemove("remove ", "r ")) {
			String[] argsSplit = command.splitRemaining(" ");
			if (argsSplit.length != 2) {
				debug(player, "Missing arguments!");
				return;
			}
			uniqueRemove(player, argsSplit[0], argsSplit[1]);
		} else if (command.matchAndRemove("select ", "s ")) {
			uniqueSelect(player, command.getRemaining());
		} else if (command.matchAndRemove("deselect", "des")) {
			uniqueDeselect(player);
		}
	}
	
	@Override
	public boolean executeHtmlCommand(L2PcInstance player, L2Npc npc, CommandProcessor command) {
		AbstractBuffer buffer = BufferServiceRepository.getInstance().getConfig().determineBuffer(npc, player);
		if (buffer == null) {
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_NOT_FOUND).addString("Buffer"));
			return false;
		}
		
		if (command.matchAndRemove("any ", "a ")) {
			return htmlShowAny(player, buffer, npc, command.getRemaining());
		} else if (command.matchAndRemove("main", "m")) {
			return htmlShowMain(player, buffer, npc);
		} else if (command.matchAndRemove("category ", "c ")) {
			return htmlShowCategory(player, buffer, npc, command.getRemaining());
		} else if (command.matchAndRemove("preset ", "p ")) {
			return htmlShowPreset(player, buffer, npc, command.getRemaining());
		} else if (command.matchAndRemove("buff ", "b ")) {
			String[] argsSplit = command.splitRemaining(" ");
			if (argsSplit.length != 2) {
				debug(player, "Missing arguments!");
				return false;
			}
			return htmlShowBuff(player, buffer, npc, argsSplit[0], argsSplit[1]);
		} else if (command.matchAndRemove("unique ", "u ")) {
			return htmlShowUnique(player, buffer, npc, command.getRemaining());
		}
		
		return false;
	}
	
	@Override
	public boolean executeActionCommand(L2PcInstance player, L2Npc npc, CommandProcessor command) {
		SystemMessage abortSysMsg = null;
		AbstractBuffer buffer = null;
		
		if (player.isDead()) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Buffer");
		} else if (isInsideAnyZoneOf(player, Configuration.bufferService().getForbidInZones())) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Buffer");
		} else if (Configuration.bufferService().getForbidInEvents() && ((player.getEventStatus() != null) || (player.getBlockCheckerArena() != -1) || player.isOnEvent() || player.isInOlympiadMode() || TvTEvent.isPlayerParticipant(player.getObjectId()))) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Buffer");
		} else if (Configuration.bufferService().getForbidInDuell() && player.isInDuel()) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Buffer");
		} else if (Configuration.bufferService().getForbidInFight() && AttackStanceTaskManager.getInstance().hasAttackStanceTask(player)) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Buffer");
		} else if (Configuration.bufferService().getForbidInPvp() && (player.getPvpFlag() == 1)) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Buffer");
		} else if (Configuration.bufferService().getForbidForChaoticPlayers() && player.getKarma() > 0) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Buffer");
		} else {
			buffer = BufferServiceRepository.getInstance().getConfig().determineBuffer(npc, player);
			if (buffer == null) {
				abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_NOT_FOUND);
				abortSysMsg.addString("Buffer");
			}
		}
		
		if (abortSysMsg != null) {
			player.sendPacket(abortSysMsg);
			return false;
		}
		
		if (command.matchAndRemove("target ", "t ")) {
			executeTargetCommand(player, buffer, command);
		} else if (command.matchAndRemove("category ", "c ")) {
			executeCategoryCommand(player, buffer, command);
		} else if (command.matchAndRemove("unique ", "u ")) {
			executeUniqueCommand(player, buffer, command);
		}
		
		return true;
	}
	
	@Override
	protected boolean isDebugEnabled() {
		return Configuration.bufferService().getDebug();
	}
	
	public static BufferService getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder {
		protected static final BufferService INSTANCE = new BufferService();
	}
}
