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

import com.l2jserver.datapack.custom.service.base.model.entity.IRefable;
import com.l2jserver.datapack.custom.service.teleporter.TeleporterServiceBypassHandler;
import com.l2jserver.datapack.custom.service.teleporter.model.TeleporterConfig;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;

/**
 * Npc teleporter.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class NpcTeleporter extends AbstractTeleporter implements IRefable<Integer> {
	private int npcId;
	private boolean directFirstTalk;
	
	public NpcTeleporter() {
		super(TeleporterServiceBypassHandler.BYPASS);
	}
	
	@Override
	public void afterDeserialize(TeleporterConfig config) {
		super.afterDeserialize(config);
		
		getPlaceholder().addChild("ident", String.valueOf(npcId));
	}
	
	public L2NpcTemplate getNpc() {
		return NpcData.getInstance().getTemplate(npcId);
	}
	
	public int getNpcId() {
		return npcId;
	}
	
	public boolean getDirectFirstTalk() {
		return directFirstTalk;
	}
	
	@Override
	public final String getName() {
		return getNpc().getName();
	}
	
	@Override
	public final Integer getId() {
		return npcId;
	}
}
