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

import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.items.L2Item;

/**
 * Item Requirement.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public class ItemRequirement {
	private int id;
	private long amount;
	
	private final transient HTMLTemplatePlaceholder placeholder;
	
	public ItemRequirement() {
		id = 0;
		amount = 0;
		
		placeholder = new HTMLTemplatePlaceholder("placeholder", null);
	}
	
	public ItemRequirement(int id, long amount) {
		this.id = id;
		this.amount = amount;
		
		placeholder = new HTMLTemplatePlaceholder("placeholder", null);
		
		afterDeserialize();
	}
	
	public void afterDeserialize() {
		final L2Item item = getItem();
		placeholder.addChild("id", String.valueOf(item.getId())).addChild("icon", item.getIcon()).addChild("name", item.getName()).addChild("amount", String.valueOf(amount));
	}
	
	public final int getItemId() {
		return id;
	}
	
	public final long getItemAmount() {
		return amount;
	}
	
	public HTMLTemplatePlaceholder getPlaceholder() {
		return placeholder;
	}
	
	public final L2Item getItem() {
		return ItemTable.getInstance().getTemplate(id);
	}
}
