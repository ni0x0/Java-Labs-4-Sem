package lab1.chem.demo.entities;

public class SolDatas {

	private Integer solMass;
	private Integer subPerc;
	
	public SolDatas () {}
	
	public SolDatas (int solMass, int subPerc) {
		this.solMass = solMass;
		this.subPerc =  subPerc;
	}
	
	public Integer getSolMass() {
		return solMass;
	}
	public Integer getSubPerc() {
		return subPerc;
	}
	
	public void setSolMass(Integer mass) {
		solMass = mass;
	}
	public void setSubPerc(Integer perc) {
		subPerc = perc;
	}
	
}
