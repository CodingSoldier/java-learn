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
Redo Log重做日志，存储已经提交的数据，实现事务持久性
	重做事物缓冲区，每秒刷新到硬盘
	innodb_log_buffer_size	16777216  #单位是字节，16M

	重做日志文件
	SHOW VARIABLES LIKE 'innodb_log_files_in_group';
	innodb_log_files_in_group	2
	有两个文件/var/lib/mysql/目录下的ib_logfile0、ib_logfile1


Undo Log回滚日志，存储旧的数据值


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
sql_mode  only_full_group_by  group by 后要列出查询列中所有非聚合函数的列
sync_binlog 默0是系统决定，主库要设置为1，每次事务完成后都写二进制日志文件，避免主库崩溃，有一部分二进制日志在缓冲中，导致主备不一致

max_connections 最大连接数，默认100，太小，可以设置为2000


基准测试 mysqlslap
mysqlslap --password=密码 --concurrency=1,50,100 --iterations=3 --number-int-cols=5 --number-char-cols=5 --auto-generate-sql --auto-generate-sql-add-autoincrement --engine=innodb --number-of-queries=10 --create-schema=sbtest

--only-print  打印sql，不运行测试
mysqlslap --password=密码 --concurrency=1,50,100 --iterations=3 --number-int-cols=5 --number-char-cols=5 --auto-generate-sql --auto-generate-sql-add-autoincrement --engine=innodb --number-of-queries=10 --create-schema=sbtest --only-print



基准测试sysbench
curl -s https://packagecloud.io/install/repositories/akopytov/sysbench/script.rpm.sh | sudo bash
yum -y install sysbench


cd /tmp
sysbench --test=fileio --file-total-size=1G prepare

sysbench --test=fileio --num-threads=8 --file-total-size=1G --file-test-mode=rndrw --report-interval=1 run

cd /usr/share/sysbench/tests/include/oltp_legacy/
sysbench --test=./oltp.lua --mysql-table-engine=innodb --oltp-table-size=10000 --mysql-db=imooc --mysql-user=root --mysql-password=cpq..123 --oltp-tables-count=10 --mysql-socket=/var/lib/mysql/mysql.sock prepare

sysbench --test=./oltp.lua --mysql-table-engine=innodb --oltp-table-size=10000 --mysql-db=imooc --mysql-user=root --mysql-password=cpq..123 --oltp-tables-count=10 --mysql-socket=/var/lib/mysql/mysql.sock run


整数类型
tinynit      1字节      -128~127                    0~255
smallint     2字节      -32768~32767                0~65535
mediumint    3字节      -8388608~8388607            0~16777215
int          4字节      -2147483648~2147483647      0~4294967295
bigint       8字节

实数类型
float     4字节   非精确类型
double    8字节   非精确类型
decimal   每4个字节存9个数字，小数点占一个字节     是
非精确类型 说明：
比方说两个double数据使用sum()函数求和，-101.1 + 80.5 得到的结果可能是 -20.599999999999994，这种情况在代码中也存在。

varchar(长度)、char(长度) 中的长度是以字符为单位的，而不是以字节为单位
char类型：字符串末尾空格会被删除，最大宽度255
char类型更新比varchar快，因为char定长，更新不会导致叶分裂，varchar更新后长度增加，可能导致叶分裂

datatime类型默认 YYYY-MM-DD HH:MM:SS，占8字节
timestamp时间戳，以YYYY-MM-DD HH:MM:SS显示，占4字节，实际上int类型，时间范围是1970-01-01~2038-01-19，在不同时区下，时间值不同

date类型，存年月日，储存生日，3字节
time类型，存时间部分


二进制日志格式
SHOW VARIABLES LIKE '%binlog_format%'

改成段模式
SET GLOBAL binlog_format='statement'


开启bin_log
vim /etc/my.cnf
加入配置
# server_id一定要有，不然重启会报错
server_id=100  
log_bin=master_log	

service mysqld restart 

#查看binlog，看到SQL语句
mysqlbinlog master_log.000001

#新建binlog文件
flush logs;


改回默认段的模式
SET GLOBAL binlog_format='row'
避免了主从复制不一致的问题，记录的是每一行的数据修改，而不是记录SQL语句

#加入 -vv 才能看懂增删改查，但也不是展示完整的SQL语句，而是一般SQL、一半数据
mysqlbinlog -vv  master_log.000003

改成
SET GLOBAL binlog_row_image="minimal";  或者noblob可以减少二进制文件的大小，只记录更新的行


混合日志格式binlog_format，能用SQL语句记录的就用SQL，不能用SQL的，比如uuid()，就用row格式


建议使用mixed或者row格式
当使用row格式时，建议设置binlog_row_image=minimal


从服务器查看线程
SHOW PROCESSLIST;


使用多线程复制
stop slave;
set global slave_parallel_type='logical_clock';
set global slave_parallel_workers=4;
start slave;


SHOW PROCESSLIST;

SHOW VARIABLES LIKE '%slave_parallel_type%';

SHOW VARIABLES LIKE '%slave_parallel_workers%';




索引使用sakila数据库演示

B-tree索引的叶子节点存放数据主键
mysql发生一次io，默认能读取16K，索引比数据行要小，所以读取一页能获取索引数量比数据行数多
索引使用键值存放，排序的时候可以避免生成临时表
B-tree索引有顺序，mysql查找索引是顺序io


索引列上不能使用表达式或函数
where to_days(out_date)  out_date的索引无效

覆盖索引，索引中存放了select子句中要查询的值，explain的Extra行显示Using index则为使用了覆盖索引。比方说select的查询列只包含主键，主键值就在索引的叶子节点上


