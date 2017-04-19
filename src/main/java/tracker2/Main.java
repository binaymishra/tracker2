package tracker2;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		try {
			TrackerService trackerService = ctx.getBean(TrackerService.class);
			/*List<Map<String, Long>> maps = trackerService.findUserIpAnIntervalForTableA(120);
			for(Map<String, Long> map :maps){
				for(Map.Entry<String, Long> e : map.entrySet()){
					System.out.println(String.format("[%s	]  	= %d ", e.getKey(), e.getValue()));
				}
			}
			System.out.println("for 120 minutes. "+maps.size());*/
			
			//the early-birds(6 AM - 9 AM users)
			//trackerService.userSessionsTableA(6, 9);
			//the munchers(12 PM - 2 PM users)
			//trackerService.userSessionsTableA(12, 14);
			//the stompers(5 PM - 7 PM users)
			//trackerService.userSessionsTableA(17, 19);
			
			//trackerService.locationTableA();
			
			trackerService.locationTableB();
			
		} catch (Exception e) {
			System.err.println(e.toString());
			//e.printStackTrace();
		}finally{
			ctx.close();
		}

	}

}
