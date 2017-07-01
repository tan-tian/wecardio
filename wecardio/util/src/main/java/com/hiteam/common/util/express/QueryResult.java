package com.hiteam.common.util.express;

import java.util.ArrayList;
import java.util.List;

public class QueryResult extends QueryParam {

    /**查询结果*/
    private Constats.ExQueryStatus exQueryStatus = Constats.ExQueryStatus.Error;
    /**信息*/
    private String message = "";
    /**快递状态*/
    private Constats.ExState exState = Constats.ExState.OnWay;
    /**快递运行信息*/
    private List<Info> infos = new ArrayList<Info>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Constats.ExQueryStatus getExQueryStatus() {
        return exQueryStatus;
    }

    public void setExQueryStatus(Constats.ExQueryStatus exQueryStatus) {
        this.exQueryStatus = exQueryStatus;
    }

    public Constats.ExState getExState() {
        return exState;
    }

    public void setExState(Constats.ExState exState) {
        this.exState = exState;
    }

    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }
}
