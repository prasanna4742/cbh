package com.prasanna.cbhapp;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasanna.cbhapp.model.Employee;
import com.prasanna.cbhapp.model.SalarySummaryPOJO;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class CBHFEatureStepsTest extends CbhTestCommonTest {

    private String validToken;

    private RequestSpecification apiRequest = RestAssured.given()
        .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
        .header(HttpHeaders.ACCEPT, ContentType.JSON);

    private Response response;
    
    @Given("The cbh api app is running")
    public void verifyAPIAppRunning(){
        assertTrue(baseVerifyAPIAppRunning()==HttpStatus.SC_OK);
    }

    @Given("Valid token exists")
    public void Valid_token_exists() {
        validToken = getToken("cbh-write", "cbh-write"); 
        assertTrue(!validToken.isEmpty());
        //set it in request object
        apiRequest.header(HttpHeaders.AUTHORIZATION, "Bearer "+validToken);
    }

    @When("POST operation on employees is performed")
    public void POST_operation_on_employees_is_performed() {

        Employee testEmployee = 
            new Employee("prasanna", 200000L, "USD", "R&D", "Innovation", false);

        response = apiRequest.request().body(testEmployee)
            .post(apiURI);
    }

    @When("Get operation on employees is performed")
    public void Get_operation_on_employees_is_invoked() {
        response = apiRequest.request()
            .get(apiURI);   
    }

    @Then("Response should be {int}")
    public void Response_should_be(int code) {
        response.then().statusCode(code);
    }

    @When("Delete operation on employees is performed")
    public void Delete_operation_on_employees_is_performed() {

        Get_operation_on_employees_is_invoked();
        if(response.statusCode() != HttpStatus.SC_OK){
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode arrNode = mapper.readTree(response.body().asString());
            for(JsonNode node : arrNode){
                String id = node.get("id").asText();
                response = apiRequest.request().delete(apiURI+"/"+id);
                break;                
            }
        } catch (JsonProcessingException e) {
            
            e.printStackTrace();
        }
    }

    @Given("Token does not exist")
    public void Token_does_not_exist() {
        //NoOP, do not set auth header, for 401 case
   }

    @Given("Invalid token exists")
    public void Invalid_token_exists() {
        validToken = getToken("cbh-norole", "cbh-norole"); 
        assertTrue(!validToken.isEmpty());
        //set it in request object
        apiRequest.header(HttpHeaders.AUTHORIZATION, "Bearer "+validToken);
       // Write code here that turns the phrase above into concrete actions
    }

    @When("salarySummary API is called")
    public void salarySummary_API_is_called() {

        // response = RestAssured.given()
        //     .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
        //     .header(HttpHeaders.ACCEPT, ContentType.JSON)
        //     .header(HttpHeaders.AUTHORIZATION, "Bearer "+validToken) 
        response = apiRequest.request()
            .get(apiURI+"/salarySummary");
    }

    private Employee[] getAllEmployeesData(){
        Get_operation_on_employees_is_invoked();
        if(response.statusCode() != HttpStatus.SC_OK){
            return null;
        }
        Employee[] empArr = response.body().as(Employee[].class);
        if(empArr == null || empArr.length == 0){
            return null;
        }
        return empArr;
    }

    @Then("SS for entire dataset should be validated")
    public void SS_for_entire_dataset_should_be_validated() {
        SalarySummaryPOJO ssPOJO = response.body().as(SalarySummaryPOJO.class);
        // System.out.println(ssPOJO);

        // Get_operation_on_employees_is_invoked();
        // if(response.statusCode() != HttpStatus.SC_OK){
        //     return;
        // }
        // Employee[] empArr = response.body().as(Employee[].class);
        // if(empArr == null || empArr.length == 0){
        //     return;
        // }
        Employee[] empArr = getAllEmployeesData();
        if(empArr == null){
            return;
        }        
        SalarySummaryPOJO expectedPOJO = calculateSS(empArr);
        
        assertTrue(expectedPOJO.getMin() == ssPOJO.getMin(), "Assert min salary calculation");
        assertTrue(expectedPOJO.getMax() == ssPOJO.getMax(), "Assert max salary calculation");
        assertTrue(expectedPOJO.getAvg() == ssPOJO.getAvg(), "Assert avg salary calculation");        
    }

    private SalarySummaryPOJO calculateSS(Employee[] empArr){
        long min=empArr[0].getSalary(), max=0;
        double avg=0;
        for (Employee e : empArr){
            long sal = e.getSalary();
            if(sal < min){
                min = sal;
            }
            if(sal > max){
                max = sal;
            }
            avg = avg + sal;
        }
        avg = avg/empArr.length;
        return new SalarySummaryPOJO(min, avg, max);
    }

    @When("serchableSalarySummary API is called")
    public void serchableSalarySummary_API_is_called() {

        response = apiRequest.request()
            .param("on_contract", "true")
            .get(apiURI+"/serchableSalarySummary");
    }

    @Then("SS for on contract employees should be validated")
    public void SS_for_on_contract_employees_should_be_validated() {
        SalarySummaryPOJO ssPOJO = response.body().as(SalarySummaryPOJO.class);
        Employee[] empArr = getAllEmployeesData();
        if(empArr == null){
            return;
        }        

        List<Employee> empList = new ArrayList<Employee>();
        List<Employee> onContractEmpList = new ArrayList<Employee>();
        Collections.addAll(empList, empArr);
        empList.stream()
            .filter(e -> e.isOnContract()==true)
            .forEach(onContractEmpList::add);

        empArr = onContractEmpList.toArray(new Employee[0]);
        SalarySummaryPOJO expectedPOJO = calculateSS(empArr);
        
        assertTrue(expectedPOJO.getMin() == ssPOJO.getMin(), "Assert min salary calculation");
        assertTrue(expectedPOJO.getMax() == ssPOJO.getMax(), "Assert max salary calculation");
        assertTrue(expectedPOJO.getAvg() == ssPOJO.getAvg(), "Assert avg salary calculation");                
    }

    @When("salarySummaryByDepartment API is called")
    public void salarySummaryByDepartment_API_is_called() {
        response = apiRequest.request()
        .get(apiURI+"/salarySummaryByDepartment");        
    }

    @Then("SS by department should be validated")
    public void SS_by_department_should_be_validated() {
        SalarySummaryPOJO[] ssPOJOArr = response.body().as(SalarySummaryPOJO[].class);
        if(ssPOJOArr == null || ssPOJOArr.length == 0){
            return;
        }
        
        Employee[] empArr = getAllEmployeesData();
        if(empArr == null){
            return;
        }        
        List<Employee> empList = new ArrayList<Employee>();
        Collections.addAll(empList, empArr);
        Map<String, List<Employee>> empPerDept = 
            empList.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));

        for(SalarySummaryPOJO ssPOJO : ssPOJOArr){
            List<Employee> deptEmpList = empPerDept.get(ssPOJO.getDepartment());
            Employee[] deptEmpArr = deptEmpList.toArray(new Employee[0]);
            SalarySummaryPOJO expectedPOJO = calculateSS(deptEmpArr);

            assertTrue(expectedPOJO.getMin() == ssPOJO.getMin(), "Assert min salary calculation for" + ssPOJO.getDepartment());
            assertTrue(expectedPOJO.getMax() == ssPOJO.getMax(), "Assert max salary calculation for" + ssPOJO.getDepartment());
            assertTrue(expectedPOJO.getAvg() == ssPOJO.getAvg(), "Assert avg salary calculation for" + ssPOJO.getDepartment());                    
        }
     }

    @When("salarySummaryBySubDepartment API is called")
    public void salarySummaryBySubDepartment_API_is_called() {
        response = apiRequest.request()
        .get(apiURI+"/salarySummaryBySubDepartment");        
    }

    @Then("SS by salarySummaryBySubDepartment should be validated")
    public void SS_by_salarySummaryBySubDepartment_should_be_validated() {

        // System.out.println(response.body());
        // String output = response.body().asString();
        // ObjectMapper mapper = new ObjectMapper();

        // SalarySummaryPOJO[] ssPOJOArr = new SalarySummaryPOJO[0];
        // try {
        //     ssPOJOArr = mapper.readValue(output, SalarySummaryPOJO[].class);
        // } catch (JsonProcessingException e1) {
        //     e1.printStackTrace();
        // }

        // if(ssPOJOArr == null || ssPOJOArr.length == 0){
        //     return;
        // }


        SalarySummaryPOJO[] ssPOJOArr = response.body().as(SalarySummaryPOJO[].class);
        if(ssPOJOArr == null || ssPOJOArr.length == 0){
            return;
        }
        
        Employee[] empArr = getAllEmployeesData();
        if(empArr == null){
            return;
        }        
        List<Employee> empList = new ArrayList<Employee>();
        Collections.addAll(empList, empArr);
        Map<String, List<Employee>> empPerDeptSubDept = 
            empList.stream()
            .collect(Collectors.groupingBy(e -> e.getDepartment()+","+e.getSubDepartment()));

        for(SalarySummaryPOJO ssPOJO : ssPOJOArr){
            List<Employee> deptEmpList = empPerDeptSubDept.get(ssPOJO.getDepartment()+","+ssPOJO.getSubDepartment());
            Employee[] deptEmpArr = deptEmpList.toArray(new Employee[0]);
            SalarySummaryPOJO expectedPOJO = calculateSS(deptEmpArr);

            assertTrue(expectedPOJO.getMin() == ssPOJO.getMin(), "Assert min salary calculation for" + ssPOJO.getDepartment());
            assertTrue(expectedPOJO.getMax() == ssPOJO.getMax(), "Assert max salary calculation for" + ssPOJO.getDepartment());
            assertTrue(expectedPOJO.getAvg() == ssPOJO.getAvg(), "Assert avg salary calculation for" + ssPOJO.getDepartment());                    
        }
    }
}