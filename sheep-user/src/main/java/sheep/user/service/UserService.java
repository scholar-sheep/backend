package sheep.user.service;

import org.springframework.stereotype.Service;
import sheep.user.entity.Paper;
import sheep.user.entity.User;
import sheep.user.entity.UserCollect;

import java.util.List;

@Service
public interface UserService {
        //User selectById(int id);
        //void register(UserRegisterVo userRegisterVo);

        List<User> getUserList();

        User getUserById(int ID);
        User getUserByName(String username);
        int getUserByMobile(String tel);

        User getUserByTel(String mobile);

        int updateUserInfo(User user);

        int addUser(User user);

        int changeAvatar(int ID,String avatar);

        int follow(int currentID,int followID);

        List<Integer> getFollow(int ID);

        String getCode(String mobile);
        String sendVerificationCodeSms(String mobile);
        Object getCodeByMobile(String mobile);
        int updateUserCode(String mobile,String code);

        int collect(int userId,int paperId,int infoId);
        List<UserCollect> getCollect(int ID);
        Paper getPaperById(int paperId,int infoId);
}
