***********安装***********
yum install -y build-essential openssl openssl-devel unixODBC unixODBC-devel make gcc gcc-c++ kernel-devel m4 ncurses-devel tk tc xz lsof

vim /etc/hostname  要修改主机名
重启linux

下载：
wget www.rabbitmq.com/releases/erlang/erlang-18.3-1.el7.centos.x86_64.rpm
wget http://repo.iotti.biz/CentOS/7/x86_64/socat-1.7.3.2-5.el7.lux.x86_64.rpm
wget www.rabbitmq.com/releases/rabbitmq-server/v3.6.5/rabbitmq-server-3.6.5-1.noarch.rpm


1、新建/usr/local/software目录，目录放下面3个文件
	erlang-18.3-1.el7.centos.x86_64.rpm
	rabbitmq-server-3.6.5-1.noarch.rpm
	socat-1.7.3.2-1.1.el7.x86_64.rpm

2、按循序安装
	rpm -ivh erlang-18.3-1.el7.centos.x86_64.rpm 	
	rpm -ivh socat-1.7.3.2-1.1.el7.x86_64.rpm
	rpm -ivh rabbitmq-server-3.6.5-1.noarch.rpm

配置文件：
vim /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.5/ebin/rabbit.app
比如修改密码、配置等等，
例如：loopback_users 中的 <<"guest">>,只保留guest
{loopback_users, [guest]}



服务启动和停止：
启动 rabbitmq-server start &
停止 rabbitmq-server stop   rabbitmqctl  stop_app

查看是否启动成功 lsof -i:端口号，有pid则成功
lsof -i:5672
COMMAND   PID     USER   FD   TYPE DEVICE SIZE/OFF NODE NAME
beam.smp 8965 rabbitmq   52u  IPv6  63781      0t0  TCP *:amqp (LISTEN)


前端可视化界面
管理插件：rabbitmq-plugins enable rabbitmq_management

停止 rabbitmqctl stop

java代码通信端口号5672

访问前端管控台：http://192.168.40.129:15672/
账号密码是guest  guest
若无法登陆，直接   iptables -F
service iptables save
service iptables restart



rabbitmq-server start &
rabbitmq-plugins enable rabbitmq_management






