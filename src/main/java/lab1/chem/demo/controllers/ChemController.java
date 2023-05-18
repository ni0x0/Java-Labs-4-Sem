package lab1.chem.demo.controllers;

import java.util.Comparator;
import java.util.List;
//import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
import lab1.chem.demo.entities.AsyncAnswer;
import lab1.chem.demo.entities.ResponseToList;
import lab1.chem.demo.entities.SolDatas;
import lab1.chem.demo.entities.Solution;
import lab1.chem.demo.entities.ValidationChemeError;
//import lab1.chem.demo.memory.DataBaseConnector;
//import lab1.chem.demo.memory.DataBaseConnector;
import lab1.chem.demo.memory.MemCacheStore;
import lab1.chem.demo.service.AsyncService;
import lab1.chem.demo.service.CallCounter;
import lab1.chem.demo.service.ChemCalculate;
import lab1.chem.demo.service.DBConService;
import lab1.chem.demo.validators.ChemCheckValidator;


@RestController
@RequestMapping("/vv1")
public class ChemController {
	private static final Logger logger = LoggerFactory.getLogger(ChemController.class);
	
	private ChemCalculate chemCalcService;
	private ChemCheckValidator chemValidator;
	
	private MemCacheStore cacheStore;
	private int solId = 0;
	
	private CallCounter callCounterService;
	
	//private DataBaseConnector DBConnector;
	private DBConService dBConnector;
	private AsyncService asyncService;
	
	//@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	//private Integer s_id;
	
	@Autowired
	public ChemController(final ChemCalculate chemCalcService, ChemCheckValidator chemValidator, MemCacheStore cacheStore, CallCounter callCounterService, DBConService dBConnector, AsyncService asyncService) {
		this.chemCalcService = chemCalcService;
		this.chemValidator = chemValidator;
		this.cacheStore = cacheStore;
		this.callCounterService = callCounterService;
		this.dBConnector = dBConnector;
		this.asyncService = asyncService;
		
		logger.info("Controller Initialization");
	}
	
	@GetMapping("/chemeget")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> getSubstanceMass(@RequestParam int solutionMass, @RequestParam int substancePercent) {
		
		logger.info("Start Request Process");
		solId++;
		
		//callCounterService.callsCountIncrement();
		//callCounterService.syncCallsCountIncrement();
		
		ValidationChemeError response = chemValidator.validateSolutionParametres(solutionMass, substancePercent);
		
		if (response.getErrorMessages().size() != 0) {
			response.setStatusMessage(HttpStatus.BAD_REQUEST.name());
			logger.error("Argument Values Problem");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);	
		}
		try {
			logger.info("Start Calculating Process");
			Solution chemSolut = new Solution();
			chemSolut = chemCalcService.calculateSubstanceMass(solutionMass, substancePercent);
			
			if(chemSolut.getSubstanceMass() == 0 || chemSolut.getSubstanceMass() == solutionMass) {
				response.addErrorMessage("Bad calculations have been occured");
				response.setStatusMessage(HttpStatus.CONFLICT.name());
				logger.error("Bad Value for Substance Mass");
				
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);	
			} 
			else {
				logger.info("Substance Mass is sucsessfully calculated");
				cacheStore.addSol(solId,  chemSolut);

				//dBConnector.save(chemSolut);
				
				dBConnector.saveDBData(chemSolut);
				
				callCounterService.goodCallsCountIncrement();
				callCounterService.goodSyncCallsCountIncrement();
				
				return ResponseEntity.ok(chemSolut);
			}
				
		}
		catch (Exception e) {
			response.addErrorMessage("Internal Server Error Occured");
			response.setStatusMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
			logger.error("Internal Server Error Occured");
			
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);	
		}
		
	}
	
	
	@GetMapping("/chemmemory/store")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> getMemStore() {
		return ResponseEntity.ok(cacheStore);
	}
	
	@GetMapping("/chemmemory/storesize")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> getMemStoreSize() {
		return ResponseEntity.ok(cacheStore.getMemStoreSize());
	}
	
	@GetMapping("/chemmemory/idsolution")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> getMemStoreIdSolut(@RequestParam int id) {
		return ResponseEntity.ok(cacheStore.getSol(id));
	}
	
	@GetMapping("/chemmemory/ids")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> getChemIds() {
		return ResponseEntity.ok(cacheStore.getMemStoreIds());
	}
	
	@GetMapping("/chemmemory/solutions")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> getChemSolutions() {
		return ResponseEntity.ok(cacheStore.getMemStoreSolutions());
	}

	@GetMapping("/callsstat")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> getCallsStatistic() {
		return ResponseEntity.ok(callCounterService);
	}
	@GetMapping("/cleancallsstat")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> cleanCallsStatistic() {
		callCounterService.cleanCounters();
		return ResponseEntity.ok(callCounterService);
	}
	

	@PostMapping("/chemesget")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseToList getSubstanceMasses(@RequestBody List<SolDatas> params){
		ResponseToList response = new ResponseToList();        
		
		List<SolDatas> wrongParams = chemValidator.validateChemList(params);
		response.setWrongD(wrongParams);
		
		wrongParams.forEach(wrongParam -> {
			params.removeIf(data -> data.equals(wrongParam));
		});

		
		solId += wrongParams.size();
		params.forEach(param ->{
			Solution solut = chemCalcService.calculateSubstanceMass(param.getSolMass(), param.getSubPerc());
			response.addRightD(solut);
			solId++;
			cacheStore.addSol(solId,  solut);
			dBConnector.saveDBData(solut);
		});
		
		if (wrongParams.isEmpty() == false) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMsg("Wrong values in " + wrongParams.size() + " arguments");
		}
		else {
			response.setStatus(HttpStatus.OK);
			response.setMsg("No problems with accepted datas");
		}
		
		response.setMostConcentratedSolut(
				response.getRightDatas().stream()
				.max(Comparator.comparingInt(Solution::getSubstancePercent))
				.orElse(null));
		
		response.setLeastConcentratedSolut(
				response.getRightDatas().stream()
				.min(Comparator.comparingInt(Solution::getSubstancePercent))
				.orElse(null));
		
		response.setMediumConcentration(
				response.getRightDatas().stream()
				.mapToInt(Solution::getSubstancePercent)
				.average()
				.orElse(0));
		
		
		return response;
	}
	
	@GetMapping("/getdbdata")
	@ResponseStatus(HttpStatus.OK)
	public List<Solution> getSolDBDatas() {
		return dBConnector.getDBData();
	}
	
	@GetMapping("/chemasync")
	@ResponseStatus(HttpStatus.OK)
	public AsyncAnswer saveSolSync (@RequestParam int solutionMass, @RequestParam int substancePercent) {
		
		AsyncAnswer asAnsw = new AsyncAnswer();
		Integer sId = dBConnector.getNewSolId();
		asAnsw.setAnswer("Check next Id:");
		asAnsw.setIdAnswer(sId);
		
		asyncService.asyncCalculations(solutionMass, substancePercent, sId);
		
		logger.info("Controller Method saveSolSync completed.");
		
		return asAnsw;
	}
	
	@GetMapping("/chemgetiddbdata")
	@ResponseStatus(HttpStatus.OK)
	public Solution getDBDataById (@RequestParam Integer sId) {
		
		Solution respSol = dBConnector.getSolById(sId); // = new Solution();
		
		return respSol;
	}
	

}

