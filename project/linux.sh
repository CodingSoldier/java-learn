开机启动
脚本赋予开机启动权限
chmod +x xxx.sh

vim /etc/rc.d/rc.local
加上脚本
sh xxx.sh
可能需要执行
chmod +x /etc/rc.d/rc.local

后台启动jenkins应用
nohup $JAVA_HOME/bin/java -jar -Xms512m -Xmx512m -XX:+UseConcMarkSweepGC  /usr/local/software/jenkins/
jenkins.war --httpPort=8888 > /tmp/jenkins.log 2>&1 &


##########################ssh################################
本地使用xshell
新建连接——>用户秘钥——>生成——>输入密钥名称——>密码为空——下一步，保存为文件，保存公钥

服务器生成秘钥对
cd .ssh
一路回车
ssh-keygen -t rsa
vi authorized_keys
输入xshell生成的公钥

xshell使用公钥连接即可


liunx允许账号密码远程登录
vi /etc/ssh/sshd_config
	PermitRootLogin yes
	# PermitEmptyPasswords yes
	PasswordAuthentication yes
	PubkeyAuthentication yes
	
service sshd restart


linux修改密码
	passwd root
	输入简单密码会提示太简单，无视提示，再次输入简单密码即可成功



yum -y install lrzsz gcc gcc-c++ openssl openssl-devel pcre pcre-devel zlib zlib-devel ruby wget bash-completion zlib.i686 libstdc++.i686 lsof unzip zip bind-utils  readline-devel autoconf  man-pages nano vim-enhanced net-tools curl lftp make telnet.x86_64


###############################################linux网络#######################################################

ipconfig         查看ip，负责公网
ipconfig /all      查看MAC地址，计算机网卡的硬件地址，负责局域网
netstat –an       查看本机启动端口

修改网卡信息，仅修改网络可以使用 systemctl restart network 重启网络即可生效
vim /etc/sysconfig/network-scripts/ifcfg-ens33

网卡信息
DEVICE=ens33                    网卡设备名，也有一个NAME配置项
BOOTPROTO=none                  是否自动获取IP（none不指定、static静态ip、dhcp通过dhcp服务器分配）            
HWADDR=00:XX:XX:XX:XX:XX        MAC地址
NM__CONTROLLED=yes              是否可以有Network Manager图形管理器工具托管
ONBOOT=yes                      是否随网络服务启动，ens33生效
TYPE=Ethernet                   以太网类型
UUID=“XXXX”                     唯一识别码
IPADDR=192.168.1.222            IP地址
NETMASK=255.255.255.0           子网掩码
GATEWAY=192.168.1.1             网关
DNS1=114.114.114.114            DNS
IPV6INIT=no                     ipv6
USERCTL=no                      不允许非root用户控制此网卡

修改主机信息  vim /etc/sysconfig/network
NETWORKING=yes                 是否利用网络
HOSTNAME=localhost.localdomain    主机名
修改主机配置需要重启服务器才生效，所以配好后要临时修改
hostname localhost.localdomain  这样临时修改，永久修改都生效了
hostname 查询主机名
主机名对linux来说不重要

修改DNS，网卡DNS优先于此配置的DNS
vim /etc/resolv.conf
search localdomain com      默认域名，即是说ping baidu 会自动帮我们加上一级域名baidu.localdomain、baidu.com
nameserver 192.168.101.2    DNS

网络管理器的文本用户界面（Network Manager Text User Interface），以图形接麦你控制网络
使用 nmtui 命令进入图形界面修改，图形界面修改完成后也要 systemctl restart network
一个网卡可以配置个IP，使用nmtui新增
IPADDR=192.168.3.179
NETMASK=255.255.255.0
GATEWAY=192.168.3.1
DNS1=192.168.1.20
PREFIX=24
IPADDR1=192.168.3.180
PREFIX1=24
NETMASK1=255.255.255.0
IPADDR2=192.168.3.181
PREFIX2=24
NETMASK2=255.255.255.0

