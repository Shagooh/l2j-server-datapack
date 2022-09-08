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
package com.l2jserver.datapack.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.ai.fantasy_isle.HandysBlockCheckerEvent;
import com.l2jserver.datapack.ai.fantasy_isle.MC_Show;
import com.l2jserver.datapack.ai.group_template.AltarsOfSacrifice;
import com.l2jserver.datapack.ai.group_template.BeastFarm;
import com.l2jserver.datapack.ai.group_template.DenOfEvil;
import com.l2jserver.datapack.ai.group_template.DragonValley;
import com.l2jserver.datapack.ai.group_template.FairyTrees;
import com.l2jserver.datapack.ai.group_template.FeedableBeasts;
import com.l2jserver.datapack.ai.group_template.FleeMonsters;
import com.l2jserver.datapack.ai.group_template.FrozenLabyrinth;
import com.l2jserver.datapack.ai.group_template.GiantsCave;
import com.l2jserver.datapack.ai.group_template.HotSprings;
import com.l2jserver.datapack.ai.group_template.IsleOfPrayer;
import com.l2jserver.datapack.ai.group_template.LairOfAntharas;
import com.l2jserver.datapack.ai.group_template.MinionSpawnManager;
import com.l2jserver.datapack.ai.group_template.MonasteryOfSilence;
import com.l2jserver.datapack.ai.group_template.NonLethalableNpcs;
import com.l2jserver.datapack.ai.group_template.NonTalkingNpcs;
import com.l2jserver.datapack.ai.group_template.PavelArchaic;
import com.l2jserver.datapack.ai.group_template.PlainsOfDion;
import com.l2jserver.datapack.ai.group_template.PlainsOfLizardman;
import com.l2jserver.datapack.ai.group_template.PolymorphingAngel;
import com.l2jserver.datapack.ai.group_template.PolymorphingOnAttack;
import com.l2jserver.datapack.ai.group_template.PrimevalIsle;
import com.l2jserver.datapack.ai.group_template.PrisonGuards;
import com.l2jserver.datapack.ai.group_template.RaidBossCancel;
import com.l2jserver.datapack.ai.group_template.RandomSpawn;
import com.l2jserver.datapack.ai.group_template.RangeGuard;
import com.l2jserver.datapack.ai.group_template.Remnants;
import com.l2jserver.datapack.ai.group_template.Sandstorms;
import com.l2jserver.datapack.ai.group_template.SeeThroughSilentMove;
import com.l2jserver.datapack.ai.group_template.SelMahumDrill;
import com.l2jserver.datapack.ai.group_template.SelMahumSquad;
import com.l2jserver.datapack.ai.group_template.SilentValley;
import com.l2jserver.datapack.ai.group_template.StakatoNest;
import com.l2jserver.datapack.ai.group_template.SummonPc;
import com.l2jserver.datapack.ai.group_template.TreasureChest;
import com.l2jserver.datapack.ai.group_template.TurekOrcs;
import com.l2jserver.datapack.ai.group_template.VarkaKetra;
import com.l2jserver.datapack.ai.group_template.WarriorFishingBlock;
import com.l2jserver.datapack.ai.individual.Anais;
import com.l2jserver.datapack.ai.individual.Ballista;
import com.l2jserver.datapack.ai.individual.Beleth;
import com.l2jserver.datapack.ai.individual.BlackdaggerWing;
import com.l2jserver.datapack.ai.individual.BleedingFly;
import com.l2jserver.datapack.ai.individual.BloodyBerserker;
import com.l2jserver.datapack.ai.individual.BloodyKarik;
import com.l2jserver.datapack.ai.individual.BloodyKarinness;
import com.l2jserver.datapack.ai.individual.Core;
import com.l2jserver.datapack.ai.individual.CrimsonHatuOtis;
import com.l2jserver.datapack.ai.individual.DarkWaterDragon;
import com.l2jserver.datapack.ai.individual.DivineBeast;
import com.l2jserver.datapack.ai.individual.DrakosWarrior;
import com.l2jserver.datapack.ai.individual.DustRider;
import com.l2jserver.datapack.ai.individual.EmeraldHorn;
import com.l2jserver.datapack.ai.individual.Epidos;
import com.l2jserver.datapack.ai.individual.EvasGiftBox;
import com.l2jserver.datapack.ai.individual.FrightenedRagnaOrc;
import com.l2jserver.datapack.ai.individual.GiganticGolem;
import com.l2jserver.datapack.ai.individual.Gordon;
import com.l2jserver.datapack.ai.individual.GraveRobbers;
import com.l2jserver.datapack.ai.individual.Knoriks;
import com.l2jserver.datapack.ai.individual.MuscleBomber;
import com.l2jserver.datapack.ai.individual.NecromancerOfTheValley;
import com.l2jserver.datapack.ai.individual.Orfen;
import com.l2jserver.datapack.ai.individual.QueenAnt;
import com.l2jserver.datapack.ai.individual.QueenShyeed;
import com.l2jserver.datapack.ai.individual.RagnaOrcCommander;
import com.l2jserver.datapack.ai.individual.RagnaOrcHero;
import com.l2jserver.datapack.ai.individual.RagnaOrcSeer;
import com.l2jserver.datapack.ai.individual.ShadowSummoner;
import com.l2jserver.datapack.ai.individual.SinEater;
import com.l2jserver.datapack.ai.individual.SinWardens;
import com.l2jserver.datapack.ai.individual.Valakas;
import com.l2jserver.datapack.ai.individual.Antharas.Antharas;
import com.l2jserver.datapack.ai.individual.Baium.Baium;
import com.l2jserver.datapack.ai.individual.Sailren.Sailren;
import com.l2jserver.datapack.ai.individual.Venom.Venom;
import com.l2jserver.datapack.ai.npc.Abercrombie.Abercrombie;
import com.l2jserver.datapack.ai.npc.Alarm.Alarm;
import com.l2jserver.datapack.ai.npc.Alexandria.Alexandria;
import com.l2jserver.datapack.ai.npc.ArenaManager.ArenaManager;
import com.l2jserver.datapack.ai.npc.Asamah.Asamah;
import com.l2jserver.datapack.ai.npc.AvantGarde.AvantGarde;
import com.l2jserver.datapack.ai.npc.BlackJudge.BlackJudge;
import com.l2jserver.datapack.ai.npc.BlackMarketeerOfMammon.BlackMarketeerOfMammon;
import com.l2jserver.datapack.ai.npc.CastleAmbassador.CastleAmbassador;
import com.l2jserver.datapack.ai.npc.CastleBlacksmith.CastleBlacksmith;
import com.l2jserver.datapack.ai.npc.CastleChamberlain.CastleChamberlain;
import com.l2jserver.datapack.ai.npc.CastleCourtMagician.CastleCourtMagician;
import com.l2jserver.datapack.ai.npc.CastleMercenaryManager.CastleMercenaryManager;
import com.l2jserver.datapack.ai.npc.CastleSiegeManager.CastleSiegeManager;
import com.l2jserver.datapack.ai.npc.CastleTeleporter.CastleTeleporter;
import com.l2jserver.datapack.ai.npc.CastleWarehouse.CastleWarehouse;
import com.l2jserver.datapack.ai.npc.ClanTrader.ClanTrader;
import com.l2jserver.datapack.ai.npc.ClassMaster.ClassMaster;
import com.l2jserver.datapack.ai.npc.Dorian.Dorian;
import com.l2jserver.datapack.ai.npc.DragonVortex.DragonVortex;
import com.l2jserver.datapack.ai.npc.EchoCrystals.EchoCrystals;
import com.l2jserver.datapack.ai.npc.FameManager.FameManager;
import com.l2jserver.datapack.ai.npc.Fisherman.Fisherman;
import com.l2jserver.datapack.ai.npc.ForgeOfTheGods.ForgeOfTheGods;
import com.l2jserver.datapack.ai.npc.ForgeOfTheGods.Rooney;
import com.l2jserver.datapack.ai.npc.ForgeOfTheGods.TarBeetle;
import com.l2jserver.datapack.ai.npc.FortressArcherCaptain.FortressArcherCaptain;
import com.l2jserver.datapack.ai.npc.FortressSiegeManager.FortressSiegeManager;
import com.l2jserver.datapack.ai.npc.FreyasSteward.FreyasSteward;
import com.l2jserver.datapack.ai.npc.Jinia.Jinia;
import com.l2jserver.datapack.ai.npc.Katenar.Katenar;
import com.l2jserver.datapack.ai.npc.KetraOrcSupport.KetraOrcSupport;
import com.l2jserver.datapack.ai.npc.Kier.Kier;
import com.l2jserver.datapack.ai.npc.ManorManager.ManorManager;
import com.l2jserver.datapack.ai.npc.MercenaryCaptain.MercenaryCaptain;
import com.l2jserver.datapack.ai.npc.Minigame.Minigame;
import com.l2jserver.datapack.ai.npc.MonumentOfHeroes.MonumentOfHeroes;
import com.l2jserver.datapack.ai.npc.NevitsHerald.NevitsHerald;
import com.l2jserver.datapack.ai.npc.NpcBuffers.NpcBuffers;
import com.l2jserver.datapack.ai.npc.NpcBuffers.impl.CabaleBuffer;
import com.l2jserver.datapack.ai.npc.PriestOfBlessing.PriestOfBlessing;
import com.l2jserver.datapack.ai.npc.Rafforty.Rafforty;
import com.l2jserver.datapack.ai.npc.Rignos.Rignos;
import com.l2jserver.datapack.ai.npc.Selina.Selina;
import com.l2jserver.datapack.ai.npc.Sirra.Sirra;
import com.l2jserver.datapack.ai.npc.SubclassCertification.SubclassCertification;
import com.l2jserver.datapack.ai.npc.Summons.MerchantGolem.GolemTrader;
import com.l2jserver.datapack.ai.npc.Summons.Pets.BabyPets;
import com.l2jserver.datapack.ai.npc.Summons.Pets.ImprovedBabyPets;
import com.l2jserver.datapack.ai.npc.Summons.Servitors.Servitors;
import com.l2jserver.datapack.ai.npc.SupportUnitCaptain.SupportUnitCaptain;
import com.l2jserver.datapack.ai.npc.SymbolMaker.SymbolMaker;
import com.l2jserver.datapack.ai.npc.Teleports.Asher.Asher;
import com.l2jserver.datapack.ai.npc.Teleports.CrumaTower.CrumaTower;
import com.l2jserver.datapack.ai.npc.Teleports.DelusionTeleport.DelusionTeleport;
import com.l2jserver.datapack.ai.npc.Teleports.ElrokiTeleporters.ElrokiTeleporters;
import com.l2jserver.datapack.ai.npc.Teleports.GatekeeperSpirit.GatekeeperSpirit;
import com.l2jserver.datapack.ai.npc.Teleports.GhostChamberlainOfElmoreden.GhostChamberlainOfElmoreden;
import com.l2jserver.datapack.ai.npc.Teleports.GrandBossTeleporters.GrandBossTeleporters;
import com.l2jserver.datapack.ai.npc.Teleports.HuntingGroundsTeleport.HuntingGroundsTeleport;
import com.l2jserver.datapack.ai.npc.Teleports.Klemis.Klemis;
import com.l2jserver.datapack.ai.npc.Teleports.MithrilMinesTeleporter.MithrilMinesTeleporter;
import com.l2jserver.datapack.ai.npc.Teleports.NewbieGuide.NewbieGuide;
import com.l2jserver.datapack.ai.npc.Teleports.NoblesseTeleport.NoblesseTeleport;
import com.l2jserver.datapack.ai.npc.Teleports.OracleTeleport.OracleTeleport;
import com.l2jserver.datapack.ai.npc.Teleports.PaganTeleporters.PaganTeleporters;
import com.l2jserver.datapack.ai.npc.Teleports.SeparatedSoul.SeparatedSoul;
import com.l2jserver.datapack.ai.npc.Teleports.StakatoNestTeleporter.StakatoNestTeleporter;
import com.l2jserver.datapack.ai.npc.Teleports.SteelCitadelTeleport.SteelCitadelTeleport;
import com.l2jserver.datapack.ai.npc.Teleports.StrongholdsTeleports.StrongholdsTeleports;
import com.l2jserver.datapack.ai.npc.Teleports.Survivor.Survivor;
import com.l2jserver.datapack.ai.npc.Teleports.TeleportToFantasy.TeleportToFantasy;
import com.l2jserver.datapack.ai.npc.Teleports.TeleportToRaceTrack.TeleportToRaceTrack;
import com.l2jserver.datapack.ai.npc.Teleports.TeleportToUndergroundColiseum.TeleportToUndergroundColiseum;
import com.l2jserver.datapack.ai.npc.Teleports.TeleportWithCharm.TeleportWithCharm;
import com.l2jserver.datapack.ai.npc.Teleports.ToIVortex.ToIVortex;
import com.l2jserver.datapack.ai.npc.TerritoryManagers.TerritoryManagers;
import com.l2jserver.datapack.ai.npc.TownPets.TownPets;
import com.l2jserver.datapack.ai.npc.Trainers.HealerTrainer.HealerTrainer;
import com.l2jserver.datapack.ai.npc.Tunatun.Tunatun;
import com.l2jserver.datapack.ai.npc.VarkaSilenosSupport.VarkaSilenosSupport;
import com.l2jserver.datapack.ai.npc.VillageMasters.FirstClassTransferTalk.FirstClassTransferTalk;
import com.l2jserver.datapack.ai.npc.WeaverOlf.WeaverOlf;
import com.l2jserver.datapack.ai.npc.WyvernManager.WyvernManager;
import com.l2jserver.datapack.village_master.Alliance.Alliance;
import com.l2jserver.datapack.village_master.Clan.Clan;
import com.l2jserver.datapack.village_master.DarkElfChange1.DarkElfChange1;
import com.l2jserver.datapack.village_master.DarkElfChange2.DarkElfChange2;
import com.l2jserver.datapack.village_master.DwarfBlacksmithChange1.DwarfBlacksmithChange1;
import com.l2jserver.datapack.village_master.DwarfBlacksmithChange2.DwarfBlacksmithChange2;
import com.l2jserver.datapack.village_master.DwarfWarehouseChange1.DwarfWarehouseChange1;
import com.l2jserver.datapack.village_master.DwarfWarehouseChange2.DwarfWarehouseChange2;
import com.l2jserver.datapack.village_master.ElfHumanClericChange2.ElfHumanClericChange2;
import com.l2jserver.datapack.village_master.ElfHumanFighterChange1.ElfHumanFighterChange1;
import com.l2jserver.datapack.village_master.ElfHumanFighterChange2.ElfHumanFighterChange2;
import com.l2jserver.datapack.village_master.ElfHumanWizardChange1.ElfHumanWizardChange1;
import com.l2jserver.datapack.village_master.ElfHumanWizardChange2.ElfHumanWizardChange2;
import com.l2jserver.datapack.village_master.KamaelChange1.KamaelChange1;
import com.l2jserver.datapack.village_master.KamaelChange2.KamaelChange2;
import com.l2jserver.datapack.village_master.OrcChange1.OrcChange1;
import com.l2jserver.datapack.village_master.OrcChange2.OrcChange2;

