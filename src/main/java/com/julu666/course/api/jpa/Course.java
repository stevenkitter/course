package com.julu666.course.api.jpa;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "courses")
public class Course extends Base {
    private String courseId;
    private String userId;
    private String title;
    private String description;
    @PrePersist
    void onPrePersist() {
        this.courseId = UUID.randomUUID().toString();
    }

}


