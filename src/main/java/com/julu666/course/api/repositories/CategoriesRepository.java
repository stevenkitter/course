package com.julu666.course.api.repositories;


import com.julu666.course.api.jpa.SampleCategories;
import org.springframework.data.repository.CrudRepository;

public interface CategoriesRepository extends CrudRepository<SampleCategories, Long> {


}
