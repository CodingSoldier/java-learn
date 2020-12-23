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

	要关闭防火墙
	
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
	# 创建服务，使用 ./资源/dashboard-all.yaml
	kubectl apply -f dashboard-all.yaml

	# 全部执行完后，要配置证书和私钥
	# 查看dashboard在哪个节点上
	kubectl get pod -n kube-system -o wide
	# 在dashboard节点上生成证书
	openssl genrsa -des3 -passout pass:x -out dashboard.pass.key 2048
	openssl rsa -passin pass:x -in dashboard.pass.key -out dashboard.key
	openssl req -new -key dashboard.key -out dashboard.csr
	输入证书subject
	openssl x509 -req -sha256 -days 365 -in dashboard.csr -signkey dashboard.key -out dashboard.crt
	# 查看容器docker容器id
	docker ps | grep dashboard
	# 找到证书目录
	docker inspect docker容器id
	"Source": " /var/lib/kubelet/pods/c1a09a76-7eef-11e9-9162-080027aaa94d/volumes/kubernetes.io~secret/kubernetes-dashboard-certs ",
	"Destination": "/certs",
	# 拷贝证书dashboard.crt、dashboard.key 到 "Source" 对应的目录下
	# 重启容器
	docker restart docker容器id
	# 登陆地址
	https://192.168.1.212:30005/#!/overview?namespace=default


harbor  https://github.com/goharbor/harbor

https://github.com/goharbor/harbor/blob/release-1.6.0/docs/installation_guide.md
系统要求：2 CPU，4GB Mem，40GB Disk
	可新增一块磁盘挂载到/harboarddir目录
	然后建立软连接。ln -s /harboarddir/data/ /data
需要安装docker-compose
	sudo curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
	sudo chmod +x /usr/local/bin/docker-compose
	docker-compose --version
安装步骤归结为以下内容
	1、下载安装程序。
		# 下载
		wget https://storage.googleapis.com/harbor-releases/release-1.6.0/harbor-offline-installer-v1.6.0.tgz
		# 解压
		tar xvf harbor-offline-installer-v1.6.0.tgz
		# 进入harbor目录
		cd harbor/
	2、配置harbor.cfg
		vim harbor.cfg
		# hostname修改为本机ip地址
		hostname = 192.168.3.248
		# admin账号的密码
		harbor_admin_password = Harbor12345
	3、运行install.sh以安装并启动Harbor。
		./install.sh
		完成后直接使用IP访问，账号密码 admin Harbor12345


docker登陆harbor
	1、修改客户端的/etc/docker/daemon.json
		vim /etc/docker/daemon.json
		{
		  "registry-mirrors": ["https://ojmdil2j.mirror.aliyuncs.com"],
		  "insecure-registries": ["harbor.cfg中的hostname"]
		}

		systemctl daemon-reload
		systemctl restart docker
	2、登陆harbor
		docker login 192.168.3.248 或者 docker login -u admin -p Harbor12345 192.168.3.248
		输入账号密码
	3、推送镜像到harbor。harbor中有示例，点击 项目 -> 点击一个项目 -> 推送镜像
	# 打标签 docker tag 本地镜像:标签名 harbor服务器IP/harbor项目名/镜像名:标签名
	docker tag hello-world:latest 192.168.3.248/public-projects/busybox:v1.0
	# 推送镜像
	docker push 192.168.3.248/public-projects/busybox:v1.0
	
	# 拉取镜像
	docker pull 192.168.3.248/public-projects/busybox:v1.0


06-k8s服务发现.jpg
集群内部相互访问
	1.1、DNS+CLusterIP方式，PodA使用服务名访问PodB
	1.2、PodA访问HeadlessService，HeadlessService返回PodC列表
集群内部访问外部
	2.1、外部服务IP+PORT
	2.2、这种太扯淡
外部访问集群内部
	3.1、使用NodePort
	3.2、hostport
	3.3、Ingress

nginx-ingress默认监听所有namespace下的ingress资源


Jenkins Pipeline script 的右侧下拉选框有示例代码，示例中的代码使用的是“脚本式流水线”，“声明式流水线”比“脚本式流水线”更强大
本人使用的是“声明式流水线”

通过一个中间文件在两个shell脚本之间传递变量
echo '123' > val_file
val=$(cat val_file)
echo $val

使用sed命令可以替换文本，举例：
1、service.yaml配置如下
apiVersion: v1
kind: Service
metadata:
  name: {{name}}
spec:
  selector:
    app: {{name}}
  # 使用NodePort方式暴露一个外部端口供调试使用
  type: NodePort
  ports:
  - name: http
    port: {{port}}
    targetPort: {{port}}
    # 外部端口
    nodePort: 30013

2、替换{{name}}、{{port}}
name="app3"
port="8080"
sed -i "s,{{name}},${name},g;s,{{port}},${port},g" service.yaml







