package sheep.portal.service;

import org.springframework.stereotype.Service;

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
}