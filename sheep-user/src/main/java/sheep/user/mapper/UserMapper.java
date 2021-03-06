package sheep.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;
import sheep.user.entity.MobileCode;
import sheep.user.entity.Paper;
import sheep.user.entity.User;
import sheep.user.entity.UserCollect;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user")
    List<User> getUserList();

    @Select("select * from user where ID = #{ID}")
    User getUserById(int ID);

    @Update("update user set username = #{username},usertype = #{usertype},mobile = #{mobile},email = #{email},note = #{note},sex = #{sex},birthday = #{birthday} where ID = #{ID}")
    int updateUserInfo(User user);

    @Update("update user set password = #{password} where ID = #{ID}")
    int updateUserPassword(int ID,String password);

    @Insert("insert into user(username,password,mobile) values (#{username},#{password},#{mobile})")
    int addUser(User user);

    @Select("select * from user where username = #{username}")
    User getUserByName(String username);

    @Select("select count(*) from user where mobile = #{mobile}")
    int getUserByMobile(String mobile);

    @Select("select * from user where mobile = #{tel}")
    User getUserByTel(String tel);

    @Insert("insert into follow(currentID,followID) values (#{currentID},#{followID})")
    int follow(int currentID,int followID);

    @Select("select followID from follow where currentID = #{ID}")
    List<Integer> getFollow(int ID);

    @Update("update user set avatar=#{avatar} where ID=#{ID}")
    int changeAvatar(int ID,String avatar);

    @Insert("insert into mobileCode(mobile,code) values (#{mobile},#{code})")
    int insertUserCode(String mobile,String code);

//    @Select("select code from mobileCode where mobile = #{mobile}")
//    Object getCodeByMobile(String mobile);

    @Insert("insert into collect(userId,paperId,infoId) values (#{userId},#{paperId},#{infoId})")
    int collect(int userId,int paperId,int infoId);

    @Select("select * from collect where userId = #{ID}")
    List<UserCollect> getCollect(int ID);

    @Select("select * from paper_info where paper_id = #{paperId} and info_id = #{infoId}")
    Paper getPaperById(int paperId,int infoId);

    //12.20
    @Update("update mobileCode set code = #{code} where mobile = #{mobile}")
    int updateUserCode(String mobile,String code);

    @Select("select count(*) from mobileCode where mobile = #{mobile}")
    int getCodeByMobile(String mobile);

    @Select("select code from mobileCode where mobile = #{mobile}")
    String getCodeSaved(String mobile);
}
