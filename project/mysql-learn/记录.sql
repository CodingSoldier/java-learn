mysql 数据存储位置 /var/lib/mysql
	tableName.frm  存储表结构


Innodb存储数据的方式
SHOW VARIABLES LIKE 'innodb_file_per_table';
innodb_file_per_table	ON
    表数据存储在独立表空间  tableName.idb  

改变innodb_file_per_table为OFF  
SET GLOBAL innodb_file_per_table='OFF';
新建t2表
/var/lib/mysql/learn/ 目录下只有t2.frm，没有t2.idb，数据存储在共享表空间/var/lib/mysql/ibdata1

Redo Log、Undo Log 与事务关系
Redo Log重做日志，存储已经提交的事务，实现事务持久性
	重做事物缓冲区，每秒刷新到硬盘
	innodb_log_buffer_size	16777216  #单位是字节，16M

	重做日志文件
	SHOW VARIABLES LIKE 'innodb_log_files_in_group';
	innodb_log_files_in_group	2
	有两个文件/var/lib/mysql/目录下的ib_logfile0、ib_logfile1


Undo Log回滚日志，存储未提交的事务


mysql读锁是共享锁，写锁是独占锁；读锁与读锁可以并发执行，写锁与任何锁都互斥
执行
begin
UPDATE table_name SET field1=new-value1

另一个标签页执行
select * from table_name where field1=new-value1;
仍然可以获取数据，似乎没有被写锁排斥，造成这样的原因是此时select语句是读取Undo Log中的数据



# 表级锁
LOCK TABLE t WRITE;
# 在另一个标签页执行查询，查询被阻塞
SELECT * FROM t;
#解锁后查询被放行
UNLOCK TABLES;


mysql读取配置文件的顺序
/etc/my.cnf   /etc/mysql/my.nf  /home/mysql/my.cnf   ~/.my.cnf

设置全局参数
set global 参数名=参数值
set @@global.参数名:=参数值 (在sql语句中使用)
set @@global.参数名=参数值
有些参数即便设置为全局有效，但对于之前创建的mysql连接，任然使用旧的参数值，新建的连接才使用新参数值。例如wait_timeout、interactive_timeout

会话参数
set [session] 参数名=参数值
set @@session.参数名:=参数值 (在sql语句中使用)
set @@session.参数名=参数值


为每个连接线程分配的缓冲
sort_buffer_size   每个连接排序缓冲区的内存
join_buffer_size   连接缓冲区，每连接查询一张表，申请一个
read_buffer_size   myisam读缓冲区大小，要是4K的倍数
read_rnd_buffer_size   查询索引缓冲区大小

缓冲池内存
Innodb_buffer_pool_size 官方建议为mysql服务器宿主机75%以上



Innodb_log_file_size  单个事物日志的大小
Innodb_log_files_in_group 事物日志文件个数，这个不需要改
事物日志中大小 Innodb_log_file_size * Innodb_log_files_in_group

事物日志不是每次提交事物都写入事物日志中，而是先写入事物日志缓冲区
事物日志缓冲区 Innodb_log_buffer_size，一秒后写入事物日志文件



Innodb_flush_method=O_DIRECT  避免Innodb、操作系统双缓存


expire_logs_days  指定自动清理binlog的天数
max_allowed_packet 控制Mysql可以接受的包的大小，可以设置大点

read_only 禁止非super权限用户写权限，用户备库中
skip_slave_start 禁用slave自动恢复
sql_mode  only_fuul_group_by  group by 后要列出查询列中所有非聚合函数的列
sync_binlog 默0是系统决定，主库要设置为1，每次事务完成后都写二进制日志文件，避免主库崩溃，有一部分二进制日志在缓冲中，导致主备不一致

max_connections 最大连接数，默认100，太小，可以设置为2000


基准测试 mysqlslap
mysqlslap --password=密码 --concurrency=1,50,100 --iterations=3 --number-int-cols=5 --number-char-cols=5 --auto-generate-sql --auto-generate-sql-add-autoincrement --engine=innodb --number-of-queries=10 --create-schema=sbtest

--only-print  打印sql，不运行测试
mysqlslap --password=密码 --concurrency=1,50,100 --iterations=3 --number-int-cols=5 --number-char-cols=5 --auto-generate-sql --auto-generate-sql-add-autoincrement --engine=innodb --number-of-queries=10 --create-schema=sbtest --only-print



基准测试sysbench
curl -s https://packagecloud.io/install/repositories/akopytov/sysbench/script.rpm.sh | sudo bash
sudo yum -y install sysbench

















