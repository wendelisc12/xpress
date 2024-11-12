package com.example.xpress.controllers;

import com.example.xpress.DTO.AuthenticationDTO;
import com.example.xpress.DTO.RegisterDTO;
import com.example.xpress.entities.Cart;
import com.example.xpress.entities.Users;
import com.example.xpress.repository.CartRepository;
import com.example.xpress.repository.UserRepository;
import com.example.xpress.secutiry.LoginResponseDTO;
import com.example.xpress.secutiry.SecurityFilter;
import com.example.xpress.service.TokenService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    CartRepository cartRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((Users) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token, "logged in successfully."));
    }

    @GetMapping("/users")
    public List<Users> getUsers(@RequestHeader String token){
        List<Users> results = this.userRepository.findAll();
        return results;
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(userRepository.findByEmail(data.email()) != null){
            throw new RuntimeException("this user already exists!");
        }else{
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            Cart cart = new Cart();
            Users newUser = new Users();
            newUser.setEmail(data.email());
            newUser.setPassword(encryptedPassword);
            newUser.setCpf(data.cpf());
            newUser.setName(data.name());
            newUser.setRole(data.role());
            newUser.setCart(cart);
            cart.setUser(newUser);

            userRepository.save(newUser);
            cartRepository.save(cart);

            return ResponseEntity.ok("Registered successfully");
        }
    }

    @PostMapping("/logoff")
    public ResponseEntity logoff(@RequestHeader("Authorization") String token){
        SecurityFilter.invalidateToken(token);
        return ResponseEntity.ok("Logged off successfully");
    }

    @GetMapping("/points")
    public int getPoints(@RequestHeader("Authorization") String token){
        Users user = tokenService.getUserByToken(token);
        return user.getPoints();
    }


}
