package sheep.user.entity;

import lombok.Data;

@Data
public class LoginResult {
    private String Token;
    private int UserId;
}
