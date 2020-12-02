package sheep.search.Service;

import sheep.search.vo.SearchParam;
import sheep.search.vo.SearchResult;

public interface SearchPaperService {
    SearchResult getSearchResult (SearchParam searchParam);

    SearchResult hotSearchWord();

}
