package com.julu666.course.api.jpa;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "apply_books")
public class ApplyBooks extends Base implements Serializable {
    private String userId;
    private Integer status;
    private String adminId;
    private Long bookId;

    @OneToOne
    @JoinColumn(name = "bookId", referencedColumnName = "id", insertable = false, updatable = false)
    private Books books;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User user;
}
