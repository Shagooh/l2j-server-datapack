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
package com.l2jserver.datapack.handlers;

import static com.l2jserver.gameserver.config.Configuration.customs;
import static com.l2jserver.gameserver.config.Configuration.general;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.util.Util;
import com.l2jserver.datapack.handlers.actionhandlers.L2ArtefactInstanceAction;
import com.l2jserver.datapack.handlers.actionhandlers.L2DecoyAction;
import com.l2jserver.datapack.handlers.actionhandlers.L2DoorInstanceAction;
import com.l2jserver.datapack.handlers.actionhandlers.L2ItemInstanceAction;
import com.l2jserver.datapack.handlers.actionhandlers.L2NpcAction;
import com.l2jserver.datapack.handlers.actionhandlers.L2PcInstanceAction;
import com.l2jserver.datapack.handlers.actionhandlers.L2PetInstanceAction;
import com.l2jserver.datapack.handlers.actionhandlers.L2StaticObjectInstanceAction;
import com.l2jserver.datapack.handlers.actionhandlers.L2SummonAction;
import com.l2jserver.datapack.handlers.actionhandlers.L2TrapAction;
import com.l2jserver.datapack.handlers.actionshifthandlers.L2DoorInstanceActionShift;
import com.l2jserver.datapack.handlers.actionshifthandlers.L2ItemInstanceActionShift;
import com.l2jserver.datapack.handlers.actionshifthandlers.L2NpcActionShift;
import com.l2jserver.datapack.handlers.actionshifthandlers.L2PcInstanceActionShift;
import com.l2jserver.datapack.handlers.actionshifthandlers.L2StaticObjectInstanceActionShift;
import com.l2jserver.datapack.handlers.actionshifthandlers.L2SummonActionShift;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminAdmin;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminAgathion;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminAnnouncements;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminBBS;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminBuffs;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminCHSiege;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminCamera;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminChangeAccessLevel;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminClan;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminCreateItem;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminCursedWeapons;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminDebug;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminDelete;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminDisconnect;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminDoorControl;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminEditChar;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminEffects;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminElement;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminEnchant;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminEventEngine;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminEvents;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminExpSp;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminFightCalculator;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminFortSiege;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminGeodata;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminGm;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminGmChat;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminGraciaSeeds;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminGrandBoss;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminHeal;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminHtml;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminInstance;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminInstanceZone;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminInvul;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminKick;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminKill;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminLevel;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminLogin;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminMammon;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminManor;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminMenu;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminMessages;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminMobGroup;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminMonsterRace;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminPForge;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminPathNode;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminPcCondOverride;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminPetition;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminPledge;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminPolymorph;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminPunishment;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminQuest;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminReload;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminRepairChar;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminRes;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminRide;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminScan;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminShop;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminShowQuests;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminShutdown;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminSiege;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminSkill;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminSpawn;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminSummon;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminTarget;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminTargetSay;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminTeleport;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminTerritoryWar;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminTest;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminTvTEvent;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminUnblockIp;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminVitality;
import com.l2jserver.datapack.handlers.admincommandhandlers.AdminZone;
import com.l2jserver.datapack.handlers.bypasshandlers.Augment;
import com.l2jserver.datapack.handlers.bypasshandlers.Buy;
import com.l2jserver.datapack.handlers.bypasshandlers.BuyShadowItem;
import com.l2jserver.datapack.handlers.bypasshandlers.ChatLink;
import com.l2jserver.datapack.handlers.bypasshandlers.ClanWarehouse;
import com.l2jserver.datapack.handlers.bypasshandlers.EventEngine;
import com.l2jserver.datapack.handlers.bypasshandlers.Festival;
import com.l2jserver.datapack.handlers.bypasshandlers.Freight;
import com.l2jserver.datapack.handlers.bypasshandlers.ItemAuctionLink;
import com.l2jserver.datapack.handlers.bypasshandlers.Link;
import com.l2jserver.datapack.handlers.bypasshandlers.Loto;
import com.l2jserver.datapack.handlers.bypasshandlers.Multisell;
import com.l2jserver.datapack.handlers.bypasshandlers.NpcViewMod;
import com.l2jserver.datapack.handlers.bypasshandlers.Observation;
import com.l2jserver.datapack.handlers.bypasshandlers.OlympiadManagerLink;
import com.l2jserver.datapack.handlers.bypasshandlers.OlympiadObservation;
import com.l2jserver.datapack.handlers.bypasshandlers.PlayerHelp;
import com.l2jserver.datapack.handlers.bypasshandlers.PrivateWarehouse;
import com.l2jserver.datapack.handlers.bypasshandlers.QuestLink;
import com.l2jserver.datapack.handlers.bypasshandlers.QuestList;
import com.l2jserver.datapack.handlers.bypasshandlers.ReceivePremium;
import com.l2jserver.datapack.handlers.bypasshandlers.ReleaseAttribute;
import com.l2jserver.datapack.handlers.bypasshandlers.RentPet;
import com.l2jserver.datapack.handlers.bypasshandlers.Rift;
import com.l2jserver.datapack.handlers.bypasshandlers.SkillList;
import com.l2jserver.datapack.handlers.bypasshandlers.SupportBlessing;
import com.l2jserver.datapack.handlers.bypasshandlers.SupportMagic;
import com.l2jserver.datapack.handlers.bypasshandlers.TerritoryStatus;
import com.l2jserver.datapack.handlers.bypasshandlers.TutorialClose;
import com.l2jserver.datapack.handlers.bypasshandlers.VoiceCommand;
import com.l2jserver.datapack.handlers.bypasshandlers.Wear;
import com.l2jserver.datapack.handlers.chathandlers.ChatAll;
import com.l2jserver.datapack.handlers.chathandlers.ChatAlliance;
import com.l2jserver.datapack.handlers.chathandlers.ChatBattlefield;
import com.l2jserver.datapack.handlers.chathandlers.ChatClan;
import com.l2jserver.datapack.handlers.chathandlers.ChatHeroVoice;
import com.l2jserver.datapack.handlers.chathandlers.ChatParty;
import com.l2jserver.datapack.handlers.chathandlers.ChatPartyMatchRoom;
import com.l2jserver.datapack.handlers.chathandlers.ChatPartyRoomAll;
import com.l2jserver.datapack.handlers.chathandlers.ChatPartyRoomCommander;
import com.l2jserver.datapack.handlers.chathandlers.ChatPetition;
import com.l2jserver.datapack.handlers.chathandlers.ChatShout;
import com.l2jserver.datapack.handlers.chathandlers.ChatTell;
import com.l2jserver.datapack.handlers.chathandlers.ChatTrade;
import com.l2jserver.datapack.handlers.communityboard.ClanBoard;
import com.l2jserver.datapack.handlers.communityboard.FavoriteBoard;
import com.l2jserver.datapack.handlers.communityboard.FriendsBoard;
import com.l2jserver.datapack.handlers.communityboard.HomeBoard;
import com.l2jserver.datapack.handlers.communityboard.HomepageBoard;
import com.l2jserver.datapack.handlers.communityboard.MailBoard;
import com.l2jserver.datapack.handlers.communityboard.MemoBoard;
import com.l2jserver.datapack.handlers.communityboard.RegionBoard;
import com.l2jserver.datapack.handlers.itemhandlers.BeastSoulShot;
import com.l2jserver.datapack.handlers.itemhandlers.BeastSpiritShot;
import com.l2jserver.datapack.handlers.itemhandlers.BlessedSpiritShot;
import com.l2jserver.datapack.handlers.itemhandlers.Book;
import com.l2jserver.datapack.handlers.itemhandlers.Bypass;
import com.l2jserver.datapack.handlers.itemhandlers.Calculator;
import com.l2jserver.datapack.handlers.itemhandlers.CharmOfCourage;
import com.l2jserver.datapack.handlers.itemhandlers.Disguise;
import com.l2jserver.datapack.handlers.itemhandlers.Elixir;
import com.l2jserver.datapack.handlers.itemhandlers.EnchantAttribute;
import com.l2jserver.datapack.handlers.itemhandlers.EnchantScrolls;
import com.l2jserver.datapack.handlers.itemhandlers.EventItem;
import com.l2jserver.datapack.handlers.itemhandlers.ExtractableItems;
import com.l2jserver.datapack.handlers.itemhandlers.FishShots;
import com.l2jserver.datapack.handlers.itemhandlers.Harvester;
import com.l2jserver.datapack.handlers.itemhandlers.ItemSkills;
import com.l2jserver.datapack.handlers.itemhandlers.ItemSkillsTemplate;
import com.l2jserver.datapack.handlers.itemhandlers.ManaPotion;
import com.l2jserver.datapack.handlers.itemhandlers.Maps;
import com.l2jserver.datapack.handlers.itemhandlers.MercTicket;
import com.l2jserver.datapack.handlers.itemhandlers.NicknameColor;
import com.l2jserver.datapack.handlers.itemhandlers.PetFood;
import com.l2jserver.datapack.handlers.itemhandlers.Recipes;
import com.l2jserver.datapack.handlers.itemhandlers.RollingDice;
import com.l2jserver.datapack.handlers.itemhandlers.Seed;
import com.l2jserver.datapack.handlers.itemhandlers.SevenSignsRecord;
import com.l2jserver.datapack.handlers.itemhandlers.SoulShots;
import com.l2jserver.datapack.handlers.itemhandlers.SpecialXMas;
import com.l2jserver.datapack.handlers.itemhandlers.SpiritShot;
import com.l2jserver.datapack.handlers.itemhandlers.SummonItems;
import com.l2jserver.datapack.handlers.itemhandlers.TeleportBookmark;
import com.l2jserver.datapack.handlers.punishmenthandlers.BanHandler;
import com.l2jserver.datapack.handlers.punishmenthandlers.ChatBanHandler;
import com.l2jserver.datapack.handlers.punishmenthandlers.JailHandler;
import com.l2jserver.datapack.handlers.telnethandlers.ChatsHandler;
import com.l2jserver.datapack.handlers.telnethandlers.DebugHandler;
import com.l2jserver.datapack.handlers.telnethandlers.HelpHandler;
import com.l2jserver.datapack.handlers.telnethandlers.PlayerHandler;
import com.l2jserver.datapack.handlers.telnethandlers.ReloadHandler;
import com.l2jserver.datapack.handlers.telnethandlers.ServerHandler;
import com.l2jserver.datapack.handlers.telnethandlers.StatusHandler;
import com.l2jserver.datapack.handlers.telnethandlers.ThreadHandler;
import com.l2jserver.datapack.handlers.usercommandhandlers.ChannelDelete;
import com.l2jserver.datapack.handlers.usercommandhandlers.ChannelInfo;
import com.l2jserver.datapack.handlers.usercommandhandlers.ChannelLeave;
import com.l2jserver.datapack.handlers.usercommandhandlers.ClanPenalty;
import com.l2jserver.datapack.handlers.usercommandhandlers.ClanWarsList;
import com.l2jserver.datapack.handlers.usercommandhandlers.Dismount;
import com.l2jserver.datapack.handlers.usercommandhandlers.InstanceZone;
import com.l2jserver.datapack.handlers.usercommandhandlers.Loc;
import com.l2jserver.datapack.handlers.usercommandhandlers.Mount;
import com.l2jserver.datapack.handlers.usercommandhandlers.MyBirthday;
import com.l2jserver.datapack.handlers.usercommandhandlers.OlympiadStat;
import com.l2jserver.datapack.handlers.usercommandhandlers.PartyInfo;
import com.l2jserver.datapack.handlers.usercommandhandlers.SiegeStatus;
import com.l2jserver.datapack.handlers.usercommandhandlers.Time;
import com.l2jserver.datapack.handlers.usercommandhandlers.Unstuck;
import com.l2jserver.datapack.handlers.voicedcommandhandlers.AutoLoot;
import com.l2jserver.datapack.handlers.voicedcommandhandlers.Banking;
import com.l2jserver.datapack.handlers.voicedcommandhandlers.ChangePassword;
import com.l2jserver.datapack.handlers.voicedcommandhandlers.ChatAdmin;
import com.l2jserver.datapack.handlers.voicedcommandhandlers.Debug;
import com.l2jserver.datapack.handlers.voicedcommandhandlers.Lang;
import com.l2jserver.datapack.handlers.voicedcommandhandlers.StatsVCmd;
import com.l2jserver.datapack.handlers.voicedcommandhandlers.Wedding;
import com.l2jserver.gameserver.handler.ActionHandler;
import com.l2jserver.gameserver.handler.ActionShiftHandler;
import com.l2jserver.gameserver.handler.AdminCommandHandler;
import com.l2jserver.gameserver.handler.BypassHandler;
import com.l2jserver.gameserver.handler.ChatHandler;
import com.l2jserver.gameserver.handler.CommunityBoardHandler;
import com.l2jserver.gameserver.handler.IHandler;
import com.l2jserver.gameserver.handler.ItemHandler;
import com.l2jserver.gameserver.handler.PunishmentHandler;
import com.l2jserver.gameserver.handler.TelnetHandler;
import com.l2jserver.gameserver.handler.UserCommandHandler;
import com.l2jserver.gameserver.handler.VoicedCommandHandler;

