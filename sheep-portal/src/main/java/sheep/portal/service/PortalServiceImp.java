package sheep.portal.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.portal.entity.Follow;
import sheep.portal.entity.PortalAndUser;
import sheep.portal.exception.AdoptFailException;
import sheep.portal.exception.FollowFailException;
import sheep.portal.exception.NoPortalException;
import sheep.portal.mapper.FollowMapper;
import sheep.portal.mapper.PortalAndUserMapper;
import sheep.portal.mapper.PortalMapper;
import sheep.portal.entity.Portal;

@Service
public class PortalServiceImp implements PortalService{
    @Autowired
    PortalMapper portalMapper;

    @Autowired
    PortalAndUserMapper portalAndUserMapper;

    @Autowired
    FollowMapper followMapper;

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
    public int deleteById(String id){
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
    public int update(String id, Portal portal){
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
    public Portal selectById(String id) {
        //有没有该门户，没有则返回找不到门户错误
        int count = portalMapper.selectCount(new QueryWrapper<Portal>().eq("id", id));
        if(count != 1) throw new NoPortalException();
        return portalMapper.selectById(id);
    }


    /**
     * 认领门户，传入被认领的门户id和申请认领的用户id
     * @param portal_id
     * @param user_id
     * @return
     */
    public void adoptPortal (String portal_id, int user_id){
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
    public void unadoptPortal(String portal_id){
        //该门户有没有被认领过
        int count = portalAndUserMapper.selectCount(new QueryWrapper<PortalAndUser>().eq("portal_id", portal_id));
        if(count != 1) throw new AdoptFailException();
        portalAndUserMapper.unadoptPortal(portal_id);
    }

    /**
     * 查询门户是否被用户认领
     * @param portal_id
     * @param user_id
     * @return 0：没有被认领；1：被正常认领
     */
    public int isAdopt(String portal_id, int user_id){
        int count = portalAndUserMapper.selectCount(new QueryWrapper<PortalAndUser>().eq("portal_id", portal_id).eq("user_id", user_id));
        if(count == 0) return 0;
        else return count;
    }

    /**
     * 根据用户id查找门户id
     * @param user_id
     */
    public String findPortalByUserId(int user_id)
    {
        int count = portalAndUserMapper.selectCount(new QueryWrapper<PortalAndUser>().eq("user_id", user_id));
        if(count != 1) throw new NoPortalException();
      return portalAndUserMapper.findByUser_id(user_id);
    }

    /**
     * 用户关注学者
     * @param portal_id
     * @param user_id
     */
    public void follow(String portal_id, int user_id){
        if(isFollow(portal_id, user_id) != 0 || isAdopt(portal_id, user_id) != 0)
            throw new FollowFailException();
        followMapper.follow(portal_id, user_id);
    }

    /**
     * 用户取消关注学者
     * @param portal_id
     * @param user_id
     */
    public void unfollow(String portal_id, int user_id){
        if(isFollow(portal_id, user_id) != 1)
            throw new FollowFailException();
        followMapper.unfollow(portal_id, user_id);
    }

    /**
     * 查询该用户是否已关注该学者
     * @param portal_id
     * @param user_id
     * @return 0：没有关注；1：正常关注
     */
    public int isFollow(String portal_id, int user_id){
        int count = followMapper.selectCount(new QueryWrapper<Follow>().eq("portal_id", portal_id).eq("user_id", user_id));
        if(count == 0) return 0;
        else return count;
    }

    /**
     * 查询门户被关注次数
     * @param portal_id
     * @return
     */
    public int followNum(String portal_id){
        return followMapper.followNum(portal_id);
    }

}
