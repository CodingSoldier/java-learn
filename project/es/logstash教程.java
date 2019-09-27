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


Apache日志实例
  .\bin\logstash.bat -f .\demo_data_learn\imooc_log\ls.conf





        