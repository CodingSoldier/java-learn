package self.licw.o2o.service.combine;

import self.licw.o2o.entity.dto.MainPageInfoDto;
import self.licw.o2o.entity.dto.Result;

public interface HeadLineShopCategoryCombineService {
    Result<MainPageInfoDto> getMainPageInfo();
}
