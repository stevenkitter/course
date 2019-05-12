package com.julu666.course.api.jpa;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "files")
public class TKFile extends Base implements Serializable {
    private String fileId;
    private String userId;
    private String fileName;
    private String thumbnailName;
    private String sourceId;

    @JsonIgnoreProperties("tkFile")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sourceId", referencedColumnName="courseId",  insertable = false, updatable = false)
    private Course course;


    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler","tkFiles"})
    @JoinColumn(name = "sourceId", referencedColumnName="answerId",  insertable = false, updatable = false)
    private Answer answer;

    @PrePersist
    void onPrePersist() {
        this.fileId = UUID.randomUUID().toString();
    }
}
