package tracker2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import tracker2.domain.TableA;
import tracker2.domain.TableB;
import tracker2.domain.TableC;

@Component
public class TrackerComponent {
	
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
	
	static final String COMMA = ",";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	/**
	 * @return
	 * @throws IOException
	 */
	public List<TableA> readFileTableA20120407() throws IOException{
		File file = resourceLoader.getResource("classpath:TableA20120407.csv").getFile();
		List<TableA> tableA = new ArrayList<TableA>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			String line;
			reader.readLine(); // skip first line i,e header of the file.
			while((line = reader.readLine()) !=  null){
				String[] arr = line.split(COMMA);
				tableA.add(new TableA(arr[0].trim(), arr[1].trim(), arr[2].trim(), arr[3].trim(), Long.parseLong(arr[4]), Long.parseLong(arr[5])));
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}catch (IOException e) {
			System.err.println(e.toString());
		}finally{
			reader.close();
		}
		return tableA;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public List<TableB> readFileTableB20120409() throws IOException{
		File file = resourceLoader.getResource("classpath:TableB20120409.csv").getFile();
		List<TableB> tableB = new ArrayList<TableB>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			String line;
			reader.readLine(); // skip first line i,e header of the file.
			while((line = reader.readLine()) !=  null){
				String[] arr = line.split(COMMA);
				tableB.add(new TableB(arr[0].trim(), arr[1].trim(), arr[2].trim(), arr[3].trim(), Long.parseLong(arr[4]), Long.parseLong(arr[5])));
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}catch (IOException e) {
			System.err.println(e.toString());
		}finally{
			reader.close();
		}
		return tableB;
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	public List<TableC> readFileTableC() throws IOException{
		File file = resourceLoader.getResource("classpath:TableC.csv").getFile();
		List<TableC> tableC = new ArrayList<TableC>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			String line;
			reader.readLine(); // skip first line i,e header of the file.
			while((line = reader.readLine()) !=  null){
				String[] arr = line.split(COMMA);
				tableC.add(new TableC(arr[0], Double.valueOf(arr[1]), Double.valueOf(arr[2]), arr[3], arr[4]));
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}catch (IOException e) {
			System.err.println(e.toString());
		}finally{
			reader.close();
		}
		return tableC;
	}
	
	
	/**
	 * @param tableAs
	 * @return
	 */
	public void morning(List<TableA> tableAs){
		Map<String, List<Long>> morningUserSessionDurationsMap = new HashMap<String, List<Long>>();
		List<TableA> tableA6to9 = new ArrayList<TableA>();
		for(TableA tableA : tableAs){
			
			long startTime 	= tableA.getStartTime() * 1000L;
			long endTime 	= tableA.getEndTime() * 1000L;
			
			Calendar fromHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			fromHour.setTimeInMillis(startTime);
			
			Calendar toHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			toHour.setTimeInMillis(endTime);
			
			if(fromHour.get(Calendar.HOUR_OF_DAY) >= 6 && toHour.get(Calendar.HOUR_OF_DAY) <= 9){
				String key = tableA.getUserIp();
				List<Long> durations = morningUserSessionDurationsMap.get(key);
				if(durations == null || durations.isEmpty()){
					List<Long> d = new ArrayList<Long>();
					d.add(tableA.duration());
					morningUserSessionDurationsMap.put(key, d);
				}else{
					durations.add(tableA.duration());
					Collections.sort(durations);
					morningUserSessionDurationsMap.put(key, durations);
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
		for(Map.Entry<String,List<Long>> entry: morningUserSessionDurationsMap.entrySet()){
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
	
	/**
	 * @param tableAs
	 * @return
	 */
	public void afterNoon(List<TableA> tableAs){
		Map<String, List<Long>> afterNoonUserSessionDurationsMap = new HashMap<String, List<Long>>();
		List<TableA> tableA12to2 = new ArrayList<TableA>();
		for(TableA tableA : tableAs){
			long startTime = tableA.getStartTime() * 1000L;
			long endTime = tableA.getEndTime() * 1000L;
			
			Calendar fromHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			fromHour.setTimeInMillis(startTime);
			
			Calendar toHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			toHour.setTimeInMillis(endTime);
			
			if(fromHour.get(Calendar.HOUR_OF_DAY) >= 12 && toHour.get(Calendar.HOUR_OF_DAY) <= 14){
				String key = tableA.getUserIp();
				List<Long> durations = afterNoonUserSessionDurationsMap.get(key);
				if(durations == null || durations.isEmpty()){
					List<Long> d = new ArrayList<Long>();
					d.add(tableA.duration());
					afterNoonUserSessionDurationsMap.put(key, d);
				}else{
					durations.add(tableA.duration());
					Collections.sort(durations);
					afterNoonUserSessionDurationsMap.put(key, durations);
				}
				tableA12to2.add(tableA);
			}
		}
		// 12 PM - 2 PM users.
		System.out.println("tableA12to2 = " + tableA12to2.size());
		// A map of userIp and list if duration/session(formula : endTime - startTime) stayed connected.
				//System.out.println("morningUserSessionDurationsMap = " + morningUserSessionDurationsMap.size()); 
				//System.out.println(morningUserSessionDurationsMap);
				
				//Total duration/session
				Map<String, Long> totalDurationMap = new HashMap<String, Long>();
				for(Map.Entry<String,List<Long>> entry: afterNoonUserSessionDurationsMap.entrySet()){
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
				System.out.println("********************* Highest / Lowest Duration between (12 PM - 2 PM ) = "+highestDuration +" / "+lowestDuration+" *********************");
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
	
	/**
	 * @param tableAs
	 * @return
	 */
	public void evening(List<TableA> tableAs){
		Map<String, List<Long>> eveningUserSessionDurationsMap = new HashMap<String, List<Long>>();
		List<TableA> tableA17to19 = new ArrayList<TableA>();
		for(TableA tableA : tableAs){
			long startTime = tableA.getStartTime() * 1000L;
			long endTime = tableA.getEndTime() * 1000L;
			
			Calendar fromHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			fromHour.setTimeInMillis(startTime);
			
			Calendar toHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			toHour.setTimeInMillis(endTime);
			
			if(fromHour.get(Calendar.HOUR_OF_DAY) >= 17 && toHour.get(Calendar.HOUR_OF_DAY) <= 19){
				String key = tableA.getUserIp();
				List<Long> durations = eveningUserSessionDurationsMap.get(key);
				if(durations == null || durations.isEmpty()){
					List<Long> d = new ArrayList<Long>();
					d.add(tableA.duration());
					eveningUserSessionDurationsMap.put(key, d);
				}else{
					durations.add(tableA.duration());
					Collections.sort(durations);
					eveningUserSessionDurationsMap.put(key, durations);
				}
				tableA17to19.add(tableA);
			}
		}
		// 5 PM - 7 PM users.
		System.out.println("tableA17to19 = " + tableA17to19.size());
		// A map of userIp and list if duration/session(formula : endTime - startTime) stayed connected.
		//System.out.println("morningUserSessionDurationsMap = " + morningUserSessionDurationsMap.size()); 
		//System.out.println(morningUserSessionDurationsMap);
		
		//Total duration/session
		Map<String, Long> totalDurationMap = new HashMap<String, Long>();
		for(Map.Entry<String,List<Long>> entry: eveningUserSessionDurationsMap.entrySet()){
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
		System.out.println("********************* Highest / Lowest Duration between (5 PM - 7 PM ) = "+highestDuration +" / "+lowestDuration+" *********************");
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
