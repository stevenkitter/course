package com.julu666.course.api.jpa;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;


import javax.persistence.*;
import java.io.Serializable;

//INSERT INTO books (name, author, price,category_id) VALUES ('网络营销实用教程', '温明剑', 3500, 1)

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "books")
public class Books extends Base implements Serializable {
    private String name;
    private String icon;
    private String author;
    private Integer price;
    private Long categoryId;



    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "books"})
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    private SampleCategories sampleCategories;

}
