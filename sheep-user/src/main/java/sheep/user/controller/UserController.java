package sheep.user.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sheep.user.entity.JsonResult;
import sheep.user.entity.MyPasswordEncoder;
import sheep.user.entity.User;
//import sheep.user.exception.ErrorType;
//import sheep.user.exception.ResultDTO;
import sheep.user.service.UserServiceImp;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;


@RestController
public class UserController {
    /*@Autowired
    UserService userService;*/

    @Autowired
    private UserServiceImp userService;

    //返回所有用户列表
    @RequestMapping(value = "/getuserlist", method = RequestMethod.GET)
    public List<User> getUserList(){
        return userService.getUserList();
    }

    //通过username查找到用户
    @RequestMapping(value="/getuserbyname/{username}",method = RequestMethod.GET)
    public User getUserByName(@PathVariable String username){
        return userService.getUserByName(username);
    }

    //注册
    @PostMapping("/register")
    public String addUser(User user){
        user.setPassword(new MyPasswordEncoder().encode(user.getPassword()));
        return userService.addUser(user);
    }

    //登录
    /*@PostMapping("/login")
    public Map<String,String> login(@RequestBody User user){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",user.getUsername());
        wrapper.eq("password",user.getPassword());
        //数据库中查询用户信息
        User userInSaved = userService.getUserByName(user.getUsername());
        HashMap<String,String> result = new HashMap<>();//返回结果信息给前端

        if(userInSaved == null){
            result.put("code","401");
            result.put("msg","用户名或密码错误");
        }

        Map<String,String> map = new HashMap<>();//用来存放payload信息
        map.put("ID",userInSaved.getID()+"");
        map.put("username",userInSaved.getUsername());
        map.put("usertype",userInSaved.getUsertype()+"");

        //生成token令牌
        String token = JWTUtil.generateToken(map);

        //返回前端token
        result.put("code","200");
        result.put("msg","登录成功");
        result.put("token",token);
        return result;
    }*/

    //查看个人信息
    @RequestMapping(value = "/user/info/{ID}",method = RequestMethod.GET)
    public User getUserById(@PathVariable("ID") int ID){
        return userService.getUserById(ID);
    }

    //修改个人信息 username usertype必填 数据库password去掉not null
    @PutMapping(value = "/user/info/{ID}")
    public User updateUserInfo(@PathVariable("ID") int ID,User user){
        user.setID(ID);
        return userService.updateUserInfo(user);
    }

    //上传头像
    @PutMapping(value = "/user/avatar/{ID}")
    public String uploadAvatar(@RequestParam("head")MultipartFile file,@PathVariable("ID") int ID,
                                           HttpServletRequest request)throws IllegalStateException, IOException {
        //1.确定保存的文件夹
        String dirPath = request.getServletContext().getRealPath("upload");//会在webapp下面创建此文件夹
        System.out.println("dirPath="+dirPath);

        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        //2.确定保存的文件名
        String originalFilename = file.getOriginalFilename();
        int beginIndex = originalFilename.lastIndexOf(".");
        String suffix ="";
        if(beginIndex!=-1) {
            suffix = originalFilename.substring(beginIndex);
        }
        String filename =UUID.randomUUID().toString()+suffix;
        //创建文件对象，表示要保存的头像文件,第一个参数表示存储的文件夹，第二个参数表示存储的文件
        File dest = new File(dir,filename);
        //执行保存
        file.transferTo(dest);
        //更新数据表
        String avatar = "/upload/"+filename;
        //Integer ID = Integer.valueOf(request.getSession().getAttribute("ID").toString());
        // 通过uid找到用户
        User results = userService.getUserById(ID);
        return userService.changeAvatar(results.getID(), avatar);
    }


    /**
     * 注册用户
     * @return
     */
    /*
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
    */
}
