package sheep.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public String login(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String loginType = request.getHeader("X-Forward-LoginType");
        User user = null;
        if (loginType.equals("Username")) {
            String username = request.getHeader("X-Forward-Username");
            user = userService.getUserByName(username);
        } else if (loginType.equals("Tel")) {
            String phoneNumber = request.getHeader("X-Forward-Tel");
            user = userService.getUserByTel(phoneNumber);
        }
        
        if (user == null) {
           response.setStatus(403);
           return "";
        }
        
        String password = request.getHeader("X-Forward-Password");

        if (encoder.matches(password, user.getPassword())) {
            String token = JwtUtil.generatorToken();
            response.setHeader("X-Token", token);
            response.setStatus(200);
        } else {
            response.setStatus(403);
        }

        return "";
    }
}
