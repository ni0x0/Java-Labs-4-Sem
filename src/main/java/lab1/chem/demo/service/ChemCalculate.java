package lab1.chem.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lab1.chem.demo.controllers.ChemController;
import lab1.chem.demo.entities.Solution;

@Service
public class ChemCalculate {
	
	private static final Logger logger = LoggerFactory.getLogger(ChemController.class);
	
	public Solution calculateSubstanceMass (int solution_mass, int substance_percent) {
		
		Solution chemSolut = new Solution();
		
		chemSolut.setSolutionMass(solution_mass);
		chemSolut.setSubstancePercent(substance_percent);
		
		
		chemSolut.setSubstanceMass((solution_mass * substance_percent)/100);
		
		logger.info("Calculating Service made its job");
		return chemSolut;
	}
	
}
