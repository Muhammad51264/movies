package com.task.movies.repository;

import com.task.movies.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

}