package Controller;

import DTO.EmployeeDTO;
import Entity.Employee;
import Service.EmployeeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;
    // phan trang
    @GetMapping
    public Page<EmployeeDTO> getAll(Pageable pageable) {
        return employeeService.findAll(pageable);
    }
    @PostMapping("/add/{departmentId}")
    public List<Employee> addEmployeesToDepartment(@RequestBody List<Employee> employees,
                                                   @PathVariable Long departmentId) {
        return employeeService.addEmployeesToDepartment(employees, departmentId);
    }
    @PutMapping("/update")
    public Employee updateEmployee(@RequestBody Employee updatedEmployee) {
        return employeeService.updateEmployee(updatedEmployee);
    }
    @DeleteMapping("/delete/{employeeId}")
    public void deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
    }
}
