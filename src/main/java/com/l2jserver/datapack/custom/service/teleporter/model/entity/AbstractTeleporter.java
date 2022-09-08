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
package com.l2jserver.datapack.custom.service.teleporter.model.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.l2jserver.datapack.custom.service.base.model.entity.CustomServiceServer;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.teleporter.model.TeleporterConfig;

/**
 * Base class for teleporter service types.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public abstract class AbstractTeleporter extends CustomServiceServer {
	private List<String> soloTeleports;
	private List<String> partyTeleports;
	private List<String> commandChannelTeleports;
	
	private List<String> soloTeleportCategories;
	private List<String> partyTeleportCategories;
	private List<String> commandChannelTeleportCategories;
	
	public transient Map<String, SoloTeleport> soloTeleportsMap;
	public transient Map<String, GroupTeleport> partyTeleportsMap;
	public transient Map<String, GroupTeleport> commandChannelTeleportsMap;
	
	private transient Map<String, SoloTeleportCategory> soloTeleportCategoriesMap;
	private transient Map<String, GroupTeleportCategory> partyTeleportCategoriesMap;
	private transient Map<String, GroupTeleportCategory> commandChannelTeleportCategoriesMap;
	
	public AbstractTeleporter(String bypassPrefix) {
		super(bypassPrefix, "teleporter");
		
		soloTeleportsMap = new LinkedHashMap<>();
		partyTeleportsMap = new LinkedHashMap<>();
		commandChannelTeleportsMap = new LinkedHashMap<>();
		
		soloTeleportCategoriesMap = new LinkedHashMap<>();
		partyTeleportCategoriesMap = new LinkedHashMap<>();
		commandChannelTeleportCategoriesMap = new LinkedHashMap<>();
	}
	
	public void afterDeserialize(TeleporterConfig config) {
		super.afterDeserialize();
		
		for (String id : soloTeleports) {
			soloTeleportsMap.put(id, config.getGlobal().getSoloTeleports().get(id));
		}
		for (String id : partyTeleports) {
			partyTeleportsMap.put(id, config.getGlobal().getGroupTeleports().get(id));
		}
		for (String id : commandChannelTeleports) {
			commandChannelTeleportsMap.put(id, config.getGlobal().getGroupTeleports().get(id));
		}
		
		for (String id : soloTeleportCategories) {
			soloTeleportCategoriesMap.put(id, config.getGlobal().getSoloTeleportCategories().get(id));
		}
		for (String id : partyTeleportCategories) {
			partyTeleportCategoriesMap.put(id, config.getGlobal().getGroupTeleportCategories().get(id));
		}
		for (String id : commandChannelTeleportCategories) {
			commandChannelTeleportCategoriesMap.put(id, config.getGlobal().getGroupTeleportCategories().get(id));
		}
		
		if (!soloTeleports.isEmpty()) {
			HTMLTemplatePlaceholder telePlaceholder = getPlaceholder().addChild("solo_teleports", null).getChild("solo_teleports");
			for (Entry<String, SoloTeleport> soloTeleport : soloTeleportsMap.entrySet()) {
				telePlaceholder.addAliasChild(String.valueOf(telePlaceholder.getChildrenSize()), soloTeleport.getValue().getPlaceholder());
			}
		}
		if (!partyTeleports.isEmpty()) {
			HTMLTemplatePlaceholder telePlaceholder = getPlaceholder().addChild("party_teleports", null).getChild("party_teleports");
			for (Entry<String, GroupTeleport> partyTeleport : partyTeleportsMap.entrySet()) {
				telePlaceholder.addAliasChild(String.valueOf(telePlaceholder.getChildrenSize()), partyTeleport.getValue().getPlaceholder());
			}
		}
		if (!commandChannelTeleports.isEmpty()) {
			HTMLTemplatePlaceholder telePlaceholder = getPlaceholder().addChild("command_channel_teleports", null).getChild("command_channel_teleports");
			for (Entry<String, GroupTeleport> commandChannelTeleport : commandChannelTeleportsMap.entrySet()) {
				telePlaceholder.addAliasChild(String.valueOf(telePlaceholder.getChildrenSize()), commandChannelTeleport.getValue().getPlaceholder());
			}
		}
		
		if (!soloTeleportCategories.isEmpty()) {
			HTMLTemplatePlaceholder catsPlaceholder = getPlaceholder().addChild("solo_categories", null).getChild("solo_categories");
			for (Entry<String, SoloTeleportCategory> soloTeleportCategories : soloTeleportCategoriesMap.entrySet()) {
				catsPlaceholder.addAliasChild(String.valueOf(catsPlaceholder.getChildrenSize()), soloTeleportCategories.getValue().getPlaceholder());
			}
		}
		if (!partyTeleportCategories.isEmpty()) {
			HTMLTemplatePlaceholder catsPlaceholder = getPlaceholder().addChild("party_categories", null).getChild("party_categories");
			for (Entry<String, GroupTeleportCategory> partyTeleportCategories : partyTeleportCategoriesMap.entrySet()) {
				catsPlaceholder.addAliasChild(String.valueOf(catsPlaceholder.getChildrenSize()), partyTeleportCategories.getValue().getPlaceholder());
			}
		}
		if (!commandChannelTeleportCategories.isEmpty()) {
			HTMLTemplatePlaceholder catsPlaceholder = getPlaceholder().addChild("command_channel_categories", null).getChild("command_channel_categories");
			for (Entry<String, GroupTeleportCategory> commandChannelTeleportCategories : commandChannelTeleportCategoriesMap.entrySet()) {
				catsPlaceholder.addAliasChild(String.valueOf(catsPlaceholder.getChildrenSize()), commandChannelTeleportCategories.getValue().getPlaceholder());
			}
		}
	}
	
	public final Map<String, SoloTeleport> getSoloTeleports() {
		return soloTeleportsMap;
	}
	
	public final Map<String, GroupTeleport> getPartyTeleports() {
		return partyTeleportsMap;
	}
	
	public final Map<String, GroupTeleport> getCommandChannelTeleports() {
		return commandChannelTeleportsMap;
	}
	
	public final Map<String, SoloTeleportCategory> getSoloTeleportCategories() {
		return soloTeleportCategoriesMap;
	}
	
	public final Map<String, GroupTeleportCategory> getPartyTeleportCategories() {
		return partyTeleportCategoriesMap;
	}
	
	public final Map<String, GroupTeleportCategory> getCommandChannelTeleportCategories() {
		return commandChannelTeleportCategoriesMap;
	}
}
