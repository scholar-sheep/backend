package sheep.portal.exception;

public class CreatePortalException extends RuntimeException {
    public CreatePortalException() {
        super("您已拥有门户");
    }
}