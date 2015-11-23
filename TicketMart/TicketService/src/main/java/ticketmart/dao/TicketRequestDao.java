package ticketmart.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ticketmart.bean.SeatHold;

public interface TicketRequestDao {
	/**
	* The number of seats in the requested level that are neither held nor reserved
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the number of tickets available on the provided level
	*/
	int numSeatsAvailable(Optional<Integer> venueLevel);
	
	/**
	* The number of seats in the requested level that are neither held nor reserved
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the HashMap of number of tickets available on the provided level
	*/
	HashMap <Integer,Integer> numberOfSeatsAvailable(Optional<Integer> venueLevel);
	
	/**
	* The number of seats in the venue that are neither held nor reserved
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the HashMap of number of tickets available on the venue
	*/
	Map<Integer,Integer> getNumSeatsOnHoldMap();	
	
	/**
	* The number of seats in the venue that are held 
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the number of tickets available on the venue
	*/
	int getNumSeatsOnHold(Optional<Integer> levelId);
	
	/**
	* The number of seats in the venue that are sold 
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the number of tickets available on the venue
	*/
	Map<Integer,Integer> getNumSeatsSoldMap();
	
	/**
	* The number of seats in the venue that are sold 
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the HashMap of number of tickets available on the venue
	*/
	int getNumSeatsSold(Optional<Integer> levelId);
	
	/**
	* Commit seats held for a specific customer
	* @param seatHoldId the seat hold identifier
	* @param customerEmail the email address of the customer to which the seat hold is assigned
	* @return a reservation confirmation code 
	*/ 
	SeatHold reserveSeats(Long requestId, String customerEmail);
	
	/**
	* Find and hold the best available seats for a customer
	* @param numSeats the number of seats to find and hold
	* * @param minLevel the minimum venue level 
	* @param maxLevel the maximum venue level 
	* @param customerEmail unique identifier for the customer
	* @return a SeatHold object identifying the specific seats and related information 
	*/
	SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel,Optional<Integer> maxLevel, String customerEmail);
}
