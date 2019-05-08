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
@Table(name = "admins")
public class Admin extends Base {
    private String userId;
    private String phone;
    private String password;

    @PrePersist
    void onPrePersist() {
        this.userId = UUID.randomUUID().toString();
    }
}
