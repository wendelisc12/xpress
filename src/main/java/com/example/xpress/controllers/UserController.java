package com.example.xpress.controllers;

import com.example.xpress.entities.UsedCoupon;
import com.example.xpress.entities.Users;
import com.example.xpress.repository.UserRepository;
import com.example.xpress.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/{id}")
    public Users getUser(@PathVariable Long id){
        Users user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        return user;
    }

    @GetMapping("/all")
    public List<Users> getAllUsers(){
        List<Users> users = userRepository.findAll();
        return users;
    }

    @GetMapping
    public Users getUserLogged(@RequestHeader("Authorization") String token){
        Users user = tokenService.getUserByToken(token);
        return user;
    }

    @GetMapping("/coupons/{id}")
    public List<UsedCoupon> getCouponsByUser(@PathVariable Long id){
        Users user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        return user.getUsedCoupons();
    }


    @GetMapping("/points/{id}")
    public int getPoints(@RequestHeader("Authorization") String token){
        Users user = tokenService.getUserByToken(token);
        return user.getPoints();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
        return new ResponseEntity<>("User deleted with success.", HttpStatus.OK);
    }

    @PutMapping
    public Users editUser(@RequestBody Users user){
        Users userEdited = userRepository.save(user);
        return userEdited;
    }
}
