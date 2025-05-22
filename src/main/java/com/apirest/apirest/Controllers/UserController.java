package com.apirest.apirest.Controllers;


import com.apirest.apirest.Entities.User;
import com.apirest.apirest.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

@PostMapping("/register")
public ResponseEntity<?> createUser(@RequestBody User user, @AuthenticationPrincipal Jwt jwt) {
    String email = jwt.getClaimAsString("email");

    System.out.println(jwt);
    System.out.println("Email del token: " + email);

    // Buscar si el usuario ya existe
    Optional<User> existingUser = userRepo.findByEmail(email);

    System.out.println("User desde frontend: " + user);

    if (existingUser.isEmpty()) {
        // No existe, crear nuevo usuario con el email del token
        user.setEmail(email);  // Sobrescribe el email que viene del frontend, por seguridad
        user.setRoleUser(2);
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuario registrado exitosamente"));

    } else {
        // Ya existe, validamos si el email del token coincide
        if (existingUser.get().getEmail().equals(email)) {
            return ResponseEntity.ok(Map.of("message", "Acceso permitido. Usuario ya registrado"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido para este usuario");
        }
    }
}

}
