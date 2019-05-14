package com.julu666.course.api.jpa;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "answers")
public class Answer extends Base implements Serializable {

    private String answerId;
    private String userId;
    private String title;
    private String content;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User user;


    @JsonIgnoreProperties({"answer"})
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "sourceId", referencedColumnName="answerId",  insertable = false, updatable = false)
    private List<TKFile> tkFiles;

    @PrePersist
    void onPrePersist() {
        this.answerId = UUID.randomUUID().toString();
    }


    @JsonInclude
    @Transient
    private String createTime;
}
