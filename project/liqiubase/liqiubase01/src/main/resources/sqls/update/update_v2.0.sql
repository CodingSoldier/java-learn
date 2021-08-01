CREATE TABLE `test2_01` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 添加字段
ALTER TABLE test1_1 ADD COLUMN  `age` int(11) NOT NULL DEFAULT '0' COMMENT '年纪默认0';

INSERT INTO `test2_01` (`id`, `name`) VALUES (1, '原始值ttt--test2_01');

update test1_1 set name='更新值nnnn' where id = 1;


INSERT INTO `test1_2` (`id`, `name`) VALUES (1, '更新值test1_2222');

