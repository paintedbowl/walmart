package ticketmart.service;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import gnu.getopt.Getopt;
import ticketmart.bean.SeatHold;
import ticketmart.bean.TicketRequestInfo;
import ticketmart.dao.impl.TicketRequestDaoImpl;
import ticketmart.dao.impl.VenueInfoDaoImpl;

public class TicketMartService {
	public static final String FIND_TICKET_AVAILABLITY = "find";
	public static final String HOLD_TICKET = "hold";
	public static final String RESERVE_TICKET = "reserve";
	public static final String EVENT = "Bob Marley Live";
	
    private static void printFindSeatsUsage() {
        System.out.println("Usage: TicketsAvailbale.bat"         		
        		+" -l <Level ID> (optional)");
    }
    private static void printHoldSeatsUsage() {
    	System.out.println("Usage: FindAndHoldTickets.bat "
    			+" -x <Number Of Seats>"
        		+" -e <Customer Email> "
        		+" -n <Min Level ID> (optional)"
        		+" -m <Max Level ID> (optional)");
    	System.out.println("eg: FindAndHoldTickets.bat -x 10 -e correct_email@gmail.com -n 2 -m 4");
    }
    private static void printReserveSeatsUsage() {
    	System.out.println("Usage: ReserveTickets.bat "
    			+" -r <Request ID>"
        		+" -e <Customer Email> ");
    	System.out.println("eg: ReserveTickets.bat -r 1448223830004 -e correct_email@gmail.com");
    }
    
    private static void printValidationMessage(String reqType,String message){
    	System.out.println("\n************* Ticket Mart *****************");
    	System.out.println("\n"+message+"\n");
    	if(FIND_TICKET_AVAILABLITY.equalsIgnoreCase(reqType)) printFindSeatsUsage();
    	if(HOLD_TICKET.equalsIgnoreCase(reqType)) printHoldSeatsUsage();
    	if(RESERVE_TICKET.equalsIgnoreCase(reqType)) printReserveSeatsUsage();    	
    	System.out.println("\n************* End Of Message *****************");
    	System.exit(0) ;
    }
    
