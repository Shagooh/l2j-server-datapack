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

import com.l2jserver.datapack.custom.service.teleporter.model.TeleporterConfig;

/**
 * Group teleport.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public class GroupTeleport extends SoloTeleport {
	private int minMembers;
	private int maxMembers;
	private int maxDistance;
	private boolean allowIncomplete;
	
	public GroupTeleport() {
	}
	
	@Override
	public void afterDeserialize(TeleporterConfig config) {
		super.afterDeserialize(config);
		
		getPlaceholder().addChild("min_members", String.valueOf(minMembers)).addChild("max_members", String.valueOf(maxMembers)).addChild("max_distance", String.valueOf(maxDistance));
	}
	
	public int getMinMembers() {
		return minMembers;
	}
	
	public int getMaxMembers() {
		return maxMembers;
	}
	
	public int getMaxDistance() {
		return maxDistance;
	}
	
	public boolean getAllowIncomplete() {
		return allowIncomplete;
	}
}
