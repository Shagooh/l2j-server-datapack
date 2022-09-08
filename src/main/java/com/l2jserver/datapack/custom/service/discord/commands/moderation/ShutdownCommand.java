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
import com.l2jserver.gameserver.Shutdown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.Color;

import static com.l2jserver.gameserver.config.Configuration.discord;

/**
 * Shutdown Command.
 * @author Stalitsa
 * @version 2.6.2.0
 */
public class ShutdownCommand extends AbstractCommand {

	private static final String[] COMMANDS = {
		"shutdown",
		"sdn"
	};

	@Override
	public String[] getCommands() {
		return COMMANDS;
	}

	@Override
	public void executeCommand(MessageReceivedEvent event, String[] args) {
		EmbedBuilder eb = new EmbedBuilder();

		if (!canExecute(event)) {
			return;
		}
		
		if (args.length != 2) {
			eb.setColor(Color.RED);
			eb.setDescription("Wrong Arguments. Please just provide a number in seconds.");
			event.getTextChannel().sendMessageEmbeds(eb.build()).queue();
			event.getMessage().addReaction("\u274C").queue();
			return;
		}
		
		int seconds;
		try {
			seconds = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			eb.setColor(Color.RED);
			eb.setDescription("Wrong Arguments. Please just provide a number in seconds.");
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			event.getMessage().addReaction("\u274C").queue();
			return;
		}
		
		String gmName = event.getAuthor().getAsMention();
		String commandName = args[0].substring(discord().getPrefix().length()).toUpperCase();
		Shutdown.getInstance().startTelnetShutdown(event.getAuthor().getName(), seconds, false); //Using telnet method.
		eb.setColor(Color.GREEN);
		eb.setDescription("GM: {" + gmName + "} issued command. **" + commandName + "** in " + seconds + " " + "seconds!");
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
		event.getMessage().addReaction("\u2705").queue();
	}
}