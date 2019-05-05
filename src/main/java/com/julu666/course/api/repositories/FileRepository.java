package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.TKFile;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<TKFile, Long> {

}
