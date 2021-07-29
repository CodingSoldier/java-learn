CREATE TABLE `test02` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `test02`(`name`) VALUES ('aaaaa');
INSERT INTO `test02`(`id`, `name`) VALUES (2, 'vvvv2222');
