package com.julu666.course.api.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "exercises")

public class Exercise extends Base {
    private String exerciseId;
    private String userId;
    private Long price;
    private String title;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exerciseId", referencedColumnName="sourceId",  insertable = false, updatable = false)
    private TKFile tkFile;


    @PrePersist
    void onPrePersist() {
        this.exerciseId = UUID.randomUUID().toString();
    }
}
