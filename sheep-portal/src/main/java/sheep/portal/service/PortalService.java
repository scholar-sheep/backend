package sheep.portal.service;

import org.springframework.stereotype.Service;
import sheep.portal.entity.Portal;
import sheep.portal.entity.PortalAndUser;

@Service
public interface PortalService {
    Portal selectById(int id);
    int deleteById(int id);
    int update(int id, Portal portal);
    Portal addPortal(Portal portal);
    Portal adoptPortal(int portal_id, int user_id);
}