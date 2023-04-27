阿里云物模型

Demo 使用说明
1.修改 device_id.son文件，填入三元组等信息：
      对于企业实例, 或者2021年07月30日之后（含当日）开通的物联网平台服务下公共实例,
      实例的详情页面下会有实例id,一般格式为iot-*******，请将其填入device_id.json的instanceId字段，
      请参考https://help.aliyun.com/document_detail/147356.htm

      对于2021年07月30日之前（不含当日）开通的物联网平台服务下公共实例，
      请将开通物联网平台时所选的区域信息，填入device_id.json的region字段
      具体包括如下选项：
          上海  ------  cn-shanghai
          新加坡 -----  ap-southeast-1
          日本  -----   ap-northeast-1
          美西  -----   us-west-1
          德国  -----   eu-central-1
       比如上海的region的话，请填入cn-shanghai
       注：北京和深圳地域的用户请在device_id.json中填写instanceId,不要填写region

修改 com.aliyun.alink.devicesdk.demo.FileUtils.readFile 文件地址

2.执行HelloWorld工程的main方法
