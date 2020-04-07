public class Main {

	public static void main(String[] args) throws Exception {
		new ReaderWritter(System.in).programStart();
	}
	

	//ADD WHEN NEEDED
	/*public List<String> nameComparer() throws IOException {
		List<String> file = Files.readAllLines(new File("D:/eclipse/eclipse-workspace/jsoup selector/temp/toCompare.txt").toPath(), Charset.forName("UTF-8"));
		
		file.add("相异内容数如下");
		while (!file.get(0).equals("相异内容数如下")) {
			String targetName = file.get(0);
			int occurences = Collections.frequency(file, targetName);
			file.removeAll(Collections.singletonList(targetName));
			if ((occurences == 1))
				file.add(targetName);
		}
		
		return file;
	}*/
	


}
