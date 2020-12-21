package sheep.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import sheep.portal.entity.Portal;

@Mapper
@Repository
public interface PortalMapper extends BaseMapper<Portal>{
    /**
     * 创建门户，目前需要姓名，规范格式姓名，职称，所属单位
     * @param portal
     * @return
     */
    @Insert("insert into portal(id, name, normalized_name, position, h_index) values (#{id},#{name}, #{normalizedName}, #{position}, #{hIndex})")
    int addPortal(Portal portal);


}
