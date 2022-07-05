#!/bin/sh

# 修改时间
cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

exec java ${JAVA_OPS} -jar /app/app.jar --spring.profiles.active=test
