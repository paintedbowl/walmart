package ticketmart.bean;

import java.util.ArrayList;
import java.util.List;

public class SeatHold {
	private Long requestId;
	private List<TicketRequestInfo> requests = new ArrayList<TicketRequestInfo>();
	private String customerEmail;
	private boolean requestFulfilled = true;
	
	public SeatHold(Long _requestId,String _customerEmail,List<TicketRequestInfo> _requests, boolean _requestFulfilled){
		this.requestId=_requestId;
		this.requests=_requests;
		this.customerEmail=_customerEmail;
		this.requestFulfilled = _requestFulfilled;
	}
	
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long _requestId) {
		this.requestId = _requestId;
	}
	public List<TicketRequestInfo> getRequests() {
		return requests;
	}
	public void setRequests(List<TicketRequestInfo> _requests) {
		this.requests = _requests;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String _customerEmail) {
		this.customerEmail = _customerEmail;
	}

	public boolean isRequestFulfilled() {
		return requestFulfilled;
	}

	public void setRequestFulfilled(boolean requestFulfilled) {
		this.requestFulfilled = requestFulfilled;
	}
}
