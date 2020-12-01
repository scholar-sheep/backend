package sheep.user.service;

import org.springframework.stereotype.Service;
import sheep.user.entity.User;

import java.util.List;

@Service
public interface UserService {
        //User selectById(int id);
        //void register(UserRegisterVo userRegisterVo);

        List<User> getUserList();

        User getUserById(int ID);

        User updateUserInfo(User user);

        String addUser(User user);

        User getUserByName(String username);

        String changeAvatar(int ID,String avatar);
}
