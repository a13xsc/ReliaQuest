package com.example.rqchallenge.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Employee {
    private Long id;
    @JsonAlias("name")
    private String employeeName;
    @JsonAlias("salary")
    private Integer employeeSalary;
    @JsonAlias("age")
    private Integer employeeAge;
    private String profileImage;
}
