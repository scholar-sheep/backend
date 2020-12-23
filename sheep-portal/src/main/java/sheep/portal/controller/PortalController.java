package sheep.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sheep.common.exception.ErrorType;
import sheep.portal.exception.FollowFailException;
import sheep.portal.pojo.PaperModel;
import sheep.portal.util.*;
import sheep.common.utils.ResultDTO;
import sheep.portal.entity.EsPortal;
import sheep.portal.pojo.PaperParam;
import sheep.portal.pojo.WholePortal;
import sheep.portal.exception.AdoptFailException;
import sheep.portal.exception.CreatePortalException;
import sheep.portal.exception.NoPortalException;
import sheep.portal.service.EsPortalService;
import sheep.portal.service.PortalService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PortalController {
    @Autowired
    PortalService portalService;

    @Autowired
    EsPortalService esPortalService;


    /**
     * POST创建门户
     * @return
     */
    @RequestMapping(value = "/portal/register", method = RequestMethod.POST)
    @LoginRequired
    public Object register(@RequestBody EsPortal esPortal) {
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            int user_id=Integer.parseInt(request.getHeader("X-UserId"));
            esPortalService.save(esPortal);
            portalService.adoptPortal(esPortal.getId(), user_id);
        }
        catch(CreatePortalException createPortalException){
            return ResultDTO.errorOf(ErrorType.CREATE_PORTAL_ERROR);
        }
        catch (AdoptFailException adoptFailException){
            return ResultDTO.errorOf(ErrorType.CREATE_PORTAL_ERROR);
        }
        return ResultDTO.okOf(esPortal.getId());
    }

    /**
     * 管理员删除门户
     * @param id
     * @return
     */
    @RequestMapping(value = "/portal/admin", method = RequestMethod.DELETE)
    @LoginRequired
    @Permissions(role="isAdmin")
    public Object deletePortal(@RequestParam(value ="id") String id){
        try{
            //删除es里的信息
            System.out.println(esPortalService.deleteByID(id));

            //删除PortalAndUser表里的认领信息
            portalService.unadoptPortal(id);
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
     * 更新门户信息
     * @param esPortal
     * @return
     */
    @RequestMapping(value="/portal/admin", method = RequestMethod.PUT)
    @LoginRequired
    @Permissions(role="isOwnerOrAdmin")
    public Object updatePortal(@RequestParam(value="portal_id") String id, @RequestBody EsPortal esPortal){
        try{
            System.out.println(esPortal);
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
    @RequestMapping(value = "/portal", method = RequestMethod.GET)
    public Object getInformation(@RequestParam(value = "sort",required = false) String sort,
                                 @RequestParam(value= "id") String id) {
        try{
            EsPortal esPortal = esPortalService.getInformation(id);
            List<PaperModel> paperList = esPortalService.getPaperList(id, sort);
            WholePortal wholePortal = new WholePortal(esPortal,paperList);
            return ResultDTO.okOf(wholePortal);
        }
        catch(NoPortalException noPortalException){
            noPortalException.printStackTrace();
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
        catch(IOException e){
            e.printStackTrace();
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
    }

    /**
     * 认领门户，路径参数为门户id，查询参数为当前user_id
     * @return
     */
    @RequestMapping(value = "/portal/apply", method = RequestMethod.POST)
    @LoginRequired
    public Object adoptPortal(@RequestParam(value = "portal_id") String portal_id){
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            int user_id=Integer.parseInt(request.getHeader("X-UserId"));
//            int user_id = 1;
            portalService.adoptPortal(portal_id, user_id);
        }
        catch(AdoptFailException adoptFailException){
            return ResultDTO.errorOf(ErrorType.ADOPT_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * 取消认领门户，路径参数为门户id
     * @return
     */
    @RequestMapping(value = "/portal/unapply", method = RequestMethod.DELETE)
    @LoginRequired
    @Permissions(role="isOwnerOrAdmin")
    public Object unadoptPortal(@RequestParam(value = "portal_id") String portal_id){
        try{
            portalService.unadoptPortal(portal_id);
        }
        catch (AdoptFailException adoptFailException){
            return ResultDTO.errorOf(ErrorType.ADOPT_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * 添加论文，路径参数为门户id 查询参数为论文id
     * @param portal_id
     * @param paper_id
     * @return
     */
    @PostMapping(value = "/portal/addPaper")
    @Permissions(role="isOwnerOrAdmin")
    public Object addPaper(@RequestParam(value="portal_id") String portal_id,@RequestParam(value="paper_id") String paper_id) {
       try
       {
           int result=esPortalService.addPaper(portal_id, paper_id);
           if(result==1) return ResultDTO.okOf();
           else if(result==2) return ResultDTO.errorOf(ErrorType.ADD_REPEAT_ERROR);
           else return ResultDTO.errorOf((ErrorType.ADD_PAPER_ERROR));
       }
       catch(IOException e)
       {
           return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
       }
    }

    /**
     * 删除论文，路径参数为门户id 查询参数为论文id
     * @param portal_id
     * @param paper_id
     * @return
     */
    @DeleteMapping(value = "/portal/deletePaper")
    @Permissions(role="isOwnerOrAdmin")
    public Object delPaper(@RequestParam(value="portal_id") String portal_id,@RequestParam(value="paper_id") String paper_id) {
        try
        {
            int result = esPortalService.deletePaper(portal_id, paper_id);
            if(result == 1) return ResultDTO.okOf();
            else return ResultDTO.errorOf(ErrorType.DELETE_PAPER_ERROR);
        }
        catch(IOException e)
        {
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
    }

    /**
     * 创建论文
     * @param portal_id
     * @return
     */
    @PostMapping(value = "/portal/createPaper")
    @Permissions(role="isOwnerOrAdmin")
    public Object createPaper(@RequestParam(value="portal_id") String portal_id, @RequestBody PaperParam paperParam) throws IOException {
        System.out.println(paperParam);
        int result = esPortalService.createPaper(portal_id, paperParam);
        if(result == 1) return ResultDTO.okOf();
        else return ResultDTO.errorOf(ErrorType.CREATE_PAPER_ERROR);
    }

    /**
     * 查看我的门户
     *
     * @return
     */
    @RequestMapping(value = "/myPortal/", method = RequestMethod.GET)
    @LoginRequired
    public Object getPortal(@RequestParam(required = false,value = "sort") String sort) {
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            int user_id=Integer.parseInt(request.getHeader("X-UserId"));
            String portal_id = portalService.findPortalByUserId(user_id);
            return this.getInformation(sort,portal_id);
        }
        catch(NoPortalException noPortalException){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
    }

    /**
     * 用户关注学者
     * @param portal_id
     * @return
     */
    @RequestMapping(value = "/portal/follow", method = RequestMethod.POST)
    @LoginRequired
    public Object follow(@RequestParam(value = "portal_id") String portal_id){
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            int user_id = Integer.parseInt(request.getHeader("X-UserId"));
//            int user_id = 3;
            portalService.follow(portal_id, user_id);
        }
        catch(FollowFailException e){
            return ResultDTO.errorOf(ErrorType.FOLLOW_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * 用户取消关注学者
     * @param portal_id
     * @return
     */
    @RequestMapping(value = "/portal/unfollow", method = RequestMethod.DELETE)
    @LoginRequired
    public Object unfollow(@RequestParam(value = "portal_id") String portal_id){
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            int user_id = Integer.parseInt(request.getHeader("X-UserId"));
//            int user_id = 2;
            portalService.unfollow(portal_id, user_id);
        }
        catch (Exception e){
            return ResultDTO.errorOf(ErrorType.FOLLOW_ERROR);
        }
        return ResultDTO.okOf();
    }

    /**
     * 查询门户被关注数
     * @param portal_id
     * @return
     */
    @RequestMapping(value = "/portal/followNum", method = RequestMethod.GET)
    @LoginRequired
    public Object followNum(@RequestParam(value = "portal_id") String portal_id){
        return ResultDTO.okOf(portalService.followNum(portal_id));
    }

    /**
     * 获取当前用户的关注列表
     * @return
     */
    @RequestMapping(value = "/portal/followList", method = RequestMethod.GET)
    @LoginRequired
    public Object followList(){
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            int user_id = Integer.parseInt(request.getHeader("X-UserId"));
            List<String> idList = portalService.followList(user_id);
            List<EsPortal> followList = new ArrayList<>();
            for(String id : idList){
                followList.add(esPortalService.getInformation(id));
            }
            return ResultDTO.okOf(followList);
        }
        catch(IOException e){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
    }

    /**
     * 判断当前用户与该学者的关系
     * @param portal_id
     * @return 关注关系： ；认领关系： ；没关系：；
     */
    @RequestMapping(value = "/portal/relationship", method = RequestMethod.GET)
    @LoginRequired
    public Object userAndPortalRelationship(@RequestParam(value = "portal_id") String portal_id){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        int user_id = Integer.parseInt(request.getHeader("X-UserId"));
//        int user_id = 2;
        if(portalService.isAdopt(portal_id, user_id) == 1)
            return ResultDTO.okOf("认领关系");
        else if(portalService.isFollow(portal_id, user_id) == 1)
            return ResultDTO.okOf("关注关系");
        else
            return ResultDTO.okOf("没关系");
    }

    /**
     * 查询当前用户是否已经有门户了
     * @return
     */
    @RequestMapping(value = "/portal/hasPortalOrNot", method = RequestMethod.GET)
    @LoginRequired
    public Object findPortalByUserId(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        int user_id = Integer.parseInt(request.getHeader("X-UserId"));
        try{
            String portal_id = portalService.findPortalByUserId(user_id);
            return ResultDTO.okOf(portal_id);
        }
        catch (NoPortalException e){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
    }

}
