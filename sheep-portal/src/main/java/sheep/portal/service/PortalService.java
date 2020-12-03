package sheep.portal.service;

import org.springframework.stereotype.Service;
import sheep.portal.entity.Portal;
import sheep.portal.entity.PortalAndUser;

@Service
public interface PortalService {
    Portal selectById(int id);
    int deleteById(int id);
    int update(int id, Portal portal);
    void addPortal(int user_id, Portal portal);
    void adoptPortal(int portal_id, int user_id);
    void unadoptPortal(int portal_id);
    int getLastInsertUserID();

}