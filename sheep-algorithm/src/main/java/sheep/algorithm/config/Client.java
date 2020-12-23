package sheep.algorithm.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class Client {
    public static RestHighLevelClient getClient() {
        //HttpHost httpHost = new HttpHost("152.136.122.218", 9200);
//        HttpHost httpHost = new HttpHost("192.168.56.10", 9200);
//        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
//        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
//        return restHighLevelClient;
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("152.136.122.218", 9200))
                        .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                            @Override
                            public RequestConfig.Builder customizeRequestConfig(
                                    RequestConfig.Builder requestConfigBuilder) {
                                return requestConfigBuilder.setConnectTimeout(5000 * 1000) // 连接超时（默认为1秒）
                                        .setSocketTimeout(6000 * 1000);// 套接字超时（默认为30秒）//更改客户端的超时限制默认30秒现在改为100*1000分钟
                            }
                        }));
    }
}
