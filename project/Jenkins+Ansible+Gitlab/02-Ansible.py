#################### Ansible安装 #######################
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


######### Playbooks框架与格式 #########
目录结构
根目录
	inventory/       # 目录、Server详细清单
		testenv      # 文件、具体清单与变量声明
	roles/           # 目录、roles任务列表
		testbox/     # 目录、testbox详细任务
			tasks/   
				main.yml    # 文件、testbox主任务
	deploy.yml       # 文件、Playbook任务入口


testenv文件样例
# Server组列表，组名称支持多个
[testservers]
# 目标部署服务器主机名
test.example.com

# Server组列表参数
[testservers:vars]
# 目标主机Key/Value参数
server_name=test.example.com
user=root
output=/root/test.txt


main.yml文件样例。main.yml可以使用inventory目录中定义的XXX:vars变量
- name: Print server name and user to remote testbox
  shell: "echo 'Currently {{user}} is logining {{server_name}} ' > {{output}}"

deploy.yml任务入口文件
# hosts，Server列表
- hosts: "testservers"
  # true，获取Server基本信息
  gather_facts: true
  # 指定目标服务器系统用户
  remote_user: root
  # 进入roles/testbox任务目标
  roles:
  	- testbox




Ansible控制远程主机需要配置SSH免密码秘钥认证
	1、Ansible服务端创建SSH本地秘钥
		su - deploy
		ssh-keygen -t rsa
	2、Ansible服务端创建与目标部署机器的秘钥认证
		ssh-copy-id -i /home/deploy/.ssh/id_rsa.pub root@192.168.1.150
		测试能否登陆
		ssh root@192.168.1.150




加载ansible
su - deploy
cd /home/deploy/.py3-a2.5-env/
source /home/deploy/.py3-a2.5-env/bin/activate
source /home/deploy/.py3-a2.5-env/ansible/hacking/env-setup -q

创建test_playbooks目录，新建playbooks项目
cd ansible
mkdir test_playbooks
cd test_playbooks
mkdir inventory

vim inventory/testenv

[testservers]
192.168.1.150

[testservers:vars]
server_name=192.168.1.150
user=root
output=/root/test.txt

mkdir -p roles/testbox/tasks
vim roles/testbox/tasks/main.yml

- name: Print server name and user to remote testbox
  shell: "echo 'Currently {{user}} is logining {{server_name}} ' > {{output}}"

vim deploy.yml

- hosts: "testservers"
  gather_facts: true
  remote_user: root
  roles:
  - testbox


执行Playbooks，部署到testenv环境，在test_playbooks目录执行
ansible-playbook -i inventory/testenv ./deploy.yml


##############Ansible Playbooks常用模块介绍################
File模块，在目标主机创建文件或目录，并赋予其系统权限
- name: create a file
file: 'path=/root/foo.txt state=touch mode=0755 owner=foo group=foo'

Copy模块
- name: copy a file
copy: 'remote_src=no src=roles/testbox/files/foo.sh dest=/root/foo.sh mode=0644 force=yes'
# remote_src=no 将ansible主机的文件传送到目标主机
# src ansible主机文件
# dest 目标主机文件
# mode 目标主机文件权限
# force copy任务强制执行

Stat模块，获取远程文件状态信息
- name: check if foo.sh exists
  stat: 'path=/root/foo.sh'
  # path 文件路径 
  register: script_stat
  # register 将stat获取到的文件状态信息传送给script_stat

Debug模块，打印语句到Ansible执行输出
- debug: msg=foo.sh exist
  when: script_stat.stat.exists
  # 如果script_stat的stat信息存在，则打印debug中定义的msg信息

Command/Shell模块，用来执行Linux目标主机命令行。Shell模块使用的是/bin/bash，功能更强大，例如可以使用重定向符号、管道符号
- name: run the script
  command: "sh /root/foo.sh"

- name: run the script
  shell: "echo 'test' > /root/test.txt"  

Template模块，实现Ansible服务端到目标主机的jinja2模板传送
- name: write the nginx config file
  template: src=roles/testbox/template/nginx.conf.j2 dest=/etc/nginx/nginx.conf
  # 将src模板文件（jinja2模板格式）传送到dest。src模板文件中定义的变量将使用inventory中定义的变量

Packaging模块，调用目标主机系统包管理工具（yum，apt）进行安装
- name: ensure nginx is at the latest version
  yum: pkg=nginx state=latest
  # 安装nginx，版本为最后一个版本。目标主机必须是Redhat系

- name: ensure nginx is at the latest version
  apt: pkg=nginx state=latest
  # 安装nginx，版本为最后一个版本。目标主机必须是Debian系

Service模块，管理目标主机系统服务
- name: start nginx service
  service: name=nginx state=started
  # 启动nginx


##########使用Ansible模块部署###########
目标主机执行
useradd foo
useradd deploy
mkdir /etc/nginx
rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm


ansible主机
su - deploy
cd ~
新建 test_playbooks
cd test_playbooks 使用之前新建的test_playbooks
cd roles/testbox/tasks

mkdir roles/testbox/files
vim roles/testbox/files/foo.sh
echo "this is a test script"

vim ~/test_playbooks/inventory/testenv

[testservers]
192.168.1.150

[testservers:vars]
server_name=192.168.1.150
user=root
output=/root/test.txt
server_name=192.168.1.150
port=80
user=deploy
worker_processes=4
max_open_file=65505
root=/www

cd ~/test_playbooks/roles/testbox
mkdir templates
cd templates
vim nginx.conf.j2
详情查看test_playbooks文件夹

vim main.yml

- name: Print server name and user to remote testbox
  shell: "echo 'Currently {{ user }} is logining {{ server_name }}' > {{ output }}"
- name: create a file
  file: 'path=/root/foo.txt state=touch mode=0755 owner=foo group=foo'
- name: copy a file
  copy: 'remote_src=no src=roles/testbox/files/foo.sh dest=/root/foo.sh mode=0644 force=yes'
- name: check if foo.sh exists
  stat: 'path=/root/foo.sh'
  register: script_stat
- debug: msg="foo.sh exists"
  when: script_stat.stat.exists
- name: run the script
  command: 'sh /root/foo.sh'
- name: write the nginx config file
  template: src=roles/testbox/templates/nginx.conf.j2 dest=/etc/nginx/nginx.conf
- name: ensure nginx is at the latest version
  yum: pkg=nginx state=latest
- name: start nginx service
  service: name=nginx state=started

加载python、ansible环境
source /home/deploy/.py3-a2.5-env/bin/activate
source /home/deploy/.py3-a2.5-env/ansible/hacking/env-setup -q

# 执行任务
ansible-playbook -i inventory/testenv ./deploy.yml


进入目标主机
cat /etc/nginx/nginx.conf
systemctl status nginx



# shell脚本执行，ansible-playbook通过-e传递参数给ansible-playbooks执行
ansible-playbook -i inventory/testenv ./deploy.yml -e project=nginx -e branch=$branch -e env=$deploy_env











