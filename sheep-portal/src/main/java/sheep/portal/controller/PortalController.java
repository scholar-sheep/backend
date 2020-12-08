package sheep.portal.controller;

import org.elasticsearch.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sheep.common.exception.ErrorType;
import sheep.common.utils.ResultDTO;
import sheep.portal.entity.EsPortal;
import sheep.portal.entity.Portal;
import sheep.portal.pojo.PaperList;
import sheep.portal.pojo.PaperParam;
import sheep.portal.pojo.WholePortal;
import sheep.portal.exception.AdoptFailException;
import sheep.portal.exception.CreatePortalException;
import sheep.portal.exception.NoPortalException;
import sheep.portal.service.EsPortalService;
import sheep.portal.service.PortalService;

import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
    public Object register(@PathVariable("user_id") int user_id, @RequestBody WholePortal wholePortal) {
        try{
            System.out.println(wholePortal.toString());
            //将wholeportal拆解到mysql里的portal和es里的esportal
            Portal portal = new Portal(wholePortal);
            EsPortal esPortal = new EsPortal(wholePortal);
            //将mysql的插入数据
            portalService.addPortal(user_id, portal);
            //获取刚刚存储进mysql的自动生成的id
            //String portal_id = portalService.getLastInsertUserID();
            //mysql的portalAndUser表插入认领信息（自己创建默认认领）
            portalService.adoptPortal(portal.getId(), user_id);
            //将es里的插入数据
            esPortal.setId(portal.getId());
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

    @RequestMapping(value = "/portal/admin/{id}", method = RequestMethod.DELETE)
    public Object deletePortal(@PathVariable("id") String id){
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
        catch (IOException e){
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
    public Object updatePortal(@PathVariable("id") String id,@RequestBody WholePortal wholePortal){
        try{
            System.out.println(wholePortal.toString());
            Portal portal = new Portal(wholePortal);
            System.out.println(portal.getPosition());
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
    public Object getInformation(@PathVariable("id") String id,@RequestParam(required = false,value = "page_num")Integer page_num,
                                 @RequestParam(required = false,value = "sort") String sort) {
        try{
            Portal portal = portalService.selectById(id);
            EsPortal esPortal = esPortalService.getInformation(id);
            PaperList paperList=esPortalService.getPaperList(id,sort,page_num);
            WholePortal wholePortal = new WholePortal(portal, esPortal,paperList);
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
    public Object adoptPortal(@PathVariable("portal_id") String portal_id, @RequestParam("user_id") int user_id){
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
    public Object unadoptPortal(@PathVariable("portal_id") String portal_id){
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
    @GetMapping(value = "/portal/addPaper/{portal_id}")
    public Object addPaper(@PathVariable("portal_id")String portal_id,@RequestParam("paper_id")String paper_id) {
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
    @DeleteMapping(value = "/portal/deletePaper/{portal_id}")
        public Object delPaper(@PathVariable("portal_id")String portal_id,@RequestParam("paper_id")String paper_id) {
        try
        {
            int result=esPortalService.deletePaper(portal_id, paper_id);
            if(result==1) return ResultDTO.okOf();
            else return ResultDTO.errorOf(ErrorType.DELETE_PAPER_ERROR);
        }
        catch(IOException e)
        {
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
    }
    @PostMapping(value = "/portal/createPaper/{portal_id}")
    public Object createPaper(@PathVariable("portal_id")String portal_id, @RequestBody PaperParam paperParam){
        int result=esPortalService.createPaper(portal_id, paperParam);
        if(result==1) return ResultDTO.okOf();
        else return ResultDTO.errorOf(ErrorType.CREATE_PAPER_ERROR); }


    /**
     * 根据用户id查看门户
     * @param user_id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/myPortal/{user_id}", method = RequestMethod.GET)
    public Object getPortal(@PathVariable("user_id") int user_id,@RequestParam(required = false,value = "page_num")Integer page_num,
                                 @RequestParam(required = false,value = "sort") String sort) {
        try{
            String portal_id=portalService.findPortalByUserId(user_id);
            return this.getInformation(portal_id,page_num,sort);
        }
        catch(NoPortalException noPortalException){
            return ResultDTO.errorOf(ErrorType.PORTAL_ERROR);
        }
    }
}