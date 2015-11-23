package ticketmart.dao.impl;

import java.io.FileReader;
import java.io.FileWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ticketmart.bean.SeatHold;
import ticketmart.bean.TicketRequestInfo;
import ticketmart.dao.LevelCategory;
import ticketmart.dao.TicketRequestDao;
import ticketmart.dao.VenueInfoDao;

public class TicketRequestDaoImpl implements TicketRequestDao{
	public static Long ALLOWED_HOLD_TIME = Integer.valueOf(60).longValue();
	public static String EVENT_TYPE_HOLD = "H";
	public static String EVENT_TYPE_SOLD = "S";
	
	public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
	    return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	@Override
	public Map<Integer, Integer> getNumSeatsOnHoldMap() {
		Map<Integer,Integer> tktsOnHold = new HashMap<Integer,Integer>();
		int numOfTkts = 0;
		Instant currentTime = Instant.now();

		Set<Long> requestsKeys =  initMap.keySet();
		for(Long requestId : requestsKeys){
			List<TicketRequestInfo> requests = initMap.get(requestId);
			Integer levelId = null;
			for(TicketRequestInfo event :  emptyIfNull(requests)){	
				levelId = event.getLevelId();
				if(event.getCreateTime().plusSeconds(ALLOWED_HOLD_TIME).isBefore(currentTime) &&
						EVENT_TYPE_HOLD.equalsIgnoreCase(event.getEventType())){					
					if(tktsOnHold.containsKey(levelId)){
						numOfTkts = tktsOnHold.get(levelId);					
					}
					numOfTkts=numOfTkts+event.getNumOfSeats();					
				}
				tktsOnHold.put(levelId, Integer.valueOf(numOfTkts));
			}
		}
		return tktsOnHold;
	}

	@Override
	public int getNumSeatsOnHold(Optional<Integer> levelId) {
		int numOfTkts = 0;
		Instant currentTime = Instant.now();

		Set<Long> eventsKeys =  initMap.keySet();
		for(Long key : eventsKeys){
			List<TicketRequestInfo> events = initMap.get(key);	
			for(TicketRequestInfo event :  emptyIfNull(events)){				 
				if(event.getCreateTime().plusSeconds(ALLOWED_HOLD_TIME).isBefore(currentTime) &&
						EVENT_TYPE_HOLD.equalsIgnoreCase(event.getEventType())){	
					if(levelId.isPresent()){
						if(event.getLevelId().equals(levelId.get())){
							numOfTkts=numOfTkts+event.getNumOfSeats();
						}
					}else{
						numOfTkts=numOfTkts+event.getNumOfSeats();
					}
				}
			}
		}
		return numOfTkts;
	}

	@Override
	public Map<Integer, Integer> getNumSeatsSoldMap() {
		Map<Integer,Integer> tktsOnSold = new HashMap<Integer,Integer>();
		int numOfTkts = 0;
		Set<Long> eventsKeys =  initMap.keySet();
		for(Long requestId : eventsKeys){
			List<TicketRequestInfo> events = initMap.get(requestId);	
			Integer levelId = null;
			for(TicketRequestInfo event :  emptyIfNull(events)){
				levelId = event.getLevelId();
				if(EVENT_TYPE_SOLD.equalsIgnoreCase(event.getEventType())){					
					if(tktsOnSold.containsKey(levelId)){
						numOfTkts = tktsOnSold.get(levelId);					
					}
					numOfTkts=numOfTkts+event.getNumOfSeats();
				}
				tktsOnSold.put(levelId, Integer.valueOf(numOfTkts));
			}
		}
		return tktsOnSold;
	}

	@Override
	public int getNumSeatsSold(Optional<Integer> levelId) {
		int numOfTkts = 0;
		Set<Long> eventsKeys =  initMap.keySet();
		for(Long key : eventsKeys){
			List<TicketRequestInfo> events = initMap.get(key);	
			for(TicketRequestInfo event :  emptyIfNull(events)){
				if(EVENT_TYPE_SOLD.equalsIgnoreCase(event.getEventType())){	
					if(levelId.isPresent()){
						if(event.getLevelId().equals(levelId.get())){
							numOfTkts=numOfTkts+event.getNumOfSeats();
						}
					}else{
						numOfTkts=numOfTkts+event.getNumOfSeats();
					}
				}
			}
		}
		return numOfTkts;
	}
	

	
	private static final Map<Long,List<TicketRequestInfo>> initMap  ;
	static {
		initMap = new HashMap<Long, List<TicketRequestInfo>>() ;
		try{
			JSONArray jsonArray = getCurrentEvents();		
			Long levelId;
			Long requestId;
			List<TicketRequestInfo> events = null;
			for (int i = 0, size = jsonArray.size(); i < size; i++){
				JSONObject objectInArray = (JSONObject)jsonArray.get(i);
				levelId = (Long)objectInArray.get("LevelId");
				requestId = (Long)objectInArray.get("requestId");
				TicketRequestInfo event = new TicketRequestInfo(
						levelId.intValue(),
						(String)objectInArray.get("type"),
						Long.valueOf((Long)objectInArray.get("NumOfSeats")).intValue(),
						Instant.parse((String)objectInArray.get("time")));
				if(initMap.containsKey(requestId)){
					events = initMap.get(requestId);					
				}else{
					events = new ArrayList<TicketRequestInfo>();
				}
				events.add(event);
				initMap.put(requestId, events) ;
		    }
		}catch(Exception exp){
			System.out.println("Error while reading the events Info. Please make sure that the ticket_requests.json exists.");
			exp.printStackTrace();
		}
	}
	
