package com.c.service.combine;


import com.c.entity.dto.MainPageInfoDTO;
import com.c.entity.dto.Result;

public interface HeadLineShopCategoryCombineService {
    Result<MainPageInfoDTO> getMainPageInfo();
}
