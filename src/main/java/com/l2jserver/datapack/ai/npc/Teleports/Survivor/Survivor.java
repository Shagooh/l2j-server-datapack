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
package com.l2jserver.datapack.ai.npc.Teleports.Survivor;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.instancemanager.GraciaSeedsManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;

/**
 * Gracia Survivor teleport AI.<br>
 * Original Jython script by Kerberos.
 * @author Plim
 */
public final class Survivor extends AbstractNpcAI {
	// NPC
	private static final int SURVIVOR = 32632;
	// Misc
	private static final int MIN_LEVEL = 75;
	// Location
	private static final Location TELEPORT = new Location(-149406, 255247, -80);
	
	public Survivor() {
		super(Survivor.class.getSimpleName(), "ai/npc/Teleports");
		addStartNpc(SURVIVOR);
		addTalkId(SURVIVOR);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
		if (event.equalsIgnoreCase("STATUS")) {
			String htmltext = getHtm(player.getHtmlPrefix(), "32632-4.htm");
			String destructionStatus = "";
			String infinityStatus = "";
			// TODO: Implement Seed of Infinity & stages
			// switch(GraciaSeedsManager.getInstance().getSoIState()){
			// case 1:
			// infinityStatus = "It's under the enemy's occupation, and the military forces of adventurers and clan members are unleashing an onslaught upon the Hall of Suffering and the Hall of Erosion.";
			// case 2:
			// infinityStatus = "It's under enemy occupation, but the situation is currently favorable, and an infiltration route to the Heart has been secured. All that is left is the final battle with Ekimus and the clean-up of his followers hiding in the Hall of Suffering!";
			// case 3:
			// infinityStatus = "Our forces have occupied it and are currently investigating the depths.";
			// case 4:
			// infinityStatus = "It's under occupation by our forces, but the enemy has resurrected and is attacking toward the Hall of Suffering and the Hall of Erosion.";
			// case 5:
			// infinityStatus = "It's under occupation by our forces, but the enemy has already overtaken the Hall of Erosion and is driving out our forces from the Hall of Suffering toward the Heart. It seems that Ekimus will revive shortly.";
			// }
			switch (GraciaSeedsManager.getInstance().getSoDState()) {
				case 1:
					destructionStatus = "It's currently occupied by the enemy and our troops are attacking.";
				case 2:
					destructionStatus = "It's under occupation by our forces, and I heard that Kucereus' clan is organizing the remnants.";
				default:
					destructionStatus = "Although we currently have control of it, the enemy is pushing back with a powerful attack.";
			}
			htmltext = htmltext.replaceAll("%sod%", destructionStatus);
			htmltext = htmltext.replaceAll("%soi%", infinityStatus);
			return htmltext;
		}
		if ("32632-2.htm".equals(event)) {
			if (player.getLevel() < MIN_LEVEL) {
				event = "32632-3.htm";
			} else if (player.getAdena() < 150000) {
				return event;
			} else {
				takeItems(player, Inventory.ADENA_ID, 150000);
				player.teleToLocation(TELEPORT);
				return null;
			}
		}
		return event;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		return "32632-1.htm";
	}
}
