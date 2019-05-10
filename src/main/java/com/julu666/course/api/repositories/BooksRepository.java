package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Books;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BooksRepository extends CrudRepository<Books, Long> {

    List<Books> findByCategoryId(Long categoryId);
}
