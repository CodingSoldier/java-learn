DROP TABLE IF EXISTS test2_01;

update test1_1 set name='回滚值rollback-name' where id = 1;

ALTER TABLE test1_1 DROP COLUMN `age` ;
