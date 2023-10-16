package com.example.rqchallenge.employees;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllEmployees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].employee_name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].employee_salary").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].employee_age").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].profile_image").exists());
    }

    @Test
    void getEmployeesByNameSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/search/Tiger")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].employee_name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].employee_salary").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].employee_age").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].profile_image").exists());
      }

    @Test
    void getEmployeeById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_salary").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_age").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.profile_image").exists());
      }

    @Test
    void getHighestSalaryOfEmployees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/highestSalary")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNumber());
      }
}