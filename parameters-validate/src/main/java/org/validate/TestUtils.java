package validate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-context.xml"})
public class TestUtils {
    @Autowired
    ValidateExecute validateExecute;

    @Test
    public void t() throws Exception{
       validateExecute.test1();
    }
}
