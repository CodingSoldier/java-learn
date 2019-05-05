######################安装ubuntu-16.04.6-server-amd64.iso##############
cpu数量设置为2
必须装英文版
https://blog.csdn.net/ai2000ai/article/details/81167288
输入用户密码 cpq cpq..123

允许root登陆
https://jingyan.baidu.com/article/066074d615b8f4c3c31cb067.html
root cpq..123


虚拟机全局设置，创建NAT网络
#设置xshell使用root登陆
每台虚拟机——>设置(单独设置)——网络——NAT网络——名字选择全局的NAT网络名



###################翻墙，虚拟机走物理机代理#######################
# vim /etc/profile
# export http_proxy=http://192.168.1.104:1080   #192.168.1.104:1080是走物理机的代理
# export https_proxy=http://192.168.1.104:1080
# source /etc/profile 
# curl www.google.com

##################################安装docker###################################
#更新包索引
apt-get update

# 添加Docker的官方GPG密钥
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -

# 设定稳定存储库
add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

vi /etc/apt/sources.list
/docker  查看是否已经添加docker官方源

apt-get update

# 查看可安装的docker版本
apt-cache madison docker-ce
# 安装docker17.03.2，若安装不成功，则配置代理
apt-get install -y docker-ce=17.03.2~ce-0~ubuntu-xenial
# 查看版本
docker version
# 查看docker服务运行状态
systemctl status docker

vim /etc/docker/daemon.json

{
  "registry-mirrors": ["https://ojmdil2j.mirror.aliyuncs.com"]
}

systemctl daemon-reload
systemctl restart docker

docker run hello-world






############################安装kubernetes####################################

#注释swap分区
vim /etc/fstab
# /dev/mapper/cryptswap1 none swap sw 0 0

# 修改主机名，不能喝主机名相同
vim /etc/hostname

# 重启节点，使swap生效
reboot now

# 使用阿里源
vi /etc/apt/sources.list.d/kubernetes.list
# 填入
deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
#更新本地包缓存
apt-get update
#列出版本列表
apt-cache madison kubeadm

# 安装
apt-get install kubelet=1.14.1-00 kubectl=1.14.1-00 kubeadm=1.14.1-00

kubeadm version

systemctl status kubelet


docker pull mirrorgooglecontainers/kube-apiserver-amd64:v1.14.1
docker pull mirrorgooglecontainers/kube-controller-manager-amd64:v1.14.1
docker pull mirrorgooglecontainers/kube-scheduler-amd64:v1.14.1
docker pull mirrorgooglecontainers/kube-proxy-amd64:v1.14.1
docker pull mirrorgooglecontainers/etcd-amd64:3.3.10
docker pull mirrorgooglecontainers/pause-amd64:3.1
docker pull coredns/coredns:1.3.1


docker tag mirrorgooglecontainers/kube-apiserver-amd64:v1.14.1 k8s.gcr.io/kube-apiserver:v1.14.1
docker tag mirrorgooglecontainers/kube-controller-manager-amd64:v1.14.1 k8s.gcr.io/kube-controller-manager:v1.14.1
docker tag mirrorgooglecontainers/kube-scheduler-amd64:v1.14.1 k8s.gcr.io/kube-scheduler:v1.14.1
docker tag mirrorgooglecontainers/kube-proxy-amd64:v1.14.1 k8s.gcr.io/kube-proxy:v1.14.1
docker tag mirrorgooglecontainers/etcd-amd64:3.3.10 k8s.gcr.io/etcd:3.3.10
docker tag mirrorgooglecontainers/pause-amd64:3.1 k8s.gcr.io/pause:3.1
docker tag coredns/coredns:1.3.1 k8s.gcr.io/coredns:1.3.1


#若出错，需执行 kubeadm reset
kubeadm init --apiserver-advertise-address=10.0.2.15  --pod-network-cidr=192.168.16.0/20


# 配置KUBECONFIG
echo "export KUBECONFIG=/etc/kubernetes/admin.conf" >> ~/.bash_profile
source ~/.bash_profile


#查看主节点运行情况，DNS没起来，安装网络插件后就好了
kubectl get pods -n kube-system -o wide

#安装网络插件 Weave Net 
curl -L "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')" > weave.yaml

vi weave.yaml
/env
加入，不能使用tab键
- name: IPALLOC_RANGE
  value: 192.168.16.0/20

#创建网络插件
kubectl apply -f weave.yaml

#查看主节点运行情况，过一会儿，网络插件就起来了
kubectl get pods -n kube-system -o wide 


##################坑坑坑坑##########################
# 在init的过程中卡住，另开一个终端，运行
journalctl -f -u kubelet.service
# 删除节点
kubectl delete node k8s3
# 删除软件
apt-get --purge remove  kubeadm kubernetes-cni kubelet kubectl
# 创建token
kubeadm token create
# 查看token
kubeadm token list



