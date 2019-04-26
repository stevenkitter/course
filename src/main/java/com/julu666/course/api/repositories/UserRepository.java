package com.julu666.course.api.repositories;


import com.julu666.course.api.jpa.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users, Long> {


}
