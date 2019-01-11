package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class B_Helloworld {
    /*获取流程引擎，默认获取classpath下的activiti.cfg.xml配置文件。resources = classLoader.getResources("activiti.cfg.xml");*/
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    //流程引擎需要activiti.cfg.xml，部署需要helloworld.bpmn
    @Test
    public void deployment() throws Exception{
        //流程引擎的service是操作数据库的对象，RepositoryService管理流程定义，即bpmn文件。getRepositoryService方法会对数据库进行操作，查看act_re_deployment表
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()  //创建部署对象
                .name("流程引擎部署Deployment的名称aaaaaaaaaa") //给部署取个名字
                .addClasspathResource("diagram/helloworld.bpmn")
                //部署对象需要bpmn配置文件，一次只能加载一个，addClasspathResource()从类路径下加载资源文件，前面不能加 /
                .addClasspathResource("diagram/helloworld.png")  //可PNG图片，可选
                .deploy();
        System.out.println("部署Id：  "+deployment.getId()); // 部署Id
        System.out.println("部署名称：  "+deployment.getName()); // 部署名称

        /*部署Id：  10001
          部署名称：  流程引擎部署Deployment的名称*/
    }

    //启动流程实例getRuntimeService
    @Test
    public void startProcessInstance() throws Exception{
        //流程定义id就是helloworld.bpmn整个文件的定义id，使用key启动默认是按照最新版本的流程定义启动。
        String processDefinitionKey = "helloworld";
        //RuntimeService执行管理，包括启动、推进、删除流程实例等操作，查看表act_re_procdef
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey);

        System.out.println("流程实例Id: "+processInstance.getId()); // 流程实例Id
        System.out.println("流程定义Id: "+processInstance.getProcessDefinitionId()); // 流程定义Id
        /*流程实例Id: 5001
          流程定义Id: helloworld:1:4   */
    }

    /*查询任务getTaskService，任务对应着bpmn文件中间绿色方块的userTask，通过assignee查询，上边方法已经启动一个流程实例，所以第一个绿色方块 “提交申请” 就变成了一个正在运行的任务。表act_ru_task会有一条记录，表示正在运行的任务
    * */
    @Test
    public void findTaskByAssignee() throws  Exception{
        //helloworld.bpmn中第一个任务提交申请的Assignee已经写死为提交人assignee，也可在act_ru_task中查询到所有正在运行的task
        String assignee = "提交人assignee";
        List<Task> taskList = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        if(taskList != null && taskList.size() > 0){
            for (Task task : taskList){
                System.out.println("任务Id：  "+task.getId());
                System.out.println("任务名称：  "+task.getName());  //bpmn文件中UserTask的name
                System.out.println("任务定义id:  "+task.getTaskDefinitionKey());  //bpmn文件中UserTask的id
                System.out.println("任务的创建时间：  "+task.getCreateTime());
                System.out.println("任务的办理人:  "+task.getAssignee());  //bpmn文件中UserTask的Assignee
                System.out.println("流程实例Id:  "+task.getProcessInstanceId());
                System.out.println("执行对象Id:  "+task.getExecutionId());
                System.out.println("流程定义Id:  "+task.getProcessDefinitionId());  //helloworld:1:4 和 processInstance.getProcessDefinitionId()是一样的
                System.out.println("#####################################################");
                //bpmn文件UserTask
            }
        }
    }

    /*通过taskId完成任务，taskId就是act_ru_task表中的主键ID，*/
    @Test
    public void completeTaskByTaskId() throws Exception{
        String taskId = "2504";
        processEngine.getTaskService().complete(taskId);
        //上边代码执行完，结果是：act_ru_task中ID= 2504的记录被删除，同时新增一条记录，这记录就是部门经理审批这环节有了一个正在运行的任务，
        System.out.println("完成任务，任务Id：  "+taskId);
    }























}