#########################从节点###############################
#注释swap分区
vim /etc/fstab
# /dev/mapper/cryptswap1 none swap sw 0 0

# 修改主机名，不能喝主机名相同
vim /etc/hostname

# 重启节点，使swap生效
reboot now

# 使用阿里源
vi /etc/apt/sources.list.d/kubernetes.list
# 填入
deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
#更新本地包缓存
apt-get update

# 主从节点的版本必须相同
apt-get install kubelet=1.14.1-00 kubectl=1.14.1-00 kubeadm=1.14.1-00

docker pull mirrorgooglecontainers/pause-amd64:3.1
docker pull mirrorgooglecontainers/kube-proxy-amd64:v1.14.1

docker tag mirrorgooglecontainers/pause-amd64:3.1 k8s.gcr.io/pause:3.1
docker tag mirrorgooglecontainers/kube-proxy-amd64:v1.14.1 k8s.gcr.io/kube-proxy:v1.14.1


kubeadm join 10.0.2.15:6443 --token trhv5v.yckqzyzqyawj1svz \
    --discovery-token-ca-cert-hash sha256:137962665379e831d10b002f5f1df3c7f14838a8a72b6483553d9dce8003ae0e 

# 可能会报iptable问题，从节点
vim /etc/sysctl.conf
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
sysctl -p

kubectl get nodes

# 重新加入节点，从节点执行
kubeadm reset




# 配置在非master节点上管理集群
1、分发 /etc/kubernetes/admin.conf
# master节点上执行
scp /etc/kubernetes/admin.conf root@10.0.2.4:/etc/kubernetes
# node2执行
vim .bashrc 
export KUBECONFIG='/etc/kubernetes/admin.conf'
source .bashrc 
kubectl get nodes 






##########################Dashboard图形化管理工具############################

docker pull mirrorgooglecontainers/kubernetes-dashboard-amd64:v1.10.1 
docker tag mirrorgooglecontainers/kubernetes-dashboard-amd64:v1.10.1 k8s.gcr.io/kubernetes-dashboard-amd64:v1.10.1 

# 下载 kubernetes-dashboard.yaml
# https://github.com/kubernetes/dashboard/blob/master/aio/deploy/recommended/kubernetes-dashboard.yaml
# https://github.com/kubernetes/dashboard/releases
kubectl apply -f  kubernetes-dashboard.yaml

kubectl get pods -n kube-system | grep dashboard

# master节点建立代理
kubectl proxy
提示Starting to serve on 127.0.0.1:8001
# 建立virtualbox ssh端口转发，用xshell时已经设置
# chrome安装switchyomega

kubectl get services --all-namespaces





#########################集群探索##############################
# master init 过程
证书文件位置  
  /etc/kubernetes/pki/

