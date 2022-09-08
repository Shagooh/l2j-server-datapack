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
package com.l2jserver.datapack.custom.service.teleporter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.custom.service.base.CustomServiceScript;
import com.l2jserver.datapack.custom.service.base.util.CommandProcessor;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.teleporter.model.TeleporterConfig;
import com.l2jserver.datapack.custom.service.teleporter.model.entity.AbstractTeleporter;
import com.l2jserver.datapack.custom.service.teleporter.model.entity.GroupTeleport;
import com.l2jserver.datapack.custom.service.teleporter.model.entity.GroupTeleportCategory;
import com.l2jserver.datapack.custom.service.teleporter.model.entity.SoloTeleport;
import com.l2jserver.datapack.custom.service.teleporter.model.entity.SoloTeleportCategory;
import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.handler.BypassHandler;
import com.l2jserver.gameserver.handler.ItemHandler;
import com.l2jserver.gameserver.handler.VoicedCommandHandler;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.model.AbstractPlayerGroup;
import com.l2jserver.gameserver.model.L2CommandChannel;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.TvTEvent;
import com.l2jserver.gameserver.model.instancezone.InstanceWorld;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.taskmanager.AttackStanceTaskManager;

/**
 * Teleporter service.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class TeleporterService extends CustomServiceScript {
	private static final Logger LOG = LoggerFactory.getLogger(TeleporterService.class.getName());
	
	public static final String SCRIPT_NAME = "teleporter";
	public static final Path SCRIPT_PATH = Paths.get(SCRIPT_COLLECTION, SCRIPT_NAME);
	
	private static TeleporterConfig config;
	private static TeleporterService script;
	
	public static TeleporterConfig getConfig() {
		return config;
	}
	
	public static TeleporterService getService() {
		return script;
	}
	
	public static void main(String[] args) {
		if (!Configuration.teleporterService().enable()) {
			LOG.info("Disabled.");
			return;
		}
		
		LOG.info("Loading...");
		try {
			config = new TeleporterConfig();
			script = new TeleporterService();
		} catch (Exception e) {
			LOG.error("Failed to load!", e);
			return;
		}
		
		getConfig().registerNpcs(script);
	}
	
	public TeleporterService() {
		super(SCRIPT_NAME);
		
		BypassHandler.getInstance().registerHandler(TeleporterServiceBypassHandler.getInstance());
		
		if (Configuration.teleporterService().getVoicedEnable()) {
			VoicedCommandHandler.getInstance().registerHandler(TeleporterServiceVoicedCommandHandler.getInstance());
			ItemHandler.getInstance().registerHandler(TeleporterServiceItemHandler.getInstance());
		}
	}
	
	@Override
	public boolean unload() {
		BypassHandler.getInstance().removeHandler(TeleporterServiceBypassHandler.getInstance());
		if (Configuration.teleporterService().getVoicedEnable()) {
			VoicedCommandHandler.getInstance().removeHandler(TeleporterServiceVoicedCommandHandler.getInstance());
			ItemHandler.getInstance().removeHandler(TeleporterServiceItemHandler.getInstance());
		}
		return super.unload();
	}
	
	// ////////////////////////////////////
	// UTILITY METHODS
	// ////////////////////////////////////
	private void _showAdvancedHtml(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc, String htmlPath, Map<String, HTMLTemplatePlaceholder> placeholders) {
		showAdvancedHtml(player, teleporter, npc, htmlPath, placeholders);
	}
	
	private void _showTeleportHtml(L2PcInstance player, AbstractTeleporter teleporter, SoloTeleport teleport, L2Npc npc, String htmlPath) {
		Map<String, HTMLTemplatePlaceholder> placeholders = new HashMap<>();
		placeholders.put("teleport", teleport.getPlaceholder());
		
		_showAdvancedHtml(player, teleporter, npc, htmlPath, placeholders);
	}
	
	private boolean _takeTeleportItems(SoloTeleport teleport, L2PcInstance initiator) {
		if (!teleport.getItems().isEmpty()) {
			HashMap<Integer, Long> items = new HashMap<>();
			fillItemAmountMap(items, teleport);
			
			for (Entry<Integer, Long> item : items.entrySet()) {
				if (initiator.getInventory().getInventoryItemCount(item.getKey(), 0, true) < item.getValue()) {
					initiator.sendMessage("Not enough items!");
					return false;
				}
			}
			
			for (Entry<Integer, Long> item : items.entrySet()) {
				initiator.destroyItemByItemId("TeleporterService", item.getKey(), item.getValue(), initiator, true);
			}
		}
		
		return true;
	}
	
	private InstanceWorld _createInstance(String instanceDefinition) {
		int instanceId = InstanceManager.getInstance().createDynamicInstance(instanceDefinition);
		InstanceWorld world = new InstanceWorld();
		world.setInstanceId(instanceId);
		// TODO: world.setTemplateId(???);
		InstanceManager.getInstance().addWorld(world);
		return world;
	}
	
	// ////////////////////////////////////
	// HTML COMMANDS
	// ////////////////////////////////////
	private boolean _showAnyHtml(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc, String html) {
		_showAdvancedHtml(player, teleporter, npc, html, new HashMap<>());
		return true;
	}

	private boolean _showMainHtml(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc) {
		_showAdvancedHtml(player, teleporter, npc, "main.html", new HashMap<String, HTMLTemplatePlaceholder>());
		return true;
	}
	
	private boolean _showSoloCategoryList(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc) {
		_showAdvancedHtml(player, teleporter, npc, "solo_category_list.html", new HashMap<String, HTMLTemplatePlaceholder>());
		return true;
	}
	
	private boolean _showPartyCategoryList(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc) {
		_showAdvancedHtml(player, teleporter, npc, "p_category_list.html", new HashMap<String, HTMLTemplatePlaceholder>());
		return true;
	}
	
	private boolean _showCommandChannelCategoryList(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc) {
		_showAdvancedHtml(player, teleporter, npc, "cc_category_list.html", new HashMap<String, HTMLTemplatePlaceholder>());
		return true;
	}
	
	private boolean _showSoloListHtml(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc, String soloListIdent) {
		Map<String, HTMLTemplatePlaceholder> placeholders = new HashMap<>();
		
		if (soloListIdent != null && !soloListIdent.isEmpty()) {
			SoloTeleportCategory category = teleporter.getSoloTeleportCategories().get(soloListIdent);
			placeholders.put("category", category.getPlaceholder());
		}
		
		_showAdvancedHtml(player, teleporter, npc, "solo_teleport_list.html", placeholders);
		return true;
	}
	
	private boolean _showPartyListHtml(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc, String partyListIdent) {
		Map<String, HTMLTemplatePlaceholder> placeholders = new HashMap<>();
		
		if (partyListIdent != null && !partyListIdent.isEmpty()) {
			GroupTeleportCategory category = teleporter.getPartyTeleportCategories().get(partyListIdent);
			placeholders.put("category", category.getPlaceholder());
		}
		
		_showAdvancedHtml(player, teleporter, npc, "p_teleport_list.html", placeholders);
		return true;
	}
	
	private boolean _showCommandChannelListHtml(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc, String commandChannelListIdent) {
		Map<String, HTMLTemplatePlaceholder> placeholders = new HashMap<>();
		
		if (commandChannelListIdent != null && !commandChannelListIdent.isEmpty()) {
			GroupTeleportCategory category = teleporter.getCommandChannelTeleportCategories().get(commandChannelListIdent);
			placeholders.put("category", category.getPlaceholder());
		}
		
		_showAdvancedHtml(player, teleporter, npc, "cc_teleport_list.html", placeholders);
		return true;
	}
	
	private boolean _showSoloTeleportHtml(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc, String teleIdent, String catIdent) {
		SoloTeleport teleport = null;
		if (catIdent != null && !catIdent.isEmpty()) {
			SoloTeleportCategory category = teleporter.getSoloTeleportCategories().get(catIdent);
			if (category == null) {
				debug(player, "Invalid category ident: " + catIdent);
				return false;
			}
			
			teleport = category.getSoloTeleports().get(teleIdent);
		} else {
			teleport = teleporter.getSoloTeleports().get(teleIdent);
		}
		
		if (teleport == null) {
			debug(player, "Invalid teleport ident: " + teleIdent);
			return false;
		}
		
		_showTeleportHtml(player, teleporter, teleport, npc, "solo_teleport.html");
		return true;
	}
	
	private boolean _showPartyTeleportHtml(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc, String teleIdent, String catIdent) {
		GroupTeleport teleport = null;
		if (catIdent != null && !catIdent.isEmpty()) {
			GroupTeleportCategory category = teleporter.getPartyTeleportCategories().get(catIdent);
			if (category == null) {
				debug(player, "Invalid category ident: " + catIdent);
				return false;
			}
			
			teleport = category.getGroupTeleports().get(teleIdent);
		} else {
			teleport = teleporter.getPartyTeleports().get(teleIdent);
		}
		
		if (teleport == null) {
			debug(player, "Invalid teleport ident: " + teleIdent);
			return false;
		}
		
		_showTeleportHtml(player, teleporter, teleport, npc, "p_teleport.html");
		return true;
	}
	
	private boolean _showCommandChannelTeleportHtml(L2PcInstance player, AbstractTeleporter teleporter, L2Npc npc, String teleIdent, String catIdent) {
		GroupTeleport teleport = null;
		if (catIdent != null && !catIdent.isEmpty()) {
			GroupTeleportCategory category = teleporter.getCommandChannelTeleportCategories().get(catIdent);
			if (category == null) {
				debug(player, "Invalid category ident: " + catIdent);
				return false;
			}
			
			teleport = category.getGroupTeleports().get(teleIdent);
		} else {
			teleport = teleporter.getCommandChannelTeleports().get(teleIdent);
		}
		
		if (teleport == null) {
			debug(player, "Invalid teleport ident: " + teleIdent);
			return false;
		}
		
		_showTeleportHtml(player, teleporter, teleport, npc, "cc_teleport.html");
		return true;
	}
	
	// ////////////////////////////////////
	// TELEPORT COMMANDS
	// ////////////////////////////////////
	private void _makeTeleport(SoloTeleport teleport, L2PcInstance initiator) {
		if (!_takeTeleportItems(teleport, initiator)) {
			return;
		}
		
		InstanceWorld world = null;
		if (!teleport.getInstance().isEmpty()) {
			world = _createInstance(teleport.getInstance());
			world.addAllowed(initiator.getObjectId());
		}
		
		initiator.teleToLocation(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getHeading(), world != null ? world.getInstanceId() : initiator.getInstanceId(), teleport.getRandomOffset());
	}
	
	private void _makeGroupTeleport(GroupTeleport teleport, L2PcInstance initiator, AbstractPlayerGroup group) {
		final L2PcInstance leader = group.getLeader();
		if (leader != initiator) {
			initiator.sendMessage("You are not the leader!");
			return;
		}
		
		final int memberCount = group.getMemberCount();
		if (group.getMemberCount() < teleport.getMinMembers()) {
			group.broadcastString("Not enough members!");
			return;
		}
		
		final int leaderInstanceId = leader.getInstanceId();
		final ArrayList<L2PcInstance> membersInRange = new ArrayList<>(memberCount);
		membersInRange.add(leader);
		
		for (L2PcInstance member : group.getMembers()) {
			if ((member != leader) && ((member.getInstanceId() != leaderInstanceId) || (member.calculateDistance(leader, false, false) > teleport.getMaxDistance()))) {
				continue;
			}
			
			membersInRange.add(member);
		}
		
		if (membersInRange.size() < memberCount) {
			if (!teleport.getAllowIncomplete()) {
				group.broadcastString("Your group is not together!");
				return;
			} else if (membersInRange.size() < teleport.getMinMembers()) {
				group.broadcastString("Not enough members around!");
				return;
			}
		}
		
		if (membersInRange.size() > teleport.getMaxMembers()) {
			group.broadcastString("Too many members!");
			return;
		}
		
		if (!_takeTeleportItems(teleport, initiator)) {
			return;
		}
		
		InstanceWorld world = null;
		int instanceId = initiator.getInstanceId();
		if (!teleport.getInstance().isEmpty()) {
			world = _createInstance(teleport.getInstance());
			instanceId = world.getInstanceId();
		}
		
		for (L2PcInstance member : membersInRange) {
			if (world != null) {
				world.addAllowed(member.getObjectId());
			}
			member.teleToLocation(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getHeading(), instanceId, teleport.getRandomOffset());
		}
	}
	
	private void _teleportSolo(L2PcInstance player, AbstractTeleporter teleporter, String teleId, String catId) {
		SoloTeleport teleport = null;
		if (catId != null && !catId.isEmpty()) {
			SoloTeleportCategory category = teleporter.getSoloTeleportCategories().get(catId);
			if (category == null) {
				debug(player, "Invalid category ident: " + catId);
				return;
			}
			
			teleport = category.getSoloTeleports().get(teleId);
		} else {
			teleport = teleporter.getSoloTeleports().get(teleId);
		}
		
		if (teleport == null) {
			debug(player, "Invalid solo teleport id: " + teleId);
			return;
		}
		
		_makeTeleport(teleport, player);
	}
	
	private void _teleportParty(L2PcInstance player, AbstractTeleporter teleporter, String teleId, String catId) {
		GroupTeleport teleport = null;
		if (catId != null && !catId.isEmpty()) {
			GroupTeleportCategory category = teleporter.getPartyTeleportCategories().get(catId);
			if (category == null) {
				debug(player, "Invalid category ident: " + catId);
				return;
			}
			
			teleport = category.getGroupTeleports().get(teleId);
		} else {
			teleport = teleporter.getPartyTeleports().get(teleId);
		}
		
		if (teleport == null) {
			debug(player, "Invalid party teleport id: " + teleId);
			return;
		}
		
		final L2Party party = player.getParty();
		if (party == null) {
			player.sendMessage("You are not in a party!");
			return;
		}
		
		_makeGroupTeleport(teleport, player, party);
	}
	
	private void _teleportCommandChannel(L2PcInstance player, AbstractTeleporter teleporter, String teleId, String catId) {
		GroupTeleport teleport = null;
		if (catId != null && !catId.isEmpty()) {
			GroupTeleportCategory category = teleporter.getCommandChannelTeleportCategories().get(catId);
			if (category == null) {
				debug(player, "Invalid category ident: " + catId);
				return;
			}
			
			teleport = category.getGroupTeleports().get(teleId);
		} else {
			teleport = teleporter.getCommandChannelTeleports().get(teleId);
		}
		
		if (teleport == null) {
			debug(player, "Invalid command channel teleport id: " + teleId);
			return;
		}
		
		final L2Party party = player.getParty();
		if (party == null) {
			player.sendMessage("You are not in a party!");
			return;
		}
		
		final L2CommandChannel commandChannel = party.getCommandChannel();
		if (commandChannel == null) {
			player.sendMessage("Your party is not in a command channel!");
			return;
		}
		
		_makeGroupTeleport(teleport, player, commandChannel);
	}
	
	private void _executeTeleportCommand(L2PcInstance player, AbstractTeleporter teleporter, CommandProcessor command) {
		if (command.matchAndRemove("solo ", "s ")) {
			String[] args = command.splitRemaining(" ");
			_teleportSolo(player, teleporter, args[0], args.length > 1 ? args[1] : null);
		} else if (command.matchAndRemove("party ", "p ")) {
			String[] args = command.splitRemaining(" ");
			_teleportParty(player, teleporter, args[0], args.length > 1 ? args[1] : null);
		} else if (command.matchAndRemove("command_channel ", "cc ")) {
			String[] args = command.splitRemaining(" ");
			_teleportCommandChannel(player, teleporter, args[0], args.length > 1 ? args[1] : null);
		}
	}
	
	//
	// //////////////////////////////
	
	@Override
	protected boolean executeHtmlCommand(L2PcInstance player, L2Npc npc, CommandProcessor command) {
		AbstractTeleporter teleporter = getConfig().determineTeleporter(npc, player);
		if (teleporter == null) {
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_NOT_FOUND).addString("Teleporter"));
			return false;
		}
		
		if (command.matchAndRemove("any ", "a ")) {
			return _showAnyHtml(player, teleporter, npc, command.getRemaining());
		} else if (command.matchAndRemove("main", "m")) {
			return _showMainHtml(player, teleporter, npc);
		} else if (command.matchAndRemove("solo_category_list", "scl")) {
			return _showSoloCategoryList(player, teleporter, npc);
		} else if (command.matchAndRemove("party_category_list", "pcl")) {
			return _showPartyCategoryList(player, teleporter, npc);
		} else if (command.matchAndRemove("command_channel_category_list", "cccl")) {
			return _showCommandChannelCategoryList(player, teleporter, npc);
		} else if (command.matchAndRemove("solo_list ", "sl ")) {
			return _showSoloListHtml(player, teleporter, npc, command.getRemaining());
		} else if (command.matchAndRemove("party_list ", "pl ")) {
			return _showPartyListHtml(player, teleporter, npc, command.getRemaining());
		} else if (command.matchAndRemove("command_channel_list ", "ccl ")) {
			return _showCommandChannelListHtml(player, teleporter, npc, command.getRemaining());
		} else if (command.matchAndRemove("solo_list", "sl")) {
			return _showSoloListHtml(player, teleporter, npc, null);
		} else if (command.matchAndRemove("party_list", "pl")) {
			return _showPartyListHtml(player, teleporter, npc, null);
		} else if (command.matchAndRemove("command_channel_list", "ccl")) {
			return _showCommandChannelListHtml(player, teleporter, npc, null);
		} else if (command.matchAndRemove("solo_teleport ", "st ")) {
			String[] args = command.splitRemaining(" ");
			return _showSoloTeleportHtml(player, teleporter, npc, args[0], args.length > 1 ? args[1] : null);
		} else if (command.matchAndRemove("party_teleport ", "pt ")) {
			String[] args = command.splitRemaining(" ");
			return _showPartyTeleportHtml(player, teleporter, npc, args[0], args.length > 1 ? args[1] : null);
		} else if (command.matchAndRemove("command_channel_teleport ", "cct ")) {
			String[] args = command.splitRemaining(" ");
			return _showCommandChannelTeleportHtml(player, teleporter, npc, args[0], args.length > 1 ? args[1] : null);
		}
		
		return false;
	}
	
	@Override
	protected boolean executeActionCommand(L2PcInstance player, L2Npc npc, CommandProcessor command) {
		SystemMessage abortSysMsg = null;
		AbstractTeleporter teleporter = null;
		
		if (player.isDead() || player.isAlikeDead()) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Teleporter");
		} else if (isInsideAnyZoneOf(player, Configuration.teleporterService().getForbidInZones())) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Teleporter");
		} else if (Configuration.teleporterService().getForbidInEvents() && ((player.getEventStatus() != null) || (player.getBlockCheckerArena() != -1) || player.isOnEvent() || player.isInOlympiadMode() || TvTEvent.isPlayerParticipant(player.getObjectId()))) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Teleporter");
		} else if (Configuration.teleporterService().getForbidInDuell() && player.isInDuel()) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Teleporter");
		} else if (Configuration.teleporterService().getForbidInFight() && AttackStanceTaskManager.getInstance().hasAttackStanceTask(player)) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Teleporter");
		} else if (Configuration.teleporterService().getForbidInPvp() && (player.getPvpFlag() == 1)) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Teleporter");
		} else if (Configuration.teleporterService().getForbidForChaoticPlayers() && player.getKarma() > 0) {
			abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			abortSysMsg.addString("Teleporter");
		} else {
			teleporter = getConfig().determineTeleporter(npc, player);
			if (teleporter == null) {
				abortSysMsg = SystemMessage.getSystemMessage(SystemMessageId.S1_NOT_FOUND);
				abortSysMsg.addString("Teleporter");
			}
		}
		
		if (abortSysMsg != null) {
			player.sendPacket(abortSysMsg);
			return false;
		}
		
		if (command.matchAndRemove("teleport ", "t ")) {
			_executeTeleportCommand(player, teleporter, command);
			return false;
		}
		
		return true;
	}
	
	@Override
	protected boolean isDebugEnabled() {
		return Configuration.teleporterService().getDebug();
	}
}