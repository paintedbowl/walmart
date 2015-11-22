package ticketmart.bean;

import java.time.Instant;

public class TicketRequestInfo {
	private Integer levelId;
	private String eventType;
	private int numOfSeats;
	private Instant createTime;
	
	public TicketRequestInfo(Integer _levelId,String _eventType,int _numOfSeats,Instant _createTime){
		this.levelId=_levelId;
		this.eventType=_eventType;
		this.numOfSeats=_numOfSeats;
		this.createTime=_createTime;
	}
	public Integer getLevelId() {
		return levelId;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String _eventType) {
		this.eventType = _eventType;
	}
	public int getNumOfSeats() {
		return numOfSeats;
	}
	public Instant getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Instant createTime) {
		this.createTime = createTime;
	}
	public void setNumOfSeats(int numOfSeats) {
		this.numOfSeats = numOfSeats;
	}
	  
}
