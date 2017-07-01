package com.borsam.repository.entity.doctor;

import javax.persistence.*;

/**
 * Created by Tony on 2017/6/15.
 */
@Entity
@Table(name = "doctor_token", schema = "wecardio_test")
public class DoctorToken {
    private long did;
    private String token;
    private int updated;

    @Id
    @Column(name = "did")
    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "updated")
    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorToken that = (DoctorToken) o;

        if (did != that.did) return false;
        if (updated != that.updated) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (did ^ (did >>> 32));
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + updated;
        return result;
    }
}
