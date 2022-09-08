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
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplateFunc;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplateUtils;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * If Children function.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class IfChildrenFunc extends HTMLTemplateFunc {
	public static final IfChildrenFunc INSTANCE = new IfChildrenFunc();
	
	private static final Pattern CHILDREN_OF_PLACEHOLDER_PATTERN = Pattern.compile("\\s*[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*\\s*,");
	
	private static final Pattern CHILD_PLACEHOLDER_PATTERN = Pattern.compile("\\s*[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*");
	
	private static final Pattern OP_PATTERN = Pattern.compile("\\s*(<|>|<=|>=|==|!=|\\sENDS_WITH\\s|\\sSTARTS_WITH\\s)\\s*");
	
	private static final Pattern RVALUE_PATTERN = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*|\"(\\\\.|\\\\\\s|\\s|[^\\\\\"])*\")");
	
	private static final Pattern THEN_PATTERN = Pattern.compile("\\s*\\sTHEN\\s");
	
	private IfChildrenFunc() {
		super("IFCHILDS", "ENDIFCHILDS", false);
	}
	
	@Override
	public Map<String, HTMLTemplatePlaceholder> handle(StringBuilder content, L2PcInstance player, Map<String, HTMLTemplatePlaceholder> placeholders, HTMLTemplateFunc[] funcs) {
		try {
			Matcher matcher = getMatcher(CHILDREN_OF_PLACEHOLDER_PATTERN, content, 0);
			String childrenPlaceholderString = matcher.group().substring(0, matcher.group().length() - 1);
			HTMLTemplatePlaceholder childrenPlaceholder = HTMLTemplateUtils.getPlaceholder(childrenPlaceholderString, placeholders);
			if (childrenPlaceholder == null) {
				content.setLength(0);
				return null;
			}
			
			matcher = getMatcher(CHILD_PLACEHOLDER_PATTERN, content, matcher.end());
			String childPlaceholderString = matcher.group().trim();
			int findIndex = matcher.end();
			
			matcher = getMatcher(OP_PATTERN, content, findIndex);
			String op = matcher.group().trim();
			findIndex = matcher.end();
			
			matcher = getMatcher(RVALUE_PATTERN, content, findIndex);
			String rValue = matcher.group();
			if (rValue.charAt(0) == '"') {
				rValue = rValue.substring(1, rValue.length() - 1);
			} else {
				rValue = HTMLTemplateUtils.getPlaceholderValue(rValue, placeholders);
			}
			findIndex = matcher.end();
			
			matcher = getMatcher(THEN_PATTERN, content, findIndex);
			findIndex = matcher.end();
			
			for (Entry<String, HTMLTemplatePlaceholder> entry : childrenPlaceholder.getChildren().entrySet()) {
				HTMLTemplatePlaceholder childPlaceholder = entry.getValue().getChild(childPlaceholderString);
				if (childPlaceholder == null) {
					continue;
				}
				
				try {
					boolean ok = false;
					switch (op) {
						case "<":
							ok = Integer.parseInt(childPlaceholder.getValue()) < Integer.parseInt(rValue);
							break;
						case ">":
							ok = Integer.parseInt(childPlaceholder.getValue()) > Integer.parseInt(rValue);
							break;
						case "<=":
							ok = Integer.parseInt(childPlaceholder.getValue()) <= Integer.parseInt(rValue);
							break;
						case ">=":
							ok = Integer.parseInt(childPlaceholder.getValue()) >= Integer.parseInt(rValue);
							break;
						case "==":
							ok = childPlaceholder.getValue().equals(rValue);
							break;
						case "!=":
							ok = !childPlaceholder.getValue().equals(rValue);
							break;
						case "ENDS_WITH":
							ok = childPlaceholder.getValue().endsWith(rValue);
							break;
						case "STARTS_WITH":
							ok = childPlaceholder.getValue().startsWith(rValue);
							break;
					}
					
					if (!ok) {
						// condition is not met, no content to show
						content.setLength(0);
						return null;
					}
				} catch (Exception e) {
					// on an exception the types are incompatible with the operator, this function ignores such cases
				}
			}
			
			content.delete(0, findIndex);
		} catch (Exception ex) {
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
