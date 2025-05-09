package com.apirest.apirest.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apirest.apirest.Entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
