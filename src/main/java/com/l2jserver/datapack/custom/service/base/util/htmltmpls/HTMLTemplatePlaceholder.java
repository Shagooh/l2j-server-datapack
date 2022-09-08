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
package com.l2jserver.datapack.custom.service.base.util.htmltmpls;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the class for the built-in value placeholder.<br>
 * It has a name, a value and can contain child placeholder.<br>
 * To reference the value of a placeholder in a template document<br>
 * you use <b>%placeholder_name%</b>. To reference the value of a child<br>
 * placeholder you use <b>%placeholder_name.child_placeholder_name%.</b>
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class HTMLTemplatePlaceholder {
	/** the name of this placeholder */
	private final String name;
	/** the value of this placeholder */
	private volatile String value;
	/** the child placeholders of this placeholder */
	private final Map<String, HTMLTemplatePlaceholder> children;
	
	/**
	 * Public constructor to create a new placeholder
	 * @param name the name of the new placeholder
	 * @param value the value of the new placeholder
	 */
	public HTMLTemplatePlaceholder(String name, String value) {
		this(name, value, new LinkedHashMap<>());
	}
	
	/**
	 * Private constructor to create alias placeholders of other placeholders
	 * @param name the name of the alias placeholder
	 * @param value the value of the alias placeholder
	 * @param children the children of the alias placeholder
	 */
	private HTMLTemplatePlaceholder(String name, String value, Map<String, HTMLTemplatePlaceholder> children) {
		this.name = name;
		this.value = value;
		this.children = children;
	}
	
	/**
	 * Creates an alias for this placeholder.<br>
	 * An alias placeholder will hold the reference to the children map from the original placeholder. This means, adding a new child to the alias will also add the child to the original placeholder and vice versa.
	 * @param name name of the alias placeholder
	 * @return the newly created alias placeholder
	 */
	public HTMLTemplatePlaceholder createAlias(String name) {
		return new HTMLTemplatePlaceholder(name, value, children);
	}
	
	/**
	 * Adds a child placeholder to this placeholder.
	 * @param name the name of the new child placeholder
	 * @param value the value of the new child placeholder
	 * @return this placeholder
	 */
	public HTMLTemplatePlaceholder addChild(String name, String value) {
		children.put(name, new HTMLTemplatePlaceholder(name, value));
		return this;
	}
	
	public HTMLTemplatePlaceholder addAliasChild(String aliasName, HTMLTemplatePlaceholder placeholder) {
		children.put(aliasName, placeholder.createAlias(aliasName));
		return this;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	/**
	 * Method to get a child placeholder of this placeholder by name
	 * @param name the name of the child placeholder to find
	 * @return the child placeholder
	 */
	public HTMLTemplatePlaceholder getChild(String name) {
		return HTMLTemplateUtils.getPlaceholder(name, children);
	}
	
	/**
	 * @return the child placeholder map of this placeholder as unmodifiable map
	 */
	public Map<String, HTMLTemplatePlaceholder> getChildren() {
		return Collections.unmodifiableMap(children);
	}
	
	/**
	 * @return the count of child placeholders in this placeholder
	 */
	public int getChildrenSize() {
		return children.size();
	}
}