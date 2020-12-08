package sheep.paper.Service;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;

import java.io.IOException;

public interface PaperService {
    GetResponse getDetail(GetRequest getRequest) throws IOException;
}
