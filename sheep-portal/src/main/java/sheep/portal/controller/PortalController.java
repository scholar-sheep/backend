package sheep.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sheep.common.exception.ErrorType;
import sheep.common.utils.ResultDTO;
import sheep.portal.entity.Portal;
import sheep.portal.exception.AdoptFailException;
import sheep.portal.exception.CreatePortalException;
import sheep.portal.exception.NoPortalException;
import sheep.portal.service.PortalService;

import javax.annotation.Resource;

@RestController
public class PortalController {
    @Autowired
    PortalService portalService;

    /**
     * POST创建门户，返回1
     * @return
     */
    @RequestMapping(value = "/expert/register", method = RequestMethod.POST)
    public Object register(Portal portal) {
        try{
            portalService.addPortal(portal);
        }
        catch(CreatePortalException createPortalException){
            return ResultDTO.errorOf(ErrorType.CREATE_PORTAL_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * DELETE删除指定ID的门户并返回该门户信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/expert/admin/{id}", method = RequestMethod.DELETE)
    public Object deletePortal(@PathVariable("id") int id){
        try{
            portalService.deleteById(id);
        }
        catch(NoPortalException noPortalException){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * PUT更新门户信息，返回更新后的信息
     * @param portal
     * @return
     */
    @RequestMapping(value="/expert/admin/{id}", method = RequestMethod.PUT)
    public Object updatePortal(@PathVariable("id") int id, Portal portal){
        try{
            portalService.update(id, portal);
        }
        catch(NoPortalException noPortalException){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * GET根据门户id查看门户详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/expert/{id}", method = RequestMethod.GET)
    public Object getInformation(@PathVariable("id") int id) {
        try{
            Portal portal = portalService.selectById(id);
        }
        catch(NoPortalException noPortalException){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * 认领门户，路径参数为门户id，查询参数为当前user_id
     * @param portal_id
     * @param user_id
     * @return
     */
    @RequestMapping(value = "/expert/apply/{portal_id}")
    public Object adoptPortal(@PathVariable("portal_id") int portal_id, @RequestParam("user_id") int user_id){
        try{
            portalService.adoptPortal(portal_id, user_id);
        }
        catch(AdoptFailException adoptFailException){
            return ResultDTO.errorOf(ErrorType.ADOPT_ERROR);
        }
        return ResultDTO.okOf();
    }
}
