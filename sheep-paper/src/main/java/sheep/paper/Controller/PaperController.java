package sheep.paper.Controller;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sheep.common.exception.ErrorType;
import sheep.common.utils.ResultDTO;
import sheep.paper.Entity.Author;
import sheep.paper.Entity.Paper;
import sheep.paper.Repository.PaperRepository;
import sheep.paper.Service.PaperService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paper/basic")
public class PaperController {
    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PaperService paperService;

    private Paper _getMySqlInfoById(int paperId) {
        return paperRepository.findPaperByPaperId(paperId);
    }

    private Map<String, Object> _getEsInfoById(String paperIdStr) {
        GetRequest getRequest = new GetRequest("paper", paperIdStr);
        try {
            GetResponse response = paperService.getDetail(getRequest);
            return response.getSourceAsMap();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     *
     * @Description 根据 Paper ID 获取 MySQL 中的数据
     * @Param [paperIdStr]
     * @return java.lang.Object
     **/
    @GetMapping("/info/mysql/{paperIdStr}")
    public Object getMySqlInfoById(@PathVariable String paperIdStr) {
        int paperId;
        try {
            paperId = Integer.parseInt(paperIdStr);
        } catch (Exception e) {
            return ResultDTO.errorOf(ErrorType.PAPER_ID_ILLEGAL_ERROR);
        }
        Paper paper = _getMySqlInfoById(paperId);
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
        Map map = _getEsInfoById(paperIdStr);
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
        int paperId;
        try {
            paperId = Integer.parseInt(paperIdStr);
        } catch (Exception e) {
            return ResultDTO.errorOf(ErrorType.PAPER_ID_ILLEGAL_ERROR);
        }
        Paper paper = this._getMySqlInfoById(paperId);
        Map<String, Object> responseMap = this._getEsInfoById(paperIdStr);
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
     * @Description 获取引文格式
     * @Param [paperId]
     * @return java.lang.String
     **/
    @GetMapping("/ref/{paperIdStr}")
    public Object gerRefString(@PathVariable String paperIdStr) {
        int paperid;
        try {
            paperid = Integer.parseInt(paperIdStr);
        } catch (Exception e) {
            return ResultDTO.errorOf(ErrorType.PAPER_NOT_EXIST_ERROR);
        }
        Paper paper = _getMySqlInfoById(paperid);

        Map map = _getEsInfoById(paperIdStr);

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
            int type = paper.getDocType();
            switch (type) {
                case 0: {
                    String venue = paper.getVenue();
                    int volume = paper.getVolume();
                    int issue = paper.getIssue();
                    int start = paper.getPaperStat();
                    int end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'J' + "]." + venue + '.' + year + '.'
                            + volume + '(' + issue + "):" + start + '-' + end + '.');
                }
                case 1:
                case 6: {
                    String venue = paper.getVenue();
                    String publisher = paper.getPublisher();
                    int start = paper.getPaperStat();
                    int end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'M' + "]." + venue + ':' + publisher
                            + '.' + year + ':' + start + '-' + end + '.');
                }
                case 2: {
                    String publisher = paper.getPublisher();
                    int issue = paper.getIssue();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'N' + "]." + publisher
                            + '.' + year + '(' + issue + ")" + '.');
                }
                case 3: {
                    String venue = paper.getVenue();
                    String publisher = paper.getPublisher();
                    int start = paper.getPaperStat();
                    int end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'C' + "]." + venue + ':' + publisher
                            + '.' + year + ':' + start + '-' + end + '.');
                }
                case 4: {
                    String venue = paper.getVenue();
                    int start = paper.getPaperStat();
                    int end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'D' + "]." + venue + ':'
                            + authors.get(0).get("name") + '.' + year + ':' + start + '-' + end + '.');
                }
                case 5: {
                    String venue = paper.getVenue();
                    String publisher = paper.getPublisher();
                    int start = paper.getPaperStat();
                    int end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'R' + "]." + venue + ':' + publisher
                            + '.' + year + ':' + start + '-' + end + '.');
                }
                default: {
                    String venue = paper.getVenue();
                    int volume = paper.getVolume();
                    int issue = paper.getIssue();
                    int start = paper.getPaperStat();
                    int end = paper.getPaperEnd();
                    return ResultDTO.okOf("[1]" + authorPart + title + '[' + 'Z' + "]." + venue + '.' + year + '.'
                            + volume + '(' + issue + "):" + start + '-' + end + '.');
                }
            }

        }
    }

}
