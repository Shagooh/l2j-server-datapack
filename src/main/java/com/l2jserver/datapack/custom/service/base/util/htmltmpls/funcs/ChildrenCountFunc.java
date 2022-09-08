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
package com.l2jserver.datapack.custom.service.base.util.htmltmpls.funcs;

import java.util.Map;

import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplateFunc;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplateUtils;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Children Count function.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class ChildrenCountFunc extends HTMLTemplateFunc {
	public static final ChildrenCountFunc INSTANCE = new ChildrenCountFunc();
	
	private ChildrenCountFunc() {
		super("CHILDSCOUNT", "ENDCHILDSCOUNT", false);
	}
	
	@Override
	public Map<String, HTMLTemplatePlaceholder> handle(StringBuilder content, L2PcInstance player, Map<String, HTMLTemplatePlaceholder> placeholders, HTMLTemplateFunc[] funcs) {
		HTMLTemplatePlaceholder placeholder = HTMLTemplateUtils.getPlaceholder(content.toString(), placeholders);
		content.setLength(0);
		if (placeholder != null) {
			content.append(placeholder.getChildrenSize());
		}
		return null;
	}
}
