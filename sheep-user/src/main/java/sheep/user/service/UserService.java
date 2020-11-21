package sheep.user.service;

import sheep.user.entity.User;
import sheep.user.vo.UserRegisterVo;

public interface UserService {
        User selectById(int id);
        void register(UserRegisterVo userRegisterVo);
}
