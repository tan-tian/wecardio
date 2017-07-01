package com.borsam.service.pub;

import com.borsam.plugin.PaymentPlugin;
import com.borsam.plugin.StoragePlugin;

import java.util.List;

/**
 * Service - 插件
 * Created by Sebarswee on 2015/6/30.
 */
public interface PluginService {

    /**
     * 获取存储插件
     * @return 存储插件
     */
    public List<StoragePlugin> getStoragePlugins();

    /**
     * 获取存储插件
     * @param isEnabled 是否启用
     * @return 存储插件
     */
    public List<StoragePlugin> getStoragePlugins(boolean isEnabled);

    /**
     * 获取支付插件
     * @return 支付插件
     */
    public List<PaymentPlugin> getPaymentPlugins();

    /**
     * 获取支付插件
     * @param isEnabled 是否启用
     * @return 支付插件
     */
    public List<PaymentPlugin> getPaymentPlugins(boolean isEnabled);

    /**
     * 获取支付插件
     * @param id ID
     * @return 支付插件
     */
    public PaymentPlugin getPaymentPlugin(String id);
}
