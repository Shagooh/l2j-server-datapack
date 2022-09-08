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
package com.l2jserver.datapack.custom.service.base;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.datapack.custom.service.base.model.entity.CustomServiceProduct;
import com.l2jserver.datapack.custom.service.base.model.entity.CustomServiceServer;
import com.l2jserver.datapack.custom.service.base.model.entity.ItemRequirement;
import com.l2jserver.datapack.custom.service.base.util.CommandProcessor;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplateParser;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.funcs.ChildrenCountFunc;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.funcs.ExistsFunc;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.funcs.ForeachFunc;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.funcs.IfChildrenFunc;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.funcs.IfFunc;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.funcs.IncludeFunc;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * Custom service abstract script.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public abstract class CustomServiceScript extends AbstractNpcAI {
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomServiceScript.class);
	
	public static final String SCRIPT_COLLECTION = "service";
	
	private final String scriptName;
	
	private final Path scriptPath;
	
	private final Map<Integer, String> lastPlayerHtmls = new ConcurrentHashMap<>();
	
	public CustomServiceScript(String name) {
		super(name, SCRIPT_COLLECTION);
		
		Objects.requireNonNull(name);
		scriptName = name;
		scriptPath = Paths.get(SCRIPT_COLLECTION, scriptName);
	}
	
	private void setLastPlayerHtml(L2PcInstance player, String command) {
		lastPlayerHtmls.put(player.getObjectId(), command);
	}
	
	private void showLastPlayerHtml(L2PcInstance player, L2Npc npc) {
		String lastHtmlCommand = lastPlayerHtmls.get(player.getObjectId());
		if (lastHtmlCommand != null) {
			executeHtmlCommand(player, npc, new CommandProcessor(lastHtmlCommand));
		}
	}
	
	private String generateAdvancedHtml(L2PcInstance player, CustomServiceServer service, String path, Map<String, HTMLTemplatePlaceholder> placeholders) {
		final String htmlPath = "/data/" + scriptPath + "/html/" + service.getHtmlFolder() + "/" + path;
		debug(player, htmlPath);
		return HTMLTemplateParser.fromCache(htmlPath, player, placeholders, IncludeFunc.INSTANCE, IfFunc.INSTANCE, ForeachFunc.INSTANCE, ExistsFunc.INSTANCE, IfChildrenFunc.INSTANCE, ChildrenCountFunc.INSTANCE);
	}
	
	protected final boolean isInsideAnyZoneOf(L2Character character, ZoneId... zones) {
		if (zones == null) {
			return false;
		}
		
		for (ZoneId zone : zones) {
			if (character.isInsideZone(zone)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected final void fillItemAmountMap(Map<Integer, Long> items, CustomServiceProduct product) {
		for (ItemRequirement item : product.getItems()) {
			Long amount = items.get(item.getItem().getId());
			if (amount == null) {
				amount = 0L;
			}
			items.put(item.getItem().getId(), amount + item.getItemAmount());
		}
	}
	
	protected final void showAdvancedHtml(L2PcInstance player, CustomServiceServer service, L2Npc npc, String path, Map<String, HTMLTemplatePlaceholder> placeholders) {
		placeholders.put(service.getHtmlAccessorName(), service.getPlaceholder());
		String html = generateAdvancedHtml(player, service, path, placeholders);
		
		debug(html);
		
		switch (service.getDialogType()) {
			case NPC:
				player.sendPacket(new NpcHtmlMessage(npc == null ? 0 : npc.getObjectId(), html));
				break;
			case COMMUNITY:
				Util.sendCBHtml(player, html, npc == null ? 0 : npc.getObjectId());
				break;
		}
	}
	
	@Override
	public final String onFirstTalk(L2Npc npc, L2PcInstance player) {
		executeCommand(player, npc, null);
		return null;
	}
	
	public final void debug(L2PcInstance player, String message) {
		if (player.isGM() && isDebugEnabled()) {
			player.sendMessage(scriptName + ": " + message);
		}
	}
	
	public final void debug(String message) {
		if (isDebugEnabled()) {
			LOG.info("Custom Service Debug:" + message);
		}
	}
	
	public final void executeCommand(L2PcInstance player, L2Npc npc, String commandString) {
		if ((commandString == null) || commandString.isEmpty()) {
			commandString = "html main";
		}
		
		debug(player, "--------------------");
		debug(player, commandString);
		
		CommandProcessor command = new CommandProcessor(commandString);
		
		if (command.matchAndRemove("html ", "h ")) {
			String playerCommand = command.getRemaining();
			if (!executeHtmlCommand(player, npc, command)) {
				setLastPlayerHtml(player, "main");
			} else {
				setLastPlayerHtml(player, playerCommand);
			}
		} else {
			if (executeActionCommand(player, npc, command)) {
				showLastPlayerHtml(player, npc);
			}
		}
	}
	
	/**
	 * Method for Html command processing. The default html command is "main".<br>
	 * This also means, "main" must be implemented. The return value indicates if the user supplied html command should be saved as last html command.
	 * @param player
	 * @param npc
	 * @param command
	 * @return {@code true} save the html command as last html command, {@code false} don't save the html command as last html command
	 */
	protected abstract boolean executeHtmlCommand(L2PcInstance player, L2Npc npc, CommandProcessor command);
	
	/**
	 * Method for action command processing. The return value indicates if the last saved player html command should be executed after this method.
	 * @param player
	 * @param npc
	 * @param command
	 * @return {@code true} execute last saved html command of the player, {@code false} don't execute last saved html command of the player
	 */
	protected abstract boolean executeActionCommand(L2PcInstance player, L2Npc npc, CommandProcessor command);
	
	/**
	 * Method to determine if debugging is enabled.
	 * @return {@code true} debugging is enabled, {@code false} debugging is disabled
	 */
	protected abstract boolean isDebugEnabled();
}
