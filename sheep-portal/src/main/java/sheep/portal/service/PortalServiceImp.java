package sheep.portal.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.redisson.api.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.portal.entity.EsPortal;
import sheep.portal.entity.Follow;
import sheep.portal.entity.Message;
import sheep.portal.entity.PortalAndUser;
import sheep.portal.exception.AdoptFailException;
import sheep.portal.exception.FollowFailException;
import sheep.portal.exception.NoPortalException;
import sheep.portal.mapper.FollowMapper;
import sheep.portal.mapper.MessageMapper;
import sheep.portal.mapper.PortalAndUserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortalServiceImp implements PortalService{

    @Autowired
    PortalAndUserMapper portalAndUserMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    MessageMapper messageMapper;


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
//        int count = followMapper.isFollow(portal_id, user_id);
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

    /**
     * 获取用户的关注列表
     * @param user_id
     * @return
     */
    public List<String> followList(int user_id){
        return followMapper.followList(user_id);
    }



    /**
     * 根据当前用户ID获取和他聊天的所有人的ID
     * @param this_user_id
     * @return
     */
    public List<Integer> peopleList(int this_user_id){
        List<Integer> list1 = messageMapper.peopleList1(this_user_id);
        List<Integer> list2 = messageMapper.peopleList2(this_user_id);
        list1.removeAll(list2);
        list1.addAll(list2);
        return list1;
    }

    /**
     * 根据当前用户ID和指定用户id获取他们的聊天记录
     * @param this_user_id
     * @param that_user_id
     * @return
     */
    public List<Message> messageList(int this_user_id, int that_user_id){
        List<Message> messageList = messageMapper.messageList(this_user_id, that_user_id);
        return messageList;
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(Message message){
        messageMapper.sendMessage(message);
    }

    /**
     * 已读消息
     * @param this_user_id
     * @param that_user_id
     */
    public void readMessage(int this_user_id, int that_user_id){
        messageMapper.readMessage(this_user_id, that_user_id);
    }

    /**
     * 删除对话框
     * @param this_user_id
     * @param that_user_id
     */
    public void deleteDislog(int this_user_id, int that_user_id){
        messageMapper.deleteDislog(this_user_id, that_user_id);
    }

}
