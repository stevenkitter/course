package com.julu666.course.api.jpa;



import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
public class Base implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;

    private Timestamp created_at;

    private Timestamp updated_at;
}
