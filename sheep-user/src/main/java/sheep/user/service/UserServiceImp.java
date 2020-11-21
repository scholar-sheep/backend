package sheep.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.user.exception.UserExistException;
import sheep.user.mapper.UserMapper;
import sheep.user.entity.User;
import sheep.user.vo.UserRegisterVo;

import java.util.Date;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    UserMapper userMapper;

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
}
