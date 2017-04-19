package tracker2.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class TableA {
	
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
	
	String userIp;
	String userMac;
	String apName;
	String apMac;
	Long startTime;
	Long endTime;
	

	public TableA(String userIp, String userMac, String apName, String apMac, Long startTime, Long endTime) {
		super();
		this.userIp = userIp;
		this.userMac = userMac;
		this.apName = apName;
		this.apMac = apMac;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserMac() {
		return userMac;
	}

	public void setUserMac(String userMac) {
		this.userMac = userMac;
	}

	public String getApName() {
		return apName;
	}

	public void setApName(String apName) {
		this.apName = apName;
	}

	public String getApMac() {
		return apMac;
	}

	public void setApMac(String apMac) {
		this.apMac = apMac;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "TableA [userIp=" + userIp + ", userMac=" + userMac + ", apName=" + apName + ", apMac=" + apMac
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", duration()=" + duration()
				+ ", getStartTimeUTC=" + getStartTimeUTC() + ", getEndTimeUTC=" + getEndTimeUTC() + "]";
	}

	public long duration() {
		return (this.endTime - this.startTime);
	}
	
	public String getStartTimeUTC(){
			DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(this.startTime * 1000L);
			String startDateUTC = DATE_FORMAT.format(calendar.getTime());
			return startDateUTC;
	}
	
	public String getEndTimeUTC(){
			DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(this.endTime * 1000L);
			String startDateUTC = DATE_FORMAT.format(calendar.getTime());
			return startDateUTC;
	}
}