/**
 * AI loader.
 * @author Zoey76
 * @version 2.6.2.0
 */
public class AILoader {
	
	private static final Logger LOG = LoggerFactory.getLogger(AILoader.class);
	
	private static final Class<?>[] SCRIPTS = {
		// NPC
		Abercrombie.class,
		Alarm.class,
		Alexandria.class,
		ArenaManager.class,
		Asamah.class,
		AvantGarde.class,
		BlackJudge.class,
		BlackMarketeerOfMammon.class,
		CastleAmbassador.class,
		CastleBlacksmith.class,
		CastleCourtMagician.class,
		CastleChamberlain.class,
		CastleMercenaryManager.class,
		CastleSiegeManager.class,
		CastleTeleporter.class,
		CastleWarehouse.class,
		ClanTrader.class,
		ClassMaster.class,
		Dorian.class,
		DragonVortex.class,
		EchoCrystals.class,
		FameManager.class,
		Fisherman.class,
		ForgeOfTheGods.class,
		Rooney.class,
		TarBeetle.class,
		FortressArcherCaptain.class,
		FortressSiegeManager.class,
		FreyasSteward.class,
		Jinia.class,
		Katenar.class,
		KetraOrcSupport.class,
		Kier.class,
		ManorManager.class,
		MercenaryCaptain.class,
		Minigame.class,
		MonumentOfHeroes.class,
		NevitsHerald.class,
		NpcBuffers.class,
		CabaleBuffer.class,
		PriestOfBlessing.class,
		Rignos.class,
		Rafforty.class,
		Selina.class,
		Sirra.class,
		SubclassCertification.class,
		GolemTrader.class,
		BabyPets.class,
		ImprovedBabyPets.class,
		Servitors.class,
		SupportUnitCaptain.class,
		SymbolMaker.class,
		Asher.class,
		CrumaTower.class,
		DelusionTeleport.class,
		ElrokiTeleporters.class,
		GatekeeperSpirit.class,
		GhostChamberlainOfElmoreden.class,
		GrandBossTeleporters.class,
		HuntingGroundsTeleport.class,
		Klemis.class,
		MithrilMinesTeleporter.class,
		NewbieGuide.class,
		NoblesseTeleport.class,
		OracleTeleport.class,
		PaganTeleporters.class,
		SeparatedSoul.class,
		StakatoNestTeleporter.class,
		SteelCitadelTeleport.class,
		StrongholdsTeleports.class,
		Survivor.class,
		TeleportToFantasy.class,
		TeleportToRaceTrack.class,
		TeleportToUndergroundColiseum.class,
		TeleportWithCharm.class,
		ToIVortex.class,
		TerritoryManagers.class,
		TownPets.class,
		HealerTrainer.class,
		Tunatun.class,
		VarkaSilenosSupport.class,
		FirstClassTransferTalk.class,
		WeaverOlf.class,
		WyvernManager.class,
		// Fantasy Isle
		MC_Show.class,
		HandysBlockCheckerEvent.class,
		// Group Template
		AltarsOfSacrifice.class,
		BeastFarm.class,
		DenOfEvil.class,
		DragonValley.class,
		FairyTrees.class,
		FeedableBeasts.class,
		FleeMonsters.class,
		FrozenLabyrinth.class,
		GiantsCave.class,
		HotSprings.class,
		IsleOfPrayer.class,
		LairOfAntharas.class,
		MinionSpawnManager.class,
		MonasteryOfSilence.class,
		NonLethalableNpcs.class,
		NonTalkingNpcs.class,
		PavelArchaic.class,
		PlainsOfDion.class,
		PlainsOfLizardman.class,
		PolymorphingAngel.class,
		PolymorphingOnAttack.class,
		PrimevalIsle.class,
		PrisonGuards.class,
		RaidBossCancel.class,
		RandomSpawn.class,
		RangeGuard.class,
		Remnants.class,
		Sandstorms.class,
		SeeThroughSilentMove.class,
		SelMahumDrill.class,
		SelMahumSquad.class,
		SilentValley.class,
		StakatoNest.class,
		SummonPc.class,
		TreasureChest.class,
		TurekOrcs.class,
		VarkaKetra.class,
		WarriorFishingBlock.class,
		// Individual
		Antharas.class,
		Baium.class,
		Sailren.class,
		Venom.class,
		Anais.class,
		Ballista.class,
		Beleth.class,
		BlackdaggerWing.class,
		BleedingFly.class,
		BloodyBerserker.class,
		BloodyKarik.class,
		BloodyKarinness.class,
		CrimsonHatuOtis.class,
		Core.class,
		DarkWaterDragon.class,
		DivineBeast.class,
		DrakosWarrior.class,
		DustRider.class,
		EmeraldHorn.class,
		Epidos.class,
		EvasGiftBox.class,
		FrightenedRagnaOrc.class,
		GiganticGolem.class,
		Gordon.class,
		GraveRobbers.class,
		Knoriks.class,
		MuscleBomber.class,
		Orfen.class,
		QueenAnt.class,
		QueenShyeed.class,
		RagnaOrcCommander.class,
		RagnaOrcHero.class,
		RagnaOrcSeer.class,
		NecromancerOfTheValley.class,
		ShadowSummoner.class,
		SinEater.class,
		SinWardens.class,
		Valakas.class,
		// Village Master
		Clan.class,
		Alliance.class,
		DarkElfChange1.class,
		DarkElfChange2.class,
		DwarfBlacksmithChange1.class,
		DwarfBlacksmithChange2.class,
		DwarfWarehouseChange1.class,
		DwarfWarehouseChange2.class,
		ElfHumanClericChange2.class,
		ElfHumanFighterChange1.class,
		ElfHumanFighterChange2.class,
		ElfHumanWizardChange1.class,
		ElfHumanWizardChange2.class,
		KamaelChange1.class,
		KamaelChange2.class,
		OrcChange1.class,
		OrcChange2.class
	};
	
	public static void main(String[] args) {
		int n = 0;
		for (var ai : SCRIPTS) {
			try {
				ai.getDeclaredConstructor().newInstance();
				n++;
			} catch (Exception ex) {
				LOG.error("Error loading AI {}!", ai.getSimpleName(), ex);
			}
		}
		LOG.info("Loaded {} AI scripts.", n);
		
	}
}
