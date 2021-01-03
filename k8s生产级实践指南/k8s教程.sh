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
	# 坑爹的是机器重启后，目录下的证书又没了
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

ingress支持多个域名，linux、window都要配置多个hosts映射
apiVersion: networking.k8s.io/v1beta1
kind: Ingress
metadata:
  name: ingress-my
  annotations:
    kubernetes.io/ingress.class: "nginx"
spec:
  rules:
  - host: weave.k8s01.com
    http:
      paths:
      - path: /
        backend:
          serviceName: ui-weave-scope
          servicePort: 80
  - host: web1.k8s01.com
    http:
      paths:
      - path: /
        backend:
          serviceName: web1
          servicePort: 80


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


Namespace资源共享与隔离
	资源对象的隔离：Service、Deployments、Pod
	资源配额的隔离：Cpu、Memory


kubectl create namespace dev

kubectl get namespace

不同命名空间下
	POD的ip可以互通，Service的IP可以互通，service的ip不可ping，要用wget
	不同命名空间下，访问service需要在service name后面加上namespace，例如：
		wget service-myapp.dev   //dev是命名空间

07-node上传机器资源给APIServer.jpg
	集群节点将机器信息上传给ApiServer


资源限制
vim pod-resource.yaml
apiVersion: v1
kind: Pod
metadata:
  name: pod-resource
  labels:
    app: myapp
    tier: frontend
spec:
  containers:
  - name: myapp
    image: ikubernetes/stress-ng
    command: ["/usr/bin/stress-ng", "-m 1", "-c 1", "--metrics-brief"]
    resources:
      # 指定最低资源需求，1个cpu分成1000m，128Mi即128M
      # 如果集群所有机器的CPU、memory总和小于request中的配置，pod会一直处于pending状态
      # 如果pod有两个副本，集群所有机器资源CPU、memory总和只够运行一个，则一个pod能运行，一个处于pending
      requests:
        cpu: "200m"
        memory: "128Mi"
      # 资源限制,cpu:1表示最多使用1整个cpu
      # 如果集群所有机器的CPU、memory总和小于limits的配置，pod依旧可以运行起来
      limits:
        cpu: "1"
        memory: "256Mi"

部署
kubectl apply -f pod-resource.yaml

资源配置与服务可靠性等级
Requests == Limits   最可靠，优先级最高
不设置Requests、Limits，此服务最不可靠，优先级最低，建议设置Requests、Limits
Limits > Requests   比较可靠

资源限制
vim limits-test.yaml

apiVersion: v1
kind: LimitRange
metadata:
  name: test-limits
spec:
  limits:
  - max:
      cpu: 4000m
      memory: 2Gi
    min:
      cpu: 100m
      memory: 100Mi
    maxLimitRequestRatio:
      cpu: 3
      memory: 2
    type: Pod
  # Pod是逻辑概念，没有default值
  #
  # Container是Pod中的容器，是物理概念，可以有默认值。
  # 如果pod中的容器没定义默认值，则此空间下的容器将使用默认值
  - default:
      cpu: 300m
      memory: 200Mi
    defaultRequest:
      cpu: 200m
      memory: 100Mi
    max:
      cpu: 2000m
      memory: 1Gi
    min:
      cpu: 100m
      memory: 100Mi
    maxLimitRequestRatio:
      cpu: 5
      memory: 4
    type: Container

将资源限制应用在test命名空间
kubectl create -f limits-test.yaml -n test
kubectl describe limits -n test

在test namespace部署一个未定义资源限制的pod
kubectl apply -f myapp-pod-service-test.yaml
查看pod资源限制
kubectl get pods -n test -o yaml


vim compute-resource.yaml

apiVersion: v1
kind: ResourceQuota
metadata:
  name: resource-quota
spec:
  hard:
  	# pods最多有4个
    pods: 4
    requests.cpu: 2000m
    requests.memory: 4Gi
    limits.cpu: 4000m
    limits.memory: 8Gi


Pod驱逐策略 - Eviction
当内存持续1分30秒都小于1.5G，驱逐Pod
--eviction-soft=memory.available<1.5Gi
--eviction-soft-grace-period=memory.available=1m30s

--eviction-hard满足条件立即驱逐
--eviction-hard=memory.available<100Mi,nodefs.available<1Gi,nodefs.inodesFree<5%

磁盘紧缺
	删除死掉的pod、容器
	删除没用的镜像
	按优先级、资源占用情况驱逐pod

内存紧缺
	驱逐不可靠的pod，内存占用最大的最先被驱逐
	驱逐基本可靠的pod
	驱逐可靠的pod

Lable可以贴到Pod、Deployment、Service、Node（节点）等资源上

vim nginx-dev.yaml

