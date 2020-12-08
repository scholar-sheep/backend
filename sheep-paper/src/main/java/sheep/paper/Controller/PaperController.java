package sheep.paper.Controller;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

//    查询 MySQL 中的全部项
    @GetMapping("/info/mysql/{paperIdStr}")
    public Paper getMySqlInfoById(@PathVariable String paperIdStr) {
        int paperId;
        try {
            paperId = Integer.parseInt(paperIdStr);
        } catch (Exception e) {
            return null;
        }
        return paperRepository.findPaperByPaperId(paperId);
    }

//    查询 ElasticSearch 中的全部项
    @GetMapping("/info/es/{paperIdStr}")
    public Map<String, Object> getEsInfoById(@PathVariable String paperIdStr) {
        GetRequest getRequest = new GetRequest("paper", paperIdStr);
        try {
            GetResponse response = paperService.getDetail(getRequest);
            return response.getSourceAsMap();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    获取 paper 的完整信息
    @GetMapping("/info/full/{paperIdStr}")
    public Paper getFullInfoById(@PathVariable String paperIdStr) {
        Paper paper = this.getMySqlInfoById(paperIdStr);
        Map<String, Object> responseMap = this.getEsInfoById(paperIdStr);
        if (paper == null && responseMap==null) {
            return null;
        }
        else if (responseMap==null) {
            return paper;
        }
        else {
            if (paper==null) {
                paper = new Paper();
            }
            paper.setPaperAbstract((String) responseMap.get("abstract"));
            paper.setAuthorName((String) ((Map<String, Object>) responseMap.get("author")).get("name"));
            paper.setAuthorOrg((String) ((Map<String, Object>) responseMap.get("author")).get("org"));
            paper.setFieldOfStudy((String) responseMap.get("fos"));
            paper.setKeywords((String) responseMap.get("keywords"));
            paper.setnCitation((Integer) responseMap.get("n_citation"));
            paper.setTitle((String) responseMap.get("title"));
            paper.setUrl((String) responseMap.get("url"));
            paper.setVenue((String) responseMap.get("venue"));
            paper.setYear((Integer) responseMap.get("year"));
            return paper;
        }
    }

//    获取引文格式
    @GetMapping("/ref/{paperId}")
    public String gerRefString(@PathVariable String paperId) {
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
            return authorName + ',' + title + ',' + '[' + 'J' + "]," + year;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
