package com.prasanna.cbhapp.errors;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse 
{
  public ErrorResponse(String message, List<String> details) {
    this.message = message;
    this.details = details;
  }
 
  private String message;
 
  private List<String> details; 
 }