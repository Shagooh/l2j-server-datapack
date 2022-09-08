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
package com.l2jserver.datapack.custom.service.discord.commands;

import static com.l2jserver.gameserver.config.Configuration.discord;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.datapack.custom.service.discord.AbstractCommand;
import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.entity.Castle;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Top Command.
 * @author Stalitsa
 * @version 2.6.3.0
 */
public class TopCommand extends AbstractCommand {
	private static final String NoClansMsg = "This server does not have any clans yet";
	private static final String NoPlayersMsg = "This server does not have any players yet";
	private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#,###");

	/**
	 * When the choice is players these are the parameters that the user can specify to get the info for.
	 */
	private final String[] params = {
		"pvp",
		"pk",
		"adena",
		"online"
	};

	/**
	 * The choice of information that the user wants to see.
	 */
	private final String[] choice = {
		"players",
		"clans"
	};

	private static final String[] COMMANDS = {
		"top",
		"tp"
	};

	@Override
	public String[] getCommands() {
		return COMMANDS;
	}

	@Override
	public void executeCommand(MessageReceivedEvent event, String[] args) {
		EmbedBuilder eb = new EmbedBuilder().setColor(Color.ORANGE);
		if (args.length == 1 || !Arrays.asList(choice).contains(args[1])) {
			eb.setColor(Color.RED).setDescription("Wrong Arguments.");
			eb.addField(discord().getPrefix() + " " + String.join(", ", getCommands()) + " <choice>", "Choice: \n```css\n" + String.join(", ", choice) + "```", false);
			event.getTextChannel().sendMessageEmbeds(eb.build()).queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
			event.getMessage().addReaction("\u274C").queue();
			return;
		}

		switch (args[1]) {
			case "players" -> {
				if (args.length == 2 || args.length > 3 || !Arrays.asList(params).contains(args[2])) {
					eb.setColor(Color.RED).setDescription("Wrong Arguments.");
					eb.addField("Example:", discord().getPrefix() + " top players \nParams: \n```css\n" + String.join(", ", params) + "```", false);
					event.getTextChannel().sendMessageEmbeds(eb.build()).queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
					event.getMessage().addReaction("\u274C").queue();
					return;
				}
				eb.setDescription("***___TOP 10 " + args[2].toUpperCase() + " PLAYERS___***");
				int count = 0;
				switch (args[2]) {
					case "pvp" -> {
						List<PlayerHolder> topPvP = getAllPlayers().stream().sorted(Comparator.comparing(PlayerHolder::getPvps).reversed()).collect(Collectors.toList());
						for (PlayerHolder pl : topPvP) {
							int level = pl.getLevel();
							String playerName = pl.getName();
							String msg = "`\n***Level:*** `" + level + "`\n***PvP:*** `" + pl.getPvps() + "`\n***Pk:*** `" + pl.getPks() + "`";
							if (count < 10) {
								count++;
								eb.addField(count + ". " + playerName, msg, true);
							}
						}
					}
					case "pk" -> {
						List<PlayerHolder> topPk = getAllPlayers().stream().sorted(Comparator.comparing(PlayerHolder::getPks).reversed()).collect(Collectors.toList());
						for (PlayerHolder pl : topPk) {
							int level = pl.getLevel();
							String playerName = pl.getName();
							String msg = "`\n***Level:*** `" + level + "`\n***PvP:*** `" + pl.getPvps() + "`\n***Pk:*** `" + pl.getPks() + "`";
							if (count < 10) {
								count++;
								eb.addField(count + ". " + playerName, msg, true);
							}
						}
					}
					case "online" -> {
						List<PlayerHolder> topOnlineTime = getAllPlayers().stream().sorted(Comparator.comparing(PlayerHolder::getOnlineTime).reversed()).collect(Collectors.toList());
						for (PlayerHolder pl : topOnlineTime) {
							int level = pl.getLevel();
							String playerName = pl.getName();
							String msg = "`\n***Level:*** `" + level + "`\n***PvP:*** `" + pl.getPvps() + "`\n***Pk:*** `" + pl.getPks() + "`\n***Online:*** `" + pl.getOnlineTime() + "`";
							if (count < 10) {
								count++;
								eb.addField(count + ". " + playerName, msg, true);
							}
						}
					}
					case "adena" -> {
						List<PlayerHolder> topAdena = getAllPlayers().stream().sorted(Comparator.comparing(PlayerHolder::getAdena).reversed()).collect(Collectors.toList());
						for (PlayerHolder pl : topAdena) {
							String adena = DECIMAL_FORMATTER.format(pl.getAdena());
							int level = pl.getLevel();
							String playerName = pl.getName();
							String msg = "`\n***Level:*** `" + level + "`\n***PvP:*** `" + pl.getPvps() + "`\n***Pk:*** `" + pl.getPks() + "`\n***Online:*** `" + pl.getOnlineTime() + "`\n***Adena:*** `" + adena + "` ";
							if (count < 10) {
								count++;
								eb.addField(count + ". " + playerName, msg, true);
							}
						}
					}
				}
				if (getAllPlayers().isEmpty()) {
					eb.addField("No Players", NoPlayersMsg, true);
				} else {
					eb.setFooter("Total Players: " + getAllPlayers().size(), event.getGuild().getIconUrl());
				}
				eb.setThumbnail(event.getGuild().getIconUrl());
				event.getMessage().addReaction("\u2705").queue();
				event.getChannel().sendMessageEmbeds(eb.build()).queue(message -> message.delete().queueAfter(60, TimeUnit.SECONDS));
			}
			case "clans" -> {
				if (args.length > 2) {
					eb.setColor(Color.RED).setDescription("Wrong Arguments.");
					eb.addField(discord().getPrefix() + " " + String.join(", ", getCommands()) + " <choice>", "Choice: \n```css\n" + String.join(", ", choice) + "```", false);
					event.getTextChannel().sendMessageEmbeds(eb.build()).queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
					event.getMessage().addReaction("\u274C").queue();
					return;
				}
				eb.setDescription("***___TOP 10 CLANS___***");
				List<L2Clan> clans = ClanTable.getInstance().getClans().stream().sorted(Comparator.comparing(L2Clan::getLevel).reversed()).collect(Collectors.toList());
				int count = 0;
				for (L2Clan clan : clans) {
					int clanID = clan.getId();
					int clanLevel = clan.getLevel();
					String reputation = DECIMAL_FORMATTER.format(clan.getReputationScore());
					String castleName = "[No-Castle]";
					String allyStatus = "[No-ally]";
					String clanName = clan.getName();
					String allyName = clan.getAllyId() > 0 ? clan.getAllyName() : "[No-ally]";
					String leaderName = clan.getLeaderName();
					for (Castle castle : CastleManager.getInstance().getCastles()) {
						if (castle.getOwnerId() == clan.getId()) {
							castleName = castle.getName();
						}
					}
					if (clan.getAllyId() != 0) {
						allyStatus = clan.getAllyId() == clanID ? "Alliance Leader" : "Affiliated Clan";
					}
					String msg = "***Leader:*** `" + leaderName + "`\n***Clan Level:*** `" + clanLevel + "`\n***Reputation:*** `" + reputation + "`\n***Castle:*** `" + castleName + "`\n***Ally Name:*** `" + allyName + "`\n***Ally Status:*** `" + allyStatus + "` " + "\n***Clan Wars:*** `" + clan.getWarList().size() + "`";
					if (count < 10) {
						count++;
						eb.addField(clanName, msg, true);
					}
				}
				if (clans.isEmpty()) {
					eb.addField("No Clans", NoClansMsg, true);
				} else {
					eb.setFooter("Total Clans: " + ClanTable.getInstance().getClans().size(), event.getGuild().getIconUrl());
				}
				eb.setThumbnail(event.getGuild().getIconUrl());
				event.getMessage().addReaction("\u2705").queue();
				event.getChannel().sendMessageEmbeds(eb.build()).queue(message -> message.delete().queueAfter(60, TimeUnit.SECONDS));
			}
		}
	}

