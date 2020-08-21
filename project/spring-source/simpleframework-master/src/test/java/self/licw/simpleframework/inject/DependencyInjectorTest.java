package self.licw.simpleframework.inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import self.licw.o2o.controller.frontend.MainPageController;
import self.licw.o2o.service.combine.impl.HeadLineShopCategoryCombineServiceImpl;
import self.licw.o2o.service.combine.impl.HeadLineShopCategoryCombineServiceImpl2;
import self.licw.simpleframework.core.BeanContainer;

public class DependencyInjectorTest {
    @Test
    @DisplayName("测试依赖注入doIOC")
    public void doIocTest(){
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("self.licw.o2o");
        Assertions.assertEquals(true,beanContainer.isLoaded());
        MainPageController controller = (MainPageController)beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(null,controller.getHeadLineShopCategoryCombineService());
        //执行doIoc方法执行依赖注入
        new DependencyInjector().doIoc();
        Assertions.assertNotEquals(null,controller.getHeadLineShopCategoryCombineService());
        Assertions.assertEquals(true,controller.getHeadLineShopCategoryCombineService() instanceof HeadLineShopCategoryCombineServiceImpl);
        Assertions.assertEquals(false,controller.getHeadLineShopCategoryCombineService() instanceof HeadLineShopCategoryCombineServiceImpl2);
    }
}
