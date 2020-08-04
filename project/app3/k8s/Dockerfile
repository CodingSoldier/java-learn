FROM openjdk:8
ADD *.jar /app/app.jar
ADD entrypoint.sh /app/
# PORT="10013" 是项目端口号
ENV PORT="10013" JAVA_OPS="-Xmx256m -Xms256m -XX:+UseConcMarkSweepGC"
RUN chmod +x /app/entrypoint.sh
ENTRYPOINT  ["/app/entrypoint.sh"]
EXPOSE $PORT
STOPSIGNAL SIGTERM
