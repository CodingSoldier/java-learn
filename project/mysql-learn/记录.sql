遗留问题：
    P8 重复读取bug
    P9 死锁

P9
START TRANSACTION;
UPDATE spring_cache SET tc = '11' WHERE num=11 AND NAME = 'n1';
UPDATE spring_cache SET tc = '22' WHERE num=22 AND NAME = 'n2';
COMMIT;

START TRANSACTION;
UPDATE spring_cache SET tc = '22' WHERE num=22 AND NAME = 'n2';
UPDATE spring_cache SET tc = '11' WHERE num=11 AND NAME = 'n1';
COMMIT;









p609
符号分割文件备份(备份中没有表结构)
select * into outfile '输出文件必须是secure_file_priv的值/var/lib/mysql-files/book1.txt' fields terminated by ',' optionally enclosed by '"' lines terminated by '\n' from cpq.book;

book1.txt存储结果：
1,"12354","aaa","iii","ccc"
2,"22vv","aqa222","ii22","cc21111"

查看secure_file_priv
show variables like '%secure%'
secure_file_priv	/var/lib/mysql-files/

还原备份
load data infile '/var/lib/mysql-files/book1.txt' into table cpq.book fields terminated by ',' optionally enclosed by '"' lines terminated by '\n';


