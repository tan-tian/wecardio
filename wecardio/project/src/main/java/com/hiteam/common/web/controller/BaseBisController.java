package com.hiteam.common.web.controller;

import com.hiteam.common.base.pojo.search.Filter;
import com.hiteam.common.base.pojo.search.Order;
import com.hiteam.common.base.pojo.search.Page;
import com.hiteam.common.base.pojo.search.Pageable;
import com.hiteam.common.base.service.BaseService;
import com.hiteam.common.web.Message;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Description: 业务Controller基类，默认提供添删改查能力
 * Author: tantian
 * Version:
 * Since: Ver 1.1
 * Date: 2014-12-26 16:21
 * </pre>
 */
public abstract class BaseBisController<T> extends BaseController{
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpSession session;

    @Autowired
    private BaseService<T, Long> baseService;

    /**
     * 添加一个对象
     * @param object 对象入参
     * @return Message
     */
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public Message create(@Valid T object) {
        baseService.save(object);
        return SUCCESS_MSG;
    }

    /**
     * 修改一个对象
     * @param object 对象入参
     * @return Message
     */
    @RequestMapping(method = RequestMethod.POST, value = "/saveOrUpdate")
    public Message saveOrUpdate(@Valid final  T object) {
        List<T> list = new ArrayList(){{
            add(object);
        }};

        baseService.saveOrUpdate(list);
        return SUCCESS_MSG;
    }

    /**
     * 根据ID获取对象
     * @param id 主键ID
     * @return T
     */
    @RequestMapping(value = "/get/{id}")
    public T get(@PathVariable("id") Long id) {
        T t = baseService.find(id);
        return t;
    }

    /**
     * 根据ID数组，批量获取对象
     * @param ids 主键数组
     * @return List
     */
    @RequestMapping(value = "/getList/{ids}")
    public List<T> getList(@RequestParam("ids") Long[] ids) {
        List<T> t = baseService.findList(ids);
        return t;
    }

    /**
     * 分页查找对象
     * @param pageable 分页信息
     * @return Page
     */
    @RequestMapping(value = "/find",method = RequestMethod.POST)
    public Page<T> find(Pageable pageable,@RequestParam Map params){
        List<Filter> filters =  new ArrayList<Filter>();

        if(MapUtils.isNotEmpty(params)){
            Iterator<Map.Entry> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                if(entry.getKey().equals("pageSize")
                        || entry.getKey().equals("pageNo")){
                    continue;
                }

                filters.add(Filter.eq(entry.getKey().toString(), entry.getValue()));
            }
        }

        //默认根据创建时间排序
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("createDate"));

        pageable.setOrders(orders);

        pageable.setFilters(filters);
        Page<T> page = baseService.findPage(pageable);

        return page;
    }

    /**
     * 根据主键ID删除数据
     * @param id 主键ID
     * @return Message
     */
    @RequestMapping(value = "/delete/{id}")
    public Message delete(@PathVariable("id") Long id) {
       baseService.delete(id);
        return SUCCESS_MSG;
    }

    /**
     * 根据主键ID数组批量删除数据
     * @param ids 主键ID数组
     * @return  Message
     */
    @RequestMapping(value = "/delete",method=RequestMethod.POST)
    public Message delete(@RequestParam("ids") Long[] ids)  {
        baseService.delete(ids);
        return SUCCESS_MSG;
    }
}
