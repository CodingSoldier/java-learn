package self.licw.o2o.controller;

import self.licw.o2o.entity.HeadLine;
import self.licw.o2o.entity.dto.Result;
import self.licw.simpleframework.core.annotation.Controller;
import self.licw.simpleframework.mvc.annotation.RequestMapping;
import self.licw.simpleframework.mvc.annotation.RequestParam;
import self.licw.simpleframework.mvc.annotation.ResponseBody;
import self.licw.simpleframework.mvc.type.ModelAndView;
import self.licw.simpleframework.mvc.type.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "/result/null",method = RequestMethod.GET)
    public void resultNull() {
        System.out.println("resultNull");
    }

    @RequestMapping(value = "/error",method = RequestMethod.GET)
    public void error() {
        throw new RuntimeException("抛出异常");
    }

    @RequestMapping(value = "/query",method = RequestMethod.GET)
    @ResponseBody
    public Result query() {
        List<HeadLine> headLines = new ArrayList<>();
        HeadLine headLine1 = new HeadLine();
        headLine1.setLineId(1L);
        headLine1.setLineName("头条1");
        headLine1.setLineLink("www.baidu.com");
        headLine1.setLineImg("wawawa");
        headLines.add(headLine1);

        HeadLine headLine2 = new HeadLine();
        headLine2.setLineId(2L);
        headLine2.setLineName("头条2");
        headLine2.setLineLink("www.google.com");
        headLine2.setLineImg("hahaha");
        headLines.add(headLine2);

        Result<List<HeadLine>> result = new Result<>();
        result.setData(headLines);
        result.setCode(200);
        return result;
    }

    /**
     * http://localhost:8080/simpleframework-master/test/model/view
     * 点击提交
     */
    @RequestMapping(value = "/model/view",method = RequestMethod.POST)
    public ModelAndView modelView(@RequestParam("lineName") String lineName){
        HeadLine headLine = new HeadLine();
        headLine.setLineName(lineName);

        Result<HeadLine> result = new Result<>();
        result.setData(headLine);
        result.setCode(200);
        result.setMsg("ok"+lineName);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("form-page.jsp").addViewData("result",result);
        return modelAndView;
    }

}
