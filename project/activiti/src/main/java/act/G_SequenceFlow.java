package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class G_SequenceFlow {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**部署流程*/
    @Test
    public void deploymentProcessDefinition(){
        //如果找不到资源文件，请查看资源文件是否被打包到target中
        InputStream b = this.getClass().getResourceAsStream("G_SequenceFlow.bpmn");
        InputStream png = this.getClass().getResourceAsStream("G_SequenceFlow.png");
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("G流程定义")
                .addInputStream("G_SequenceFlow.bpmn", b)
                //addInputStream（名称和资源文件名称要一模一样）不然结果是能部署，但是没有生成流程定义，
                .addInputStream("G_SequenceFlow.png", png)
                .deploy();
        System.out.println("部署Id："+deployment.getId());
        System.out.println("部署名称："+deployment.getName());
    }

    /** 启动流程实例 */
    @Test
    public void startProcessInstance(){
        String processDefinitionKey = "processKey"; // 使用Key的启动，默认按照对心版本的流程定义启动
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey);

        System.out.println("流程实例Id:"+pi.getId());
        System.out.println("流程定义Id:"+pi.getProcessDefinitionId());
    }

    /**完成我的任务*/
    @Test
    public void completeMyPersonalTask(){
        String taskId = "75004";
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("message","重要");
        processEngine.getTaskService()
                .complete(taskId,variables);
        /*
        complete方法接收的第二个参数是Map<String, Object>，message是当前任务“【审批】部门经理”到“【审批】总经理”之间连线，
        连线Main config中配置了${message=='不重要'}、${message=='重要'}，在完成当前任务是，complete接收message变量，并将值通过message变量值决定走哪条线。
        */

        System.out.println("完成任务：任务Id"+taskId);
    }

}
















