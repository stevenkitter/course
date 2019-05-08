package com.julu666.course.api.jpa;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.UUID;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends Base{
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

    @PrePersist
    void onPrePersist() {
        this.userId = UUID.randomUUID().toString();
    }
}
