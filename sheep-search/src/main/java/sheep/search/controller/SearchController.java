package sheep.search.controller;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sheep.common.exception.ErrorType;
import sheep.common.utils.ResultDTO;
import sheep.search.Service.SearchPaperService;
import sheep.search.Service.*;
import sheep.search.vo.PaperModel;
import sheep.search.vo.ScholarParam;
import sheep.search.vo.SearchParam;
import sheep.search.vo.SearchResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

    @Autowired
    private SearchPaperService advancedSearchService;
    @Autowired
    private SearchScholarService searchScholarService;
    @Autowired
    private PaperService paperService;

    @PostMapping(value = {"/searchPaper"})
    public Object Search(@RequestBody SearchParam searchParam, HttpServletRequest request) {
        searchParam.set_queryString(request.getQueryString());
        SearchResult result=advancedSearchService.getSearchResult(searchParam);
        return ResultDTO.okOf(result);
    }

    @PostMapping(value = {"/searchScholar"})
    public Object Search(@RequestBody ScholarParam searchParam, HttpServletRequest request) {
        if(StringUtils.isEmpty(searchParam.getName()))
            return ResultDTO.errorOf(ErrorType.MISS_NANME);
        SearchResult result=searchScholarService.getSearchResult(searchParam);
        return ResultDTO.okOf(result);
    }
    @GetMapping(value = {"/hotSearch"})
    public Object Search( HttpServletRequest request) {
        SearchResult result=advancedSearchService.hotSearchWord();
        return ResultDTO.okOf(result);
    }

    @PostMapping(value = "/recommend")
    public Object getRecommendList(@RequestParam String paperIdStr, HttpServletRequest request) {
        SearchResult result;
        try {
            result = paperService.getRecommendById(paperIdStr);
        } catch (Exception e) {
            return ResultDTO.errorOf(ErrorType.PAPER_NOT_EXIST_ERROR);
        }
        return ResultDTO.okOf(result);
    }

}
