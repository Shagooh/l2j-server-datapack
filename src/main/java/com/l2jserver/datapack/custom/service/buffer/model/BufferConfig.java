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
package com.l2jserver.datapack.custom.service.buffer.model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.l2jserver.datapack.custom.service.buffer.BufferService;
import com.l2jserver.datapack.custom.service.buffer.model.entity.AbstractBuffer;
import com.l2jserver.datapack.custom.service.buffer.model.entity.NpcBuffer;
import com.l2jserver.datapack.custom.service.buffer.model.entity.VoicedBuffer;
import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Buffer configuration.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class BufferConfig {
	
	private static final Logger LOG = LoggerFactory.getLogger(BufferConfig.class);
	
	private GlobalConfig global;
	
	private VoicedBuffer voiced;
	
	private Map<Integer, NpcBuffer> npcs;
	
	public BufferConfig() {
		try {
			final var gson = new Gson();
			
			final var jsonPath = Paths.get(Configuration.server().getDatapackRoot().getAbsolutePath(), "data", BufferService.SCRIPT_PATH.toString(), "json");
			
			global = gson.fromJson(Files.newBufferedReader(jsonPath.resolve("global.json")), GlobalConfig.class);
			voiced = gson.fromJson(Files.newBufferedReader(jsonPath.resolve("voiced.json")), VoicedBuffer.class);
			npcs = new HashMap<>();
			
			final var npcsDir = Paths.get(jsonPath.toString(), "npcs");
			try (var dirStream = Files.newDirectoryStream(npcsDir, "*.json")) {
				for (var entry : dirStream) {
					if (!Files.isRegularFile(entry)) {
						continue;
					}
					
					final var npc = gson.fromJson(Files.newBufferedReader(entry), NpcBuffer.class);
					npcs.put(npc.getId(), npc);
				}
			}
			
			global.afterDeserialize(this);
			voiced.afterDeserialize(this);
			for (var npc : npcs.values()) {
				npc.afterDeserialize(this);
			}
		} catch (Exception ex) {
			LOG.error("Error loading buffer configuration!", ex);
		}
	}
	
	public AbstractBuffer determineBuffer(L2Npc npc, L2PcInstance player) {
		if (npc == null) {
			if (!Configuration.bufferService().getVoicedEnable() || ((Configuration.bufferService().getVoicedRequiredItem() > 0) && (player.getInventory().getAllItemsByItemId(Configuration.bufferService().getVoicedRequiredItem()) == null))) {
				return null;
			}
			return voiced;
		}
		return npcs.get(npc.getId());
	}
	
	public void registerNpcs(BufferService scriptInstance) {
		for (var npc : npcs.values()) {
			if (npc.getDirectFirstTalk()) {
				scriptInstance.addFirstTalkId(npc.getId());
			}
			scriptInstance.addStartNpc(npc.getId());
			scriptInstance.addTalkId(npc.getId());
		}
	}
	
	public GlobalConfig getGlobal() {
		return global;
	}
	
	public final VoicedBuffer getVoiced() {
		return voiced;
	}
	
	public final Map<Integer, NpcBuffer> getNpcs() {
		return npcs;
	}
}