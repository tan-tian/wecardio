package com.hiteam.common.repository.entity.pub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiteam.common.base.repository.entity.OrderEntity;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * 城市 - Entity
 */
@Entity
@Table(name = "t_pub_city")
public class City extends OrderEntity {

    private Country country;    // 所属国家
    private Area area;          // 所属地区
    private Province province;  // 所属省份
    private String name;        // 城市名称
    private String zipCode;     // 邮编

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
            valueColumnName = "maker_value", pkColumnValue = "t_pub_city", allocationSize = 1)
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
     * 获取所属省份
     * @return 所属省份
     */
    @NotEmpty
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provinceId", nullable = false)
    public Province getProvince() {
        return province;
    }

    /**
     * 设置所属省份
     * @param province 省份
     */
    public void setProvince(Province province) {
        this.province = province;
    }

    /**
     * 获取城市名称
     * @return 城市名称
     */
    @NotEmpty
    @Length(max = 200)
    @Column(name = "name", length = 200)
    public String getName() {
        return name;
    }

    /**
     * 设置城市名称
     * @param name 城市名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取邮编
     * @return 邮编
     */
    @NotEmpty
    @Length(max = 200)
    @Column(name = "zipCode")
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 设置邮编
     * @param zipCode 邮编
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
