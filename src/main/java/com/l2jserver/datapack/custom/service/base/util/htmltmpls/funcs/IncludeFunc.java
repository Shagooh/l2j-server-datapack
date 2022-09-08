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
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Include function.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class IncludeFunc extends HTMLTemplateFunc {
	public static final IncludeFunc INSTANCE = new IncludeFunc();
	
	private IncludeFunc() {
		super("INC", "ENDINC", true);
	}
	
	@Override
	public Map<String, HTMLTemplatePlaceholder> handle(StringBuilder content, L2PcInstance player, Map<String, HTMLTemplatePlaceholder> placeholders, HTMLTemplateFunc[] funcs) {
		String fileContent = HtmCache.getInstance().getHtm(player != null ? player.getHtmlPrefix() : null, content.toString());
		if (fileContent != null) {
			content.ensureCapacity(fileContent.length());
			content.setLength(0);
			content.append(fileContent);
		}
		return null;
	}
}
