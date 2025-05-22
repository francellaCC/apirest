package com.apirest.apirest.Repositories;

import com.apirest.apirest.Entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByCode(String code);

    boolean existsByCode(String code); // Verifica si existe un idioma con ese codigo
}
