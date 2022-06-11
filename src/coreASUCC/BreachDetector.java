package coreASUCC;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BreachDetector {

	private List<PostRecord> postRecords;
	
	public BreachDetector(List<PostRecord> prs) {
		this.postRecords = prs;
	}
	
	public List<String> checkExcessiveReply() {
		List<String> guilties = new ArrayList<String>();
		String[] userArray = generateUserSet();
		for (int i = 0; i < userArray.length; i++ ) {
			boolean excessive = checkUserExcessiveReply(userArray[i]);
			if (excessive)
				guilties.add(userArray[i]);
		}
		return guilties;
	}
	
	private boolean checkUserExcessiveReply(String userID) {
		List<PostRecord> uprs = new ArrayList<PostRecord>();
		List<Integer> userReplyTopics = new ArrayList<Integer>(), userReplyTimeStamps = new ArrayList<Integer>();
		//分离出指定用户的postRecord和对应ArrayLists
		for (int i = 0; i < postRecords.size(); i++ ) {
			PostRecord pr = postRecords.get(i);
			if (pr.getUserID().equals(userID)) {
				uprs.add(pr);
				userReplyTopics.add(pr.getTopicID());
				userReplyTimeStamps.add(pr.getTimeStamp());
			}
		}
		int topicNumber = new HashSet<Integer>(userReplyTopics).size();
		if (topicNumber < 8)
			return false;
		//检测第i回复前30分钟内是否有八个回复
			for(int i = 0; i < uprs.size() - 8; i++) {
				List<Integer> checkList = new ArrayList<Integer>();
				int lowerLimit = userReplyTimeStamps.get(i) - 1800;
				for (int j = 0; userReplyTimeStamps.get(i+j) >= lowerLimit; j++) {
					if (j > 0 && userReplyTopics.get(i).equals(userReplyTopics.get(i+j)))
						break;
					checkList.add(userReplyTopics.get(i+j));
				}
				Set<Integer> checkSet = new HashSet<Integer>(checkList);
				if (checkSet.size() >= 8)
					return true;
			}
		//如果无大于等于八个则return false
		return false;
	}
	
	private String[] generateUserSet(){
		List<String> repeatedUserList = new ArrayList<String> ();
		for (int i = 0; i < postRecords.size(); i++)
			repeatedUserList.add(postRecords.get(i).getUserID());
		String[] userArray = new HashSet<String>(repeatedUserList).toArray(String[]::new);
		return userArray;
	}
	
}
