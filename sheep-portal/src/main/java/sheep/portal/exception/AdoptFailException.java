package sheep.portal.exception;

public class AdoptFailException extends RuntimeException {
    public AdoptFailException() {
        super("该门户已被认领或您已认领过门户");
    }
}