package com.borsam.repository.entity.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Entity - 语言
 * Created by tantian on 2015/7/17.
 */
@Embeddable
public class Language implements Serializable {

    private Long id;            // 语言ID
    private String name;        // 语言名称

    /**
     * 获取语言ID
     * @return 语言ID
     */
    @Column(name = "language_id", nullable = false)
    public Long getId() {
        return id;
    }

    /**
     * 设置语言ID
     * @param id 语言ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取语言名称
     * @return 语言名称
     */
    @Column(name = "language_name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    /**
     * 设置语言名称
     * @param name 语言名称
     */
    public void setName(String name) {
        this.name = name;
    }
}
