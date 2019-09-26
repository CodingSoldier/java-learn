使用自定义配置文件
  .\bin\logstash.bat -f .\imooc-config\codec.conf
  在doc窗口输入hello，会被logstash格式化为json


多实例
  1、复制config目录中的所有配置到instance2目录
  2、修改 instance2/logstash.yml
      node.name: test2
      path.data: instance2-data
      # 指定配置文件
      path.config: imooc-config/test.conf
  3、修改config/logstash.yml
      # 指定配置文件
      path.config: imooc-config/codec.conf

  4、.\bin\logstash.bat  启动一个实例
        输入hello，返回json数据
     .\bin\logstash.bat --path.settings instance2  启动第二个实例
        输入hello，返回json数据，json中多了一个name属性


收集Apache日志：
  编写  .\demo_data\imooc_data\ls.conf
  运行logstash  .\bin\logstash.bat -f .\demo_data\imooc_data\ls.conf -r
  postman发送请求 
    get  
    http://127.0.0.1:7474  
    body raw  Text  数据如下
      183.162.52.7 - - [10/Nov/2016:00:01:02 +0800] "POST /api3/getadv HTTP/1.1" 200 813 "www.imooc.com" "-" cid=0&timestamp=1478707261865&uid=2871142&marking=androidbanner&secrect=a6e8e14701ffe9f6063934780d9e2e6d&token=f51e97d1cb1a9caac669ea8acc162b96 "mukewang/5.0.0 (Android 5.1.1; Xiaomi Redmi 3 Build/LMY47V),Network 2G/3G" "-" 10.100.134.244:80 200 0.027 0.027
  使用 http://grokdebug.herokuapp.com/ 获取日志的grok配置

使用x-pack 
  logstash.yml开启注释内容
  xpack.management.elasticsearch.url: ["http://localhost:9200"]


        