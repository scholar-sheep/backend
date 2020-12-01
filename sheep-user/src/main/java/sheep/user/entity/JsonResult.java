package sheep.user.entity;

import java.io.Serializable;

public class JsonResult implements Serializable {
    private static final long serialVersionUID = -4800793124936904868L;
    public static final int SUCCESS=200;
    public static final int ERROR=201;

    /**
     * 返回是否成功的状态,200表示成功,
     * 201或其他值 表示失败
     */
    private int state;
    /**
     * 成功时候,返回的JSON数据
     */
    private Object data;
    /**
     * 是错误时候的错误消息
     */
    private String message;


    public JsonResult() {
    }


    public JsonResult(int state, Object data, String message) {
        this.state = state;
        this.data = data;
        this.message = message;
    }

    public JsonResult(Throwable e){
        state = ERROR;
        data=null;
        message=e.getMessage();
    }

    public JsonResult(Object data){
        state = SUCCESS;
        this.data=data;
        message="";
    }

    public int getState() {
        return state;
    }


    public void setState(int state) {
        this.state = state;
    }


    public Object getData() {
        return data;
    }


    public void setData(Object data) {
        this.data = data;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "JsonResult [state=" + state + ", data=" + data + ", message=" + message + "]";
    }
}
