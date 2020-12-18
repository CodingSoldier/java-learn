安装nginx  

下载  http://nginx.org/en/download.html

yum -y install gcc pcre-devel zlib-devel openssl openssl-devel

tar -zxvf nginx-1.9.9.tar.gz

cd nginx-1.9.9

./configure --prefix=/usr/local/nginx

make
make install

# nginx命令
/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf  # 启动指定配置文件
/usr/local/nginx/sbin/nginx                      # 启动
/usr/local/nginx/sbin/nginx -s reload            # 重新载入配置文件
/usr/local/nginx/sbin/nginx -s stop              # 停止 Nginx

自启动脚本的方式似乎无效了，需要些unit文件的方式
vim /usr/lib/systemd/system/nginx.service

[Unit]
Description=nginx 
After=network.target 
 
[Service] 
Type=forking 
ExecStart=/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf
ExecReload=/usr/local/nginx/sbin/nginx -s reload
ExecStop=/usr/local/nginx/sbin/nginx -s stop
PrivateTmp=true 
 
[Install] 
WantedBy=multi-user.target


设置nginx服务开机自启动
systemctl enable nginx

检查nginx配置是否正确
nginx -t -c /etc/nginx/nginx.conf


###########yum安装并配置SSL###############

openssl req \
       -newkey rsa:2048 -nodes -keyout rsa_private.key \
       -x509 -days 365 -out cert.crt \
       -subj "/C=CN/ST=GD/L=SZ/O=vihoo/OU=dev/CN=192.168.3.180/emailAddress=yy@qq.com"

一条命令生成证书
openssl req \
       -newkey rsa:2048 -nodes -keyout rsa_private.key \
       -x509 -days 365 -out cert.crt \
       -subj "/C=CN/ST=GD/L=SZ/O=vihoo/OU=dev/CN=192.168.3.179/emailAddress=yy@qq.com"

安装nginx，使用yum安装。
EPEL的全称叫 Extra Packages for Enterprise Linux 。
EPEL是由 Fedora 社区打造，为 RHEL 及衍生发行版如 CentOS、Scientific Linux 等提供高质量软件包的项目。
装上了 EPEL之后，就相当于添加了一个第三方源。

yum install -y epel-release
yum install -y nginx

rpm -ql nginx  查看nginx的配置文件

# 启动nginx。配置了systemd，可以使用systemctl启动，路径/usr/lib/systemd/system/nginx.service
systemctl start nginx

有了/usr/sbin/nginx文件，也可以直接使用 nginx 命令启动

nginx日志
	/var/log/nginx/*.log

配置nginx https
	查看nginx的配置文件，配置文件的注释中已经有了https的配置
	less /etc/nginx/nginx.conf
	# 创建证书目录，私钥目录
	mkdir /etc/pki/nginx
	mkdir /etc/pki/nginx/private
	#拷贝证书和私钥到此目录，/etc/httpd/pki/
	cp /etc/httpd/pki/server.crt /etc/pki/nginx/server.crt
	cp /etc/httpd/pki/server.key /etc/pki/nginx/private/server.key
	放开nginx https的注释
	# 重启nginx即可
	systemctl restart nginx

nginx强制走https
server {
    listen       80 default_server;
    listen       [::]:80 default_server;

    省略其他

    # 注释掉80端口转发规则
    # location / {
    # }

    # 80端口重定向到443
    return 301 https://$host$request_uri;

    省略其他
}



