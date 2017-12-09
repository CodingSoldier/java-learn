package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/11/5
 */
public class A_createTableAndProcessEngine {
    /**
     * 通过代码的方式创建Activiti数据表（共25张表）和 流程引擎配置对象ProcessEngineConfiguration
     */
    @Test
    public void createTableByCode() {
        //创建流程引擎配置对象StandaloneProcessEngineConfiguration，其包含了数据库连接配置
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        // 连接数据库的配置
        processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        processEngineConfiguration
                .setJdbcUrl("jdbc:mysql://localhost:3306/activiti?useUnicode=true&characterEncoding=utf8");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("cpq..123");
        /**
         public static final String DB_SCHEMA_UPDATE_FALSE = "false";不能自动创建表，需要表存在
         public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";先删除表再创建表
         public static final String DB_SCHEMA_UPDATE_TRUE = "true";如果表不存在，自动创建表
         */
        processEngineConfiguration
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        // 通过processEngineConfiguration获取工作流的核心对象，ProcessEnginee对象
        ProcessEngine processEngine = processEngineConfiguration
                .buildProcessEngine();
        System.out.println("创建ProcessEnginee对象成功:" + processEngine);
    }


    /**使用配置文件创建工作流需要的23张表 和 流程引擎配置对象ProcessEngineConfiguration，并直接获取流程引擎ProcessEngine*/
    @Test
    public void createTable_2(){
//		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
//		//工作流的核心对象，ProcessEnginee对象
//		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        /*ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml")	//
                .buildProcessEngine();
        System.out.println("processEngine:"+processEngine);*/

        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.xml")
                .buildProcessEngine();


    }

}