/**
 * Master handler.
 * @author UnAfraid
 * @author Zoey76
 */
public class MasterHandler {
	private static final Logger LOG = LoggerFactory.getLogger(MasterHandler.class);
	
	private static final Class<?>[] ACTION_HANDLERS = {
		L2ArtefactInstanceAction.class,
		L2DecoyAction.class,
		L2DoorInstanceAction.class,
		L2ItemInstanceAction.class,
		L2NpcAction.class,
		L2PcInstanceAction.class,
		L2PetInstanceAction.class,
		L2StaticObjectInstanceAction.class,
		L2SummonAction.class,
		L2TrapAction.class,
	};
	
	private static final Class<?>[] ACTION_SHIFT_HANDLERS = {
		L2DoorInstanceActionShift.class,
		L2ItemInstanceActionShift.class,
		L2NpcActionShift.class,
		L2PcInstanceActionShift.class,
		L2StaticObjectInstanceActionShift.class,
		L2SummonActionShift.class,
	};
	
	private static final Class<?>[] ADMIN_HANDLERS = {
		AdminAdmin.class,
		AdminAgathion.class,
		AdminAnnouncements.class,
		AdminBBS.class,
		AdminBuffs.class,
		AdminCamera.class,
		AdminChangeAccessLevel.class,
		AdminCHSiege.class,
		AdminClan.class,
		AdminPcCondOverride.class,
		AdminCreateItem.class,
		AdminCursedWeapons.class,
		AdminDebug.class,
		AdminDelete.class,
		AdminDisconnect.class,
		AdminDoorControl.class,
		AdminEditChar.class,
		AdminEffects.class,
		AdminElement.class,
		AdminEnchant.class,
		AdminEventEngine.class,
		AdminEvents.class,
		AdminExpSp.class,
		AdminFightCalculator.class,
		AdminFortSiege.class,
		AdminGeodata.class,
		AdminGm.class,
		AdminGmChat.class,
		AdminGraciaSeeds.class,
		AdminGrandBoss.class,
		AdminHeal.class,
		AdminHtml.class,
		AdminInstance.class,
		AdminInstanceZone.class,
		AdminInvul.class,
		AdminKick.class,
		AdminKill.class,
		AdminLevel.class,
		AdminLogin.class,
		AdminMammon.class,
		AdminManor.class,
		AdminMenu.class,
		AdminMessages.class,
		AdminMobGroup.class,
		AdminMonsterRace.class,
		AdminPathNode.class,
		AdminPetition.class,
		AdminPForge.class,
		AdminPledge.class,
		AdminPolymorph.class,
		AdminPunishment.class,
		AdminQuest.class,
		AdminReload.class,
		AdminRepairChar.class,
		AdminRes.class,
		AdminRide.class,
		AdminScan.class,
		AdminShop.class,
		AdminShowQuests.class,
		AdminShutdown.class,
		AdminSiege.class,
		AdminSkill.class,
		AdminSpawn.class,
		AdminSummon.class,
		AdminTarget.class,
		AdminTargetSay.class,
		AdminTeleport.class,
		AdminTerritoryWar.class,
		AdminTest.class,
		AdminTvTEvent.class,
		AdminUnblockIp.class,
		AdminVitality.class,
		AdminZone.class,
	};
	
