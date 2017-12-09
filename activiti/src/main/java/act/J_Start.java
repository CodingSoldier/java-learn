package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;

public class J_Start {

		private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		@Test
		public void deploymentProcessDefinition_inputStream() {
			// 获取同包下面的文件
			InputStream inputStreamBpmn = this.getClass().getResourceAsStream("J_start.bpmn");
			InputStream inputStreamPng = this.getClass().getResourceAsStream("J_start.png");

			Deployment deployment = processEngine.getRepositoryService()
					.createDeployment()
					.name("开始与结束")
					.addInputStream("J_start.bpmn", inputStreamBpmn) //
					.addInputStream("J_start.png", inputStreamPng) //
					.deploy(); // 完成部署
			System.out.println("部署Id：" + deployment.getId());
			System.out.println("部署名称：" + deployment.getName());
		}

		/** 启动流程实例、判断流程是否结束，判断历史 */
		@Test
		public void startProcessInstance() {
			String processDefinitionKey = "start"; // 使用Key的启动，默认按照对心版本的流程定义启动
			ProcessInstance pi = processEngine.getRuntimeService() // 与正在执行的流程实例和执行对象相关的Service
					.startProcessInstanceByKey(processDefinitionKey); // 使用流程定义的Key启动流程实例，key对应helloworld.bpmn文件中的流程名称

			System.out.println("流程实例Id:" + pi.getId()); // 流程实例Id：102501
			System.out.println("流程定义Id:" + pi.getProcessDefinitionId()); // 流程定义Id：exclusiveGateWay:1:100004
			
			/**判断流程是否结束*/
			ProcessInstance rpi = processEngine.getRuntimeService()
							.createProcessInstanceQuery()
							.processInstanceId(pi.getId())
							.singleResult();
			// 说明流程实例结束
			if (rpi == null) {
				/**查询历史获取流程的相关信息*/
				HistoricProcessInstance hpi = processEngine.getHistoryService()
								.createHistoricProcessInstanceQuery()
								.processInstanceId(pi.getId())
								.singleResult();

				System.out.println(hpi.getId()+"  "+hpi.getStartTime() + "      " + hpi.getEndTime() + "    " + hpi.getDurationInMillis() );
			}
		}


}
