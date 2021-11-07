package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class L_TaskAssignee {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deploymentProcessDefinition_inputStream() {
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("L_TaskAssignee.bpmn");
        InputStream inputStreamPng = this.getClass().getResourceAsStream("L_TaskAssignee.png");
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("动态指定assignee")
                .addInputStream("L_TaskAssignee.bpmn", inputStreamBpmn)
                .addInputStream("L_TaskAssignee.png", inputStreamPng)
                .deploy();
        System.out.println("部署Id：" + deployment.getId());
        System.out.println("部署名称：" + deployment.getName());
    }

    /**
     * 在流程启动阶段可以设置流程变量，将周芷若赋值给第一个任务块中的assignee的表达式${userName}
     */
    @Test
    public void startProcessInstance() {
        String processDefinitionKey = "TaskAssigneeKey";

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("userName", "周芷若");
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey, variables);

        System.out.println("流程实例Id:" + pi.getId());
        System.out.println("流程定义Id:" + pi.getProcessDefinitionId());

        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("周芷若")
                .list();

        System.out.println(tasks.toString());
    }

}
