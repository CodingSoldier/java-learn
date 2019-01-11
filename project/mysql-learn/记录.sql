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