# emqx 

### docker 安装

docker pull emqx/emqx:v4.0.5

docker run -tid --name emqx -p 1883:1883 -p 8083:8083 -p 8081:8081 -p 8883:8883 -p 8084:8084 -p 18083:18083 emqx/emqx:v4.0.5

控制台访问地址：http://192.168.1.231:18083/

默认用户名/密码：admin/public

### 认证
EMQX 默认开启匿名认证，任何客户端都可以连接

/opt/emqx/etc

allow_anonymous = true

emqx restart 重启

启用 emqx_auth_username 插件，使用api接口新增密码

curl --location --request POST 'http://192.168.1.231:18083/api/v4/auth_username' \
--header 'Authorization: Basic YWRtaW46cHVibGlj' \
--header 'Content-Type: application/json' \
--data-raw '{
"username": "user",
"password": "123456"
}'

Authorization 是 Basic空格 加上 admin/public 的base64编码

默认配置中 ACL 是开放授权的，acl_nomatch = allow，需要改为禁止
配置文件位置：etc/emqx.conf
acl_nomatch = deny

### webhook
修改 /opt/emqx/etc/plugins/emqx_web_hook.conf

web.hook.api.url = http://192.168.1.3:8991/mqtt/webhook

emqx restart 重启

在EMQX后台启动 emqx_web_hook 插件
















