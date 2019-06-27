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
interactive_timeout    交互连接的timeout时间，对sleep状态线程有效
wait_timeout           非交互连接的timeout时间，对sleep状态线程有效
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






