	private static JSONArray getCurrentEvents() throws Exception{
		String currentDirectory = System.getProperty("user.dir");
		//System.out.println("Current working directory : "+currentDirectory);
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(currentDirectory+"/ticket_requests.json"));		
		JSONObject jsonObject = (JSONObject) obj;				
		return (JSONArray) jsonObject.get(VenueInfoDaoImpl.VENUE_NAME);	
	}
	
	@Override
	public SeatHold reserveSeats(Long requestId, String customerEmail) {
		List<TicketRequestInfo> requests = new ArrayList<TicketRequestInfo>();
		try{
			List<TicketRequestInfo> allRequests = initMap.get(requestId);
			for(TicketRequestInfo request : emptyIfNull(allRequests)){
				if(EVENT_TYPE_HOLD.equalsIgnoreCase(request.getEventType())){
					requests.add(request);
				}
			}			
			persistRequests(requestId,requests,EVENT_TYPE_SOLD);			
		}catch(Exception exp){
			System.out.println("Error Reserving seats for the show."+exp.toString());
			exp.printStackTrace();
		}
		return new SeatHold(requestId,customerEmail,requests,true);
		
	}
	
//	/*
//	 * Since this project is using JSON object to persist the events, the events has to be written to the file system. 
//	 * If it was to use a relational database updating the existing events are much easier.
//	 */
//	private void persistSoldRequests(Long requestId,List<TicketRequestInfo> _events, String requestType) throws Exception{
//		JSONArray jsonArray = getCurrentEvents();		
//		JSONObject eventsJson = new JSONObject();
//		JSONArray eventArray = new JSONArray();
//		// remove the expired requests
//		removeExpiredRequests(jsonArray,eventArray);
//
//		// Add new requests	
//		for(TicketRequestInfo tktInfo : emptyIfNull(_events))	{
//			JSONObject event = new JSONObject(); 
//			event.put("requestId",requestId);
//			event.put("LevelId",tktInfo.getLevelId());
//			event.put("type",requestType);
//			event.put("NumOfSeats",tktInfo.getNumOfSeats());
//			event.put("time",tktInfo.getCreateTime());	
//			eventArray.add(event);
//		}
//		eventsJson.put(VenueInfoDaoImpl.VENUE_NAME, eventArray);	
//		writeToJsonFile(eventsJson);
//	}
	
	/*
	 * Since this project is using JSON object to persist the events, the events has to be written to the file system. 
	 * If it was to use a relational database updating the existing events are much easier.
	 */
	private void persistRequests(Long requestId,List<TicketRequestInfo> _events, String requestType) throws Exception{
		JSONArray jsonArray = getCurrentEvents();		
		JSONObject eventsJson = new JSONObject();
		JSONArray eventArray = new JSONArray();
		// remove the expired requests
		removeExpiredRequests(jsonArray,eventArray,requestId);
		// Add new requests	
		for(TicketRequestInfo tktInfo : emptyIfNull(_events))	{
			JSONObject event = new JSONObject(); 
			event.put("requestId",requestId);
			event.put("LevelId",tktInfo.getLevelId());
			event.put("type",requestType);
			event.put("NumOfSeats",tktInfo.getNumOfSeats());
			event.put("time",tktInfo.getCreateTime().toString());	
			eventArray.add(event);
		}
		eventsJson.put(VenueInfoDaoImpl.VENUE_NAME, eventArray);	
		writeToJsonFile(eventsJson);
	}
	
	private void removeExpiredRequests(JSONArray jsonArray,JSONArray eventArray,Long _requestId){
		for (int i = 0, size = jsonArray.size(); i < size; i++){
			JSONObject objectInArray = (JSONObject)jsonArray.get(i);
			Instant currentTime = Instant.now();
			Instant createTime = Instant.parse((String)objectInArray.get("time"));
			String eventType = (String)objectInArray.get("type");
			Long requestId = (Long)objectInArray.get("requestId");
			if((EVENT_TYPE_HOLD.equalsIgnoreCase(eventType)) && (createTime.plusSeconds(ALLOWED_HOLD_TIME).isBefore(currentTime) && !requestId.equals(_requestId))
				|| !(EVENT_TYPE_HOLD.equalsIgnoreCase(eventType))) {
					JSONObject event = new JSONObject(); 
					event.put("requestId",(Long)objectInArray.get("requestId"));
					event.put("LevelId",(Long)objectInArray.get("LevelId"));
					event.put("type",eventType);
					event.put("NumOfSeats",(Long)objectInArray.get("NumOfSeats"));
					event.put("time",(String)objectInArray.get("time"));	
					eventArray.add(event);
				}
		}
	}
	private void writeToJsonFile(JSONObject eventsJson)throws Exception{
		String currentDirectory = System.getProperty("user.dir");
		//System.out.println("Current working directory : "+currentDirectory);
		try (FileWriter eventFile = new FileWriter(currentDirectory+"/ticket_requests.json")) {
			eventFile.write(eventsJson.toJSONString());
			//System.out.println("Successfully Copied JSON Object to File...");
			eventFile.close();
		}
	}

	@Override
	public SeatHold findAndHoldSeats(int _numSeats, Optional<Integer> _minLevel, Optional<Integer> _maxLevel,
			String customerEmail){
		Integer minLevel = 1;
		Integer maxLevel = 4;
		if(_minLevel.isPresent()) minLevel=_minLevel.get();
		if(_maxLevel.isPresent()) maxLevel=_maxLevel.get();
		int seatsHeld,availableSeats,pendingSeats;
		seatsHeld=availableSeats=0;
		pendingSeats = _numSeats;
		int levelId = 1;
		boolean requestFulfilled = true;
		Long requestId = Instant.now().toEpochMilli();
		List<TicketRequestInfo> requests = new ArrayList<TicketRequestInfo>();
		try{
			for(LevelCategory category : LevelCategory.values()){
				levelId = category.getIntValue();
				availableSeats = numSeatsAvailable(Optional.of(levelId));
				if(pendingSeats > 0 && availableSeats > 0 && levelId >= minLevel && levelId <= maxLevel){
					if(availableSeats >= pendingSeats){					
						seatsHeld=pendingSeats;
						pendingSeats = 0;
					}else{
						seatsHeld=availableSeats;
						pendingSeats = pendingSeats-availableSeats;					
					}
					TicketRequestInfo event = new TicketRequestInfo(Integer.valueOf(levelId),EVENT_TYPE_HOLD,seatsHeld,Instant.now());
					requests.add(event);
				}			
			}
			if(!requests.isEmpty()){
				// update the json file with the request
				persistRequests(requestId,requests,EVENT_TYPE_HOLD );
			}
		}catch(Exception exp){
			System.out.println("Error findAndHoldSeats seats for the show."+exp.toString());
			exp.printStackTrace();
		}
		if(pendingSeats > 0){
			requestFulfilled = false;
		}
		return new SeatHold(requestId, customerEmail, requests,requestFulfilled);
	}
	
	public HashMap <Integer,Integer> numberOfSeatsAvailable(Optional<Integer> venueLevel) {
		HashMap <Integer,Integer> numOfSeatsAvailableMap = new HashMap <Integer,Integer>();
		int numOfSeatsAvailable = 0;
		VenueInfoDao venue = new VenueInfoDaoImpl();
		if(venueLevel.isPresent()){
			numOfSeatsAvailable =  venue.getNumSeats(venueLevel.get().intValue()) -
					(getNumSeatsOnHold(venueLevel) + getNumSeatsSold(venueLevel));
			numOfSeatsAvailableMap.put(venueLevel.get(),Integer.valueOf(numOfSeatsAvailable));
		}else{
			
			HashMap <Integer,Integer> venueSeatsAvailableMap = venue.getNumSeatsMap();
			Set<Integer> venueSeats =  venueSeatsAvailableMap.keySet();
			for(Integer levelId : venueSeats){
				numOfSeatsAvailable =  venue.getNumSeats(levelId)-
					(getNumSeatsOnHold(Optional.of(levelId)) + getNumSeatsSold(Optional.of(levelId)));
				numOfSeatsAvailableMap.put(levelId,Integer.valueOf(numOfSeatsAvailable));
			}
		}
		
		return numOfSeatsAvailableMap;
	}
	
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		int numOfSeatsAvailable = 0;
		VenueInfoDao venue = new VenueInfoDaoImpl();
		if(venueLevel.isPresent()){
			int levelId = venueLevel.get().intValue();
			numOfSeatsAvailable =  venue.getNumSeats(levelId) -
					(getNumSeatsOnHold(venueLevel) + getNumSeatsSold(venueLevel));
		}else{
			numOfSeatsAvailable =  venue.getNumSeats()-
					((getNumSeatsOnHold(Optional.empty()) + getNumSeatsSold(Optional.empty())));
		}
		return numOfSeatsAvailable;
	}
	
	
	public static void main(String[] args) throws Exception {
		TicketRequestDao client = new TicketRequestDaoImpl();
		int results = client.getNumSeatsOnHold(Optional.empty());
		System.out.println("seats="+results);
		results = client.getNumSeatsSold(Optional.empty()); //Optional.of(Integer.valueOf(3))
		System.out.println("seats="+results);
		client.reserveSeats(Long.valueOf("1000010"),"paintedbowl@paint.com");
		client.findAndHoldSeats(200, Optional.of(2), Optional.of(3), "paintedbowl@paint.com");
	}
}
