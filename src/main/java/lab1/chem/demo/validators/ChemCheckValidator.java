package lab1.chem.demo.validators;

import org.springframework.stereotype.Component;

import lab1.chem.demo.entities.SolDatas;
import lab1.chem.demo.entities.ValidationChemeError;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ChemCheckValidator {
	private static final Logger logger = LoggerFactory.getLogger(ChemCheckValidator.class);
	
	public ValidationChemeError validateSolutionParametres(int solutionMass, int substancePercent) {
		ValidationChemeError response = new ValidationChemeError();
		
		if (solutionMass < 2) {
			logger.error("Too small value for Solution Mass");
			response.addErrorMessage("Too small value for Solution Mass");
		}
		if (substancePercent < 1 || substancePercent > 99) {
			logger.error("Substance Percent must has value from 1 to 99");
			response.addErrorMessage("Substance Percent must has value from 1 to 99");
		}
		
		
		return response;
	}
	
	public List<SolDatas> validateChemList(List<SolDatas> inParams){
		List<SolDatas> outParams = new ArrayList<SolDatas>();
		
		inParams.forEach(data -> {
			if (data.getSolMass() < 2 || data.getSubPerc() < 1 || data.getSubPerc() > 99)
			outParams.add(data);
		});
		
		return outParams;
	}

}
