package ssm.module01.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssm.module01.mapper.UserMapper;
import ssm.module01.pojo.User;

import javax.annotation.Resource;

@Service
public class ServiceTransactional {
    @Resource
    private UserMapper mapper;

    @Transactional
    public void testT() throws Exception{

        try{
            User user = new User();
            user.setUsername("插入名字");
            int a = 1/0;
            /*执行insert之前发生异常，不会执行insert语句，插入失效*/
            int num = mapper.insert(user);
        } catch (Exception e){
             /*异常被捕获，后面的代码继续执行，相当于testT方法没有抛出异常，不被spring捕获。导致的结果就是：mapper.insert(user)没有执行，但是后面的mapper.updateByPrimaryKey(updateUser)执行了*/
            e.printStackTrace();
        }

        User updateUser = new User();
        updateUser.setId(1);
        updateUser.setUsername("444444444改变名字");
        mapper.updateByPrimaryKey(updateUser);
    }

    @Transactional
    public void insert01() throws Exception{
        User user = new User();
        user.setUsername("666插入名字");
        int num = mapper.insert(user);
    }
    @Transactional
    public void update01() throws Exception{
        User updateUser = new User();
        updateUser.setId(1);
        updateUser.setUsername("666改变名字");
        mapper.updateByPrimaryKey(updateUser);
        int a = 1/0;
        /*insert01、update01合并在doubleMethod中去执行，insert01()已经执行完、updateByPrimaryKey()也执行成功，但此处发生异常，整个doubleMethod回滚。插入更新都没有成功
        所谓事务的传播行为是指，如果在开始当前事务之前，一个事务上下文已经存在，此时有若干选项可以指定一个事务性方法的执行行为。在TransactionDefinition定义中包括了如下几个表示传播行为的常量：
TransactionDefinition.PROPAGATION_REQUIRED：默认值，如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
         */
    }
    @Transactional
    public void doubleMethod01() throws Exception{
        insert01();
        update01();
    }

    @Transactional
    public void insert02() throws Exception{
        User user = new User();
        user.setUsername("222222222插入名字");
        //user.setSex("错误的性别值");
        try {
            /*本SQL插入错误数据，此SQL执行失败，但由于捕获了mapper.insert(user)的异常，所以不会影响到mapper.updateByPrimaryKey(updateUser)的执行。结果是插入失败，后面的更新成功
            */
            int num = mapper.insert(user);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("出错");
        }

    }
    @Transactional
    public void update02() throws Exception{
        User updateUser = new User();
        updateUser.setId(1);
        updateUser.setUsername("2222222改变名字");
        updateUser.setSex("错误的性别值");
        try {
           /*本SQL使用错误数据更新，此SQL执行失败，但由于捕获了mapper.updateByPrimaryKey(updateUser)的异常，所以不会影响到之前的mapper.insert(user)。结果是insert02中的插入成功，更新失败
            */
            mapper.updateByPrimaryKey(updateUser);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("出错");
        }
    }
    @Transactional
    public void doubleMethod02() throws Exception{
        insert02();
        update02();
    }

    @Transactional
    public void io() throws Exception{
        User updateUser = new User();
        updateUser.setId(1);
        updateUser.setUsername("33333333333");
        updateUser.setSex("w22222222222");
        mapper.updateByPrimaryKey(updateUser);
    }

}
