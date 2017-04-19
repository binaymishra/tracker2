package tracker2;

import java.util.List;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tracker2.domain.TableA;
import tracker2.domain.TableB;
import tracker2.domain.TableC;

public class Main {

	public static void main(String[] args) {
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		try {
			TrackerService 		trackerService 		= ctx.getBean(TrackerService.class);
			TrackerComponent 	trackerComponent	= ctx.getBean(TrackerComponent.class);
			
			List<TableA> tableAList = trackerComponent.readFileTableA20120407();
			List<TableB> tableBList = trackerComponent.readFileTableB20120409();
			List<TableC> tableCList = trackerComponent.readFileTableC();
			/*
			for(Map<String, Long> userIpCountMap :trackerService.findUserIpAnIntervalForTableA(tableAList, 12)){
				for(Map.Entry<String, Long> entry : userIpCountMap.entrySet()){
					System.out.println(entry.getKey()+ " = "+entry.getValue());
				}
			}*/
			
			//the early-birds(6 AM - 9 AM users)
			trackerService.userSessionsTableA(tableAList, 6, 9);
			//the munchers(12 PM - 2 PM users)
			trackerService.userSessionsTableA(tableAList, 12, 14);
			//the stompers(5 PM - 7 PM users)
			trackerService.userSessionsTableA(tableAList, 17, 19);
			
			
			trackerService.locationTableA(tableAList, tableCList);
			
			trackerService.locationTableB(tableBList, tableCList);
			
		} catch (Exception e) {
			System.err.println(e.toString());
			//e.printStackTrace();
		}finally{
			ctx.close();
		}

	}

}
