package com.example.rqchallenge.employees;

import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.EmployeeResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class EmployeeController implements IEmployeeController {

    private static final String LOG_PREFIX = "[EmployeeController] ";
    private static final String BASE_URL = "https://dummy.restapiexample.com";
    private final RestTemplate restTemplate;

    public EmployeeController() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = getListWithAllEmployeesFromRestApi();
        List<String> employeeNames =
                employees.stream().map(Employee::getEmployeeName).collect(Collectors.toList());
        infoLog("received employee list: " + employeeNames);
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        List<Employee> allEmployees = getListWithAllEmployeesFromRestApi();
        infoLog(String.format("got %d employees", allEmployees.size()));
        infoLog("filtering employees by name...");
        List<Employee> filteredEmployees =
                allEmployees.stream()
                        .filter(employee -> employee.getEmployeeName().contains(searchString))
                        .collect(Collectors.toList());
        infoLog(
                String.format(
                        "got employees: %s",
                        filteredEmployees.stream()
                                .map(Employee::getEmployeeName)
                                .collect(Collectors.toList())));
        return ResponseEntity.ok(filteredEmployees);
    }

    private List<Employee> getListWithAllEmployeesFromRestApi() {
        String url = BASE_URL + "/api/v1/employees";
        infoLog("getting all employees...");
        ResponseEntity<EmployeeResponse<List<Employee>>> responseEntity =
                restTemplate.exchange(
                        url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        // TODO: revisit in the future in case responseEntity.getBody() can be null
        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        String url = BASE_URL + "/api/v1/employee/" + id;
        infoLog("getting employee by id...");
        ResponseEntity<EmployeeResponse<Employee>> responseEntity =
                restTemplate.exchange(
                        url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        // TODO: revisit in the future in case responseEntity.getBody() can be null
        EmployeeResponse<Employee> employeeResponse =
                Objects.requireNonNull(responseEntity.getBody());
        infoLog("received employee: " + employeeResponse.getData());
        return ResponseEntity.ok(employeeResponse.getData());
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        List<Employee> allEmployees = getListWithAllEmployeesFromRestApi();
        Optional<Integer> maxSalary =
                allEmployees.stream().map(Employee::getEmployeeSalary).max(Integer::compareTo);
        infoLog(String.format("max salary: %d", maxSalary.orElse(null)));
        return ResponseEntity.of(maxSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<Employee> allEmployees = getListWithAllEmployeesFromRestApi();
        infoLog("getting top ten highest earning employees...");
        List<Employee> topTenHighestEarningEmployees =
                allEmployees.stream()
                        .sorted(Comparator.comparing(Employee::getEmployeeSalary).reversed())
                        .limit(10)
                        .collect(Collectors.toList());
        infoLog(String.format("top ten highest earning employees: %s", topTenHighestEarningEmployees));
        return ResponseEntity.ok(topTenHighestEarningEmployees.stream()
                .map(Employee::getEmployeeName).collect(Collectors.toList()));
    }

    // changed return type to match REST response
    // the README is incorrect, the actual response only contains the id of the employee
    @Override
    public ResponseEntity<Long> createEmployee(Map<String, Object> employeeInput) {
        String url = BASE_URL + "/api/v1/create";
        infoLog("creating employee...");
        HttpEntity<String> httpEntity = new HttpEntity<>(employeeInput.toString());
        infoLog(httpEntity.getBody());
        ResponseEntity<EmployeeResponse<Employee>> responseEntity =
                restTemplate.exchange(
                        url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});
        // TODO: revisit in the future in case responseEntity.getBody() can be null
        EmployeeResponse<Employee> employeeResponse =
                Objects.requireNonNull(responseEntity.getBody());
        infoLog("created employee with id " + employeeResponse.getData().getId());
        return ResponseEntity.ok(employeeResponse.getData().getId());
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        String url = BASE_URL + "/api/v1/delete/" + id;
        infoLog("deleting employee by id...");
        ResponseEntity<EmployeeResponse<Long>> responseEntity =
                restTemplate.exchange(
                        url, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {});
        // TODO: revisit in the future in case responseEntity.getBody() can be null
        EmployeeResponse<Long> employeeResponse = Objects.requireNonNull(responseEntity.getBody());
        infoLog("deleted employee " + id);
        return ResponseEntity.ok(employeeResponse.getMessage());
    }

    private void infoLog(String message) {
        log.info(LOG_PREFIX + message);
    }
}
