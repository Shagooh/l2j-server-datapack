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

import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.teleporter.model.TeleporterConfig;

/**
 * Group teleport category.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public class GroupTeleportCategory extends AbstractTeleportCategory {
	private List<String> groupTeleports;
	
	private transient Map<String, GroupTeleport> groupTeleportsMap;
	
	public GroupTeleportCategory() {
		groupTeleportsMap = new LinkedHashMap<>();
	}
	
	@Override
	public void afterDeserialize(TeleporterConfig config) {
		super.afterDeserialize(config);
		
		for (String id : groupTeleports) {
			groupTeleportsMap.put(id, config.getGlobal().getGroupTeleports().get(id));
		}
		
		if (!groupTeleports.isEmpty()) {
			HTMLTemplatePlaceholder telePlaceholder = getPlaceholder().addChild("teleports", null).getChild("teleports");
			for (Entry<String, GroupTeleport> groupTeleport : groupTeleportsMap.entrySet()) {
				telePlaceholder.addAliasChild(String.valueOf(telePlaceholder.getChildrenSize()), groupTeleport.getValue().getPlaceholder());
			}
		}
	}
	
	public Map<String, GroupTeleport> getGroupTeleports() {
		return groupTeleportsMap;
	}
}
