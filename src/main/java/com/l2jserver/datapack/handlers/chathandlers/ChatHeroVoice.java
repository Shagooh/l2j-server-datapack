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
package com.l2jserver.datapack.handlers.chathandlers;

import static com.l2jserver.gameserver.config.Configuration.general;

import com.l2jserver.gameserver.handler.IChatHandler;
import com.l2jserver.gameserver.model.BlockList;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.PcCondOverride;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;

/**
 * Hero chat handler.
 * @author durgus
 */
public class ChatHeroVoice implements IChatHandler {
	private static final int[] COMMAND_IDS = {
		17
	};
	
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String target, String text) {
		if (activeChar.isHero() || activeChar.canOverrideCond(PcCondOverride.CHAT_CONDITIONS)) {
			if (activeChar.isChatBanned() && general().getBanChatChannels().contains(type)) {
				activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
				return;
			}
			
			if (!activeChar.getFloodProtectors().getHeroVoice().tryPerformAction("hero voice")) {
				activeChar.sendMessage("Action failed. Heroes are only able to speak in the global channel once every 10 seconds.");
				return;
			}
			
			CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
			for (L2PcInstance player : L2World.getInstance().getPlayers()) {
				if ((player != null) && !BlockList.isBlocked(player, activeChar)) {
					player.sendPacket(cs);
				}
			}
		}
	}
	
	@Override
	public int[] getChatTypeList() {
		return COMMAND_IDS;
	}
}
