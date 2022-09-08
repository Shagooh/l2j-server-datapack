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

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.impl.character.OnCreatureSkillUse;
import com.l2jserver.gameserver.model.events.listeners.ConsumerEventListener;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.BuffInfo;
import com.l2jserver.gameserver.model.skills.targets.TargetType;

/**
 * Trigger Skill By Skill effect implementation.
 * @author Zealar
 */
public final class TriggerSkillBySkill extends AbstractEffect {
	private final int _castSkillId;
	private final int _chance;
	private final SkillHolder _skill;
	private final TargetType _targetType;
	
	public TriggerSkillBySkill(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params) {
		super(attachCond, applyCond, set, params);
		
		_castSkillId = params.getInt("castSkillId", 0);
		_chance = params.getInt("chance", 100);
		_skill = new SkillHolder(params.getInt("skillId", 0), params.getInt("skillLevel", 0));
		_targetType = params.getEnum("targetType", TargetType.class, TargetType.ONE);
	}
	
	public void onSkillUseEvent(OnCreatureSkillUse event) {
		if ((_chance == 0) || ((_skill.getSkillId() == 0) || (_skill.getSkillLvl() == 0) || (_castSkillId == 0))) {
			return;
		}
		
		if (_castSkillId != event.getSkill().getId()) {
			return;
		}
		
		if (Rnd.get(100) > _chance) {
			return;
		}
		
		final var triggerSkill = _skill.getSkill();
		final var targets = _targetType.getTargets(triggerSkill, event.getCaster(), event.getTarget());
		for (var object : targets) {
			if ((object == null) || !object.isCharacter()) {
				continue;
			}
			
			final var target = (L2Character) object;
			if (!target.isInvul()) {
				event.getCaster().makeTriggerCast(triggerSkill, target);
			}
		}
	}
	
	@Override
	public void onExit(BuffInfo info) {
		info.getEffected().removeListenerIf(EventType.ON_CREATURE_SKILL_USE, listener -> listener.getOwner() == this);
	}
	
	@Override
	public void onStart(BuffInfo info) {
		info.getEffected().addListener(new ConsumerEventListener(info.getEffected(), EventType.ON_CREATURE_SKILL_USE, (OnCreatureSkillUse event) -> onSkillUseEvent(event), this));
	}
}
