######安装########
cd  /usr/local/consul
wget https://releases.hashicorp.com/consul/1.4.4/consul_1.4.4_linux_amd64.zip
unzip consul_1.4.4_linux_amd64.zip
	解压出来后是一个超大的可执行文件
mv consul /usr/local/bin/

开发环境模式运行
consul agent -dev -ui -node=consul-dev -client=192.168.3.179
访问
http://192.168.3.179:8500/ui/dc1/services


#######集群模式。server节点
touch /etc/myconsul.d
consul agent -server -bootstrap-expect=1 \
    -data-dir=/tmp/consul -node=agent-one -bind=192.168.3.179 \
    -enable-script-checks=true -config-dir=/etc/myconsul.d

########client节点，-ui -client=0.0.0.0 加上这两个参数才能打开UI页面，以及在外网连接此agent
touch /etc/myconsul.d
consul agent -ui -client=0.0.0.0 \
	-data-dir=/tmp/consul -node=agent-two -bind=192.168.3.180 \
     -enable-script-checks=true -config-dir=/etc/myconsul.d

#######server节点
consul join 192.168.3.180
consul members


-bind：为该节点绑定一个地址
-enable-script-checks=true：设置检查服务为可用
-join：加入到已有的集群中
-server 表示当前使用的server模式
-node：指定当前节点在集群中的名称 
-config-file - 要加载的配置文件
-config-dir：指定配置文件，定义服务的，默认所有以.json结尾的文件都会读
-datacenter: 数据中心没名称，不设置的话默认为dc
-client: 客户端模式
-ui: 使用consul自带的ui界面 
-data-dir consul存储数据的目录
-bootstrap：用来控制一个server是否在bootstrap模式，在一个datacenter中只能有一个server处于bootstrap模式，当一个server处于bootstrap模式时，可以自己选举为raft leader。
-bootstrap-expect：在一个datacenter中期望提供的server节点数目，当该值提供的时候，consul一直等到达到指定sever数目的时候才会引导整个集群，该标记不能和bootstrap公用

这两个参数十分重要， 二选一，如果两个参数不使用的话，会出现就算你使用join将agent加入了集群仍然会报 
2018/10/14 15:40:00 [ERR] agent: failed to sync remote state: No cluster leader



无效的节点可以去server节点的服务器上删除
	curl -X PUT http://127.0.0.1:8500/v1/agent/service/deregister/pay-service-127.0.0.1

service模式也可以这么删
Healthy Nodes
	注册中心名
	服务头名称
	服务id
删除的时候删服务id
	curl -X PUT http://127.0.0.1:8500/v1/agent/service/deregister/服务id




######部署指南##### https://kingfree.gitbook.io/consul/day-1-operations/deployment-guide
#######server模式

1、添加consul systemd
vim /etc/systemd/system/consul.service

[Unit]
Description="HashiCorp Consul - A service mesh solution"
Documentation=https://www.consul.io/
Requires=network-online.target
After=network-online.target
ConditionFileNotEmpty=/etc/consul.d/consul.hcl

[Service]
ExecStart=/usr/local/bin/consul agent -config-dir=/etc/consul.d/
ExecReload=/usr/local/bin/consul reload
KillMode=process
Restart=on-failure
LimitNOFILE=65536

[Install]
WantedBy=multi-user.target

2、consul基础配置
vim /etc/consul.d/consul.hcl

datacenter = "dc1"
data_dir = "/opt/consul"
encrypt = "Luj2FZWwlt8475wD1WtwUQ=="
retry_join = ["192.168.3.180"]
performance {
  raft_multiplier = 1
}

3、server配置
vim /etc/consul.d/server.hcl

server = true
bootstrap_expect = 3


####client模式
1、添加consul systemd
vim /etc/systemd/system/consul.service

[Unit]
Description="HashiCorp Consul - A service mesh solution"
Documentation=https://www.consul.io/
Requires=network-online.target
After=network-online.target
ConditionFileNotEmpty=/etc/consul.d/consul.hcl

[Service]
ExecStart=/usr/local/bin/consul agent -config-dir=/etc/consul.d/
ExecReload=/usr/local/bin/consul reload
KillMode=process
Restart=on-failure
LimitNOFILE=65536

[Install]
WantedBy=multi-user.target

2、consul基础配置，选择一台加入ui
vim /etc/consul.d/consul.hcl

datacenter = "dc1"
data_dir = "/opt/consul"
encrypt = "Luj2FZWwlt8475wD1WtwUQ=="
ui = true
client_addr = "0.0.0.0"
performance {
  raft_multiplier = 1
}



参数与命令行的区别
ui: 相当于-ui 命令行标志。
acl_token：agent会使用这个token和consul server进行请求
acl_ttl：控制TTL的cache，默认是30s
addresses：一个嵌套对象，可以设置以下key：dns、http、rpc
advertise_addr：等同于-advertise
bootstrap：等同于-bootstrap
bootstrap_expect：等同于-bootstrap-expect
bind_addr：等同于-bind
ca_file：提供CA文件路径，用来检查客户端或者服务端的链接
cert_file：必须和key_file一起
check_update_interval：
client_addr：等同于-client
datacenter：等同于-dc
data_dir：等同于-data-dir
disable_anonymous_signature：在进行更新检查时禁止匿名签名
enable_debug：开启debug模式
enable_syslog：等同于-syslog
encrypt：等同于-encrypt
key_file：提供私钥的路径
leave_on_terminate：默认是false，如果为true，当agent收到一个TERM信号的时候，它会发送leave信息到集群中的其他节点上。
log_level：等同于-log-level node_name:等同于-node 
ports：这是一个嵌套对象，可以设置以下key：dns(dns地址：8600)、http(http api地址：8500)、rpc(rpc:8400)、serf_lan(lan port:8301)、serf_wan(wan port:8302)、server(server rpc:8300) 
protocol：等同于-protocol
rejoin_after_leave：等同于-rejoin
retry_join：等同于-retry-join
retry_interval：等同于-retry-interval 
server：等同于-server
syslog_facility：当enable_syslog被提供后，该参数控制哪个级别的信息被发送，默认Local0
ui_dir：等同于-ui-dir


#######systemd操作
systemctl enable consul
systemctl start consul
systemctl status consul
