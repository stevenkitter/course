package com.julu666.course.api.repositories;

import com.julu666.course.api.jpa.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, Long> {

    Optional<Admin> findByPhone(String phone);
}
