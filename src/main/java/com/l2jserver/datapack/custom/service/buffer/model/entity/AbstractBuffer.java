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
package com.l2jserver.datapack.custom.service.buffer.model.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.l2jserver.datapack.custom.service.base.model.entity.CustomServiceServer;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.buffer.model.BufferConfig;
import com.l2jserver.gameserver.config.Configuration;

/**
 * Abstract buffer.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public abstract class AbstractBuffer extends CustomServiceServer {
	private boolean canHeal;
	private boolean canCancel;
	private List<String> presetBuffCategories;
	private List<String> buffCategories;
	
	private final transient Map<String, BuffCategory> presetBuffCatsMap = new LinkedHashMap<>();
	private final transient Map<String, BuffCategory> buffCatsMap = new LinkedHashMap<>();
	
	public AbstractBuffer(String bypassPrefix) {
		super(bypassPrefix, "buffer");
	}
	
	public void afterDeserialize(BufferConfig config) {
		super.afterDeserialize();
		
		for (String id : presetBuffCategories) {
			presetBuffCatsMap.put(id, config.getGlobal().getCategories().get(id));
		}
		
		for (String id : buffCategories) {
			buffCatsMap.put(id, config.getGlobal().getCategories().get(id));
		}
		
		if (canHeal) {
			getPlaceholder().addChild("can_heal", null);
		}
		if (canCancel) {
			getPlaceholder().addChild("can_cancel", null);
		}
		if (!presetBuffCategories.isEmpty()) {
			HTMLTemplatePlaceholder presetBufflistsPlaceholder = getPlaceholder().addChild("presets", null).getChild("presets");
			for (Entry<String, BuffCategory> presetBufflist : presetBuffCatsMap.entrySet()) {
				presetBufflistsPlaceholder.addAliasChild(String.valueOf(presetBufflistsPlaceholder.getChildrenSize()), presetBufflist.getValue().getPlaceholder());
			}
		}
		if (!buffCategories.isEmpty()) {
			HTMLTemplatePlaceholder buffCatsPlaceholder = getPlaceholder().addChild("categories", null).getChild("categories");
			for (Entry<String, BuffCategory> buffCat : buffCatsMap.entrySet()) {
				buffCatsPlaceholder.addAliasChild(String.valueOf(buffCatsPlaceholder.getChildrenSize()), buffCat.getValue().getPlaceholder());
			}
		}
		
		getPlaceholder().addChild("max_unique_lists", String.valueOf(Configuration.bufferService().getMaxUniqueLists()));
	}
	
	public final boolean getCanHeal() {
		return canHeal;
	}
	
	public final boolean getCanCancel() {
		return canCancel;
	}
	
	public Map<String, BuffCategory> getPresetBuffCats() {
		return presetBuffCatsMap;
	}
	
	public final Map<String, BuffCategory> getBuffCats() {
		return buffCatsMap;
	}
}
