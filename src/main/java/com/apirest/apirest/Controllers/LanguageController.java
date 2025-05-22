package com.apirest.apirest.Controllers;

import com.apirest.apirest.Entities.Language;
import com.apirest.apirest.Entities.User;
import com.apirest.apirest.Repositories.LanguageRepository;
import com.apirest.apirest.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createLanguage(@RequestBody Language languageRequest, @AuthenticationPrincipal Jwt jwt) {
        if (languageRepository.existsByCode(languageRequest.getCode())) {
            return ResponseEntity.badRequest().body(null);
        }

        // Obtener email del usuario autenticado desde el Principal
        String email = jwt.getClaimAsString("email");

        // Buscar usuario en BD
        Optional<User> existingUser = userRepository.findByEmail(email);


        // Validar que es admin (roleUser == 1)
        if (existingUser.get().getRoleUser() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Language language = new Language();
        language.setName(languageRequest.getName());
        language.setCode(languageRequest.getCode());
        language.setCreatedBy(existingUser.get());

        languageRepository.save(language);

        System.out.println(language);
        return ResponseEntity.ok(Map.of("message", "Lenguaje creado correctamente"));
    }
}
