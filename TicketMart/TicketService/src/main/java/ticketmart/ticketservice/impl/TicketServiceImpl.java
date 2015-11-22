package ticketmart.ticketservice.impl;

import java.util.HashMap;
import java.util.Optional;

import ticketmart.bean.SeatHold;
import ticketmart.dao.TicketRequestDao;
import ticketmart.dao.impl.TicketRequestDaoImpl;
import ticketmart.ticketservice.TicketService;

public class TicketServiceImpl implements TicketService{

	public HashMap <Integer,Integer> numberOfSeatsAvailable(Optional<Integer> venueLevel) {
		TicketRequestDao request = new TicketRequestDaoImpl();
		return request.numberOfSeatsAvailable(venueLevel);
	}

	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		TicketRequestDao request = new TicketRequestDaoImpl();
		return request.findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail);
	}

	public SeatHold reserveSeats(Long requestId, String customerEmail) {
		TicketRequestDao request = new TicketRequestDaoImpl();
		return request.reserveSeats(requestId, customerEmail);
	}
	
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		TicketRequestDao request = new TicketRequestDaoImpl();
		return request.numSeatsAvailable(venueLevel);
	}
}
