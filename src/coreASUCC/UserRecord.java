package coreASUCC;

public class UserRecord {

	private String name;
	private int frequency;
	private String hoverUrl;
	private String rank;

	public UserRecord(String name, int frequency, String hoverUrl) {
		this.name = name;
		this.frequency = frequency;
		this.hoverUrl = hoverUrl;
	}

	public String getName() {
		return name;
	}

	public int getFrequency() {
		return frequency;
	}

	public String getHoverUrl() {
		return hoverUrl;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

}
