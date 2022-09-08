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
package com.l2jserver.datapack.hellbound;

import static com.l2jserver.gameserver.config.Configuration.customs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.handlers.admincommandhandlers.AdminHellbound;
import com.l2jserver.datapack.handlers.voicedcommandhandlers.Hellbound;
import com.l2jserver.datapack.hellbound.ai.Amaskari;
import com.l2jserver.datapack.hellbound.ai.Chimeras;
import com.l2jserver.datapack.hellbound.ai.DemonPrince;
import com.l2jserver.datapack.hellbound.ai.HellboundCore;
import com.l2jserver.datapack.hellbound.ai.Keltas;
import com.l2jserver.datapack.hellbound.ai.NaiaLock;
import com.l2jserver.datapack.hellbound.ai.OutpostCaptain;
import com.l2jserver.datapack.hellbound.ai.Ranku;
import com.l2jserver.datapack.hellbound.ai.Slaves;
import com.l2jserver.datapack.hellbound.ai.Typhoon;
import com.l2jserver.datapack.hellbound.ai.npc.Bernarde.Bernarde;
import com.l2jserver.datapack.hellbound.ai.npc.Budenka.Budenka;
import com.l2jserver.datapack.hellbound.ai.npc.Buron.Buron;
import com.l2jserver.datapack.hellbound.ai.npc.Deltuva.Deltuva;
import com.l2jserver.datapack.hellbound.ai.npc.Falk.Falk;
import com.l2jserver.datapack.hellbound.ai.npc.Hude.Hude;
import com.l2jserver.datapack.hellbound.ai.npc.Jude.Jude;
import com.l2jserver.datapack.hellbound.ai.npc.Kanaf.Kanaf;
import com.l2jserver.datapack.hellbound.ai.npc.Kief.Kief;
import com.l2jserver.datapack.hellbound.ai.npc.Natives.Natives;
import com.l2jserver.datapack.hellbound.ai.npc.Quarry.Quarry;
import com.l2jserver.datapack.hellbound.ai.npc.Shadai.Shadai;
import com.l2jserver.datapack.hellbound.ai.npc.Solomon.Solomon;
import com.l2jserver.datapack.hellbound.ai.npc.Warpgate.Warpgate;
import com.l2jserver.datapack.hellbound.ai.zones.AnomicFoundry.AnomicFoundry;
import com.l2jserver.datapack.hellbound.ai.zones.BaseTower.BaseTower;
import com.l2jserver.datapack.hellbound.ai.zones.TowerOfInfinitum.TowerOfInfinitum;
import com.l2jserver.datapack.hellbound.ai.zones.TowerOfNaia.TowerOfNaia;
import com.l2jserver.datapack.hellbound.ai.zones.TullyWorkshop.TullyWorkshop;
import com.l2jserver.datapack.hellbound.instancess.DemonPrinceFloor.DemonPrinceFloor;
import com.l2jserver.datapack.hellbound.instancess.RankuFloor.RankuFloor;
import com.l2jserver.datapack.hellbound.instancess.UrbanArea.UrbanArea;
import com.l2jserver.gameserver.handler.AdminCommandHandler;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.handler.VoicedCommandHandler;

/**
 * Hellbound loader.
 * @author Zoey76
 */
public final class HellboundLoader {
	
	private static final Logger LOG = LoggerFactory.getLogger(HellboundLoader.class);
	
	private static final Class<?>[] SCRIPTS = {
		// Commands
		AdminHellbound.class,
		Hellbound.class,
		// AIs
		Amaskari.class,
		Chimeras.class,
		DemonPrince.class,
		HellboundCore.class,
		Keltas.class,
		NaiaLock.class,
		OutpostCaptain.class,
		Ranku.class,
		Slaves.class,
		Typhoon.class,
		// NPCs
		Bernarde.class,
		Budenka.class,
		Buron.class,
		Deltuva.class,
		Falk.class,
		Hude.class,
		Jude.class,
		Kanaf.class,
		Kief.class,
		Natives.class,
		Quarry.class,
		Shadai.class,
		Solomon.class,
		Warpgate.class,
		// Zones
		AnomicFoundry.class,
		BaseTower.class,
		TowerOfInfinitum.class,
		TowerOfNaia.class,
		TullyWorkshop.class,
		// Instances
		DemonPrinceFloor.class,
		UrbanArea.class,
		RankuFloor.class,
	};
	
	public static void main(String[] args) {
		// Data
		HellboundPointData.getInstance();
		HellboundSpawns.getInstance();
		// Engine
		HellboundEngine.getInstance();
		int n = 0;
		for (var clazz : SCRIPTS) {
			try {
				final var script = clazz.getDeclaredConstructor().newInstance();
				if (script instanceof IAdminCommandHandler) {
					AdminCommandHandler.getInstance().registerHandler((IAdminCommandHandler) script);
				} else if (customs().hellboundStatus() && (script instanceof IVoicedCommandHandler)) {
					VoicedCommandHandler.getInstance().registerHandler((IVoicedCommandHandler) script);
				}
				n++;
			} catch (Exception ex) {
				LOG.error("Failed loading {}!", clazz.getSimpleName(), ex);
			}
		}
		LOG.info("Loaded {} Hellbound scripts.", n);
	}
}
