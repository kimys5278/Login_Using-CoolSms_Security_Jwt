package com.example.nodebackend.data.repository;

import com.example.nodebackend.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User getByUserId(String userId);
}
