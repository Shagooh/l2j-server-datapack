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
package com.l2jserver.datapack.handlers.effecthandlers.instant;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.ai.L2CharacterAI;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.BuffInfo;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.stats.Formulas;

/**
 * Betray instant effect test.
 * @author Zoey76
 * @version 2.6.3.0
 */
@ExtendWith(MockitoExtension.class)
public class InstantBetrayTest {
	
	@Mock
	private BuffInfo buffInfo;
	@Mock
	private L2Character effector;
	@Mock
	private L2Character effected;
	@Mock
	private L2Attackable effectedMinion;
	@Mock
	private L2Attackable effectedLeader;
	@Mock
	private Skill skill;
	@Mock
	private L2PcInstance masterPlayer;
	@Mock
	private L2CharacterAI creatureAI;
	
	private static InstantBetray effect;
	
	@BeforeAll
	static void init() {
		mockStatic(Formulas.class);
		final var set = new StatsSet(Map.of("name", "InstantBetray"));
		final var params = new StatsSet(Map.of("chance", "80", "time", "30"));
		effect = new InstantBetray(null, null, set, params);
	}
	
	@Test
	public void test_null_effected() {
		effect.onStart(buffInfo);
	}
	
	@Test
	public void test_effected_is_raid() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(effected.isRaid()).thenReturn(true);
		
		effect.onStart(buffInfo);
	}
	
	@Test
	public void test_effected_not_servitor_summon_raid_minion() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(effected.isRaid()).thenReturn(false);
		when(effected.isServitor()).thenReturn(false);
		when(effected.isSummon()).thenReturn(false);
		when(effected.isRaidMinion()).thenReturn(false);
		
		effect.onStart(buffInfo);
	}
	
	@Test
	public void test_effected_is_servitor_probability_fail() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(buffInfo.getEffector()).thenReturn(effector);
		when(buffInfo.getSkill()).thenReturn(skill);
		when(effected.isRaid()).thenReturn(false);
		when(effected.isServitor()).thenReturn(true);
		when(effected.getActingPlayer()).thenReturn(masterPlayer);
		when(Formulas.calcProbability(80, effector, effected, skill)).thenReturn(false);
		
		effect.onStart(buffInfo);
	}
	
	@Test
	public void test_effected_is_servitor() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(buffInfo.getEffector()).thenReturn(effector);
		when(buffInfo.getSkill()).thenReturn(skill);
		when(effected.isRaid()).thenReturn(false);
		when(effected.isServitor()).thenReturn(true);
		when(effected.getActingPlayer()).thenReturn(masterPlayer);
		when(Formulas.calcProbability(80, effector, effected, skill)).thenReturn(true);
		when(effected.getAI()).thenReturn(creatureAI);
		
		effect.onStart(buffInfo);
	}
	
	@Test
	public void test_effected_is_summon() {
		when(buffInfo.getEffected()).thenReturn(effected);
		when(buffInfo.getEffector()).thenReturn(effector);
		when(buffInfo.getSkill()).thenReturn(skill);
		when(effected.isRaid()).thenReturn(false);
		when(effected.isServitor()).thenReturn(false);
		when(effected.isSummon()).thenReturn(true);
		when(effected.getActingPlayer()).thenReturn(masterPlayer);
		when(Formulas.calcProbability(80, effector, effected, skill)).thenReturn(true);
		when(effected.getAI()).thenReturn(creatureAI);
		
		effect.onStart(buffInfo);
	}
	
	@Test
	public void test_effected_is_raid_minion() {
		when(buffInfo.getEffected()).thenReturn(effectedMinion);
		when(buffInfo.getEffector()).thenReturn(effector);
		when(buffInfo.getSkill()).thenReturn(skill);
		when(effectedMinion.isRaid()).thenReturn(false);
		when(effectedMinion.isServitor()).thenReturn(false);
		when(effectedMinion.isSummon()).thenReturn(false);
		when(effectedMinion.isRaidMinion()).thenReturn(true);
		when(effectedMinion.getLeader()).thenReturn(effectedLeader);
		when(Formulas.calcProbability(80, effector, effectedMinion, skill)).thenReturn(true);
		when(effectedMinion.getAI()).thenReturn(creatureAI);
		
		effect.onStart(buffInfo);
	}
}
