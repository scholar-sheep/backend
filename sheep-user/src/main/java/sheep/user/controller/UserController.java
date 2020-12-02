package sheep.user.controller;
import org.springframework.validation.BindingResult;
import sheep.common.utils.ResultDTO;
import sheep.common.exception.ErrorType;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sheep.user.entity.User;
//import sheep.user.exception.ErrorType;
//import sheep.user.exception.ResultDTO;
import sheep.user.exception.UserExistException;
import sheep.user.service.UserService;
import sheep.user.vo.UserRegisterVo;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController {
    @Autowired
    UserService userService;


    /**
     * 查看个人信息
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/userinfo/{id}", method = RequestMethod.GET)
    public Object getInformation(@PathVariable("id") int id) {
        User user = userService.selectById(id);
        return user;
    }

    /**
     * 注册用户
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Object register(@Valid @RequestBody UserRegisterVo userRegisterVo,BindingResult bindingResult) {
        try{
        userService.register(userRegisterVo);

        if (bindingResult.hasErrors()) {
            Map<String , String> map = new HashMap<>();
            bindingResult.getFieldErrors().forEach( (item) -> {
                String message = item.getDefaultMessage();
                String field = item.getField();
                map.put( field , message );
            } );
            return ResultDTO.errorOf( 400 , "非法参数 !" , map);
        }
        }
        catch(UserExistException userExistException)
        {
            return ResultDTO.errorOf(ErrorType.NAME_REPEAT);
        }
        return ResultDTO.okOf();
    }

}
