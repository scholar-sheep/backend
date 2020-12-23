package sheep.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sheep.user.entity.LoginResult;
import sheep.user.entity.MyPasswordEncoder;
import sheep.user.entity.User;
import sheep.user.service.UserServiceImp;
import sheep.user.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PassportController {

    private final UserServiceImp userService;
    private final MyPasswordEncoder encoder;

    public PassportController(UserServiceImp userService) {
        this.userService = userService;
        this.encoder = new MyPasswordEncoder();
    }

    @PostMapping("/passport/login")
    public Object login(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = null;
        try {
            String loginType = request.getHeader("XForwardLoginType");
            if (loginType.equals("Username")) {
                String username = request.getHeader("XForwardUsername");
                user = userService.getUserByName(username);
            } else if (loginType.equals("Tel")) {
                String phoneNumber = request.getHeader("XForwardTel");
                user = userService.getUserByTel(phoneNumber);
            }
        } catch (Exception e) {
            response.setStatus(500);
            return "required param not found";
        }

        if (user == null) {
           response.setStatus(403);
           return "login fail";
        }

        String password = request.getHeader("XForwardPassword");

        if (encoder.matches(password, user.getPassword())) {
            String token = JwtUtil.generatorToken(user.getID());
            response.setHeader("X-Token", token);
            response.setStatus(200);
            LoginResult result = new LoginResult();
            result.setToken(token);
            result.setUserId(user.getID());
            return result;
        } else {
            response.setStatus(403);
            return "login fail";
        }
    }
}
