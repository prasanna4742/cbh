package com.prasanna.cbhapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prasanna.cbhapp.model.Employee;
import com.prasanna.cbhapp.model.QEmployee;
import com.prasanna.cbhapp.model.SalarySummaryPOJO;
import com.prasanna.cbhapp.repository.EmployeeRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Transactional
@Service
public class EmployeeService {
 
    @Autowired
    EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Employee create(Employee employee){
        return employeeRepository.save(employee);
    }

    public Optional<Employee> get(Long id){
        return employeeRepository.findById(id);
    }

    public void delete(Long id){
        employeeRepository.deleteById(id);
    }

    public List<Employee> getAll(){
        List<Employee> empList = new ArrayList<Employee>();
        employeeRepository.findAll().forEach(empList::add);
        return empList;
    }

    // public SalarySummaryPOJO getSalarySummary() {
    //     CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    //     CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
    //     final Root dataRange = cq.from(Employee.class);

    //     cq.multiselect(
    //         cb.min(dataRange.get("salary")), 
    //         cb.avg(dataRange.get("salary")), 
    //         cb.max(dataRange.get("salary")));

    //     Object[] resultList = entityManager.createQuery(cq).getSingleResult();
    //     return new SalarySummaryPOJO(
    //         (Long)resultList[0], 
    //         (Double)resultList[1], 
    //         (Long)resultList[2]); 
    // }    

    @SuppressWarnings({"unchecked"})
    public SalarySummaryPOJO getFilteredSalarySummary(SalaryFilter salaryFilter) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QEmployee employee = QEmployee.employee;
        JPAQuery<QEmployee> tuple = (JPAQuery<QEmployee>) queryFactory.from(employee);

        //Add where conditions if needed
        if(salaryFilter != null && salaryFilter.getOnContract() != null){
            tuple = tuple.where(employee.onContract.eq(salaryFilter.getOnContract()));
        }        
        Tuple result = 
            tuple
                .select(employee.salary.min().longValue(), 
                        employee.salary.avg().doubleValue(), 
                        employee.salary.max().longValue())
                .fetchFirst();

        return new SalarySummaryPOJO(
            result.get(employee.salary.min().longValue()),
            result.get(employee.salary.avg().doubleValue()),
            result.get(employee.salary.max().longValue()));
    }

    @SuppressWarnings({"unchecked"})
    public List<SalarySummaryPOJO> getsalarySummaryByDepartment() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QEmployee employee = QEmployee.employee;
        JPAQuery<QEmployee> tuple = (JPAQuery<QEmployee>) queryFactory.from(employee);

        List<SalarySummaryPOJO> salSummaryList = new ArrayList<SalarySummaryPOJO>();
        tuple
            .select(employee.department,
                employee.salary.min().longValue(), 
                employee.salary.avg().doubleValue(), 
                employee.salary.max().longValue())
            .groupBy(employee.department)
            .fetch()
            .forEach(r -> salSummaryList.add(new SalarySummaryPOJO(
                    r.get(employee.department),
                    r.get(employee.salary.min().longValue()),
                    r.get(employee.salary.avg().doubleValue()),
                    r.get(employee.salary.max().longValue()))
            ));
        return salSummaryList;
    }

    @SuppressWarnings({"unchecked"})
    public List<SalarySummaryPOJO> getsalarySummaryBySubDepartment() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QEmployee employee = QEmployee.employee;
        JPAQuery<QEmployee> tuple = (JPAQuery<QEmployee>) queryFactory.from(employee);

        List<SalarySummaryPOJO> salSummaryList = new ArrayList<SalarySummaryPOJO>();
        tuple
            .select(
                employee.department,
                employee.subDepartment,
                employee.salary.min().longValue(), 
                employee.salary.avg().doubleValue(), 
                employee.salary.max().longValue())
            .groupBy(employee.department)
            .groupBy(employee.subDepartment)
            .fetch()
            .forEach(r -> salSummaryList.add(new SalarySummaryPOJO(
                    r.get(employee.department),
                    r.get(employee.subDepartment),
                    r.get(employee.salary.min().longValue()),
                    r.get(employee.salary.avg().doubleValue()),
                    r.get(employee.salary.max().longValue()))
            ));

        return salSummaryList;
    }    
}