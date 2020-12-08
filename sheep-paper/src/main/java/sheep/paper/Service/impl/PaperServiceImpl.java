package sheep.paper.Service.impl;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sheep.paper.Service.PaperService;

import java.io.IOException;

@Service
public class PaperServiceImpl implements PaperService {

    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient highLevelClient;

    @Override
    public GetResponse getDetail(GetRequest getRequest) throws IOException {
        return highLevelClient.get(getRequest, RequestOptions.DEFAULT);
    }
}
