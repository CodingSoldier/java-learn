package com.demo.oauth2;

import com.demo.oauth2.oauthresource.mapper.OauthResourceMapper;
import com.demo.oauth2.oauthresource.model.OauthResource;
import com.demo.oauth2.oauthresource.model.OauthResourceExample;
import com.demo.oauth2.oauthrole.mapper.OauthRoleMapper;
import com.demo.oauth2.oauthrole.model.OauthRole;
import com.demo.oauth2.oauthrole.model.OauthRoleExample;
import com.demo.oauth2.oauthuser.mapper.OauthUserMapper;
import com.demo.oauth2.oauthuser.model.OauthUser;
import com.demo.oauth2.oauthuser.model.OauthUserExample;
import com.demo.old.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/oauth2/client")
public class ClientPattern {

    public static final String OAUTH2_CLIENT_TOKEN = "oauth2:client:token:";

    @Autowired
    RedisService redisService;

    @Autowired
    OauthUserMapper oauthUserMapper;
    @Autowired
    OauthRoleMapper oauthRoleMapper;
    @Autowired
    OauthResourceMapper oauthResourceMapper;

    @GetMapping("/get/token")
    public Map<String, String> getToken(@RequestParam String client_id, @RequestParam String client_secret) throws Exception{
        if (StringUtils.isBlank(client_id)){
            throw new Exception("client_id空");
        }
        if (StringUtils.isBlank(client_secret)){
            throw new Exception("client_secret空");
        }
        String access_token = createAccessInfo(client_id, client_secret);
        Map<String, String> r = new HashMap<>();
        r.put("access_token", access_token);
        return r;
    }

    //创建token、访问规则
    private String createAccessInfo(String userId, String password) throws Exception{

        OauthUser user = getOauthUser(userId, password);
        List<OauthRole> roleList = getOauthRoleList(user);
        List<String> urlRegexList = getUrlRegexList(roleList);
        if (urlRegexList.size() == 0){
            throw new Exception("账户无url资源权限");
        }

        String accessToken = UUID.randomUUID().toString();
        String tokenKey = OAUTH2_CLIENT_TOKEN + accessToken;
        redisService.putSet(tokenKey, urlRegexList);  //设置token缓存
        return accessToken;
    }

    //用户
    private OauthUser getOauthUser(String userId, String password) throws Exception{
        OauthUserExample userExample = new OauthUserExample();
        OauthUserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andIdEqualTo(userId);
        userCriteria.andPasswordEqualTo(password);
        List<OauthUser> userList = oauthUserMapper.selectByExampleWithBLOBs(userExample);
        if (userList.size() != 1){
            throw new Exception("账号密码错误");
        }
        return userList.get(0);
    }

    //角色列表
    private List<OauthRole> getOauthRoleList(OauthUser oauthUser) throws Exception{
        String roleStr = oauthUser.getRoleList();
        if (StringUtils.isBlank(roleStr)){
            throw new Exception("账户无角色、无资源权限");
        }
        String[] roleArr = roleStr.split(",|，");
        List<String> roleIdList = Arrays.asList(roleArr);
        OauthRoleExample roleExample = new OauthRoleExample();
        OauthRoleExample.Criteria roleCriteria = roleExample.createCriteria();
        roleCriteria.andIdIn(roleIdList);
        List<OauthRole> roleList = oauthRoleMapper.selectByExampleWithBLOBs(roleExample);
        return roleList;
    }

    //访问规则列表
    private List<String> getUrlRegexList(List<OauthRole> roleList) throws Exception{
        List<String> resourceIdList = new ArrayList<>();
        for (OauthRole role:roleList){
            String resourceStr = role.getResourceList();
            if (StringUtils.isNotBlank(resourceStr)){
                String[] resourceArr = resourceStr.split(",|，");
                List<String> idList = Arrays.asList(resourceArr);
                resourceIdList.addAll(idList);
            }
        }
        if (resourceIdList.size() == 0){
            throw new Exception("账户无资源权限");
        }
        OauthResourceExample re = new OauthResourceExample();
        OauthResourceExample.Criteria rc = re.createCriteria();
        rc.andIdIn(resourceIdList);
        List<OauthResource> resourceList = oauthResourceMapper.selectByExample(re);
        List<String> urlRegexList = new ArrayList<>();
        for (OauthResource r:resourceList){
            String urlRegex = r.getUrlRegex();
            if (StringUtils.isNotBlank(urlRegex)){
                urlRegexList.add(urlRegex);
            }
        }
        return urlRegexList;
    }

}
