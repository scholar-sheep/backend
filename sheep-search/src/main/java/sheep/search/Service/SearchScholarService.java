package sheep.search.Service;

import sheep.search.vo.ScholarParam;
import sheep.search.vo.SearchParam;
import sheep.search.vo.SearchResult;

public interface SearchScholarService {
    SearchResult getSearchResult (ScholarParam searchParam);
}
