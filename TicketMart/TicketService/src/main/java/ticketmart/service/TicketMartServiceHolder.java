package ticketmart.service;

import java.util.HashMap;
import java.util.Optional;

import ticketmart.bean.SeatHold;
import ticketmart.ticketservice.TicketService;
import ticketmart.ticketservice.impl.TicketServiceImpl;

public class TicketMartServiceHolder {
	private TicketMartServiceHolder() {
	}

	private static class SingletonHolder { 
		private static TicketMartServiceHolder INSTANCE = new TicketMartServiceHolder();
		private static void setInstance(TicketMartServiceHolder inst) {
			INSTANCE = inst ;
		}
	}
	public static TicketMartServiceHolder getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public static SeatHold reserveSeats(String requestId,String customerEmail){
		TicketService client = new TicketServiceImpl();
		return client.reserveSeats(Long.valueOf(requestId), customerEmail);
	}
	
	public static SeatHold findAndHoldSeats(int numberOfSeats,Optional<Integer> levelIdMin,Optional<Integer> levelIdMax,String customerEmail){
		TicketService client = new TicketServiceImpl();
		return client.findAndHoldSeats(numberOfSeats, levelIdMin, levelIdMax, customerEmail);
	}
	
	public static HashMap <Integer,Integer> numberOfSeatsAvailable(Optional<Integer> levelId){
		TicketService client = new TicketServiceImpl();
		return client.numberOfSeatsAvailable(levelId);
	}
}
