1、VMware 安装两台虚拟机，解决ip冲突问题
	1.1、https://blog.csdn.net/lmmzsn/article/details/78921247
	1.2、https://blog.csdn.net/qq_22815337/article/details/79750689

#禁用validate-password功能
编辑my.cnf配置文件，validate-password=0,重启mysql。
通过mysql 语句修改validate-password只在当前环境变量下有效，重启mysql后无效

#之前做过备库，需要停止备库
stop slave
#需要重置备库
reset slave
#删除主备旧的log_bin文件


主备库都创建同一个账号
GRANT REPLICATION SLAVE,REPLICATION CLIENT ON *.* TO user01@'192.168.%' IDENTIFIED BY 'user01password';

# 主库开启日志功能、设置server_id
log_bin=mysql-log-bin
server_id=10

#从数据库
log_bin=mysql-log-bin
server_id=2
relay_log=/var/lib/mysql/mysql-relay-bin
log_slave_updates=1

#可能需要重启数据库
service mysqld restart

#查看二进制日志文件名
show master status;

#设置备库
change master to master_host='192.168.40.129',master_user='user01',master_password='user01password',master_log_file='mysql-log-bin.000001',master_log_pos=0;


#跳过所有错误。复制出错后，复制会停止，在备库配置
slave-skip-errors=all























