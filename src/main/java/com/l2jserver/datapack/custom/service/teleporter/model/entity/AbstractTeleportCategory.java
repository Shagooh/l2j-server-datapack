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

import com.l2jserver.datapack.custom.service.base.model.entity.Refable;
import com.l2jserver.datapack.custom.service.teleporter.model.TeleporterConfig;

/**
 * Base class for teleporter categories.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public abstract class AbstractTeleportCategory extends Refable {
	private String name;
	
	public void afterDeserialize(TeleporterConfig config) {
		super.afterDeserialize();
		
		getPlaceholder().addChild("name", name);
	}
	
	public final String getName() {
		return name;
	}
}
