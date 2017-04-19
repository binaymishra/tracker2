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
	
	@Autowired
	public TrackerServiceImpl(final TrackerRepository trackerRepository) {
		this.trackerRepository = trackerRepository;
	}
	
	@PostConstruct
	public void init() throws IOException{
		trackerRepository.insertTableA();
		trackerRepository.insertTableB();
		trackerRepository.insertTableC();
	}

	@Override
	public List<Map<String, Long>> findUserIpAnIntervalForTableA(long minutes) {
		List<TableA> tableA = trackerRepository.fetchAllFromTableA();
		
		long startTime = tableA.get(0).getStartTime();
		long endTime = tableA.get(tableA.size() - 1).getEndTime();
		
		Calendar calendar = Calendar.getInstance();
		
		List<Map<String, Long>> userIpCounts = new ArrayList<Map<String,Long>>();
		while(startTime <= endTime){
			startTime += minutes * 60;
			Map<String, Long> userIpMap = new HashMap<String, Long>();
			for(TableA a: tableA){
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
		List<TableA> tableAList = trackerRepository.fetchAllFromTableA();
		Map<String, List<Long>> userSessionDurationsMap = new HashMap<String, List<Long>>();
		List<TableA> tableA6to9 = new ArrayList<TableA>();
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
				tableA6to9.add(tableA);
			}
			
		}
		// 6 AM - 9 AM users.
		System.out.println("tableA6to9 = " + tableA6to9.size()); 
		// A map of userIp and list if duration/session(formula : endTime - startTime) stayed connected.
		//System.out.println("morningUserSessionDurationsMap = " + morningUserSessionDurationsMap.size()); 
		//System.out.println(morningUserSessionDurationsMap);
		
		//Total duration/session
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
		
		//System.out.println("totalDurationMap.size() = "+totalDurationMap.size());
		//System.out.println("totalDurationMap = "+totalDurationMap);
		List<Long> temp = new ArrayList<Long>(totalDurationMap.values());
		Collections.sort(temp);
		//System.out.println("sortedDuration = "+temp);
		Long lowestDuration = temp.get(0);
		Long highestDuration = temp.get(temp.size() - 1);
		System.out.println("********************* Highest / Lowest Duration between (6 AM - 9 AM ) = "+highestDuration +" / "+lowestDuration+" *********************");
		for(Map.Entry<String, Long> entry :totalDurationMap.entrySet()){
			String key = entry.getKey();
			Long valLong = entry.getValue();
			if(lowestDuration.equals(valLong)){
				System.out.println(String.format("[%s ] userIp has lowest duration/session = %d.", key, valLong));
			}if(highestDuration.equals(valLong)){
				System.out.println(String.format("[%s ] userIp has highest duration/session = %d.", key, valLong));
			}
		}
	}

}
