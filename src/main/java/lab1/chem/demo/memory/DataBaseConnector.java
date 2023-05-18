package lab1.chem.demo.memory;

//import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import lab1.chem.demo.entities.Solution;

@Repository
public interface DataBaseConnector extends CrudRepository<Solution, Integer> {
	
	//Optional<Solution> findFirstByOrderById();
	//Solution findFirstByOrderById();
	Solution findFirstByOrderByIdDesc();
	
}
