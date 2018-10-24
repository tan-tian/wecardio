package com.hiteam.common.util.convert;

import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

/**
 * OpenOffice服务类
 * Created by tantian on 2014/11/2.
 */
public class OfficeManagerService {

    private OfficeManager officeManager;
    private String officeHome;
    private int maxTasksPerProcess;

    public void init() {
        // maxTasksPerProcess = 300000
        officeManager = new DefaultOfficeManagerConfiguration()
                .setOfficeHome(officeHome)
                .setMaxTasksPerProcess(maxTasksPerProcess)
                .buildOfficeManager();
        officeManager.start();
    }

    public void destory() {
        officeManager.stop();
    }

    public OfficeManager getOfficeManager() {
        return officeManager;
    }

    public void setOfficeManager(OfficeManager officeManager) {
        this.officeManager = officeManager;
    }

    public String getOfficeHome() {
        return officeHome;
    }

    public void setOfficeHome(String officeHome) {
        this.officeHome = officeHome;
    }

    public int getMaxTasksPerProcess() {
        return maxTasksPerProcess;
    }

    public void setMaxTasksPerProcess(int maxTasksPerProcess) {
        this.maxTasksPerProcess = maxTasksPerProcess;
    }
}