yum install -y httpd
yum install -y mod_ssl
systemctl start httpd
httpd服务使用自签名证书
	yum install -y openssl  # openssl的配置文件/etc/pki/tls/openssl.cnf，定义了证书、私钥的默认位置
	cd /etc/httpd/
	mkdir pki
	cd pki
	1、在客户端生成秘钥
		# genrsa使用RSA加密方式，-out输出，server.key私钥，2048位
		openssl genrsa -out server.key 2048 
	2、使用客户端秘钥与客户端信息生成证书签名请求文件。客户端秘钥作用是加密客户端信息，证书签名请求文件不包含客户端秘钥
		# 生成证书签名请求，Certificate Signing Request 证书签名请求，CSR
		openssl req -new -key server.key -out server.csr
		# 填写Country Name
		CN
		# 后面是省、市、公司、部分 随便填
		# Common Name 可以你的名字或者机器的hostname，填写机器IP
		192.168.3.179
		# 填写邮箱
		xxxx
		# challenge password 和 company name 不设置，直接回车
	3、生成自签名证书，没有网可以
		# 生成证书。x509类型表示自签证书，-days 3650 有效期10年，-in表示输入，-signkey 秘钥，-out输出证书
		openssl x509 -req -days 3650 -in server.csr -signkey server.key -out server.crt
	# 拷贝私钥到 /etc/pki/tls/private
	cp server.key /etc/pki/tls/private
	# 拷贝证书到 /etc/pki/tls/certs
	cp server.crt /etc/pki/tls/certs
	# 返回上一级
	cd ../
	# 修改Apache ssl配置文件
	vim conf.d/ssl.conf
	# 证书位置
	SSLCertificateFile /etc/pki/tls/certs/server.crt
	# 私钥位置
	SSLCertificateKeyFile /etc/pki/tls/private/server.key
	# 重启httpd服务
	systemctl restart httpd
	# 浏览器使用https访问
	https://192.168.3.179/



虚拟机网络配置：
桥接，使用物理机的物理网卡和物理机通信，也可以和同一网段内的其他物理机通信
NAT使用虚拟网卡VMnet8和物理主机通信，不能和同一网段内的其他物理机通信
Host-only 使用虚拟网卡VMnet1，不能连接公网，只能和物理主机通信

虚拟机复制、配置网卡:
	1、使用桥接模式
修改虚拟机，编辑—虚拟网络编辑器，桥接模式默认是自动桥接，需要修改，指定桥接到哪个网卡
Vim /etc/sysconfig/network-scripts/ifcfg-ens33

BOOTPROTO="static"
IPADDR=192.168.4.240
NETMASK=255.255.255.0
GATEWAY=192.168.4.1 
DNS1=192.168.1.20
DNS2=192.168.1.19
DNS3=114.114.114.114

# IPADDR要和物理机在同一个网段，GATEWAY使用物理机的，DNS也是用物理机的

service network restart  重启网络服务

2、使用nat模式
BOOTPROTO="static"
IPADDR=192.168.101.100    #必须在虚拟机NAT的网段下
NETMASK=255.255.255.0
GATEWAY=192.168.4.1      #使用虚拟机NAT的网关
DNS1=114.114.114.114     #使用此DNS服务器

#复制，
修改UUID
删除MAC地址行
重启linux服务器


Ifdown lo 禁用lo网卡
Ifup lo启动lo网卡

Ctrl + ] 退出telnet


######################################################################################################

命令后加上 &，程序会在后台运行，但是终端关闭后，此终端下的后台进程也会比关闭
可以使用 nohup [命令] &  来运行，即关闭终端时，nohup后面的命令进程不接收SINGHUP信号



需求：查看进程中 和 vim相关的进程，并保留头部文字，以便确认哪个进程号是pid，哪个是ppid
命令：ps -ef|head -n 1;ps -ef|grep -i vim

VMware配置固定ip为192.168.40.129
编辑 ——> 虚拟网路编辑器 ——> 点击更改设置 ——> 点击NAT模式 ——> DHCP设置 
——> 起始IP地址 192.168.40.129 结束IP地址 192.168.40.130
http://blog.csdn.net/ekey_code/article/details/73824214


################Shell 变量##################
xxx 变量名   $xxx 变量值
set 查看系统已经定义的环境变量
set -u 设定此选项，调用未声明变量时会报错
unset xxx 删除变量
env 查看系统环境变量

export 变量名=变量值
或者
变量名=变量值
export 变量名


