## liquibase sql脚本管理工具（类似与git--代码管理工具）

1. [官方文档](https://docs.liquibase.com/start/home.html)    
2. [脚本命令参数执行示例](https://docs.liquibase.com/commands/home.html#database-inspection-commands)    
3. [文件定义格式（sql、xml等等）](https://docs.liquibase.com/concepts/changelogs/sql-format.html)    
4. [官方文档--springboot使用方式](https://contribute.liquibase.com/extensions-integrations/directory/integration-docs/springboot/)  
5. [源码怎么构建](https://contribute.liquibase.com/code/)  

## Changelog 文件的结构
changelog->changeset()->changeTypes(多个操作类型，例如insert、update、delete、createTable等等)
![结构](https://gitee.com/RenZhenGongZuo/base-components/raw/master/img/liquibase/struct1.png)

## 最佳实践
[文档地址](https://docs.liquibase.com/concepts/bestpractices.html)

1. 定义好版本目录
```text
com
  example
    db
      changelog
        db.changelog-root.xml
        db.changelog-1.0.xml
        db.changelog-1.1.xml
        db.changelog-2.0.xml
      DatabasePool.java
      AbstractDAO.java
```
1. 定义团队的changeset ID 格式  我们建议您使用从 1 开始的递增数字序列
2. 请为每个changeset增加 注释 
3. 规划回滚策略 (为了确保安全且可预测的回滚，请在生产环境中运行它们之前在开发环境中对其进行测试)
```text
1. 通过脚本执行回滚
2. 通过测试用例执行 (自己打liquibase的扩展测试包，然后引用)
        //回滚到某个时间点
        liquibase = createLiquibase(rollbackChangeLog);
        liquibase.update(this.contexts);

        liquibase = createLiquibase(rollbackChangeLog);
        liquibase.rollback(new Date(0), this.contexts);
        //生成回滚的sql
        liquibase = createLiquibase(rollbackChangeLog);
        liquibase.futureRollbackSQL(new Contexts(this.contexts), new LabelExpression(), writer);
        //打tag回滚到tag
        liquibase = createLiquibase(completeChangeLog);
        liquibase.checkLiquibaseTables(false, null, new Contexts(), new LabelExpression());
        liquibase.tag("empty");
        liquibase = createLiquibase(rollbackChangeLog);
        liquibase.update(new Contexts());
        liquibase.rollback("empty", new Contexts());
        //回滚8个changeset
        liquibase = createLiquibase(rollbackChangeLog);
        liquibase.rollback(8, this.contexts);
```
4. liquibase 永远不会将相同的变更集应用于同一环境，除非您覆盖其默认值(变更集属性 runOnChange 设置为 "true")

## 团队管理数据库
1. 一个团队管理一个数据库或一个团队管理多个数据库
   ![图片1](https://gitee.com/RenZhenGongZuo/base-components/raw/master/img/liquibase/img1.png)
   ![图片2](https://gitee.com/RenZhenGongZuo/base-components/raw/master/img/liquibase/img2.png)
2. 多个团队管理一个数据库或多个团队管理多个数据库
   ![创建数据库](https://gitee.com/RenZhenGongZuo/base-components/raw/master/img/liquibase/img3.png)
    1. 您将使用单个 URL 和凭据连接到所有数据库。这要求您使用具有多个数据库权限的单个服务帐户。
    2. 在 SQL 脚本中，每个对象都需要使用数据库名称、架构名称和对象名称进行完全限定
    3. Liquibase 跟踪表 （ DATABASECHANGELOG 和 DATABASECHANGELOGLOCK ） 将仅在 URL 中指定的一个数据库中创建。对多个数据库的部署将由
       URL 中指定的数据库中的单个 DATABASECHANGELOG 跟踪表进行跟踪。
    4. 当多个团队共享公共数据库时，无法使用每个团队自己的应用程序存储库。此用例需要为共享数据库设置专用的 SQL 存储库。

```text
-- 面向对象发布1
com
  example
    db
      changelog
        changelog-root.xml
        changelog-index.xml
        changelog-procedure.xml
        changelog-table.xml
        changelog-view.xml
-- 面向对象发布2
com
  example
    db
      changelog
        changelog-root.xml
        changelog-indexes
          my-favorite-index.xml
          that-other-index.xml
        changelog-tables
          employees.xml
          customers.xml
-- 
com
  example
    db
      changelog
        changelog-root.xml
        changelog-1.0.xml
        changelog-1.1.xml
        changelog-2.0.xml
com
  example
    db
      changelog
        changelog-root.xml
        changelog-1.x
          changelog-1.0.xml
          changelog-1.1.xml
        changelog-2.x
          changelog-2.0.xml        
```

[官网文档地址](https://contribute.liquibase.com/code/get-started/env-setup/)

<details> <summary>自己操作后的命令记录</summary>

## diff 的命令允许您将两个相同类型或不同类型的数据库相互比较

```text
liquibase diff 
--url="jdbc:oracle:thin:@<IP OR HOSTNAME>:<PORT>:<SERVICE NAME OR SID>"
--username=<USERNAME>
--password=<PASSWORD>
--reference-url="jdbc:oracle:thin:@<IP OR HOSTNAME>:<PORT>:<SERVICE NAME OR SID>"
--reference-username=<USERNAME>
--reference-password=<PASSWORD>
```

## generate-changelog 命令创建一个更改日志文件，该文件具有一系列变更集，这些变更集描述如何重新创建数据库的当前状态

[generate-changelog](https://docs.liquibase.com/commands/inspection/generate-changelog.html)

```text
第一种方法.通过liquibase.properties 指定数据源信息，然后执行下面命令
liquibase generate-changelog --changelog-file=example-changelog.xml 
第二种方法
liquibase generate-changelog 
--url="jdbc:oracle:thin:@<IP OR HOSTNAME>:<PORT>:<SERVICE NAME OR SID>"
--username=<USERNAME>
--password=<PASSWORD>

如果您的数据库需要 & in URL，则在命令行上指定 URL 时，可能需要将 URL 括在双引号中
```

</details>