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
package com.l2jserver.datapack.instances;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.instances.CastleDungeon.CastleDungeon;
import com.l2jserver.datapack.instances.CavernOfThePirateCaptain.CavernOfThePirateCaptain;
import com.l2jserver.datapack.instances.ChambersOfDelusion.ChamberOfDelusionEast;
import com.l2jserver.datapack.instances.ChambersOfDelusion.ChamberOfDelusionNorth;
import com.l2jserver.datapack.instances.ChambersOfDelusion.ChamberOfDelusionSouth;
import com.l2jserver.datapack.instances.ChambersOfDelusion.ChamberOfDelusionSquare;
import com.l2jserver.datapack.instances.ChambersOfDelusion.ChamberOfDelusionTower;
import com.l2jserver.datapack.instances.ChambersOfDelusion.ChamberOfDelusionWest;
import com.l2jserver.datapack.instances.CrystalCaverns.CrystalCaverns;
import com.l2jserver.datapack.instances.DarkCloudMansion.DarkCloudMansion;
import com.l2jserver.datapack.instances.DisciplesNecropolisPast.DisciplesNecropolisPast;
import com.l2jserver.datapack.instances.ElcadiasTent.ElcadiasTent;
import com.l2jserver.datapack.instances.FinalEmperialTomb.FinalEmperialTomb;
import com.l2jserver.datapack.instances.HideoutOfTheDawn.HideoutOfTheDawn;
import com.l2jserver.datapack.instances.IceQueensCastle.IceQueensCastle;
import com.l2jserver.datapack.instances.IceQueensCastleNormalBattle.IceQueensCastleNormalBattle;
import com.l2jserver.datapack.instances.JiniaGuildHideout1.JiniaGuildHideout1;
import com.l2jserver.datapack.instances.JiniaGuildHideout2.JiniaGuildHideout2;
import com.l2jserver.datapack.instances.JiniaGuildHideout3.JiniaGuildHideout3;
import com.l2jserver.datapack.instances.JiniaGuildHideout4.JiniaGuildHideout4;
import com.l2jserver.datapack.instances.Kamaloka.Kamaloka;
import com.l2jserver.datapack.instances.LibraryOfSages.LibraryOfSages;
import com.l2jserver.datapack.instances.MithrilMine.MithrilMine;
import com.l2jserver.datapack.instances.MonasteryOfSilence1.MonasteryOfSilence1;
import com.l2jserver.datapack.instances.NornilsGarden.NornilsGarden;
import com.l2jserver.datapack.instances.NornilsGardenQuest.NornilsGardenQuest;
import com.l2jserver.datapack.instances.PailakaDevilsLegacy.PailakaDevilsLegacy;
import com.l2jserver.datapack.instances.PailakaSongOfIceAndFire.PailakaSongOfIceAndFire;
import com.l2jserver.datapack.instances.SanctumOftheLordsOfDawn.SanctumOftheLordsOfDawn;
import com.l2jserver.datapack.instances.SecretAreaInTheKeucereusFortress1.SecretAreaInTheKeucereusFortress1;

/**
 * Instance class-loader.
 * @author FallenAngel
 */
public final class InstanceLoader {
	private static final Logger LOG = LoggerFactory.getLogger(InstanceLoader.class);
	
	private static final Class<?>[] SCRIPTS = {
		CastleDungeon.class,
		CavernOfThePirateCaptain.class,
		CrystalCaverns.class,
		DarkCloudMansion.class,
		DisciplesNecropolisPast.class,
		ElcadiasTent.class,
		FinalEmperialTomb.class,
		HideoutOfTheDawn.class,
		ChamberOfDelusionEast.class,
		ChamberOfDelusionNorth.class,
		ChamberOfDelusionSouth.class,
		ChamberOfDelusionSquare.class,
		ChamberOfDelusionTower.class,
		ChamberOfDelusionWest.class,
		IceQueensCastle.class,
		IceQueensCastleNormalBattle.class,
		JiniaGuildHideout1.class,
		JiniaGuildHideout2.class,
		JiniaGuildHideout3.class,
		JiniaGuildHideout4.class,
		Kamaloka.class,
		LibraryOfSages.class,
		MithrilMine.class,
		MonasteryOfSilence1.class,
		NornilsGarden.class,
		NornilsGardenQuest.class,
		PailakaDevilsLegacy.class,
		PailakaSongOfIceAndFire.class,
		SanctumOftheLordsOfDawn.class,
		SecretAreaInTheKeucereusFortress1.class,
	};
	
	public static void main(String[] args) {
		int n = 0;
		for (var script : SCRIPTS) {
			try {
				script.getDeclaredConstructor().newInstance();
				n++;
			} catch (Exception ex) {
				LOG.warn("Failed loading {}!", script.getSimpleName(), ex);
			}
		}
		LOG.info("Loaded {} instnaces.", n);
	}
}
