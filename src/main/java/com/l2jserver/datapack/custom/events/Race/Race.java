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
package com.l2jserver.datapack.custom.events.Race;

import static com.l2jserver.gameserver.config.Configuration.general;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Event;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.skills.AbnormalType;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.util.Broadcast;

/**
 * @author Gnacik
 */
public final class Race extends Event {
	// Event NPC's list
	private final Set<L2Npc> _npcs = ConcurrentHashMap.newKeySet();
	// Npc
	private L2Npc _npc;
	// Player list
	private final Set<L2PcInstance> _players = ConcurrentHashMap.newKeySet();
	// Event Task
	ScheduledFuture<?> _eventTask = null;
	// Event state
	private static boolean _isActive = false;
	// Race state
	private static boolean _isRaceStarted = false;
	// 5 min for register
	private static final int TIME_REGISTER = 5;
	// 5 min for race
	private static final int TIME_RACE = 10;
	// NPC's
	private static final int START_NPC = 900103;
	private static final int STOP_NPC = 900104;
	// Skills (Frog by default)
	private static int _skill = 6201;
	// We must keep second NPC spawn for radar
	private static int[] _randspawn = null;
	// Locations
	private static final String[] LOCATIONS = {
		"Heretic catacomb enterance",
		"Dion castle bridge",
		"Floran village enterance",
		"Floran fort gate"
	};
	
	// @formatter:off
	private static final int[][] COORDS =
	{
		// x, y, z, heading
		{ 39177, 144345, -3650, 0 },
		{ 22294, 155892, -2950, 0 },
		{ 16537, 169937, -3500, 0 },
		{  7644, 150898, -2890, 0 }
	};
	private static final int[][] REWARDS =
	{
		{ 6622, 2 }, // Giant's Codex
		{ 9625, 2 }, // Giant's Codex -
		{ 9626, 2 }, // Giant's Codex -
		{ 9627, 2 }, // Giant's Codex -
		{ 9546, 5 }, // Attr stones
		{ 9547, 5 },
		{ 9548, 5 },
		{ 9549, 5 },
		{ 9550, 5 },
		{ 9551, 5 },
		{ 9574, 3 }, // Mid-Grade Life Stone: level 80
		{ 9575, 2 }, // High-Grade Life Stone: level 80
		{ 9576, 1 }, // Top-Grade Life Stone: level 80
		{ 20034,1 }  // Revita pop
	};
	// @formatter:on
	
	private Race() {
		super(Race.class.getSimpleName(), "custom/events");
		addStartNpc(START_NPC);
		addFirstTalkId(START_NPC);
		addTalkId(START_NPC);
		addStartNpc(STOP_NPC);
		addFirstTalkId(STOP_NPC);
		addTalkId(STOP_NPC);
	}
	
	@Override
	public boolean eventStart(L2PcInstance eventMaker) {
		// Don't start event if its active
		if (_isActive) {
			return false;
		}
		
		// Check Custom Table - we use custom NPC's
		if (!general().customNpcData()) {
			_log.info(getName() + ": Event can't be started, because custom npc table is disabled!");
			eventMaker.sendMessage("Event " + getName() + " can't be started because custom NPC table is disabled!");
			return false;
		}
		
		// Set Event active
		_isActive = true;
		// Spawn Manager
		_npc = recordSpawn(START_NPC, 18429, 145861, -3090, 0, false, 0);
		
		// Announce event start
		Broadcast.toAllOnlinePlayers("* Race Event started! *");
		Broadcast.toAllOnlinePlayers("Visit Event Manager in Dion village and signup, you have " + TIME_REGISTER + " min before Race Start...");
		
		// Schedule Event end
		_eventTask = ThreadPoolManager.getInstance().scheduleGeneral(() -> StartRace(), TIME_REGISTER * 60 * 1000);
		
		return true;
		
	}
	
	protected void StartRace() {
		// Abort race if no players signup
		if (_players.isEmpty()) {
			Broadcast.toAllOnlinePlayers("Race aborted, nobody signup.");
			eventStop();
			return;
		}
		// Set state
		_isRaceStarted = true;
		// Announce
		Broadcast.toAllOnlinePlayers("Race started!");
		// Get random Finish
		int location = getRandom(0, LOCATIONS.length - 1);
		_randspawn = COORDS[location];
		// And spawn NPC
		recordSpawn(STOP_NPC, _randspawn[0], _randspawn[1], _randspawn[2], _randspawn[3], false, 0);
		// Transform players and send message
		for (L2PcInstance player : _players) {
			if (player.isOnline()) {
				if (player.isInsideRadius(_npc, 500, false, false)) {
					sendMessage(player, "Race started! Go find Finish NPC as fast as you can... He is located near " + LOCATIONS[location]);
					transformPlayer(player);
					player.getRadar().addMarker(_randspawn[0], _randspawn[1], _randspawn[2]);
				} else {
					sendMessage(player, "I told you stay near me right? Distance was too high, you are excluded from race");
					_players.remove(player);
				}
			}
		}
		// Schedule timeup for Race
		_eventTask = ThreadPoolManager.getInstance().scheduleGeneral(() -> timeUp(), TIME_RACE * 60 * 1000);
	}
	
