package com.apirest.apirest.Repositories;

import com.apirest.apirest.Entities.Language;
import com.apirest.apirest.Entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findById(Long id);
    List<Module> findByLanguageId(Long languageId);
}
