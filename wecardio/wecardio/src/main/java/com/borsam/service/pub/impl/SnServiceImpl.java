package com.borsam.service.pub.impl;

import com.borsam.service.pub.SnService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * Service - SN
 * Created by tantian on 2015/8/7.
 */
@Service("snServiceImpl")
public class SnServiceImpl implements SnService {

    @Override
    public String generate(Long id) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year + (month < 10 ? "0" + month : "" + month) + (day < 10 ? "0" + day : day) + ((int) (new Date().getTime() / 1000)) + id;
    }
}
