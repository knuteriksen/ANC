import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import genius.core.Bid;
import genius.core.Domain;
import genius.core.boaframework.BOAparameter;
import genius.core.boaframework.NegotiationSession;
import genius.core.boaframework.OpponentModel;
import genius.core.issue.Issue;
import genius.core.issue.IssueDiscrete;
import genius.core.issue.Value;
import genius.core.utility.AdditiveUtilitySpace;
import negotiator.boaframework.opponentmodel.tools.UtilitySpaceAdapter;

public class KnutModel extends OpponentModel {
	
	/*
	 * Define aquí los atributos de la clase y el objeto que necesites
	 */
	private Bid opponent_last_offer_received;
	private int opponent_count_offers_received;
	
	private double delta;
	
	/*<Issue ID, Estimated weight*/
	private Map<Integer, Double> estimated_issue_weights;
	
	/*<Issue ID, <Value, Number of times value has appeared>*/
	private Map<Integer, Map<Value, Integer>> count_values_of_issues;
	

	@Override
	public String getName() {
		return "Knut Model";
	}
	
	@Override
	public void init(NegotiationSession negotiationSession, Map<String, Double> parameters) {
		super.init(negotiationSession, parameters);
		
		/*En esta parte del código debes poner cualquier instrucción que necesites
		 * para la inicialización del modelo de aprendizaje
		 */
		opponent_last_offer_received = null;
		opponent_count_offers_received = 0;
		delta = 0.15;
		estimated_issue_weights = new HashMap<Integer, Double>();
		count_values_of_issues = new HashMap<Integer, Map<Value,Integer>>();
		
		//Get list of all issues in negotiation
		List<Issue> issue_lst = negotiationSession.getIssues();
		
		//Iterate through issues
		for (Issue temp : issue_lst) {
			//Initialize estimated weight for for issue
			estimated_issue_weights.put(temp.getNumber(), 0.0);
			// Initialize counter for values of issues
			HashMap<Value, Integer> value_map = new HashMap<Value, Integer>();
			count_values_of_issues.put(temp.getNumber(), value_map);
		}
		
	}
	
	@Override
	protected void updateModel(Bid bid, double time) {
		 /*
		  * En esta parte del código debes poner la lógica a realizar por el modelo
		  * cuando una oferta del oponente es recibida
		  */
		
		/*
		 * Only update weights when a an issue changes value from the previous bid
		 */

		
		// Increment the total number of offers received
		opponent_count_offers_received++;
		
		// The issues and its value from the new bid received
		HashMap<Integer, Value> new_values = bid.getValues();

		// If the offer received is the first offer
		if (opponent_count_offers_received < 2) {
			
			//Update last offer received
			opponent_last_offer_received = bid;
			
			// Initialize the count_values_of_issues map
			for (HashMap.Entry<Integer, Value> entry : new_values.entrySet()) {
				//Create map with <Value, 1>
				HashMap<Value, Integer> value_map = new HashMap<Value, Integer>();
				value_map.put(entry.getValue(), 1);
				//Add issue to the map
				count_values_of_issues.put(entry.getKey(), value_map);
			}
			
			return;
		}
		
		// The issues and its value from the last offer received
		HashMap<Integer, Value> old_values = opponent_last_offer_received.getValues();
		
		// Iterate through new values
		for (HashMap.Entry<Integer, Value> entry : new_values.entrySet()) {
			
			// Check if values of issue exist
		    if (count_values_of_issues.get(entry.getKey()).containsKey(entry.getValue())) {
		  
		    	// Update counter
		    	count_values_of_issues.get(entry.getKey()).put(entry.getValue(), count_values_of_issues.get(entry.getKey()).get(entry.getValue())+1);
		    			    	
		    	// Check if value has not changed from last bid 
		    	if(old_values.get(entry.getKey()) == entry.getValue()) {
		   
		    		//Update estimated weight for issue
		    		estimated_issue_weights.put(entry.getKey(), estimated_issue_weights.get(entry.getKey())+delta);
		    	}
		    }
		    
		    // If value does not exist, then it must be new, and the weight should not be updated
		    else {
		    	//Create map with <Value, 1>
				HashMap<Value, Integer> value_map = new HashMap<Value, Integer>();
				value_map.put(entry.getValue(), 1);
				//Add map to issue
				count_values_of_issues.put(entry.getKey(), value_map);
		    }
		}
		
	}
	
	@Override
	public double getBidEvaluation(Bid bid) {
		/*
		 * En esta parte del modelo debes poner la lógica empleada para que, dada una oferta,
		 * se utilice el modelo de las preferencias del oponente para determinar su utilidad estimada
		 */
			
		//Estimated utility
		double	estimated_utility = 0.0;
		
		if (opponent_count_offers_received < 1) {
			return estimated_utility;
		}
		
		//Obtain the total sum of the estimated weights
		double estimated_weights_sum = 0.0;
		
		for (Map.Entry<Integer, Double> entry : estimated_issue_weights.entrySet()) {
			estimated_weights_sum += entry.getValue();
		}
		
		// The issues and its value from the new bid received
		HashMap<Integer, Value> new_values = bid.getValues();
		
		//Iterate over issues
		for (Map.Entry<Integer, Value> entry : new_values.entrySet()) {
			//Estimate V_i for this issue
			double estimated_issue_value = (double) (count_values_of_issues.get(entry.getKey()).get(entry.getValue())/opponent_count_offers_received);
			double normalized_issue_weight = estimated_issue_weights.get(entry.getKey())/estimated_weights_sum;
			
			estimated_utility += normalized_issue_weight*estimated_issue_value;
		}
		
		return estimated_utility;
	}
	
	@Override
	public AdditiveUtilitySpace getOpponentUtilitySpace() {
		AdditiveUtilitySpace utilitySpace = new UtilitySpaceAdapter(this, this.negotiationSession.getDomain());
		return utilitySpace;
	}
	
	@Override
	public Set<BOAparameter> getParameterSpec(){
		Set<BOAparameter> set = new HashSet<BOAparameter>();
		/* Aquí describe los parámetros que necesita el algoritmo de aprendizaje. Ejemplos:
			set.add(new BOAparameter("n", 20.0, "The number of own best offers to be used for genetic operations"));
			set.add(new BOAparameter("n_opponent", 20.0, "The number of opponent's best offers to be used for genetic operations"));
		*/
		return set;
	}

}