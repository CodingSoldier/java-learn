package self.licw.simpleframework.aop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import self.licw.simpleframework.aop.T.t1;
import self.licw.simpleframework.aop.T.t2;
import self.licw.simpleframework.aop.T.t3;
import self.licw.simpleframework.aop.aspect.AspectInfo;

import java.util.ArrayList;
import java.util.List;

public class AspectListExecutorTest {
    @DisplayName("Aspect排序验证")
    @Test
    public void sortTest(){
        List<AspectInfo> aspectInfos = new ArrayList<>();
        aspectInfos.add(new AspectInfo(2,new t1(),null));
        aspectInfos.add(new AspectInfo(5,new t2(),null));
        aspectInfos.add(new AspectInfo(1,new t3(),null));
        AspectListExecutor aspectListExecutor = new AspectListExecutor(AspectListExecutorTest.class,aspectInfos);
        List<AspectInfo> sort = aspectListExecutor.getAspectList();
        for (AspectInfo aspectInfo:sort){
            System.out.println(aspectInfo.getAspect().getClass().getName());
        }
    }
}
