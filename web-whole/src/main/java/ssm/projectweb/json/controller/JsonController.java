package ssm.projectweb.json.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ssm.projectweb.json.JsonVo;
import ssm.utils.OutPutData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/18
 */
@RequestMapping("/json")
@Controller
public class JsonController {

    //@RequestParam Map<String, Object> params只能接收简单类型数据，不能接收list、map
    @RequestMapping("/requestParamMap")
    @ResponseBody
    public OutPutData requestParamMap(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
        OutPutData outPutData = new OutPutData();

        return outPutData;
    }

    //@ModelAttribute(ps:可以不加注释)，前台get请求也可以发送list、map绑定到bean，但是不能绑定到map，Date依旧是传时间对象
    @RequestMapping("/f2")
    @ResponseBody
    //public OutPutData f2(HttpServletRequest request, @ModelAttribute JsonVo jsonVo) throws Exception{
    public OutPutData f2(HttpServletRequest request, JsonVo jsonVo) throws Exception{
        OutPutData outPutData = new OutPutData();
        System.out.println(jsonVo);
        return outPutData;
    }

}
