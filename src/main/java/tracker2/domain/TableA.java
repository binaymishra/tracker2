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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apMac == null) ? 0 : apMac.hashCode());
		result = prime * result + ((apName == null) ? 0 : apName.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((userIp == null) ? 0 : userIp.hashCode());
		result = prime * result + ((userMac == null) ? 0 : userMac.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableA other = (TableA) obj;
		if (apMac == null) {
			if (other.apMac != null)
				return false;
		} else if (!apMac.equals(other.apMac))
			return false;
		if (apName == null) {
			if (other.apName != null)
				return false;
		} else if (!apName.equals(other.apName))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (userIp == null) {
			if (other.userIp != null)
				return false;
		} else if (!userIp.equals(other.userIp))
			return false;
		if (userMac == null) {
			if (other.userMac != null)
				return false;
		} else if (!userMac.equals(other.userMac))
			return false;
		return true;
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
