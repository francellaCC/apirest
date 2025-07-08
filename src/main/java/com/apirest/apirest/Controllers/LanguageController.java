package com.apirest.apirest.Controllers;

import com.apirest.apirest.Entities.Language;
import com.apirest.apirest.Entities.User;
import com.apirest.apirest.Repositories.LanguageRepository;
import com.apirest.apirest.Repositories.ModuleRepository;
import com.apirest.apirest.Repositories.UserRepository;
import com.apirest.apirest.dto.LanguajeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/languages")
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @GetMapping
    public ResponseEntity<List<LanguajeDTO>> getAllLanguages() {
        List<LanguajeDTO>  languajes = languageRepository.findAll().stream().
                map(language -> new LanguajeDTO(
                        language.getId(),
                        language.getName(),
                        language.getCode()
                )).collect(Collectors.toList());

        return ResponseEntity.ok(languajes);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createLanguage(@RequestBody Language languageRequest, @AuthenticationPrincipal Jwt jwt) {
        if (languageRepository.existsByCode(languageRequest.getCode())) {
            return ResponseEntity.badRequest().body(null);
        }

        // Obtener email del usuario autenticado desde el Principal
        String email = jwt.getClaimAsString("email");

        System.out.println(email);
        // Buscar usuario en BD
        Optional<User> existingUser = userRepository.findByEmail(email);


        // Validar que es admin (roleUser == 1)
        if (existingUser.get().getRoleUser() != 1) {
            System.out.println(existingUser.get().getRoleUser());
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLanguage(@PathVariable Long  id, @RequestBody Language languageRequest , @AuthenticationPrincipal Jwt jwt){
        String email = jwt.getClaimAsString("email");

        System.out.println(jwt);
        System.out.println("Email del token: " + email);

        // Buscar si el usuario ya existe
        Optional<User> existingUser = userRepository.findByEmail(email);

        // Validar que es admin (roleUser == 1)
        if (existingUser.get().getRoleUser() != 1) {
            System.out.println(existingUser.get().getRoleUser());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found"));

        language.setName(languageRequest.getName());
        language.setCode(languageRequest.getCode());

        languageRepository.save(language);
        return ResponseEntity.ok(Map.of("message", "Idioma actualizado correctamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLanguage(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isEmpty() || existingUser.get().getRoleUser() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found"));

        // Opcional: Verificar si hay módulos asociados a este idioma
        if (!moduleRepository.findByLanguageId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar el idioma porque tiene módulos asociados");
        }

        languageRepository.delete(language);
        return ResponseEntity.ok(Map.of("message", "Idioma eliminado correctamente"));
    }

    @GetMapping("/{id}")
    public  ResponseEntity<?> getLanguageById(@PathVariable Long id){
        Optional<Language> language = languageRepository.findById(id);

        if(language.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Lenguaje no encontrado"));
        }

        return ResponseEntity.ok(language.get());
    }


}
