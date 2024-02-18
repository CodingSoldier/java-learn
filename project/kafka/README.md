下载kafka_2.13-3.6.1
https://downloads.apache.org/kafka/3.6.1/kafka_2.13-3.6.1.tgz

kafka_2.13-3.6.1\config\zookeeper.properties 修改 
maxClientCnxns=10000

kafka_2.13-3.6.1\config\server.properties 修改 
listeners=PLAINTEXT://:9092
zookeeper.connect=127.0.0.1:2181

新建 kafka_2.13-3.6.1\zookerper-start.bat
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

新建 kafka_2.13-3.6.1\kafka-start.bat
.\bin\windows\kafka-server-start.bat .\config\server.properties

启动zookerper-start.bat、kafka-start.bat

进入：kafka_2.13-3.6.1\bin\windows>  
创建topic
.\kafka-topics.bat --bootstrap-server 127.0.0.1:9092 --create --partitions 1 --replication-factor 1 --topic first


