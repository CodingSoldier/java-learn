package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;


public class D_ProcessInstancetest {

    // 自动读取classPath里面的activiti.cfg.xml文件直接加载对应的资源
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); // 所有对象都要使用的

    /**
     * 部署流程定义zip文件
     */
    @Test
    public void deploymentProcessDefinition_zip() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagram/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = processEngine.getRepositoryService() // 与流程定义和部署对象相关的Service
                .createDeployment() // 创建一个部署对象
                .name("zip流程定义") // 添加部署的名称
                .addZipInputStream(zipInputStream) // 制定zip格式文件完成部署
                .deploy(); // 完成部署
        System.out.println("部署Id：" + deployment.getId()); // 部署Id:27501
        System.out.println("部署名称：" + deployment.getName()); // 部署名称:zip流程定义
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        String processDefinitionKey = "helloworld"; // 使用Key的启动，默认按照最新版本的流程定义启动
        ProcessInstance pi = processEngine.getRuntimeService() // 与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey); // 使用流程定义的Key启动流程实例，key对应helloworld.bpmn文件中的流程名称

        System.out.println("流程实例Id" + pi.getId()); // 流程实例Id：30001
        System.out.println("流程定义Id" + pi.getProcessDefinitionId()); // 流程定义Id：helloworld:1:27504
    }


    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask() {
        String assignee = "提交人assignee";
        List<Task> list = processEngine.getTaskService() // 与正在执行的任务管理相关的Service
                .createTaskQuery() // 创建任务查询对象
                /** 查询条件 */
                .taskAssignee(assignee) // 制定个人任务查询，指定办理人
                //.taskCandidateGroup(candidateUser) // 组任务的办理人查询
                //.processInstanceId(processInstanceId)//流程实例Id
                //.processDefinitionId(processDefinitionId)//流程定义id
                //.executionId(executionId)// 使用执行id查询
                /** 排序 */
                .orderByTaskCreateTime().asc()// 使用创建时间排序
                /** 结果集*/
                //.singleResult()//
                //.count()//
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务Id：" + task.getId());// 32502
                System.out.println("任务名称：" + task.getName());
                System.out.println("任务的创建时间：" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例Id:" + task.getProcessInstanceId());
                System.out.println("执行对象Id:" + task.getExecutionId());
                System.out.println("流程定义Id:" + task.getProcessDefinitionId());
                System.out.println("#####################################################");
            }
        }
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask() {
        String taskId = "27504";
        processEngine.getTaskService()//与正在执行的任务管理相关的Service
                .complete(taskId);
        System.out.println("完成任务：任务Id" + taskId);
    }

    /*查询流程状态，判断流程是否在运行，还是已经结束，表：act_ru_execution*/
    @Test
    public void isProcessEnd() {
        String processInstanceId = "27501";
        ProcessInstance pi = processEngine.getRuntimeService()
                //getRuntimeService()表示正在执行的流程实例和执行对象
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        System.out.println("pi为空则运行结束，pi: " + pi);

    }


    /**
     * 查询历史任务
     */
    @Test
    public void findHistroyTask() {
        String assignee = "提交人assignee";
        List<HistoricTaskInstance> list = processEngine.getHistoryService() // 与历史数据相关的Service
                .createHistoricTaskInstanceQuery()// 历史任务表
                .taskAssignee(assignee) // 设置对应人
                .list();

        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance hti : list) {
                System.out.println(hti.getId() + "" + hti.getName() + "" + hti.getProcessInstanceId() + " " + hti.getStartTime()
                        + "" + hti.getEndTime() + "" + hti.getDurationInMillis());
                System.out.println("#################################################");
            }
        }
    }

    /**
     * 查询历史流程实例 ，act_hi_procinst
     */
    @Test
    public void finHistroyProcessInstance() {
        String processInstanceId = "32501";
        HistoricProcessInstance hpi = processEngine.getHistoryService() //  与历史数据相关的Service
                .createHistoricProcessInstanceQuery() // 创建流程实例查询表
                .processInstanceId(processInstanceId)
                .singleResult();

        System.out.println(hpi.getId() + "" + hpi.getProcessDefinitionId() + "" + hpi.getStartTime() + "" + hpi.getEndTime()
                + "" + hpi.getDurationInMillis());

    }

}
















