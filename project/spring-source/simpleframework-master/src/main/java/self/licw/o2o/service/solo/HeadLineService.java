package self.licw.o2o.service.solo;


import self.licw.o2o.entity.HeadLine;
import self.licw.o2o.entity.dto.Result;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {

    Result<List<HeadLine>> getHeadLineList(HeadLine headLineCondition);
    Result<Boolean> addHeadLine(HeadLine headLine);
    Result<Boolean> removeHeadLine(int headLineId);
    Result<Boolean> modifyHeadLine(HeadLine headLine);
    Result<HeadLine> getHeadLinebyId(int headLineId);

}
