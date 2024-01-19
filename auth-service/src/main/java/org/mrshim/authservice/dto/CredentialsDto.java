package org.mrshim.authservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CredentialsDto {

    private String type;
    private String value;
    private String temporary;

    public CredentialsDto(String value) {
        this.type = "password";
        this.value = value;
        this.temporary = "false";
    }
}
