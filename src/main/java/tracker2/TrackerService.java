package tracker2;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public interface TrackerService {
	
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
	
	List<Map<String, Long>> findUserIpAnIntervalForTableA(long minutes);
	
	void userSessionsTableA(int startHour, int endHour);

}
