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

import com.l2jserver.datapack.custom.service.discord.commands.OnlineCommand;
import com.l2jserver.datapack.custom.service.discord.commands.TopCommand;
import com.l2jserver.datapack.custom.service.discord.commands.moderation.AbortCommand;
import com.l2jserver.datapack.custom.service.discord.commands.moderation.AnnounceCommand;
import com.l2jserver.datapack.custom.service.discord.commands.moderation.RestartCommand;
import com.l2jserver.datapack.custom.service.discord.commands.moderation.ShutdownCommand;
import com.l2jserver.datapack.custom.service.discord.listeners.ChatListener;
import com.l2jserver.datapack.custom.service.discord.listeners.BasicListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.Color;

import static com.l2jserver.gameserver.config.Configuration.discord;

/**
 * Main class of Discord Bot.
 * @author Stalitsa
 * @version 2.6.2.0
 */
public class DiscordBot {
	
	private static final Logger LOG = LoggerFactory.getLogger(DiscordBot.class);
	
	private static JDA jda;
	
	private static final Object[] COMMANDS = {
		new AbortCommand(),
		new AnnounceCommand(),
		new OnlineCommand(),
		new TopCommand(),
		new RestartCommand(),
		new ShutdownCommand(),
		new ChatListener(),
		new BasicListener(),
	};
	
	public static void main(String[] args) {
		if (!discord().enableBot()) {
			LOG.info("Discord Bot is Disabled.");
			return;
		}
		try {
			jda = JDABuilder.createDefault(discord().getBotToken()) //
				.setAutoReconnect(true) //
				.addEventListeners(COMMANDS) //
				.enableIntents(GatewayIntent.GUILD_MEMBERS) //
				.enableIntents(GatewayIntent.GUILD_MESSAGES) //
				.setMemberCachePolicy(MemberCachePolicy.ALL) //
				.setChunkingFilter(ChunkingFilter.ALL) //
				// Login to Discord now that we are all setup.
				.build() //
				.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
			jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.listening(": -- L2J"));
			LOG.info("Discord Bot Started.");
		} catch (InterruptedException | LoginException ex) {
			LOG.error("Failed to start the Discord Bot!", ex);
		}
	}
	
	/**
	 * Send a message in the specified channel
	 * @param msg the message to send. This will be shown as a description of an embed.
	 * @param channelId the channel to send the msg. // planned to be used by console logs
	 */
	public void sendMessageTo(String msg, String channelId) {
		sendMessageTo(new EmbedBuilder().setColor(Color.GREEN).setDescription(msg), channelId);
	}
	
	/**
	 * Send a message in the specified channel
	 * @param ed the embed message to send. (The embed build(); is done here.)
	 * @param channelId the channel to send the embed. // planned to be used by console logs
	 */
	public static void sendMessageTo(EmbedBuilder ed, String channelId) {
		MessageChannel channel = jda.getTextChannelById(channelId);
		if (channel != null) {
			channel.sendMessageEmbeds(ed.build()).queue(); // this actually sends the information to discord.
		}
	}
}
