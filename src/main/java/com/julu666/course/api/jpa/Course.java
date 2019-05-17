package com.julu666.course.api.jpa;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
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
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sourceId", referencedColumnName="courseId",  insertable = false, updatable = false)
    private List<TKFile> tkFiles;

    @PrePersist
    void onPrePersist() {
        this.courseId = UUID.randomUUID().toString();
    }

}


