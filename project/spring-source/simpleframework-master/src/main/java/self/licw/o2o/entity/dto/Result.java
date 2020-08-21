package self.licw.o2o.entity.dto;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;
}
