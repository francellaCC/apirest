package com.apirest.apirest.Controllers;


import com.apirest.apirest.Entities.Language;
import com.apirest.apirest.Entities.Module;
import com.apirest.apirest.Entities.User;
import com.apirest.apirest.Repositories.LanguageRepository;
import com.apirest.apirest.Repositories.ModuleRepository;
import com.apirest.apirest.Repositories.UserRepository;
import com.apirest.apirest.dto.LanguajeDTO;
import com.apirest.apirest.dto.ModuleDTO;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/modules")
public class ModuleController {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired

    private UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<ModuleDTO>> getAllModule() {
        List<Module> modules =  moduleRepository.findAll();
        List<ModuleDTO> dtos = modules.stream().map(module -> new ModuleDTO(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getOrder(),
                new LanguajeDTO(
                        module.getLanguage().getId(),
                        module.getLanguage().getName()
                )
        )).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createModule(@RequestBody Module moduleRequest, @AuthenticationPrincipal Jwt jwt ){
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

//        Optional<Language> language = languageRepository.findByCode(moduleRequest.getLanguage().getCode());

        Language language = languageRepository.findById(moduleRequest.getLanguage().getId())
                .orElseThrow(() -> new RuntimeException("Language not found"));


        Module module = new Module();
        module.setCreatedBy(existingUser.get());
        module.setDescription(moduleRequest.getDescription());
        module.setTitle(moduleRequest.getTitle());
        module.setOrder(moduleRequest.getOrder());
        module.setLanguage(language);

        moduleRepository.save(module);
        return ResponseEntity.ok(Map.of("message", "Modulo creado correctamente"));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateModule( @PathVariable Long id, @RequestBody Module moduleRequest , @AuthenticationPrincipal Jwt jwt){
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

        Module module = moduleRepository.findById(id).orElseThrow(()-> new RuntimeException(("Module not found")));

        //validar que el lenguaje enviado no sea null
        if(moduleRequest.getLanguage()== null || moduleRequest.getLanguage().getId() == null){
            return ResponseEntity.badRequest().body("Lenguage ID is required");
        }

        Language  language = languageRepository.findById(moduleRequest.getLanguage().getId())
                .orElseThrow(()-> new RuntimeException("Language not found"));


        module.setTitle(moduleRequest.getTitle());
        module.setDescription(moduleRequest.getDescription());
        module.setOrder(moduleRequest.getOrder());
        module.setLanguage(language);

        moduleRepository.save(module);
        return ResponseEntity.ok(Map.of("message", "Módulo actualizado correctamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModule(@PathVariable Long id , @AuthenticationPrincipal Jwt jwt){
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

        Module module = moduleRepository.findById(id).orElseThrow(()-> new RuntimeException("Module not Found"));

        moduleRepository.delete(module);

        return ResponseEntity.ok(Map.of("message", "Módulo eliminado correctamente"));
    }

    @GetMapping("/{id}")
    public  ResponseEntity<?> getModuleById(@PathVariable Long id){
        Optional<Module> moduleOpt = moduleRepository.findById(id);
        if (moduleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Modulo no encontrado"));
        }
        Module module = moduleOpt.get();
        ModuleDTO dto = new ModuleDTO(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getOrder(),
                new LanguajeDTO(
                        module.getLanguage().getId(),
                        module.getLanguage().getName()
                )
        );

        return ResponseEntity.ok(dto);
    }
}
