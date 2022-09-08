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
package com.l2jserver.datapack.gracia.ai;

import static com.l2jserver.gameserver.config.Configuration.rates;
import static com.l2jserver.gameserver.config.Configuration.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.datapack.quests.Q00692_HowtoOpposeEvil.Q00692_HowtoOpposeEvil;
import com.l2jserver.gameserver.GeoData;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.data.xml.impl.DoorData;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.instancemanager.GraciaSeedsManager;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2Territory;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2DoorInstance;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Energy Seeds AI.
 * @author Gigiikun, Lomka
 */
public class EnergySeeds extends AbstractNpcAI {
	private static final int HOWTOOPPOSEEVIL_CHANCE = 60;
	private static final int RATE = 1;
	private static final List<L2Npc> SOD_TELEPORTER_SPAWNS = new ArrayList<>();
	private static final Map<Integer, ESSpawn> SEED_SPAWNS = new HashMap<>();
	protected static final Map<L2Npc, Integer> _spawnedNpcs = new ConcurrentHashMap<>();
	private final Map<Integer, L2Territory> _spawnZoneList = new HashMap<>();
	
	private static final int TEMPORARY_TELEPORTER = 32602;
	// @formatter:off
	private static final int[] SEED_IDS =
	{
		18678, 18679, 18680, 18681, 18682, 18683
	};
	
	private static final int[][] ANNIHILATION_SUPRISE_MOB_IDS =
	{
		{ 22746, 22747, 22748, 22749 },
		{ 22754, 22755, 22756 },
		{ 22760, 22761, 22762 }
	};
	
	private static final int[] SEED_OF_DESTRUCTION_DOORS =
	{
		12240003, 12240004, 12240005, 12240006, 12240007, 12240008, 12240009,
		12240010, 12240011, 12240012, 12240013, 12240014, 12240015, 12240016,
		12240017, 12240018, 12240019, 12240020, 12240021, 12240022, 12240023,
		12240024, 12240025, 12240026, 12240027, 12240028, 12240029, 12240030,
		12240031
	};
	// @formatter:on
	
	private static final Location SOD_EXIT_POINT = new Location(-248717, 250260, 4337);
	private static final int SOD_ZONE = 60009;
	
	private enum GraciaSeeds {
		INFINITY,
		DESTRUCTION,
		ANNIHILATION_BISTAKON,
		ANNIHILATION_REPTILIKON,
		ANNIHILATION_COKRAKON
	}
	
	public EnergySeeds() {
		super(EnergySeeds.class.getSimpleName(), "gracia/AI");
		registerMobs(SEED_IDS);
		addFirstTalkId(SEED_IDS);
		addEnterZoneId(SOD_ZONE);
		loadSpawns();
		startAI();
	}
	
	protected boolean isSeedActive(GraciaSeeds seed) {
		switch (seed) {
			case INFINITY:
				return false; // TODO: Do checks here, when Seed of Infinity is implemented.
			case DESTRUCTION:
				return GraciaSeedsManager.getInstance().getSoDState() == 2;
			case ANNIHILATION_BISTAKON:
			case ANNIHILATION_REPTILIKON:
			case ANNIHILATION_COKRAKON:
				return true;
		}
		return true;
	}
	
