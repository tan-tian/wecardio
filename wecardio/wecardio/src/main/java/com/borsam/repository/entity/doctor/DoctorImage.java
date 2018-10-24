package com.borsam.repository.entity.doctor;

import javax.persistence.*;

/**
 * Created by tantian on 2017/6/15.
 */
@Entity
@Table(name = "profile_doctor_pic", schema = "wecardio_test")
public class DoctorImage {
    private long id;
    private DoctorProfile doctor;
    private String filePath;
    private String source;
    private String thumbnail;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Transient
    public boolean isEmpty() {
        return false;
    }

    public void setDoctor(DoctorProfile doctor) {
        this.doctor = doctor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "did", nullable = false)
    public DoctorProfile getDoctor() {
        return doctor;
    }

    @Basic
    @Column(name = "s_large")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Basic
    @Column(name = "s_thumbnail")
    public String getSource() {
        return source;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Basic
    @Column(name = "s_source")
    public String getThumbnail() {
        return thumbnail;
    }
}
