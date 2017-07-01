package com.borsam.repository.entity.patient;

import com.borsam.repository.entity.token.AccountToken;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity - 患者Token
 * Created by Sebarswee on 2015/6/27.
 */
@Entity
@Table(name = "patient_token")
public class PatientToken extends AccountToken {

    @Id
    @Column(name = "uid")
    @Override
    public Long getId() {
        return super.getId();
    }
}
