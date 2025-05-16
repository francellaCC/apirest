package com.apirest.apirest.Controllers;


import com.apirest.apirest.Entities.User;
import com.apirest.apirest.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;
//    @GetMapping
//    public ResponseEntity<String> obtenerTodosLosUsuarios() {
//        return new ResponseEntity<>("¡Obtención de usuarios exitosa!", HttpStatus.OK);
//    }

    @PostMapping("/register")
    public User createUser(@RequestBody User user){
        System.out.println("User:" + user);
        return userRepo.save(user);
    }
}
