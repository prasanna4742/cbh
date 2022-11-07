package com.prasanna.cbhapp.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prasanna.cbhapp.errors.RecordNotFoundException;
import com.prasanna.cbhapp.model.Employee;
import com.prasanna.cbhapp.model.SalarySummaryPOJO;
import com.prasanna.cbhapp.service.EmployeeService;
import com.prasanna.cbhapp.service.SalaryFilter;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@RestController()
@RequestMapping("/api")
@SecurityScheme(
    name = "Bearer Authentication", type = SecuritySchemeType.HTTP, 
    bearerFormat = "JWT", scheme = "bearer")
@SecurityRequirement(name = "Bearer Authentication")
@Validated
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PreAuthorize("hasRole('cbh-readrole')")
    @GetMapping(path="/employees", produces={MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee  record/s fetched.",
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = Employee.class)) } ),
        @ApiResponse(responseCode = "401", description = "User not authenticated, no bearer token passed", content = @Content),
        @ApiResponse(responseCode = "403", description = "User not authorized to access this resource ", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    public ResponseEntity<List<Employee>> getAllemployees() {
        return new ResponseEntity<>(employeeService.getAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('cbh-writerole')")
    @PostMapping(path="/employees", 
        consumes = {MediaType.APPLICATION_JSON_VALUE}, 
        produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee Record Created.",
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = Employee.class)) } ),
            @ApiResponse(responseCode = "401", description = "User not authenticated, no bearer token passed", content = @Content),
            @ApiResponse(responseCode = "403", description = "User not authorized to access this resource ", content = @Content),    
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })    
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee ) {

        Employee emp = employeeService.create(employee);
        return new ResponseEntity<Employee>(emp, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('cbh-writerole')")
    @DeleteMapping(path="/employees/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})  
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Employee Record Deleted.",
                        content = @Content),
        @ApiResponse(responseCode = "404", description = "Invalid employee id provided.",
                        content = @Content),
        @ApiResponse(responseCode = "401", description = "User not authenticated, no bearer token passed", content = @Content),
        @ApiResponse(responseCode = "403", description = "User not authorized to access this resource ", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })    
    public ResponseEntity<String> deleteEmployee(@PathVariable("employeeId") long employeeId) {

        Optional<Employee> employee = employeeService.get(employeeId);
        if(employee.isPresent()){
            employeeService.delete(employeeId);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        }
        else {
            throw new RecordNotFoundException("Invalid employee id:"+employeeId);
        }
    }

    @PreAuthorize("hasRole('cbh-readrole')")
    @GetMapping(path="/employees/salarySummary",produces={MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salary summary record/s fetched.",
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = SalarySummaryPOJO.class)) } ),
        @ApiResponse(responseCode = "401", description = "User not authenticated, no bearer token passed", content = @Content),
        @ApiResponse(responseCode = "403", description = "User not authorized to access this resource ", content = @Content),    
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })        
    public ResponseEntity<SalarySummaryPOJO> getOverallSalarySummary() {
        return new ResponseEntity<SalarySummaryPOJO>
            (employeeService.getFilteredSalarySummary(null), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('cbh-readrole')")
    @GetMapping(path="/employees/serchableSalarySummary", produces={MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Overall Salary summary record/s fetched.",
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = SalarySummaryPOJO.class)) } ),
        @ApiResponse(responseCode = "401", description = "User not authenticated, no bearer token passed", content = @Content),
        @ApiResponse(responseCode = "403", description = "User not authorized to access this resource ", content = @Content),    
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })            
    public ResponseEntity<SalarySummaryPOJO> filteredSalarySummary(
        // @RequestParam(value = "department", required = false) String department
        // We could easily add this filter as well but not a requirement right now.
        @RequestParam(value = "on_contract", required = false) Boolean onContractBoolean){
        if(onContractBoolean != null){
            return new ResponseEntity<>
                (employeeService.getFilteredSalarySummary(new SalaryFilter(onContractBoolean)), HttpStatus.OK);
        }

        return new ResponseEntity<>(employeeService.getFilteredSalarySummary(null), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('cbh-readrole')")
    @GetMapping(path="/employees/salarySummaryByDepartment", produces={MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salary summary record/s by department fetched.",
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = SalarySummaryPOJO.class)) } ),
        @ApiResponse(responseCode = "401", description = "User not authenticated, no bearer token passed", content = @Content),
        @ApiResponse(responseCode = "403", description = "User not authorized to access this resource ", content = @Content),    
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })        
    public ResponseEntity<List<SalarySummaryPOJO>> salarySummaryByDepartment(){
        return new ResponseEntity<>(employeeService.getsalarySummaryByDepartment(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('cbh-readrole')")
    @GetMapping(path="/employees/salarySummaryBySubDepartment", produces={MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salary summary record/s by sub-department fetched.",
            content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = SalarySummaryPOJO.class)) } ),
        @ApiResponse(responseCode = "401", description = "User not authenticated, no bearer token passed", content = @Content),
        @ApiResponse(responseCode = "403", description = "User not authorized to access this resource ", content = @Content),        
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })        
    public ResponseEntity<List<SalarySummaryPOJO>> salarySummaryBySubDepartment(){
        return new ResponseEntity<>(employeeService.getsalarySummaryBySubDepartment(), HttpStatus.OK);
    }
}