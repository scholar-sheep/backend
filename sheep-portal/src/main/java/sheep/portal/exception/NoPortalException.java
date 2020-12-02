package sheep.portal.exception;

public class NoPortalException extends RuntimeException {
    public NoPortalException() {
        super("门户不存在");
    }
}