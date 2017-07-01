package com.borsam.service.pub;

/**
 * Service - SN
 * Created by Sebarswee on 2015/8/7.
 */
public interface SnService {

    /**
     * 生成SN
     * @param id ID
     * @return SN
     */
    public String generate(Long id);
}
