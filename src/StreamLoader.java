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
		do {
			if (contents.isEmpty())
				break;
			timeStamp = contents.last().attr("data-timestamp");
			System.out.println("��������ʱ��� " + timeStamp + "���ֵĶ�̬��");
			contents = load(timeStamp);
			fullContents.addAll(contents);
		} while (streamForm.select("[data-role=loadMoreContainer]").text().equals("���ظ��ද̬"));
		System.out.print("Ŀ�궯̬���������");
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
