package lab1.chem.demo.entities;

import java.util.List;
import java.util.ArrayList;


public class ValidationChemeError {
	private List<String> errorMessages = new ArrayList<String>();
	private String statusMessage;
	
	public void addErrorMessage(String _errorMess) {
		errorMessages.add(_errorMess);
	}	
	
	public List<String> getErrorMessages(){
		return errorMessages;
	}
	
	public void setStatusMessage(String _status) {
		statusMessage = _status;
	}
	
	public String getStatus() {
		return statusMessage;
	}
	
}
