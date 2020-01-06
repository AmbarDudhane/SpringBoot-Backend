package employee;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long>{

	@Query("SELECT max(e.id) from Employee e")
	public Long findMaxId();
}
