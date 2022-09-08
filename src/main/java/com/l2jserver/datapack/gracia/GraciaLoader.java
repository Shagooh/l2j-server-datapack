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
package com.l2jserver.datapack.gracia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.gracia.ai.DraconianTroops;
import com.l2jserver.datapack.gracia.ai.EnergySeeds;
import com.l2jserver.datapack.gracia.ai.Lindvior;
import com.l2jserver.datapack.gracia.ai.Maguen;
import com.l2jserver.datapack.gracia.ai.StarStones;
import com.l2jserver.datapack.gracia.ai.SeedOfAnnihilation.SeedOfAnnihilation;
import com.l2jserver.datapack.gracia.ai.SeedOfDestruction.Defence;
import com.l2jserver.datapack.gracia.ai.npc.Allenos.Allenos;
import com.l2jserver.datapack.gracia.ai.npc.FortuneTelling.FortuneTelling;
import com.l2jserver.datapack.gracia.ai.npc.GeneralDilios.GeneralDilios;
import com.l2jserver.datapack.gracia.ai.npc.Lekon.Lekon;
import com.l2jserver.datapack.gracia.ai.npc.Nemo.Nemo;
import com.l2jserver.datapack.gracia.ai.npc.Nottingale.Nottingale;
import com.l2jserver.datapack.gracia.ai.npc.Seyo.Seyo;
import com.l2jserver.datapack.gracia.ai.npc.TemporaryTeleporter.TemporaryTeleporter;
import com.l2jserver.datapack.gracia.ai.npc.ZealotOfShilen.ZealotOfShilen;
import com.l2jserver.datapack.gracia.instances.SecretArea.SecretArea;
import com.l2jserver.datapack.gracia.instances.SeedOfDestruction.Stage1;
import com.l2jserver.datapack.gracia.instances.SeedOfDestruction.MountedTroops.ChamblainsMountedTroop;
import com.l2jserver.datapack.gracia.instances.SeedOfDestruction.MountedTroops.GreatWarriorsMountedTroop;
import com.l2jserver.datapack.gracia.instances.SeedOfDestruction.MountedTroops.SoldiersMountedTroop;
import com.l2jserver.datapack.gracia.instances.SeedOfDestruction.MountedTroops.WarriorsMountedTroop;
import com.l2jserver.datapack.gracia.instances.SeedOfInfinity.HallOfSuffering.HallOfSuffering;
import com.l2jserver.datapack.gracia.vehicles.AirShipGludioGracia.AirShipGludioGracia;
import com.l2jserver.datapack.gracia.vehicles.KeucereusNorthController.KeucereusNorthController;
import com.l2jserver.datapack.gracia.vehicles.KeucereusSouthController.KeucereusSouthController;
import com.l2jserver.datapack.gracia.vehicles.SoDController.SoDController;
import com.l2jserver.datapack.gracia.vehicles.SoIController.SoIController;

/**
 * Gracia class-loader.
 * @author Pandragon
 */
public final class GraciaLoader {
	
	private static final Logger LOG = LoggerFactory.getLogger(GraciaLoader.class);
	
	private static final Class<?>[] SCRIPTS = {
		// AIs
		DraconianTroops.class,
		EnergySeeds.class,
		Lindvior.class,
		Maguen.class,
		StarStones.class,
		// NPCs
		Allenos.class,
		FortuneTelling.class,
		GeneralDilios.class,
		Lekon.class,
		Nemo.class,
		Nottingale.class,
		Seyo.class,
		TemporaryTeleporter.class,
		ZealotOfShilen.class,
		// Seed of Annihilation
		SeedOfAnnihilation.class,
		// Instances
		Defence.class,
		SecretArea.class,
		Stage1.class,
		ChamblainsMountedTroop.class,
		GreatWarriorsMountedTroop.class,
		SoldiersMountedTroop.class,
		WarriorsMountedTroop.class,
		// Seed of Destruction
		HallOfSuffering.class, // Seed of Infinity
		// Vehicles
		AirShipGludioGracia.class,
		KeucereusNorthController.class,
		KeucereusSouthController.class,
		SoDController.class,
		SoIController.class,
	};
	
	public static void main(String[] args) {
		int n = 0;
		for (var script : SCRIPTS) {
			try {
				script.getDeclaredConstructor().newInstance();
				n++;
			} catch (Exception ex) {
				LOG.error("Failed loading {}!", script.getSimpleName(), ex);
			}
		}
		LOG.info("Loaded {} Gracia scripts.", n);
	}
}
