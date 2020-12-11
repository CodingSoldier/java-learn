#!/bin/bash
JAVA_HOME=/usr/local/software/jdk1.8.0_201
DJENKINS_HOME=/usr/local/software/jenkins/jenkins_home
nohup $JAVA_HOME/bin/java -DJENKINS_HOME=$DJENKINS_HOME -jar -Xms512m -Xmx512m -XX:+UseConcMarkSweepGC  /usr/local/software/jenkins/jenkins.war --httpPort=8888 > /tmp/jenkins.log 2>&1 &



