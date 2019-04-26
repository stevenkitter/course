package com.julu666.course.api.services;


import com.julu666.course.api.jpa.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users, Integer> {


}
