package com.test.controller;


import com.test.HeadLine;
import com.test.Result;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test2")
public class Test2Controller {

	@GetMapping("/path/{userId}")
	public Result path(@PathVariable("userId")long userId) {
		Result result = new Result();
		result.setCode(200);
		result.setData(userId);
		return result;
	}

    @PostMapping(value = "/page")
    public Result page(@RequestBody PageVo pageVo) {
        List<HeadLine> headLines = new ArrayList<>();
        HeadLine headLine1 = new HeadLine();
        headLine1.setLineId(1L);
        headLine1.setLineName(pageVo.getName());
        headLine1.setLineLink("www.baidu.com");
        headLine1.setLineImg(String.valueOf(pageVo.getNum()));
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

}