	public static void main(String[] argv) throws Exception {		
        
        Optional<Integer> levelId = Optional.empty() ;
        Optional<Integer> levelIdMin = Optional.empty() ;
        Optional<Integer> levelIdMax = Optional.empty() ;
        String customerEmail = null ;
        String requestId = null ;
        int numberOfSeats = 0 ;
        String testCaseName=null;
        try{
	        Getopt g = new Getopt(TicketMartService.class.getName(), argv, "t:l:e:n:m:r:x:");
	        g.setOpterr(false);
	        int c ;
	        while ((c = g.getopt()) != -1) {
	            switch (c) {
	            case 't':
	            	testCaseName = g.getOptarg() ;
	                break;
	            case 'l':
	            	levelId = Optional.of(Integer.valueOf(g.getOptarg().toLowerCase()));
	                break;
	            case 'e':
	            	customerEmail = g.getOptarg() ;
	                break;
	            case 'n':
	            	levelIdMin = Optional.of(Integer.valueOf(g.getOptarg().toLowerCase()));
	                break;
	            case 'm':
	            	levelIdMin = Optional.of(Integer.valueOf(g.getOptarg().toLowerCase()));
	                break;
	            case 'r':
	            	requestId = g.getOptarg() ;
	                break;
	            case 'x':
	            	numberOfSeats = Integer.valueOf(g.getOptarg()).intValue();
	                break;
	            case '?':
	            default:
	            	System.out.println("invalid option provided..");
	                System.exit(0);
	            }
	        }	        
			
	        if(FIND_TICKET_AVAILABLITY.equalsIgnoreCase(testCaseName)){
	        	HashMap <Integer,Integer> results = TicketMartServiceHolder.getInstance().numberOfSeatsAvailable(levelId);
	    		Set<Integer> keys =  results.keySet();
	    		System.out.println("\n************* TicketMart *****************");
	    		System.out.println("\nVenue:"+VenueInfoDaoImpl.VENUE_NAME);
	    		System.out.println("Event:"+TicketMartService.EVENT+"\n");
	    		for(Integer level : TicketRequestDaoImpl.emptyIfNull(keys)){
	    			System.out.println("Level: "+level.toString()+"\t Number Of Seats: "+results.get(level).toString());
	    		}
	    		System.out.println("\n************* End Of Message *****************");
	        }
	        
	        if(HOLD_TICKET.equalsIgnoreCase(testCaseName)){
	        	if(customerEmail == null){
	        		TicketMartService.printValidationMessage(testCaseName,"Please provice Customer Email to hold the tickets.");
	        	}
	        	if(numberOfSeats == 0){
	        		TicketMartService.printValidationMessage(testCaseName,"Please provice number of tickets to be held.");
	        	}
	        	SeatHold results = TicketMartServiceHolder.getInstance().findAndHoldSeats(numberOfSeats, levelIdMin, levelIdMax, customerEmail);
	    		System.out.println("\n************* TicketMart *****************");
	    		System.out.println("\nVenue:"+VenueInfoDaoImpl.VENUE_NAME);	    		
	    		System.out.println("Event:"+EVENT+"\n");
	    		
	    		System.out.println("Request ID:"+results.getRequestId());
	    		System.out.println("Customer Email:"+results.getCustomerEmail());
	    		
	    		if(results.getRequests() != null && !results.getRequests().isEmpty()){
	    			if(!results.isRequestFulfilled()){
		    			System.out.println("\n The request has been processed partially. Please use the TicketsAvailbale.bat to view the available tickets.");
		    		}
		    		System.out.println("\nTicket Information:");
		    		for(TicketRequestInfo tkt : TicketRequestDaoImpl.emptyIfNull(results.getRequests())){
		    			System.out.println("Level: "+tkt.getLevelId()+"\t Number Of Seats: "+tkt.getNumOfSeats());
		    		}	    		
		    		System.out.println("\nYou have one minute to confirm this reservation. Please use the Request Id "+results.getRequestId()+" to reserve the seats.");
	    		}else{
	    			System.out.println("\n Sorry, there are no tickets available for the request. Please use the TicketsAvailbale.bat to view the available tickets.");
	    		}
	    		System.out.println("\n************* End Of Message *****************");
	        }
	        
	        if(RESERVE_TICKET.equalsIgnoreCase(testCaseName)){
	        	if(customerEmail == null){
	        		TicketMartService.printValidationMessage(testCaseName,"Please provice Customer Email to hold the tickets.");
	        	}
	        	if(requestId == null){
	        		TicketMartService.printValidationMessage(testCaseName,"Please provice a request id to reserve the ticket.");
	        	}
	        	SeatHold results = TicketMartServiceHolder.getInstance().reserveSeats(requestId, customerEmail);
	    		System.out.println("\n************* TicketMart *****************");
	    		System.out.println("\nVenue:"+VenueInfoDaoImpl.VENUE_NAME);	    		
	    		System.out.println("Event:"+EVENT+"\n");
	    		
	    		System.out.println("Request ID:"+results.getRequestId());
	    		System.out.println("Customer Email:"+results.getCustomerEmail());
	    		System.out.println("\nTicket Information:");
	    		if(results.getRequests() != null && results.getRequests().isEmpty()){
	    			System.out.println("Request ID "+results.getRequestId()+"not found");
	    			
	    		}else{
		    		for(TicketRequestInfo tkt : TicketRequestDaoImpl.emptyIfNull(results.getRequests())){
		    			System.out.println("Level: "+tkt.getLevelId()+"\t Number Of Seats: "+tkt.getNumOfSeats());
		    		}
		    		System.out.println("\n The ticket above has been reserved.");
	    		}
	    		System.out.println("\n************* End Of Message *****************");
	        }
	        
        }catch(Exception exp){
        	System.out.println("Error while processing the request.."+exp.toString());
        	exp.printStackTrace();
        }

        System.exit(0) ;
	}
}
