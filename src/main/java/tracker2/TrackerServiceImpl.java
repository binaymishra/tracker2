package tracker2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import tracker2.domain.TableA;
import tracker2.domain.TableB;
import tracker2.domain.TableC;

@Service
public class TrackerServiceImpl implements TrackerService{
	
	private Comparator<TableA> sortByStartTime = new Comparator<TableA>() {
		
		@Override
		public int compare(TableA o1, TableA o2) {
			return o1.getStartTime().compareTo(o2.getStartTime());
		}
	};
	

	@Override
	public List<Map<String, Long>> findUserIpAnIntervalForTableA(List<TableA> tableAList, long minutes) {
		
		Collections.sort(tableAList, sortByStartTime);
		
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
		System.out.println(String.format("startTime = %d, endTime = %d. userIpCounts = %d", startTime, endTime, userIpCounts.size()));
		return userIpCounts;
	}

	@Override
	public void userSessionsTableA(List<TableA> tableAList, final int startHour, final int endHour) {
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
		//Long lowestDuration = temp.get(0);
		//Long highestDuration = temp.get(temp.size() - 1);
		List<Long> highestFive = temp.subList(temp.size() - 6, temp.size() - 1);
		System.out.println(String.format("********************* Highest 5 :"+highestFive.toString()+" *********************", startHour, endHour));
		for(Map.Entry<String, Long> entry :totalDurationMap.entrySet()){
			String key = entry.getKey();
			Long valLong = entry.getValue();
			if(highestFive.contains(valLong)){
				System.out.println(String.format("[%-20s ] userIp has lowest session  = %d hours", key, valLong / (60 * 60)));
			}
		}
	}

	@Override
	public void locationTableA(List<TableA> tableAList, List<TableC> tableCList) {
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
	public void locationTableB(List<TableB> tableBList, List<TableC> tableCList) {
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
