package com.julu666.course.api.repositories;


import com.julu666.course.api.jpa.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {


}


