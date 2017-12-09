package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/11/27
 */
public class I_ParallelGateWay {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**部署流程*/
    @Test
    public void deploymentProcessDefinition(){
        InputStream b = this.getClass().getResourceAsStream("I_ParallelGateWay.bpmn");
        InputStream png = this.getClass().getResourceAsStream("I_ParallelGateWay.png");
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("并行网管")
                .addInputStream("I_ParallelGateWay.bpmn", b)
                //addInputStream（名称和资源文件名称要一模一样）不然结果是能部署，但是没有生成流程定义，
                .addInputStream("I_ParallelGateWay.png", png)
                .deploy();
        System.out.println("部署Id："+deployment.getId());
        System.out.println("部署名称："+deployment.getName());
    }

    /** 启动流程实例 */
    @Test
    public void startProcessInstance(){
        String processDefinitionKey = "I_ParallelGateWayKey"; // 使用Key的启动，默认按照对心版本的流程定义启动
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey);

        System.out.println("流程实例Id:"+pi.getId());
        System.out.println("流程定义Id:"+pi.getProcessDefinitionId());
    }

    /**完成我的任务*/
    @Test
    public void completeMyPersonalTask(){
        String taskId = "125002";

        processEngine.getTaskService()
                .complete(taskId);

        System.out.println("完成任务：任务Id"+taskId);
    }

}
















