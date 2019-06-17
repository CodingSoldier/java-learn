FROM openjdk:8
ADD *.jar /app/app.jar
ADD entrypoint.sh /app/
ENV TIME Asia/Shanghai
ENV JAVA_OPS -Xmx512m -Xms512m -XX:+UseConcMarkSweepGC
RUN set -e \
    && chmod +x /app/entrypoint.sh \
    && ln -snf /usr/share/zoneinfo/$TIME /etc/localtime \
    && echo $TIME > /etc/timezone
ENTRYPOINT  ["/app/entrypoint.sh"]
EXPOSE 8080
STOPSIGNAL SIGTERM