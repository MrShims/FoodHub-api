package org.mrshim.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor

public class RequestKeycloakCreateUserDto {


    private String username;

    private String firstName;

    private String lastName;

    private String email;


    private boolean enabled;
    private boolean emailVerified;


    private List<CredentialsDto> credentials;


    public RequestKeycloakCreateUserDto()
    {
        this.enabled = true;
        this.emailVerified = false;

    }

    public RequestKeycloakCreateUserDto(String username, String firstName, String lastName, String email, List<CredentialsDto> credentials) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.credentials = credentials;
        this.enabled = true;
        this.emailVerified = false;
    }




}
