package com.prasanna.cbhapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class SalarySummaryPOJO {

    @JsonProperty("min_salary")
    private long min;

    @JsonProperty("avg_salary")
    private double avg;

    @JsonProperty("max_salary")
    private long max;

    @JsonProperty("department")
    private String department;

    @JsonProperty("subDepartment")
    private String subDepartment;

    public SalarySummaryPOJO() {

    }

    public SalarySummaryPOJO(Long min, Double avg, Long max) {
        this.min = min.longValue();
        this.avg = avg;
        this.max = max.longValue();
    }

    public SalarySummaryPOJO(String department, Long min, Double avg, Long max) {
        this.department = department;
        this.min = min.longValue();
        this.avg = avg;
        this.max = max.longValue();
    }

    public SalarySummaryPOJO(String department, String subDepartment, Long min, Double avg, Long max) {
        this.subDepartment = subDepartment;
        this.department = department;
        this.min = min.longValue();
        this.avg = avg;
        this.max = max.longValue();
    }


    public long getMin() {
        return min;
    }
    public void setMin(long min) {
        this.min = min;
    }
    public double getAvg() {
        return avg;
    }
    public void setAvg(double avg) {
        this.avg = avg;
    }    
    public long getMax() {
        return max;
    }
    public void setMax(long max) {
        this.max = max;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(String subDepartment) {
        this.subDepartment = subDepartment;
    }
}