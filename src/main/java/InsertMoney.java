import java.io.File;

public class InsertMoney {
	
	public static void insert10yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 10;
		update(totalInsertedMoney);
		
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf10yen = moneyStock.numberOf10yen + 1;
		MoneyStockLogic.update(moneyStock);
	}
	
	public static void insert50yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 50;
		update(totalInsertedMoney);
		
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf50yen = moneyStock.numberOf50yen + 1;
		MoneyStockLogic.update(moneyStock);
	}
	
	public static void insert100yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 100;
		update(totalInsertedMoney);
		
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf10yen = moneyStock.numberOf100yen + 1;
		MoneyStockLogic.update(moneyStock);
	}
	
	public static void insert500yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 500;
		update(totalInsertedMoney);
		
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf500yen = moneyStock.numberOf500yen + 1;
		MoneyStockLogic.update(moneyStock);
	}
	
	public static void insert1000yen() throws Exception {
		int alreadyInsertedMoney = find();
		int totalInsertedMoney = alreadyInsertedMoney + 1000;
		update(totalInsertedMoney);
		
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf1000yen = moneyStock.numberOf1000yen + 1;
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
