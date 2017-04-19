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

@Service
public class TrackerServiceImpl implements TrackerService{
	
	private TrackerRepository trackerRepository;
	
	private List<TableA> tableAList;
	
	@Autowired
	public TrackerServiceImpl(final TrackerRepository trackerRepository) {
		this.trackerRepository = trackerRepository;
	}
	
	@PostConstruct
	public void init() throws IOException{
		trackerRepository.insertTableA();
		trackerRepository.insertTableB();
		trackerRepository.insertTableC();
		
		tableAList = trackerRepository.fetchAllFromTableA();
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

}
