package ticketmart.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ticketmart.bean.SeatHold;

public interface TicketRequestDao {
	int numSeatsAvailable(Optional<Integer> venueLevel);
	HashMap <Integer,Integer> numberOfSeatsAvailable(Optional<Integer> venueLevel);
	Map<Integer,Integer> getNumSeatsOnHoldMap();	
	int getNumSeatsOnHold(Optional<Integer> levelId);
	Map<Integer,Integer> getNumSeatsSoldMap();
	int getNumSeatsSold(Optional<Integer> levelId);
	SeatHold reserveSeats(Long requestId, String customerEmail);
	SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel,Optional<Integer> maxLevel, String customerEmail);
}
