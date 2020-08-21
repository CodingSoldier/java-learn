package self.licw.o2o.service.solo.impl;

import lombok.extern.slf4j.Slf4j;
import self.licw.o2o.entity.HeadLine;
import self.licw.o2o.entity.dto.Result;
import self.licw.o2o.service.solo.HeadLineService;
import self.licw.simpleframework.core.annotation.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HeadLineServiceImpl implements HeadLineService {
    @Override
    public Result<List<HeadLine>> getHeadLineList(HeadLine headLineCondition) {
        return null;
    }

    @Override
    public Result<Boolean> addHeadLine(HeadLine headLine) {
        log.info("addheadline 被执行啦 lineName[{}]",headLine.getLineName());
        Result<Boolean> result = new Result<Boolean>();
        result.setCode(200);
        result.setMsg("请求成功");
        result.setData(true);
        return result;
    }

    @Override
    public Result<Boolean> removeHeadLine(int headLineId) {
        return null;
    }

    @Override
    public Result<Boolean> modifyHeadLine(HeadLine headLine) {
        return null;
    }

    @Override
    public Result<HeadLine> getHeadLinebyId(int headLineId) {
        List<HeadLine> headLines = new ArrayList<>();
        HeadLine headLine1 = new HeadLine();
        headLine1.setLineId(1L);
        headLine1.setLineName("头条1");
        headLine1.setLineLink("www.baidu.com");
        headLine1.setLineImg("wawawa");
        headLines.add(headLine1);

//        HeadLine headLine2 = new HeadLine();
//        headLine2.setLineId(2L);
//        headLine2.setLineName("头条2");
//        headLine2.setLineLink("www.google.com");
//        headLine2.setLineImg("hahaha");
//        headLines.add(headLine2);

        Result<HeadLine> result = new Result<>();
        result.setData(headLine1);
        result.setCode(200);
        return result;
    }
}
