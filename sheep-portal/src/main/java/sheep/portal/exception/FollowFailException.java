package sheep.portal.exception;

public class FollowFailException extends RuntimeException {
    public FollowFailException() {
        super("您已关注该学者");
    }
}