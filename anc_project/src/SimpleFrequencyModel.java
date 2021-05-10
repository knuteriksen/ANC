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

public class SimpleFrequencyModel extends OpponentModel {
	
	/*
	 * Define aquí los atributos de la clase y el objeto que necesites
	 */
	
	

	@Override
	public String getName() {
		return "Simple Frequency Model";
	}
	
	@Override
	public void init(NegotiationSession negotiationSession, Map<String, Double> parameters) {
		super.init(negotiationSession, parameters);
		
		/*En esta parte del código debes poner cualquier instrucción que necesites
		 * para la inicialización del modelo de aprendizaje
		 */
		
		
		
	}
	
	@Override
	protected void updateModel(Bid bid, double time) {
		 /*
		  * En esta parte del código debes poner la lógica a realizar por el modelo
		  * cuando una oferta del oponente es recibida
		  */
		
		
	}
	
	@Override
	public double getBidEvaluation(Bid bid) {
		/*
		 * En esta parte del modelo debes poner la lógica empleada para que, dada una oferta,
		 * se utilice el modelo de las preferencias del oponente para determinar su utilidad estimada
		 */
		return 0;
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