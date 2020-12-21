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
     * @Description 根据 Paper ID 获取 MySQL 中的数据
     * @Param [paperIdStr]
     * @return java.lang.Object
     **/
    @GetMapping("/info/mysql/{paperIdStr}")
    public Object getMySqlInfoById(@PathVariable String paperIdStr) {
        Paper paper = paperService.getMySqlInfoById(paperIdStr);
        if (paper==null) {
            return ResultDTO.errorOf(ErrorType.PAPER_NOT_EXIST_ERROR);
        }
        else {
            return ResultDTO.okOf(paper);
        }
    }

    /*
     *
     * @Description 根据 Paper ID 获取 ElasticSearch 中的信息
     * @Param [paperIdStr]
     * @return java.lang.Object
     **/
    @GetMapping("/info/es/{paperIdStr}")
    public Object getEsInfoById(@PathVariable String paperIdStr) {
        Map map = paperService.getEsInfoById(paperIdStr);
        if (map==null) {
            return ResultDTO.errorOf(ErrorType.PAPER_NOT_EXIST_ERROR);
        }
        else {
            return ResultDTO.okOf(map);
        }
    }

    /*
     *
     * @Description 根据 Paper ID 获取全部信息
     * @Param [paperIdStr]
     * @return java.lang.Object
     **/
    @GetMapping("/info/full/{paperIdStr}")
    public Object getFullInfoById(@PathVariable String paperIdStr) {
        Paper paper = paperService.getMySqlInfoById(paperIdStr);
        Map<String, Object> responseMap = paperService.getEsInfoById(paperIdStr);
        if (paper == null && responseMap==null) {
            return ResultDTO.errorOf(ErrorType.PAPER_NOT_EXIST_ERROR);
        }
        else if (responseMap==null) {
            return ResultDTO.okOf(paper);
        }
        else {
            if (paper==null) {
                paper = new Paper();
            }
            paper.setPaperAbstract((String) responseMap.get("abstract"));
//            List<Author> authors = (List<Author>) responseMap.get("author");
//            for (Author author : authors) {
//                paper.appendAuthors(author);
//            }
            paper.setAuthors((List<Author>) responseMap.get("author"));
            paper.setFieldOfStudy((String) responseMap.get("fos"));
            paper.setKeywords((List<String>) responseMap.get("keywords"));
            paper.setnCitation((Integer) responseMap.get("n_citation"));
            paper.setTitle((String) responseMap.get("title"));
            paper.setUrl((String) responseMap.get("url"));
            paper.setVenue((String) responseMap.get("venue"));
            paper.setYear((Integer) responseMap.get("year"));
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
            Paper paper = paperService.getMySqlInfoById(favorite.getPaperid());
            Map<String, Object> responseMap = paperService.getEsInfoById(favorite.getPaperid());
            if (paper == null && responseMap==null) {
                continue;
            }
            BriefPaperInfo paperInfo = paperService.makeUpBriefPaperInfoWithOutFavorInfo(paper, responseMap);
            paperInfo.setFavored(true);
            paperList.add(paperInfo);
        }
        return ResultDTO.okOf(paperList);
    }

    /*
     *
     * @Description 获取引文格式
     * @Param [paperId]
     * @return java.lang.String
     **/
    @GetMapping("/ref/{paperIdStr}")
    public Object gerRefString(@PathVariable String paperIdStr) {
        Paper paper = paperService.getMySqlInfoById(paperIdStr);
        Map map = paperService.getEsInfoById(paperIdStr);

        if (map==null || paper==null) {
            return ResultDTO.errorOf(ErrorType.PAPER_NOT_EXIST_ERROR);
        }
        else {
            List<Map> authors = (List<Map>) (map.get("author"));
            StringBuilder sb = new StringBuilder();
            // TODO 作者数量大于 6 时应该最多写 6 个？暂时就都加上吧
            for (Map author: authors) {
                sb.append(author.get("name"));
                sb.append('.');
            }
            String authorPart = sb.toString();
            String title = map.get("title").toString();
            String year = map.get("year").toString();
            // TODO 实际类型的处理
            // 论文的类型编号暂时设计如下：
            // 0 - 期刊类 - J
            // 1 - 专著类 - M
            // 2 - 报纸类 - N
            // 3 - 论文集 - C
            // 4 - 学位论文 - D
            // 5 - 研究报告 - R
            // 6 - 译注 - M
            // 7 - 其他 - Z
            // TODO 某些项为空的处理？
            String type = paper.getDocType();
            switch (type) {
                case "0": {
                    String venue = paper.getVenue();
                    String volume = paper.getVolume();
                    String issue = paper.getIssue();
                    String start = paper.getPaperStart();
                    String end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'J' + "]." + venue + '.' + year + '.'
                            + volume + '(' + issue + "):" + start + '-' + end + '.');
                }
                case "1":
                case "6": {
                    String venue = paper.getVenue();
                    String publisher = paper.getPublisher();
                    String start = paper.getPaperStart();
                    String end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'M' + "]." + venue + ':' + publisher
                            + '.' + year + ':' + start + '-' + end + '.');
                }
                case "2": {
                    String publisher = paper.getPublisher();
                    String issue = paper.getIssue();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'N' + "]." + publisher
                            + '.' + year + '(' + issue + ")" + '.');
                }
                case "3": {
                    String venue = paper.getVenue();
                    String publisher = paper.getPublisher();
                    String start = paper.getPaperStart();
                    String end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'C' + "]." + venue + ':' + publisher
                            + '.' + year + ':' + start + '-' + end + '.');
                }
                case "4": {
                    String venue = paper.getVenue();
                    String start = paper.getPaperStart();
                    String end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'D' + "]." + venue + ':'
                            + authors.get(0).get("name") + '.' + year + ':' + start + '-' + end + '.');
                }
                case "5": {
                    String venue = paper.getVenue();
                    String publisher = paper.getPublisher();
                    String start = paper.getPaperStart();
                    String end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'R' + "]." + venue + ':' + publisher
                            + '.' + year + ':' + start + '-' + end + '.');
                }
                default: {
                    String venue = paper.getVenue();
                    String volume = paper.getVolume();
                    String issue = paper.getIssue();
                    String start = paper.getPaperStart();
                    String end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'Z' + "]." + venue + '.' + year + '.'
                            + volume + '(' + issue + "):" + start + '-' + end + '.');
                }
            }

        }
    }

}
