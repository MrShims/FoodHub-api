package org.mrshim.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateUserDto {

   private String username;
   private String firstName;
   private String lastName;
   private String email;
   private String password;
   private String confirmPassword;
}