	/**
	 * We had to do a sql query to get all players online and offline.
	 * We could use {L2World.getInstance().getPlayers();} but it only returns the online players.
	 */
	private List<PlayerHolder> getAllPlayers() {
		List<PlayerHolder> players = new ArrayList<>();
		try (Connection con = ConnectionFactory.getInstance().getConnection()) {
			String selectPlayers = "SELECT char_name, pvpkills, pkkills, onlinetime, level, items.item_id, items.count FROM characters INNER JOIN items ON items.owner_id = characters.charId WHERE characters.accesslevel = 0 AND items.item_id=57";
			try (PreparedStatement st = con.prepareStatement(selectPlayers)) {
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					PlayerHolder p = new PlayerHolder();
					p.adena = rs.getInt("count");
					p.pks = rs.getInt("pkkills");
					p.pvps = rs.getInt("pvpkills");
					p.name = rs.getString("char_name");
					p.onlineTime = rs.getInt("onlinetime");
					p.level = rs.getInt("level");
					players.add(p);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return players;
	}

	/**
	 * A data holder for the sql query above. We store the info that we need to show on the top list.
	 */
	private static class PlayerHolder {
		public String getName() {
			return name;
		}

		public int getPvps() {
			return pvps;
		}

		public int getPks() {
			return pks;
		}

		public int getAdena() {
			return adena;
		}

		public int getOnlineTime() {
			return onlineTime;
		}

		public int getLevel() {
			return level;
		}

		String name;
		int pvps;
		int pks;
		int adena;
		int onlineTime;
		int level;
	}
}
