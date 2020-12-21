package sheep.portal.service;

import org.springframework.stereotype.Service;
import sheep.portal.entity.Portal;
import sheep.portal.entity.PortalAndUser;

@Service
public interface PortalService {
    Portal selectById(String id);
    int deleteById(String id);
    int update(String id, Portal portal);
    String findPortalByUserId(int user_id);
    void addPortal(int user_id, Portal portal);
    void adoptPortal(String portal_id, int user_id);
    void unadoptPortal(String portal_id);
    int isAdopt(String portal_id, int user_id);

    void follow(String portal_id, int user_id);
    void unfollow(String portal_id, int user_id);
    int isFollow(String portal_id, int user_id);
    int followNum(String portal_id);
}