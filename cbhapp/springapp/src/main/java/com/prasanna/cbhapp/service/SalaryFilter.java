package com.prasanna.cbhapp.service;

public class SalaryFilter {
 
    private Boolean onContract;

    public SalaryFilter(){

    }
    public SalaryFilter(Boolean onContract){
        this.onContract = onContract;
    }

    public Boolean getOnContract() {
        return onContract;
    }

    public void setOnContract(Boolean onContract) {
        this.onContract = onContract;
    }    
}
