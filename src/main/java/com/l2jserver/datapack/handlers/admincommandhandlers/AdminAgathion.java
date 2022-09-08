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
package com.l2jserver.datapack.handlers.admincommandhandlers;

import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class AdminAgathion implements IAdminCommandHandler {
	private static final String[] ADMIN_COMMANDS = {
		"admin_agathion"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar) {
		if (command.startsWith("admin_agathion")) {
			try {
				String val = command.substring(14);
				activeChar.setAgathionId(Integer.valueOf(val.trim()));
				activeChar.broadcastUserInfo();
				activeChar.sendMessage("Set agathion visually to " + val);
			} catch (Exception e) {
				activeChar.sendMessage("Usage: //agathion <npcId>");
				return false;
			}
		}

		return true;
	}
	
	@Override
	public String[] getAdminCommandList() {
		return ADMIN_COMMANDS;
	}
}
