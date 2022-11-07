package com.prasanna.cbhapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "employee")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Employee {

    public Employee(){
    }

    public Employee(String name, Long salary, String currency, String department, String subDepartment, Boolean onContract ){
        this.name = name;
        this.salary = salary;
        this.currency = currency;
        this.department = department;
        this.subDepartment = subDepartment;
        this.onContract = onContract;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull(message = "Name field cannot be empty.")
    @Size(min = 2, max = 30, message="Name must be minimum 2 chars and maximum 30 characters long.")
    private String name;

    @Column(name = "salary")
    @NotNull(message = "Salary field cannot be empty.")
    @Positive(message = "Salary must be greater than zero, you gotta pay your employees.")
    private Long salary;

    @Column(name = "currency")
    @Size(min = 3, max = 3, message = "Currency must be three characters long.")
    @NotNull(message = "Currency field cannot be empty.")
    private String currency;
    
    @Column(name = "department")
    @NotNull(message = "Department field cannot be empty.")
    @Size(min = 1, max = 100, message ="Department name must be between 1 to 100 characters.")
    private String department;
    
    @JsonProperty("sub_department")
    @Column(name = "sub_department")
    @NotNull(message = "Sub-department field cannot be empty.")
    @Size(min = 1, max = 100, message="Subdepartment name must be between 1 to 100 characters.")
    private String subDepartment;

    @JsonProperty("on_contract")
    @Column(name = "on_contract")
    private boolean onContract;
}