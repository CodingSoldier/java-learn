###################基于日志点的复制###################################

vim /etc/my.cnf
#禁用validate-password功能，通过mysql 语句修改validate-password只在当前环境变量下有效，重启mysql后无效
validate-password=0

# 二进制日志
server_id=1
log_bin=master_log
max_binlog_size=1000M
binlog_format=row
expire_logs_days=7
sync_binlog=1


#主创建复制账号
create user repl@'192.168.1.%' identified by '123456';
GRANT REPLICATION SLAVE ON *.* TO repl@'192.168.1.%' IDENTIFIED BY '123456';


备份主库的test01数据库，导出来的all.sql似乎不太好用，还有changelog的信息
还不如通过navicat同步数据
mysqldump --single-transaction --master-data --triggers --routines -uroot -pcpq..123 --databases test01 >> all.sql;

删除binlog
rm -rf master_log.*




备库修改
#之前做过备库，需要停止备库
stop slave
#需要重置备库
reset slave
#删除主备旧的log_bin文件

改uuid
vim /etc/sysconfig/network-scripts/ifcfg-enp0s3
vim /var/lib/mysql/auto.cnf


vim /etc/my.cnf
#password功能，通过mysql 语句修改validate-password只在当前环境变量下有效，重启mysql后无效
validate-password=0
server_id=2
log_bin=/var/lib/mysql/master_log
max_binlog_size=1000M
binlog_format=row
expire_logs_days=7
sync_binlog=1

relay_log=/var/lib/mysql/mysql_relay_log
log_slave_updates=on
read_only=on



# 导入sql
# mysql -uroot -p密码 < all.sql



#重启数据库
service mysqld restart

#设置备库
# master_log_file='mysql-log-bin.000001', \
# master_log_pos=0;
# 这两个参数通过mysqldump导出的SQL中有
-- CHANGE MASTER TO MASTER_LOG_FILE='master_log.000003', MASTER_LOG_POS=154;

change master to master_host='192.168.1.196', \
	master_user='repl', \
	master_password='123456', \
	master_log_file='master_log.000005', \
	master_log_pos=154;

show slave status \G;

start slave; 


从节点查看线程
SHOW PROCESSLIST








stop slave;   
reset slave;
start slave; 
show slave status \G; 


删除log
rm -rf master_log.* mysql-log-bin.*

# 主库自定义配置
#不使用密码强度规则
validate-password=0
# 主库开启日志功能、设置server_id
log_bin=/var/lib/mysql/mysql-log-bin
server_id=10
#日志同步到磁盘
sync_binlog=1
#一个日志最大容量
max_binlog_size = 500M
#日志过期时间
expire_logs_days = 9



#备库自定义配置
#不使用密码强度规则
validate-password=0
#从数据库
log_bin=/var/lib/mysql/mysql-log-bin
server_id=2
relay_log=/var/lib/mysql/mysql-relay-bin
#备库从主库获取数据后更新自己的数据，这些数据/操作默认不会写入binlog，为了能让此备库在以后可以升级为主库。
#在复制过程中记录自己的二进制日志，备库也可以变成主库
log_slave_updates=1
#备库重启后不启动复制，需要手动开启
#skip_slave_start
#不常用
read_only 
sync_master_info=1
sync_relay_log=1
sync_relay_log_info=1
#复制出错后，跳过所有错误
slave-skip-errors=all
#一个日志最大容量
max_binlog_size = 500M
#日志过期时间
expire_logs_days = 9






##############################gtid配置##################################

主服务器
vim /etc/my.cnf

validate-password=0

# 二进制日志
server_id=1
log_bin=master_log
max_binlog_size=1000M
binlog_format=row
expire_logs_days=7
sync_binlog=1

#gtid配置
gtid_mode=on
enforce-gtid_consistency=on


systemctl restart mysqld


备库
vim /etc/my.cnf

validate-password=0
server_id=2
log_bin=/var/lib/mysql/master_log
max_binlog_size=1000M
binlog_format=row
expire_logs_days=7
sync_binlog=1

relay_log=/var/lib/mysql/mysql_relay_log
# log_slave_updates=on
read_only=on

# gtid配置
gtid_mode=on
enforce_gtid_consistency=on
master_info_repository=TABLE
relay_log_info_repository=TABLE


stop slave;
reset slave;

systemctl restart mysqld


主库
mysqldump --single-transaction --master-data=2 --triggers --routines -uroot -pcpq..123 --databases test01 >> all2.sql;


备库
change master to master_host='192.168.1.196', \
	master_user='repl', \
	master_password='123456', \
	master_auto_position=1;

start slave;

show slave status \G;



后面的5表示执行了5个事务
Retrieved_Gtid_Set: da776ac6-941c-11e9-a91c-08002759e027:1-5
Executed_Gtid_Set: da776ac6-941c-11e9-a91c-08002759e027:1-5

从库发生异常，可以跳过一个事务
Retrieved_Gtid_Set: da776ac6-941c-11e9-a91c-08002759e027:1-5
Executed_Gtid_Set: da776ac6-941c-11e9-a91c-08002759e027:1-4
接收到的gtid是da776ac6-941c-11e9-a91c-08002759e027:1-5，当前执行到了da776ac6-941c-11e9-a91c-08002759e027:1-4
第5个事务无法执行，跳过第5个事务
stop slave;
show variables like 'gtid_next';
gtid_next	AUTOMATIC
set gtid_next='da776ac6-941c-11e9-a91c-08002759e027:1-5';
-- 注入一个空事务
begin;commit;
-- 还原gtid_next
set gtid_next='AUTOMATIC';
start slave;






