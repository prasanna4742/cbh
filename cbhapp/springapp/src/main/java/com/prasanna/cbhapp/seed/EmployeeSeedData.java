package com.prasanna.cbhapp.seed;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasanna.cbhapp.model.Employee;
import com.prasanna.cbhapp.repository.EmployeeRepository;

@Component
public class EmployeeSeedData {

    @Value("classpath:employeeDataSet.json")
    Resource resourceFile;

    @Autowired
    EmployeeRepository employeeRepository;


    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        // System.out.println("Add initial dataset to employee DB.");
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream jsonStream = resourceFile.getInputStream()) {
                List<Employee> empList =mapper.readValue(jsonStream,
                        new TypeReference<List<Employee>>(){});

                employeeRepository.saveAll(empList);
                //employeeRepository.findAll().forEach(System.out::println);

            } catch (IOException e) {
                e.printStackTrace();            
        }
    }
}