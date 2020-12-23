package sheep.algorithm.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class Client {
    public static RestHighLevelClient getClient() {
        //HttpHost httpHost = new HttpHost("152.136.122.218", 9200);
        HttpHost httpHost = new HttpHost("192.168.56.10", 9200);
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        return restHighLevelClient;
    }
}
