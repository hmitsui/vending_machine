package mitsui.logic;

import java.io.File;

import mitsui.entity.MoneyStockEntity;
import mitsui.path.FilePath;
import mitsui.service.MoneyStockLogic;
import mitsui.util.FileUtil;

public class InsertMoney {
	
	public static void insert10Yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 10;
		update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf10Yen = moneyStock.numberOf10Yen + 1;
		MoneyStockLogic.update(moneyStock);
	}
	
	public static void insert50Yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 50;
		update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf50Yen = moneyStock.numberOf50Yen + 1;
		MoneyStockLogic.update(moneyStock);
	}
	
	public static void insert100Yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 100;
		update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf100Yen = moneyStock.numberOf100Yen + 1;
		MoneyStockLogic.update(moneyStock);
	}
	
	public static void insert500Yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 500;
		update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf500Yen = moneyStock.numberOf500Yen + 1;
		MoneyStockLogic.update(moneyStock);
	}
	
	public static void insert1000Yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 1000;
		update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf1000Yen = moneyStock.numberOf1000Yen + 1;
		MoneyStockLogic.update(moneyStock);
	}
	
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
