package com.borsam.repository.entity.doctor;

import com.borsam.repository.entity.token.AccountToken;

import javax.persistence.*;

/**
 * Created by tantian on 2017/6/15.
 */
@Entity
@Table(name = "doctor_token", schema = "wecardio_test")
public class DoctorToken extends AccountToken {

    @Id
    @Column(name = "did")
    @Override
    public Long getId() {
        return super.getId();
    }

    public boolean hasExpired() {
        return true;
    }
}
