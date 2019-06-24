
MMM两主多从高可用配置

主主主主主主主1
validate-password=0
####server_id要改
server_id=1
log_bin=/var/lib/mysql/master_log
max_binlog_size=1000M
binlog_format=row
expire_logs_days=7
sync_binlog=1

relay_log=/var/lib/mysql/relay_log
log_slave_updates=on

########## master_host要改
change master to master_host='192.168.1.197', \
	master_user='repl', \
	master_password='123456', \
	master_log_file='master_log.000001', \
	master_log_pos=154;

start slave; 

show slave status \G;


主主主主主主主2
validate-password=0
####server_id要改
server_id=2
log_bin=/var/lib/mysql/master_log
max_binlog_size=1000M
binlog_format=row
expire_logs_days=7
sync_binlog=1

relay_log=/var/lib/mysql/relay_log
log_slave_updates=on

########## master_host要改
change master to master_host='192.168.1.198', \
	master_user='repl', \
	master_password='123456', \
	master_log_file='master_log.000001', \
	master_log_pos=154;

start slave; 

show slave status \G;



从从从从从从节点指定主节点为198
validate-password=0
server_id=3
log_bin=/var/lib/mysql/master_log
max_binlog_size=1000M
binlog_format=row
expire_logs_days=7
sync_binlog=1
read_only=on

relay_log=/var/lib/mysql/relay_log
log_slave_updates=on

########## master_host要改
change master to master_host='192.168.1.198', \
	master_user='repl', \
	master_password='123456', \
	master_log_file='master_log.000001', \
	master_log_pos=154;

start slave; 

show slave status \G;



wget http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
wget http://rpms.famillecollet.com/enterprise/remi-release-6.rpm

rpm -ivh epel-release-6-8.noarch.rpm
rpm -ivh remi-release-6.rpm

vim /etc/yum.repos.d/remi.repo 
[remi]的enabled改为1
enabled=1

vim /etc/yum.repos.d/epel.repo 
baseurl取消注释，mirrorlist注释掉

yum search mmm

先安装好Perl环境
wget http://search.cpan.org/CPAN/authors/id/S/SH/SHAY/perl-5.26.1.tar.gz

# 解包
tar -zxvf perl-5.26.1.tar.gz
# 进入文件目录
cd perl-5.26.1 
./Configure -des -Dprefix=/usr/local/perl

# 编译
make
# 安装
make install


每个节点都安装代理
yum install mysql-mmm-agent.noarch -y


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


重复索引
unique key也是一种索引，列建立了unique后就没有必要在此列建立其他索引了
index(a), index(a,b)  重复







