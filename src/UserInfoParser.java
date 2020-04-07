import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UserInfoParser {
	
	private Elements loadedStream;
	private List<UserField> userFields = new ArrayList<UserField>();
	
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
		for (int i = 0; i < userFields.size(); i++) 
			total = total + userFields.get(i).getFrequency();
		infoStrings.add("共计" + total);
		for (int i = 0; i < userFields.size(); i++) {
			UserField uf = userFields.get(i);
			infoStrings.add(uf.getName() + "	" + uf.getFrequency() + "	" + uf.getRank() );
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
			UserField uf = new UserField(user.text(), Integer.parseInt(user.attr("frequency")), user.attr("data-ipshover-target"));
			this.userFields.add(uf);
		}
		for (int i = 0; i < userFields.size(); i++) {
			System.out.println(userFields.get(i).getName() + " " + userFields.get(i).getFrequency());
		}
		System.out.println("基本用户信息整理完毕");
		
	}
	
	public void retrieveInfo() throws Exception {
		for (int i = 0; i < userFields.size(); i++) {
			UserField uf = userFields.get(i);
			System.out.println("正在读取" + uf.getName());
			Document extraInfo = Jsoup.connect(uf.getHoverUrl()).get();
			uf.setRank(extraInfo.selectFirst(".ipsType_normal").text());
		}
	}

}
