package TicketMart.TicketService;

import java.util.HashMap;
import java.util.Optional;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ticketmart.dao.TicketRequestDao;
import ticketmart.dao.impl.TicketRequestDaoImpl;
import ticketmart.dao.impl.VenueInfoDaoImpl;

/**
 * Unit test for simple App.
 */
public class TicketMartTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TicketMartTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	TestSuite suite = new TestSuite( TicketMartTest.class );
//    	suite.addTest(new TicketMartTest("testVenueInfo"));
//    	suite.addTest(new TicketMartTest("testNumerOfSeatsAvailable"));
//    	suite.addTest(new TicketMartTest("testFindAndHoldSeats"));
//    	suite.addTest(new TicketMartTest("testReserveSeats"));
        return new TestSuite( TicketMartTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testTicketMart()
    {
        assertTrue( true );
    }
    
    public void testVenueInfo(){
    	VenueInfoDaoImpl client = new VenueInfoDaoImpl();
    	int results = client.getNumSeats(5);
    	System.out.println("seats="+results);
    }
    
    public void testNumerOfSeatsAvailable() {
    	TicketRequestDao client = new TicketRequestDaoImpl();
		HashMap <Integer,Integer> results = client.numberOfSeatsAvailable(Optional.empty());
	}

	public void testFindAndHoldSeats() {
		TicketRequestDao client = new TicketRequestDaoImpl();
		client.findAndHoldSeats(200, Optional.of(2), Optional.of(3), "paintedbowl@paint.com");
	}

	public void testReserveSeats() {
		TicketRequestDao client = new TicketRequestDaoImpl();
		client.reserveSeats(Long.valueOf("1000010"),"paintedbowl@paint.com");
	}

	public void testNumSeatsAvailable() {
		TicketRequestDao client = new TicketRequestDaoImpl();
		int results = client.numSeatsAvailable(Optional.empty());
		System.out.println("results="+results);
	}
}
