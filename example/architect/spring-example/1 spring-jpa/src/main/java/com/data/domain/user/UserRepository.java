package com.data.domain.user;

import com.data.datasource.m3_dynamic.TargetSource;
import com.data.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User>  findAll();
    User findById(Long id);
}
