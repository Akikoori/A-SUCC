import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.jsoup.select.Elements;

public class ReaderWritter {

	private Scanner inputScanner;
	
	public ReaderWritter(InputStream in) {
		this.inputScanner = new Scanner(in);
	}
	
	public void programStart() throws Exception {
		int choice;
		System.out.println("请选择模式：");
		System.out.println("0:整理特定动态流的数据");
		//System.out.println("1:查找toCompare.txt中的误差");
		do {
			choice = Integer.parseInt(inputScanner.nextLine());
			if (!(choice == 0) && !(choice == 1))
				System.out.println("请输入0或1");
		} while (!(choice == 0) && !(choice == 1));
		writeData(choice);
	}
	
	public void writeData(int choice) throws Exception {
		List<String> sortedInfo;
		File resultDirection;
		if (choice == 0) {
			System.out.println("请输入动态流编号：");
			String index = inputScanner.nextLine();
			inputScanner.close();
			Elements stream = new StreamLoader(index).fullLoad();
			sortedInfo = new UserInfoParser(stream).extractDistributionInfo();
			resultDirection = new File("D:/eclipse/eclipse-workspace/jsoup selector/temp/parsedName.txt");
		//} else {
			//names = nameComparer();
			//resultDirection = new File("D:/eclipse/eclipse-workspace/jsoup selector/temp/comparedAnomaly.txt");
		//}
			if(resultDirection.exists()) 
				resultDirection.delete();
			PrintWriter writer = new PrintWriter(resultDirection);
			for(int i = 0; i<sortedInfo.size(); i++)
				writer.println(sortedInfo.get(i));
			writer.close();
		}
		
		System.out.print("printed");
	}
	
}
