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
package com.l2jserver.datapack.ai.npc.Fisherman;

import static com.l2jserver.gameserver.config.Configuration.character;

import java.util.List;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.L2SkillLearn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2MerchantInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.AcquireSkillType;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.AcquireSkillList;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * Fisherman AI.
 * @author Adry_85
 * @since 2.6.0.0
 */
public class Fisherman extends AbstractNpcAI {
	// NPC
	private static final int[] FISHERMAN = {
		31562,
		31563,
		31564,
		31565,
		31566,
		31567,
		31568,
		31569,
		31570,
		31571,
		31572,
		31573,
		31574,
		31575,
		31576,
		31577,
		31578,
		31579,
		31696,
		31697,
		31989,
		32007,
		32348
	};
	
	public Fisherman() {
		super(Fisherman.class.getSimpleName(), "ai/npc");
		addStartNpc(FISHERMAN);
		addTalkId(FISHERMAN);
		addFirstTalkId(FISHERMAN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		String htmltext = null;
		switch (event) {
			case "LearnFishSkill": {
				showFishSkillList(player);
				break;
			}
			case "fishing_championship.htm": {
				htmltext = event;
				break;
			}
			case "BuySellRefund": {
				((L2MerchantInstance) npc).showBuyWindow(player, npc.getId() * 100, true);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		if ((player.getKarma() > 0) && !character().karmaPlayerCanShop()) {
			return npc.getId() + "-pk.htm";
		}
		return npc.getId() + ".htm";
	}
	
	/**
	 * Display the Fishing Skill list to the player.
	 * @param player the player
	 */
	public static void showFishSkillList(L2PcInstance player) {
		final List<L2SkillLearn> fishskills = SkillTreesData.getInstance().getAvailableFishingSkills(player);
		final AcquireSkillList asl = new AcquireSkillList(AcquireSkillType.FISHING);
		int count = 0;
		
		for (L2SkillLearn s : fishskills) {
			if (SkillData.getInstance().getSkill(s.getSkillId(), s.getSkillLevel()) != null) {
				count++;
				asl.addSkill(s.getSkillId(), s.getSkillLevel(), s.getSkillLevel(), s.getLevelUpSp(), 1);
			}
		}
		
		if (count > 0) {
			player.sendPacket(asl);
		} else {
			final int minlLevel = SkillTreesData.getInstance().getMinLevelForNewSkill(player, SkillTreesData.getInstance().getFishingSkillTree());
			if (minlLevel > 0) {
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN_S1);
				sm.addInt(minlLevel);
				player.sendPacket(sm);
			} else {
				player.sendPacket(SystemMessageId.NO_MORE_SKILLS_TO_LEARN);
			}
		}
	}
}
