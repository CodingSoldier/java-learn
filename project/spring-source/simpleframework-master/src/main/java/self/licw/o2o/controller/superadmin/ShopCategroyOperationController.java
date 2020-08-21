package self.licw.o2o.controller.superadmin;

import self.licw.o2o.entity.ShopCategory;
import self.licw.o2o.entity.dto.Result;
import self.licw.o2o.service.solo.ShopCategoryService;
import self.licw.simpleframework.core.annotation.Controller;
import self.licw.simpleframework.inject.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ShopCategroyOperationController {
    @Autowired
    private ShopCategoryService shopCategoryService;

    Result<List<ShopCategory>> getHeadLineList(HttpServletRequest request, HttpServletResponse response){
        return shopCategoryService.getShopCategoryList(new ShopCategory());
    }

    Result<Boolean> addHeadLine(HttpServletRequest request, HttpServletResponse response){
        return shopCategoryService.addShopCategory(new ShopCategory());
    }

    Result<Boolean> removeHeadLine(HttpServletRequest request, HttpServletResponse response) {
        return shopCategoryService.removeShopCategory(1);
    }
    Result<Boolean> modifyHeadLine(HttpServletRequest request, HttpServletResponse response){
        return shopCategoryService.modifyShopCategory(new ShopCategory());
    }
    Result<ShopCategory> getHeadLinebyId(int headLineId){
        return shopCategoryService.getShopCategorybyId(1);
    }
}
