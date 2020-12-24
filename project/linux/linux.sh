yum makecache

yum -y install gcc gcc-c++ make vim-enhanced bash-completion net-tools bind-utils wget lrzsz man-pages curl telnet telnet-server

####################liunx允许账号密码远程登录####################
vi /etc/ssh/sshd_config
	PermitRootLogin yes
	# PermitEmptyPasswords yes
	PasswordAuthentication yes
	PubkeyAuthentication yes
	
service sshd restart


linux修改密码
	passwd root
	输入简单密码会提示太简单，无视提示，再次输入简单密码即可成功

##########################xshell ssh连接################################
本地使用xshell
新建连接——>用户秘钥——>生成——>输入密钥名称——>密码为空——下一步，保存为文件，保存公钥

服务器生成秘钥对
cd .ssh
一路回车
ssh-keygen -t rsa
vi authorized_keys
输入xshell生成的公钥

xshell使用公钥连接即可


###############################################VirtualBox与linux网络#######################################################

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



VMware配置固定ip为192.168.40.129
编辑 ——> 虚拟网路编辑器 ——> 点击更改设置 ——> 点击NAT模式 ——> DHCP设置 
——> 起始IP地址 192.168.40.129 结束IP地址 192.168.40.130
http://blog.csdn.net/ekey_code/article/details/73824214



