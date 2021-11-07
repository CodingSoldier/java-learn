package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;

public class K_ReceiveTask {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deploymentProcessDefinition_inputStream() {
        // 获取同包下面的文件
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("K_receiveTask.bpmn");
        InputStream inputStreamPng = this.getClass().getResourceAsStream("K_receiveTask.png");

        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("receiveTask接收任务")
                .addInputStream("K_receiveTask.bpmn", inputStreamBpmn) //
                .addInputStream("K_receiveTask.png", inputStreamPng) //
                .deploy(); // 完成部署
        System.out.println("部署Id：" + deployment.getId());
        System.out.println("部署名称：" + deployment.getName());
    }

    /**
     * 启动流程实例、设置流程变量、获取流程变量、向后执行（当前任务是自动操作的情况）
     */
    @Test
    public void startProcessInstance() {
        String processDefinitionKey = "receiveTaskKey";
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey);

        System.out.println("流程实例Id:" + pi.getId());
        System.out.println("流程定义Id:" + pi.getProcessDefinitionId());

        /** 查询执行对象Id */
        Execution execution1 = processEngine.getRuntimeService()
                .createExecutionQuery()
                .processInstanceId(pi.getId()) // 流程实例id
                .activityId("receivetask1") // 当日销售额的Key。活动节点的id的属性值
                .singleResult();

        /**使用流程变量来设置一个当日销售额用来传递业务参数*/
        processEngine.getRuntimeService()
                .setVariable(execution1.getId(), "汇总当日销售额", 21000);

        /** 向后执行一部，如果流程处于等待状态，使得流程继续等待 */
        processEngine.getRuntimeService()
                .signal(execution1.getId());

        /** 查询执行对象Id */
        Execution execution2 = processEngine.getRuntimeService()
                .createExecutionQuery()
                .processInstanceId(pi.getId()) // 流程实例id
                .activityId("receivetask2") // 发数据给老板的Key。活动节点的id的属性值
                .singleResult();

        /**从流程变量中获取汇总当日销售额*/
        Integer value = (Integer) processEngine.getRuntimeService()
                .getVariable(execution2.getId(), "汇总当日销售额");

        System.out.println("给老板发送短信：金额是：" + value);

        /** 向后执行一部，如果流程处于等待状态，使得流程继续等待 */
        processEngine.getRuntimeService()
                .signal(execution2.getId());
    }

}
