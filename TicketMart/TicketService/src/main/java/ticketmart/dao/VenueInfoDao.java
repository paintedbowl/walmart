package ticketmart.dao;

import java.util.HashMap;

public interface VenueInfoDao {	
	/**
	* The number of seats in the venue that are neither held nor reserved
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the  number of tickets available on all the level
	*/
	int getNumSeats();
	
	/**
	* The number of seats in the requested level that are neither held nor reserved
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the HashMap of number of tickets available on the provided level
	*/
	HashMap <Integer,Integer> getNumSeatsMap();
	
	/**
	* The number of seats in the requested level that are neither held nor reserved
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the number of tickets available on the provided level
	*/
	int getNumSeats(int levelId);	
}
