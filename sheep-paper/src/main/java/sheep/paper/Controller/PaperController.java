package sheep.paper.Controller;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sheep.common.exception.ErrorType;
import sheep.common.utils.ResultDTO;
import sheep.paper.Entity.Author;
import sheep.paper.Entity.BriefPaperInfo;
import sheep.paper.Entity.Favorite;
import sheep.paper.Entity.Paper;
import sheep.paper.Service.PaperService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/paper/basic")
public class PaperController {

    @Autowired
    private PaperService paperService;

    /*
     *
     * @Description 根据 Paper ID 获取论文信息
     * @Param [paperIdStr]
     * @return java.lang.Object
     **/
    @GetMapping("/info/{paperIdStr}")
    public Object getEsInfoById(@PathVariable String paperIdStr, HttpServletRequest request) {
        Map map = paperService.getEsInfoById(paperIdStr);
        Paper paper = paperService.makeUpPaperInfoWithOutFavorInfo(map, paperIdStr);
        if (map==null) {
            return ResultDTO.errorOf(ErrorType.PAPER_NOT_EXIST_ERROR);
        }
        else {
            int userId = Integer.parseInt(request.getHeader("X-UserId"));
            paper.setFavored(paperService.checkFavor(userId, paperIdStr));
            return ResultDTO.okOf(paper);
        }
    }

    /*
     *
     * @Description 获取用于展示列表的简略信息
     * @Param [paperIdStr]
     * @return java.lang.Object
     **/
    @GetMapping("/info/brief/{paperId}")
    public Object getBriefInfoById(@PathVariable String paperId, HttpServletRequest request) {
        int userId;
        try {
            userId = Integer.parseInt(request.getHeader("X-UserId"));
        } catch (Exception e) {
            return ResultDTO.errorOf(ErrorType.USER_ID_ILLEGAL_ERROR);
        }
        BriefPaperInfo paperInfo = paperService.getBriefPaperInfoById(paperId, userId);
        return ResultDTO.okOf(paperInfo);
    }

    /*
     *
     * @Description 收藏学术成果
     * @Param [userId, paperId, infoId]
     * @return java.lang.Object
     **/
    @PostMapping(value = "/favor")
    public Object collect(@RequestBody String paperId, HttpServletRequest request){
        int userId;
        try {
            userId = Integer.parseInt(request.getHeader("X-UserId"));
        } catch (Exception e) {
            return ResultDTO.errorOf(ErrorType.USER_ID_ILLEGAL_ERROR);
        }
        if(paperService.favor(userId, paperId))
            return ResultDTO.okOf();
        else return ResultDTO.errorOf(ErrorType.FAVOR_ERROR);
    }

    /*
     *
     * @Description 取消收藏学术成果
     * @Param [paperId]
     * @return java.lang.Object
     **/
    @PostMapping(value = "/unfavor")
    public Object undoCollect(@RequestBody String paperId, HttpServletRequest request) {
        int userId;
        try {
            userId = Integer.parseInt(request.getHeader("X-UserId"));
        } catch (Exception e) {
            return ResultDTO.errorOf(ErrorType.USER_ID_ILLEGAL_ERROR);
        }
        if (paperService.unfavor(userId, paperId)) {
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(ErrorType.UNFAVOR_ERROR);
    }

    /*
     *
     * @Description 查看收藏列表
     * @Param [ID]
     * @return java.lang.Object
     **/
    @RequestMapping(value = "/favorlist/{ID}",method = RequestMethod.GET)
    public Object getCollect(@PathVariable("ID") int ID){
        List<Favorite> result = paperService.getfavorites(ID);
        List<BriefPaperInfo> paperList = new ArrayList<>();
        for (Favorite favorite : result) {
            Map<String, Object> responseMap = paperService.getEsInfoById(favorite.getPaperid());
            if (responseMap==null) {
                continue;
            }
            BriefPaperInfo paperInfo = paperService.makeUpBriefPaperInfoWithOutFavorInfo(responseMap, favorite.getPaperid());
            paperInfo.setFavored(true);
            paperList.add(paperInfo);
        }
        return ResultDTO.okOf(paperList);
    }

}
