package com.julu666.course.api.jpa;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "apply_teachers")
public class ApplyTeacher extends Base {
    private String userId;
    private String adminId = "";
    private String fileId;
    private String content = ""; // 老师申请的理由
    private Integer status = 0; // 0 申请中 1否定 2通过
    private String reason = ""; // 申请人填写的理由
}
