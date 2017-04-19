package tracker2;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import tracker2.domain.TableA;
import tracker2.domain.TableB;
import tracker2.domain.TableC;

public interface TrackerService {
	
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
	
	List<Map<String, Long>> findUserIpAnIntervalForTableA(List<TableA> tableAList, long minutes);
	
	void userSessionsTableA(List<TableA> tableAList, final int startHour, final int endHour);
	
	void locationTableA(List<TableA> tableAList, List<TableC> tableCList);
	
	void locationTableB(List<TableB> tableBList, List<TableC> tableCList);

}
