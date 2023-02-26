package lab1.chem.demo.entities;

public class Solution {
	private int solutionMass;
	private Integer substanceMass;
	private Integer substancePercent;
	
	// Setters
	public void set_solutionMass(Integer _mass) {
		this.solutionMass = _mass;
	}
	public void set_substanceMass(Integer _mass) {
		this.substanceMass = _mass;
	}
	public void set_substancePercent(Integer _percent) {
		this.substancePercent = _percent;
	}
	
	// Getters
	public Integer get_solutionMass() {
		return this.solutionMass;
	}
	public Integer get_substanceMass() {
		return this.substanceMass;
	}
	public Integer get_substancePercent() {
		return this.substancePercent;
	}
	
	// Tasks Solving Methods
	public Integer count_substanceMass() {
		substanceMass = (solutionMass * substancePercent)/100;
		return this.substanceMass;
	}
	
	
}
