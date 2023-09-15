package org.mrshim.authservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class RequestLoginDto {


    private String username;

    private String password;


}
