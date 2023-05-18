package lab1.chem.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

//import java.util.HashMap;
//import java.util.Map;

//import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
//import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lab1.chem.demo.entities.AsyncAnswer;
import lab1.chem.demo.entities.ResponseToList;
import lab1.chem.demo.entities.SolDatas;
import lab1.chem.demo.entities.Solution;
import lab1.chem.demo.entities.ValidationChemeError;
import lab1.chem.demo.service.AsyncService;
import lab1.chem.demo.service.CallCounter;
//import lab1.chem.demo.controllers.ChemController;
import lab1.chem.demo.service.ChemCalculate;
import lab1.chem.demo.validators.ChemCheckValidator;
//import lab1.chem.demo.memory.DataBaseConnector;
import lab1.chem.demo.service.DBConService;
import lab1.chem.demo.memory.MemCacheStore;

public class TestChemController {

	private ChemCalculate calcService = mock(ChemCalculate.class);
	private ChemCheckValidator chemValidator = mock(ChemCheckValidator.class);
	private MemCacheStore cacheStore = mock(MemCacheStore.class);
	private CallCounter callCounterService = mock(CallCounter.class);
	private DBConService dBConnector = mock(DBConService.class);
	private AsyncService asyncService = mock(AsyncService.class);
	
	@InjectMocks
	private ChemController chemController = new ChemController(calcService, chemValidator, cacheStore, callCounterService, dBConnector, asyncService);
	
	@Test
	public void goodControllerTest() {
		int solutionMass = 400;
		int substancePercent = 10;
		
		ValidationChemeError checkedResponse = new ValidationChemeError();
		
		Solution chemSolut = new Solution(solutionMass, 40, substancePercent);
		
		when(chemValidator.validateSolutionParametres(solutionMass, substancePercent)).thenReturn(checkedResponse);
		when(calcService.calculateSubstanceMass(solutionMass, substancePercent)).thenReturn(chemSolut);
		
		ResponseEntity<Object> response = chemController.getSubstanceMass(solutionMass, substancePercent);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(chemSolut, response.getBody());
	}
	
