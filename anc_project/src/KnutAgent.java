import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import genius.core.bidding.BidDetails;
import genius.core.issue.Issue;
import genius.core.issue.Value;
import genius.core.AgentID;
import genius.core.Bid;
import genius.core.actions.Accept;
import genius.core.actions.Action;
import genius.core.actions.EndNegotiation;
import genius.core.actions.Offer;
import genius.core.parties.AbstractNegotiationParty;
import genius.core.parties.NegotiationInfo;
import genius.core.utility.AbstractUtilitySpace;
import genius.core.BidHistory;
import genius.core.BidIterator;
import genius.core.Domain;


public class KnutAgent extends AbstractNegotiationParty 
{
	private static double MINIMUM_TARGET = 0.8;
	private static double RANDOM_STOP_TIME = 0.97;
	private static double AVERAGE_STOP_TIME = 0.90;
	private static double MAXIMUM_STOP_TIME = 0.45;
	
	private Bid last_bid_received;
	private Bid my_prev_bid;
	private BidHistory bids_received;
	
	

	/**
	 * Initializes a new instance of the agent.
	 */
	@Override
	public void init(NegotiationInfo info) 
	{
		super.init(info);
		//previous_bids  = new ArrayList<Bid>();
		bids_received = new BidHistory();
	}

	
	@Override
	public Action chooseAction(List<Class<? extends Action>> possibleActions) 
	{
		// Check for acceptance if we have received an offer
		if (last_bid_received != null) {
			double time = timeline.getTime();
			
			if (time < MAXIMUM_STOP_TIME) {	//Do not accept any received offers. Always try to maximize utility.
				System.out.println("Maximum Utility");
				Bid bid = generateMaximumUtilityBid();
				my_prev_bid = bid;
				if (getUtility(last_bid_received) > getUtility(my_prev_bid)) {
					return new Accept(getPartyId(), last_bid_received);
				}
				return new Offer(getPartyId(), bid);
			}
			
			else if (time < AVERAGE_STOP_TIME) { //Do not accept any received offers. Respond with average tit for tat.
				System.out.println("Average Utility");
				Bid bid = generateAveragedTitForTatBid();
				my_prev_bid = bid;
				if (getUtility(last_bid_received) > getUtility(my_prev_bid)) {
					return new Accept(getPartyId(), last_bid_received);
				}
				return new Offer(getPartyId(), bid);
			}			
			
			else if (time < RANDOM_STOP_TIME){
				System.out.println("Random above target");
				Bid bid = generateRandomBidAboveTarget();
				my_prev_bid = bid;
				if (getUtility(last_bid_received) > getUtility(my_prev_bid)) {
					return new Accept(getPartyId(), last_bid_received);
				}	
				else {return new Offer(getPartyId(), bid);
				}
			}
			else {
				System.out.println("Accept");
				if (getUtility(last_bid_received) > MINIMUM_TARGET) {
					return new Accept(getPartyId(), last_bid_received);
				}
				Bid bid = generateRandomBidAboveTarget();
				my_prev_bid = bid;
				return new Offer(getPartyId(), bid);
			}
		}	
		// Otherwise, send out a maximum utility bid 
		return new Offer(getPartyId(), generateMaximumUtilityBid());
	}

	private Bid generateRandomBidAboveTarget() 
	{
		Bid bid;
		double util;
		int i = 0;
		// try 100 times to find a bid under the target utility
		do 
		{
			bid = generateRandomBid();
			util = utilitySpace.getUtility(bid);
		} 
		while (util < MINIMUM_TARGET && i++ < 100);		
		return bid;
	}
	
	private Bid generateAveragedTitForTatBid() 
	{
		Bid bid;
		double time_end = timeline.getTime();
		
		/*If we are early in the negotiation I expect to see smaller changes, and thus uses a larger window
		 * If we are closer to the end of the negotiation I expect greater changes, and thus uses a smaller window*/
		
		double time_start = time_end - 0.15;
		if (AVERAGE_STOP_TIME - time_end < 0.05) {
			time_start = time_end - 0.075;
		}
				
		
		BidHistory bids_received_within_time = new BidHistory();
		bids_received_within_time = bids_received.filterBetweenTime(time_start, time_end).sortToTime();
						
		Bid first_bid = bids_received_within_time.getFirstBidDetails().getBid();
		Bid last_bid = bids_received_within_time.getLastBidDetails().getBid();	
		
		double first_bid_util = getUtility(first_bid); 
		double last_bid_util = getUtility(last_bid);		
		
		double max_utility = getUtility(generateMaximumUtilityBid());
		double min_utility = getUtility(generateMinimumUtilityBid());
		
		double avg_utility_change = (last_bid_util - first_bid_util)/(bids_received_within_time.getHistory().size()-1);

				
		/*
		 * 
		 * Make sure that there is at least a gap of 0.04 between the lower and upper bound
		 * 
		 */
		
		
		/*Not lower than minimum*/		
		double utility_start = Math.max(getUtility(my_prev_bid)-(avg_utility_change*1.05), min_utility);
		
		/*Not higher than maximum*/
		double utility_end = Math.min(getUtility(my_prev_bid)-avg_utility_change*0.95, max_utility);
		
		double gap = utility_end - utility_start;
		
		if (gap <= 0.04) {
			utility_start = utility_start + gap - 0.04;
		}
		
		bid = getBidsOfUtility(utility_start, utility_end, first_bid.getDomain());
		
		return bid;
	}
	
	private Bid getBidsOfUtility(double lowerBound, double upperBound, Domain domain) {
		// In big domains, we need to find only this amount of bids around the
		// target. Should be at least 2.
		
		BidHistory possibleBidHistory = new BidHistory();
		BidIterator myBidIterator = new BidIterator(domain);
		
		while (myBidIterator.hasNext()) {
			Bid b = myBidIterator.next();
			try {
				double util = utilitySpace.getUtility(b);
				if (util >= lowerBound && util <= upperBound) {
					possibleBidHistory.add(new BidDetails(b,util));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Bid bid = possibleBidHistory.getBestBidDetails().getBid();
		
		return bid;
	}
	
	private Bid generateMaximumUtilityBid() 
	{
		Bid bid = null;
		try {
		    bid = this.utilitySpace.getMaxUtilityBid();
		} catch (Exception e) {             
		    e.printStackTrace();            
		}		
		return bid;
	}
	
	private Bid generateMinimumUtilityBid() 
	{
		Bid bid = null;
		try {
		    bid = this.utilitySpace.getMinUtilityBid();
		} catch (Exception e) {             
		    e.printStackTrace();            
		}		
		return bid;
	}
	

	/**
	 * Remembers the offers received by the opponent.
	 */
	@Override
	public void receiveMessage(AgentID sender, Action action) 
	{
		if (action instanceof Offer) 
		{
			last_bid_received = ((Offer) action).getBid();
			BidDetails bid_details = new BidDetails(last_bid_received, utilitySpace.getUtility(last_bid_received), timeline.getTime());
			bids_received.add(bid_details);
		}
	}
	
	
	@Override
	public String getDescription() 
	{
		return "Places random bids >= " + MINIMUM_TARGET;
	}

	/**
	 * This stub can be expanded to deal with preference uncertainty in a more sophisticated way than the default behavior.
	 */
	@Override
	public AbstractUtilitySpace estimateUtilitySpace() 
	{
		return super.estimateUtilitySpace();
	}

}
