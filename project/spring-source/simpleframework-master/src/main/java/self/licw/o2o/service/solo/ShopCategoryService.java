package self.licw.o2o.service.solo;


import self.licw.o2o.entity.HeadLine;
import self.licw.o2o.entity.ShopCategory;
import self.licw.o2o.entity.dto.Result;

import java.util.List;

public interface ShopCategoryService {
    Result<List<ShopCategory>> getShopCategoryList(ShopCategory ShopCategoryCondition);
    Result<Boolean> addShopCategory(ShopCategory ShopCategory);
    Result<Boolean> removeShopCategory(int ShopCategoryId);
    Result<Boolean> modifyShopCategory(ShopCategory ShopCategory);
    Result<ShopCategory> getShopCategorybyId(int ShopCategoryId);
}
