package sheep.common.exception;

public enum  ErrorType implements sheep.common.exception.ErrorCode {
    NO_LOGIN(2001,"未登录，请先登录！"),
    SYSTEM_ERROR(2002,"服务器异常，请稍后重试"),
    INFORMATION_ERROR(2003,"账号或密码错误"),
    NAME_REPEAT(2004,"用户名重复"),
    PUBLISH_ERROR(2005,"文档发表失败"),
    UPDATE_FAILED(2006,"文档更新失败"),
    TEAM_CREATE_FAILED(2007,"团队创建失败"),
    TEAM_JOIN_FAILED(2008,"加入团队失败"),
    READ_NOTIFICATION_FAILED(2009, "你无权访问该文档"),
    SOMEONE_EDITING(2010, "有人正在编辑文档，你无法进行修改"),
    DOC_DELETED(2011,"文档已删除"),
    USER_HAVE_ENTER(2012,"该用户已加入团队"),

    ADOPT_ERROR(401,"该门户已被认领或您已认领过门户"),
    CREATE_PORTAL_ERROR(402, "您已拥有门户"),
    PORTAL_ERROR(403, "门户不存在");



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
