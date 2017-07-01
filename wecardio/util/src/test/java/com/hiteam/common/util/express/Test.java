package com.hiteam.common.util.express;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * <pre>
 * Description:
 * Author: Zhang zhongtao
 * Version:
 * Since: Ver 1.1
 * Date: 2015-02-12 15:40
 * </pre>
 */
public class Test {
    public static void main(String[] args) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig =
                RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        HttpGet httpget = new HttpGet("http://www.kuaidi100.com/kuaidiresult?id=12638879");
        httpget.setConfig(requestConfig);

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            @Override
            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };

        String responseBody = httpclient.execute(httpget, responseHandler);
        System.out.println("responseBody = " + responseBody);
    }
}
