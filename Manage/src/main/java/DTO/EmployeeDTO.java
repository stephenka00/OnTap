package DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EmployeeDTO {
    private Long id;
    private String name;
    private Date birthDate;
    private String address;
    private double basicSalary;
    private double netSalary;
    private double insuranceContribution;
    private String position;
    private String departmentName;
    private String managerName;
}
