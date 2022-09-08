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
package com.l2jserver.datapack.custom.service.teleporter.model;

import java.util.Map;

import com.l2jserver.datapack.custom.service.teleporter.model.entity.GroupTeleport;
import com.l2jserver.datapack.custom.service.teleporter.model.entity.GroupTeleportCategory;
import com.l2jserver.datapack.custom.service.teleporter.model.entity.SoloTeleport;
import com.l2jserver.datapack.custom.service.teleporter.model.entity.SoloTeleportCategory;

/**
 * Global configuration.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public class GlobalConfig {
	private Map<String, SoloTeleport> soloTeleports;
	private Map<String, GroupTeleport> groupTeleports;
	
	private Map<String, SoloTeleportCategory> soloTeleportCategories;
	private Map<String, GroupTeleportCategory> groupTeleportCategories;
	
	public void afterDeserialize(TeleporterConfig config) {
		for (SoloTeleport teleport : soloTeleports.values()) {
			teleport.afterDeserialize(config);
		}
		
		for (GroupTeleport teleport : groupTeleports.values()) {
			teleport.afterDeserialize(config);
		}
		
		for (SoloTeleportCategory soloCat : soloTeleportCategories.values()) {
			soloCat.afterDeserialize(config);
		}
		
		for (GroupTeleportCategory groupCat : groupTeleportCategories.values()) {
			groupCat.afterDeserialize(config);
		}
	}
	
	public Map<String, SoloTeleport> getSoloTeleports() {
		return soloTeleports;
	}
	
	public Map<String, GroupTeleport> getGroupTeleports() {
		return groupTeleports;
	}
	
	public Map<String, SoloTeleportCategory> getSoloTeleportCategories() {
		return soloTeleportCategories;
	}
	
	public Map<String, GroupTeleportCategory> getGroupTeleportCategories() {
		return groupTeleportCategories;
	}
}
