package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.List;


public class E_ProcessVariables {
    // 自动读取classPath里面的activiti.cfg.xml文件直接加载对应的资源
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); // 所有对象都要使用的

    /**
     * 部署流程定义zip文件
     */
    @Test
    public void deploymentProcessDefinition_inputStream() {
        InputStream inputStreambpmn = this.getClass().getResourceAsStream("/diagram/processVariables.bpmn");
        InputStream inputStreampng = this.getClass().getResourceAsStream("/diagram/processVariables.png");
        Deployment deployment = processEngine.getRepositoryService() // 与流程定义和部署对象相关的Service
                .createDeployment() // 创建一个部署对象
                .name("processVariables部署000") // 添加部署的名称
                .addInputStream("processVariables.bpmn", inputStreambpmn)// 使用资源文件名称（要求与资源文件的名称要一致），和输入流完成部署
                .addInputStream("processVariables.png", inputStreampng)// 使用资源文件名称（要求与资源文件的名称要一致），和输入流完成部署
                .deploy(); // 完成部署
        System.out.println("部署Id：" + deployment.getId()); // 部署Id:55001
        System.out.println("部署名称：" + deployment.getName()); // 部署名称:流程定义输入流

    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        // 使用Key的启动，默认按照对心版本的流程定义启动， act_re_procdef 表中查找
        String processDefinitionKey = "processVariablesKey";
        ProcessInstance pi = processEngine.getRuntimeService() // 与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey); // 使用流程定义的Key启动流程实例，key对应helloworld.bpmn文件中的流程名称
        System.out.println("流程实例Id" + pi.getId()); // 流程实例Id：57501
        System.out.println("流程定义Id" + pi.getProcessDefinitionId()); // 流程定义Id：psocessVariables:1:55004
    }

    //设置流程变量
    @Test
    public void setVariables() {
        TaskService taskService = processEngine.getTaskService();
        String taskId = "52502";  // act_ru_task 表查看

        //表 act_ru_variable
        taskService.setVariableLocal(taskId, "请假天数", 1);
        //表示与任务ID绑定，当前任务完成之后，此变量消失，再通过taskService获取变量只能获取到"请假日期"、"请假原因"
        taskService.setVariable(taskId, "请假日期", new Date());
        taskService.setVariable(taskId, "请假原因", "回家吃饭");
    }

    //获取流程变量
    @Test
    public void getVariables() {
        TaskService taskService = processEngine.getTaskService();
        String taskId = "40004";
        Object dayNum = taskService.getVariable(taskId, "请假天数");
        Object date = (Date) taskService.getVariable(taskId, "请假日期");
        String reason = (String) taskService.getVariable(taskId, "请假原因");
        System.out.println("请假天数： " + dayNum);
        System.out.println("请假日期： " + date);
        System.out.println("请假原因： " + reason);
    }

    @Test
    public void setVeriablesByBean() {
        TaskService taskService = processEngine.getTaskService();
        String taskId = "32504";
        E_Person p = new E_Person();
        p.setId(20);
        p.setName("翠花");
        taskService.setVariable(taskId, "人员信息", p);
        //变量值是一个bean
        taskService.setVariable(taskId, "人员信息（变量2）", p);
    }

    @Test
    public void getVeriablesBeanMsg() {
        TaskService taskService = processEngine.getTaskService();
        String taskId = "32504";
        System.out.println(taskService.getVariable(taskId, "人员信息").toString());
        //得到的变量值是一个bean
        System.out.println(taskService.getVariable(taskId, "人员信息（变量2）").toString());
    }

    @Test
    public void completeTaskByTaskId() throws Exception {
        String taskId = "50004";
        processEngine.getTaskService().complete(taskId);
        //上边代码执行完，结果是：act_ru_task中ID= 2504的记录被删除，同时新增一条记录，这记录就是部门经理审批这环节有了一个正在运行的任务，
        System.out.println("完成任务，任务Id：  " + taskId);
    }

    /**
     * 模拟设置和获取流程变量的场景
     */
    @Test
    public void setAndGetVariables() {
        /**与流程实例，执行对象*/
        RuntimeService runtimeService = processEngine.getRuntimeService();
        /**与任务相关（正在执行）*/
        TaskService taskService = processEngine.getTaskService();

        /** 设置流程变量 */
        //runtimeService.setVariable(executionId, variableName, value);// 表示使用执行Id，流程变量名称，设置流程变量的值（一次只能设置一个值）
        //runtimeService.setVariables(executionId, map);// 表示使用执行id以及流程变量map ，map的key就是流程变量的名称、map集合的value就是流程变量的名称。一次多个

        //taskService.setVariable(raskId, variableName, value);// 表示使用任务Id，流程变量名称，设置流程变量的值（一次只能设置一个值）
        //taskService.setVariables(taskId, map);// 表示使用任务id以及流程变量map ，map的key就是流程变量的名称、map集合的value就是流程变量的名称。一次多个

        //runtimeService.startProcessInstanceById(processInstanceKey, variableMap);// 启动流程实例的时候设置流程变量
        //taskService.complete(taskId, variableMap); // 完成任务的时候进行设置流程变量

        /** 获取流程变量 */
        //runtimeService.getVariable(executionId, variableName);// 使用执行对象Id和流程变量名称获取流程变量的值
        //runtimeService.getVariables(executionId); //使用执行对象Id和流程变量名称获取map集合
        //runtimeService.getVariables(executionId, variableNames); // 使用执行对象id,获取流程变量的值，通过设置流程变量的名称存放到集合里面，然后获取对应的返回值的map

        //taskService.getVariable(taskId, variableName);// 使用任务对象Id和流程变量名称获取流程变量的值
        //taskService.getVariables(taskId); //使用任务对象Id和流程变量名称获取map集合
        //taskService.getVariables(taskId, variableNames); // 使用任务对象id,获取流程变量的值，通过设置流程变量的名称存放到集合里面，然后获取对应的返回值的map
    }

    @Test
    public void findHistoryProcessVariables() {
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .variableName("请假天数")
                .list();
        if (list != null) {
            System.out.println(list.toString());
        }
    }

    @Test
    public void t() {

    }
}


















