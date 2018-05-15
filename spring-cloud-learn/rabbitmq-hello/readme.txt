直接安装Erland、RabbitMQ


安装界面管理插件
    在开始菜单找到RabbitMQ Command Prompt
    输入：rabbitmq-plugins enable rabbitmq_management命令安装插件

启动Rabbitmq
    在菜单中找到 RabbitMQ Service - start 启动
    在菜单中找到 RabbitMQ Service - stop 停止


启动成功后，浏览器中输入http://localhost:15672，可以看到管理界面，
默认用户密码是：guest/guest