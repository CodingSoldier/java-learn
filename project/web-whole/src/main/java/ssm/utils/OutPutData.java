package ssm.utils;

public class OutPutData {
    private Object data;
    private boolean success;
    private String message = "";
    private Throwable throwable;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
