wget http://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
-- mysql8  wget http://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
yum -y install mysql57-community-release-el7-11.noarch.rpm
-- yum -y install mysql80-community-release-el7-3.noarch.rpm

查看一下安装效果
yum repolist enabled | grep mysql.*

yum -y install mysql-community-server

systemctl start  mysqld
systemctl status mysqld

修改密码
grep "password" /var/log/mysqld.log
mysql -uroot -p
改变validate_password_policy策略为0，仅校验密码长度，最小长度为8位
set global validate_password_policy=0;
ALTER USER 'root'@'localhost' IDENTIFIED BY '密码';

设置用户 root 可以在任意 IP 下被访问：
grant all privileges on *.* to root@"%" identified by "密码";

刷新权限使之生效：
flush privileges;


查看 MySQL 的字符集：
mysql> show variables like '%character%';

查看指定的数据库中指定数据表的字符集，如查看 mysql 数据库中 servers 表的字符集：
  show table status from mysql like '%servers%';
查看指定数据库中指定表的全部列的字符集，如查看 mysql 数据库中 servers 表的全部的列的字符集：
show full columns from servers;

设置 MySQL 的字符集为 UTF-8：
打开 /etc 目录下的 my.cnf 文件（此文件是 MySQL 的主配置文件）：
/etc/my.cnf
在 [mysqld] 前添加如下代码：
[client]
default-character-set=utf8
在 [mysqld] 后添加如下代码：
character_set_server=utf8
再次查看字符集：
mysql> show variables like '%character%';
+--------------------------+----------------------------+
| Variable_name            | Value                      |
+--------------------------+----------------------------+
| character_set_client    | utf8                      |
| character_set_connection | utf8                      |
| character_set_database  | utf8                      |
| character_set_filesystem | binary                    |
| character_set_results    | utf8                      |
| character_set_server    | utf8                      |
| character_set_system    | utf8                      |
| character_sets_dir      | /usr/share/mysql/charsets/ |
+--------------------------+----------------------------+
8 rows in set (0.01 sec)

/var/lib/mysql 是存放数据库文件的目录；

/var/log 目录下的 mysqld.log 文件记录 MySQL 的日志；

忘记密码时，可用如下方法重置：
# service mysqld stop
# mysqld_safe --user=root --skip-grant-tables --skip-networking &
# mysql -u root 
mysql> use mysql;
mysql> update user set password=password("new_password") where user="root"; 
mysql> flush privileges;

























