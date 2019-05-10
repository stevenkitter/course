package com.julu666.course.api.jpa;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sample_categories")
@Data
public class SampleCategories extends Base implements Serializable {
    private String name;
    private String icon;

    @JsonIgnore
    @OneToMany(mappedBy = "sampleCategories")
    private List<Books> books;
}
