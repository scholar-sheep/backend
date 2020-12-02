package sheep.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import sheep.portal.entity.Portal;
import sheep.portal.entity.PortalAndUser;

@Mapper
@Repository
public interface PortalAndUserMapper extends BaseMapper<PortalAndUser> {
    @Insert("insert into PortalAndUser(portal_id, user_id) values (#{portal_id}, #{user_id})")
    int adoptPortal(int portal_id, int user_id);
}
