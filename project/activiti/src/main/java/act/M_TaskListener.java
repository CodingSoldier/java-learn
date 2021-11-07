package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class M_TaskListener {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void deploymentProcessDefinition_inputStream() {
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("M_TaskListener.bpmn");
        InputStream inputStreamPng = this.getClass().getResourceAsStream("M_TaskListener.png");
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("监听实现assignee赋值")
                .addInputStream("M_TaskListener.bpmn", inputStreamBpmn)
                .addInputStream("M_TaskListener.png", inputStreamPng)
                .deploy();
        System.out.println("部署Id：" + deployment.getId());
        System.out.println("部署名称：" + deployment.getName());
    }

    /**
     * 通过监听实现assignee赋值
     */
    @Test
    public void startProcessInstance() {
        String processDefinitionKey = "TaskAssigneeListenerKey";

        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey);


        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("调用其他方法返回assignee的值")
                .list();

        System.out.println(tasks.toString());
    }

}
