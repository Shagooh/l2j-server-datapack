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

import static com.l2jserver.gameserver.model.effects.L2EffectType.BUFF;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.effects.L2EffectType;
import com.l2jserver.gameserver.model.skills.BuffInfo;

/**
 * Despawn instant effect implementation.
 * @author Zoey76
 * @version 2.6.2.0
 */
public final class InstantDespawn extends AbstractEffect {
	
	private final int chance;
	
	public InstantDespawn(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params) {
		super(attachCond, applyCond, set, params);
		this.chance = params.getInt("chance", 0);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info) {
		final var effected = info.getEffected();
		if (effected == null) {
			return;
		}
		
		final var player = effected.getActingPlayer();
		if (player == null) {
			return;
		}
		
		final var summon = player.getSummon();
		if (summon == null) {
			return;
		}
		
		if (Rnd.get(100) < chance) {
			return;
		}
		
		summon.unSummon(player);
	}
	
	@Override
	public L2EffectType getEffectType() {
		return BUFF;
	}
}
