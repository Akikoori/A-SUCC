package coreASUCC;

public class PostRecord {

	private String userID;
	private int timestamp;
	private int topicID;
	private String content;

	public PostRecord(String userID, int timestamp, int topicID, String content) {
		this.userID = userID;
		this.timestamp = timestamp;
		this.topicID = topicID;
		this.content = content;
	}

	public String getUserID() {
		return userID;
	}

	public int getTimeStamp() {
		return timestamp;
	}

	public int getTopicID() {
		return topicID;
	}

	public String getContent() {
		return content;
	}

}
