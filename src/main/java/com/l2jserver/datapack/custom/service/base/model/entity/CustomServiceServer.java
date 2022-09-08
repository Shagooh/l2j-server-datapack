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

import com.l2jserver.datapack.custom.service.base.DialogType;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;

/**
 * Custom Service Server.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public abstract class CustomServiceServer {
	private DialogType dialogType;
	private String htmlFolder;
	
	private final transient HTMLTemplatePlaceholder placeholder;
	private final transient String bypassPrefix;
	private final transient String htmlAccessorName;
	
	public CustomServiceServer(String bypassPrefix, String htmlAccessorName) {
		dialogType = DialogType.NPC;
		htmlFolder = null;
		
		placeholder = new HTMLTemplatePlaceholder("service", null);
		this.bypassPrefix = "bypass -h " + bypassPrefix;
		this.htmlAccessorName = htmlAccessorName;
	}
	
	public void afterDeserialize() {
		placeholder.addChild("bypass_prefix", bypassPrefix).addChild("name", getName());
	}
	
	public final DialogType getDialogType() {
		return dialogType;
	}
	
	public final String getHtmlFolder() {
		return htmlFolder;
	}
	
	public final HTMLTemplatePlaceholder getPlaceholder() {
		return placeholder;
	}
	
	public final String getBypassPrefix() {
		return bypassPrefix;
	}
	
	public final String getHtmlAccessorName() {
		return htmlAccessorName;
	}
	
	public abstract String getName();
}
