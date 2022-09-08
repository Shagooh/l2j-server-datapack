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
package com.l2jserver.datapack.handlers.effecthandlers.pump;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.skills.BuffInfo;

/**
 * Transform Hangover pump effect test.
 * @author Zoey76
 * @version 2.6.3.0
 */
@ExtendWith(MockitoExtension.class)
public class TransformHangoverTest {
	
	@Mock
	private BuffInfo buffInfo;
	
	private static TransformHangover effect;
	
	@BeforeAll
	static void init() {
		final var set = new StatsSet(Map.of("name", "TransformHangover"));
		final var params = new StatsSet(Map.of());
		effect = new TransformHangover(null, null, set, params);
	}
	
	@Test
	public void test_on_action_time() {
		effect.onActionTime(buffInfo);
	}
}
