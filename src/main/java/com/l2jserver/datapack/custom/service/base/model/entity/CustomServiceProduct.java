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
package com.l2jserver.datapack.custom.service.base.model.entity;

import java.util.List;

import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;

/**
 * Custom Service Product.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public abstract class CustomServiceProduct extends Refable {
	private List<ItemRequirement> items;
	
	protected CustomServiceProduct() {
	}
	
	@Override
	public void afterDeserialize() {
		super.afterDeserialize();
		
		if (!items.isEmpty()) {
			HTMLTemplatePlaceholder itemsPlaceholder = getPlaceholder().addChild("items", null).getChild("items");
			for (ItemRequirement item : items) {
				item.afterDeserialize();
				itemsPlaceholder.addAliasChild(String.valueOf(itemsPlaceholder.getChildrenSize()), item.getPlaceholder());
			}
		}
	}
	
	public final List<ItemRequirement> getItems() {
		return items;
	}
}
