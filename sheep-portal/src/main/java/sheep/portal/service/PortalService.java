package sheep.portal.service;

import org.springframework.stereotype.Service;
import sheep.portal.entity.EsPortal;
import sheep.portal.entity.Message;

import java.util.List;
import java.util.Map;

@Service
public interface PortalService {
    String findPortalByUserId(int user_id);
    void adoptPortal(String portal_id, int user_id);
    void unadoptPortal(String portal_id);
    int isAdopt(String portal_id, int user_id);

    void follow(String portal_id, int user_id);
    void unfollow(String portal_id, int user_id);
    int isFollow(String portal_id, int user_id);
    int followNum(String portal_id);

    List<String> followList(int user_id);

    //消息功能
    List<Integer> peopleList(int this_user_id);
    List<Message> messageList(int this_user_id, int that_user_id);
    void sendMessage(Message message);
    void readMessage(int this_user_id, int that_user_id);
    void deleteDislog(int this_user_id, int that_user_id);
}