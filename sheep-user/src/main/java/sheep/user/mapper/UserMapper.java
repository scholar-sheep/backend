package sheep.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;
import sheep.user.entity.User;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user")
    List<User> getUserList();

    @Select("select * from user where ID = #{ID}")
    User getUserById(int ID);

    @Update("update user set username = #{username},usertype = #{usertype},head = #{head},password = #{password},email = #{email},note = #{note} where ID = #{ID}")
    int updateUserInfo(User user);

    @Insert("insert into user(username,password,usertype) values (#{username},#{password},#{usertype})")
    int addUser(User user);

    @Select("select * from user where username = #{username}")
    User getUserByName(String username);
}
