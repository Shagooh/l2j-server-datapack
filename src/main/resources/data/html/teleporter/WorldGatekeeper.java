/*
 * Copyright © 2004-2020 L2J DataPack
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
package com.l2jserver.datapack.ai.npc.Teleports.WorldGatekeeper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.datapack.quests.Q00130_PathToHellbound.Q00130_PathToHellbound;
import com.l2jserver.datapack.quests.Q10268_ToTheSeedOfInfinity.Q10268_ToTheSeedOfInfinity;
import com.l2jserver.datapack.quests.Q10269_ToTheSeedOfDestruction.Q10269_ToTheSeedOfDestruction;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * World Teleporter AI.
 * 
 * @author Maneco2
 */
public final class WorldGatekeeper extends AbstractNpcAI {
	// NPC
	private static final int GATEKEEPER = 7077;
	// Itens
	private static final int CARAVAN_PREMIUM_CERTIFICATE = 9852;
	// Misc
	private static final int MIN_LEVEL = 40;
	private static final int TELEPORT_PRICE = 50000;
	private static final Map<Integer, Location> LOCATIONS = new HashMap<>();
	// Base Gatekeeper Zone
	private static final L2ZoneType ZONE = ZoneManager.getInstance().getZoneById(11040);
	// Locations
	private static final Location[] LEVEL_1 = { new Location(-40361, -103014, -2808), // Valley of Heroes
			new Location(18981, 6289, -3440), // Southern Shilen's Garden
			new Location(47296, 59630, -3624), // Southern Elven Village
			new Location(-122433, 73133, -2872), // Stronghold I
			new Location(120041, -192662, -3456), // Southern Dwarven Village
			new Location(-24734, -130918, -679) // Northern Immortal Plateau
	};
	private static final Location[] LEVEL_10 = { new Location(-21326, 14209, -3368), // Dark Forest
			new Location(20176, 50769, -3694), // Elven Forest
			new Location(-104346, 214847, -3428), // Northem Territory
			new Location(28982, 74951, -3781), // Elven Fortress
			new Location(-47069, 59449, -3326), // School of Dark Arts
			new Location(-21702, 40656, -3269), // Swampland
			new Location(139780, -177245, -1534) // Abandoned Coal Mines
	};
	private static final Location[] LEVEL_15 = { new Location(-859, 101244, -3720), // Evil Hunting Grounds
			new Location(136931, -205027, -3657), // Westem Mining Zone
			new Location(-75479, 169680, -3720), // Windmill Hill
			new Location(-10722, 75846, -3595), // Neutral Zone
			new Location(-51628, 195547, -3752), // Langk Lizardmen
			new Location(-42484, 120276, -3483), // Ruins of Agony
			new Location(-20067, 137498, -3897) // Ruins of Despair
	};
	private static final Location[] LEVEL_20 = { new Location(-46945, 140560, -2942), // Abandoned Camp
			new Location(32367, 133746, -3128), // Dion Hills
			new Location(-25573, 160371, -2565), // Windawood Manor
			new Location(-6047, 202524, -3729), // Ol Mahum Checkpoint
			new Location(-28030, 127601, -3327), // Eastern Region
			new Location(-68702, 115447, -3615), // Fellmere Harvesting
			new Location(22762, 146214, -3344) // Western Town of Dion
	};
	private static final Location[] LEVEL_25 = { new Location(89, 178990, -3726), // Plains of Dion
			new Location(5687, 126325, -3675), // Cruma Marshlands
			new Location(-89751, 105392, -3576), // Orc Barracks
			new Location(-52793, 190617, -3492), // Forgotten Temple
			new Location(-17096, 206692, -3665), // Wastelands
			new Location(47308, 110988, -2115), // Fortress of Resistance
			new Location(-88502, 82603, -2766) // Windy Hill
	};
	private static final Location[] LEVEL_30 = { new Location(-9992, 176190, -4164), // The Ant Nest
			new Location(85364, 131315, -3680), // Breka's Stronghold
			new Location(-42352, 198596, -2758), // Red Rock Ridge
			new Location(103225, 134546, -3616), // Gorgon Flower Garden
			new Location(111578, -153663, -1532), // Plunderous Plains
			new Location(44621, 148474, -3702), // Execution Grounds
			new Location(11052, 171455, -3590) // Floran Agricultural Area
	};
	private static final Location[] LEVEL_35 = { new Location(111180, 200879, -3624), // Alligator Beach
			new Location(117129, -140602, -2895), // Sky Wagon Relic
			new Location(35731, 187288, -2996), // BeeHive
			new Location(69625, 121530, -3528), // Death Pass
			new Location(105825, 109674, -3212) // Hardin's Academy
	};
	static {
		LOCATIONS.put(1, new Location(42460, 143949, -5381)); // Heretic
		LOCATIONS.put(2, new Location(45667, 170301, -4981)); // Branded
		LOCATIONS.put(3, new Location(77168, 78399, -5125)); // Apostate
		LOCATIONS.put(4, new Location(139901, 79674, -5429)); // Witch
		LOCATIONS.put(5, new Location(-20009, 13503, -4901)); // Dark Omens
		LOCATIONS.put(6, new Location(113411, 84541, -6544)); // Forbidden Path
		LOCATIONS.put(7, new Location(-41567, 209223, -5090)); // Sacrifice
		LOCATIONS.put(8, new Location(45251, 124484, -5413)); // Pilgrim's
		LOCATIONS.put(9, new Location(110673, 174014, -5439)); // Worship
		LOCATIONS.put(10, new Location(-22403, 77372, -5176)); // Patriot's
		LOCATIONS.put(11, new Location(-52952, 79101, -4741)); // Devotion
		LOCATIONS.put(12, new Location(117704, 132796, -4831)); // Martyrdom
		LOCATIONS.put(13, new Location(82498, 209212, -5439)); // Saint's
		LOCATIONS.put(14, new Location(171739, -17599, -4901)); // Disciple's
		LOCATIONS.put(15, new Location(9775, 15589, -4574)); // Dark Elven Village
		LOCATIONS.put(16, new Location(-44905, 75508, -3608)); // Altar of Rites
		LOCATIONS.put(17, new Location(-21326, 14209, -3368)); // Dark Forest
		LOCATIONS.put(18, new Location(-21702, 40656, -3269)); // Swampland
		LOCATIONS.put(19, new Location(-61114, 74865, -3357)); // Spider Nest
		LOCATIONS.put(20, new Location(-10722, 75846, -3595)); // Neutral Zone
		LOCATIONS.put(21, new Location(24333, 10926, -3725)); // The Shilen Temple
		LOCATIONS.put(22, new Location(-47069, 59449, -3326)); // School of Dark Arts
		LOCATIONS.put(23, new Location(46921, 51431, -2980)); // Elven Village
		LOCATIONS.put(24, new Location(20176, 50769, -3694)); // Elven Forest
		LOCATIONS.put(25, new Location(28982, 74951, -3781)); // Elven Fortress
		LOCATIONS.put(26, new Location(-10722, 75846, -3595)); // Neutral Zone
		LOCATIONS.put(27, new Location(53550, 83486, -3527)); // Iris Lake
		LOCATIONS.put(28, new Location(28299, 60783, -3688)); // Southern Elven Forest
		LOCATIONS.put(29, new Location(-21702, 40656, -3269)); // Swampland
		LOCATIONS.put(30, new Location(-84191, 244534, -3729)); // Talking Island Village
		LOCATIONS.put(31, new Location(-96788, 258297, -3607)); // Talking Island Harbor
		LOCATIONS.put(32, new Location(-112786, 234919, -3695)); // Elven Ruins
		LOCATIONS.put(33, new Location(-111732, 244308, -3454)); // Singing Waterfall
		LOCATIONS.put(34, new Location(-110128, 215270, -3331)); // Northem Territory
		LOCATIONS.put(35, new Location(-100011, 236821, -3530)); // Obelisk of Victory
		LOCATIONS.put(36, new Location(-45146, -112549, -240)); // Orc Village
		LOCATIONS.put(37, new Location(-17617, -112913, -2640)); // Immortal Plateau
		LOCATIONS.put(38, new Location(-3323, -79071, -2732)); // Southern Immortal Plateau
		LOCATIONS.put(39, new Location(-24734, -130918, -679)); // Northern Immortal Plateau
		LOCATIONS.put(40, new Location(9361, -112485, -2538)); // Cave of Trials
		LOCATIONS.put(41, new Location(8203, -138764, -1006)); // Frozen Waterfall
		LOCATIONS.put(42, new Location(115171, -178211, -910)); // Dwarf Village
		LOCATIONS.put(43, new Location(139780, -177245, -1534)); // Abandoned Coal Mines
		LOCATIONS.put(44, new Location(169208, -207968, -3465)); // Eastem Mining Zone
		LOCATIONS.put(45, new Location(136931, -205027, -3657)); // Westem Mining Zone
		LOCATIONS.put(46, new Location(172066, -173422, 3452)); // Mithril Mines
		LOCATIONS.put(47, new Location(178776, -184396, -353)); // Northern Mithril Mines
		LOCATIONS.put(48, new Location(-116949, 46535, 367)); // Kamael Village
		LOCATIONS.put(49, new Location(-73958, 51931, -3684)); // Soul Harbor
		LOCATIONS.put(50, new Location(-122433, 73133, -2872)); // Stronghold I
		LOCATIONS.put(51, new Location(-95574, 52165, -2033)); // Stronghold II
		LOCATIONS.put(52, new Location(-85939, 37046, -2053)); // Stronghold III
		LOCATIONS.put(53, new Location(-86942, 42641, -2659)); // Nornil's Cave
		LOCATIONS.put(54, new Location(-110607, 64035, -2901)); // Hills of Gold
		LOCATIONS.put(55, new Location(83400, 148004, -3405)); // Town of Giran
		LOCATIONS.put(56, new Location(47933, 186742, -3486)); // Giran Harbor
		LOCATIONS.put(57, new Location(105825, 109674, -3212)); // Hardin's Academy
		LOCATIONS.put(58, new Location(43420, 206838, -3757)); // Devil's Isle
		LOCATIONS.put(59, new Location(85364, 131315, -3680)); // Breka's Stronghold
		LOCATIONS.put(60, new Location(103225, 134546, -3616)); // Gorgon Flower Garden
		LOCATIONS.put(61, new Location(147948, -55307, -2734)); // Town of Goddard
		LOCATIONS.put(62, new Location(125584, -40772, -3742)); // Varka Silenos Outpost
		LOCATIONS.put(63, new Location(146217, -68839, -3728)); // Ketra Orc Outpost
		LOCATIONS.put(64, new Location(149636, -112454, -2064)); // Hot Springs
		LOCATIONS.put(65, new Location(165501, -47504, -3589)); // Wall of Argos
		LOCATIONS.put(66, new Location(108028, -87801, -2908)); // Monastery of Silence
		LOCATIONS.put(67, new Location(175989, -116110, -3770)); // Forge of the Gods
		LOCATIONS.put(68, new Location(146824, 25814, -2013)); // Town of Aden
		LOCATIONS.put(69, new Location(168900, 38548, -4103)); // Forsaken Plains
		LOCATIONS.put(70, new Location(187592, 20481, -3609)); // The Forbidden Gateway
		LOCATIONS.put(71, new Location(159208, -12887, -2908)); // Blazing Swamp
		LOCATIONS.put(72, new Location(183508, -15173, -2771)); // Fields of Massacre
		LOCATIONS.put(73, new Location(171691, 56453, -5600)); // Silent Valley
		LOCATIONS.put(74, new Location(114206, 16140, -5103)); // Tower of Insolence
		LOCATIONS.put(75, new Location(174475, 52693, -4370)); // The Giants Cave
		LOCATIONS.put(76, new Location(15648, 142957, -2706)); // Town of Dion
		LOCATIONS.put(77, new Location(5687, 126325, -3675)); // Cruma Marshlands
		LOCATIONS.put(78, new Location(17181, 114184, -3443)); // Cruma Tower
		LOCATIONS.put(79, new Location(47308, 110988, -2115)); // Fortress of Resistance
		LOCATIONS.put(80, new Location(89, 178990, -3726)); // Plains of Dion
		LOCATIONS.put(81, new Location(35731, 187288, -2996)); // BeeHive
		LOCATIONS.put(82, new Location(60116, 165624, -2962)); // Tanor Canyon
		LOCATIONS.put(83, new Location(44621, 148474, -3702)); // Execution Grounds
		LOCATIONS.put(84, new Location(-12783, 122747, -3117)); // Town of Gludio
		LOCATIONS.put(85, new Location(-28030, 127601, -3327)); // Eastern Region
		LOCATIONS.put(86, new Location(-42484, 120276, -3483)); // Ruins of Agony
		LOCATIONS.put(87, new Location(-20067, 137498, -3897)); // Ruins of Despair
		LOCATIONS.put(88, new Location(-9992, 176190, -4164)); // The Ant Nest
		LOCATIONS.put(89, new Location(-25573, 160371, -2565)); // Windawood Manor
		LOCATIONS.put(90, new Location(-859, 101244, -3720)); // Evil Hunting Grounds
		LOCATIONS.put(91, new Location(117030, 76916, -2701)); // Hunters Village
		LOCATIONS.put(92, new Location(105825, 109674, -3212)); // Hardin's Academy
		LOCATIONS.put(93, new Location(124976, 61824, -3976)); // Southern Enchanted Valley
		LOCATIONS.put(94, new Location(104508, 34070, -3880)); // Northern Enchanted Valley
		LOCATIONS.put(95, new Location(141999, 81255, -3006)); // The Forest of Mirrors
		LOCATIONS.put(96, new Location(73135, 118345, -3710)); // Dragon Valley
		LOCATIONS.put(97, new Location(131583, 114425, -3704)); // Antharas's Lair
		LOCATIONS.put(98, new Location(82915, 53179, -1496)); // Town of Oren
		LOCATIONS.put(99, new Location(105825, 109674, -3212)); // Hardin's Academy
		LOCATIONS.put(100, new Location(76880, 63831, -3655)); // Western Sel Mahum
		LOCATIONS.put(101, new Location(79242, 71461, -3473)); // Southern Sel Mahum
		LOCATIONS.put(102, new Location(87678, 61159, -3668)); // Sel Mahum Center
		LOCATIONS.put(103, new Location(87663, 85492, -3102)); // Plains of Lizardmen
		LOCATIONS.put(104, new Location(91773, -12089, -2447)); // Outlaw Forest
		LOCATIONS.put(105, new Location(64342, 26835, -3773)); // Sea of Spores
		LOCATIONS.put(106, new Location(87119, -143355, -1293)); // Town of Schuttgart
		LOCATIONS.put(107, new Location(68356, -110635, -1981)); // Den of Evil
		LOCATIONS.put(108, new Location(111578, -153663, -1532)); // Plunderous Plains
		LOCATIONS.put(109, new Location(113879, -108792, -856)); // Frozen Labyrinth
		LOCATIONS.put(110, new Location(47719, -115743, -3744)); // Crypts of Disgrace
		LOCATIONS.put(111, new Location(91057, -117623, -3928)); // Pavel Ruins
		LOCATIONS.put(112, new Location(172066, -173422, 3452)); // Mithril Mines
		LOCATIONS.put(113, new Location(-80788, 149843, -3044)); // Gludin Village
		LOCATIONS.put(114, new Location(-90180, 149983, -3607)); // Gludin Harbor
		LOCATIONS.put(115, new Location(-48260, 213265, -3359)); // Langk Lizardmen
		LOCATIONS.put(116, new Location(-75479, 169680, -3720)); // Windmill Hill
		LOCATIONS.put(117, new Location(-68702, 115447, -3615)); // Fellmere Harvesting
		LOCATIONS.put(118, new Location(-52793, 190617, -3492)); // Forgotten Temple
		LOCATIONS.put(119, new Location(-88502, 82603, -2766)); // Windy Hill
		LOCATIONS.put(120, new Location(-46945, 140560, -2942)); // Abandoned Camp
		LOCATIONS.put(121, new Location(-17096, 206692, -3665)); // Wastelands
		LOCATIONS.put(122, new Location(-42352, 198596, -2758)); // Red Rock Ridge
		LOCATIONS.put(123, new Location(43810, -47753, -797)); // Rune Township
		LOCATIONS.put(124, new Location(38003, -38430, -3609)); // Rune Harbor
		LOCATIONS.put(125, new Location(65443, -71494, -3708)); // Valley of Saints
		LOCATIONS.put(126, new Location(52050, -54350, -3158)); // Forest of the Dead
		LOCATIONS.put(127, new Location(69740, -50091, -3282)); // Swamp of Screams
		LOCATIONS.put(128, new Location(108028, -87801, -2908)); // Monastery of Silence
		LOCATIONS.put(129, new Location(89631, -44740, -2142)); // Stakato Nest
		LOCATIONS.put(130, new Location(10632, -24418, -3644)); // Primeval Isle
		LOCATIONS.put(131, new Location(111381, 219358, -3546)); // Heine
		LOCATIONS.put(132, new Location(87362, 163017, -3576)); // Field of Silence
		LOCATIONS.put(133, new Location(81818, 226469, -3608)); // Field of Whispers
		LOCATIONS.put(134, new Location(113942, 178188, -3277)); // Alligator Island
		LOCATIONS.put(135, new Location(84526, 235081, -3740)); // Garden of Eva
		LOCATIONS.put(136, new Location(149808, 194740, -3692)); // Isle of Prayer
		LOCATIONS.put(137, new Location(148280, 172195, -955)); // Parmassus
		LOCATIONS.put(138, new Location(-12783, 122747, -3117)); // Keucereus to Gludio
	}

