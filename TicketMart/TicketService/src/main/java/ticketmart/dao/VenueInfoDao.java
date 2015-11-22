package ticketmart.dao;

import java.util.HashMap;

public interface VenueInfoDao {	
	int getNumSeats();	
	HashMap <Integer,Integer> getNumSeatsMap();
	int getNumSeats(int levelId);	
}
