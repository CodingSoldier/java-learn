package com.demo.old.sysuser.service;

import com.demo.old.sysuser.mapper.SysUserMapper;
import com.demo.old.sysuser.model.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

@Service
public class SysUserServiceImpl implements SysUserService {

    Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    SysUserMapper sysUserMapper;

    @Transactional
    @Override
    public int updateByPrimaryKeySelective(SysUser record) throws Exception{
        sysUserMapper.updateByPrimaryKeySelective(record);
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("application.yml");
        InputStreamReader isr = new InputStreamReader(new BufferedInputStream(is));
        OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream("E:\\my-code\\java-learn\\spring-boot-learn\\src\\main\\resources\\a.yml")));
        char[] chars = new char[1024*8];
        int length = 0;
        while ((length = isr.read(chars)) != -1){
            osw.write(chars, 0, length);
        }
        osw.flush();
        isr.close();
        is.close();
        osw.close();

        //t();

        //try {
        //    throw new RuntimeException("异常");
        //}catch (Exception e){
        //    logger.error("异常", e);
        //}
        return 0;
    }

    public void t() throws Exception{
        throw new IOException("askdfja");
    }

}
