package self.licw.simpleframework.aop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import self.licw.o2o.controller.superadmin.HeadLineOperationController;
import self.licw.simpleframework.core.BeanContainer;
import self.licw.simpleframework.inject.DependencyInjector;

public class AspectWeaverTest {
    @Test
    @DisplayName("测试doaop")
    public void doAopTest(){
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("self.licw.o2o");
        new AspectWeaver().doAop();
        new DependencyInjector().doIoc();
        HeadLineOperationController headLineOperationController = (HeadLineOperationController)beanContainer.getBean(HeadLineOperationController.class);
        headLineOperationController.addHeadLine(null,null);
    }
}
