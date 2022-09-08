CREATE TABLE IF NOT EXISTS `custom_buffer_service_ulist_buffs` (
  `ulist_id` int(10) unsigned NOT NULL,
  `ulist_buff_ident` varchar(255) NOT NULL,
  PRIMARY KEY (`ulist_id`,`ulist_buff_ident`),
  CONSTRAINT `custom_buffer_service_ulist_buffs_ibfk_1` FOREIGN KEY (`ulist_id`) REFERENCES `custom_buffer_service_ulists` (`ulist_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;