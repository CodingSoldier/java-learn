Ansible有两种安装方式
Yum安装。由于ansible依赖了Python，使用yum全局安装会造成服务器Python版本混乱
	yum -y install ansible
源码安装
	0、准备工作
		关闭防火墙
			systemctl stop firewalld
			systemctl disable firewalld
		禁用selinux
			vim /etc/sysconfig/selinux
			SELINUX=disabled
			重启机器，查看selinux禁用是否生效，执行 getenforce
	1、安装Python3.6
		yum -y install zlib-devel bzip2 bzip2-devel readline-devel sqlite sqlite-devel openssl-devel xz xz-devel libffi-devel
		wget https://www.python.org/ftp/python/3.6.5/Python-3.6.5.tar.xz
		tar xf Python-3.6.5.tar.xz
		cd Python-3.6.5/
		./configure --prefix=/usr/local --with-ensurepip=install --enable-shared LDFLAGS="-Wl,-rpath /usr/local/lib"
		# --prefix=/usr/local 将Python安装到/usr/local目录
		# --with-ensurepip=install 安装pip报管理工具
		# --enable-shared LDFLAGS="-Wl,-rpath /usr/local/lib"  匹配当前Python参数值
		
		make && make altinstall 
		# 编译并安装
		
		which pip3.6
		# 查看pip包管理工具
		
		ln -s /usr/local/bin/pip3.6 /usr/local/bin/pip
		# 创建软连接，可直接是pip命令
	2、安装virtualenv
		pip install virtualenv
	3、创建Ansible账户，创建一个Python3.6版本的virtualenv实例
		useradd deploy && su - deploy
		virtualenv -p /usr/local/bin/python3.6 .py3-a2.5-env   # .py3-a2.5-env是virtualenv实例名称
		# 进入virtualenv实例
		cd /home/deploy/.py3-a2.5-env/
	4、git下载ansible2.5版本
		su - root
		yum -y install git nss curl
		su - deploy
		git clone https://github.com/ansible/ansible.git
		# wget https://github.com/ansible/ansible/archive/refs/tags/v2.5.15.tar.gz
	5、进入Python3.6 virtualenv环境
		source /home/deploy/.py3-a2.5-env/bin/activate
		# 退出virtualenv环境  deactivate
	6、安装ansible的依赖包
		pip install paramiko PyYAML jinja2
	7、在Python3.6虚拟环境下加载ansible2.5
		# 移动ansible目录到.py3-a2.5-env目录下
		mv ansible .py3-a2.5-env/
		cd .py3-a2.5-env/ansible
		# 切换到stable-2.5分支
		git checkout stable-2.5
		# 在python虚拟环境下加载ansible2.5版本
		source /home/deploy/.py3-a2.5-env/ansible/hacking/env-setup -q
	8、验证ansible2.5是否安装成功
		ansible --version


