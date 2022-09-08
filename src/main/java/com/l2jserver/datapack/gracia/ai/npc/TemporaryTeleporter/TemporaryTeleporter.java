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
package com.l2jserver.datapack.gracia.ai.npc.TemporaryTeleporter;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.instancemanager.GraciaSeedsManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Gracia Temporary Teleporter AI
 * @author Lomka
 */
public class TemporaryTeleporter extends AbstractNpcAI {
    private static final int TEMPORARY_TELEPORTER = 32602;
    
    // Teleport Locations
    private static final Location SOD_DOCK = new Location(-248683, 250243, 4336);
    private static final Location CENTRAL_SQUARE = new Location(-245833, 220174, -12104);
    private static final Location FORTRESS_OF_DESTRUCTION = new Location(-251624, 213420, -12072);
    private static final Location CRIMSON_THRONE = new Location(-249774, 207316, -11952);
    private static final Location REMNANT_TP_POINT = new Location(-248567, 250117, 4336);
    
    public TemporaryTeleporter() {
        super(TemporaryTeleporter.class.getSimpleName(), "gracia/AI/NPC");
        addStartNpc(TEMPORARY_TELEPORTER);
        addFirstTalkId(TEMPORARY_TELEPORTER);
        addTalkId(TEMPORARY_TELEPORTER);
    }
    
    @Override
    public String onFirstTalk(L2Npc npc, L2PcInstance player) {
        final int seedState = GraciaSeedsManager.getInstance().getSoDState();
        if (seedState == 2) {
            if (player.getInstanceId() != 0) {
                return "32602-02.html";
            }
            return "32602-01.html";
        }
        return "32602.html";
    }
    
    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        switch (event) {
            case "TP_CENTRAL_SQUARE": {
                teleportPlayer(player, CENTRAL_SQUARE, 0, true);
                break;
            }
            case "TP_FORTRESS_OF_DESTRUCTION": {
                teleportPlayer(player, FORTRESS_OF_DESTRUCTION, 0, true);
                break;
            }
            case "TP_CRIMSON_THRONE": {
                teleportPlayer(player, CRIMSON_THRONE, 0, true);
                break;
            }
            case "EXIT_SOD": {
                teleportPlayer(player, SOD_DOCK, 0, true);
                break;
            }
            case "EXIT_REMNANTS": {
                teleportPlayer(player, REMNANT_TP_POINT, 0, true);
                break;
            }
        }
        return super.onAdvEvent(event, npc, player);
    }
}
