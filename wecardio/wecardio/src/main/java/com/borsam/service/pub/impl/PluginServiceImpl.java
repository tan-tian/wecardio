package com.borsam.service.pub.impl;

import com.borsam.plugin.PaymentPlugin;
import com.borsam.plugin.StoragePlugin;
import com.borsam.service.pub.PluginService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Service - 插件
 * Created by Sebarswee on 2015/6/30.
 */
@Service("pluginServiceImpl")
public class PluginServiceImpl implements PluginService {

    @Resource
    private List<StoragePlugin> storagePlugins = new ArrayList<StoragePlugin>();

    @Resource
    private List<PaymentPlugin> paymentPlugins = new ArrayList<PaymentPlugin>();

    @Resource
    private Map<String, PaymentPlugin> paymentPluginMap = new HashMap<String, PaymentPlugin>();

    @Override
    public List<StoragePlugin> getStoragePlugins() {
        Collections.sort(storagePlugins);
        return storagePlugins;
    }

    @Override
    public List<StoragePlugin> getStoragePlugins(boolean isEnabled) {
        List<StoragePlugin> result = new ArrayList<StoragePlugin>();
        CollectionUtils.select(storagePlugins, new Predicate() {
            public boolean evaluate(Object object) {
                StoragePlugin storagePlugin = (StoragePlugin) object;
                return storagePlugin.getIsEnabled() == isEnabled;
            }
        }, result);
        Collections.sort(result);
        return result;
    }

    @Override
    public List<PaymentPlugin> getPaymentPlugins() {
        Collections.sort(paymentPlugins);
        return paymentPlugins;
    }

    @Override
    public List<PaymentPlugin> getPaymentPlugins(boolean isEnabled) {
        List<PaymentPlugin> result = new ArrayList<PaymentPlugin>();
        CollectionUtils.select(paymentPlugins, new Predicate() {
            public boolean evaluate(Object object) {
                PaymentPlugin paymentPlugin = (PaymentPlugin) object;
                return paymentPlugin.isEnabled() == isEnabled;
            }
        }, result);
        Collections.sort(result);
        return result;
    }

    @Override
    public PaymentPlugin getPaymentPlugin(String id) {
        return paymentPluginMap.get(id);
    }
}
