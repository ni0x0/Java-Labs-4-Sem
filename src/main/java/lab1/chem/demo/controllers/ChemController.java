package lab1.chem.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lab1.chem.demo.entities.Solution;

@RestController
@RequestMapping("/vv1")
public class ChemController {
	Solution chemSolut = new Solution();
	
	@GetMapping("chemget")
	public Solution get_substanceMass(int solution_mass, int substance_percent) {
		chemSolut.set_solutionMass(solution_mass);
		chemSolut.set_substancePercent(substance_percent);
		
		chemSolut.count_substanceMass();
		
		return chemSolut;
	}
	
	
	@GetMapping("strget")
	public String strget() {
		return "Hiiii suuuu0";
	}
	
}
