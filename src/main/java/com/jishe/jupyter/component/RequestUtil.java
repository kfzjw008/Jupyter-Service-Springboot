package com.jishe.jupyter.component;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

/**
 * @program: jupyter
 * @description: request请求工具类
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-13 11:04
 **/
public class RequestUtil {
    public String CreateRequestUtil(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            URIBuilder builder = new URIBuilder(url);  // 创建uri
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri); // 创建http GET请求
            response = httpclient.execute(httpGet);// 执行请求
            // 判断返回状态是否为200 ：if (response.getStatusLine().getStatusCode() == 200)
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
