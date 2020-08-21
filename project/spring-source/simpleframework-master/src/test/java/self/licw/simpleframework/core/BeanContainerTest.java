package self.licw.simpleframework.core;

import org.junit.jupiter.api.*;
import self.licw.o2o.controller.frontend.MainPageController;
import self.licw.o2o.entity.dto.MainPageInfoDto;
import self.licw.o2o.service.solo.HeadLineService;
import self.licw.o2o.service.solo.impl.HeadLineServiceImpl;
import self.licw.simpleframework.core.annotation.Controller;

//根据注解的顺序执行test方法
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanContainerTest {
    private static BeanContainer beanContainer;
    //该注解类的作用是，在执行所有util测试类时，最开始先执行一次这个
    @BeforeAll
    static void init(){
        beanContainer = BeanContainer.getInstance();
    }

    @DisplayName("load beanContainer")
    @Test
    @Order(1) //第一个执行的单元test方法
    public void loanBeansTest(){
        Assertions.assertEquals(false,beanContainer.isLoaded());
        beanContainer.loadBeans("self.licw.o2o");
        Assertions.assertEquals(6,beanContainer.size());
        Assertions.assertEquals(true,beanContainer.isLoaded());
    }

    @DisplayName("根据类对象获取其实例")
    @Test
    @Order(2)
    public void getBeanTest(){
        MainPageController controller = (MainPageController) beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(true,controller instanceof MainPageController);
        MainPageInfoDto mainPageInfoDto = (MainPageInfoDto) beanContainer.getBean(MainPageInfoDto.class);
        Assertions.assertEquals(null,mainPageInfoDto);
    }

    @DisplayName("根据注解获取对应的实例")
    @Order(3)
    @Test
    public void getClasssByAnnotationTest(){
        Assertions.assertEquals(3,beanContainer.getClassesByAnnotation(Controller.class).size());
    }

    @DisplayName("根据接口或父类获取对应的实例")
    @Order(4)
    @Test
    public void getClasssBySuperTest(){
        Assertions.assertEquals(true,beanContainer.getClassesBySuper(HeadLineService.class).contains(HeadLineServiceImpl.class));
    }


}
