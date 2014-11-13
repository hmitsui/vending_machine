import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

// TODO apache.commons.io.FileUtilsが使える
public class FileUtil {
	
	public static boolean canReadFile(File file) {
		if (!file.exists()) {
			return false;
		}
		if (!file.isFile()) {
			return false;
		}
		if (!file.canRead()) {
			return false;
		}
		return true;
	}
	
	public static boolean canWriteFile(File file) {
		if (!file.exists()) {
			return false;
		}
		if (!file.isFile()) {
			return false;
		}
		if (!file.canWrite()) {
			return false;
		}
		return true;
	}
	
	public static String readOneLineFile(File file) throws Exception {
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		String sentence = bufferedReader.readLine();
		
		bufferedReader.close();
		fileReader.close();
		
		return sentence;
	}
	
	public static List<String> readFile(File file) throws Exception {
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		List<String> sentenceList = new ArrayList<String>();
		String sentence = bufferedReader.readLine();
		while (sentence != null) {
			sentenceList.add(sentence);
			sentence = bufferedReader.readLine();
		}
		
		bufferedReader.close();
		fileReader.close();
		
		return sentenceList;
	}
	
	public static void writeFile(File file, String str) throws Exception {
		FileWriter fileWriter = new FileWriter(file);
		
		fileWriter.write(str);
		
		fileWriter.close();
	}
	
	public static void writeFile(File file, String str1, String str2) throws Exception {
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		bufferedWriter.write(str1);
		bufferedWriter.newLine();
		bufferedWriter.write(str2);
		
		bufferedWriter.close();
		fileWriter.close();
	}
}