package ssm.utils;

public class OutPutData {
    private Object data;
    private boolean success;
    //描述请求结果的标示
    private String type;
    private String message = "";

    public OutPutData() {

    }
    public OutPutData(Object data, boolean success) {
        this.data = data;
        this.success = success;
    }
    public OutPutData(Object data, boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
