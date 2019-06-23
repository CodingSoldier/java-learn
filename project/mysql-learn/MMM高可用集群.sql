
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


