package com.julu666.course.api.jpa;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "courses")
public class Course extends Base implements Serializable {
    private String courseId;
    private String userId;
    private String title;
    private String description;

    @JsonIgnoreProperties("course")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", referencedColumnName="sourceId",  insertable = false, updatable = false)
    private TKFile tkFile;

    @PrePersist
    void onPrePersist() {
        this.courseId = UUID.randomUUID().toString();
    }

}


