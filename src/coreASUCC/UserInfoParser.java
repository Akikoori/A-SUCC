package coreASUCC;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UserInfoParser {

	private Elements loadedStream;
	private List<UserRecord> userRecords = new ArrayList<UserRecord>();

	public UserInfoParser(Elements stream) {
		loadedStream = stream;
	}

	public List<String> extractDistributionInfo() throws Exception {
		sortElements();
		retrieveInfo();
		return stringConvert();
	}

	public List<String> stringConvert() throws Exception {
		List<String> infoStrings = new ArrayList<String>();
		int total = 0;
		for (int i = 0; i < userRecords.size(); i++)
			total = total + userRecords.get(i).getFrequency();
		infoStrings.add("共计" + total);
		for (int i = 0; i < userRecords.size(); i++) {
			UserRecord ur = userRecords.get(i);
			infoStrings.add(ur.getName() + "	" + ur.getFrequency() + "	" + ur.getRank());
		}
		return infoStrings;
	}

	public void sortElements() throws Exception {
		Elements userNameList = loadedStream.select(".ipsStreamItem_status > a:first-child");
		Element seperation = new Element("p").text("SEPERATION");
		userNameList.add(seperation);

		while (!userNameList.get(0).equals(seperation)) {
			Element target = userNameList.get(0);
			String userName = target.text();
			Elements byTarget = new Elements();
			for (int i = 0; i < userNameList.size(); i++) {
				if (userNameList.get(i).text().equals(userName))
					byTarget.add(userNameList.get(i));
			}
			int occurences = byTarget.size();
			userNameList.removeAll(byTarget);
			target.attr("frequency", Integer.toString(occurences));
			userNameList.add(target);
		}
		System.out.println("余项已去除");

		for (int i = 1; i < userNameList.size(); i++) {
			Element user = userNameList.get(i);
			UserRecord ur = new UserRecord(user.text(), Integer.parseInt(user.attr("frequency")),
					user.attr("data-ipshover-target"));
			this.userRecords.add(ur);
		}
		for (int i = 0; i < userRecords.size(); i++) {
			System.out.println(userRecords.get(i).getName() + " " + userRecords.get(i).getFrequency());
		}
		System.out.println("基本用户信息整理完毕");

	}

	public void retrieveInfo() throws Exception {
		for (int i = 0; i < userRecords.size(); i++) {
			UserRecord ur = userRecords.get(i);
			System.out.println("正在读取" + ur.getName());
			Document extraInfo = Jsoup.connect(ur.getHoverUrl()).get();
			ur.setRank(extraInfo.selectFirst(".ipsType_normal").text());
		}
	}

}
