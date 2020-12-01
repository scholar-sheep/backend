package sheep.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import sheep.user.mapper.UserMapper;
import sheep.user.entity.User;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        try{
            List<User> users = userMapper.getUserList();
            return users;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public User getUserById(int ID) {
        try{
            User user = userMapper.getUserById(ID);
            return user;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public User updateUserInfo(User user) {
        try{
            userMapper.updateUserInfo(user);
            return getUserById(user.getID());
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public String addUser(User user) {
        try{
            int i = userMapper.addUser(user);
            return "添加成功"+i+"条数据";
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public User getUserByName(String username) {
        try{
            User user = userMapper.getUserByName(username);
            return user;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public String changeAvatar(int ID, String avatar) {
        try {
            User result = userMapper.getUserById(ID);
            if (result != null) {
                result.setHead(avatar);
                int rows = userMapper.updateUserInfo(result);
                if(rows==1)return "头像修改成功";
                else return "头像修改失败";
            }
            else return "用户不存在";
        }catch (Exception e){
            throw e;
        }
    }


    /**
    public User selectById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public void register(UserRegisterVo userRegisterVo)
    {
        // 检查用户名是否唯一
        checkUserNameUnique(userRegisterVo.getUserName());
        //该用户信息唯一，进行插入
        User entity = new User();
        //保存基本信息
        entity.setUsername(userRegisterVo.getUserName());
        entity.setCreate_time(new Date());
        //使用加密保存密码 --待实现
        entity.setPassword(userRegisterVo.getPassword());

        // 保存用户信息
        userMapper.insert(entity);
    }
    private void checkUserNameUnique(String userName) {
        Integer count = userMapper.selectCount(new QueryWrapper<User>().eq("username", userName));
        if (count > 0) {
            throw new UserExistException();
        }
    }
    */
}
