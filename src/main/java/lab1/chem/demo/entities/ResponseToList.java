package lab1.chem.demo.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ResponseToList {

	private String responseMsg; 
	
	private List<SolDatas> wrongData = new ArrayList<SolDatas>();
	private List<Solution> rightData = new ArrayList<Solution>();
	
	private HttpStatus status;
	
	private Solution mostConcentratedSolut;
	private Solution leastConcentratedSolut;
	private double mediumConcentration;
	
	
	public void setMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}		
	public String getMsg(){
		return this.responseMsg;
	}
	
	public void addWrongD(SolDatas wrD) {
		wrongData.add(wrD);
	}
	public void setWrongD(List<SolDatas> wrDs) {
		wrongData = wrDs;
	}
	public List<SolDatas> getWrongDatas(){
		return wrongData;
	}
	
	public void addRightD(Solution rD) {
		rightData.add(rD);
	}		
	public List<Solution> getRightDatas(){
		return rightData;
	}
	
	
	public void setStatus(HttpStatus stat) {
		status = stat;
	}		
	public HttpStatus getStatus(){
		return status;
	}
	
	
	public void setMostConcentratedSolut(Solution solut) {
		this.mostConcentratedSolut = solut;
	}		
	public Solution getMostConcentratedSolut(){
		return this.mostConcentratedSolut;
	}
	public void setLeastConcentratedSolut(Solution solut) {
		this.leastConcentratedSolut = solut;
	}		
	public Solution getLeastConcentratedSolut(){
		return this.leastConcentratedSolut;
	}
	public void setMediumConcentration(double medConcentration) {
		this.mediumConcentration = medConcentration;
	}		
	public double getMediumConcentration(){
		return this.mediumConcentration;
	}
	
	
	/*
	@Override
	public boolean equals(Object obj) {
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    ResponseToList other = (ResponseToList) obj;
	    return 
	    		this.responseMsg == other.responseMsg &&
	    		this.mostConcentratedSolut == other.mostConcentratedSolut &&
	    		this.leastConcentratedSolut == other.leastConcentratedSolut &&
	    		this.rightData.equals(other.rightData) &&
	    		this.wrongData.equals(other.wrongData) &&
	            this.status == other.status &&
	            this.mediumConcentration == other.mediumConcentration;
	}
	*/
	
}




























//private List<String> errorMessages = new ArrayList<String>();
/*
public void addErrorMessage(String errorMess) {
	errorMessages.add(errorMess);
}		
public List<String> getErrorMessages(){
	return errorMessages;
}
*/