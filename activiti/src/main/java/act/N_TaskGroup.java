package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class N_TaskGroup {

		private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		@Test
		public void deploymentProcessDefinition_inputStream() {
			InputStream inputStreamBpmn = this.getClass().getResourceAsStream("N_TaskGroup.bpmn");
			InputStream inputStreamPng = this.getClass().getResourceAsStream("N_TaskGroup.png");
			Deployment deployment = processEngine.getRepositoryService()
					.createDeployment()
					.name("组业务")
					.addInputStream("N_TaskGroup.bpmn", inputStreamBpmn)
					.addInputStream("N_TaskGroup.png", inputStreamPng)
					.deploy();
			System.out.println("部署Id：" + deployment.getId());
			System.out.println("部署名称：" + deployment.getName());
		}


	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "N_taskGroupKey";
		//设置组任务成员
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userNameList", "成员1,成员2,成员3");

		ProcessInstance pi = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey,variables);  //流程启动时添加组成员

		/** 组任务查询 */
		List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
				.taskCandidateUser("成员1")   //组任务候选者查询
				//.taskAssignee("人1")   //组任务没有assignee
				.list();
        System.out.println(tasks.toString());
    }

	/**查询正在执行的任务办理人表*/
	@Test
	public void findRunPersonTask(){
		//任务ID，表act_ru_identitylink的TASK_ID，候选人有task_id
		String taskId = "240004";
		List<IdentityLink> list = processEngine.getTaskService()
				.getIdentityLinksForTask(taskId);
		if(list!=null && list.size()>0){
			for(IdentityLink identityLink:list){
				System.out.println(identityLink.getTaskId()+"   "
						+identityLink.getType()+"   "
						+identityLink.getProcessInstanceId()+"   "
						+identityLink.getUserId());
			}
		}
	}
	/**查询历史任务的办理人表*/
	@Test
	public void findHistoryPersonTask(){
		//流程实例ID  act_ru_identitylink的流程实例ID，参与者有流程实例id
		String processInstanceId = "227501";
		List<HistoricIdentityLink> list = processEngine.getHistoryService()//
				.getHistoricIdentityLinksForProcessInstance(processInstanceId);
		if(list!=null && list.size()>0){
			for(HistoricIdentityLink identityLink:list){
				System.out.println(identityLink.getTaskId()+"   "
						+identityLink.getType()+"   "
						+identityLink.getProcessInstanceId()+"   "
						+identityLink.getUserId());
			}
		}
	}

	/**拾取任务，将组任务分配给个人，指定任务的办理人。组任务一定要指定办理人，不然没有assignee去完成这个任务*/
	@Test
	public void claim(){
		String taskId = "240004";
		//分配的个人任务（可以是组任务中的成员，也可以是非组任务的成员）
		String userId = "非组成员111";
		processEngine.getTaskService()
				.claim(taskId, userId);
		//使用claim方法指定任务的assignee
		//在act_ru_task和act_ru_identitylink中可以看到"非组成员111"有了一个参与者participant的任务
	}

	/**将个人任务回退到组任务，前提，之前一定是个组任务*/
	@Test
	public void setAssigee(){
		String taskId = "240004";
		processEngine.getTaskService()//
				.setAssignee(taskId, null);  //将任务的assignee设置为null即可
	}

	/**向组任务中添加成员*/
	@Test
	public void addGroupUser(){
		//任务ID
		String taskId = "6204";
		//成员办理人
		String userId = "大H";
		processEngine.getTaskService()//
				.addCandidateUser(taskId, userId);
	}

	/**从组任务中删除成员*/
	@Test
	public void deleteGroupUser(){
		//任务ID
		String taskId = "6204";
		//成员办理人
		String userId = "小B";
		processEngine.getTaskService()//
				.deleteCandidateUser(taskId, userId);
	}
}
