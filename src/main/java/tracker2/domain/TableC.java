package tracker2.domain;

public class TableC {
	String prefix;
	Double lat;
	Double lon;
	String name;
	String category;
	
	public TableC() {
		// TODO Auto-generated constructor stub
	}

	public TableC(String prefix, Double lat, Double lon, String name, String category) {
		super();
		this.prefix = prefix;
		this.lat = lat;
		this.lon = lon;
		this.name = name;
		this.category = category;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "TableC [prefix=" + prefix + ", lat=" + lat + ", lon=" + lon + ", name=" + name + ", category="
				+ category + "]";
	}
}
