package tracker2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tracker2.domain.TableA;
import tracker2.domain.TableB;
import tracker2.domain.TableC;

@Service
public class TrackerServiceImpl implements TrackerService{
	
	
	private TrackerComponent trackerComponent;
	
	private List<TableA> tableAList;
	
	private List<TableB> tableBList;
	
	private List<TableC> tableCList;
	
	@Autowired
	public TrackerServiceImpl(final TrackerComponent trackerComponent) {
		this.trackerComponent = trackerComponent;
	}
	
	@PostConstruct
	public void init() throws IOException{
		
		tableAList = trackerComponent.readFileTableA20120407();
		tableBList = trackerComponent.readFileTableB20120409();
		tableCList = trackerComponent.readFileTableC();
	}

	@Override
	public List<Map<String, Long>> findUserIpAnIntervalForTableA(long minutes) {
		
		long startTime = tableAList.get(0).getStartTime();
		long endTime = tableAList.get(tableAList.size() - 1).getEndTime();
		
		Calendar calendar = Calendar.getInstance();
		
		List<Map<String, Long>> userIpCounts = new ArrayList<Map<String,Long>>();
		while(startTime <= endTime){
			startTime += minutes * 60;
			Map<String, Long> userIpMap = new HashMap<String, Long>();
			for(TableA a: tableAList){
				if(a.getStartTime() <= startTime){
					String key = a.getUserIp();
					Long count = userIpMap.get(key);
					Long newCount = count == null ? 1 : count + 1; 
					userIpMap.put(key,  newCount);
				}
			}
			calendar.setTimeInMillis(startTime * 1000L);
			userIpCounts.add(userIpMap);
		}
		return userIpCounts;
	}

	@Override
	public void userSessionsTableA(final int startHour, final int endHour) {
		Map<String, List<Long>> userSessionDurationsMap = new HashMap<String, List<Long>>();
		List<TableA> tableAs = new ArrayList<TableA>();
		for(TableA tableA : tableAList){
			
			long startTime 	= tableA.getStartTime() * 1000L;
			long endTime 	= tableA.getEndTime() * 1000L;
			
			Calendar fromHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			fromHour.setTimeInMillis(startTime);
			
			Calendar toHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			toHour.setTimeInMillis(endTime);
			
			if(fromHour.get(Calendar.HOUR_OF_DAY) >= startHour && toHour.get(Calendar.HOUR_OF_DAY) <= endHour){
				String key = tableA.getUserIp();
				List<Long> durations = userSessionDurationsMap.get(key);
				if(durations == null || durations.isEmpty()){
					List<Long> d = new ArrayList<Long>();
					d.add(tableA.duration());
					userSessionDurationsMap.put(key, d);
				}else{
					durations.add(tableA.duration());
					Collections.sort(durations);
					userSessionDurationsMap.put(key, durations);
				}
				tableAs.add(tableA);
			}
		}
		
		Map<String, Long> totalDurationMap = new HashMap<String, Long>();
		for(Map.Entry<String,List<Long>> entry: userSessionDurationsMap.entrySet()){
			String key = entry.getKey();
			for(Long duration : entry.getValue()){
				Long value = totalDurationMap.get(key);
				if(value == null){
					totalDurationMap.put(key, duration);
				}else{
					Long newValue = value + duration;
					totalDurationMap.put(key, newValue);
				}
			}
		}
		
		List<Long> temp = new ArrayList<Long>(totalDurationMap.values());
		Collections.sort(temp);
		Long lowestDuration = temp.get(0);
		Long highestDuration = temp.get(temp.size() - 1);
		System.out.println(String.format("********************* Highest / Lowest Duration between (%d  - %d ) = "+highestDuration +" / "+lowestDuration+" *********************", startHour, endHour));
		for(Map.Entry<String, Long> entry :totalDurationMap.entrySet()){
			String key = entry.getKey();
			Long valLong = entry.getValue();
			if(lowestDuration.equals(valLong)){
				System.out.println(String.format("[%-20s ] userIp has lowest session  = %d sec", key, valLong));
			}if(highestDuration.equals(valLong)){
				System.out.println(String.format("[%-20s ] userIp has highest session = %d sec", key, valLong));
			}
		}
	}

	@Override
	public void locationTableA() {
		Map<TableC, List<TableA>> mapOfTableAs = new HashMap<TableC, List<TableA>>();
		for(TableC c: tableCList){
			for(TableA a : tableAList){
				if(a.getApName().contains(c.getPrefix())){
					List<TableA> values = mapOfTableAs.get(c);
					if(values == null || values.isEmpty()){
						List<TableA> val = new ArrayList<TableA>();
						val.add(a);
						mapOfTableAs.put(c, val);
					}else{
						values.add(a);
						mapOfTableAs.put(c, values);
					}
				}
			}
		}
		
		
		for(Map.Entry<TableC, List<TableA>> entry: mapOfTableAs.entrySet()){
			TableC key = entry.getKey();
			List<TableA> values = entry.getValue();
			
			System.out.println(key.getPrefix() +" : "+values.size());
			
			
		}
	}

	@Override
	public void locationTableB() {
		Map<TableC, List<TableB>> mapOfTableAs = new HashMap<TableC, List<TableB>>();
		for(TableC c: tableCList){
			for(TableB b : tableBList){
				if(b.getApName().contains(c.getPrefix())){
					List<TableB> values = mapOfTableAs.get(c);
					if(values == null || values.isEmpty()){
						List<TableB> val = new ArrayList<TableB>();
						val.add(b);
						mapOfTableAs.put(c, val);
					}else{
						values.add(b);
						mapOfTableAs.put(c, values);
					}
				}
			}
		}
		
		
		for(Map.Entry<TableC, List<TableB>> entry: mapOfTableAs.entrySet()){
			TableC key = entry.getKey();
			List<TableB> values = entry.getValue();
			
			System.out.println(key.getPrefix() +" : "+values.size());
			
			
		}
	}

}
