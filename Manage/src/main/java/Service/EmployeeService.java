package Service;

import DTO.EmployeeDTO;
import ENUM.Position;
import Entity.Department;
import Entity.Employee;
import Entity.FormerEmployee;
import Mapper.EmployeeMapper;
import Repository.DepartmentRepository;
import Repository.EmployeeRepository;
import Repository.FormerEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private FormerEmployeeRepository formerEmployeeRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private DepartmentRepository departmentRepository;

    public Page<EmployeeDTO> findAll(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<EmployeeDTO> employeeDTOs = employees.getContent().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(employeeDTOs, pageable, employees.getTotalElements());
    }

    // ensure that whole activities in progress with only one transaction and
    // solve cases when adding not only 1 employee but multiple employees at once
    @Transactional
    public List<Employee> addEmployeesToDepartment(List<Employee> employees, Long departmentId) {
        // find out by id
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // check and update role to ensure that
        // there is no more than 1 department head
        // and no more than 2 deputy departments in the department.
        long currentManagerCount = department.getEmployees().stream()
                .filter(e -> e.getPosition() == Position.MANAGER)
                .count();

        long currentDeputyManagerCount = department.getEmployees().stream()
                .filter(e -> e.getPosition() == Position.DEPUTY)
                .count();

        for (Employee employee : employees) {
            if (employee.getPosition() == Position.MANAGER) {
                if (currentManagerCount >= 1) {
                    throw new IllegalArgumentException("Department already has a manager.");
                }
                currentManagerCount++;
                department.setManager(employee);
            } else if (employee.getPosition() == Position.DEPUTY) {
                if (currentDeputyManagerCount >= 2) {
                    throw new IllegalArgumentException("Department already has two deputies.");
                }
                currentDeputyManagerCount++;
            }
            // set up department to every single employee
            employee.setDepartment(department);
        }

        // save whole employee into database
        List<Employee> savedEmployees = employeeRepository.saveAll(employees);

        // update amount of employees
        int newEmployeesCount = employees.size();
        department.setNumberOfEmployees(department.getNumberOfEmployees() + newEmployeesCount);
        departmentRepository.save(department);

        return savedEmployees;
    }
    @Transactional
    public Employee updateEmployee(Employee updatedEmployee) {
        // Tìm nhân viên cũ theo ID
        Employee existingEmployee = employeeRepository.findById(updatedEmployee.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Tìm phòng ban của nhân viên
        Department department = existingEmployee.getDepartment();

        if (department != null) {
            // Kiểm tra và cập nhật vai trò nếu thay đổi
            if (updatedEmployee.getPosition() != existingEmployee.getPosition()) {
                if (updatedEmployee.getPosition() == Position.MANAGER) {
                    if (department.getManager() != null && !department.getManager().getId().equals(existingEmployee.getId())) {
                        throw new IllegalArgumentException("Department already has a manager.");
                    }
                    department.setManager(updatedEmployee);
                } else if (updatedEmployee.getPosition() == Position.DEPUTY) {
                    long deputyCount = department.getEmployees().stream()
                            .filter(e -> e.getPosition() == Position.DEPUTY)
                            .count();
                    if (deputyCount >= 2) {
                        throw new IllegalArgumentException("Department already has two deputies.");
                    }
                } else {
                    // Nếu nhân viên hiện tại là trưởng phòng hoặc phó phòng và chuyển thành nhân viên cấp dưới
                    if (existingEmployee.getPosition() == Position.MANAGER) {
                        department.setManager(null);
                    } else if (existingEmployee.getPosition() == Position.DEPUTY) {
                        // Không cần làm gì vì chỉ cần kiểm tra số lượng phó phòng ở trên
                    }
                }
            }

            // Cập nhật thông tin nhân viên
            existingEmployee.setName(updatedEmployee.getName());
            existingEmployee.setBirthDate(updatedEmployee.getBirthDate());
            existingEmployee.setAddress(updatedEmployee.getAddress());
            existingEmployee.setPosition(updatedEmployee.getPosition());
            existingEmployee.setBasicSalary(updatedEmployee.getBasicSalary());
            existingEmployee.setNetSalary(updatedEmployee.getNetSalary());
            existingEmployee.setInsuranceContribution(updatedEmployee.getInsuranceContribution());

            // Lưu nhân viên
            Employee savedEmployee = employeeRepository.save(existingEmployee);

            // Lưu phòng ban nếu có sự thay đổi
            departmentRepository.save(department);

            return savedEmployee;
        }

        return updatedEmployee;
    }
    @Transactional
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Department department = employee.getDepartment();
        if (department != null) {
            department.setNumberOfEmployees(department.getNumberOfEmployees() - 1);
            departmentRepository.save(department);
        }

        FormerEmployee formerEmployee = new FormerEmployee(employee);
        formerEmployeeRepository.save(formerEmployee);

        employeeRepository.delete(employee);
    }
}

