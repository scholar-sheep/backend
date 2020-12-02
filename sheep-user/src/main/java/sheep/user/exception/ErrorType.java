package sheep.user.exception;

public enum  ErrorType implements ErrorCode{
    NO_LOGIN(2001,"未登录，请先登录！"),
    SYSTEM_ERROR(2002,"服务器异常，请稍后重试"),
    INFORMATION_ERROR(2003,"账号或密码错误"),
    NAME_REPEAT(2004,"用户名重复");


    @Override
    public Integer getType() {
        return type;
    }

    @Override
    public String getMessage() {
        return message;
    }

    private Integer type;
    private String message;

    ErrorType(Integer type, String message) {
        this.type = type;
        this.message = message;
    }
}
