package com.julu666.course.api.jpa;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "apply_teachers")
public class ApplyTeacher extends Base implements Serializable {
    private String userId;
    private String adminId = "";
    private String atId;
    private String content = ""; // 老师申请的理由
    private Integer status = 0; // 0 申请中 1否定 2通过
    private String reason = ""; // 申请人填写的理由


    //relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atId", referencedColumnName = "sourceId", insertable = false, updatable = false)
    private TKFile tkFile;


    @PrePersist
    void onPrePersist() {
        this.atId = UUID.randomUUID().toString();
    }
}
