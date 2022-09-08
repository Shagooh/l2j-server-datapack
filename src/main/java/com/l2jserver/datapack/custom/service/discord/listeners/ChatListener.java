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
package com.l2jserver.datapack.custom.service.discord.listeners;

import com.l2jserver.datapack.custom.service.discord.DiscordBot;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.Containers;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerChat;
import com.l2jserver.gameserver.model.events.listeners.ConsumerEventListener;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.items.type.CrystalType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.l2jserver.gameserver.config.Configuration.discord;

/**
 * Chat Listener
 * @author Stalitsa
 * @version 2.6.2.0
 */
public class ChatListener extends ListenerAdapter {

    private static final Pattern ITEM_LINK = Pattern.compile("[\b]\tType=[0-9]+[\\s]+\tID=([0-9]+)[\\s]+\tColor=[0-9]+[\\s]+\tUnderline=[0-9]+[\\s]+\tTitle=\u001B(.[^\u001B]*)[^\b]");

    public ChatListener() {
        Containers.Global().addListener(new ConsumerEventListener(Containers.Global(), EventType.ON_PLAYER_CHAT, (OnPlayerChat event) -> {
            EmbedBuilder eb = new EmbedBuilder();
            String type = switch (event.getChatType()) {
                case 1 -> "Shout";
                case 8 -> "Trade";
                case 17 -> "Hero";
                default -> null;
            };
            if (type != null && discord().enableBot()) {
                String replacedText = onShiftItems(event.getActiveChar(),  event.getText());
                eb.setColor(Color.CYAN);
                eb.setTitle("***___" + type + "___***");
                eb.setDescription("**" + event.getActiveChar().getName() + ":** ``" + replacedText + "``");
                DiscordBot.sendMessageTo(eb, discord().getGameChatChannelId());
            }
        }, this));
    }

    private static String onShiftItems(L2PcInstance activeChar, String message) {
        Matcher matcher = ITEM_LINK.matcher(message);
        while (matcher.find()) {
            int objectId = Integer.parseInt(matcher.group(1));
            final L2ItemInstance item = activeChar.getInventory().getItemByObjectId(objectId);
            DecimalFormat df = new DecimalFormat("#,###");
            long count = item.getCount();
            String enchant = item.getEnchantLevel() > 0 ? " +" + item.getEnchantLevel() : "";
            String name = item.getItem().getItemGrade() != CrystalType.NONE ? item.getItem().getItemGrade().name() + "-" + item.getName() : "" + item.getName();
            final String info = name  + enchant + (item.isStackable() ? "\n" + df.format(count) : "");
            message = message.replace(matcher.group(0) + "\b", info);
        }
        if (message.contains("\b")) {
            return message.substring(0, message.indexOf(8));
        }
        return message;
    }
}
