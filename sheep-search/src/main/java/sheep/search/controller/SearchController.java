package sheep.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sheep.common.exception.ErrorType;
import sheep.common.utils.ResultDTO;
import sheep.search.Service.SearchPaperService;
import sheep.search.Service.*;
import sheep.search.vo.ScholarParam;
import sheep.search.vo.SearchParam;
import sheep.search.vo.SearchResult;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SearchController {

    @Autowired
    private SearchPaperService advancedSearchService;
    @Autowired
    private SearchScholarService searchScholarService;

    @PostMapping(value = {"/search/paper"})
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
    @GetMapping(value = {"/search/hot"})
    public Object Search( HttpServletRequest request) {
        SearchResult result=advancedSearchService.hotSearchWord();
        return ResultDTO.okOf(result);
    }

}
