package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

import java.util.List;

public class F_HistoryQuery {

    // 自动读取classPath里面的activiti.cfg.xml文件直接加载对应的资源
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); // 所有对象都要使用的

    /**
     * 查询历史流程实例
     */
    @Test
    public void finHistroyProcessInstance() {
        String processInstanceId = "30001";
        HistoricProcessInstance hpi = processEngine.getHistoryService() //  与历史数据相关的Service
                .createHistoricProcessInstanceQuery() // 创建流程实例查询表
                .processInstanceId(processInstanceId)// 你想怎么查，就这么查询
                .singleResult();

        System.out.println(hpi.getId() + "" + hpi.getProcessDefinitionId() + "" + hpi.getStartTime() + "" + hpi.getEndTime()
                + "" + hpi.getDurationInMillis());
    }

    /**
     * 查询历史活动
     */
    @Test
    public void finHistroyActiviti() {
        String processInstanceId = "30001";

        List<HistoricActivityInstance> list = processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();

        if (list != null && list.size() > 0) {
            for (HistoricActivityInstance hai : list) {
                System.out.println(hai.getId() + "    " + hai.getProcessInstanceId() + "    " + hai.getActivityType() + "    "
                        + hai.getStartTime() + "    " + hai.getEndTime());

            }
        }
    }

    /**
     * 查询历史任务
     */
    @Test
    public void findHistroyTask() {

        String processInstanceId = "30001";
        List<HistoricTaskInstance> list = processEngine.getHistoryService() // 与历史数据相关的Service
                .createHistoricTaskInstanceQuery()// 历史任务表
                .processInstanceId(processInstanceId)
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
     * 查询流程变量历史表
     */
    @Test
    public void findHistoryProcessVariables() {

        String processInstanceId = "57501";
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list(); // 针对流程id、任务id等

        if (list != null && list.size() > 0) {
            for (HistoricVariableInstance hvi : list) {
                System.out.println(hvi.getId() + ";" + hvi.getProcessInstanceId() + ";" + hvi.getVariableName() + ";" + hvi.getVariableTypeName() + ";" + hvi.getValue());
                System.out.println("####################################################");

            }
        }
    }

}
