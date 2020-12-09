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
@RequestMapping("api/paper/basic")
public class PaperController {
    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PaperService paperService;

    public Paper _getMySqlInfoById(int paperId) {
        return paperRepository.findPaperByPaperId(paperId);
    }

    public Map<String, Object> _getEsInfoById(String paperIdStr) {
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
    @GetMapping("/ref/{paperId}")
    public Object gerRefString(@PathVariable String paperId) {
        GetRequest getRequest = new GetRequest("paper", paperId);
        try {
            GetResponse response = paperService.getDetail(getRequest);
            Map<String, Object> sourceInResponse = response.getSourceAsMap();
//            TODO 稍后处理一下多个作者的情况
            Map<String, Object> author = (Map<String, Object>) sourceInResponse.get("author");
            String authorName = author.get("name").toString();
            String title = sourceInResponse.get("title").toString();
            String year = sourceInResponse.get("year").toString();
//            TODO 规范引用的格式
//            TODO 文献类型的处理
//            return sourceInResponse.toString();
            return ResultDTO.okOf(authorName + ',' + title + ',' + '[' + 'J' + "]," + year);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultDTO.errorOf(ErrorType.PAPER_NOT_EXIST_ERROR);
        }
    }

}
