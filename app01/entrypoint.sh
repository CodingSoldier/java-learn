#!/bin/sh
# exec后面的命令都在同一进程中执行，app.jar服务也跟entrypoint.sh为同一个pid#
# 则执行docker stop containerId时，服务和容器马上停止，若服务是容器的子pid，则容器10s后才停止

# 自定义环境变量启动 docker run --name=app01 -e JAVA_OPS='-Xmx256m -Xms256m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails' codingsoldier/app01
exec java ${JAVA_OPS} -jar /app/app.jar
