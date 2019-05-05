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
@Table(name = "files")
public class TKFile extends Base {
    private String fileId;
    private String userId;
    private String fileName;
    private String thumbnailName;
    private String sourceId;

    @PrePersist
    void onPrePersist() {
        this.fileId = UUID.randomUUID().toString();
    }
}