	private static final Class<?>[] BYPASS_HANDLERS = {
		Augment.class,
		Buy.class,
		BuyShadowItem.class,
		ChatLink.class,
		ClanWarehouse.class,
		EventEngine.class,
		Festival.class,
		Freight.class,
		ItemAuctionLink.class,
		Link.class,
		Loto.class,
		Multisell.class,
		NpcViewMod.class,
		Observation.class,
		OlympiadObservation.class,
		OlympiadManagerLink.class,
		QuestLink.class,
		PlayerHelp.class,
		PrivateWarehouse.class,
		QuestList.class,
		ReceivePremium.class,
		ReleaseAttribute.class,
		RentPet.class,
		Rift.class,
		SkillList.class,
		SupportBlessing.class,
		SupportMagic.class,
		TerritoryStatus.class,
		TutorialClose.class,
		VoiceCommand.class,
		Wear.class,
	};
	
	private static final Class<?>[] CHAT_HANDLERS = {
		ChatAll.class,
		ChatAlliance.class,
		ChatBattlefield.class,
		ChatClan.class,
		ChatHeroVoice.class,
		ChatParty.class,
		ChatPartyMatchRoom.class,
		ChatPartyRoomAll.class,
		ChatPartyRoomCommander.class,
		ChatPetition.class,
		ChatShout.class,
		ChatTell.class,
		ChatTrade.class,
	};
	
