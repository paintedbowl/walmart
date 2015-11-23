package ticketmart.dao.impl;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ticketmart.bean.TicketInfo;
import ticketmart.dao.VenueInfoDao;

public class VenueInfoDaoImpl implements VenueInfoDao{
	public final static String VENUE_NAME = "Bootleg Theater";
	
	/**
	* The number of seats in the venue that are neither held nor reserved
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the  number of tickets available on all the level
	*/
	@Override
	public int getNumSeats() {
		int numOfSeatsAvailable = 0;
		for (Long levelId : initMap.keySet()) {
			numOfSeatsAvailable = numOfSeatsAvailable+initMap.get(levelId).getNumOfSeats();
		}
		return numOfSeatsAvailable;
	}
	
	/**
	* The number of seats in the requested level that are neither held nor reserved
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the HashMap of number of tickets available on the provided level
	*/
	public HashMap <Integer,Integer> getNumSeatsMap() {
		HashMap <Integer,Integer> numOfSeatsAvailableMap = new HashMap <Integer,Integer>();
		int numOfSeatsAvailable = 0;
		for (Long levelId : initMap.keySet()) {
			numOfSeatsAvailable=0;
			numOfSeatsAvailable = numOfSeatsAvailable+initMap.get(levelId).getNumOfSeats();
			numOfSeatsAvailableMap.put(Integer.valueOf(levelId.intValue()), numOfSeatsAvailable);
		}
		return numOfSeatsAvailableMap;
	}	
	
	/**
	* The number of seats in the requested level that are neither held nor reserved
	* @param venueLevel a numeric venue level identifier to limit the search
	* * @return the number of tickets available on the provided level
	*/
	@Override
	public int getNumSeats(int venueLevel) {
		TicketInfo tktInfo = initMap.get(Integer.valueOf(venueLevel).longValue());		
		return (tktInfo != null)?tktInfo.getNumOfSeats():-1;
	}
	
	private static final Map<Long,TicketInfo> initMap  ;
	static {
		initMap = new HashMap<Long, TicketInfo>() ;
		try{
			String currentDirectory = System.getProperty("user.dir");
			//System.out.println("Current working directory : "+currentDirectory);
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(currentDirectory+"/venue_details.json"));		
			JSONObject jsonObject = (JSONObject) obj;				
			JSONArray jsonArray = (JSONArray) jsonObject.get(VENUE_NAME);			
			
			for (int i = 0, size = jsonArray.size(); i < size; i++){
				JSONObject objectInArray = (JSONObject)jsonArray.get(i);
				TicketInfo tkt = new TicketInfo(
				  Long.valueOf((Long)objectInArray.get("LevelId")).intValue(),
				  (String)objectInArray.get("LevelIdName"),
				  Double.valueOf((String)objectInArray.get("Price")).doubleValue(),
				  Long.valueOf((Long)objectInArray.get("Rows")).intValue(),
				  Long.valueOf((Long)objectInArray.get("NumOfSeats")).intValue());
				initMap.put((Long)objectInArray.get("LevelId"), tkt) ;
		    }
		}catch(Exception exp){
			System.out.println("Error while reading the venue Info. Please make sure that the venue_details.json exists.");
		}
	}
}