运算
aa=11
bb=22
dd=$(expr $aa + $bb)
dd的值是aa、bb的和，注意+左右两侧必须有空格

ff=$(($aa + $bb))
a=$(( ($aa + $bb)/$aa*$bb ))


/etc/profile
/etc/profile.d/*.sh
/etc/bashrc
~/.bash_profile
~/.bashrc

etc下的环境变量，所有用户都能用
~家目录下的环境变量，只有当前用户可以使用

~/.bash_logout  退出前执行的命令
~/.bash_history   历史命令，退出登录后追加到文件中
/etc/issue  本地终端开机后的提示信息
/etc/motd  本地终端、远程终端登陆后显示信息

grep "/bin/bash" /etc/passwd

cut字符串截取
root=grep "/bin/bash" /etc/passwd | cut -f 1 -d ":"

printf '%s\t%s\t%s\t\n' 1 2 3 4 5 6

vim student.txt
ID	Name	Gender	Mark
1	furong	f	85
2	fengi	f	60
3	cang	f	70

printf '%s\t%s\t%s\t%s\n' $(cat student.txt)

awk '条件1{动作1}条件2{动作2}'
awk '{printf $2 "\t" $4 "\n"}' student.txt
awk '{print $2 "\t" $4}' student.txt

cat student.txt | grep -v Name | awk '$4>=70{print $2}'



Centos6、Centos7的变化
   系统                Centos6                     Centos7
文件系统       ext4                            xfs
修改主机名     /etc/sysconfig/network          /etc/hostname
修改时区       /etc/sysconfig/clock            timedatectl set-timezone Asia/TOkyo
查看ip信息     ifconfig                        ip
修改DNS地址    /etc/resolv.conf                -
查看端口状态   netstat                         ss   
防火墙        iptables                         firewalld
服务管理      System V init                    systemd
时间同步服务   ntp                             chrony

Centos7
	默认支持docker，内核支持OverlayFS、Repo源支持
	不再支持32位操作系统

Centos6修改时间：cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
Centos7修改时间：timedatectl set-timezone Asia/Shanghai
				主板时间修改为本地时间  timedatectl set-local-rtc 1
				主板时间还原   timedatectl set-local-rtc 0

参数补全
yum install -y bash-completion

使用ss替代了nestat
查看某种状态下的连接
ss -luntp state established

统计个数 |wc -l

通过端口查询
ss -na sport eq :22




***************************Systemd变化***************************
              Centos6         Centto7
服务管理      service         systemctl
启动项管理    chkconfig       systemctl
系统启动级别   init           systemctl
定时任务       cron           timer
日志管理       syslog         Systemd-journal

systemd通过文件类型来识别是哪种管理类型
文件拓展名      作用
.service      系统服务
.target       模拟实现运行级别
.device       定义内核识别设备
.mount        文件系统挂载点
.socket       进程间通信用的socket文件
.timer        定时器
.snapshot     文件系统快照
.swap         swap设备
.automount    自动挂载点
.path         监视文件或者目录
.scope        外部线程
.slice        分层次管理系统进程


systemctl -t help   支持的服务类型
systemctl -h 支持的参数

systemd命令

systemctl 、systemctl list-units      查看激活的单元
systemctl --failed                    运行失败的单元
systemctl list-unit-files             所有可用的单元
systemctl help <单元>                 单元的帮助手册页
systemctl daemon-reload               重新载入systemd，扫描新的或有变动的单元


systemctl start 单元   激活单元
systemctl stop 单元   停止单元
systemctl restart 单元   重启单元
systemctl reload 单元   重启单元
systemctl status 单元   重载单元
systemctl is-enabled 单元   检查单元是否配置为启动单元
systemctl enable 单元   开机激活单元
systemctl enabled --now 单元   开机启动并立即启动这个单元
systemctl disable 单元 取消开机自动激活单元
systemctl mask 单元 禁用一个单元，间接启动也不可能
systemctl unmask 单元 取消禁用的某个单元

systemctl cat 单元   查看 单元.service 文件


自定义service文件

vim imoocc_gen.sh

#!/bin/bash

# 输出当前shell的pid
echo $$ >> /var/run/imoocc_gen.pid

#循环输出
while :
do
	echo "Hi,imoocc,"$(date) >> /tmp/imoocc.res
	sleep 1
done

输出pid
cat /opt/imoocc_gen.sh 和 ps -ef|grep imoocc_gen 结果一样
拷贝一份service文件，再改改
cd /usr/lib/systemd/system
cp nginx.service imoocc_gen.service

vim imoocc_gen.service

[Unit]
Description=des
# after可以去掉
After=network.target remote-fs.target nss-lookup.target

[Service]
# 不产生子进程，使用simple（默认）
Type=simple
# 指定pid
PIDFile=/var/run/imoocc_gen.pid
# 一定要使用全路径
ExecStart=/bin/sh /opt/imoocc_gen.sh
# 停止
ExecStop=/bin/kill -s TERM $MAINPID

[Install]
WantedBy=multi-user.target

# 先reload再start
systemctl daemon-reload
systemctl start imoocc_gen.service
systemctl status imoocc_gen.service 

定时任务
/usr/lib/systemd/system/imoocc_gen.timer 
[Unit]
Description=timer unit - Print info
Documentation=http://imoocc.com

[Timer]
Unit=imoocc_gen.service
OnCalendar=2019-11-19 18:36:00

[Install]
WantedBy=multi-user.target


查看定时任务
systemctl list-timers


防火墙
/lib/firewalld/zones
启动防火墙
systemctl start firewalld.service 
查看默认区域
firewall-cmd --list-all
查看9个区域
firewall-cmd --list-all-zones
设置默认区域
firewall-cmd --set-default-zone=home
修改网卡接口区域
firewall-cmd --zone=home --change-interface=enp0s3

开放端口，阿里云还需要安全组规则
firewall-cmd --zone=home --add-port=80/tcp
开发端口后再使配置永久生效
firewall-cmd --zone=public --add-port=80/tcp --permanent
移除开放端口
firewall-cmd --zone=public --remove-port=80/tcp

重载firewall，不会关闭已经建立的连接
firewall-cmd --reload
重载firewall，会关闭所有连接
firewall-cmd --complete-reload

移除端口，使用 firewall-cmd --list-all 不会展示已经移除的端口，需要 firewall-cmd --reload  才会看到
firewall-cmd --zone=public --remove-port=80/tcp --permanent

查看firewall可用服务文件
ll /usr/lib/firewalld/services

添加服务白名单
firewall-cmd --zone=public --add-service=http

执行 firewall-cmd --list-all
services会多了http services: ssh dhcpv6-client http

# 多区域组合，默认区域是drop
firewall-cmd --set-default-zone=drop
# 192.168.3.0/24 trusted区域配置指定网段配置可以访问
firewall-cmd --zone=trusted --add-source=192.168.3.0/24 --permanent
firewall-cmd --reload


通过公网linux登陆私网linux
ssh 私网ip
yes
输入密码


GRE  深圳、杭州都创建有公网ip的ECS
两个ECS都安装GRE
查看模块是否安装  lsmod | grep ip_gre
安装模块    modprobe ip_gre

# 在 172.31.0.252 添加GRE隧道，112.74.22.58 远程公网ip，172.31.0.252是当前机器内网ip
ip tunnel add tun1 mode gre remote 112.74.22.58 local 172.31.0.252
# 激活隧道
ip link set tun1 up
# 设置互联地址
ip addr add 192.168.1.1 peer 192.168.1.2 dev tun1
# 添加路由，10.165.0.0/24 是远程公网ip ECS所在专有网络VPC的交换机
ip route add 10.165.0.0/24 dev tun1

# 在 10.165.0.8 添加GRE隧道，121.41.65.63 远程公网ip，10.165.0.8是当前机器内网ip
ip tunnel add tun1 mode gre remote 121.41.65.63 local 10.165.0.8
# 激活隧道
ip link set tun1 up
# 设置互联地址，要反过来
ip addr add 192.168.1.2 peer 192.168.1.1 dev tun1
# 添加路由，172.31.0.0/24 是远程公网ip ECS所在专有网络VPC的交换机
ip route add 172.31.0.0/24 dev tun1

公网121.41.65.63内网172.31.0.252添加安全组，信任网段10.165.0.0/24可入
公网112.74.22.58内网10.165.0.8添加安全组，信任网段172.31.0.0/24可入



	
