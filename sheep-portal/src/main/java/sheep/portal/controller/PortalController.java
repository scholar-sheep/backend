package sheep.portal.controller;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sheep.common.exception.ErrorType;
import sheep.common.utils.ResultDTO;
import sheep.portal.entity.EsPortal;
import sheep.portal.entity.Portal;
import sheep.portal.entity.WholePortal;
import sheep.portal.exception.AdoptFailException;
import sheep.portal.exception.CreatePortalException;
import sheep.portal.exception.NoPortalException;
import sheep.portal.service.EsPortalService;
import sheep.portal.service.PortalService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;

@RestController
public class PortalController {
    @Autowired
    PortalService portalService;

    @Autowired
    EsPortalService esPortalService;


    /**
     * POST创建门户，路径参数为用户id
     * @return
     */
    @RequestMapping(value = "/portal/register/{user_id}", method = RequestMethod.POST)
    public Object register(@PathVariable("user_id") int user_id, WholePortal wholePortal) {
        try{
            System.out.println(wholePortal.gethIndex());
            //将wholeportal拆解到mysql里的portal和es里的esportal
            Portal portal = new Portal(wholePortal);
            EsPortal esPortal = new EsPortal(wholePortal);
            //将mysql的插入数据
            portalService.addPortal(user_id, portal);
            //获取刚刚存储进mysql的自动生成的id
            int portal_id = portalService.getLastInsertUserID();
            //mysql的portalAndUser表插入认领信息（自己创建默认认领）
            portalService.adoptPortal(portal_id, user_id);
            //将es里的插入数据
            esPortal.setId(portal_id);
            esPortalService.save(esPortal);
        }
        catch(CreatePortalException createPortalException){
            return ResultDTO.errorOf(ErrorType.CREATE_PORTAL_ERROR);
        }
        catch (AdoptFailException adoptFailException){
            return ResultDTO.errorOf(ErrorType.CREATE_PORTAL_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * DELETE删除指定ID的门户
     * @param id
     * @return
     */
    @RequestMapping(value = "/portal/admin/{id}", method = RequestMethod.DELETE)
    public Object deletePortal(@PathVariable("id") int id){
        try{
            //删除mysql里的信息
            portalService.deleteById(id);
            //删除es里的信息
            esPortalService.deleteByID(id);
            //删除PortalAndUser表里的认领信息
            portalService.unadoptPortal(id);
        }
        catch(NoPortalException noPortalException){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * PUT更新门户信息，返回更新后的信息
     * @param id
     * @param wholePortal
     * @return
     */
    @RequestMapping(value="/portal/admin/{id}", method = RequestMethod.PUT)
    public Object updatePortal(@PathVariable("id") int id, WholePortal wholePortal){
        try{
            Portal portal = new Portal(wholePortal);
            EsPortal esPortal = new EsPortal(wholePortal);
            //更新mysql部分
            portalService.update(id, portal);
            //更新es部分
            esPortalService.update(id, esPortal);
        }
        catch(NoPortalException noPortalException){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
        catch (IOException e){
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
    @RequestMapping(value = "/portal/{id}", method = RequestMethod.GET)
    public Object getInformation(@PathVariable("id") int id) {
        try{
            Portal portal = portalService.selectById(id);
            EsPortal esPortal = esPortalService.getInformation(id);
            WholePortal wholePortal = new WholePortal(portal, esPortal);
            return ResultDTO.okOf(wholePortal);
        }
        catch(NoPortalException noPortalException){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
        catch(IOException e){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
    }

    /**
     * 认领门户，路径参数为门户id，查询参数为当前user_id
     * @param portal_id
     * @param user_id
     * @return
     */
    @RequestMapping(value = "/portal/apply/{portal_id}", method = RequestMethod.POST)
    public Object adoptPortal(@PathVariable("portal_id") int portal_id, @RequestParam("user_id") int user_id){
        try{
            portalService.adoptPortal(portal_id, user_id);
        }
        catch(AdoptFailException adoptFailException){
            return ResultDTO.errorOf(ErrorType.ADOPT_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * 取消认领门户，路径参数为门户id
     * @param portal_id
     * @return
     */
    @RequestMapping(value = "/portal/unapply/{portal_id}", method = RequestMethod.DELETE)
    public Object unadoptPortal(@PathVariable("portal_id") int portal_id){
        try{
            portalService.unadoptPortal(portal_id);
        }
        catch (AdoptFailException adoptFailException){
            return ResultDTO.errorOf(ErrorType.ADOPT_ERROR);
        }
        return ResultDTO.okOf();
    }
}
