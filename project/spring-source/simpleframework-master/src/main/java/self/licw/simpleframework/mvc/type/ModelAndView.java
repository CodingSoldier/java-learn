package self.licw.simpleframework.mvc.type;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图和数据对象
 */
@Getter
public class ModelAndView {
    // 视图路径
    private String view;
    // 页面数据
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView setView(String view){
        this.view = view;
        return this;
    }

    public ModelAndView addViewData(String attributeName, Object attributeValue){
        model.put(attributeName, attributeValue);
        return this;
    }
}
