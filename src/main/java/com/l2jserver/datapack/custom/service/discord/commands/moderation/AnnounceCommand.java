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
package com.l2jserver.datapack.custom.service.discord.commands.moderation;

import com.l2jserver.datapack.custom.service.discord.AbstractCommand;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.util.Broadcast;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.Color;

/**
 * Announce Command.
 * @author Stalitsa
 * @version 2.6.2.0
 */
public class AnnounceCommand extends AbstractCommand {

	private static final String[] COMMANDS = {
		"announce",
		"ann"
	};

	@Override
	public String[] getCommands() {
		return COMMANDS;
	}

	@Override
	public void executeCommand(MessageReceivedEvent event, String[] args) {
		EmbedBuilder eb = new EmbedBuilder().setColor(Color.RED);
		String announcement = event.getMessage().getContentRaw().replace(args[0] +" "+ args[1], "");

		if (!canExecute(event)) {
			return;
		}

		if (args.length <= 2) {
			eb.setDescription("Wrong Arguments. Please type the message to be sent.");
			event.getTextChannel().sendMessageEmbeds(eb.build()).queue();
			event.getMessage().addReaction("\u274C").queue();
			return;
		}

		if (args[1].equals("normal")) {
			Broadcast.toAllOnlinePlayers(announcement);
		}
		
		if (args[1].equals("critical")) {
			Broadcast.toAllOnlinePlayers(announcement, true);
		}
		
		if (args[1].equals("screen")) {
			ExShowScreenMessage screenMessage = new ExShowScreenMessage(announcement, 20000);
			Broadcast.toAllOnlinePlayers(screenMessage);
		}
		eb.setDescription("**In game Announcement have been sent**.");
		event.getMessage().addReaction("\u2705").queue();
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
}
