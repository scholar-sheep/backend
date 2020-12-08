package sheep.paper.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sheep.paper.Repository.PaperRepository;

@RestController
@RequestMapping("api/paper/basic")
public class PaperController {
    @Autowired
    private PaperRepository paperRepository;

    @RequestMapping("/info/{paperId}")
    public String getPaperInfo(@PathVariable int paperId) {
        return paperRepository.findPaperByPaperId(paperId).toString();
    }

}
