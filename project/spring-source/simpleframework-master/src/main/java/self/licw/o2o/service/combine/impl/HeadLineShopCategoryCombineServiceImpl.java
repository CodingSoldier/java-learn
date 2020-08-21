package self.licw.o2o.service.combine.impl;

import self.licw.o2o.entity.HeadLine;
import self.licw.o2o.entity.ShopCategory;
import self.licw.o2o.entity.dto.MainPageInfoDto;
import self.licw.o2o.entity.dto.Result;
import self.licw.o2o.service.combine.HeadLineShopCategoryCombineService;
import self.licw.o2o.service.solo.HeadLineService;
import self.licw.o2o.service.solo.ShopCategoryService;
import self.licw.simpleframework.core.annotation.Service;
import self.licw.simpleframework.inject.annotation.Autowired;

import java.util.List;

@Service
public class HeadLineShopCategoryCombineServiceImpl implements HeadLineShopCategoryCombineService {
    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Override
    public Result<MainPageInfoDto> getMainPageInfo() {
        HeadLine headLinecondition = new HeadLine();
        headLinecondition.setEnableStatus(1);
        Result<List<HeadLine>> HeadLine = headLineService.getHeadLineList(headLinecondition);

        ShopCategory shopCategoryCondition = new ShopCategory();
        Result<List<ShopCategory>> ShopCategory = shopCategoryService.getShopCategoryList(shopCategoryCondition);

        Result<MainPageInfoDto> result = mergeMainPageInfoResult(HeadLine,ShopCategory);
        return result;
    }

    private Result<MainPageInfoDto> mergeMainPageInfoResult(Result<List<HeadLine>> headLine, Result<List<ShopCategory>> shopCategory) {
        return null;
    }


}
