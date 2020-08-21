package self.licw.simpleframework.mvc.type;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储处理完后的结果数据，以及显示该数据的视图
 */
@Getter
public class ModelAndView {
    //页面所在路径
    private String view;
    //页面的data数据
    private Map<String,Object> model = new HashMap<>();

    /*
    setter方法使用自定义的，要设置this返回值
    为了使用方便,可以在一个方法中连续调用多个方法：
    modelAndView.setView("addheadline.jsp").addViewData("aaa","bbb");
     */
    public ModelAndView setView(String view){
        this.view = view;
        return this;
    }

    public ModelAndView addViewData(String attributeName,Object attributeValue){
        model.put(attributeName, attributeValue);
        return this;
    }
}
