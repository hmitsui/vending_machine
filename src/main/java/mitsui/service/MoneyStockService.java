package mitsui.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mitsui.entity.MoneyStockEntity;
import mitsui.path.FilePath;
import mitsui.util.FileUtil;

// TODO ファイルは1行にIdと値を1組与える構成に変える。
public class MoneyStockService {
	
	public static MoneyStockEntity findAll() throws Exception {
		
		File vendingMachineMoneyFile = new File(FilePath.MONEY_STOCK_FILE);
		
		if (!FileUtil.canReadFile(vendingMachineMoneyFile)) {
			throw new Exception("ファイルが存在しない、または読み込めません。");
		}
		
		String header = FileUtil.readFile(vendingMachineMoneyFile).get(0);
		String[] moneyKindArray = header.split(",");
		
		String data = FileUtil.readFile(vendingMachineMoneyFile).get(1);
		String[] moneyNumberArray = data.split(",");
		
		Map<String, String> moneyStockMap = new HashMap<String, String>();
		for (int i = 0; i < moneyKindArray.length; i++) {
			moneyStockMap.put(moneyKindArray[i], moneyNumberArray[i]);
		}
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = Integer.parseInt(moneyStockMap.get("10"));
		moneyStock.numberOf50Yen = Integer.parseInt(moneyStockMap.get("50"));
		moneyStock.numberOf100Yen = Integer.parseInt(moneyStockMap.get("100"));
		moneyStock.numberOf500Yen = Integer.parseInt(moneyStockMap.get("500"));
		moneyStock.numberOf1000Yen = Integer.parseInt(moneyStockMap.get("1000"));
		
		return moneyStock;
	}
	
	public static void update(MoneyStockEntity moneyStock) throws Exception {
		if (moneyStock == null) {
			return;
		}
		
		File vendingMachineMoneyFile = new File(FilePath.MONEY_STOCK_FILE);
		
		String header = "10,50,100,500,1000";
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append(moneyStock.numberOf10Yen).append(",").append(moneyStock.numberOf50Yen).append(",")
				.append(moneyStock.numberOf100Yen).append(",").append(moneyStock.numberOf500Yen).append(",")
				.append(moneyStock.numberOf1000Yen);
		
		if (!FileUtil.canWriteFile(vendingMachineMoneyFile)) {
			throw new Exception("ファイルが存在しない、または書き込めません。");
		}
		FileUtil.writeFile(vendingMachineMoneyFile, header, dataBuilder.toString());
	}
}