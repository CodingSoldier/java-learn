package self.licw.simpleframework.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import self.licw.simpleframework.util.ClassUtil;

import java.util.Set;

public class ClassUtilTest {
    @Test
    @DisplayName("extractPackageClassTest")
    public void extractPackageClassTest(){
        Set<Class<?>> classSet = ClassUtil.extractPackageClass("self.licw.o2o.entity");
        System.out.println(classSet);
    }
}
