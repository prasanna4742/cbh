package com.prasanna.cbhapp.controller;

import javax.validation.Valid;

import org.apache.http.entity.ContentType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.prasanna.cbhapp.model.Credentials;

@RestController()
@Validated
public class TokenController {

    private static String requestStatic = "username=USERNAME&password=PASSWORD&client_id=cbh-api-client&grant_type=password&client_secret=Msmkh5YqShf2qYENDXYAXJdwZseUoRwu";
    @PostMapping(path="/token")
    public ResponseEntity<String> getToken(@RequestBody @Valid Credentials creds){
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
    
        headers.add(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString());

        String requestBody = requestStatic.replaceAll("USERNAME", creds.getUsername()).replaceAll("PASSWORD", creds.getPassword());

        HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<String> responseEntity 
            = rest.exchange(
                "http://keycloak:8080/realms/cbh/protocol/openid-connect/token", 
                HttpMethod.POST, requestEntity, String.class);
        
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return new ResponseEntity<String>(responseEntity.getBody(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>(
                "Could not get token.", responseEntity.getStatusCode());
        }
    }    
}