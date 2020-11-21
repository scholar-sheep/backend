package sheep.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sheep.common.utils.ResultDTO;
import sheep.search.Service.SearchService;
import sheep.search.vo.SearchParam;
import sheep.search.vo.SearchResult;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping(value = {"/searchPaper"})
    public Object Search(@RequestBody SearchParam searchParam, HttpServletRequest request) {
        searchParam.set_queryString(request.getQueryString());
        SearchResult result=searchService.getSearchResult(searchParam);
        return ResultDTO.okOf(result);
    }

    @GetMapping(value = {"/hotSearch"})
    public Object Search( HttpServletRequest request) {
        SearchResult result=searchService.hotSearchWord();
        return ResultDTO.okOf(result);
    }
}
