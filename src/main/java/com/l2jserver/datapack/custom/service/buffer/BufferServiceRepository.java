/*
 * Copyright © 2004-2022 L2J DataPack
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
package com.l2jserver.datapack.custom.service.buffer;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.datapack.custom.service.base.util.htmltmpls.HTMLTemplatePlaceholder;
import com.l2jserver.datapack.custom.service.buffer.model.BufferConfig;
import com.l2jserver.datapack.custom.service.buffer.model.UniqueBufflist;
import com.l2jserver.datapack.custom.service.buffer.model.entity.BuffCategory;
import com.l2jserver.datapack.custom.service.buffer.model.entity.BuffSkill;
import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Buffer Service Data.
 * @author HorridoJoho
 * @version 2.6.2.0
 */
public final class BufferServiceRepository {
	public enum BuffType {
		BUFF,
		SONG_DANCE
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(BufferServiceRepository.class);
	
	private static final String SELECT_UNIQUE_BUFF_LISTS = "SELECT ulist_id, ulist_char_id, ulist_name FROM custom_buffer_service_ulists ORDER BY ulist_char_id";
	
	private static final String SELECT_UNIQUE_BUFF_LIST = "SELECT ulist_buff_ident FROM custom_buffer_service_ulist_buffs WHERE ulist_id=?";
	
	private static final String INSERT_UNIQUE_BUFF_LIST = "INSERT INTO custom_buffer_service_ulists (ulist_char_id, ulist_name) VALUES(?, ?)";
	
	private static final String DELETE_UNIQUE_BUFF_LIST = "DELETE FROM custom_buffer_service_ulists WHERE ulist_char_id=? AND ulist_id=?";
	
	private static final String INSERT_BUFF_TO_UNIQUE_BUFF_LIST = "INSERT INTO custom_buffer_service_ulist_buffs VALUES(?, ?)";
	
	private static final String DELETE_BUFF_FROM_BUFF_LIST = "DELETE FROM custom_buffer_service_ulist_buffs WHERE ulist_id=? AND ulist_buff_ident=?";
	
	private final BufferConfig config;
	
	protected final ConcurrentHashMap<Integer, Map<Integer, UniqueBufflist>> uniqueBufflists = new ConcurrentHashMap<>();
	
	private BufferServiceRepository() {
		config = new BufferConfig();
		
		loadUniqueBufflists();
	}
	
