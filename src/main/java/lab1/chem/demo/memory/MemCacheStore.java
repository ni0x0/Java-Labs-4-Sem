package lab1.chem.demo.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import lab1.chem.demo.entities.Solution;

@Component
public class MemCacheStore {
	private Map<Integer, Solution> memStore = new HashMap<Integer, Solution>();
	
	public void addSol(int id, Solution sol) {
		memStore.put(id, sol);
	}
	
	public Solution getSol(int id) {
		return memStore.get(id);
	}
	
	public int getMemStoreSize() {
		return memStore.size();
	}
	
	public Map<Integer, Solution> getMemStore(){
		return memStore;
	}
	
	public Set<Integer> getMemStoreIds() {
		return memStore.keySet();
	}
	
	public Collection<Solution> getMemStoreSolutions(){
		return memStore.values();
	}
	
	

}