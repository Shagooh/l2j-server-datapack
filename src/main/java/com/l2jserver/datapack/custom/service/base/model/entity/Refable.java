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

import java.util.Objects;

import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;

/**
 * Refable.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public abstract class Refable implements IRefable<String> {
	private String id;
	
	private final transient HTMLTemplatePlaceholder placeholder;
	
	protected Refable() {
		id = null;
		
		placeholder = new HTMLTemplatePlaceholder("placeholder", null);
	}
	
	protected Refable(String id) {
		Objects.requireNonNull(id);
		this.id = id;
		
		placeholder = new HTMLTemplatePlaceholder("placeholder", null);
	}
	
	public void afterDeserialize() {
		placeholder.addChild("ident", id);
	}
	
	@Override
	public final String getId() {
		return id;
	}
	
	public final HTMLTemplatePlaceholder getPlaceholder() {
		return placeholder;
	}
}
