package self.licw.o2o.controller.frontend;

import lombok.Getter;
import self.licw.o2o.entity.dto.MainPageInfoDto;
import self.licw.o2o.entity.dto.Result;
import self.licw.o2o.service.combine.HeadLineShopCategoryCombineService;
import self.licw.simpleframework.core.annotation.Controller;
import self.licw.simpleframework.inject.annotation.Autowired;
import self.licw.simpleframework.mvc.annotation.RequestMapping;
import self.licw.simpleframework.mvc.type.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Getter
@RequestMapping(value = "/main")
public class MainPageController {
    @Autowired(value = "HeadLineShopCategoryCombineServiceImpl")
    private HeadLineShopCategoryCombineService headLineShopCategoryCombineService;
    public Result<MainPageInfoDto> getMainPageInfo(HttpServletRequest request, HttpServletResponse response){
        return headLineShopCategoryCombineService.getMainPageInfo();
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public void throwException(){
        throw new RuntimeException("抛出异常测试");
    }
}
