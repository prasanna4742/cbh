package com.prasanna.cbhapp.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credentials {

    public Credentials(){

    }

    public Credentials(String username, String password){
        this.username = username;
        this.password = password;
    }

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;
}