#deploy
apiVersion: apps/v1
kind: Deployment
metadata:
  # 不同name的Deployment下的containers可以具备相同的名字
  # 即：修改Deployment的name后，本yaml还可以再次部署，再创建一个container
  # 没有地方定义pod名称，pod名称前面是Deployment的名称
  # service的selector只与pod的label有关，与deployment的name无关，即会把不同deployment的pod都关联上
  name: nginx
  namespace: dev
spec:
  selector:
    matchLabels:
      # selector的matchLabels键值对必须和template.metadata的labels键值对一样
      app: nginx
    # # label还支持表达式，选择label的key是group，值是dev或test的container
    # matchExpressions:
    # 	- {key: group, operator: In, values: [dev, test]}
  replicas: 1
  template:
    metadata:
      labels:
      	# 指定group: dev，以便被matchExpressions关联到
      	# group: dev
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.7.9
        ports:
        - containerPort: 80
---
#service
apiVersion: v1
kind: Service
metadata:
  name: nginx
  namespace: dev
spec:
  ports:
  - port: 80
    protocol: TCP
    targetPort: 80
  selector:
    app: nginx
  type: ClusterIP


根据label过滤pods
kubectl get pods -l app=nginx




# 就绪探针。
# docker容器启动成功，并不代表容器中的服务就能处理外部的请求，例如java应用启动需要一定时间
# Kubernetes提供了readinessProbe来检测pod中的容器是否可以接受外部流量
readinessProbe:
  httpGet:
    # 连接使用的schema，默认HTTP。
    scheme: HTTP
    # 访问的容器的端口名字或者端口号。端口号必须介于1和65525之间
    port: 10013
    # 访问的HTTP server的path
    path: /app3/info
  # 探测成功后，最少连续探测失败多少次才被认定为失败。默认是3。最小值是1。  
  failureThreshold: 3
  # 容器启动后第一次执行探测是需要等待多少秒。
  initialDelaySeconds: 20
  # 执行探测的频率。默认是10秒，最小1秒。
  periodSeconds: 10
  # 探测失败后，最少连续探测成功多少次才被认定为成功。默认是1。对于liveness必须是1。最小值是1。
  successThreshold: 1
  # 探测超时时间。默认1秒，最小1秒。
  timeoutSeconds: 1

#### 如果服务在 initialDelaySeconds + periodSeconds*failureThreshold 时间之后还没有启动，pod会重启，所以要注意控制时间 ####

##### coredns无法启动 #####
systemctl stop kubelet
systemctl stop docker
iptables --flush
iptables -tnat --flush 
systemctl start kubelet
systemctl start docker
保存iptables规则
service iptables save


部署策略
	Rolling update  滚动更新
	Recreate  重新创建。把旧的pod全部停掉后重新创建pod
	蓝绿部署   保持原有的不动，新增部署一个deployment；就像原来的是蓝色，新增绿色，等待所有绿色启动并测试完，修改service的selector把流量切换到新的pod上去
	金丝雀部署

修改/etc/hosts，添加本地ip与域名映射
vim /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4 k8s01.com

滚动更新：
  08-rollingUpdate.yaml
  kubectl apply -f 08-four-kind-deployment.yaml
  while true; do curl http://k8s01.com:30080/index.html; sleep 0.1; done
  修改镜像为 ikubernetes/myapp:v2

重新创建：
spec:
  strategy:
    type: Recreate

滚动更新过程可以暂停
kubectl rollout pause deploy deployment名称 -n dev
恢复部署
kubectl rollout resume deploy deployment名称 -n dev
回滚
kubectl rollout undo deploy deployment名称 -n dev

蓝绿部署步骤：
  1、deployment名字设置为app-v1.0。Pod的设置两个label {app: app, version: v1.0}
  2、启动deployment
  3、升级的时候新增一个deployment名字设置为app-v2.0。Pod的设置两个label {app: app, version: v2.0}
  4、启动新的deployment
  5、修改service的selector，选择{app: app, version: v2.0}

金丝雀部署步骤：
  在蓝绿部署的基础上稍加修改。
  3、新deployment的pod数量设置为1
  5、service的selector选择{app: app}

Pod
	Pod是最小调度单位
	本质是容器的隔离
	是逻辑概念
	每个Pod默认有一个Pause容器，第一个启动的容器就是pause容器

hosts文件由pod管理，同一个pod中多个容器的hosts是相同的


vim pod-hostname-volume.yaml

apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
spec:
  # 自定义pod的ip和hostname
  hostAliases:
  - ip: "10.244.2.120"
    hostnames:
    - "www.my-nginx.com"
  containers:
  - name: nginx
    image: nginx:1.7.9
    ports:
    - containerPort: 80
    volumeMounts:
    - name: shared-volume
      # 容器中的目录
      mountPath: /shared-web
  # 定义volume
  volumes:
  - name: shared-volume
    # 宿主机目录
    hostPath:
      path: /shared-volume-data

kubectl apply -f pod-hostname-volume.yaml 
kubectl get pods -o wide
在pod节点上查看hosts
docker exec -it cda254640d59 cat /etc/hosts


ProjectedVolume 投射数据卷，与普通的volume不一样，不是用来挂载目录的，而是APIServer投射一些文件到pod中
Secret、ConfigMap、DownwardAPI
 
####### Secret #######
# 查看secret
kubectl get secret
#生成base64
echo -n user|base64
echo -n pwd123456|base64

#创建secret文件
vim secret.yaml

apiVersion: v1
# Secret类型资源
kind: Secret
metadata:
  name: db-user-pass
# 浑浊类型，一般都用这种类型  
type: Opaque
data:
  username: dXNlcg==
  passwd: cHdkMTIzNDU2

# 创建secret，相当于把配置写到了etcd中
kubectl create -f secret.yaml

创建一个使用secret的Pod
vim pod-use-secret.yaml

apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
spec:
  containers:
  - name: nginx
    image: nginx:1.7.9
    ports:
    - containerPort: 80
    volumeMounts:
    - name: user-pass
      # 容器中的目录
      mountPath: /db-secret
      readOnly: true
  # 定义volume，使用projected类型
  volumes:
  - name: user-pass
    projected:
      sources:
      # 使用的secret是db-user-pass
      - secret:
          name: db-user-pass

部署
kubectl create -f pod-use-secret.yaml
kubectl get pods -o wide
进入容器中查看secret
docker exec -it c0a35c541874 /bin/sh
cd db-secret
db-secret目录下多了两个文件username、passwd，存储内容的是明文user、pwd123456
cat username
cat passwd

修改secret会同步到pod中
vim pod-use-secret.yaml
echo -n newpwd123456|base64
vim secret.yaml

kubectl apply -f secret.yaml

容器中的secret也会改变


####### ConfigMap #######
从文件创建configmap
kubectl create configmap web-game --from-file game.properties
查看configmap，使用显示的内容作为yaml文件，也可以创建configmap。
cm是configmap的简写
kubectl get cm web-game -o yaml

创建一个使用configmap的Pod
vim pod-use-configmap.yaml

apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
spec:
  containers:
  - name: nginx
    image: nginx:1.7.9
    ports:
    - containerPort: 80
    volumeMounts:
    - name: game
      mountPath: /etc/config/game
      readOnly: true
  # 定义volume使用configmap
  volumes:
  - name: game
    configMap:
      name: web-game

部署
kubectl create -f pod-use-configmap.yaml
进入容器中，/etc/config/game/目录下存在game.properties文件
容器中的程序可以访问/etc/config/game/game.properties拿到属性值

修改configmap内容，pod中的配置也会修改
kubectl edit cm web-game


####### ConfigMap第二种使用方式 #######
通过配置文件的形式创建configmap
vim nginx-configmap.yaml

apiVersion: v1
kind: ConfigMap
metadata:
  name: nginx-configmap
data:
  LOG_LEVEL: DEBUG

kubectl create -f nginx-configmap.yaml

vim pod-use-configmap-02.yaml

apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
spec:
  containers:
  - name: nginx
    image: nginx:1.7.9
    ports:
    - containerPort: 80
    # 方式1
    volumeMounts:
    - name: game
      mountPath: /etc/config/game
      readOnly: true
    # 方式2
    env:
      - name: LOG_LEVEL
        valueFrom:
          configMapKeyRef:
            # 指定configmap
            name: nginx-configmap
            # configmap中data的key
            key: LOG_LEVEL      
  # 方式1，定义volume使用configmap
  volumes:
  - name: game
    configMap:
      name: web-game

kubectl create -f pod-use-configmap-02.yaml

进入容器中执行
echo $LOG_LEVEL


######## DownwardAPI ########
vim pod-downwardapi.yaml

apiVersion: v1
kind: Pod
metadata:
  name: pod-downwardapi
  labels:
    app: downwardapi
    type: webapp
spec:
  containers:
  - name: nginx
    image: nginx:1.7.9
    ports:
    - containerPort: 80
    volumeMounts:
      - name: podinfo
        mountPath: /etc/podinfo
  # 创建一个volume，名字是podinfo
  # 类型是downwardAPI
  volumes:
    - name: podinfo
      projected:
        sources:
        - downwardAPI:
            items:
              # path是相对路径，根路径是容器的mountPath，例如：/etc/podinfo/labels
              # 获取metadata.labels的值
              - path: "labels"
                fieldRef:
                  fieldPath: metadata.labels
              - path: "name"
                fieldRef:
                  fieldPath: metadata.name
              - path: "namespace"
                fieldRef:
                  fieldPath: metadata.namespace 
              - path: "cpu-request"
                resourceFieldRef:
                  containerName: nginx
                  resource: limits.memory


