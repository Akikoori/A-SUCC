package coreASUCC;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PostInfoParser {

	public PostInfoParser() {

	}

	public static List<String> stringConvert(List<PostRecord> prs) throws Exception {
		List<String> infoStrings = new ArrayList<String>();
		for (int i = 0; i < prs.size(); i++) {
			PostRecord ur = prs.get(i);
			infoStrings
					.add(ur.getUserID() + "	" + ur.getTopicID() + "	" + ur.getTimeStamp() + "	" + ur.getContent());
		}
		return infoStrings;
	}

	public List<PostRecord> cleanStream(Elements stream) {
		List<PostRecord> postRecords = new ArrayList<PostRecord>();
		for (int i = 0; i < stream.size(); i++)
			postRecords.add(cleanPost(stream.get(i)));
		return postRecords;
	}

	public PostRecord cleanPost(Element post) {
		int timestamp = Integer.parseInt(post.attr("data-timestamp"));
		String[] titleArray = post.select("a[data-searchable]").attr("href").split("-")[0].split("/");
		int topicID = Integer.parseInt(titleArray[titleArray.length - 1]);
		String userID = post.select(".ipsStreamItem_status > a:first-child").text();
		String content = post.select("div[data-ipstruncate]").text();
		if (content.length() > 20)
			content = content.substring(0, 20) + "...";
		PostRecord pr = new PostRecord(userID, timestamp, topicID, content);
		return pr;
	}

}
