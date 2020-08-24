//package self.licw.o2o.controller.superadmin;
//
//import self.licw.o2o.entity.HeadLine;
//import self.licw.o2o.entity.dto.Result;
//import self.licw.o2o.service.solo.HeadLineService;
//import self.licw.simpleframework.core.annotation.Controller;
//import self.licw.simpleframework.inject.annotation.Autowired;
//import self.licw.simpleframework.mvc.annotation.RequestMapping;
//import self.licw.simpleframework.mvc.annotation.RequestParam;
//import self.licw.simpleframework.mvc.annotation.ResponseBody;
//import self.licw.simpleframework.mvc.type.ModelAndView;
//import self.licw.simpleframework.mvc.type.RequestMethod;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//
//@Controller
//@RequestMapping("/headline")
//public class HeadLineOperationController {
//    @Autowired
//    private HeadLineService headLineService;
//
//    public Result<List<HeadLine>> getHeadLineList(HttpServletRequest request, HttpServletResponse response){
//        return headLineService.getHeadLineList(new HeadLine());
//    }
//
//    @RequestMapping(value = "/add",method = RequestMethod.POST)
//    public ModelAndView addHeadLine(@RequestParam("lineName") String lineName, @RequestParam("lineLink") String lineLink,
//                                    @RequestParam("lineImg") String lineImg, @RequestParam("priority") Integer priority){
//            HeadLine headLine = new HeadLine();
//        headLine.setLineImg(lineImg);
//        headLine.setLineLink(lineLink);
//        headLine.setLineName(lineName);
//        headLine.setPriority(priority);
//        Result<Boolean> result = headLineService.addHeadLine(headLine);
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setView("addheadline.jsp").addViewData("result",result);
//        return modelAndView;
//    }
//    @RequestMapping(value = "/remove",method = RequestMethod.GET)
//    public void removeHeadLine() {
//        System.out.println("执行删除headline");
//    }
//
//    public Result<Boolean> modifyHeadLine(HttpServletRequest request, HttpServletResponse response){
//        return headLineService.modifyHeadLine(new HeadLine());
//    }
//
//    @RequestMapping(value = "/query",method = RequestMethod.GET)
//    @ResponseBody
//    public Result<HeadLine> getHeadLinebyId(){
//        return headLineService.getHeadLinebyId(1);
//    }
//}
