package coreASUCC;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StreamLoader {

	private String url;
	private String csrfKey;

	public StreamLoader(String streamIndex) {
		url = "https://sstm.moe/discover/" + streamIndex + "/";
	}

	public Elements fullLoad() throws Exception {
		Document ssc = Jsoup.connect(url).get();
		csrfKey = ssc.selectFirst("[name=csrfKey]").attr("value");
		Element streamForm = ssc.selectFirst(".ipsBox_alt");
		Elements contents = parseContent(streamForm);
		Elements fullContents = contents;
		String timeStamp;
		int safeCount = 1;
		do {
			if (contents.isEmpty())
				break;
			if (safeCount == 40) {
				System.out.print("警告！读取到超过1000条动态流！已停止加载更多内容！");
				break;
			}
			timeStamp = contents.last().attr("data-timestamp");
			System.out.println("正在载入时间戳 " + timeStamp + "部分的动态流");
			contents = load(timeStamp);
			fullContents.addAll(contents);
			safeCount++;
		} while (!streamForm.select("[data-role=loadMoreContainer]").text().equals("  没有更多动态可显示 "));
		System.out.print("目标动态流载入完毕");
		return fullContents;
	}

	private Elements load(String uTimeBefore) throws Exception {
		Document doc = Jsoup.connect(url).data("csrfKey", csrfKey).data("before", uTimeBefore).post();
		Elements c = parseContent(doc);
		return c;
	}

	private Elements parseContent(Element element) {
		Elements c = element.select(".ipsStreamItem_contentBlock");
		return c;
	}

}
