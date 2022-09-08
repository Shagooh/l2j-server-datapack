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

import static com.l2jserver.gameserver.model.effects.L2EffectType.MANAHEAL_BY_LEVEL;
import static com.l2jserver.gameserver.model.stats.Stats.MANA_CHARGE;
import static com.l2jserver.gameserver.network.SystemMessageId.S1_MP_HAS_BEEN_RESTORED;
import static com.l2jserver.gameserver.network.serverpackets.SystemMessage.getSystemMessage;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.effects.L2EffectType;
import com.l2jserver.gameserver.model.skills.BuffInfo;

/**
 * Instant Mp By Level Self effect implementation.
 * @author Zoey76
 * @version 2.6.3.0
 */
public final class InstantMpByLevelSelf extends AbstractEffect {
	
	private final double power;
	
	public InstantMpByLevelSelf(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params) {
		super(attachCond, applyCond, set, params);
		power = params.getDouble("power", 0);
	}
	
	@Override
	public L2EffectType getEffectType() {
		return MANAHEAL_BY_LEVEL;
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info) {
		final var target = info.getEffector();
		if ((target == null) || target.isDead() || target.isDoor() || target.isInvul() || target.isMpBlocked()) {
			return;
		}
		
		var amount = target.calcStat(MANA_CHARGE, power, null, null);
		// Prevents overheal and negative amount.
		amount = Math.max(Math.min(amount, target.getMaxRecoverableMp() - target.getCurrentMp()), 0);
		target.setCurrentMp(amount + target.getCurrentMp());
		target.sendPacket(getSystemMessage(S1_MP_HAS_BEEN_RESTORED).addInt((int) amount));
	}
}