	public WorldGatekeeper() {
		super(WorldGatekeeper.class.getSimpleName(), "ai/npc/Teleports");
		addFirstTalkId(GATEKEEPER);
		addStartNpc(GATEKEEPER);
		addTalkId(GATEKEEPER);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		switch (event) {
		case "Window": {
			return "7077.htm";
		}
		case "StartZones": {
			return "7077-1.htm";
		}
		case "Kingdowns": {
			return "7077-2.htm";
		}
		case "Townships": {
			return "7077-3.htm";
		}
		case "Catacomb": {
			return "7077-4.htm";
		}
		case "Necropolis": {
			return "7077-5.htm";
		}
		case "DarkElven": {
			return "7077-6.htm";
		}
		case "Elven": {
			return "7077-7.htm";
		}
		case "Human": {
			return "7077-8.htm";
		}
		case "Orc": {
			return "7077-9.htm";
		}
		case "Dwarf": {
			return "7077-10.htm";
		}
		case "Kamael": {
			return "7077-11.htm";
		}
		case "Giran": {
			return "7077-12.htm";
		}
		case "Goddard": {
			return "7077-13.htm";
		}
		case "Aden": {
			return "7077-14.htm";
		}
		case "Dion": {
			return "7077-15.htm";
		}
		case "Gludio": {
			return "7077-16.htm";
		}
		case "Hunters": {
			return "7077-17.htm";
		}
		case "Oren": {
			return "7077-18.htm";
		}
		case "Schuttgart": {
			return "7077-19.htm";
		}
		case "Gludin": {
			return "7077-20.htm";
		}
		case "Rune": {
			return "7077-21.htm";
		}
		case "Heine": {
			return "7077-22.htm";
		}
		case "SpecialAreas": {
			return "7077-23.htm";
		}
		case "LowLevel": {
			if ((player.getLevel() >= 1) && (player.getLevel() <= 9)) {
				player.teleToLocation(LEVEL_1[getRandom(LEVEL_1.length)], true);
			} else if ((player.getLevel() >= 10) && (player.getLevel() <= 14)) {
				player.teleToLocation(LEVEL_10[getRandom(LEVEL_10.length)], true);
			} else if ((player.getLevel() >= 15) && (player.getLevel() <= 19)) {
				player.teleToLocation(LEVEL_15[getRandom(LEVEL_15.length)], true);
			} else if ((player.getLevel() >= 20) && (player.getLevel() <= 24)) {
				player.teleToLocation(LEVEL_20[getRandom(LEVEL_20.length)], true);
			} else if ((player.getLevel() >= 25) && (player.getLevel() <= 29)) {
				player.teleToLocation(LEVEL_25[getRandom(LEVEL_25.length)], true);
			} else if ((player.getLevel() >= 30) && (player.getLevel() <= 34)) {
				player.teleToLocation(LEVEL_30[getRandom(LEVEL_30.length)], true);
			} else {
				player.teleToLocation(LEVEL_35[getRandom(LEVEL_35.length)], true);
			}
			break;
		}
		case "KeucereusAllianceBase": {
			final QuestState quest1 = player.getQuestState(Q10268_ToTheSeedOfInfinity.class.getSimpleName());
			final QuestState quest2 = player.getQuestState(Q10269_ToTheSeedOfDestruction.class.getSimpleName());
			if ((quest1 != null) && (quest1.getState() == State.COMPLETED) && (quest2 != null)
					&& (quest2.getState() == State.COMPLETED)) {
				player.teleToLocation(-186742, 244155, 2675); // Keucereus Alliance Base
				return null;
			} else {
				return "no-quest.htm";
			}
		}
		case "HellboundEntrance": {
			final QuestState quest1 = player.getQuestState(Q00130_PathToHellbound.class.getSimpleName());
			if ((quest1 != null) && (quest1.getState() == State.COMPLETED)) {
				player.teleToLocation(-11259, 236479, -3245); // Hellbound Entrance
				return null;
			} else {
				return "no-quest.htm";
			}
		}
		case "TowerOfInfinitum": {
			if (hasQuestItems(player, CARAVAN_PREMIUM_CERTIFICATE)) {
				if (!player.isInParty()) {
					player.sendPacket(SystemMessageId.NOT_IN_PARTY_CANT_ENTER);
				} else {
					final L2Party party = player.getParty();
					final boolean isInCC = party.isInCommandChannel();
					final List<L2PcInstance> members = (isInCC) ? party.getCommandChannel().getMembers()
							: party.getMembers();
					for (L2PcInstance groupMembers : members) {
						if (checkConditions(player)) {
							groupMembers.teleToLocation(-22203, 277099, -15049, true); // Hellbound - Tower Of Infinitum
						}
					}
				}
				return null;
			} else {
				return "no-quest.htm";
			}
		}
		case "TullyWorkshop": {
			if (hasQuestItems(player, CARAVAN_PREMIUM_CERTIFICATE)) {
				if (!player.isInParty()) {
					player.sendPacket(SystemMessageId.NOT_IN_PARTY_CANT_ENTER);
				} else {
					final L2Party party = player.getParty();
					final boolean isInCC = party.isInCommandChannel();
					final List<L2PcInstance> members = (isInCC) ? party.getCommandChannel().getMembers()
							: party.getMembers();
					for (L2PcInstance groupMembers : members) {
						if (checkConditions(player)) {
							groupMembers.teleToLocation(-12714, 273929, -15304, true); // Hellbound - Tully Workshop
						}
					}
				}
				return null;
			} else {
				return "no-quest.htm";
			}
		}
		}
		if (player.getLevel() >= MIN_LEVEL) {
			final int ask = Integer.parseInt(event);
			if (player.getAdena() >= TELEPORT_PRICE) {
				takeItems(player, Inventory.ADENA_ID, TELEPORT_PRICE);
				player.teleToLocation(LOCATIONS.get(ask), true);
			} else {
				player.sendPacket(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
			}
		}
		return super.onAdvEvent(event, npc, player);
	}

	protected boolean checkConditions(L2PcInstance player) {
		final L2Party party = player.getParty();
		final boolean isInCC = party.isInCommandChannel();
		final List<L2PcInstance> members = (isInCC) ? party.getCommandChannel().getMembers() : party.getMembers();
		final boolean isPartyLeader = (isInCC) ? party.getCommandChannel().isLeader(player) : party.isLeader(player);

		if (!isPartyLeader) {
			broadcastSystemMessage(player, null, SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER, false);
			return false;
		}

		for (L2PcInstance groupMembers : members) {
			if (groupMembers.getLevel() <= 78) {
				broadcastSystemMessage(player, groupMembers,
						SystemMessageId.C1_S_LEVEL_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED, true);
				return false;
			}

			if (!player.isInsideRadius(groupMembers, 3000, true, false)) {
				broadcastSystemMessage(player, groupMembers,
						SystemMessageId.C1_IS_IN_A_LOCATION_WHICH_CANNOT_BE_ENTERED_THEREFORE_IT_CANNOT_BE_PROCESSED,
						true);
				return false;
			}
		}
		return true;
	}

	private void broadcastSystemMessage(L2PcInstance player, L2PcInstance member, SystemMessageId msgId,
			boolean toGroup) {
		final SystemMessage sm = SystemMessage.getSystemMessage(msgId);

		if (toGroup) {
			sm.addPcName(member);

			if (player.getParty().isInCommandChannel()) {
				player.getParty().getCommandChannel().broadcastPacket(sm);
			} else {
				player.getParty().broadcastPacket(sm);
			}
		} else {
			player.broadcastPacket(sm);
		}
	}

	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		final QuestState quest1 = player.getQuestState(Q10268_ToTheSeedOfInfinity.class.getSimpleName());
		final QuestState quest2 = player.getQuestState(Q10269_ToTheSeedOfDestruction.class.getSimpleName());
		if (player.getLevel() < MIN_LEVEL) {
			return "no-level.htm";
		} else {
			if (!ZONE.isInsideZone(npc)) {
				return "7077.htm";
			} else {
				if ((quest1 != null) && (quest1.getState() == State.COMPLETED) && (quest2 != null)
						&& (quest2.getState() == State.COMPLETED)) {
					return "7077-b.htm";
				} else {
					return "no-quest.htm";
				}
			}
		}
	}
}