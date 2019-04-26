package com.julu666.course.api.jpa;



import lombok.Data;

import javax.persistence.*;
import java.sql.Time;

@Data
@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;
    @Column(name = "created_at")
    private Time created_at;
    @Column(name = "updated_at")
    private Time updated_at;
}
