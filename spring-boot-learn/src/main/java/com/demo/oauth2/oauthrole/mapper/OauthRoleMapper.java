package com.demo.oauth2.oauthrole.mapper;

import com.demo.oauth2.oauthrole.model.OauthRole;
import com.demo.oauth2.oauthrole.model.OauthRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OauthRoleMapper {
    long countByExample(OauthRoleExample example);

    int deleteByExample(OauthRoleExample example);

    int deleteByPrimaryKey(String id);

    int insert(OauthRole record);

    int insertSelective(OauthRole record);

    List<OauthRole> selectByExampleWithBLOBs(OauthRoleExample example);

    List<OauthRole> selectByExample(OauthRoleExample example);

    OauthRole selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OauthRole record, @Param("example") OauthRoleExample example);

    int updateByExampleWithBLOBs(@Param("record") OauthRole record, @Param("example") OauthRoleExample example);

    int updateByExample(@Param("record") OauthRole record, @Param("example") OauthRoleExample example);

    int updateByPrimaryKeySelective(OauthRole record);

    int updateByPrimaryKeyWithBLOBs(OauthRole record);

    int updateByPrimaryKey(OauthRole record);
}