package com.hiteam.common.repository.entity.enums;

import com.hiteam.common.base.repository.entity.BaseEntity;
import com.hiteam.common.base.repository.entity.LongEntity;

import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;

/**
 * Entity - 枚举
 */
@Entity
@Table(name = "t_pub_enum")
public class Enum extends LongEntity {

    private String tblName;             // 枚举所属表名称
    private String colName;             // 枚举字段
    private String name;                // 枚举名称
	private Integer value;              // 枚举值
	private String text;                // 枚举中文值
	private Boolean isEnabled;			// 是否有效
	private Long parentId;			    // 所级联枚举
	private String relEnumName;		    // 所级联枚举名称

    /**
     * 获取ID
     * @return ID
     */
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "t_pub_enum", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }

    /**
     * 获取所属表名
     * @return 所属表名
     */
    @Column(name = "s_tbl_name", length = 64)
    public String getTblName() {
        return tblName;
    }

    /**
     * 设置所属表名
     * @param tblName 所属表名
     */
    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    /**
     * 获取所属字段名
     * @return 所属字段名
     */
    @Column(name = "s_col_ename", length = 64)
    public String getColName() {
        return colName;
    }

    /**
     * 设置所属字段名
     * @param colName 所属字段名
     */
    public void setColName(String colName) {
        this.colName = colName;
    }

    /**
     * 获取枚举名称
     * @return 枚举名称
     */
    @Column(name = "s_col_cname", length = 128)
    public String getName() {
        return name;
    }

    /**
     * 设置枚举名称
     * @param name 枚举名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取枚举值
     * @return 枚举值
     */
    @Column(name = "i_enum_value")
    public Integer getValue() {
        return value;
    }

    /**
     * 设置枚举值
     * @param value 枚举值
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * 获取枚举显示值
     * @return 枚举显示值
     */
    @Column(name = "s_enum_name", length = 64)
    public String getText() {
        return text;
    }

    /**
     * 设置枚举显示值
     * @param text 枚举显示值
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 获取是否有效
     * @return 是否有效
     */
    @Column(name = "i_is_enabled")
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    /**
     * 设置是否有效
     * @param isEnabled 是否有效
     */
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * 获取上级枚举ID
     * @return 上级枚举ID
     */
    @Column(name = "i_rel_enumid")
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级枚举ID
     * @param parentId 上级枚举ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取上级枚举名称
     * @return 上级枚举名称
     */
    @Column(name = "s_rel_enmuname", length = 128)
    public String getRelEnumName() {
        return relEnumName;
    }

    /**
     * 设置上级枚举名称
     * @param relEnumName 上级枚举名称
     */
    public void setRelEnumName(String relEnumName) {
        this.relEnumName = relEnumName;
    }
}
