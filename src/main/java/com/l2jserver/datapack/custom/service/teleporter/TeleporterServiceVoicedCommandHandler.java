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
package com.l2jserver.datapack.custom.service.teleporter;

import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Teleporter service voiced command handler.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class TeleporterServiceVoicedCommandHandler implements IVoicedCommandHandler {
	private static final String[] COMMANDS = new String[] {
		Configuration.teleporterService().getVoicedCommand()
	};
	
	private TeleporterServiceVoicedCommandHandler() {
	}
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
		TeleporterService.getService().executeCommand(activeChar, null, params);
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList() {
		return COMMANDS;
	}
	
	static TeleporterServiceVoicedCommandHandler getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder {
		protected static final TeleporterServiceVoicedCommandHandler INSTANCE = new TeleporterServiceVoicedCommandHandler();
	}
}
