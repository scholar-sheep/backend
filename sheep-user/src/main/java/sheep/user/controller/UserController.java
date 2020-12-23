package sheep.user.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import freemarker.core.ReturnInstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.ServerResponse;
import sheep.user.entity.*;
import sheep.user.exception.ErrorType;
import sheep.user.exception.ResultDTO;
import sheep.user.service.UserServiceImp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


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
    public Object getUserByName(@PathVariable String username){
        User result = userService.getUserByName(username);
        if(result!=null)
            return userService.getUserByName(username);
        else return ResultDTO.errorOf(ErrorType.USER_NOT_FOUND);
    }

//    @PostMapping("/test")
//    public int test(@RequestParam("mobile") String mobile,@RequestParam("code")String code){
//        return userService.updateUserCode(mobile,code);
//    }

    //查看个人信息
    @RequestMapping(value = "/user/info/{ID}",method = RequestMethod.GET)
    public Object getUserById(@PathVariable("ID") int ID){
        User result = userService.getUserById(ID);
        if(result!=null)
            return ResultDTO.okOf(userService.getUserById(ID));
        else return ResultDTO.errorOf(ErrorType.USER_NOT_FOUND);
    }

    //修改个人信息 username usertype必填
    @PutMapping(value = "/user/info/{ID}")
    public Object updateUserInfo(@PathVariable("ID") int ID,@RequestBody User user){
        System.out.println(user);
        user.setID(ID);
        User origin = userService.getUserById(ID);
        if(user.getUsername()==null)
            user.setUsername(origin.getUsername());
        if(user.getUsertype()==0)
            user.setUsertype(origin.getUsertype());
        if(user.getMobile()==null)
            user.setMobile(origin.getMobile());
        if(user.getPassword()==null)
            user.setPassword(origin.getPassword());
        else user.setPassword(new MyPasswordEncoder().encode(user.getPassword()));
        if(user.getEmail()==null)
            user.setEmail(origin.getEmail());
        if(user.getNote()==null)
            user.setNote(origin.getNote());
        if(user.getBirthday()==null)
            user.setBirthday(origin.getBirthday());
        else{
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(user.getBirthday());
            calendar.add(Calendar.DATE,1);
            user.setBirthday(calendar.getTime());
        }

        int result = userService.updateUserInfo(user);
        if(result!=0){
            System.out.println("请求后"+userService.getUserById(ID).getBirthday());
            return ResultDTO.okOf(userService.updateUserInfo(user));
        }
//            return ResultDTO.okOf(userService.getUserById(ID));

        else return ResultDTO.errorOf(ErrorType.UPDATE_ERROR);
    }

    //上传头像
    @PostMapping(value = "/avatar")
    public Object uploadAvatar(@RequestParam("avatar")MultipartFile file,@RequestParam("ID")int ID, HttpServletRequest request) throws IOException {
        //先查看用户是否存在
        User user = userService.getUserById(ID);
        System.out.println("ID"+ID);
        System.out.println("file"+file);
        if(user==null) return ResultDTO.errorOf(ErrorType.USER_NOT_FOUND);
        //开始上传头像
        String path = ResourceUtils.getURL("classpath:").getPath() + "static/headImage/";
        System.out.println("path"+path);
        String url = request.getContextPath() + "/headImage/";
        File filePath = new File(path);
        System.out.println("文件的保存路径：" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdirs(); // mkdir()不会创建目录,找不到相应路径时返回false;而mkdirs()当目录不存在时则会创建相应目录
        }
        //获取原始文件名称(包含格式)
        String originalFileName = file.getOriginalFilename();
        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
//        HttpSession session = request.getSession();
//        String userId = Integer.toString((Integer)session.getAttribute("userId"));
//        String fileName = userId + "."+ type; // 新文件名，这里可以根据需要改名
        String fileName = ID + "." +type;
        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName); // 未使用outputStream.write()的时候,是一个File对象,保存在内存中,硬盘中看不到,但是可以使用这个对象
        try{
            // 使用springmvc的transferTo方法上传文件
            file.transferTo(targetFile);
        }catch (IOException e){
            e.printStackTrace();
        }
        String avatar = url+fileName;
        int i = userService.changeAvatar(ID,avatar);
        if (i != 0)
            return ResultDTO.okOf(avatar);
        else return ResultDTO.errorOf(ErrorType.UPDATE_ERROR);
    }

    //关注 currentID为当前用户ID followID为其关注的用户ID
    @PostMapping("/follow")
    public Object follow(int currentID,int followID){
        int result = userService.follow(currentID,followID);
        if(result!=0)
            return ResultDTO.okOf();
        else return ResultDTO.errorOf(ErrorType.INSERT_ERROR);
    }

    //查看关注用户列表
    @RequestMapping(value = "/follow/{ID}",method = RequestMethod.GET)
    public List<User> getFollow(@PathVariable("ID") int ID){
        List<Integer> result= userService.getFollow(ID);
        List<User> userList = new ArrayList<>();
        for(int i=0;i<result.size();i++){
            User user = userService.getUserById(result.get(i));
            userList.add(user);
        }
        return userList;
    }

    //收藏学术成果
    @PostMapping(value = "/collect")
    public Object collect(int userId,int paperId,int infoId){
        int result = userService.collect(userId,paperId,infoId);
        if(result!=0)
            return ResultDTO.okOf();
        else return ResultDTO.errorOf(ErrorType.INSERT_ERROR);
    }

    //查看收藏列表
    @RequestMapping(value = "/collect/{ID}",method = RequestMethod.GET)
    public List<Paper> getCollect(@PathVariable("ID") int ID){
        List<UserCollect> result = userService.getCollect(ID);
        System.out.println(result);
        System.out.println(result.size());
        List<Paper> paperList = new ArrayList<>();
        for(int i=0;i<result.size();i++){
            Paper paper = userService.getPaperById(result.get(i).getPaperId(),result.get(i).getInfoId());
            System.out.println(paper);
            System.out.println(result.get(i));
            paperList.add(paper);
        }
        return paperList;
    }

}
