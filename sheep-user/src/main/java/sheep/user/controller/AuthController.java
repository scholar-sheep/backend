package sheep.user.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sheep.user.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class AuthController {

    @RequestMapping("/auth")
    public void auth(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = request.getHeader("Token");
        if (token == null || token.equals("")) {
            return;
        }

        if (JwtUtil.verifyToken(token)) {
            response.setHeader("X-UserId", String.valueOf(JwtUtil.parseUserId(token)));
        }
    }
}
