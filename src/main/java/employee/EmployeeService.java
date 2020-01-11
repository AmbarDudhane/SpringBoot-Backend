package employee;

import java.io.IOException;
import java.util.ArrayList;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

	private static final String YOUR_DOMAIN_NAME = "sandbox0dcfde19948f488fab568d07693032ea.mailgun.org";
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

		try {
			JsonNode jsonnode = sendSimpleMessage(email, operation,id);
			System.out.println("JSON NODE="+jsonnode.toString());
		}catch(UnirestException e) {
			e.printStackTrace();
		}
		
		//String api_key = ""; 
//		Email from = new Email("asdpand@gmail.com");
//	    String subject = "Spring boot notification";
//	    Email to = new Email(email);
//	    Content content = new Content("text/plain", "Employee data of id "+id+" is "+operation+" successfully. \n\n From,\nSpring-Boot-App");
//	    Mail mail = new Mail(from, subject, to, content);
//
//	    SendGrid sg = new SendGrid(System.getenv("API_KEY"));
//	    Request request = new Request();
//	    try {
//	      request.setMethod(Method.POST);
//	      request.setEndpoint("mail/send");
//	      request.setBody(mail.build());
//	      Response response = sg.api(request);
//
//	    } catch (IOException ex) {
//	      throw ex;
//	    }
    }
	
	public static JsonNode sendSimpleMessage(String email, String operation, Long id) throws UnirestException {

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
            .basicAuth("api", System.getenv("MAILGUN_API_KEY"))
            .field("from", "Excited User <asdpand@gmail.COM>")
            .field("to", email)
            .field("subject", "Spring boot notification")
            .field("text", "Employee data of id "+id+" is "+operation+" successfully. \n\n From,\nSpring-Boot-App")
            .asJson();

        return request.getBody();
    }
}
