package com.hiteam.common.util.express;

import com.hiteam.common.util.json.JsonUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExpressUtilTest {
    String com = "ems";

    @Test
    public void testQuery() throws Exception {
        List<QueryParam> queryParams = new ArrayList<QueryParam>();
//        String[] billCodes = new String[]{"BE180022480AU","BE180022609AU","BE180022610AU",
//                "BE180022611AU","BE180022612AU","BE180022613AU","BE180022614AU","BE180022615AU","BE1800226159999AU"};
        String[] billCodes = new String[]{"BE186126662AU"};
        for (String billCode : billCodes) {
            QueryParam queryParam = new QueryParam();
            queryParam.setExBillCode(billCode);
            queryParam.setExComCode(com);
            queryParams.add(queryParam);
        }

        List<QueryResult> queryResults = ExpressUtil.query(queryParams);
        System.out.print("QueryResult:" + JsonUtils.toJson(queryResults));
        Assert.assertEquals(queryParams.size(), queryResults.size());
    }

    @Test
    public void testQuerySingle() throws Exception {
        QueryParam queryParam = new QueryParam();
        queryParam.setExComCode(com);
        queryParam.setExBillCode("BE180022613AU");
        QueryResult result = ExpressUtil.querySingle(queryParam);
        System.out.print("QueryResult:" + JsonUtils.toJson(result));
        Assert.assertNotNull(result);
    }
}