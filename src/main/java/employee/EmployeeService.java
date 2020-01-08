package employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
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
	
	public void updateEmployee(Employee e, Long id) {
		employeeRepository.save(e);
		sendEmail(e.getEmail(), "updated", id);
	}

	public void deleteEmployee(Long id) {
		// TODO Auto-generated method stub
		String email = employeeRepository.findById(id).get().getEmail();
		employeeRepository.deleteById(id);
		sendEmail(email, "deleted", id);
	}
	
	public void sendEmail(String email, String operation, Long id) {
		System.out.println("In sendMail");
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Spring Boot-Backend App Operation Status");
        msg.setText("Employee data of id "+id+" is "+operation+" successfully. \n\n From,\nSpring-Boot-App");

        javaMailSender.send(msg);

    }
}
