yum install binutils-2.* compat-libstdc++-33* elfutils-libelf-0.* elfutils-libelf-devel-* gcc-4.* gcc-c++-4.* glibc-2.* glibc-common-2.* glibc-devel-2.* glibc-headers-2.* ksh-2* libaio-0.* libaio-devel-0.* libgcc-4.* libstdc++-4.* libstdc++-devel-4.* make-3.* sysstat-7.* unixODBC-2.* unixODBC-devel-2.* pdksh*  libXi-1.7* libXtst-1.2*

虚拟机内存要配置为2g

登录界面切换用户，点击 no list

Global Database Name orcl
SID  orcl

创建listener要使用system账户，密码cpq..123

安装
http://www.linuxidc.com/Linux/2016-04/130559.htm
安装数据库
http://www.linuxidc.com/Linux/2017-05/144045p3.htm
打开数据库
https://jingyan.baidu.com/article/a65957f4e6e3dc24e67f9b12.html

以DBA身份进入sqlplus
sqlplus sys/change_on_intall as sysdba


navicat premium连接oracle提示连接超时，说明ip没问题，可能是端口有问题
#查看1521是否允许外部访问
iptables -L -n|grep 1521
#开放1521端口
iptables -I INPUT -p tcp -m state --state NEW -m tcp --dport 1521 -j ACCEPT  
#保存策略
iptables-save > iptables.rules
重新加载策略：iptables-restore iptables.rules
策略初始化保存： /usr/libexec/iptables/iptables.init save

navicat连接Linux oracle步骤：
	1、使用secureCRT连接192.168.40.129oracle会话
	2、lsnrctl start   #启动监听
	   sqlplus / as sysdba   #以DBA身份进入sqlplus
	   startup         #启动db数据库
	3、本地navicat即可连接虚拟机上的oracle




************************oracle sql 部分**************************

SELECT empno, ename, sal, deptno FROM EMP;  
#SQL中字段名是小写，但是查询出来的结果是大写
SELECT ename, sal*12+comm "yearmoney"  FROM "EMP"
#null加数字结果是null，若comm为null，则结果yearmoney是null。mysql也是
SELECT ename, sal*12+NVL(comm, 0) "yearmoney"  FROM "EMP"
#NVL(第一参数不为null|返回第一参数|若为为null, 返回第二参数)，mysql对应函数是IFNULL(expr1,expr2)
SELECT SYSDATE FROM DUAL
#dual是oracle中的一张虚拟表，oracle引入dual是为了符合语法，如为了查询出当前时间
SELECT * FROM "EMP" WHERE SAL between 1300 AND 1600
#1300<=SAL<=1600,MYSQL也适用
SELECT * FROM "EMP" WHERE SAL not between 1300 AND 1600
#SAL<1300 SAL>1600, MYSQL TOO
