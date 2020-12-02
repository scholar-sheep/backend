package sheep.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class UserRegisterVo   {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    private String password;
}