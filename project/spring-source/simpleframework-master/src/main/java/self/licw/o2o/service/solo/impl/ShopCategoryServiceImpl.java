package self.licw.o2o.service.solo.impl;

import self.licw.o2o.entity.HeadLine;
import self.licw.o2o.entity.ShopCategory;
import self.licw.o2o.entity.dto.Result;
import self.licw.o2o.service.solo.ShopCategoryService;
import self.licw.simpleframework.core.annotation.Service;

import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Override
    public Result<List<ShopCategory>> getShopCategoryList(ShopCategory ShopCategoryCondition) {
        return null;
    }

    @Override
    public Result<Boolean> addShopCategory(ShopCategory ShopCategory) {
        return null;
    }

    @Override
    public Result<Boolean> removeShopCategory(int ShopCategoryId) {
        return null;
    }

    @Override
    public Result<Boolean> modifyShopCategory(ShopCategory ShopCategory) {
        return null;
    }

    @Override
    public Result<ShopCategory> getShopCategorybyId(int ShopCategoryId) {
        return null;
    }
}
