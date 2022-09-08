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
package com.l2jserver.datapack.custom.service.buffer.model.entity;

import com.l2jserver.datapack.custom.service.base.model.entity.CustomServiceProduct;
import com.l2jserver.datapack.custom.service.buffer.BufferServiceRepository.BuffType;
import com.l2jserver.datapack.custom.service.buffer.model.BufferConfig;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * Buff skill.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public class BuffSkill extends CustomServiceProduct {
	private int skill;
	private int level;
	private BuffType type;
	
	public void afterDeserialize(BufferConfig config) {
		super.afterDeserialize();
		
		final Skill skill = getSkill();
		getPlaceholder().addChild("skill_id", String.valueOf(skill.getId())).addChild("skill_name", skill.getName()).addChild("skill_icon", skill.getIcon()).addChild("type", type.toString());
	}
	
	public int getSkillId() {
		return skill;
	}
	
	public int getSkillLevel() {
		return level;
	}
	
	public BuffType getType() {
		return type;
	}
	
	public Skill getSkill() {
		return SkillData.getInstance().getSkill(skill, level);
	}
	
	@SuppressWarnings("unused")
	private String getClientSkillIconSource(int skillId) {
		String format;
		if (skillId < 100) {
			format = "00" + skillId;
		} else if (skillId < 1000) {
			format = "0" + skillId;
		} else if (skillId == 1517) {
			format = "1499";
		} else if (skillId == 1518) {
			format = "1502";
		} else {
			if ((skillId > 4698) && (skillId < 4701)) {
				format = "1331";
			} else if ((skillId > 4701) && (skillId < 4704)) {
				format = "1332";
			} else {
				format = Integer.toString(skillId);
			}
		}
		
		return "icon.skill" + format;
	}
}
