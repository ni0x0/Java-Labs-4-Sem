package lab1.chem.demo.service;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
//import java.util.Optional;
//import java.util.stream.StreamSupport;

//import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lab1.chem.demo.entities.Solution;
import lab1.chem.demo.memory.DataBaseConnector;

@Service
public class DBConService {
	@Autowired
	private DataBaseConnector dBConnector;

	public Solution saveDBData(Solution sol) {
		return dBConnector.save(sol);
	}
	
	public Solution getLastSol() {
		return dBConnector.findFirstByOrderByIdDesc();
	}
	
	public Solution getSolById(Integer sId) {
		//Solution respSol = new Solution();
		//Iterable<Integer> iterVal = Arrays.asList(sId);
		//Iterable<Solution> iterSols = dBConnector.findAllById(iterVal);
		//long count = StreamSupport.stream(iterSols.spliterator(), false).estimateSize();
		
		if (dBConnector.existsById(sId) == true)
			return dBConnector.findById(sId).get();
		else 
			return new Solution();
		
		//Optional<Solution> respS = dBConnector.findById(sId);
		//if (respS == null) 
		//	return new Solution();
		//else 
		//	return respS.get();
	}
	
	public Integer getNewSolId() {
		Integer sId;
		if (dBConnector.count() == 0) 
			sId = 1;
		else 
			sId = dBConnector.findFirstByOrderByIdDesc().getId() + 1;
		
		return sId;
	}
	
    public List<Solution> getDBData() {
        Iterable<Solution> solutionsIterable = dBConnector.findAll();
        List<Solution> solutionsList = new ArrayList<>();
        solutionsIterable.forEach(solutionsList::add);
        return solutionsList;
    }
    
}
