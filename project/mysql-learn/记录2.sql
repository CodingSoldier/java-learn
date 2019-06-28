mysql账号由两部分组成   用户名@可访问控制列表
	可访问控制列表：
		1、%，可从所有外部主机(不包括本机)访问
		2、192.168.1.%，可从网段访问
		3、localhost，从服务器本地访问

创建用户语法详情
\h create user

查看权限列表
SHOW privileges;

创建用户
create user test01@'%' identified by '123456';
授权	
GRANT select ON learn.* TO test01@'%';
GRANT select,update ON learn.* TO test01@'%';
删除权限
revoke update on learn.* from test01@'%';


导出所有用户授权语句
pt-show-grants u=root,p=cpq..123,h=localhost



sql_mode
set [session/global/persist] sql_mode='XXXXX'
session当前连接线程中有效；global全局有效，重启后无效；persist(mysql-8可用)全局、重启后也有效

only_full_group_by  select、having、order by中的非聚合列要全部出现在group by后
ansi_quotes 禁止用双引号来引用字符串

SQL_MODE='ANSI'，符合ansi标准，也称宽松模式。字符串都能插入int类型的列，存储结果为0
SQL_MODE='STRICT_TRANS_TABLES'，事物存储引擎上启动严格校验
SQL_MODE='STRICT_ALL_TABLES'，所有存储引擎上启动严格校验


线程空闲等待时间，单位是秒，默认线程空闲超过8小时就断开此线程，可以设置小一点。
SELECT @@wait_timeout;

SET GLOBAL wait_timeout=600;

mysql8  set persist 是把配置写到/var/lib/mysql/mysqld-auto.cnf中，mysql重启时先读取mysqld-auto.cnf，再读取/etc/my.cnf

pt-config-diff u=root,p=cpq..123,h=localhost /etc/my.cnf


重要参数
max_connections        mysql最大连接数
interactive_timeout    交互连接的timeout时间，对sleep状态线程有效，mysql客户端
wait_timeout           非交互连接的timeout时间，对sleep状态线程有效，jdbc连接池
max_allowed_packet     控制mysql可以接受的数据包大小，主从复制时要一致
sync_binlog            表示每写多少次缓冲会向磁盘同步一次binlog
sort_buffer_size       每个会话使用排序缓冲区的大小
join_buffer_size       设置每个会话所使用的排序缓冲区的大小
read_buffer_size       MYISAM表读缓存池的大小，必须是4K的倍数。mysql-8之前的版本，生成临时表仍然是使用MYISAM表
read_rnd_buffer_size   索引缓冲区大小
binlog_cache_size      每个会话用于缓存未提交事务缓存的大小


存储引擎参数
innodb_flush_log_at_trx_commit
	0 每秒进行一次重做日志的磁盘刷新操作
	1 每次事务提交都会刷新事务日志到磁盘中  #默认
	2 每次事务提交写入系统缓存每秒向磁盘刷新一次
innodb_buffer_pool_size         设置Innodb缓冲池的大小，应为系统（只装了mysql）可用内存的75%
innodb_buffer_pool_instances    innodb缓冲池的实例个数，每个实例个大小为总缓冲池大小/实例个数
innodb_file_per_table           设置每个表独立使用一个表空间文件 



错误日志      记录mysql在启动、运行、停止时出现的问题
常规日志      记录所有发向mysql的请求，太多，用于调试，调试完后要关闭
慢查询日志    记录符合条件的查询
二进制日志    记录全部有效的数据修改日志
中继日志      用于主从复制，临时存储主从同步的二进制日志



错误日志log_error
默认位置select @@log_error

log_error_verbosity日志级别
	1  error级别的信息
	2  error、warning级别的信息
	1  error、warning、note级别的信息

log_error_services=[日志过滤组件]  mysql8的参数
	
SELECT @@log_timestamps;  日志文件中的时间，使用的是UTC时间

SET GLOBAL log_timestamps='SYSTEM'   使用系统时间作为日志的时间
执行一条会发生错误的命令
change master to master_host='192.168.4.154';
查看错误日志
cat /var/log/mysqld.log

