package ssm.module01.test;

import org.junit.Test;
import ssm.utils.BaseTest;
import ssm.module01.mapper.UserCustomMapper;
import ssm.module01.pojo.UserCustom;
import ssm.module01.service.ServiceHtmlToSql;

import javax.annotation.Resource;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/11/19
 */
public class TestHtmlToSql extends BaseTest {
    @Resource
    private ServiceHtmlToSql serviceHtmlToSql;
    @Resource
    private UserCustomMapper userMapperCustom;

    @Test
    public void test1() throws Exception{
        serviceHtmlToSql.testFindUser(10);
    }

    /*多表查询，User类中的字段不够用了，添加拓展类UserCustom，拓展mapper：UserCustomMapper.java、UserCustomMapper.xml。UserCustomMapper.xml中查询结果是resultType=UserCustom，查询结果的列可以比UserCustom中的属性少，UserCustom.java中有属性note2，查询结果可以没有note2列*/
    @Test
    public void custom() throws Exception{
        UserCustom userCustom = userMapperCustom.findUserNoteById(10);
        System.out.println(userCustom.toString());
    }
}
