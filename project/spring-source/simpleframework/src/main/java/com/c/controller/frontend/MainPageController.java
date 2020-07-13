package com.c.controller.frontend;

import com.c.entity.dto.MainPageInfoDTO;
import com.c.entity.dto.Result;
import com.c.service.combine.HeadLineShopCategoryCombineService;
import lombok.Getter;
import org.core.annotation.Controller;
import org.core.inject.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Getter
public class MainPageController {

    @Autowired(value = "HeadLineShopCategoryCombineServiceImpl")
    private HeadLineShopCategoryCombineService headLineShopCategoryCombineService;

    public Result<MainPageInfoDTO> getMainPageInfo(HttpServletRequest req, HttpServletResponse resp){
        return headLineShopCategoryCombineService.getMainPageInfo();
    }
}
