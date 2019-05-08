package com.julu666.course.api.repositories;


import com.julu666.course.api.jpa.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByWxOpenId(String wxOpenId);
    Optional<User> findByUserId(String userId);

}


