package org.mrshim.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.mrshim.authservice.dto.RequestCreateUserDto;
import org.mrshim.authservice.dto.RequestLoginDto;
import org.mrshim.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> loginUser(@RequestBody RequestCreateUserDto requestCreateUserDto) {
        if (!requestCreateUserDto.getPassword().equals(requestCreateUserDto.getConfirmPassword()))
        {
            return new ResponseEntity<>("Пароли не совпадают",HttpStatus.BAD_REQUEST);
        }
        authService.createNewUser(requestCreateUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createNewUser(@RequestBody RequestLoginDto requestLoginDto) {
        String response = authService.LoginUser(requestLoginDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