	private void loadSpawns() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			
			File file = new File(server().getDatapackRoot(), "/data/spawnZones/gracia_energy_seeds.xml");
			if (!file.exists()) {
				_log.severe("[Energy Seeds] Missing energy_seeds.xml. The spawns wont work without it!");
				return;
			}
			int npcCounter = 1;
			Document doc = factory.newDocumentBuilder().parse(file);
			Node first = doc.getFirstChild();
			if ((first != null) && "list".equalsIgnoreCase(first.getNodeName())) {
				for (Node n = first.getFirstChild(); n != null; n = n.getNextSibling()) {
					if ("npc".equalsIgnoreCase(n.getNodeName())) {
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
							if ("spawn".equalsIgnoreCase(d.getNodeName())) {
								NamedNodeMap attrs = d.getAttributes();
								Node att = attrs.getNamedItem("npcId");
								if (att == null) {
									_log.severe("[Energy Seeds] Missing npcId in npc List, skipping");
									continue;
								}
								int npcId = Integer.parseInt(attrs.getNamedItem("npcId").getNodeValue());
								
								att = attrs.getNamedItem("flag");
								if (att == null) {
									_log.severe("[Energy Seeds] Missing flag in npc List npcId: " + npcId + ", skipping");
									continue;
								}
								int flag = Integer.parseInt(attrs.getNamedItem("flag").getNodeValue());
								
								att = attrs.getNamedItem("respawnDelay");
								if (att == null) {
									_log.severe("[Energy Seeds] Missing respawnDelay in npc List npcId: " + npcId + ", skipping");
									continue;
								}
								int respawnDelay = Integer.parseInt(attrs.getNamedItem("respawnDelay").getNodeValue());
								
								att = attrs.getNamedItem("respawnRandom");
								int respawnRandom = att != null ? Integer.parseInt(attrs.getNamedItem("respawnRandom").getNodeValue()) : 0;
								
								for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling()) {
									if ("zone".equalsIgnoreCase(cd.getNodeName())) {
										attrs = cd.getAttributes();
										Node zoneIdAtt = attrs.getNamedItem("id");
										Node countAtt = attrs.getNamedItem("count");
										if (zoneIdAtt != null && countAtt != null) {
											int zoneId = Integer.parseInt(zoneIdAtt.getNodeValue());
											int count = Integer.parseInt(countAtt.getNodeValue());
											if (count > 0) {
												GraciaSeeds seedType = null;
												switch (flag) {
													case 1:
														seedType = GraciaSeeds.DESTRUCTION;
														break;
													case 2:
														seedType = GraciaSeeds.INFINITY;
														break;
													case 3:
														seedType = GraciaSeeds.ANNIHILATION_BISTAKON;
														break;
													case 4:
														seedType = GraciaSeeds.ANNIHILATION_REPTILIKON;
														break;
													case 5:
														seedType = GraciaSeeds.ANNIHILATION_COKRAKON;
														break;
												}
												for (int i = 0; i < count; i++) {
													ESSpawn spw = new ESSpawn(npcCounter, seedType, zoneId, npcId, respawnDelay, respawnRandom);
													SEED_SPAWNS.put(npcCounter, spw);
													npcCounter++;
												}
											}
										}
									}
								}
							}
						}
					} else if ("spawnZones".equalsIgnoreCase(n.getNodeName())) {
						for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
							if ("zone".equalsIgnoreCase(d.getNodeName())) {
								NamedNodeMap attrs = d.getAttributes();
								Node att = attrs.getNamedItem("id");
								if (att == null) {
									_log.severe("[Energy Seeds] Missing id in spawnZones List, skipping");
									continue;
								}
								int id = Integer.parseInt(att.getNodeValue());
								att = attrs.getNamedItem("minZ");
								if (att == null) {
									_log.severe("[Energy Seeds] Missing minZ in spawnZones List id: " + id + ", skipping");
									continue;
								}
								int minz = Integer.parseInt(att.getNodeValue());
								att = attrs.getNamedItem("maxZ");
								if (att == null) {
									_log.severe("[Energy Seeds] Missing maxZ in spawnZones List id: " + id + ", skipping");
									continue;
								}
								int maxz = Integer.parseInt(att.getNodeValue());
								L2Territory ter = new L2Territory(id);
								
								for (Node cd = d.getFirstChild(); cd != null; cd = cd.getNextSibling()) {
									if ("point".equalsIgnoreCase(cd.getNodeName())) {
										attrs = cd.getAttributes();
										int x, y;
										att = attrs.getNamedItem("x");
										if (att != null) {
											x = Integer.parseInt(att.getNodeValue());
										} else {
											continue;
										}
										att = attrs.getNamedItem("y");
										if (att != null) {
											y = Integer.parseInt(att.getNodeValue());
										} else {
											continue;
										}
										
										ter.add(x, y, minz, maxz, 0);
									}
								}
								
								_spawnZoneList.put(id, ter);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			_log.log(Level.WARNING, "[Energy Seeds] Could not parse data.xml file: " + e.getMessage(), e);
		}
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, Skill skill, List<L2Object> targets, boolean isSummon) {
		if (!targets.contains(npc) || (skill.getId() != 5780)) {
			return super.onSkillSee(npc, caster, skill, targets, isSummon);
		}
		
		npc.deleteMe();
		
		if (_spawnedNpcs.containsKey(npc) && SEED_SPAWNS.containsKey(_spawnedNpcs.get(npc))) {
			ESSpawn spawn = SEED_SPAWNS.get(_spawnedNpcs.get(npc));
			spawn.scheduleRespawn(false);
			_spawnedNpcs.remove(npc);
			if (isSeedActive(spawn._seedId)) {
				int itemId = 0;
				
				switch (npc.getId()) {
					case 18678: // Water
						itemId = 14016;
						break;
					case 18679: // Fire
						itemId = 14015;
						break;
					case 18680: // Wind
						itemId = 14017;
						break;
					case 18681: // Earth
						itemId = 14018;
						break;
					case 18682: // Divinity
						itemId = 14020;
						break;
					case 18683: // Darkness
						itemId = 14019;
						break;
					default:
						return super.onSkillSee(npc, caster, skill, targets, isSummon);
				}
				if (getRandom(100) < 33) {
					caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_SUCCEEDED);
					caster.addItem("EnergySeed", itemId, getRandom(RATE + 1, 2 * RATE), null, true);
				} else if (((skill.getLevel() == 1) && (getRandom(100) < 15)) || ((skill.getLevel() == 2) && (getRandom(100) < 50)) || ((skill.getLevel() == 3) && (getRandom(100) < 75))) {
					caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_SUCCEEDED);
					caster.addItem("EnergySeed", itemId, getRandom(1, RATE), null, true);
				} else {
					caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_FAILED);
				}
				seedCollectEvent(caster, npc, spawn._seedId);
			}
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		if (event.equalsIgnoreCase("StartSoDAi")) {
			for (int doorId : SEED_OF_DESTRUCTION_DOORS) {
				L2DoorInstance doorInstance = DoorData.getInstance().getDoor(doorId);
				if (doorInstance != null) {
					doorInstance.openMe();
				}
			}
			stopAI(GraciaSeeds.DESTRUCTION);
			startAI(GraciaSeeds.DESTRUCTION);
		} else if (event.equalsIgnoreCase("StopSoDAi")) {
			for (int doorId : SEED_OF_DESTRUCTION_DOORS) {
				L2DoorInstance doorInstance = DoorData.getInstance().getDoor(doorId);
				if (doorInstance != null) {
					doorInstance.closeMe();
				}
			}
			for (L2PcInstance ch : ZoneManager.getInstance().getZoneById(SOD_ZONE).getPlayersInside()) {
				if (ch != null) {
					ch.teleToLocation(SOD_EXIT_POINT);
				}
			}
			stopAI(GraciaSeeds.DESTRUCTION);
		} else if (event.equalsIgnoreCase("SoDDefenceStarted")) {
			for (int doorId : SEED_OF_DESTRUCTION_DOORS) {
				L2DoorInstance doorInstance = DoorData.getInstance().getDoor(doorId);
				if (doorInstance != null) {
					doorInstance.openMe();
				}
			}
			stopAI(GraciaSeeds.DESTRUCTION);
		} else if (event.equalsIgnoreCase("DeSpawnTask")) {
			if (npc.isInCombat()) {
				startQuestTimer("DeSpawnTask", 30000, npc, null);
			} else {
				npc.deleteMe();
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		player.sendPacket(ActionFailed.STATIC_PACKET);
		return null;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon) {
		if (_spawnedNpcs.containsKey(npc) && SEED_SPAWNS.containsKey(_spawnedNpcs.get(npc))) {
			SEED_SPAWNS.get(_spawnedNpcs.get(npc)).scheduleRespawn(false);
			_spawnedNpcs.remove(npc);
		}
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onEnterZone(L2Character character, L2ZoneType zone) {
		if (character.getInstanceId() != 0) {
			return super.onEnterZone(character, zone);
		}
		
		if (character instanceof L2PcInstance) {
			switch (zone.getId()) {
				case SOD_ZONE:
					if (!isSeedActive(GraciaSeeds.DESTRUCTION) && !character.isGM()) {
						character.teleToLocation(SOD_EXIT_POINT);
					}
					break;
			}
		}
		return super.onEnterZone(character, zone);
	}
	
	public void startAI() {
		// open doors if the seed is open
		handleSoDDoors();
		// spawn teleporteds in Seed of Destruction
		spawnSoDTeleporters();
		// spawn all NPCs
		for (ESSpawn spawn : SEED_SPAWNS.values()) {
			if (isSeedActive(spawn._seedId)) {
				spawn.scheduleRespawn(true);
			}
		}
	}
	
	public void startAI(GraciaSeeds type) {
		if (type == GraciaSeeds.DESTRUCTION) {
			// open doors if the seed is open
			handleSoDDoors();
			// spawn teleporteds in Seed of Destruction
			spawnSoDTeleporters();
		}
		// spawn all NPCs
		for (ESSpawn spawn : SEED_SPAWNS.values()) {
			if (spawn._seedId == type) {
				spawn.scheduleRespawn(true);
			}
		}
	}
	
	public void stopAI(GraciaSeeds type) {
		if (type == GraciaSeeds.DESTRUCTION && SOD_TELEPORTER_SPAWNS.size() > 0) {
			for (L2Npc teleporter : SOD_TELEPORTER_SPAWNS) {
				teleporter.deleteMe();
			}
			SOD_TELEPORTER_SPAWNS.clear();
		}
		for (L2Npc seed : _spawnedNpcs.keySet()) {
			if (type == SEED_SPAWNS.get(_spawnedNpcs.get(seed))._seedId) {
				seed.deleteMe();
			}
		}
		for (ESSpawn spawn : SEED_SPAWNS.values()) {
			if (spawn._seedId == type) {
				spawn.cancelRespawnSchedule();
			}
		}
	}
	
	public void seedCollectEvent(L2PcInstance player, L2Npc seedEnergy, GraciaSeeds seedType) {
		if (player == null) {
			return;
		}
		QuestState st = player.getQuestState(Q00692_HowtoOpposeEvil.class.getSimpleName());
		switch (seedType) {
			case INFINITY:
				if ((st != null) && st.isCond(3)) {
					handleQuestDrop(player, 13798);
				}
				break;
			case DESTRUCTION:
				if ((st != null) && st.isCond(3)) {
					handleQuestDrop(player, 13867);
				}
				break;
			case ANNIHILATION_BISTAKON:
				if ((st != null) && st.isCond(3)) {
					handleQuestDrop(player, 15535);
				}
				if (getRandom(100) < 50) {
					L2MonsterInstance mob = spawnSupriseMob(seedEnergy, ANNIHILATION_SUPRISE_MOB_IDS[0][getRandom(ANNIHILATION_SUPRISE_MOB_IDS[0].length)]);
					mob.setRunning();
					mob.addDamageHate(player, 0, 999);
					mob.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
				}
				break;
			case ANNIHILATION_REPTILIKON:
				if ((st != null) && st.isCond(3)) {
					handleQuestDrop(player, 15535);
				}
				if (getRandom(100) < 50) {
					L2MonsterInstance mob = spawnSupriseMob(seedEnergy, ANNIHILATION_SUPRISE_MOB_IDS[1][getRandom(ANNIHILATION_SUPRISE_MOB_IDS[1].length)]);
					mob.setRunning();
					mob.addDamageHate(player, 0, 999);
					mob.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
				}
				break;
			case ANNIHILATION_COKRAKON:
				if ((st != null) && st.isCond(3)) {
					handleQuestDrop(player, 15535);
				}
				if (getRandom(100) < 50) {
					L2MonsterInstance mob = spawnSupriseMob(seedEnergy, ANNIHILATION_SUPRISE_MOB_IDS[2][getRandom(ANNIHILATION_SUPRISE_MOB_IDS[2].length)]);
					mob.setRunning();
					mob.addDamageHate(player, 0, 999);
					mob.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
				}
				break;
		}
	}
	
	private L2MonsterInstance spawnSupriseMob(L2Npc energy, int npcId) {
		L2NpcTemplate surpriseMobTemplate = NpcData.getInstance().getTemplate(npcId);
		L2MonsterInstance monster = new L2MonsterInstance(surpriseMobTemplate);
		monster.setCurrentHpMp(monster.getMaxHp(), monster.getMaxMp());
		monster.setHeading(energy.getHeading());
		monster.setInstanceId(energy.getInstanceId());
		monster.setShowSummonAnimation(true);
		monster.spawnMe(energy.getX(), energy.getY(), energy.getZ());
		startQuestTimer("DeSpawnTask", 30000, monster, null);
		return monster;
	}
	
	private void handleQuestDrop(L2PcInstance player, int itemId) {
		double chance = HOWTOOPPOSEEVIL_CHANCE * rates().getQuestDropChanceMultiplier();
		int numItems = (int) (chance / 100);
		chance = chance % 100;
		if (getRandom(100) < chance) {
			numItems++;
		}
		numItems *= rates().getQuestDropAmountMultiplier();
		if (numItems > 0) {
			giveItems(player, itemId, numItems);
			playSound(player, Sound.ITEMSOUND_QUEST_ITEMGET);
		}
	}
	
	private void handleSoDDoors() {
		if (isSeedActive(GraciaSeeds.DESTRUCTION)) {
			for (int doorId : SEED_OF_DESTRUCTION_DOORS) {
				L2DoorInstance doorInstance = DoorData.getInstance().getDoor(doorId);
				if (doorInstance != null) {
					doorInstance.openMe();
				}
			}
		}
	}
	
	private void spawnSoDTeleporters() {
		// Seed of Destruction - Temporary Teleporters
		SOD_TELEPORTER_SPAWNS.add(addSpawn(TEMPORARY_TELEPORTER, new Location(-245790, 220320, -12104), false, 0));
		SOD_TELEPORTER_SPAWNS.add(addSpawn(TEMPORARY_TELEPORTER, new Location(-249770, 207300, -11952), false, 0));
	}
	
	private class ESSpawn {
		protected final int _spawnId;
		protected final GraciaSeeds _seedId;
		protected final int _npcId;
		protected final int _zoneId;
		protected final int _respawn;
		protected final int _respawnRandom;
		protected ScheduledFuture<?> _respawnScheduleTask = null;
		
		public ESSpawn(int spawnId, GraciaSeeds seedId, int zoneId, int npcId, int respawn, int respawnRandom) {
			_spawnId = spawnId;
			_seedId = seedId;
			_zoneId = zoneId;
			_npcId = npcId;
			_respawn = respawn;
			_respawnRandom = respawnRandom;
		}
		
		public void scheduleRespawn(boolean instantSpawn) {
			int waitTime = instantSpawn ? 0 : (_respawn + Math.max(-_respawn, getRandom(-_respawnRandom, _respawnRandom))) * 60 * 1000;
			_respawnScheduleTask = ThreadPoolManager.getInstance().scheduleGeneral(() -> {
				// if the AI is inactive, do not spawn the NPC
				if (isSeedActive(_seedId)) {
					// get a random NPC and random location within zone
					Integer spawnId = _spawnId; // the map uses "Integer", not "int"
					Location randomLocation = null;
					boolean didFindSpawn = false;
					while (!didFindSpawn) {
						Location tempRandomLocation = _spawnZoneList.get(_zoneId).getRandomPoint();
						tempRandomLocation.setZ(GeoData.getInstance().getSpawnHeight(tempRandomLocation));
						int locX = tempRandomLocation.getX();
						int locY = tempRandomLocation.getY();
						int locZ = tempRandomLocation.getZ();
						if (GeoData.getInstance().canMove(locX, locY, locZ, locX - 245, locY - 245, locZ, 0)//
							|| GeoData.getInstance().canMove(locX, locY, locZ, locX + 245, locY + 245, locZ, 0)//
							|| GeoData.getInstance().canMove(locX, locY, locZ, locX - 245, locY + 245, locZ, 0)//
							|| GeoData.getInstance().canMove(locX, locY, locZ, locX + 245, locY - 245, locZ, 0)//
							|| GeoData.getInstance().canMove(locX, locY, locZ, locX - 350, locY, locZ, 0)//
							|| GeoData.getInstance().canMove(locX, locY, locZ, locX, locY - 350, locZ, 0)//
							|| GeoData.getInstance().canMove(locX, locY, locZ, locX + 350, locY, locZ, 0)//
							|| GeoData.getInstance().canMove(locX, locY, locZ, locX, locY + 350, locZ, 0)//
						) {
							didFindSpawn = true;
							randomLocation = tempRandomLocation;
						}
					}
					_spawnedNpcs.put(addSpawn(_npcId, randomLocation, false, 0), spawnId);
				}
			}, waitTime);
		}
		
		public void cancelRespawnSchedule() {
			if (_respawnScheduleTask != null && !_respawnScheduleTask.isDone() && !_respawnScheduleTask.isCancelled()) {
				_respawnScheduleTask.cancel(true);
			}
		}
	}
}