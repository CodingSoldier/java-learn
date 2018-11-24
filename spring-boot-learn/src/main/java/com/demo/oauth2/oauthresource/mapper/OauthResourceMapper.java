package com.demo.oauth2.oauthresource.mapper;

import com.demo.oauth2.oauthresource.model.OauthResource;
import com.demo.oauth2.oauthresource.model.OauthResourceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OauthResourceMapper {
    long countByExample(OauthResourceExample example);

    int deleteByExample(OauthResourceExample example);

    int deleteByPrimaryKey(String id);

    int insert(OauthResource record);

    int insertSelective(OauthResource record);

    List<OauthResource> selectByExample(OauthResourceExample example);

    OauthResource selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OauthResource record, @Param("example") OauthResourceExample example);

    int updateByExample(@Param("record") OauthResource record, @Param("example") OauthResourceExample example);

    int updateByPrimaryKeySelective(OauthResource record);

    int updateByPrimaryKey(OauthResource record);
}