常规日志
SELECT @@general_log
SELECT @@log_output = file存储在文件中，table存储在表中，none不存常规日志
general_log_file  常规日志文件
mysql.general_log 常规日志表 

慢查询
SELECT @@slow_query_log        是否开启慢查询
SELECT @@slow_query_log_file   慢查询日志
long_query_time                超过xx秒则记录
log_quueries_not_using_indexes 没使用索引的查询记录到慢查询日志
log_slow_admin_statements      记录管理命令


二进制日志  log-bin
SHOW variables like 'log_bin%'
binlog_format=row | statement | mixed     记录数据、记录sql语句、混合模式
binlog_row_image=full | minimal | noblob  记录所有列的值、记录修改列的值
expire_logs-days    二进制日志过期时间
purge binary logs to 'mysql-bin.010'  删除001~009的日志
purge binary logs before '2019-04-02 22:22:22'   2019-04-02 22:22:22 之前的日志会被清除

中继日志     relay_log
relay_log   中继日志名
relay_log_purge on|off  默认on自动清理


innodb使用聚簇索引，聚簇索引的叶子节点指向数据行中的主键，所以主键最好使用自增主键，小而且有序。使用uuid会发生数据重排列
innodb事物的实现方式
原子性A 使用Undo log记录数据修改前的状态
一致性C 使用Redo log记录数据修改后的状态
隔离性I 使用共享锁、排他锁
持久性  使用undo log、redo log配合实现

MVCC多版本并发控制
简单来说，读操作如果遇到了锁，会去Undo log中读取旧的值，实现写的同时也可以读。


备份
mysqldump --help;
备份
mysqldump -uroot -pcpq..123 --databases learn > learn.sql
恢复
mysql -uroot -pcpq..123 < learn.sql
带where条件的备份
mysqldump -uroot -pcpq..123 --where "ip_start='1.0.16.0'" learn t_ip_address > t_ip_address.sql

mysqlpump备份
使用zlib压缩
mysqlpump -uroot -pcpq..123 --compress-output=zlib --databases learn > learn.zlib
解压缩
zlib_decompress learn.zlib learn.sql

使用xtrabackup
yum install -y https://repo.percona.com/yum/percona-release-latest.noarch.rpm
yum install -y percona-xtrabackup-24
-- 卸载yum源 rpm  -e --nodeps percona-release

innobackupex --help
mkdir -p /tmp/backup
-- 全备份
innobackupex --user=root --password=cpq..123 /tmp/backup/
-- 自定义目录/tmp/backup/20190628 --no-timestamp，并行线程数--parallel=2
innobackupex --user=root --password=cpq..123 /tmp/backup/20190628 --no-timestamp --parallel=2
-- 模拟灾难恢复过程，把20190628目录中的备份转为mysql文件
innobackupex --apply-log 2019-06-28_11-12-59
恢复比较麻烦


监控
SHOW GLOBAL STATUS LIKE 'Com%'
-- 更新数
SHOW GLOBAL STATUS LIKE 'Com_update'

SHOW GLOBAL STATUS WHERE variable_name IN ('Queries', 'uptime');
Queries	99
Uptime	1585

SHOW GLOBAL STATUS WHERE variable_name IN ('Queries', 'uptime');
Queries	106
Uptime	1624

每秒请求数
QPS=(106-99)/(1624-1585)


SHOW GLOBAL STATUS WHERE variable_name IN ('com_insert','com_delete','com_update', 'uptime');
SHOW GLOBAL STATUS WHERE variable_name IN ('com_insert','com_delete','com_update', 'uptime');
每秒事物处理次数
TPS

并发数
SHOW GLOBAL STATUS LIKE 'Threads_running'
当前连接数(有部分连接处于sleep状态，不属于并发)
SHOW GLOBAL STATUS LIKE 'Threads_connected'

缓存命中率
-- query_cache_type	OFF  关闭了查询缓存，下面两个值也不相等
SHOW GLOBAL STATUS LIKE 'Innodb_buffer_pool_read_requests'  从缓存池中读取的次数
SHOW GLOBAL STATUS LIKE 'Innodb_buffer_pool_reads'       从物理磁盘读取的次数
















