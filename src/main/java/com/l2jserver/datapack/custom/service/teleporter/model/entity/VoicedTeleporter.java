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
import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.items.L2Item;

/**
 * Voiced teleporter.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class VoicedTeleporter extends AbstractTeleporter {
	public VoicedTeleporter() {
		super("voice ." + Configuration.teleporterService().getVoicedCommand());
	}
	
	@Override
	public void afterDeserialize(TeleporterConfig config) {
		super.afterDeserialize(config);
	}
	
	public L2Item getRequiredItem() {
		return ItemTable.getInstance().getTemplate(Configuration.teleporterService().getVoicedRequiredItem());
	}
	
	@Override
	public String getName() {
		return Configuration.teleporterService().getVoicedName();
	}
}
