package sheep.user.exception;

public enum  ErrorType implements ErrorCode{
    NAME_REPEAT(1001,"用户名已存在"),
    MOBILE_ERROR(1002,"手机号格式错误"),
    INFORMATION_ERROR(1003,"账号或密码错误"),
    USER_NOT_FOUND(1004,"用户不存在"),
    INSERT_ERROR(1005,"插入错误"),
    UPDATE_ERROR(1006,"更新错误"),
    CODE_ERROR(1007,"验证码错误"),
    MOBILE_REPEAT(1008,"手机号已存在");


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
