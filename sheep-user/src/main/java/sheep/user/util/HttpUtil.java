package sheep.user.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HttpUtil {
    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * POST请求
     * @param url 请求地址
     * @param params post请求的参数
     * @return 返回结果的字符串格式。
     */
    public static String post(String url, Map<String,String> params){
        //因为RestTemplate的参数只支持LinkedMultiValueMap,所以需要将Map转成LinkedMultiValueMap；
        MultiValueMap<String,String> params1 = new LinkedMultiValueMap<>();
        params.entrySet().forEach(param -> {
            params1.add(param.getKey(),param.getValue());
        });
        String responseBody = new String();
        try {
            //发送post请求
            responseBody = restTemplate.postForObject(url, params1, String.class);
        }catch (HttpClientErrorException e){
            //异常处理
            e.printStackTrace();
        }
        return responseBody;
    }
}