进入容器中，执行 cat /etc/podinfo/labels 得到结果

app="downwardapi"
type="webapp


k8s的日志，默认位置
stdout、stderr
/var/lib/docker/containers/container名字/container名字-json.log

08-日志采集方案.jpg

 

helm是kubernetes的包管理工具，类似于yum/apt
helm解决的问题
	1、把yaml作为一个整体管理
	2、实现yaml的高效复用
	3、实现应用级别的版本管理

安装helm v3版本非常简单，下载helm二进制文件，复制到/usr/bin

官方安装页面
下载
https://get.helm.sh/helm-v3.4.2-linux-amd64.tar.gz
解压
tar zxvf helm-v3.4.2-linux-amd64.tar.gz
移动到/usr/bin目录
mv linux-amd64/helm /usr/bin/
验证是否安装成功
helm

添加存储库
helm repo add stable http://mirror.azure.cn/kubernetes/charts
helm repo add aliyun https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts
helm repo update
查看配置的存储库
helm repo list
helm search repo stable
删除存储库：
helm repo remove aliyun

查找 chart
helm search repo weave
安装，并将stable/weave-scope，将引用起名为ui
helm install ui stable/weave-scope
查看发布状态
helm list
查看状态
helm status ui

通过ingress暴露端口，也可以通过NodePort暴露
vim ingress-weave.yaml

apiVersion: networking.k8s.io/v1beta1
kind: Ingress
metadata:
  name: ingress-my
  annotations:
    kubernetes.io/ingress.class: "nginx"
spec:
  rules:
  - host: k8s01.com
    http:
      paths:
      - path: /
        backend:
          serviceName: ui-weave-scope
          servicePort: 80

http://k8s01.com:30080/

自定义chart
helm create mychart
cd mychart/
cd template
在template目录下创建deployment.yaml。--dry-run打印api对象而不是创建
kubectl create deployment web1 --image=nginx --dry-run -o yaml > deployment.yaml
运行deployment
kubectl apply -f deployment.yaml
通过deployment创建service.yaml
kubectl expose deployment web1 --port=80 --target-port=80 --type=NodePort --dry-run -o yaml > service.yaml
删除deployment
kubectl delete -f deployment.yaml
回到mychart父级目录
cd ../../
安装mychart
helm install web1 mychart/
查看端口
kubectl get svc
升级
helm upgrade web1 mychart/
卸载web1
helm uninstall web1

helm create template-chart



StatefulSet有状态应用
	每个pod独立，保持pod启动顺序和唯一性
	唯一的网络标识符，持久存储
	有序，比如mysql主从

部署有状态应用需要：
	无头service
		无头service的ClusterIP为null

部署一个StatefulSet
kubectl apply -f statefulset.yaml

StatefulSet部署的pod名称后面会带0、1、2数字标识
StatefulSet的pod的域名格式是
	主机名称.service名称.命名空间.svc.cluster.local
	例如：
	nginx-statefulset-0.nginx.default.svc.cluster.local

创建StatefulSet应用的yaml文件一般要有3部分
	无头service
	StatefulSet
	pvc
并且pvc的名称后面也带有0、1、2标识，
执行 kubectl delete -f statefulw文件.yaml 后，pvc不会被删除，应用重启后数据还在


通过ingress控制器、NodePort类型的service访问集群中的应用不需要经过APIServer


APIServer用于管理集群
	例如: kubectl客户端命令会经过APIServer

通过APIServer管理集群需要经过3层
	1、认证
	2、授权
	3、准入控制

k8s使用ssl认证是双向认证
	1、客户端需要认证服务端证书
	2、服务端事先签发一个证书给客户端，服务端要验证客户端身份，即证书的中subject

k8s提供rest风格的api接口访问资源，访问资源
起一个支持http的代理端口
kubectl proxy --port=8080
访问接口
curl http://localhost:8080/api/v1/namespaces
获取kube-system名称空间下的所有deployment
curl http://localhost:8080/apis/apps/v1/namespaces/kube-system/deployments

url可以认为是资源对象，请求方法被称为动作，例如:
	get、post、put、delete





ServiceMesh 服务网格，解决微服务网络层面的问题
ServiceMesh的实现项目有两种Linkerd、Istio
Istio
https://istio.io/latest/zh/
每个pod都有一个代理，使用代理接管流量









