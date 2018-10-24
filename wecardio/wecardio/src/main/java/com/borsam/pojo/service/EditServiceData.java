package com.borsam.pojo.service;

import com.borsam.repository.entity.service.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tantian on 2015/7/25.
 */
public class EditServiceData implements Serializable {

    private List<Service> services = new ArrayList<>();

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
