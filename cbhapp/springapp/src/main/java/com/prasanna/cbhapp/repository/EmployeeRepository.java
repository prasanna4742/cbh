package com.prasanna.cbhapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.prasanna.cbhapp.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>{
}
