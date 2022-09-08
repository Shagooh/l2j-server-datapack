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

import static com.l2jserver.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;
import static com.l2jserver.gameserver.ai.CtrlIntention.AI_INTENTION_IDLE;
import static com.l2jserver.gameserver.model.effects.EffectFlag.BETRAYED;
import static com.l2jserver.gameserver.model.effects.L2EffectType.DEBUFF;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.effects.L2EffectType;
import com.l2jserver.gameserver.model.skills.BuffInfo;
import com.l2jserver.gameserver.model.stats.Formulas;

/**
 * Betray instant effect implementation.
 * @author Zoey76
 * @version 2.6.2.0
 */
public final class InstantBetray extends AbstractEffect {
	
	private final int chance;
	
	private final int time;
	
	public InstantBetray(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params) {
		super(attachCond, applyCond, set, params);
		this.chance = params.getInt("chance", 0);
		this.time = params.getInt("time", 0);
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
		
		if (effected.isRaid()) {
			return;
		}
		
		final var target = effected.isServitor() || effected.isSummon() ? effected.getActingPlayer() //
			: effected.isRaidMinion() ? ((L2Attackable) effected).getLeader() : null;
		if (target == null) {
			return;
		}
		
		if (!Formulas.calcProbability(chance, info.getEffector(), effected, info.getSkill())) {
			return;
		}
		
		final var effectedAI = effected.getAI();
		effectedAI.setIntention(AI_INTENTION_ATTACK, target);
		
		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(() -> effectedAI.setIntention(AI_INTENTION_IDLE, target), 0, time, SECONDS);
	}
	
	@Override
	public int getEffectFlags() {
		return BETRAYED.getMask();
	}
	
	@Override
	public L2EffectType getEffectType() {
		return DEBUFF;
	}
}
