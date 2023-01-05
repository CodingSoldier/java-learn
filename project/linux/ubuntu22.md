## apt是新出的工具，用于取代apt-get和apt-cache，apt 将分散在 apt-get 和 apt-cache 的基础操作统一包含了起来

    操作                                   apt                           apt-
同步远程仓库中的记录表                   sudo apt update              sudo apt-get update
将本地所有软件包更新至远程仓库最新版本      sudo apt upgrade             sudo apt-get upgrade
在软件仓库中搜索某一软件包                apt search <package>         apt-cache search <package>
查看软件包具体信息                       apt show <package>           apt-cache show <package>
安装软件包                             sudo apt install <package>   sudo apt-get install <package>
卸载软件包                             sudo apt remove <package>    sudo apt-get remove <package>

安装vim
apt -y install vim

## ubuntu版本 >= 17 读取/etc/netplan/*.yaml中描述的网络配置
```yaml
# This is the network config written by 'subiquity'
network:
  ethernets:
    enp0s3:
      # 禁用dhcp
      dhcp4: false
      # 静态IP地址，/24 表示网络号为24为，若使用子网掩码表示，则子网掩码是255.255.255.0
      addresses:
        - 10.0.2.200/24
      routes:
        - to: default
          # 路由（网关）
          via: 10.0.2.1
      nameservers:
        # dns解析
        addresses: [114.114.114.114,8.8.8.8]
    enp0s8:
      dhcp4: false
      addresses:
        - 192.168.56.10/24
      routes:
        # 双网卡只能有一个网关配置为default
        - to: 192.168.56.0/24
          via: 192.168.56.1
      nameservers:
        addresses: [114.114.114.114,8.8.8.8]
  version: 2
```
执行 netplan apply 命令可以让配置直接生效。virtualbox需要重启才生效

## 允许root使用ssh登录
默认root密码是随机的，即每次开机都有一个新的root密码。输入 sudo passwd 修改密码
允许root 使用 ssh 登录
sudo vim /etc/ssh/sshd_config
#PermitRootLogin prohibit-password
改为
PermitRootLogin yes

使配置生效
sudo service ssh restart

## 修改主机名
vim /etc/hostname

## 替换为阿里源
cd /etc/apt/
cp sources.list sources.list_bak
vim sources.list
ggdG
输入如下内容
deb http://mirrors.aliyun.com/ubuntu/ jammy main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ jammy-security main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy-security main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ jammy-updates main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy-updates main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ jammy-proposed main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy-proposed main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ jammy-backports main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ jammy-backports main restricted universe multiverse

同步远程仓库中的记录表
apt update

将本地所有软件包更新至远程仓库最新版本
apt upgrade


## 安装常用软件，manpages-dev包含gcc,g++,和make
sudo apt -y install manpages-dev vim iputils-ping telnet



