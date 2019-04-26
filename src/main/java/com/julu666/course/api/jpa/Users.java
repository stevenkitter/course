package com.julu666.course.api.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class Users extends Base{
    private String userId = "das";
    private Integer userRole = 0;
    private String wxSessionId = "";
    private String wxOpenId = "";
    private String wxSessionKey = "";
    private String wxUnionid = "";
    private String wxNickName = "";
    private String avatarUrl = "";
    private Integer gender = 0;
    private String country = "";
    private String province = "";
    private String city = "";
    private String language = "";
}
