package sheep.paper.Controller;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sheep.paper.Repository.PaperRepository;
import sheep.paper.Service.PaperService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/paper/basic")
public class PaperController {
    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PaperService paperService;

    @RequestMapping("/info/{paperId}")
    public String getPaperInfo(@PathVariable int paperId) {
        return paperRepository.findPaperByPaperId(paperId).toString();
    }

    @GetMapping("/detail/{paperId}")
    public String getPaperDetailById(@PathVariable String paperId) {
        GetRequest getRequest = new GetRequest("paper", paperId);
        try {
            GetResponse response = paperService.getDetail(getRequest);
            return response.getSourceAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

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
