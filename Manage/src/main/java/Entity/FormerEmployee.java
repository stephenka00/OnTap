package Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.Date;

@Entity
@Getter
@Setter
public class FormerEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date dob;
    private String address;
    public FormerEmployee() {}

    public FormerEmployee(Employee employee) {
        this.name = employee.getName();
        this.dob = employee.getBirthDate();
        this.address = employee.getAddress();

    }
}
