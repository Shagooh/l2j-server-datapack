/*
 * Copyright © 2004-2022 L2J DataPack
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

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.skills.BuffInfo;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExVoteSystemInfo;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;

/**
 * This effect instantly raises recommendations to give out by the specified amount.
 * @author NosBit
 * @author HorridoJoho
 * @since 2.6.3.0
 */
public final class BonusCountUp extends AbstractEffect {
	private final int _amount;
	
	public BonusCountUp(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params) {
		super(attachCond, applyCond, set, params);
		_amount = params.getInt("amount", 0);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info) {
		super.onStart(info);
		
		final L2PcInstance player = info.getEffector().getActingPlayer();
		if (player != null) {
			int recomHaveIncrease = _amount;
			if ((player.getRecSystem().getHave() + _amount) >= 255) {
				recomHaveIncrease = 255 - player.getRecSystem().getHave();
			}
			
			if (recomHaveIncrease > 0) {
				player.getRecSystem().setHave(player.getRecSystem().getHave() + recomHaveIncrease);
				
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_OBTAINED_S1_RECOMMENDATIONS);
				sm.addInt(recomHaveIncrease);
				player.sendPacket(sm);
				player.sendPacket(new UserInfo(player));
				player.sendPacket(new ExVoteSystemInfo(player));
			}
		}
	}
}
