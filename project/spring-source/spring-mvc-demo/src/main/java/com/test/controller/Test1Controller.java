package com.test.controller;


import com.test.HeadLine;
import com.test.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/test1")
public class Test1Controller {

    @RequestMapping(value = "/result/null",method = RequestMethod.GET)
    public void resultNull() {
        System.out.println("resultNull");
    }

    @RequestMapping(value = "/error",method = RequestMethod.GET)
    public void error() {
        throw new RuntimeException("抛出异常");
    }

    @GetMapping(value = "/query")
    @ResponseBody
    public Result query(@RequestParam("pageNum")String pageNum, @RequestParam("pageSize")String pageSize) {
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

    // localhost:8080/test1/form-page?lineName=1111
	@RequestMapping(value = "/form-page",method = RequestMethod.GET)
	public ModelAndView modelView(@RequestParam("lineName") String lineName){

		HashMap<String, Object> model = new HashMap<>();
		model.put("lineName", lineName);
		ModelAndView modelAndView = new ModelAndView("form-page", model);

		return modelAndView;
	}

	@RequestMapping(value = "/index",method = RequestMethod.GET)
	public ModelAndView index(){
		return new ModelAndView("index");
	}

}
