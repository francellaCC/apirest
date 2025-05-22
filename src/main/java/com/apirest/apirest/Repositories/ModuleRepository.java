package com.apirest.apirest.Repositories;

import com.apirest.apirest.Entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {
}
