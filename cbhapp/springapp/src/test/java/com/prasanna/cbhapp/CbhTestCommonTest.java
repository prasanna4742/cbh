package com.prasanna.cbhapp;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasanna.cbhapp.model.Credentials;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CbhTestCommonTest {
    public static final String baseURI = "http://cbh-api:8080/cbhapp";
    public static final String swaggerURI = baseURI + "/swagger-ui/index.html";
    public static final String tokenURI = baseURI + "/token";
    public static final String apiURI = baseURI + "/api/employees";
    
    public int baseVerifyAPIAppRunning(){
        return RestAssured.given()
            .get(swaggerURI)
            .statusCode();          
    }

    public String getToken(String username, String password) {
        Credentials creds = new Credentials (username, password);
        Response response = RestAssured.given()
            .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
            .request().body(creds)
            .post(tokenURI);

        if(response.statusCode() == HttpStatus.SC_OK){
            String responseBody = response.getBody().asString();

            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode jsonNode = mapper.readTree(responseBody);
                System.out.println(jsonNode);
                return jsonNode.get("access_token").asText();
    
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
