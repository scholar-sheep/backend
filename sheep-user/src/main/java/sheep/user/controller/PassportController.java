package sheep.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sheep.user.entity.LoginResult;
import sheep.user.entity.MyPasswordEncoder;
import sheep.user.entity.User;
import sheep.user.exception.ErrorType;
import sheep.user.exception.ResultDTO;
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
                System.out.println("username: " + username + "\n");
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
            response.setHeader("Token", token);
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

    //注册
    @PostMapping("/passport/register")
    public Object addUser(@RequestBody User user, HttpServletResponse response){
        System.out.println("register"+user);
        User result1 = userService.getUserByName(user.getUsername());
        //用户名重复
        if(result1!=null)
            return ResultDTO.errorOf(ErrorType.NAME_REPEAT);
        //手机号格式不对
        if(user.getMobile().length()!=11)
            return ResultDTO.errorOf(ErrorType.MOBILE_ERROR);
        //通过手机号去数据库找验证码
        String code = userService.getCodeSaved(user.getMobile());
        String codeSaved = code;
        //userService.updateUserCode(user.getMobile(), );
        if(user.getCode().equals(codeSaved)){
            //对密码加密传输
            user.setPassword(new MyPasswordEncoder().encode(user.getPassword()));
            if(userService.addUser(user)!=0) {
                LoginResult l = new LoginResult();
                l.setToken(JwtUtil.generatorToken(user.getID()));
                l.setUserId(user.getID());
                response.setStatus(200);
                return ResultDTO.okOf(l);
            }
            else return ResultDTO.errorOf(ErrorType.INSERT_ERROR);
        }
        else return ResultDTO.errorOf(ErrorType.CODE_ERROR);
    }

    //获取验证码
    @PostMapping("passport/code/{mobile}")
    public Object getCode(@PathVariable("mobile") String mobile){
//        @PostMapping("/code")
//        public Object getCode(@RequestParam("mobile") String mobile){
        //手机号重复
        int result = userService.getUserByMobile(mobile);
        if(result==0){
            ResultDTO code =  ResultDTO.okOf();
            code.setData(userService.getCode(mobile));
            return code;
        }
        return ResultDTO.errorOf(ErrorType.MOBILE_REPEAT);
    }
}
