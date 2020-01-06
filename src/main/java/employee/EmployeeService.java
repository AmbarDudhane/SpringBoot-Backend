package employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<>();
		employeeRepository.findAll().forEach(employees::add);
		return employees;
	}

	public Employee getEmployee(Long id) {
		Optional<Employee> emp = employeeRepository.findById(id);
		return emp.get();
	}
	
	public void addEmployee(Employee e) {
		Long maxKey = employeeRepository.findMaxId();
		System.out.println("Maximum id is = "+ maxKey);
		e.setId(maxKey+1);
		employeeRepository.save(e);
	}
	
	public void updateEmployee(Employee e, int id) {
		employeeRepository.save(e);
	}

	public void deleteEmployee(Long id) {
		// TODO Auto-generated method stub
		employeeRepository.deleteById(id);
	}
}
