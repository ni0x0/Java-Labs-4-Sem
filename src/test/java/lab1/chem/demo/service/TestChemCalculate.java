package lab1.chem.demo.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import lab1.chem.demo.entities.Solution;
//import lab1.chem.demo.service.ChemCalculate;

public class TestChemCalculate {
	
	private ChemCalculate calcService = new ChemCalculate();
	
	/*
	public void setUp(){
		
	}
	*/
	
	@Test
	public void test_RightCalculation() {
		int solution_mass = 400;
		int substance_percent = 10;
		
		Solution chemSolut = calcService.calculateSubstanceMass(solution_mass, substance_percent);
		assertEquals(40, chemSolut.getSubstanceMass());
	}
	
	@Test
	public void test_NullCalculation() {
		int solution_mass = 2;
		int substance_percent = 30;
		
		Solution chemSolut = calcService.calculateSubstanceMass(solution_mass, substance_percent);
		assertEquals(0, chemSolut.getSubstanceMass());
	}
	
	
}
