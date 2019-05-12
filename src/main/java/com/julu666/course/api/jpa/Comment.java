package com.julu666.course.api.jpa;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comments")
public class Comment extends Base implements Serializable {
    private String commentId;
    private String userId;
    private String content;
    private String answerId;


    @PrePersist
    void onPrePersist() {
        this.commentId = UUID.randomUUID().toString();
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User user;

}
