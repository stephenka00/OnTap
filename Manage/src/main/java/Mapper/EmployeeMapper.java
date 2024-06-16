package Mapper;

import DTO.EmployeeDTO;
import Entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    //map to DTO
    public EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setBirthDate(employee.getBirthDate());
        dto.setAddress(employee.getAddress());
        dto.setBasicSalary(employee.getBasicSalary());
        dto.setNetSalary(employee.getNetSalary());
        dto.setInsuranceContribution(employee.getInsuranceContribution());
        dto.setPosition(employee.getPosition().name());
        if (employee.getDepartment() != null) {
            dto.setDepartmentName(employee.getDepartment().getName());
            if (employee.getDepartment().getManager() != null) {
                dto.setManagerName(employee.getDepartment().getManager().getName());
            }
        }
        return dto;
    }
}
