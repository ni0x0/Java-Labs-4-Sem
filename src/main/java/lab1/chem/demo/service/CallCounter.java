package lab1.chem.demo.service;

import org.springframework.stereotype.Service;

@Service
public class CallCounter {
	private Integer syncCallsCount = Integer.valueOf(0);
	private Integer callsCount = Integer.valueOf(0);
	private Integer goodSyncCallsCount = Integer.valueOf(0);
	private Integer goodCallsCount = Integer.valueOf(0);	

	// Incrementors
	public synchronized void syncCallsCountIncrement() {
		syncCallsCount++;
	}
	public void callsCountIncrement() {
		callsCount++;
	}
	public synchronized void goodSyncCallsCountIncrement() {
		goodSyncCallsCount++;
	}
	public void goodCallsCountIncrement() {
		goodCallsCount++;
	}
	
	// Getters
	public Integer getSyncCallsCount() {
		return syncCallsCount;
	}
	public Integer getCallsCount() {
		return callsCount;
	}
	public Integer getGoodSyncCallsCount() {
		return goodSyncCallsCount;
	}
	public Integer getGoodCallsCount() {
		return goodCallsCount;
	}
	
	public void cleanCounters() {
		syncCallsCount = Integer.valueOf(0);
		callsCount = Integer.valueOf(0);
		goodSyncCallsCount = Integer.valueOf(0);
		goodCallsCount = Integer.valueOf(0);		
	}
	
}
