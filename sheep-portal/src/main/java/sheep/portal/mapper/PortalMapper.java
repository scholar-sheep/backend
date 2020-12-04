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
    @Insert("insert into portal(name, normalized_name, position, h_index) values (#{name}, #{normalizedName}, #{position}, #{hIndex})")
    int addPortal(Portal portal);

    //SELECT MAX(id) FROM table 该方法在多线程等情况下可能会造成不正确。
    //SELECT LAST_INSERT_ID() 这两个都是单个连接的，不存在所谓的两个人都同时插入，分不清的问题
    @Select("select MAX(id) FROM portal")
    int getLastInsertUserID();

//    @Update("update portal set name = #{name}, normalized_name = #{normalizedName}, position = #{position}, h_index = #{hIndex} WHERE id=#{id}")
//    int updateById(Portal portal);

}
