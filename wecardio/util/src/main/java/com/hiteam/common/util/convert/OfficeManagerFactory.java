package com.hiteam.common.util.convert;

import com.hiteam.common.util.ConfigUtils;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

/**
 * OpenOffice服务工厂类
 * Created by Sebarswee on 2014/11/1.
 */
public class OfficeManagerFactory {
    private OfficeManager officeManager;

    private OfficeManagerFactory() {
        officeManager = new DefaultOfficeManagerConfiguration()
                .setOfficeHome(ConfigUtils.config.getProperty("OpenOfficeHome"))
                .setMaxTasksPerProcess(300000)
                .buildOfficeManager();
        officeManager.start();
    }

    private static class SingletonHolder {
        private static final OfficeManagerFactory INSTANCE = new OfficeManagerFactory();
    }

    public static OfficeManagerFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取officeManager
     * @return officeManager
     */
    public OfficeManager getOfficeManager() {
        return officeManager;
    }

    /**
     * 停止officeManager
     */
    public void stopOfficeManager() {
        this.officeManager.stop();
    }
}
