package com.hiteam.common.util.express;

import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.util.lang.DateUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class ExpressUtil {
    private static Logger logger = LoggerFactory.getLogger(ExpressUtil.class);
    private static final String URL = "http://www.kuaidi100.com/query?id=1&type=%s&postid=%s&valicode=&temp=%s";

    /**
     * 获取快递单信息
     *
     * @param params 查询参数
     * @return 结果
     */
    public static List<QueryResult> query(List<QueryParam> params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        List<QueryResult> queryResults = new ArrayList<QueryResult>();

        try {
            for (QueryParam param : params) {
                QueryResult result = httpRequest(httpclient, param);
                queryResults.add(result);
            }
        } finally {
            httpclient.close();
        }

        return queryResults;
    }

    /**
     * 获取单个快递单信息
     *
     * @param param 查询参数
     * @return 结果
     */
    public static QueryResult querySingle(QueryParam param) throws Exception{
        return query(Arrays.asList(param)).get(0);
    }

    private static String excueteRequest(String url,CloseableHttpClient httpclient) throws Exception {

        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setSocketTimeout(10000).setConnectTimeout(10000)
                        .build();
        HttpGet httpget = new HttpGet(url);
        httpget.setConfig(requestConfig);

        logger.info("Executing request " + httpget.getRequestLine());

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

        String result = httpclient.execute(httpget, responseHandler);

        return result;
    }

    private static QueryResult httpRequest(CloseableHttpClient httpclient, QueryParam queryParam) throws Exception {
        QueryResult result = new QueryResult();
        result.setExBillCode(queryParam.getExBillCode());
        result.setExComCode(queryParam.getExComCode());

//        String url = String.format(URL, queryParam.getExComCode(), queryParam.getExBillCode(),Math.random());
        String url = "http://www.kuaidi100.com/applyurl?key=" + queryParam.getKey()+
                "&com="+queryParam.getExComCode()+"&nu="+queryParam.getExBillCode();

        try {

            String responseBody = excueteRequest(url, httpclient);

            url = "http://www.kuaidi100.com/query?id=1&type=" + queryParam.getExComCode() +
                    "&postid=" + queryParam.getExBillCode() + "&valicode=&temp=" + Math.random();

            responseBody = excueteRequest(url, httpclient);

            //json数据转换为Map
            Map map = JsonUtils.toObject(responseBody, Map.class);
            //Map转换为bean
            map2Bean(map, result);

        } catch (Exception ex) {
            logger.error("请求快递接口异常", ex);
            result.setExQueryStatus(Constats.ExQueryStatus.Error);
            result.setMessage("请求快递接口异常");
        }

        return result;
    }

    /**
     * Map数据转换为结果bean
     * @param map
     * @param result
     */
    private static void map2Bean(Map map,QueryResult result) throws Exception{
        result.setMessage(MapUtils.getString(map,"message",""));

        //快递查询状态
        Integer status = MapUtils.getInteger(map,"status",Constats.ExQueryStatus.NoResult.ordinal());

        try {
            result.setExQueryStatus(Constats.ExQueryStatus.values()[status]);
        }catch (Exception ex){
            Constats.ExQueryStatus exQueryStatus = Constats.ExQueryStatus.Error;
            if(status == 200){
                exQueryStatus = Constats.ExQueryStatus.Success;
            }
            result.setExQueryStatus(exQueryStatus);
        }

        //快递状态
        Integer state = MapUtils.getInteger(map,"state",Constats.ExState.OnWay.ordinal());
        try {
            result.setExState(Constats.ExState.values()[state]);
        } catch (Exception e) {
            result.setExState(Constats.ExState.Unknown);
        }

        //快递详细信息
        Object dataList = MapUtils.getObject(map, "data", null);

        if(dataList == null){
            return;
        }

        //快递详细信息
        List<Map> datas = (ArrayList)dataList;

        List<Info> infos = new ArrayList<Info>();

        for (Map data : datas) {
            Info info = new Info();
            info.setTime(DateUtil.parseDate(MapUtils.getString(data, "time", "1900-01-01 00:00:00"),
                    new String[]{DateUtil.DATE_FORMAT.YYYY_MM_DD_HH_MM_SS.getFormat()}));
            info.setContent(MapUtils.getString(data, "context",""));
            infos.add(info);
        }

        result.setInfos(infos);

    }

}
