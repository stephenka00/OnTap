package Entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int numberOfEmployees;
    private int roomNumber;

    @OneToOne
    private Employee manager;

    @OneToOne
    private Employee deputy;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
