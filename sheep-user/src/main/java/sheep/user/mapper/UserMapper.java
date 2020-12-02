package sheep.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sheep.user.entity.User;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}
