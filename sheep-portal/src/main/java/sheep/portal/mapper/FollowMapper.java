package sheep.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import sheep.portal.entity.Follow;

@Mapper
@Repository
public interface FollowMapper extends BaseMapper<Follow> {
    @Insert("insert into Follow(portal_id, user_id) values (#{portal_id}, #{user_id})")
    int follow(String portal_id, int user_id);

    @Delete("delete from Follow where portal_id = #{portal_id} and user_id = #{user_id}")
    int unfollow(String portal_id, int user_id);

//    @Select("select* from  Follow where portal_id = #{portal_id} and user_id = #{user_id}")
//    int isFollow(String portal_id, int user_id);
}
