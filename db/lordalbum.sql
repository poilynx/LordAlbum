DROP TABLE IF EXISTS `file`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`username` varchar(45) UNIQUE NOT NULL,
	`password` varchar(45) NOT NULL,
	PRIMARY KEY (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `file` (
	`id` integer(11) NOT NULL AUTO_INCREMENT,
	`user_id` integer(11) NOT NULL,
	`filename` varchar(45) NOT NULL,
	`content` mediumblob NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES user (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

