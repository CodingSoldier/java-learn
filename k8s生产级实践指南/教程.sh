Kubernetes - 舵手的意思，logo是一个罗盘。另外一层意思，docker的logo是一条鲸鱼托着集装箱在大海遨游，那么kubernetes就要引领docker的航向


01-kubernetes核心概念.jpg

每个POD默认有一个Pause容器，Pause将POD中的容器关联起来，并实现容器的健康检查

ReplicaSet（RS）副本集，管理多个相同的POD实例

Deployment，部署，更新应用，即更新ReplicaSet、POD。滚动更新的时候，不仅新建了POD，还新建了ReplicaSet

Service通过Selector选择POD的标签

02-kubernetes的架构设计.png
ETCD存储集群信息
ApiServer对外提供管理k8s集群的http接口
Scheduler调度器，收集各Worker节点的信息，使用调度策略选择一个Worker创建POD
ControllerManager集群的控制中心，控制POD、Service

03-认证.jpg
认证和授权ApiServer
3种认证方式
	1、客户端证书认证，使用TLS双向认证。客户端kubectl与ApiServer通信时使用
	2、BearerToken。客户端kubectl与ApiServer通信时使用
	3、ServiceAccount，用于集群内部通信使用

04-授权.jpg
授权使用RBAC
Resource：资源，POD、Service这些都属于资源
Verbs：对资源的增删改查

Role属于namespace下，只能拥有namespace下的资源
如果要拥有多个namespace下的资源，使用ClusterRole

用户与角色通过RoleBinding、ClusterRoleBinding绑定

除了RBAC之外，k8s还添加了准入控制AdmisionControl，就是一条一条的过滤器链


###安装k8s####
要求机器CPU >= 2，Memory >= 2G

kubeadm安装
准备5台机器，修改网卡信息
vim /etc/sysconfig/network-scripts/ifcfg-enp0s3
	BOOTPROTO=static
	ONBOOT=yes
	IPADDR=192.168.1.201
	NETMASK=255.255.255.0
	GATEWAY=192.168.1.1
	删除网卡的UUID

设置DNS
vim /etc/resolv.conf
nameserver 116.116.116.116

修改hostname
hostnamectl set-hostname m[123456]

重启
hostnamectl set-hostname m

Xshell点击 工具 -> 发送键输入到所有会话。命令会在所有终端执行

vi /etc/hosts 追加以下内容
192.168.3.241 m1
192.168.3.242 m2
192.168.3.243 m3
192.168.3.244 m4
192.168.3.245 m5

重启
reboot -h now

一、实践环境准备
https://gitee.com/pa/kubernetes-ha-kubeadm-private/blob/kubernetes-1.14/docs/1-prepare.md
	# 安装指定版本（这里用的是1.14.0），这一步会失败，改成两步
	# yum install -y kubeadm-1.14.0-0 kubelet-1.14.0-0 kubectl-1.14.0-0 --disableexcludes=kubernetes
	yum install -y kubelet-1.14.0-0 kubernetes-cni-0.5.1
	yum install -y kubeadm-1.14.0-0 kubectl-1.14.0-0 --disableexcludes=kubernetes

	# git仓库地址需要修改
	yum install -y git
	cd ~ && git clone https://gitee.com/pa/kubernetes-ha-kubeadm-private.git
	cd kubernetes-ha-kubeadm-private
	
	把克隆kubernetes-ha-kubeadm-private的机器的公钥配置到所有机器，实现免密登陆


二、搭建高可用集群
https://gitee.com/pa/kubernetes-ha-kubeadm-private/blob/kubernetes-1.14/docs/2-ha-deploy.md
	
	# 查看日志
	journalctl -f -u keepalived

	# 部署第一个主节点，kubeadm-config.yaml在target中
	kubeadm init --config=kubeadm-config.yaml --experimental-upload-certs

	# 执行kubeadm初始化系统，等待几分钟，拷贝最终输出结果
	kubeadm join 192.168.3.188:6443 --token aldiup.apbwlrd1hnlqc0uz \
    --discovery-token-ca-cert-hash sha256:5285e045ca699b5ce59789e9df71b5a3e81e45e171025cf5acabbb82e6cc6a44 

    ！！！！！只能做成1主2从
    ！！！！！最后部署出来DNS还不可用


三、集群可用性测试
	主节点创建nginx-ds

四. 部署dashboard	

	# 创建服务，使用/root/kubernetes-ha-kubeadm-private/target/addons/dashboard-all.yaml
	kubectl apply -f /root/kubernetes-ha-kubeadm-private/target/addons/dashboard-all.yaml








