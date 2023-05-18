package lab1.chem.demo.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lab1.chem.demo.entities.Solution;
//import lab1.chem.demo.entities.ValidationChemeError;
import lab1.chem.demo.validators.ChemCheckValidator;

//import lab1.chem.demo.entities.Solution;


@Service
public class  AsyncService {
	@Autowired
	private DBConService dBConnector;
	@Autowired
	private ChemCalculate chemCalcService;
	@Autowired
	private ChemCheckValidator chemValidator;
	
	
	
	
	public void asyncCalculations(int solMass, int substPercent, Integer sId) {

		CompletableFuture.runAsync(()->{

			if (chemValidator.validateSolutionParametres(solMass, substPercent).getErrorMessages().size() != 0) {
				return;
			}
			
			Solution chemSolut = new Solution();
			chemSolut = chemCalcService.calculateSubstanceMass(solMass, substPercent);
			chemSolut.setId(sId);
			
			dBConnector.saveDBData(chemSolut);
		});
	
		
	}

}
