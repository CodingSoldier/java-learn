package com.test.controller;


import com.test.HeadLine;
import com.test.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

        Result result = new Result();
        result.setData(headLines);
        result.setCode(200);
        return result;
    }

	@RequestMapping(value = "/form-page",method = RequestMethod.GET)
	public String formPage() {
		return "form-page";
	}
}
