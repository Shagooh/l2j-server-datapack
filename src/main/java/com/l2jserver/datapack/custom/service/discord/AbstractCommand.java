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
package com.l2jserver.datapack.custom.service.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static com.l2jserver.gameserver.config.Configuration.discord;

/**
 * Abstract Command.
 * @author Stalitsa
 * @version 2.6.2.0
 */
public abstract class AbstractCommand extends ListenerAdapter {
	
	public abstract String[] getCommands();
	
	public abstract void executeCommand(MessageReceivedEvent event, String[] args);
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.getChannelType().equals(ChannelType.PRIVATE)) {
			return;
		}
		String[] args = event.getMessage().getContentRaw().split(" ");
		if (isCommand(args, discord().getPrefix())) {
			executeCommand(event, args);
		}
	}
	
	private boolean isCommand(String[] args, String prefix) {
		List<String> commands = new ArrayList<>();
		for (String cmd : getCommands()) {
			commands.add(prefix + cmd);
		}
		return commands.contains(args[0]);
	}

	public static boolean canExecute(MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder().setColor(Color.RED);
		Guild guild = event.getJDA().getGuildById(discord().getServerId());
		Member guildMember = guild != null ? guild.getMember(event.getMessage().getAuthor()) : null;
		Role gameMaster = guild != null ? guild.getRoleById(discord().getGameMasterId())  : null;

		// Only Server owner and members with the specified role assigned can execute the command.
		if ((guildMember == null) || (gameMaster == null) || !guildMember.isOwner() || !guildMember.getRoles().contains(gameMaster)) {
			eb.setDescription("Only Staff members can use this command!");
			event.getTextChannel().sendMessageEmbeds(eb.build()).queue();
			event.getMessage().addReaction("\u274C").queue();
			return false;
		}
		return true;
	}
}
