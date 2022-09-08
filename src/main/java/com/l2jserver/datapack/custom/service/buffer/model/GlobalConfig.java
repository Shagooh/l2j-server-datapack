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
package com.l2jserver.datapack.custom.service.buffer.model;

import java.util.Map;

import com.l2jserver.datapack.custom.service.buffer.model.entity.BuffCategory;
import com.l2jserver.datapack.custom.service.buffer.model.entity.BuffSkill;

/**
 * Global configuration.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class GlobalConfig {
	private Map<String, BuffSkill> buffs;
	private Map<String, BuffCategory> buffCategories;
	
	public void afterDeserialize(BufferConfig config) {
		for (var buff : buffs.values()) {
			buff.afterDeserialize(config);
		}
		
		for (var category : buffCategories.values()) {
			category.afterDeserialize(config);
		}
	}
	
	public BuffSkill getBuff(String id) {
		return getBuffs().get(id);
	}
	
	public final Map<String, BuffSkill> getBuffs() {
		return buffs;
	}
	
	public final BuffCategory getCategory(String id) {
		return getCategories().get(id);
	}
	
	public final Map<String, BuffCategory> getCategories() {
		return buffCategories;
	}
}