	@Override
	public boolean eventStop() {
		// Don't stop inactive event
		if (!_isActive) {
			return false;
		}
		
		// Set inactive
		_isActive = false;
		_isRaceStarted = false;
		
		// Cancel task if any
		if (_eventTask != null) {
			_eventTask.cancel(true);
			_eventTask = null;
		}
		// Untransform players
		// Teleport to event start point
		for (L2PcInstance player : _players) {
			if (player.isOnline()) {
				player.untransform();
				player.teleToLocation(_npc.getX(), _npc.getY(), _npc.getZ(), true);
			}
		}
		_players.clear();
		// Despawn NPCs
		for (L2Npc npc : _npcs) {
			npc.deleteMe();
		}
		_npcs.clear();
		// Announce event end
		Broadcast.toAllOnlinePlayers("* Race Event finished *");
		
		return true;
	}
	
	@Override
	public boolean eventBypass(L2PcInstance activeChar, String bypass) {
		if (bypass.startsWith("skill")) {
			if (_isRaceStarted) {
				activeChar.sendMessage("Race already started, you cannot change transform skill now");
			} else {
				int _number = Integer.valueOf(bypass.substring(5));
				Skill _sk = SkillData.getInstance().getSkill(_number, 1);
				if (_sk != null) {
					_skill = _number;
					activeChar.sendMessage("Transform skill set to:");
					activeChar.sendMessage(_sk.getName());
				} else {
					activeChar.sendMessage("Error while changing transform skill");
				}
			}
			
		} else if (bypass.startsWith("tele")) {
			if ((Integer.valueOf(bypass.substring(4)) > 0) && (_randspawn != null)) {
				activeChar.teleToLocation(_randspawn[0], _randspawn[1], _randspawn[2]);
			} else {
				activeChar.teleToLocation(18429, 145861, -3090);
			}
		}
		showMenu(activeChar);
		return true;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		String htmltext = event;
		QuestState st = getQuestState(player, false);
		if (st == null) {
			return null;
		}
		
		if (event.equalsIgnoreCase("transform")) {
			transformPlayer(player);
			return null;
		} else if (event.equalsIgnoreCase("untransform")) {
			player.untransform();
			return null;
		} else if (event.equalsIgnoreCase("showfinish")) {
			player.getRadar().addMarker(_randspawn[0], _randspawn[1], _randspawn[2]);
			return null;
		} else if (event.equalsIgnoreCase("signup")) {
			if (_players.contains(player)) {
				return "900103-onlist.htm";
			}
			_players.add(player);
			return "900103-signup.htm";
		} else if (event.equalsIgnoreCase("quit")) {
			player.untransform();
			if (_players.contains(player)) {
				_players.remove(player);
			}
			return "900103-quit.htm";
		} else if (event.equalsIgnoreCase("finish")) {
			if (player.isAffectedBySkill(_skill)) {
				winRace(player);
				return "900104-winner.htm";
			}
			return "900104-notrans.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		getQuestState(player, true);
		
		if (npc.getId() == START_NPC) {
			if (_isRaceStarted) {
				return START_NPC + "-started-" + isRacing(player) + ".htm";
			}
			return START_NPC + "-" + isRacing(player) + ".htm";
		} else if ((npc.getId() == STOP_NPC) && _isRaceStarted) {
			return STOP_NPC + "-" + isRacing(player) + ".htm";
		}
		return npc.getId() + ".htm";
	}
	
	private int isRacing(L2PcInstance player) {
		return _players.contains(player) ? 1 : 0;
	}
	
	private L2Npc recordSpawn(int npcId, int x, int y, int z, int heading, boolean randomOffSet, long despawnDelay) {
		final L2Npc npc = addSpawn(npcId, x, y, z, heading, randomOffSet, despawnDelay);
		_npcs.add(npc);
		return npc;
	}
	
	private void transformPlayer(L2PcInstance player) {
		if (player.isTransformed() || player.isInStance()) {
			player.untransform();
		}
		if (player.isSitting()) {
			player.standUp();
		}
		
		player.getEffectList().stopSkillEffects(true, AbnormalType.SPEED_UP);
		player.stopSkillEffects(true, 268);
		player.stopSkillEffects(true, 298); // Rabbit Spirit Totem
		SkillData.getInstance().getSkill(_skill, 1).applyEffects(player, player);
	}
	
	private void sendMessage(L2PcInstance player, String text) {
		player.sendPacket(new CreatureSay(_npc.getObjectId(), 20, _npc.getName(), text));
	}
	
	private void showMenu(L2PcInstance activeChar) {
		final NpcHtmlMessage html = new NpcHtmlMessage();
		String content = getHtm(activeChar.getHtmlPrefix(), "admin_menu.htm");
		html.setHtml(content);
		activeChar.sendPacket(html);
	}
	
	protected void timeUp() {
		Broadcast.toAllOnlinePlayers("Time up, nobody wins!");
		eventStop();
	}
	
	private void winRace(L2PcInstance player) {
		int[] _reward = REWARDS[getRandom(REWARDS.length - 1)];
		player.addItem("eventModRace", _reward[0], _reward[1], _npc, true);
		Broadcast.toAllOnlinePlayers(player.getName() + " is a winner!");
		eventStop();
	}
	
	public static void main(String[] args) {
		new Race();
	}
}