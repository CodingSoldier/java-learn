package self.licw.o2o.entity.dto;

import lombok.Data;
import self.licw.o2o.entity.HeadLine;
import self.licw.o2o.entity.ShopCategory;

import java.util.List;

@Data
public class MainPageInfoDto {
    private List<HeadLine> headLineList;
    private List<ShopCategory> shopCategoryList;
}
