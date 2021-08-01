CREATE TABLE `test1_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `test1_2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `test1_1`(`name`) VALUES ('aaa');
INSERT INTO `test1_1`(`name`) VALUES ('bb');
INSERT INTO `test1_1`(`name`) VALUES ('ccc');

INSERT INTO `test1_2`(`name`) VALUES ('test1_2222');
INSERT INTO `test1_2`(`name`) VALUES ('test1_2222222');

