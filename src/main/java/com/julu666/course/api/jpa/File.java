package com.julu666.course.api.jpa;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "files")
public class File extends Base {
    private String fileId;
    private String userId;
    private String fileName;
    private String thumbnailName;
    private String sourceId;
}
