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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplateFunc;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplateUtils;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Exists function.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class ExistsFunc extends HTMLTemplateFunc {
	public static final ExistsFunc INSTANCE = new ExistsFunc();
	
	private static final Pattern NEGATE_PATTERN = Pattern.compile("\\s*!\\s*");
	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*\\s*,");
	
	private ExistsFunc() {
		super("EXISTS", "ENDEXISTS", false);
	}
	
	@Override
	public Map<String, HTMLTemplatePlaceholder> handle(StringBuilder content, L2PcInstance player, Map<String, HTMLTemplatePlaceholder> placeholders, HTMLTemplateFunc[] funcs) {
		try {
			boolean negate = false;
			Matcher m = null;
			
			try {
				m = getMatcher(NEGATE_PATTERN, content, 0);
				negate = true;
			} catch (Exception e) {
				// ignore this exception, negate is optional
			}
			
			if (m != null) {
				m = getMatcher(PLACEHOLDER_PATTERN, content, m.end());
			} else {
				m = getMatcher(PLACEHOLDER_PATTERN, content, 0);
			}
			
			HTMLTemplatePlaceholder placeholder = HTMLTemplateUtils.getPlaceholder(m.group().substring(0, m.group().length() - 1).trim(), placeholders);
			if (((placeholder == null) && !negate) || ((placeholder != null) && negate)) {
				content.setLength(0);
				return null;
			}
			
			content.delete(0, m.end());
		} catch (Exception e) {
			content.setLength(0);
		}
		return null;
	}
	
	private static Matcher getMatcher(Pattern pattern, StringBuilder content, int findIndex) throws Exception {
		Matcher m = pattern.matcher(content);
		if (!m.find(findIndex) || (m.start() > findIndex)) {
			throw new Exception();
		}
		
		return m;
	}
}
