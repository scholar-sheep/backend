package sheep.portal.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.portal.entity.PortalAndUser;
import sheep.portal.exception.AdoptFailException;
import sheep.portal.exception.NoPortalException;
import sheep.portal.mapper.PortalAndUserMapper;
import sheep.portal.mapper.PortalMapper;
import sheep.portal.entity.Portal;

@Service
public class PortalServiceImp implements PortalService{
    @Autowired
    PortalMapper portalMapper;

    @Autowired
    PortalAndUserMapper portalAndUserMapper;

    /**
     * 创建门户，创建成功返回门户详情
     * @param portal
     * @return
     */
    public void addPortal(int user_id, Portal portal){
        //该用户有没有认领过门户
        int count = portalAndUserMapper.selectCount(new QueryWrapper<PortalAndUser>().eq("user_id", user_id));
        if(count > 0) throw new AdoptFailException();
        //mysql创建门户信息
        portalMapper.addPortal(portal);
    }

    /**
     * 根据门户ID删除门户
     * @param id
     * @return
     */
    public int deleteById(int id){
        //有没有该门户，没有则返回找不到门户错误
        int count = portalMapper.selectCount(new QueryWrapper<Portal>().eq("id", id));
        if(count != 1) throw new NoPortalException();
        //有则正常删除
        return portalMapper.deleteById(id);
    }

    /**
     * 更新门户信息
     * @param portal
     * @return
     */
    public int update(int id, Portal portal){
        //有没有该门户，没有则返回找不到门户错误
        int count = portalMapper.selectCount(new QueryWrapper<Portal>().eq("id", id));
        if(count != 1) throw new NoPortalException();
        //有则正常更新
        Portal last = portalMapper.selectById(id);
        portal.setId(last.getId());
        if(portal.getName() == null) portal.setName(last.getName());
        if(portal.getNormalizedName() == null) portal.setNormalizedName(last.getNormalizedName());
        if(portal.getPosition() == null) portal.setPosition(last.getPosition());
        if(portal.getHIndex() == 0) portal.setHIndex(last.getHIndex());
        return portalMapper.updateById(portal);
    }

    /**
     * 根据门户ID查找门户
     * @param id
     * @return
     */
    public Portal selectById(int id) {
        //有没有该门户，没有则返回找不到门户错误
        int count = portalMapper.selectCount(new QueryWrapper<Portal>().eq("id", id));
        if(count != 1) throw new NoPortalException();
        //有则正常返回
        return portalMapper.selectById(id);
    }


    /**
     * 认领门户，传入被认领的门户id和申请认领的用户id
     * @param portal_id
     * @param user_id
     * @return
     */
    public void adoptPortal (int portal_id, int user_id){
        //该门户有没有被认领过
        int count1 = portalAndUserMapper.selectCount(new QueryWrapper<PortalAndUser>().eq("portal_id", portal_id));
        //该用户有没有认领过门户
        int count2 = portalAndUserMapper.selectCount(new QueryWrapper<PortalAndUser>().eq("user_id", user_id));
        //如果以上两种情况有一个发生，则认领失败
        if(count1 > 0 || count2 > 0) throw new AdoptFailException();
        portalAndUserMapper.adoptPortal(portal_id, user_id);
    }

    /**
     * 取消认领门户，传入被取消门户的id
     * @param portal_id
     */
    public void unadoptPortal(int portal_id){
        //该门户有没有被认领过
        int count = portalAndUserMapper.selectCount(new QueryWrapper<PortalAndUser>().eq("portal_id", portal_id));
        if(count != 1) throw new AdoptFailException();
        portalAndUserMapper.unadoptPortal(portal_id);
    }

    /**
     * 为了创建门户时同时插入es和mysql，先插入mysql后要获得自动生成的id，再将该id插入es
     * @return
     */
    public int getLastInsertUserID(){
        return portalMapper.getLastInsertUserID();
    }

}