	private static final Class<?>[] COMMUNITY_HANDLERS = {
		ClanBoard.class,
		FavoriteBoard.class,
		FriendsBoard.class,
		HomeBoard.class,
		HomepageBoard.class,
		MailBoard.class,
		MemoBoard.class,
		RegionBoard.class,
	};
	
	private static final Class<?>[] ITEM_HANDLERS = {
		BeastSoulShot.class,
		BeastSpiritShot.class,
		BlessedSpiritShot.class,
		Book.class,
		Bypass.class,
		Calculator.class,
		CharmOfCourage.class,
		Disguise.class,
		Elixir.class,
		EnchantAttribute.class,
		EnchantScrolls.class,
		EventItem.class,
		ExtractableItems.class,
		FishShots.class,
		Harvester.class,
		ItemSkillsTemplate.class,
		ItemSkills.class,
		ManaPotion.class,
		Maps.class,
		MercTicket.class,
		NicknameColor.class,
		PetFood.class,
		Recipes.class,
		RollingDice.class,
		Seed.class,
		SevenSignsRecord.class,
		SoulShots.class,
		SpecialXMas.class,
		SpiritShot.class,
		SummonItems.class,
		TeleportBookmark.class,
	};
	
	private static final Class<?>[] PUNISHMENT_HANDLERS = {
		BanHandler.class,
		ChatBanHandler.class,
		JailHandler.class,
	};
	
