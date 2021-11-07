package act;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class M_TaskListenerImpl implements TaskListener {
    /**
     * String EVENTNAME_CREATE = "create";
     * String EVENTNAME_ASSIGNMENT = "assignment";
     * String EVENTNAME_COMPLETE = "complete";
     * String EVENTNAME_DELETE = "delete";
     * String EVENTNAME_ALL_EVENTS = "all";
     * bpmn文件中的listener的时间可以填写上面的值
     */
    private static final long serialVersionUID = -7596650939441567109L;

    public void notify(DelegateTask delegateTask) {
        // 通过调用其他方法返回assignee的值，再设置
        delegateTask.setAssignee("调用其他方法返回assignee的值");


        //组任务设置组成员，N_TaskGroup中基于监听的组成员赋值
        //delegateTask.addCandidateUser("郭靖");
        //delegateTask.addCandidateUser("黄蓉");

    }

}
