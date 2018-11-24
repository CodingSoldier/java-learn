package com.demo.oauth2.oauthuser.mapper;

import com.demo.oauth2.oauthuser.model.OauthUser;
import com.demo.oauth2.oauthuser.model.OauthUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OauthUserMapper {
    long countByExample(OauthUserExample example);

    int deleteByExample(OauthUserExample example);

    int deleteByPrimaryKey(String id);

    int insert(OauthUser record);

    int insertSelective(OauthUser record);

    List<OauthUser> selectByExampleWithBLOBs(OauthUserExample example);

    List<OauthUser> selectByExample(OauthUserExample example);

    OauthUser selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OauthUser record, @Param("example") OauthUserExample example);

    int updateByExampleWithBLOBs(@Param("record") OauthUser record, @Param("example") OauthUserExample example);

    int updateByExample(@Param("record") OauthUser record, @Param("example") OauthUserExample example);

    int updateByPrimaryKeySelective(OauthUser record);

    int updateByPrimaryKeyWithBLOBs(OauthUser record);

    int updateByPrimaryKey(OauthUser record);
}