	private void loadUniqueBufflists() {
		try (var con = ConnectionFactory.getInstance().getConnection()) {
			try (var st = con.createStatement();
				var rs = st.executeQuery(SELECT_UNIQUE_BUFF_LISTS)) {
				while (rs.next()) {
					int charId = rs.getInt("ulist_char_id");
					int ulistId = rs.getInt("ulist_id");
					String ulistName = rs.getString("ulist_name");
					
					Map<Integer, UniqueBufflist> ulists = getPlayersULists(charId);
					ulists.put(ulistId, new UniqueBufflist(ulistId, ulistName));
				}
			}
			
			for (var ulists : uniqueBufflists.entrySet()) {
				for (var ulist : ulists.getValue().entrySet()) {
					try (var ps = con.prepareStatement(SELECT_UNIQUE_BUFF_LIST)) {
						ps.setInt(1, ulist.getKey());
						try (var rs = ps.executeQuery()) {
							while (rs.next()) {
								String buffIdent = rs.getString("ulist_buff_ident");
								BuffSkill buff = config.getGlobal().getBuff(buffIdent);
								if (buff == null) {
									LOG.warn("BufferService - Data: Buff with ident does not exists!");
								} else {
									ulist.getValue().add(buff);
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			LOG.error("Error loading unique buffs!", ex);
		}
	}
	
	private Map<Integer, UniqueBufflist> getPlayersULists(int playerObjectId) {
		return uniqueBufflists.computeIfAbsent(playerObjectId, k -> new LinkedHashMap<>());
	}
	
	private UniqueBufflist getPlayersUList(int playerObjectId, String ulistName) {
		Map<Integer, UniqueBufflist> ulists = getPlayersULists(playerObjectId);
		for (Entry<Integer, UniqueBufflist> entry : ulists.entrySet()) {
			if (entry.getValue().ulistName.equals(ulistName)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public boolean createUniqueBufflist(int playerObjectId, String ulistName) {
		// prevent duplicate entry
		if (getPlayersUList(playerObjectId, ulistName) != null) {
			return false;
		}
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(INSERT_UNIQUE_BUFF_LIST, RETURN_GENERATED_KEYS)) {
			ps.setInt(1, playerObjectId);
			ps.setString(2, ulistName);
			ps.executeUpdate();
			try (var rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					int newId = rs.getInt(1);
					getPlayersULists(playerObjectId).put(newId, new UniqueBufflist(newId, ulistName));
				}
				return true;
			}
		} catch (Exception ex) {
			LOG.warn("Failed to insert unique bufflist!", ex);
			return false;
		}
	}
	
	public void deleteUniqueBufflist(int playerObjectId, String ulistName) {
		UniqueBufflist ulist = getPlayersUList(playerObjectId, ulistName);
		if (ulist == null) {
			return;
		}
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(DELETE_UNIQUE_BUFF_LIST)) {
			ps.setInt(1, playerObjectId);
			ps.setInt(2, ulist.ulistId);
			ps.executeUpdate();
			getPlayersULists(playerObjectId).remove(ulist.ulistId);
		} catch (Exception ex) {
			LOG.warn("Failed to delete unique bufflist!", ex);
		}
	}
	
	public boolean addToUniqueBufflist(L2PcInstance player, String ulistName, BuffSkill buff) {
		UniqueBufflist ulist = getPlayersUList(player.getObjectId(), ulistName);
		// prevent duplicate entry with ulist.contains(buff)
		if ((ulist == null) || ulist.contains(buff) || ((buff.getType() == BuffType.BUFF) && (ulist.numBuffs >= player.getStat().getMaxBuffCount())) || ((buff.getType() == BuffType.SONG_DANCE) && (ulist.numSongsDances >= Configuration.character().getMaxDanceAmount()))) {
			return false;
		}
		
		if (addToUniqueBufflist(player.getObjectId(), ulist.ulistId, buff.getId())) {
			ulist.add(buff);
			return true;
		}
		return false;
	}
	
	private boolean addToUniqueBufflist(int playerObjectId, int ulistId, String buffId) {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(INSERT_BUFF_TO_UNIQUE_BUFF_LIST)) {
			ps.setInt(1, ulistId);
			ps.setString(2, buffId);
			ps.executeUpdate();
		} catch (Exception ex) {
			LOG.warn("Failed to insert buff into unique bufflist!", ex);
			return false;
		}
		return true;
	}
	
	public void removeFromUniqueBufflist(int playerObjectId, String ulistName, BuffSkill buff) {
		UniqueBufflist ulist = getPlayersUList(playerObjectId, ulistName);
		if ((ulist == null) || !ulist.contains(buff)) {
			return;
		}
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(DELETE_BUFF_FROM_BUFF_LIST)) {
			ps.setInt(1, ulist.ulistId);
			ps.setString(2, buff.getId());
			ps.executeUpdate();
			ulist.remove(buff);
		} catch (Exception ex) {
			LOG.warn("Failed to remove buff from unique bufflist!", ex);
		}
	}
	
	public BufferConfig getConfig() {
		return config;
	}
	
	public boolean canHaveMoreBufflists(L2PcInstance player) {
		return getPlayersULists(player.getObjectId()).size() < Configuration.bufferService().getMaxUniqueLists();
	}
	
	public boolean hasUniqueBufflist(int playerObjectId, String ulistName) {
		return getPlayersUList(playerObjectId, ulistName) != null;
	}
	
	public List<BuffSkill> getUniqueBufflist(int playerObjectId, String ulistName) {
		UniqueBufflist ulist = getPlayersUList(playerObjectId, ulistName);
		if (ulist == null) {
			return null;
		}
		return Collections.unmodifiableList(ulist);
	}
	
	public BuffCategory getBuffCategory(String categoryIdent) {
		return getConfig().getGlobal().getCategory(categoryIdent);
	}
	
	public HTMLTemplatePlaceholder getBuffCategoryPlaceholder(String categoryIdent) {
		BuffCategory cat = getBuffCategory(categoryIdent);
		if (cat == null) {
			return null;
		}
		
		return cat.getPlaceholder();
	}
	
	public HTMLTemplatePlaceholder getPlayersUListPlaceholder(int playerObjectId, String ulistName) {
		UniqueBufflist ulist = getPlayersUList(playerObjectId, ulistName);
		if (ulist == null) {
			return null;
		}
		return ulist.placeholder;
	}
	
	public HTMLTemplatePlaceholder getPlayersUListsPlaceholder(int playerObjectId) {
		Map<Integer, UniqueBufflist> ulists = getPlayersULists(playerObjectId);
		if (ulists.isEmpty()) {
			return null;
		}
		
		HTMLTemplatePlaceholder placeholder = new HTMLTemplatePlaceholder("uniques", null);
		for (Entry<Integer, UniqueBufflist> entry : ulists.entrySet()) {
			placeholder.addAliasChild(String.valueOf(placeholder.getChildrenSize()), entry.getValue().placeholder);
		}
		return placeholder;
	}
	
	public static BufferServiceRepository getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		static final BufferServiceRepository INSTANCE = new BufferServiceRepository();
	}
}
