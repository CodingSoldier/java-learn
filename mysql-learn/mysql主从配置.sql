1、VMware 安装两台虚拟机，解决ip冲突问题
	1.1、https://blog.csdn.net/lmmzsn/article/details/78921247
	1.2、https://blog.csdn.net/qq_22815337/article/details/79750689

2、主数据库vim /etc/my.cnf
	#主数据库server-id要比从数据库id小
	server_id=100

	#主库开启日志功能，日志文件默认在/var/lib/mysql目录下
	log_bin=master_log	

   从数据库vim /etc/my.cnf
	#从数据库
	server_id=101

3、service mysqld restart  重启mysql

4、主添加用户给从使用
	grant all privileges on *.* to 'myslave'@'192.168.40.120' identified by 'mysalave0.' with grant option;

5、主或者从 vim /var/lib/mysql/auto.cnf  修改service-uuid（主从不相同即可）
   service mysqld restart  重启mysql	

6、从数据库
   6.1 stop slave 关闭从slave 
   6.2 change master to master_host='192.168.40.129',master_user='myslave',master_password='mysalave0.',master_log_file='master_log.000001'; 
   6.3 start slave  开启slave
   6.4 show slave status \G    \G表示竖直排列
   6.5 从数据库服务器要开放端口给主数据库服务器
   6.6 最好再重启一次从mysql服务

7、主操作建库、建表、插入、更新数据，从自动备份，'myslave'@'192.168.40.120'这账号要有这些权限才行












vim server.xml
<user name="root">
    <property name="password">cpq..123</property>
    <!-- 与schema.xml中的<schema name="mycat_database"/>相对应 -->
    <property name="schemas">mycat_database</property>
    <property name="readOnly">false</property>
</user>

  	