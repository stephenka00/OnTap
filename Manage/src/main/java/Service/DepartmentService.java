package Service;
import Entity.Department;
import Repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private DepartmentRepository departmentRepository;

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }
    public void deleteById(Long id){
        departmentRepository.deleteById(id);
    }
    public void delete(Department dp) {
        departmentRepository.delete(dp);
    }

}
