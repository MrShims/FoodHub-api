package org.mrshim.authservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mrshim.authservice.dto.CredentialsDto;
import org.mrshim.authservice.dto.RequestCreateUserDto;
import org.mrshim.authservice.dto.RequestKeycloakCreateUserDto;
import org.mrshim.authservice.dto.RequestLoginDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${grant_type}")
    private String grantType;

    @Value("${client_id}")
    private String clientId;

    @Value("${client_secret}")
    private String clientSecret;


    private final WebClient.Builder webClientBuilder;


    private final ObjectMapper objectMapper;



    public void createNewUser(RequestCreateUserDto requestCreateUserDto)
    {


        RequestKeycloakCreateUserDto requestKeycloakCreateUserDto=new RequestKeycloakCreateUserDto();
        CredentialsDto credentialsDto=new CredentialsDto(requestCreateUserDto.getPassword());

        requestKeycloakCreateUserDto.setUsername(requestCreateUserDto.getUsername());
        requestKeycloakCreateUserDto.setFirstName(requestCreateUserDto.getFirstName());
        requestKeycloakCreateUserDto.setLastName(requestCreateUserDto.getLastName());
        requestKeycloakCreateUserDto.setEmail(requestCreateUserDto.getEmail());
        requestKeycloakCreateUserDto.setCredentials(List.of(credentialsDto));


        try {
            String newUser = objectMapper.writeValueAsString(requestKeycloakCreateUserDto);


            String adminToken = getAdminToken();


            webClientBuilder.defaultHeader("Authorization", "Bearer "+adminToken)
                    .build()
                    .post()
                    .uri("http://localhost:8888/admin/realms/FoodHub/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(newUser)
                    .retrieve()
                    .bodyToMono(HttpHeaders.class).block();





        } catch (JsonProcessingException e) {


            throw new RuntimeException(e);
        }


    }

    public String LoginUser(RequestLoginDto requestLoginDto)
    {

        String token = webClientBuilder.build().post()
                .uri("http://localhost:8888/realms/FoodHub/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("username", requestLoginDto.getUsername())
                        .with("password", requestLoginDto.getPassword())
                ).retrieve()
                .bodyToMono(String.class)
                .block();


        return token;



    }




    private String getAdminToken() {


        String token = webClientBuilder.build().post()
                .uri("http://localhost:8888/realms/FoodHub/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", grantType)
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(String.class)
                .block();


        try {
            JsonNode jsonNode = objectMapper.readValue(token, JsonNode.class);
            String accessToken = jsonNode.get("access_token").asText();

            return accessToken;


        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }


    }


}
