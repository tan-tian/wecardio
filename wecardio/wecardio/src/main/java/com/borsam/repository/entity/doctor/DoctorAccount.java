package com.borsam.repository.entity.doctor;

import com.hiteam.common.base.repository.entity.LongEntity;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.*;

/**
 * Created by tantian on 2017/6/15.
 */
@Entity
@Table(name = "account_doctor")
public class DoctorAccount extends LongEntity {

    private String mobile;
    private String email;
    private String password;
    private byte activeState;
    private byte deleteState;
    private int created;
    private DoctorProfile doctorProfile;
    private Boolean isActive;
    private Boolean isDelete;

    /**
     * 获取ID
     * @return ID
     */
    @DocumentId
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
    @TableGenerator(name = "tableGenerator", table = "id_maker", pkColumnName = "maker_name",
            valueColumnName = "maker_value", pkColumnValue = "doctor", allocationSize = 1)
    public Long getId() {
        return super.getId();
    }


    @Basic
    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "active_state")
    public byte getActiveState() {
        return activeState;
    }

    public void setActiveState(byte activeState) {
        this.activeState = activeState;
    }

    @Basic
    @Column(name = "delete_state")
    public byte getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(byte deleteState) {
        this.deleteState = deleteState;
    }

    @Basic
    @Column(name = "created")
    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorAccount that = (DoctorAccount) o;
        if (activeState != that.activeState) return false;
        if (deleteState != that.deleteState) return false;
        if (created != that.created) return false;
        if (mobile != null ? !mobile.equals(that.mobile) : that.mobile != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    /**
     * 获取医生信息
     * @return 医生信息
     */
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public DoctorProfile getDoctorProfile() {
        return doctorProfile;
    }

    public void setDoctorProfile(DoctorProfile doctorProfile) {
        this.doctorProfile = doctorProfile;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean getIsDelete() {
        return isDelete;
    }
}
