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
 * 省份 - Entity
 */
@Entity
@Table(name = "t_pub_province")
public class Province extends OrderEntity {

    private Country country;        // 所属国家
    private Area area;              // 所属地区
    private String name;            // 省份名称

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
            valueColumnName = "maker_value", pkColumnValue = "t_pub_province", allocationSize = 1)
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
     * 获取所属地区
     * @return 所属地区
     */
    @NotEmpty
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "areaId", nullable = false)
    public Area getArea() {
        return area;
    }

    /**
     * 设置所属地区
     * @param area 地区
     */
    public void setArea(Area area) {
        this.area = area;
    }

    /**
     * 获取省份名称
     * @return 省份名称
     */
    @NotEmpty
    @Length(max = 200)
    @Column(name = "name", length = 200)
    public String getName() {
        return name;
    }

    /**
     * 设置省份名称
     * @param name 省份名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取城市列表
     * @return 城市列表
     */
    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<City> getCitys() {
        return citys;
    }

    /**
     * 设置城市列表
     * @param citys 城市列表
     */
    public void setCitys(Set<City> citys) {
        this.citys = citys;
    }
}
