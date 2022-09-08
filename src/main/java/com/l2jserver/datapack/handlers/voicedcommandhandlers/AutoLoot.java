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
package com.l2jserver.datapack.handlers.voicedcommandhandlers;

import static com.l2jserver.gameserver.config.Configuration.character;
import static com.l2jserver.gameserver.config.Configuration.customs;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Auto Loot Extension voiced command.
 * @author Maneco2
 * @version 2.6.2.0
 */
public class AutoLoot implements IVoicedCommandHandler {
	private static final String[] VOICED_COMMANDS = {
		"loot",
		"autoloot",
		"itemloot",
		"herbloot"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params) {
		if (!customs().autoLootVoiceCommand()) {
			return false;
		}
		if (command.equals("loot")) {
			activeChar.sendMessage("Using Voices Methods:\n.autoloot: Loot all item(s).\n.itemloot: Loot all better item(s).\n.herbloot: Loot recovery(s) herb(s).");
			if (activeChar.isAutoLoot()) {
				activeChar.sendMessage("Auto Loot: enabled.");
			}
			if (activeChar.isAutoLootItem()) {
				activeChar.sendMessage("Auto Loot Item: enabled.");
			}
			if (activeChar.isAutoLootHerb()) {
				activeChar.sendMessage("Auto Loot Herbs: enabled.");
			}
		} else if (command.equals("autoloot")) {
			if (!character().autoLoot()) {
				if (activeChar.isAutoLoot()) {
					activeChar.setAutoLoot(false);
					activeChar.sendMessage("Auto Loot: disabled.");
					if (customs().autoLootVoiceRestore()) {
						activeChar.getVariables().remove("AutoLoot");
					}
				} else {
					activeChar.setAutoLoot(true);
					activeChar.sendMessage("Auto Loot: enabled.");
					if (customs().autoLootVoiceRestore()) {
						activeChar.getVariables().set("AutoLoot", true);
					}
				}
			} else {
				activeChar.sendMessage("Auto Loot already enable.");
			}
		} else if (command.equals("itemloot")) {
			if (activeChar.isAutoLootItem()) {
				activeChar.setAutoLootItem(false);
				activeChar.sendMessage("Auto Loot Item: disabled.");
				if (customs().autoLootVoiceRestore()) {
					activeChar.getVariables().remove("AutoLootItems");
				}
			} else {
				activeChar.setAutoLootItem(true);
				activeChar.sendMessage("Auto Loot Item: enabled.");
				if (customs().autoLootVoiceRestore()) {
					activeChar.getVariables().set("AutoLootItems", true);
				}
				
				if (activeChar.isAutoLoot()) {
					activeChar.setAutoLoot(false);
					activeChar.sendMessage("Auto Loot Item is now priority.");
					if (customs().autoLootVoiceRestore()) {
						activeChar.getVariables().remove("AutoLoot");
					}
				}
			}
		} else if (command.equals("herbloot")) {
			if (activeChar.isAutoLootHerb()) {
				activeChar.setAutoLootHerbs(false);
				activeChar.sendMessage("Auto Loot Herbs: disabled.");
				if (customs().autoLootVoiceRestore()) {
					activeChar.getVariables().remove("AutoLootHerbs");
				}
			} else {
				activeChar.setAutoLootHerbs(true);
				activeChar.sendMessage("Auto Loot Herbs: enabled.");
				if (customs().autoLootVoiceRestore()) {
					activeChar.getVariables().set("AutoLootHerbs", true);
				}
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList() {
		return VOICED_COMMANDS;
	}
}