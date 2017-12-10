package ssm.projectweb.module01.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ssm.projectweb.module01.pojo.User;
import ssm.projectweb.module01.service.ServiceHtmlToSql;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/11/18
 */
@Controller
@RequestMapping("/test_html_to_sql")
public class CtrlHtmlToSql {
    private static Logger logger = LoggerFactory.getLogger("CtrlHtmlToSql.class");
    @Resource
    private ServiceHtmlToSql serviceHtmlToSql;

    //  http://localhost:8080/test_html_to_sql/test1?id=10
    @RequestMapping("/test1")
    @ResponseBody
    public Map<String,Object> test1(HttpServletRequest request, @RequestParam int id) throws Exception{
        User user = serviceHtmlToSql.testFindUser(id);
        logger.info("日志，打印{}",user.getUsername());
        Map<String,Object> map = new HashMap<String,Object>();
        user.setId(66666666);
        map.put("data",user);
        return map;
    }
}
