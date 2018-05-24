package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class H_exclusiveGateWay {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**部署流程*/
    @Test
    public void deploymentProcessDefinition(){
        InputStream b = this.getClass().getResourceAsStream("H_exclusiveGateWay.bpmn");
        InputStream png = this.getClass().getResourceAsStream("H_exclusiveGateWay.png");
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("H_exclusiveGateWay")
                .addInputStream("H_exclusiveGateWay.bpmn", b)
                //addInputStream（名称和资源文件名称要一模一样）不然结果是能部署，但是没有生成流程定义，
                .addInputStream("H_exclusiveGateWay.png", png)
                .deploy();
        System.out.println("部署Id："+deployment.getId());
        System.out.println("部署名称："+deployment.getName());
    }

    /** 启动流程实例 */
    @Test
    public void startProcessInstance(){
        String processDefinitionKey = "H_exclusiveGateWayKey"; // 使用Key的启动，默认按照对心版本的流程定义启动
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey);

        System.out.println("流程实例Id:"+pi.getId());
        System.out.println("流程定义Id:"+pi.getProcessDefinitionId());
    }

    /**完成我的任务*/
    @Test
    public void completeMyPersonalTask(){
        String taskId = "115004";
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("money","333");
        //完成此任务，要经过排他网关来判断下一个流程人是谁，排他网管的线中已经设置了money变量，完成此任务也必须设置money变量，不然会报错。
        //H_exclusiveGateWay.bpmn中的condition表达式${100<money&&money<10000}不能有空格，错误示范：${100 < money && money < 10000}
        processEngine.getTaskService()
                .complete(taskId,variables);

        System.out.println("完成任务：任务Id"+taskId);
    }

}
















