package lab1.chem.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;

@Entity
@Table(name="solution")
public class Solution {
	private int solutionMass;
	private Integer substanceMass;
	private Integer substancePercent;   
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Solution () {
		this.solutionMass = 0;
		this.substanceMass = 0;
		this.substancePercent = 0;
		this.id = 0;
	}
	
	public Solution (int solMass, int subMass, int subPercent) {
		this.solutionMass = solMass;
		this.substanceMass = subMass;
		this.substancePercent =  subPercent;
		this.id = 0;
	}
	
	// Setters
	public void setSolutionMass(Integer mass) {
		this.solutionMass = mass;
	}
	public void setSubstanceMass(Integer mass) {
		this.substanceMass = mass;
	}
	public void setSubstancePercent(Integer percent) {
		this.substancePercent = percent;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	// Getters
	public Integer getSolutionMass() {
		return this.solutionMass;
	}
	public Integer getSubstanceMass() {
		return this.substanceMass;
	}
	public Integer getSubstancePercent() {
		return this.substancePercent;
	}
	public Integer getId() {
		return this.id;
	}
	
	// Tasks Solving Methods
	public Integer countSubstanceMass() {
		substanceMass = (solutionMass * substancePercent)/100;
		return this.substanceMass;
	}
	
	
}
