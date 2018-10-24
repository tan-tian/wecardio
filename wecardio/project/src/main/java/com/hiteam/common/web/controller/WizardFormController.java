package com.hiteam.common.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller - 向导型Controller
 * 使用注解@SessionAttributes(value = {"user"})    // 模型对象中名字如果是“user”将存储在会话范围，并自动暴露到模型数据中
 * Created by tantian on 2015/8/11.
 */
public class WizardFormController {

    public static final String PARAM_TARGET = "_target";    // 下一页
    public static final String PARAM_PAGE = "_page";        // 当前页

    private String[] pageViews;     // 分步视图
    private String successView;     // 成功视图
    private String cancelView;      // 取消视图

    /**
     * 设置分步视图
     * @param pageViews 视图列表
     */
    public void setPageViews(String[] pageViews) {
        this.pageViews = pageViews;
    }

    /**
     * 设置成功视图
     * @param successView 成功视图
     */
    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    /**
     * 设置取消视图
     * @param cancelView 取消视图
     */
    public void setCancelView(String cancelView) {
        this.cancelView = cancelView;
    }

    /**
     * 得到目标页码（即下一个页码）
     * @param request HttpServletRequest
     * @param currentPage 当前页码
     * @return 目标页码
     */
    public int getTargetPage(HttpServletRequest request, int currentPage) {
        // 得到下一页页码（如_target0 0就是页码）
        return WebUtils.getTargetPage(request, PARAM_TARGET, currentPage);
    }

    /**
     * 表单步骤，不是finish/cancel
     * @param request HttpServletRequest
     * @param currentPage 当前页码
     * @return 表单视图
     */
    @RequestMapping(params={"!_finish", "!_cancel"})
    public String form(HttpServletRequest request, @RequestParam(value = PARAM_PAGE, defaultValue="0") int currentPage) {
        return pageViews[getTargetPage(request, currentPage)];
    }

    /**
     * 取消
     */
    @RequestMapping(params="_cancel")
    public String cancel(SessionStatus status) {
        status.setComplete();
        return cancelView;
    }

    /**
     * 成功
     */
    @RequestMapping(params = "_finish")
    public String finish(Object command, SessionStatus status) {
        status.setComplete();
        return successView;
    }
}
