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
 * 地区 - Entity
 */
@Entity
@Table(name = "t_pub_area")
public class Area extends OrderEntity {

    private Country country;        // 所属国家
    private String name;            // 地区名称

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
            valueColumnName = "maker_value", pkColumnValue = "t_pub_area", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取所属国家
     * @return 所属国家
     */
    @NotEmpty
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId", nullable = false)
    public Country getCountry() {
        return country;
    }

    /**
     * 设置所属国家
     * @param country 国家
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * 获取地区名称
     * @return 地区名称
     */
    @JsonProperty
    @NotEmpty
    @Length(max = 200)
    @Column(name = "name", length = 200)
    public String getName() {
        return name;
    }

    /**
     * 设置地区名称
     * @param name 地区名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取省份列表
     * @return 省份列表
     */
    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<Province> getProvinces() {
        return provinces;
    }

    /**
     * 设置省份
     * @param provinces 省份列表
     */
    public void setProvinces(Set<Province> provinces) {
        this.provinces = provinces;
    }

    /**
     * 获取城市列表
     * @return 城市列表
     */
    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<City> getCitys() {
        return citys;
    }

    /**
     * 设置城市
     * @param citys 城市列表
     */
    public void setCitys(Set<City> citys) {
        this.citys = citys;
    }
}
