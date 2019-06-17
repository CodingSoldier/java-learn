FROM openjdk:8
ADD *.jar /app/app.jar
ADD entrypoint.sh /app/
# 每个工程的port都不一样
ENV PORT="10005" TIME="Asia/Shanghai" JAVA_OPS="-Xmx256m -Xms256m -XX:+UseConcMarkSweepGC"
RUN set -e \
    && chmod +x /app/entrypoint.sh \
    && ln -snf /usr/share/zoneinfo/$TIME /etc/localtime \
    && echo $TIME > /etc/timezone
ENTRYPOINT  ["/app/entrypoint.sh"]
EXPOSE $PORT
STOPSIGNAL SIGTERM

