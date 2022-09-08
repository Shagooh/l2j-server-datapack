ALTER TABLE `characters` ADD COLUMN `nevit_blessing_time` INT UNSIGNED NOT NULL DEFAULT 0 AFTER `vitality_points`;
ALTER TABLE `characters` ADD COLUMN `nevit_blessing_points` INT UNSIGNED NOT NULL DEFAULT 0 AFTER `vitality_points`;
ALTER TABLE `characters` ADD COLUMN `hunting_bonus` INT UNSIGNED NOT NULL DEFAULT 0 AFTER `vitality_points`;