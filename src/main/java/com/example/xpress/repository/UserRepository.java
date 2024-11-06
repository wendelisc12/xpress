package com.example.xpress.repository;

import com.example.xpress.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<Users, Long> {
     UserDetails findByEmail(String email);
}