配置文件位置
  .kube/config   #cd ~    cd .kube/config
  /etc/kubernetes/*.conf
  KUBECONFIG环境变量 

查看环境变量  echo $KUBECONFIG
查看配置信息  kubectl config view   

/etc/kubernetes/manifest存放组件的yaml文件
开机启动过程：kubelet会被systemd进程拉起，开机启动；kubectl会自动启动/etc/kubernetes/manifest下的容器

#DNS、网络
kubectl get daemonset -n kube-system


#join过程
--discovery-token-ca-cert-hash  后面的hash值会和master节点的ca证书值匹配，匹配上了则加入主节点



节点pod的管家：kubelet
kubelet运行于集群的所有节点上
  每个节点上的kubelet有操作系统init进程（如systemd）启动
  /lib/systemd/system/kubelet.service  #service文件
  /etc/systemd/system/kubelet.service.d/10-kubeadm.conf  #kubelet配置文件
  systemctl daemon-reload & systemctl restart kubelet  #配置的修改与生效


集群管理入口：kube-apiserver
  是kubelet启动的static pod
  APIServer的pod spec:  /etc/kubernetes/manifests/kube-apiserver.yaml


配置中心：etcd
  由kubelet启动的static pod
  /etc/kubernetes/manifests/etcd.yaml #配置文件地址

/etc/kubernetes/manifests/中的yaml文件修改后，kubelet会自动重启pod  



管理控制中心：kube-controller-manager
  负责集群内node、pod副本、服务的endpoint、命名空间、Service Account、资源配额等的管理
  由kubelet启动的static pod


调度器：kube-scheduler
  按照特定的调度算法和策略，将待调度pod绑定到集群中某个合适的node，并写入绑定信息


服务抽象实现：kube-proxy
  运行在kubernetes集群的每个节点上
  有daemonset控制器在各个节点上启动的唯一实例
  不是static pod





# 部署一个pod
kubectl create -f nginx-deployment.yaml
#查看pod
kubectl get pods --show-labels| grep deployment  
#打上标签
kubectl label pods/deployment-example-56767584bd-4xk79 status=healthy

# deployment-example是nginx-deployment.yaml部署的名称
kubectl edit deployment/deployment-example
修改replicas保存后立即生效
# 改变服务节点个数
kubectl scale --replicas=2 deployment/deployment-example

#重新部署，kubectl使用yaml文件中的配置覆盖内存中的配置
kubectl apply -f nginx-deployment.yaml 
# 删除部署，不会删除nginx-deployment.yaml文件
kubectl delete -f nginx-deployment.yaml 
#查看运行时信息
kubectl describe pods/deployment-example-56767584bd-8d48s
#查看日志
kubectl logs -f pods/deployment-example-56767584bd-8d48s
# 查看容器内部配置
kubectl exec deployment-example-56767584bd-8d48s -- cat /etc/nginx/nginx.conf





#############################kubernetes运维###################################
#查看节点
kubectl get nodes
#查看node详情
kubectl describe node/k8s2
#查看yaml文件
kubectl get node/k8s2 -o yaml|more
#隔离node然后删除node
kubectl drain --ignore-daemonsets k8s2
kubectl delete node k8s2
# 在被移除的节点上执行kubectl reset，节点恢复到未加入集群之前的状态
kubeadm reset
# 暂时隔离node
kubectl cordon k8s3
# 恢复节点
kubectl uncordon k8s3
#查看node label
kubectl get nodes --show-labels
# 查看label
kubectl get pods --show-labels
#加标签
kubectl label pod/deployment-example-56767584bd-knktt key1=value1
#删除label
kubectl label pod/deployment-example-56767584bd-knktt key1-
# 更新label
kubectl label pod/deployment-example-56767584bd-knktt key1=value2 --overwrite
# 创建namespace
kubectl create namespace foo
# 查看namespace
kubectl get namespace
# 在指定namespace中创建pods
kubectl create -f nginx-deployment.yaml -n foo
# 查看namespace下的pods
kubectl get pods -n foo
# 删除namespace，namespace中的pod也被删除了
kubectl delete namespace foo

kubectl create namespace foo
kubectl create -f nginx-deployment.yaml -n foo
kubectl config get-contexts
# 创建context
kubectl config set-context foo-ctx --namespace=foo --cluster=kubernetes --user=kubernetes-admin
# 切换context
kubectl config use-context foo-ctx
# 星号位于foo-ctx中了
kubectl config get-contexts
# 此时只能看到、管理foo-ctx中的pods
kubectl get pods
# 创建service
kubectl  create -f nginx-svc.yaml 
# 查看service
kubectl get service

# 资源请求（resource requests，简称requests）
容器希望分配到的，而且可完全保证的资源量
# 资源限制
容器最多能使用的资源上限，上限会影响资源竞争时的解决策略
若未显式配置request、limit，则request==limit

# 单独给每个pod配置计算机资源很繁琐，可以使用limitrange限制namespace的计算资源
# 创建limitrange，之前已经执行kubectl config set-context foo-ctx --namespace=foo，所以当前的namespace是foo
kubectl create -f limitrange.yaml 
# 查看limitrange
kubectl get limitrange
# 查看namespace详情中的Resource Limits
kubectl describe namespace/foo
# 删除pod后，kubernetes会重启新的pod，确保replicas数量
# 所以要删除deployment
kubectl get deployment
kubectl delete deployment deployment-example
# 创建deployment
kubectl create -f nginx-deployment.yaml 
# 查看pod的详细信息
kubectl describe pods/deployment-example-56767584bd-tn89x

# 使用resourcequota限制namespace
# 删除部署
kubectl delete -f nginx-deployment.yaml
# 创建resourcequota 
kubectl create -f resourcequota.yaml 
# 查看namespace中的Resource Quotas
kubectl describe namespace/foo
# 创建deployment
kubectl create -f nginx-deployment.yaml 
# 查看namespace信息
kubectl describe namespace/foo


平台组件日志
  Node组件日志
  Master组件日志
  Addons日志

Node组件日志
  # kubelet日志  
  journalctl -u kubelet -f
  # pod日志
  cd /var/log/containers/
  tail -fn 100 XXX.log

Master组件日志  
  cd /var/log/containers/
  ls -l | grep apiserver
  tail -fn 100 XXX.log

工作负载日志：
  使用kubectl查看pod中的某个container日志
  进入container中查看日志文件输出
  李勇docker命令查看container日志  

  # 查看pod日志
  kubectl get pod
  kubectl logs -f deployment-example-56767584bd-vnrt5


kubernetes集群时间查看
事件查看
  event是api对象
  查看event


Troubleshooting (故障诊断)

  

#####################日志管理#############################
安装成功，但是无法访问
使用efk技术栈
1、在k8s集群之外的机器上安装es
  docker pull docker.elastic.co/elasticsearch/elasticsearch:6.2.4
  mkdir -p ~/.data/es_data
  chmod g+rwx ~/.data/es_data
  chgrp 1000 ~/.data/es_data
  chown 1000 -R ~/.data/es_data
  docker run -d --restart=unless-stopped -p 9200:9200 -p 9300:9300 -v /root/.data/es_data:/usr/share/elasticsearch/data --ulimit nofile=65536:65536 -e "bootstrap.memory_lock=true" --ulimit memlock=-1:-1 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.2.4
  curl http://127.0.0.1:9200/_cat/health

2、安装fluentd
  在master上安装fluentd
  修改fluentd-daemonset-elasticsearch-rbac.yaml中FLUENT_ELASTICSEARCH_HOST的value为es地址
  # 修改集群所有节点的目录权限
  chmod 777 /var/log
  chmod 777 /var/lib/docker/containers
  # 部署fluentd
  kubectl apply -f fluentd-daemonset-elasticsearch-rbac.yaml
  # 查看状态
  kubectl get pods -n kube-system|grep fluentd
  # 查看一个节点的日志，需要用-n指定namespace
  kubectl logs -f pods/fluentd-wj6qg -n kube-system

3. 安装kibana
  # 主节点，新增桥接网卡，gateway使用宿主机gateway
  vim /etc/network/interfaces
  auto enp0s8
  iface enp0s8 inet static
  address 192.168.1.215
  netmask  255.255.255.0
  gateway  192.168.1.1
  # 重启机器
  reboot -h now

  修改kibana-deployment.yaml：
    ELASTICSEARCH_URL的值为es主机地址
    修改版本kibana-logging跟es一致，为6.2.4
    删除：
    - name: SERVER_BASEPATH
      value: /api/v1/namespaces/kube-system/services/kibana-logging/proxy  

  kibana-service.yaml
    添加，以便宿主机访问  type: NodePort 

  kubectl apply -f kibana-service.yaml
  kubectl apply -f kibana-deployment.yaml
  # 查看kibana日志
  kubectl get pods -n kube-system|grep kibana
  kubectl logs -f kibana-logging-f9b7489cb-h5dn6 -n kube-system

  # 获取访问端口
  kubectl get svc --all-namespaces
  # 宿主机访问地址
  http://192.168.1.215:32683/app/kibana#/home?_g=()
  kibana页面创建index pattern步骤
  1、点击Discover
  2、勾选Include system indices
  3、输入Index pattern的值以便匹配index，例如.monitoring*
  4、下一步，选择timestamp来过滤字段


############################服务治理##################################  
wget -c https://github.com/istio/istio/releases/download/1.0.2/istio-1.0.2-linux.tar.gz
mv istio-1.0.2-linux.tar.gz /usr/local/
cd /usr/local/
tar zxvf istio-1.0.2-linux.tar.gz 
cd istio-1.0.2/

# 安装crd
kubectl apply -f install/kubernetes/helm/istio/templates/crds.yaml
# 查看crd
kubectl get crd -n istio-system


# 安装
docker pull anjia0532/istio-release.proxy_init:1.0.2
docker pull anjia0532/istio-release.proxyv2:1.0.2
docker pull anjia0532/istio-release.galley:1.0.2
docker pull anjia0532/istio-release.grafana:1.0.2
docker pull anjia0532/istio-release.mixer:1.0.2
docker pull anjia0532/istio-release.pilot:1.0.2
docker pull anjia0532/istio-release.citadel:1.0.2
docker pull anjia0532/istio-release.servicegraph:1.0.2
docker pull anjia0532/istio-release.sidecar_injector:1.0.2

docker tag anjia0532/istio-release.proxy_init:1.0.2 gcr.io/istio-release/proxy_init:1.0.2
docker tag anjia0532/istio-release.proxyv2:1.0.2 gcr.io/istio-release/proxyv2:1.0.2
docker tag anjia0532/istio-release.galley:1.0.2 gcr.io/istio-release/galley:1.0.2
docker tag anjia0532/istio-release.grafana:1.0.2 gcr.io/istio-release/grafana:1.0.2
docker tag anjia0532/istio-release.mixer:1.0.2 gcr.io/istio-release/mixer:1.0.2
docker tag anjia0532/istio-release.pilot:1.0.2 gcr.io/istio-release/pilot:1.0.2
docker tag anjia0532/istio-release.citadel:1.0.2 gcr.io/istio-release/citadel:1.0.2
docker tag anjia0532/istio-release.servicegraph:1.0.2 gcr.io/istio-release/servicegraph:1.0.2
docker tag anjia0532/istio-release.sidecar_injector:1.0.2 gcr.io/istio-release/sidecar_injector:1.0.2

# 部署
kubectl apply -f install/kubernetes/istio-demo.yaml
# 查看
kubectl get pods -n istio-system














