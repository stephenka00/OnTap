package Entity;

import ENUM.Position;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date birthDate;
    private String address;
    private double basicSalary;
    private double netSalary;
    private double insuranceContribution;

    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
