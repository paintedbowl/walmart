package ticketmart.bean;

public class TicketInfo {
  private int levelId;
  private String levelName;
  private double price;
  private int rows;
  private int numOfSeats;
  
  public TicketInfo(int _levelId,String _levelName,double _price,int _rows,int _numOfSeats){
	  this.levelId = _levelId;
	  this.levelName = _levelName;
	  this.price = _price;
	  this.rows = _rows;
	  this.numOfSeats = _numOfSeats;
  }
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public String getLevelIdName() {
		return levelName;
	}
	public void setLevelIdName(String levelIdName) {
		levelName = levelIdName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double _price) {
		price = _price;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int _rows) {
		rows = _rows;
	}
	public int getNumOfSeats() {
		return numOfSeats;
	}
	public void setNumOfSeats(int _numOfSeats) {
		numOfSeats = _numOfSeats;
	} 
	  
  
}
