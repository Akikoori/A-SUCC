package coreASUCC;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ReaderWritter {

	private Scanner inputScanner;
	private Elements stream;
	private List<PostRecord> postRecords;

	public ReaderWritter(InputStream in) {
		this.inputScanner = new Scanner(in);
	}

	public void programStart() throws Exception {
		while (true) {

			List<String> possibleOperations = Arrays.asList("0", "01", "02", "1", "11", "2", "3", "9");

			System.out.println();
			System.out.println("请选择模式：");
			System.out.println("0:获取指定动态流");
			System.out.println("01:保存动态流");
			System.out.println("02:读取当前保存的动态流");
			System.out.println("1:整理回帖数据");
			System.out.println("11:保存回帖数据");
			System.out.println("2:检测潜在屠版行为");
			System.out.println("3:整理回复用户分布数据");
			System.out.println("9:终止程序");

			String choice;
			int trycount = 0;
			do {
				choice = inputScanner.nextLine();
				if (!possibleOperations.contains(choice))
					System.out.println("请输入上述指令之一");
				trycount++;
			} while (!possibleOperations.contains(choice) && trycount <= 5);

			switch (choice) {
			// 完整动态流相关
			case "0":
				retriveStream();
				break;
			case "01":
				if (isStreamEmpty())
					break;
				File fullStream = new File("./parsed/fullStream.txt");
				PrintWriter writer = new PrintWriter(new FileWriter(fullStream, false));
				writer.print(stream);
				writer.close();
				System.out.println("已将完整动态流保存至fullStream.txt");
				break;
			case "02":
				stream = loadFile();
				if (!isStreamEmpty())
					System.out.println("已自fullStream.txt读取动态流");
				break;

			// 回帖相关
			case "1":
				if (isStreamEmpty())
					break;
				cleanStream();
				System.out.println("动态流整理完毕");
				break;
			case "11":
				if (isStreamEmpty())
					break;
				if (postRecords == null)
					cleanStream();
				List<String> postInfo = PostInfoParser.stringConvert(postRecords);
				postInfo.add(0, "用户名" + "	" + "主题ID" + "	" + "时间戳" + "	" + "回帖内容");
				File cleanedPosts = new File("./parsed/cleanedPosts.txt");
				writeSortedData(postInfo, cleanedPosts);
				break;
			
			//违规检测
			case "2":
				if (isStreamEmpty())
					break;
				if (postRecords == null)
					cleanStream();
				List<String> potentialCriminals = new BreachDetector(postRecords).checkExcessiveReply();
				if (potentialCriminals.isEmpty()) {
					System.out.println("没有人屠版！好耶！mole！");
					break;
				}
				potentialCriminals.add(0, "下述会员有屠版嫌疑：");
				File criminalList = new File("./parsed/criminalList.txt");
				writeSortedData(potentialCriminals, criminalList);
				break;

			// 发布者相关
			case "3":
				if (isStreamEmpty())
					break;
				List<String> userInfo = new UserInfoParser(stream).extractDistributionInfo();
				File parsedName = new File("./parsed/parsedName.txt");
				writeSortedData(userInfo, parsedName);
				break;

			// 测验指令用选项
			case "8":
				PostRecord pr = new PostInfoParser().cleanPost(stream.first());
				System.out.println(pr.getUserID());
				System.out.println(pr.getTopicID());
				System.out.println(pr.getTimeStamp());
				System.out.println(pr.getContent());
				break;

			// 结束程序
			case "9":
				System.out.println("*程序终止*");
				inputScanner.close();
				return;
			default:
				System.out.println("*模式不存在，程序终止*");
				inputScanner.close();
				return;
			}
		}

	}

	private void retriveStream() throws Exception {
		System.out.println("请输入动态流编号：");
		String index = inputScanner.nextLine();
		stream = new StreamLoader(index).fullLoad();
		return;
	}

	private Elements loadFile() throws IOException {
		File saved = new File("./parsed/fullStream.txt");
		Document doc = Jsoup.parse(saved, "UTF-8");
		return doc.select(".ipsStreamItem_contentBlock");
	}

	private boolean isStreamEmpty() {
		if (stream == null) {
			System.out.println("错误！未获取任何动态流！");
			return true;
		} else {
			return false;
		}
	}

	private void writeSortedData(List<String> sortedInfo, File destination) throws Exception {
		PrintWriter writer = new PrintWriter(new FileWriter(destination, false));
		for (int i = 0; i < sortedInfo.size(); i++)
			writer.println(sortedInfo.get(i));
		writer.close();
		System.out.println("已将数据输出至" + destination.getName());
	}

	public void cleanStream() {
		postRecords = new PostInfoParser().cleanStream(stream);
	}

}
