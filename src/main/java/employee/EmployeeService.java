package employee;

import java.io.IOException;
import java.util.ArrayList;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

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
		try {
			sendEmail(e.getEmail(), "updated", id);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void deleteEmployee(Long id) {
		// TODO Auto-generated method stub
		String email = employeeRepository.findById(id).get().getEmail();
		employeeRepository.deleteById(id);
		try {
			sendEmail(email, "deleted", id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendEmail(String email, String operation, Long id) throws IOException {
//		System.out.println("In sendMail");
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(email);
//
//        msg.setSubject("Spring Boot-Backend App Operation Status");
//        msg.setText("Employee data of id "+id+" is "+operation+" successfully. \n\n From,\nSpring-Boot-App");
//
//        javaMailSender.send(msg);
		String api_key = "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"; 
		Email from = new Email("spring-notify@gmail.com");
	    String subject = "Spring boot notification";
	    Email to = new Email(email);
	    Content content = new Content("text/plain", "Employee data of id "+id+" is "+operation+" successfully. \n\n From,\nSpring-Boot-App");
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid(System.getenv(api_key));
	    Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
//	      System.out.println(response.getStatusCode());
//	      System.out.println(response.getBody());
//	      System.out.println(response.getHeaders());
	    } catch (IOException ex) {
	      throw ex;
	    }
    }
}
