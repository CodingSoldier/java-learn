package act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;


public class C_ProcessDefinition {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /*将helloworld.bpmn、helloworld.png打包成zip文件，使用zip文件部署流程，
      部署对象表act_re_deployment、流程定义表act_re_procdef多了一条记录
      资源文件表多了两条记录，一条bpmn、一条png
      主键生成策略表act_ge_property的第一行的next.dbid会改变，变为下一个deployment对象的id
    */
    @Test
    public void deploymentProcessDefinitionZip() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("diagram/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("使用zip文件流程定义")
                .addZipInputStream(zipInputStream) // 制定zip格式文件完成部署
                .deploy();
        System.out.println("部署Id：  " + deployment.getId());
        System.out.println("部署名称：  " + deployment.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void findProcessDefinition() {
        List<ProcessDefinition> pdList = processEngine.getRepositoryService() // 与流程定义和部署对象相关的Service
                .createProcessDefinitionQuery() // 创建不同的查询不同的表、创建一个查询就能够操作这张表
                /** 制定查询条件，where条件*/
                //.deploymentId(deploymentId)// 使用部署对象Id查询
                //.processDefinitionKey("helloworld")
                // 使用流程定义的Key进行查询，processDefinitionKey，对应helloworld.bpmn文件中的id属性值，act_re_procdef表中的名为KEY的列
                //.processDefinitionId("helloworld:1:4")
                // 使用流程定义Id查询，processDefinitionId为processDefinitionKey+版本+随机数
                //.processDefinitionNameLike(processDefinitionNameLike)//根据名称模糊查询
                /** 排序 */
                //.orderByProcessDefinitionVersion().asc()//按照版本升序排列
                //.orderByProcessDefinitionName().desc()//按照流程定义的名称将序排列
                /** 返回的结果集 */
                //.list();// 返回一个集合列表，封装流程定义
                //.singleResult();//返回唯一的结果集
                //.count();// 返回结果集数量
                //.listPage(paramInt1, paramInt2);// 分页查询
                .orderByProcessDefinitionVersion().asc()
                .list();

        if (pdList != null && pdList.size() > 0) {
            for (ProcessDefinition pd : pdList) {
                System.out.println("流程定义Id:" + pd.getId()); // 流程定义的key+版本+随机生成树
                System.out.println("流程定义名称：" + pd.getName()); // 对应helloworld.bpmn文件中的name属性值
                System.out.println("流程定义的Key：" + pd.getKey()); // 对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义的版本：" + pd.getVersion()); // 对应流程定义的key值相同下，版本升级，默认1
                System.out.println("资源名称bpmn文件：" + pd.getResourceName()); //
                System.out.println("资源名称png文件：" + pd.getDiagramResourceName());
                System.out.println("部署对象Id" + pd.getDeploymentId());
                System.out.println("####################################################");
            }
        }
    }

    @Test
    public void deleteProcessDefinitionByDeploymentId() {
        //deploymentId: act_re_deployment中的ID，act_re_procdef中的DEPLOYMENT_ID
        String deploymentId = "17501";
        //只能删除没有启动流程的部署，否则抛异常
        //processEngine.getRepositoryService().deleteDeployment(deploymentId);
        //System.out.println("删除成功");

        //能删流程已经启动的部署（用得较多），第二个参数默认是false，改为true
        processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
        System.out.println("删除成功");
    }

    /**
     * 查看流程图，即png图片
     */
    @Test
    public void viewPngByDeploymentId() throws IOException {
        String deploymentId = "1";
        // 获取图片资源
        List<String> list = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        // 资源文件在act_ge_bytearray表中，找到bpmn和png两个文件，取png图片
        String resourceName = "";
        if (list != null && list.size() > 0) {
            for (String name : list) {
                if (name.indexOf(".png") >= 0) {
                    resourceName = name;
                }
            }
        }
        // 通过deploymentId和资源名称（不带文件类型后缀）获取图片的输入流
        InputStream in = processEngine.getRepositoryService()
                .getResourceAsStream(deploymentId, resourceName);

        // 将图片生成到D盘的目录下
        File outFile = new File("classpath:imgfile/" + resourceName);
        System.out.println("in:" + in + "to:" + outFile);

    }

    /**
     * 附加功能，查询最新版本流程定义
     */
    @Test
    public void findLastVersionProcessDefinition() {
        List<ProcessDefinition> list = processEngine.getRepositoryService() //
                .createProcessDefinitionQuery() //
                .orderByProcessDefinitionVersion().asc()// 使用流程定义的版本号做升序排列
                .list();

        /**使用map定义的东西进行转换，获得最后一个版本的值*/

        Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                map.put(pd.getKey(), pd);
            }
        }

        List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());

        if (pdList != null && pdList.size() > 0) {
            for (ProcessDefinition pd : pdList) {
                System.out.println("流程定义Id:   " + pd.getId()); // 流程定义的key+版本+随机生成树
                System.out.println("流程定义名称：   " + pd.getName()); // 对应helloworld.bpmn文件中的name属性值
                System.out.println("流程定义的Key：   " + pd.getKey()); // 对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义的版本：   " + pd.getVersion()); // 对应流程定义的key值相同下，版本升级，默认1
                System.out.println("资源名称bpmn文件：   " + pd.getResourceName()); //
                System.out.println("资源名称png文件：   " + pd.getDiagramResourceName());
                System.out.println("部署对象Id   " + pd.getDeploymentId());
                System.out.println("####################################################");
            }
        }
    }


    /**
     * 附加功能：删除流程定义（删除key相同的所有不同版本的流程定义）
     */
    @Test
    public void deleteProcessDefinitionByKey() {
        String processDefinitionKey = "helloworld";
        // 先试用流程定义的Key查询定义，查询出所有版本
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey) // 流程定义id
                .list();

        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                String deploymentId = pd.getDeploymentId();
                processEngine.getRepositoryService().deleteDeployment(deploymentId, Boolean.TRUE);
            }
        }

        System.out.println("通过key进行删除");
    }

}




















