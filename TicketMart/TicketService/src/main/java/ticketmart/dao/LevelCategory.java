package ticketmart.dao;

import java.util.HashMap;
import java.util.Map;

public enum LevelCategory {
	ORCHESTRA(1)
	,MAIN(2)
	,BALCONY_1(3)
	,BALCONY_2(4);
	
	private static final Map<Integer,LevelCategory> intMap  ;
	static {
		intMap = new HashMap<Integer, LevelCategory>() ;
		for (LevelCategory item : LevelCategory.values()) {
			intMap.put(item.getIntValue(), item) ;
		}
	}
	
	private final int intValue ;
	private LevelCategory(int intValue) {
		this.intValue = intValue ;
	}

	public int getIntValue() {
		return this.intValue ;
	}

	public static LevelCategory fromIntValue(int intValue) {
		return intMap.get(intValue) ;
	}
}
