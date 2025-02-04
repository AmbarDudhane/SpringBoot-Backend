package employee;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService empser;
	
//	@CrossOrigin(origins = "http://localhost:4200")
	@CrossOrigin(origins = "https://spingboot-backend.herokuapp.com")
	@RequestMapping("/employees")
	public List<Employee> getEmployees(){
		System.out.println("In controller");
		return empser.getAllEmployees();
	}
	

	@RequestMapping("/employees/{id}")
	public Employee getEmployee(@PathVariable Long id) {
		return empser.getEmployee(id);
	}

//	@CrossOrigin(origins = "http://localhost:4200")
	@CrossOrigin(origins = "https://spingboot-backend.herokuapp.com")
	@RequestMapping(method = RequestMethod.POST, value="/employees")
	public void addEmployee(@RequestBody Employee emp) {
		System.out.println(emp.toString());
		empser.addEmployee(emp);
	}
	
//	@CrossOrigin(origins = "http://localhost:4200")
	@CrossOrigin(origins = "https://spingboot-backend.herokuapp.com")
	@RequestMapping(method = RequestMethod.PUT, value="/employees/{id}")
	public void updateEmployee(@RequestBody Employee emp, @PathVariable Long id) {
		System.out.println("In updateEmployee "+emp.toString());
		empser.updateEmployee(emp, id);
	}
	
	@CrossOrigin(origins = "https://spingboot-backend.herokuapp.com")
	@RequestMapping(method = RequestMethod.DELETE, value="/employees/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		empser.deleteEmployee(id);
	}
}
