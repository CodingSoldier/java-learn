package ssm.quartz;

/**
 * quartz与spring整合
 * 1、maven添加依赖
 *  <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-context-support</artifactId>
         <version>${spring.version}</version>
     </dependency>
     <dependency>
         <groupId>org.quartz-scheduler</groupId>
         <artifactId>quartz</artifactId>
         <version>2.3.0</version>
     </dependency>
     <dependency>
         <groupId>org.quartz-scheduler</groupId>
         <artifactId>quartz-jobs</artifactId>
         <version>2.3.0</version>
     </dependency>
 * 2、新建本job类，在方法中编写需要定时执行的业务代码
 * 3、新建quartz-spring.xml配置文件
 * 4、在application-context.xml中import导入quartz-spring.xml配置文件
 */
public class C_QuartzSpring_Job {

    public void execute(){
        System.out.println("**********调度*********execute方法**********");
    }

}
