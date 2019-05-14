package com.julu666.course.api.jpa;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends Base implements Serializable {
    private String userId = "";
    private Integer userRole = 0;

    @JsonIgnore
    private String wxOpenId = "";

    @JsonIgnore
    private String wxSessionKey = "";

    @JsonIgnore
    @Column(name = "wx_unionid")
    private String wxUnionid = "";

    @JsonProperty(value = "nickName")
    private String wxNickName = "";

    private String avatarUrl = "";
    private Integer gender = 0;
    private String country = "";
    private String province = "";
    private String city = "";
    private String language = "";


    //relations
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private List<ApplyTeacher> applies = new ArrayList<>();

    @PrePersist
    void onPrePersist() {
        this.userId = UUID.randomUUID().toString();
    }


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "usersExercises", joinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId",insertable = false, updatable = false), inverseJoinColumns = @JoinColumn(name = "exerciseId", referencedColumnName = "exerciseId",insertable = false, updatable = false))
    private List<Exercise> exercises;
}
