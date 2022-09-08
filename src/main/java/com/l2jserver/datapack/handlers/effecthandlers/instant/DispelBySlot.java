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

import static com.l2jserver.gameserver.model.effects.L2EffectType.DISPEL;
import static com.l2jserver.gameserver.model.skills.AbnormalType.TRANSFORM;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.effects.L2EffectType;
import com.l2jserver.gameserver.model.skills.AbnormalType;
import com.l2jserver.gameserver.model.skills.BuffInfo;

/**
 * Dispel By Slot effect implementation.
 * @author Gnacik, Zoey76, Adry_85
 */
public class DispelBySlot extends AbstractEffect {
	private final String _dispel;
	private final Map<AbnormalType, Integer> _dispelAbnormals;
	
	public DispelBySlot(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params) {
		super(attachCond, applyCond, set, params);
		
		_dispel = params.getString("dispel", null);
		if ((_dispel != null) && !_dispel.isEmpty()) {
			_dispelAbnormals = new EnumMap<>(AbnormalType.class);
			for (var ngtStack : _dispel.split(";")) {
				var ngt = ngtStack.split(",");
				if (ngt.length > 1) {
					_dispelAbnormals.put(AbnormalType.valueOf(ngt[0]), Integer.valueOf(ngt[1]));
				} else {
					_dispelAbnormals.put(AbnormalType.valueOf(ngt[0]), -1);
				}
			}
		} else {
			_dispelAbnormals = Collections.<AbnormalType, Integer> emptyMap();
		}
	}
	
	@Override
	public L2EffectType getEffectType() {
		return DISPEL;
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info) {
		final var effected = getEffected(info);
		final var effectList = effected.getEffectList();
		for (var entry : _dispelAbnormals.entrySet()) {
			// Dispel transformations (buff and by GM)
			if ((entry.getKey() == TRANSFORM)) {
				if (effected.isTransformed() || (effected.isPlayer() || (entry.getValue() == effected.getActingPlayer().getTransformationId()) || (entry.getValue() < 0))) {
					info.getEffected().stopTransformation(true);
					continue;
				}
			}
			
			final var toDispel = effectList.getBuffInfoByAbnormalType(entry.getKey());
			if (toDispel == null) {
				continue;
			}
			
			if ((entry.getKey() == toDispel.getSkill().getAbnormalType()) && ((entry.getValue() < 0) || (entry.getValue() >= toDispel.getSkill().getAbnormalLvl()))) {
				effectList.stopSkillEffects(true, entry.getKey());
			}
		}
	}
	
	protected L2Character getEffected(BuffInfo info) {
		return info.getEffected();
	}
}
