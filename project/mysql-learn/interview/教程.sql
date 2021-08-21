B+树相比于B树的优点：
    B+树的索引可以储存在内存中，通过内存索引查找数据，磁盘IO更少
    B+树的查询效率稳定，查询路径都是从根节点一路走到叶子节点，叶子节点可能存的是数据指针、数据主键
    B+树由于叶子节点有序并且有指针指向下一叶子节点，扫描批量数据更方便

hash索引仅能满足相等操作，不能用于范围查询、排序，无法用于组合索引，有hash重复问题

Innodb主键组织 在索引中，数据存储在叶子节点？

密集索引可以理解为叶子节点存储主键外还保存其他列的数据
稀疏索引可以理解为叶子节点保存主键和行数据地址指针
Innodb主键使用密集索引，如果没有定义主键，则Innodb会新建虚拟主键
主键索引：主键和数据都在叶子节点，只有一次查找
非主键索引：叶子节点不存储数据地址，而是存储主键值，然后通过主键去查找数据，所以要有两次查找才能找到数据

# 1.找到mysql的my.cnf配置文件，将max_heap_table_size改大些，改成4000M，重启下mysql服务即可。

#创建一张内存表
CREATE TABLE `person_info_memory` (
    `id` INT (7) NOT NULL AUTO_INCREMENT,
    `account` VARCHAR (10),
    `name` VARCHAR (20),
    `area` VARCHAR (20),
    `title` VARCHAR (20),
    `motto` VARCHAR (50),
    PRIMARY KEY (`id`),
    UNIQUE(`account`),
    KEY `index_area_title`(`area`,`title`)
) ENGINE = MEMORY AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8
#创建一张店铺小数据表
CREATE TABLE `shop_info_small` (
    `shop_id` INT (2) NOT NULL AUTO_INCREMENT,
    `shop_name` VARCHAR (20),
    `person_id` INT (2),
    `shop_profile` VARCHAR (50),
    PRIMARY KEY (`shop_id`),
    UNIQUE(`shop_name`)
) ENGINE = MYISAM AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8
#创建一张小数据表
CREATE TABLE `person_info_small` (
    `id` INT (2) NOT NULL AUTO_INCREMENT,
    `account` VARCHAR (10),
    `name` VARCHAR (20),
    `area` VARCHAR (20),
    `title` VARCHAR (20),
    `motto` VARCHAR (50),
    PRIMARY KEY (`id`),
    UNIQUE(`account`),
    KEY `index_area_title`(`area`,`title`)
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8
#创建一张大数据表
CREATE TABLE `person_info_large` (
    `id` INT (7) NOT NULL AUTO_INCREMENT,
    `account` VARCHAR (10),
    `name` VARCHAR (20),
    `area` VARCHAR (20),
    `title` VARCHAR (20),
    `motto` VARCHAR (50),
    PRIMARY KEY (`id`),
    UNIQUE(`account`),
    KEY `index_area_title`(`area`,`title`)
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8

#创建一个能够返回随机字符串mysql自定义函数
DELIMITER $$
CREATE FUNCTION `rand_string`(n INT) RETURNS varchar(255) CHARSET utf8
BEGIN
DECLARE chars_str varchar(100) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
DECLARE return_str varchar(255) DEFAULT '' ;
DECLARE i INT DEFAULT 0;
WHILE i < n DO
SET return_str = concat(return_str,substring(chars_str , FLOOR(1 + RAND()*12 + RAND()*50),1));
SET i = i +1;
END WHILE;
RETURN return_str;
END $$
#创建一个批量往内存表里灌数据的存储过程
DELIMITER $$
CREATE  PROCEDURE `add_person_info_large`(IN n int)
BEGIN
  DECLARE i INT DEFAULT 1;
    WHILE (i <= n ) DO
      INSERT into person_info_memory  (account,name,area,title, motto) VALUEs (rand_string(10),rand_string(20),rand_string(20) ,rand_string(20),rand_string(50));
            set i=i+1;
    END WHILE;
END $$
#创建一个批量往小表里灌数据的存储过程
DELIMITER $$
CREATE  PROCEDURE `add_person_info_small`(IN n int)
BEGIN
  DECLARE i INT DEFAULT 1;
    WHILE (i <= n ) DO
      INSERT into person_info_small  (account,name,area,title, motto) VALUEs (rand_string(10),rand_string(20),rand_string(20) ,rand_string(20),rand_string(50));
            set i=i+1;
    END WHILE;
END $$
#调用存储过程，插入100万条数据(由于我们的随机数可能会出现重复的情况，所以插入的条数也许达不到100万便会出错停止,可以自行加入些随机数优化一下)
CALL add_person_info_large(1000000);
#调用存储过程，插入10条数据到小表里
CALL add_person_info_small(2);
#将内存表的数据移动到person_info_large中
insert into person_info_large(account,name,area,title,motto)
select account,name,area,title,motto from person_info_memory;
#若遇数据冲突没法到达100万的情况，通过变换唯一键值的方式来插入数据
insert into person_info_large(account,name,area,title,motto)
select concat(substring(account, 2),'a'),concat(substring(name, 2),'a'),area,title,motto from person_info_memory;
insert into person_info_large(account,name,area,title,motto)
select concat(substring(account, 2),'b'),concat(substring(name, 2),'b'),area,title,motto from person_info_memory;


CREATE TABLE `test_myisam` (
    `id` INT (2) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR (20),
    `unique_id` INT (2),
    `normal_id` INT (2),
    PRIMARY KEY (`id`),
    UNIQUE(`unique_id`),
    INDEX(`normal_id`)
) ENGINE = MYISAM AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8

CREATE TABLE `test_innodb` (
    `id` INT (2) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR (20),
    `unique_id` INT (2),
    `normal_id` INT (2),
    PRIMARY KEY (`id`),
    UNIQUE(`unique_id`),
    INDEX(`normal_id`)
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8
insert into test_innodb (name,unique_id,normal_id) values('a',1,1),('d',4,4),('h',8,8),('k',11,11);
insert into test_myisam (name,unique_id,normal_id) values('a',1,1),('d',4,4),('h',8,8),('k',11,11);



EXPLAIN SELECT name FROM person_info_large ORDER BY `name` DESC;
EXPLAIN SELECT account FROM person_info_large ORDER BY `account` DESC;


# EXPLAIN SQL 主要查看type、extra， rows似乎没什么用

# 使用的索引竟然是有unique索引的account
EXPLAIN SELECT COUNT(id) FROM person_info_large;
# 强制使用主键索引
EXPLAIN SELECT COUNT(id) FROM person_info_large FORCE INDEX (PRIMARY);
# 使用account索引的原因大致是因为account索引叶子节点没有数据，但是聚簇索引叶子节点包含数据，索引使用account索引能在内存中加载更多的数据
# 而且使用主键索引确实比使用unique索引慢


# 联合索引，最左匹配原则
KEY `index_area_title` (`area`,`title`)
# 能用到索引
EXPLAIN SELECT * FROM person_info_large WHERE title='1rmDlCfwxtF0BwsnMFJW' AND area='ZXnPJYkFsCROxI2ZJsnA';
# 能用到索引
EXPLAIN SELECT * FROM person_info_large WHERE area='ZXnPJYkFsCROxI2ZJsnA' AND title='1rmDlCfwxtF0BwsnMFJW';
# 能用到索引
EXPLAIN SELECT * FROM person_info_large WHERE name='1rmDlCfwxtF0BwsnMFJW' AND area='ZXnPJYkFsCROxI2ZJsnA';
# 没用到
EXPLAIN SELECT * FROM person_info_large WHERE title='ZXnPJYkFsCROxI2ZJsnA' AND name='1rmDlCfwxtF0BwsnMFJW';

最左前缀匹配原则
    1、mysql会一直向右匹配直到遇到范围查询(> < between like)就停止匹配，
    比如：简历索引(a,b,c,d)的联合索引，查询语句是：a=3 and b=4 and c>5 and d=6 只用到了a、b索引
    2、=和in可以乱序，比如：索引(a,b,c)，查询语句 b=2 and a=1 and c=3 也能用到索引，mysql查询优化器会将SQL语句优化成可以识别的形式


update、delete、insert这些语句也会有分为好几步3操作，比方根据where条件查找多行、更新多行，但这多次的读、写操作都会被锁锁住
单独 select 的SQL语句不会锁住增删改查操作


having 用于对函数进行条件筛选，where 不能作用于函数
# HAVING可以筛选函数
SELECT user_id, avg(`system_user_id`) FROM t_user_system_mapper GROUP BY `user_id` HAVING AVG(`system_user_id`)>60;

# 报错，WHERE不可以筛选函数
SELECT user_id, avg(`system_user_id`) FROM t_user_system_mapper WHERE AVG(`system_user_id`)>60 GROUP BY `user_id`;



# 索引失效  https://segmentfault.com/a/1190000021464570

# 使用指定索引  https://jiajunhuang.com/articles/2019_09_25-mysql_use_index.md.html














