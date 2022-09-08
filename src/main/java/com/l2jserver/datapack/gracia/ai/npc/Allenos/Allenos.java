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
package com.l2jserver.datapack.gracia.ai.npc.Allenos;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.instancemanager.GraciaSeedsManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;

/**
 * Gracia NPC Allenos AI
 * @author Lomka
 */
public class Allenos extends AbstractNpcAI {
    private static final int ALLENOS = 32526;
    private static final Location TELEPORT_LOCATION = new Location(-245800, 220488, -12112);
    
    public Allenos() {
        super(Allenos.class.getSimpleName(), "gracia/AI/NPC");
        addStartNpc(ALLENOS);
        addFirstTalkId(ALLENOS);
        addTalkId(ALLENOS);
    }
    
    @Override
    public String onFirstTalk(L2Npc npc, L2PcInstance player) {
        String htmltext = null;
        final int seedState = GraciaSeedsManager.getInstance().getSoDState();
        if (seedState == 1) {
            return "32526.html";
        } else if (seedState == 2) {
            htmltext = "32526-02.html";
        } else if (seedState >= 3) {
            htmltext = "32526-04.html";
        }
        return htmltext;
    }
    
    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player) {
        switch (event) {
            case "ENTER_SOD": {
                if (player.isFlyingMounted()) {
                    player.sendPacket(SystemMessageId.YOU_CANNOT_ENTER_SEED_IN_FLYING_TRANSFORM);
                    break;
                }
                teleportPlayer(player, TELEPORT_LOCATION, 0, false);
                break;
            }
            case "01": {
                return "32526-01.html";
            }
            case "03": {
                return "32526-03.html";
            }
        }
        return super.onAdvEvent(event, npc, player);
    }
}