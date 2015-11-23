This is a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.
The seating arrangement and pricing details for a simple venue below.
Level Id 	Level Name 		Price 		Rows 	Seats in Row
1			 Orchestra 		$100.00 	25 		50
2 			Main 			$75.00 		20 		100
3 			Balcony 1 		$50.00 		15 		100
4 			Balcony 2 		$40.00 		15 		100

Name Of the Venue: Bootleg Theater
Name Of the Event: Bob Marley Live

The ticket service has following options:
1.	Find the number of seats available within the venue by seating level. The available seats are neither held or reserved.
Command: TicketsAvailbale.bat -l <Level ID> (optional)
examples
TicketsAvailbale.bat
TicketsAvailbale.bat -l 1

2. Find and hold the best available seats on behalf of a customer, potentially limited to specific levels. Each ticket hold will expire in 30 seconds.
Command: FindAndHoldTickets.bat -x <Number Of Seats> -e <Customer Email> -n <Min Level ID> (optional) -m <Max Level ID> (optional)");
Examples
FindAndHoldTickets.bat -x 10 -e correct_email@gmail.com
FindAndHoldTickets.bat -x 10 -e correct_email@gmail.com -n 2 -m 4

3. Reserve and commit a specific group of held seats for a customer.
Command: ReserveTickets.bat -r <Request ID> -e <Customer Email>
Examples
ReserveTickets.bat -r 1448223830004 -e correct_email@gmail.com

How To?
Follow the seps below to install, compile and execute the TicketMart Service
Step 1: Pull the github project --> https://github.com/paintedbowl/walmart.git
Step 2: cd <github_home>\TicketMart\TicketService
Step 3: compile the maven project
		mvn clean install
Step 4: Execute the .bat file for the corresponding service options listed above.