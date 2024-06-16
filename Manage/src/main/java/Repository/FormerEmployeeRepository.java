package Repository;

import Entity.FormerEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface FormerEmployeeRepository extends JpaRepository<FormerEmployee, Long> {
    Optional<FormerEmployee> findByNameAndDobAndAddress(String name, Date dob, String address);
}
