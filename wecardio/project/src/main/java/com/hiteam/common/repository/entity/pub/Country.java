package com.hiteam.common.repository.entity.pub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.OrderEntity;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 国家 - Entity
 */
@Entity
@Table(name = "t_pub_country")
public class Country extends OrderEntity {

    private String name;            // 国家名称

    /**
     * 地区列表
     */
    private Set<Area> areas = new HashSet<Area>();

    /**
     * 省份列表
     */
    private Set<Province> provinces = new HashSet<Province>();

    /**
     * 城市列表
     */
    private Set<City> citys = new HashSet<City>();

    /**
     * 获取ID
     * @return ID
     */
    @JsonProperty
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "t_pub_country", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取国家名称
     * @return 国家名称
     */
    @NotEmpty
    @Length(max = 200)
    @Column(name = "name", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    /**
     * 设置国家名称
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取地区
     * @return 地区
     */
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<Area> getAreas() {
        return areas;
    }

    /**
     * 设置地区
     * @param areas 地区
     */
    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    /**
     * 获取省份
     * @return 省份
     */
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<Province> getProvinces() {
        return provinces;
    }

    /**
     * 设置省份
     * @param provinces 省份
     */
    public void setProvinces(Set<Province> provinces) {
        this.provinces = provinces;
    }

    /**
     * 获取城市
     * @return 城市
     */
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<City> getCitys() {
        return citys;
    }

    /**
     * 设置城市
     * @param citys 城市
     */
    public void setCitys(Set<City> citys) {
        this.citys = citys;
    }
}