	@Test
	public void badParamsControllerTest() {
		int solutionMass = 0;
		int substancePercent = 10;
		
		ValidationChemeError checkedResponse = new ValidationChemeError();
		checkedResponse.addErrorMessage("Too small value for Solution Mass");
		
		when(chemValidator.validateSolutionParametres(solutionMass, substancePercent)).thenReturn(checkedResponse);
		
		ResponseEntity<Object> response = chemController.getSubstanceMass(solutionMass, substancePercent);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void badCalculationControllerTest() {
		int solutionMass = 2;
		int substancePercent = 20;
		
		ValidationChemeError checkedResponse = new ValidationChemeError();
		
		Solution chemSolut = new Solution(solutionMass, 0, substancePercent);
		
		when(chemValidator.validateSolutionParametres(solutionMass, substancePercent)).thenReturn(checkedResponse);
		when(calcService.calculateSubstanceMass(solutionMass, substancePercent)).thenReturn(chemSolut);
		
		ResponseEntity<Object> response = chemController.getSubstanceMass(solutionMass, substancePercent);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}
	
	@Test
	public void badServerControllerTest() {
		int solutionMass = 2;
		int substancePercent = 1;
		
		ValidationChemeError checkedResponse = new ValidationChemeError();
		
		//Solution chemSolut = new Solution(solution_mass, 0, substance_percent);
		
		when(chemValidator.validateSolutionParametres(solutionMass, substancePercent)).thenReturn(checkedResponse);
		when(calcService.calculateSubstanceMass(solutionMass, substancePercent)).thenThrow(new RuntimeException());
		
		ResponseEntity<Object> response = chemController.getSubstanceMass(solutionMass, substancePercent);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
	
	// MemCache
	
	@Test
	public void checkMemCacheStore() {
		cacheStore.addSol(1, new Solution (100, 40, 40));
		ResponseEntity<Object> response = chemController.getMemStore();
		assertEquals(cacheStore, response.getBody());		
	}
	
	@Test
	public void checkMemCacheStoreSize() {	
		when(cacheStore.getMemStoreSize()).thenReturn(0);
		
		ResponseEntity<Object> response = chemController.getMemStoreSize();
		assertEquals(0, response.getBody());
	}
	
	@Test
	public void checkMemCacheStoreIdSolut() {	
		//when(cacheStore.addSol(1, new Solution(100, 60, 60))).thenReturn(null);
		Solution chemSolut = new Solution(100, 60, 60);
		when(cacheStore.getSol(1)).thenReturn(chemSolut);
		
		ResponseEntity<Object> response = chemController.getMemStoreIdSolut(1);
		assertEquals(chemSolut, response.getBody());
	}
	
	@Test
	public void checkMemCacheStoreIds() {	
		when(cacheStore.getMemStoreIds()).thenReturn(null);
		
		ResponseEntity<Object> response = chemController.getChemIds();
		assertNull(response.getBody());
	}
	
	@Test
	public void checkMemCacheSoluts() {	
		when(cacheStore.getMemStoreSolutions()).thenReturn(null);
		
		ResponseEntity<Object> response = chemController.getChemSolutions();
		assertNull(response.getBody());
	}
	
	
	
	// Call Counter
	@Test
	public void checkCallsCounter() {
		ResponseEntity<Object> response = chemController.getCallsStatistic();
		assertEquals(callCounterService, response.getBody());		
	}
	
	@Test
	public void checkCallsCounterCleaner() {
		ResponseEntity<Object> response = chemController.cleanCallsStatistic();
		assertEquals(callCounterService, response.getBody());		
	}
	
	
	
	// With List Parameters
	@Test
	public void goodControllerWListTest() {
		int solMass1 = 400;
		int subPerc1 = 10;
		int solMass2 = 440;
		int subPerc2 = 50;
		
		SolDatas solData1 = new SolDatas();
		SolDatas solData2 = new SolDatas();
		
		solData1.setSolMass(solMass1);
		solData1.setSubPerc(subPerc1);
		solData2.setSolMass(solMass2);
		solData2.setSubPerc(subPerc2);
		
		List<SolDatas> inParams = new ArrayList<SolDatas>();
		inParams.add(solData1);
		inParams.add(solData2);
		
		ResponseToList waitedResponse = new ResponseToList(); 
		
		List<SolDatas> wrongParams = new ArrayList<SolDatas>();
		when(chemValidator.validateChemList(inParams)).thenReturn(wrongParams);
		waitedResponse.setWrongD(wrongParams);

		Solution solut1 = new Solution(400, 40, 10);
		Solution solut2 = new Solution(440, 220, 50);
		when(calcService.calculateSubstanceMass(solData1.getSolMass(), solData1.getSubPerc())).thenReturn(solut1);
		when(calcService.calculateSubstanceMass(solData2.getSolMass(), solData2.getSubPerc())).thenReturn(solut2);
		waitedResponse.addRightD(solut1);
		waitedResponse.addRightD(solut2);
		
		
		waitedResponse.setStatus(HttpStatus.OK);
		waitedResponse.setMsg("No problems with accepted datas");

		waitedResponse.setMostConcentratedSolut(solut2);
		waitedResponse.setLeastConcentratedSolut(solut1);
		waitedResponse.setMediumConcentration(30);
		
		
		ResponseToList response = chemController.getSubstanceMasses(inParams);
		assertEquals(waitedResponse.getMsg(), response.getMsg());
		assertEquals(waitedResponse.getWrongDatas(), response.getWrongDatas());
		assertEquals(waitedResponse.getRightDatas(), response.getRightDatas());
		assertEquals(waitedResponse.getStatus(), response.getStatus());
		assertEquals(waitedResponse.getMostConcentratedSolut(), response.getMostConcentratedSolut());
		assertEquals(waitedResponse.getLeastConcentratedSolut(), response.getLeastConcentratedSolut());
		assertEquals(waitedResponse.getMediumConcentration(), response.getMediumConcentration());
	}
	
	@Test
	public void oneBadControllerWListTest() {
		int solMass1 = 400;
		int subPerc1 = 10;
		int solMass2 = 440;
		int subPerc2 = 150;
		
		SolDatas solData1 = new SolDatas();
		SolDatas solData2 = new SolDatas();
		
		solData1.setSolMass(solMass1);
		solData1.setSubPerc(subPerc1);
		solData2.setSolMass(solMass2);
		solData2.setSubPerc(subPerc2);
		
		List<SolDatas> inParams = new ArrayList<SolDatas>();
		inParams.add(solData1);
		inParams.add(solData2);
		
		ResponseToList waitedResponse = new ResponseToList(); 
		
		List<SolDatas> wrongParams = new ArrayList<SolDatas>();
		wrongParams.add(solData2);
		when(chemValidator.validateChemList(inParams)).thenReturn(wrongParams);
		waitedResponse.setWrongD(wrongParams);

		//inParams.removeIf(data -> data.equals(solData2));

		
		Solution solut1 = new Solution(400, 40, 10);
		when(calcService.calculateSubstanceMass(solData1.getSolMass(), solData1.getSubPerc())).thenReturn(solut1);
		waitedResponse.addRightD(solut1);
		
		
		waitedResponse.setStatus(HttpStatus.BAD_REQUEST);
		waitedResponse.setMsg("Wrong values in " + wrongParams.size() + " arguments");

		waitedResponse.setMostConcentratedSolut(solut1);
		waitedResponse.setLeastConcentratedSolut(solut1);
		waitedResponse.setMediumConcentration(10);
		
		
		ResponseToList response = chemController.getSubstanceMasses(inParams);

		assertEquals(waitedResponse.getMsg(), response.getMsg());
		assertEquals(waitedResponse.getWrongDatas(), response.getWrongDatas());
		assertEquals(waitedResponse.getRightDatas(), response.getRightDatas());
		assertEquals(waitedResponse.getStatus(), response.getStatus());
		assertEquals(waitedResponse.getMostConcentratedSolut(), response.getMostConcentratedSolut());
		assertEquals(waitedResponse.getLeastConcentratedSolut(), response.getLeastConcentratedSolut());
		assertEquals(waitedResponse.getMediumConcentration(), response.getMediumConcentration());
	}
	
	
	
	@Test
	public void checkGetDBSolDatas() {	
		List<Solution> solutionsList = new ArrayList<>();
		solutionsList.add(new Solution(200, 100, 50));
		solutionsList.add(new Solution(300, 10, 30));
		
		when(dBConnector.getDBData()).thenReturn(solutionsList);

		List<Solution> responseSolList = chemController.getSolDBDatas();
		assertEquals(solutionsList, responseSolList);
	}
	
	@Test
	public void asyncCall() {	
		
		Integer sId = 21;
		Solution solut1 = new Solution(400, 40, 10);
		solut1.setId(sId);
		when(dBConnector.getSolById(sId)).thenReturn(solut1);

		Solution resp = chemController.getDBDataById(sId);
		assertEquals(solut1, resp);
	}	
	
	@Test
	public void checkGetDBDataByID() {	
		int solMass = 400;
		int subPerc = 10;
		
		AsyncAnswer asAnsw = new AsyncAnswer();
		Integer sId = 9;
		when(dBConnector.getNewSolId()).thenReturn(sId);
		asAnsw.setAnswer("Check next Id:");
		asAnsw.setIdAnswer(sId);
		
		Mockito.lenient().doNothing().when(asyncService).asyncCalculations(solMass, subPerc, sId);

		AsyncAnswer resp = chemController.saveSolSync(solMass, subPerc);

		assertEquals(asAnsw.getAnswer(), resp.getAnswer());
		assertEquals(asAnsw.getIdAnswer(), resp.getIdAnswer());
	}
	
}






