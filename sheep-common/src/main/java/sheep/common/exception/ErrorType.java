package sheep.common.exception;

public enum ErrorType implements ErrorCode {

    MISS_NANME(2001,"姓名必须指定"),
    ADOPT_ERROR(4001,"该门户已被认领或您已认领过门户"),
    CREATE_PORTAL_ERROR(4002, "您已拥有门户"),
    PORTAL_ERROR(4003, "门户不存在"),
    ADD_REPEAT_ERROR(4004,"已添加此论文"),
    ADD_PAPER_ERROR(4005,"添加论文失败"),
    DELETE_PAPER_ERROR(4006,"删除论文失败"),
    CREATE_PAPER_ERROR(4007,"创建论文失败"),
    FOLLOW_ERROR(4008, "关注失败");



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
