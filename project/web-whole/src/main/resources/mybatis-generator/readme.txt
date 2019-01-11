pom.xml配置mybatis-generator-maven-plugin，
maven插件使用方法：
新建maven命令，取名为 mybatis-generator
Working directory:填写使用本插件的项目根路径，如：E:/workspace/learnjava/webwhole
Command line: mybatis-generator:generate -e
具体的command line可通过Maven Project窗口，点击展开相应插件查看




mybatis generator 插件源码修改
    1、下载mybatis-generator-core（1.3.6版本）源码
    2、替换JavaBeansUtil.java
    3、clean、install  mybatis-generator-core工程

JavaBeansUtil.java修改了getCamelCaseString()

支持：
     "id_test"            idTest
     "idUpChar"           idUpChar
     "UpTwoInt"           upTwoInt
     "lower_Up_Date"      lowerUpDate
     "ID_ALL_DOUBLE"      idAllDouble
     "First_two_Three"    firstTwoThree
