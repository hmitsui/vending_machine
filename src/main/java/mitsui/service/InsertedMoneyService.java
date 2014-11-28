package mitsui.service;

import java.io.File;

import mitsui.path.FilePath;
import mitsui.util.FileUtil;

public class InsertedMoneyService {
	
	public static int find() throws Exception {
		File insertedMoneyFile = new File(FilePath.INSERTED_MONEY_FILE);
		
		if (!FileUtil.canReadFile(insertedMoneyFile)) {
			throw new Exception("ファイルが存在しない、または読み込めません。");
		}
		String alreadyInsertedMoney = FileUtil.readOneLineFile(insertedMoneyFile);
		
		return Integer.parseInt(alreadyInsertedMoney);
	}
	
	public static void update(int totalInsertedMoney) throws Exception {
		File insertedMoneyFile = new File(FilePath.INSERTED_MONEY_FILE);
		
		if (!FileUtil.canWriteFile(insertedMoneyFile)) {
			throw new Exception("ファイルが存在しない、または書き込めません。");
		}
		FileUtil.writeFile(insertedMoneyFile, Integer.toString(totalInsertedMoney));
	}
}
