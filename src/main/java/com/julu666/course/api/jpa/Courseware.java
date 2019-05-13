package com.julu666.course.api.jpa;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "coursewares")
public class Courseware extends Base {
    private String coursewareId;
    private String userId;
    private String title;
    private String content;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coursewareId", referencedColumnName="sourceId",  insertable = false, updatable = false)
    private TKFile tkFile;

    @PrePersist
    void onPrePersist() {
        this.coursewareId = UUID.randomUUID().toString();
    }
}
