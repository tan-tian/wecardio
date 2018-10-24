package com.borsam.web.controller.wf;

import com.borsam.repository.entity.wf.WorkItem;
import com.borsam.service.wf.QueryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller - 流程
 * Created by tantian on 2015/7/7.
 */
@Controller
@RequestMapping(value = "/wf")
public class WfController {

    @Resource(name = "queryServiceImpl")
    private QueryService queryService;

    /**
     * 获取处理信息
     */
    @RequestMapping(value = "/billProcessInfo", method = RequestMethod.POST)
    @ResponseBody
    public List<WorkItem> billProcessInfo(Long guid, Boolean showAll) {
        if (guid != null) {
            if (showAll == null) {
                showAll = false;
            }
            return queryService.queryProcessInfo(guid, showAll);
        } else {
            return new ArrayList<WorkItem>();
        }
    }

    /**
     * 获取流程信息
     */
    @RequestMapping(value = "/getFlowInfo", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> getFlowInfo(String wfCode, Long guid) {
        if (guid != null) {
            return queryService.getFlowInfo(wfCode, guid);
        } else {
            return new ArrayList<Map<String, String>>();
        }
    }
}