	private static final Class<?>[] USER_COMMAND_HANDLERS = {
		ClanPenalty.class,
		ClanWarsList.class,
		Dismount.class,
		Unstuck.class,
		InstanceZone.class,
		Loc.class,
		Mount.class,
		PartyInfo.class,
		Time.class,
		OlympiadStat.class,
		ChannelLeave.class,
		ChannelDelete.class,
		ChannelInfo.class,
		MyBirthday.class,
		SiegeStatus.class,
	};
	
	private static final Class<?>[] TELNET_HANDLERS = {
		ChatsHandler.class,
		DebugHandler.class,
		HelpHandler.class,
		PlayerHandler.class,
		ReloadHandler.class,
		ServerHandler.class,
		StatusHandler.class,
		ThreadHandler.class,
	};
	
	private static final Class<?>[] VOICED_COMMAND_HANDLERS = {
		AutoLoot.class,
		StatsVCmd.class,
		// TODO: Add configuration options for this voiced commands:
		// CastleVCmd.class,
		// SetVCmd.class,
		(customs().allowWedding() ? Wedding.class : null),
		(customs().bankingEnabled() ? Banking.class : null),
		(customs().chatAdmin() ? ChatAdmin.class : null),
		(customs().multiLangEnable() && customs().multiLangVoiceCommand() ? Lang.class : null),
		(customs().debugVoiceCommand() ? Debug.class : null),
		(customs().allowChangePassword() ? ChangePassword.class : null),
	};
	
	// TODO(Zoey76): Add this handler.
	// private static final Class<?>[] CUSTOM_HANDLERS =
	// {
	// CustomAnnouncePkPvP.class
	// };
	
	public static void main(String[] args) {
		if (general().noHandlers()) {
			LOG.info("Handlers disabled...");
			return;
		}
		
		loadHandlers(VoicedCommandHandler.getInstance(), VOICED_COMMAND_HANDLERS);
		loadHandlers(ActionHandler.getInstance(), ACTION_HANDLERS);
		loadHandlers(ActionShiftHandler.getInstance(), ACTION_SHIFT_HANDLERS);
		loadHandlers(AdminCommandHandler.getInstance(), ADMIN_HANDLERS);
		loadHandlers(BypassHandler.getInstance(), BYPASS_HANDLERS);
		loadHandlers(ChatHandler.getInstance(), CHAT_HANDLERS);
		loadHandlers(CommunityBoardHandler.getInstance(), COMMUNITY_HANDLERS);
		loadHandlers(ItemHandler.getInstance(), ITEM_HANDLERS);
		loadHandlers(PunishmentHandler.getInstance(), PUNISHMENT_HANDLERS);
		loadHandlers(UserCommandHandler.getInstance(), USER_COMMAND_HANDLERS);
		loadHandlers(TelnetHandler.getInstance(), TELNET_HANDLERS);
	}
	
	private static void loadHandlers(IHandler<?, ?> handler, Class<?>[] classes) {
		for (Class<?> c : classes) {
			if (c == null) {
				continue;
			}
			
			try {
				handler.registerByClass(c);
			} catch (Exception ex) {
				LOG.error("Failed loading handler {}!", c.getSimpleName(), ex);
			}
		}
		LOG.info("Loaded {} {}.", handler.size(), Util.splitWords(handler.getClass().getSimpleName()));
	}
}