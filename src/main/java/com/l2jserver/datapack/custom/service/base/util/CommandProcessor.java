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
package com.l2jserver.datapack.custom.service.base.util;

import java.util.Objects;

/**
 * Command processor.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class CommandProcessor {
	private String remaining;
	
	public CommandProcessor(String command) {
		Objects.requireNonNull(command);
		remaining = command;
	}
	
	public boolean matchAndRemove(String... expectations) {
		Objects.requireNonNull(expectations);
		for (String expectation : expectations) {
			Objects.requireNonNull(expectation);
			if (!expectation.isEmpty() && remaining.startsWith(expectation)) {
				remaining = remaining.substring(expectation.length());
				return true;
			}
		}
		return false;
	}
	
	public String[] splitRemaining(String regex) {
		return remaining.split(regex);
	}
	
	public String getRemaining() {
		return remaining;
	}
}
