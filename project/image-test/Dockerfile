FROM openjdk:8
ADD *.jar /app/app.jar
CMD  ["java -jar -Xms300m -Xmx300m /app/app.jar"]
EXPOSE 8080
STOPSIGNAL SIGTERM

