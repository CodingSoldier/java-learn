package ssm.projectnote.mybatis;

import org.junit.Test;
import ssm.projectnote.mybatis.mapper.HrDepartmentMapper;
import ssm.projectnote.mybatis.model.HrDepartment;
import ssm.utils.BaseTest;

import javax.annotation.Resource;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/11/25
 */
public class T extends BaseTest{
    @Resource
    HrDepartmentMapper mapper;
    @Test
    public void t1() throws Exception{
    /*type在数据库中是枚举类型，生成的pojo是String类型，数据库中列的值必须是特定字段，可以使用枚举类型*/
        HrDepartment hr = new HrDepartment();
        //hr.setType("GOOD1");  //插入失败，不是枚举值
        //hr.setType("1");  //坑爹，可根据枚举下标插入值，定义
        hr.setType("GOOD");  //插入成功
        hr.setName("名字111");
        mapper.insert(hr);
    }
}
