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

package com.l2jserver.datapack.gracia.ai;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Draconian Troops on death random mob spawn AI
 * @author Lomka
 */
public class DraconianTroops extends AbstractNpcAI {
    // Mobs in SoD have a chance to spawn DRACONIAN_SHADOW_WIFE upon death
    private static final int DRACONIAN_SHADOW_WIFE = 22545;
    private final static int[] MOB_LIST = {
        18783,
        18784,
        18785,
        18786,
        18787,
        18788,
        18789,
        18790,
        18791,
        22536,
        22537,
        22538,
        22539,
        22540,
        22541,
        22542,
        22543,
        22544,
        22546,
        22547,
        22548,
        22569,
        22570,
        22571,
        22572,
        22573,
        22574,
        22575,
        22576,
        22577,
        22578,
        22579,
        22580,
        22581,
    };
    
    public DraconianTroops() {
        super(DraconianTroops.class.getSimpleName(), "gracia/AI");
        addKillId(MOB_LIST);
    }
    
    @Override
    public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
        if (getRandom(10000) < 300) {
            L2Attackable spawn = (L2Attackable) addSpawn(DRACONIAN_SHADOW_WIFE, npc.getLocation(), false, 0, false, killer.getInstanceId());
            addAttackDesire(spawn, killer, 10000);
        }
        return super.onKill(npc, killer, isSummon);
    }
}