#Extra结果是Using index condition;
EXPLAIN SELECT * FROM rental WHERE rental_date='2005-05-09' ORDER BY inventory_id, customer_id;
#Extra结果是Using index condition; Using filesort。
#因为order by的排序和索引排序不一致，mysql无法利用索引完成排序操作的时候，MySQL Query Optimizer 不得不选择相应的排序算法来实现。
EXPLAIN SELECT * FROM rental WHERE rental_date='2005-05-09' ORDER BY inventory_id DESC, customer_id;
#Extra结果Using where; Using filesort。最左边的列使用范围查找，右边的列将无法使用索引
EXPLAIN SELECT * FROM rental WHERE rental_date>'2005-05-09' ORDER BY inventory_id, customer_id;

使用Hash索引，多加一列存储需要索引列的hash值，查询时候在where后加上hash列，需要索引的列作为条件；还需要加上索引列是因为不同值的hash值可能会重复


Innodb使用行级锁，但只有在Innodb存储引擎层过滤掉不需要的行时才有效，如果储存引擎不能过滤行，则需要锁定所有行
拷贝actor表
use sakila; 
begin;
select * from actor_copy1 where last_name='WOOD' for update;

另一个标签页
begin;
select * from actor_copy1 where last_name='Wills' for update;
能更新成功

所以连接回滚
rollback;

删除last_name索引，重复上面的操作，发现
select * from actor_copy1 where last_name='Wills' for update;
未能执行，由于Innodb未能通过索引筛选出last_name='WOOD'的行，所以全表锁定

删除索引后
BEGIN;
UPDATE actor_copy1 SET first_name="t001" WHERE last_name='GUINESS';
会锁住下面这条数据
BEGIN;
UPDATE actor_copy1 SET first_name="t002" WHERE last_name='WAHLBERG';

重复索引
unique key也是一种索引，列建立了unique后就没有必要在此列建立其他索引了
index(a), index(a,b)  重复



慢查询日志
slow_query_log  启动停止慢查询日志
slow_query_log_file  指定慢查询日志路径
long_query_time  慢查询时间

log_querues_not_using_indexes  是否记录未使用索引的SQL

set global slow_query_log=on;
set global long_query_time=0.001;
SHOW VARIABLES LIKE "%long_query_time%";
SHOW VARIABLES LIKE "%slow_query_log%";

cd /usr/share/sysbench/tests/include/oltp_legacy/
sysbench --test=./oltp.lua --mysql-table-engine=innodb --oltp-table-size=10000 --mysql-db=imooc --mysql-user=root --mysql-password=cpq..123 --oltp-tables-count=10 --mysql-socket=/var/lib/mysql/mysql.sock prepare

sysbench --test=./oltp.lua --mysql-table-engine=innodb --oltp-table-size=10000 --mysql-db=imooc --mysql-user=root --mysql-password=cpq..123 --oltp-tables-count=10 --mysql-socket=/var/lib/mysql/mysql.sock run

慢查询分析工具
mysqldumpslow


使用pt-query-digest分析慢查询
wget https://www.percona.com/downloads/percona-toolkit/2.2.16/RPM/percona-toolkit-2.2.16-1.noarch.rpm && yum localinstall -y  percona-toolkit-2.2.16-1.noarch.rpm

使用pt-query-digest
pt-query-digest --explain h=127.0.0.1 --password=cpq..123 /var/lib/mysql/dev-slow.log > slow.rep


实时获取性能
查看information_schema中的PROCESSLIST表

query_cache_type  设置查询缓存是否可用，建议关闭
query_cache_size  设置查询缓存的内存大小
query_cache_limit  设置查询缓存可用存储的最大值
query_cache_wlock_invalidate  设置数据表被锁后是否返回缓存中的数据
query_cache_min_res_unit   设置查询缓存分配的内存块最小单位

确定查询各个阶段所消耗的时间
set profiling=1;
SELECT * FROM `t_ip_address` WHERE address like '%南省张家%' OR ip_end LIKE '%223.153.4.255%';
SHOW profiles;
SHOW profile for query [Query_ID];

profiles将会被废弃，推荐使用performance_schema
use performance_schema;
update setup_instruments set enabled='yes', timed='yes' where name like 'stage%';
update setup_consumers set enabled='yes' where name like 'events%';


大表的表结构修改
建一个新表，把老表的数据导入新表中，加一个触发器把老表数据同步到新表。
使用工具(表需要有主键)
pt-online-schema-change \
--alter="MODIFY last_name VARCHAR(150)" \
--user=root --password=cpq..123 D=sakila,t=actor_copy1 \
--charset=utf8 --execute


使用left join优化not in、< >

数据量很大
select count(*)
可以新建一张汇总表，从汇总表中查询总量


数据库分片
自增主键全局唯一
设置auto_increment_increment、auto_increment_offset


###################一份master节点配置#############################
需要先关闭selinux
vim  /etc/selinux/config
SELINUX=disabled

新建、授权/var/log/mysql存放慢查询日志文件
mkdir -p /var/log/mysql
chown mysql:mysql /var/log/mysql

select @@global.wait_timeout;  查看全局wait_timeout时间
select @@wait_timeout;         得到的结果是mysql客户端这种交互性连接的wait_time时间，实际上是interactive_timeout的值

# 密码
validate-password=0
# 连接
max_connections=1000
wait_timeout=600
# 慢查询
slow_query_log=1
slow_query_log_file=/var/log/mysql/slow_query_log_file.log
long_query_time=1
# 二进制日志
server_id=4177
log_bin=master_log
binlog_format=row
binlog_row_image=minimal
#事物提交后，数据写入到二进制日志
sync_binlog=1
max_binlog_size=1000M
expire_logs_days=7
# gtid配置
gtid_mode=on
enforce_gtid_consistency=true













