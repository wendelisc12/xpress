package com.example.xpress.DTO;

import com.example.xpress.entities.UserRole;

public record RegisterDTO(String name, String email, String password, String cpf,  UserRole role) {
}
