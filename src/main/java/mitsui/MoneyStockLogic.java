package mitsui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

// TODO ファイルは1行にIdと値を1組与える構成に変える。
public class MoneyStockLogic {
	
	public static MoneyStock findAll() throws Exception {
		
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
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10Yen = Integer.parseInt(moneyStockMap.get("10"));
		moneyStock.numberOf50Yen = Integer.parseInt(moneyStockMap.get("50"));
		moneyStock.numberOf100Yen = Integer.parseInt(moneyStockMap.get("100"));
		moneyStock.numberOf500Yen = Integer.parseInt(moneyStockMap.get("500"));
		moneyStock.numberOf1000Yen = Integer.parseInt(moneyStockMap.get("1000"));
		
		return moneyStock;
	}
	
	public static void update(MoneyStock moneyStock) throws Exception {
		if (moneyStock == null) {
			return;
		}
		
		File vendingMachineMoneyFile = new File(FilePath.MONEY_STOCK_FILE);
		
		String header = "10,50,100,500,1000";
		String data = moneyStock.numberOf10Yen + "," + moneyStock.numberOf50Yen + "," + moneyStock.numberOf100Yen + ","
				+ moneyStock.numberOf500Yen + "," + moneyStock.numberOf1000Yen;
		
		if (!FileUtil.canWriteFile(vendingMachineMoneyFile)) {
			throw new Exception("ファイルが存在しない、または書き込めません。");
		}
		FileUtil.writeFile(vendingMachineMoneyFile, header, data);
	}
	
	public static boolean canGiveOtsuri(int otsuriKingaku) throws Exception {
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		
		int favoredNumberOf1000Yen = otsuriKingaku / 1000;
		int otsuriKinkagakuExcept1000Yen;
		if (favoredNumberOf1000Yen == 0) {
			otsuriKinkagakuExcept1000Yen = otsuriKingaku;
		} else {
			int otsuriNumberOf1000Yen;
			if (favoredNumberOf1000Yen <= moneyStock.numberOf1000Yen) {
				otsuriNumberOf1000Yen = favoredNumberOf1000Yen;
			} else {
				otsuriNumberOf1000Yen = moneyStock.numberOf1000Yen;
			}
			otsuriKinkagakuExcept1000Yen = otsuriKingaku - otsuriNumberOf1000Yen * 1000;
		}
		
		int favoredNumberOf500Yen = otsuriKinkagakuExcept1000Yen / 500;
		int otsuriKingakuExcept500Yen;
		if (favoredNumberOf500Yen == 0) {
			otsuriKingakuExcept500Yen = otsuriKinkagakuExcept1000Yen;
		} else {
			int otsuriNumberOf500Yen;
			if (favoredNumberOf500Yen <= moneyStock.numberOf500Yen) {
				otsuriNumberOf500Yen = favoredNumberOf500Yen;
			} else {
				otsuriNumberOf500Yen = moneyStock.numberOf500Yen;
			}
			otsuriKingakuExcept500Yen = otsuriKinkagakuExcept1000Yen - otsuriNumberOf500Yen * 500;
		}
		
		int favoredNumberOf100Yen = otsuriKingakuExcept500Yen / 100;
		int otsuriKingakuExcept100Yen;
		if (favoredNumberOf100Yen == 0) {
			otsuriKingakuExcept100Yen = otsuriKingakuExcept500Yen;
		} else {
			int otsuriNumberOf100Yen;
			if (favoredNumberOf100Yen <= moneyStock.numberOf100Yen) {
				otsuriNumberOf100Yen = favoredNumberOf100Yen;
			} else {
				otsuriNumberOf100Yen = moneyStock.numberOf100Yen;
			}
			otsuriKingakuExcept100Yen = otsuriKingakuExcept500Yen - otsuriNumberOf100Yen * 100;
		}
		
		int favoredNumberOf50Yen = otsuriKingakuExcept100Yen / 50;
		int otsuriKingakuExcept50Yen;
		if (favoredNumberOf50Yen == 0) {
			otsuriKingakuExcept50Yen = otsuriKingakuExcept100Yen;
		} else {
			int otsuriNumberOf50Yen;
			if (favoredNumberOf50Yen <= moneyStock.numberOf50Yen) {
				otsuriNumberOf50Yen = favoredNumberOf50Yen;
			} else {
				otsuriNumberOf50Yen = moneyStock.numberOf50Yen;
			}
			otsuriKingakuExcept50Yen = otsuriKingakuExcept100Yen - otsuriNumberOf50Yen * 50;
		}
		
		int favoredNumberOf10Yen = otsuriKingakuExcept50Yen / 10;
		if (favoredNumberOf10Yen <= moneyStock.numberOf10Yen) {
			return true;
		} else {
			return false;
		}
	}
	
	public static OtsuriAndGoodsDto computeNumberOfOtsuri(int otsuriKingaku) throws Exception {
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		OtsuriAndGoodsDto otsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		int favoredNumberOf1000Yen = otsuriKingaku / 1000;
		int otsuriKinkagakuExcept1000Yen;
		if (favoredNumberOf1000Yen == 0) {
			otsuriKinkagakuExcept1000Yen = otsuriKingaku;
			otsuriAndGoodsDto.numberOf1000Yen = 0;
		} else if (favoredNumberOf1000Yen <= moneyStock.numberOf1000Yen) {
			otsuriAndGoodsDto.numberOf1000Yen = favoredNumberOf1000Yen;
			otsuriKinkagakuExcept1000Yen = otsuriKingaku - otsuriAndGoodsDto.numberOf1000Yen * 1000;
		} else {
			otsuriAndGoodsDto.numberOf1000Yen = moneyStock.numberOf1000Yen;
			otsuriKinkagakuExcept1000Yen = otsuriKingaku - otsuriAndGoodsDto.numberOf1000Yen * 1000;
		}
		
		int favoredNumberOf500Yen = otsuriKinkagakuExcept1000Yen / 500;
		int otsuriKinkagakuExcept500Yen;
		if (favoredNumberOf500Yen == 0) {
			otsuriKinkagakuExcept500Yen = otsuriKinkagakuExcept1000Yen;
			otsuriAndGoodsDto.numberOf500Yen = favoredNumberOf500Yen;
		} else if (favoredNumberOf500Yen <= moneyStock.numberOf500Yen) {
			otsuriAndGoodsDto.numberOf500Yen = favoredNumberOf500Yen;
			otsuriKinkagakuExcept500Yen = otsuriKinkagakuExcept1000Yen - otsuriAndGoodsDto.numberOf500Yen * 500;
		} else {
			otsuriAndGoodsDto.numberOf500Yen = moneyStock.numberOf500Yen;
			otsuriKinkagakuExcept500Yen = otsuriKinkagakuExcept1000Yen - otsuriAndGoodsDto.numberOf500Yen * 500;
		}
		
		int favoredNumberOf100Yen = otsuriKinkagakuExcept500Yen / 100;
		int otsuriKinkagakuExcept100Yen;
		if (favoredNumberOf100Yen == 0) {
			otsuriKinkagakuExcept100Yen = otsuriKinkagakuExcept500Yen;
			otsuriAndGoodsDto.numberOf100Yen = favoredNumberOf100Yen;
		} else if (favoredNumberOf100Yen <= moneyStock.numberOf100Yen) {
			otsuriAndGoodsDto.numberOf100Yen = otsuriKinkagakuExcept500Yen / 100;
			otsuriKinkagakuExcept100Yen = otsuriKinkagakuExcept500Yen - otsuriAndGoodsDto.numberOf100Yen * 100;
		} else {
			otsuriAndGoodsDto.numberOf100Yen = moneyStock.numberOf100Yen;
			otsuriKinkagakuExcept100Yen = otsuriKinkagakuExcept500Yen - otsuriAndGoodsDto.numberOf100Yen * 100;
		}
		
		int favoredNumberOf50Yen = otsuriKinkagakuExcept100Yen / 50;
		int otsuriKinkagakuExcept50Yen;
		if (favoredNumberOf50Yen == 0) {
			otsuriKinkagakuExcept50Yen = otsuriKinkagakuExcept100Yen;
			otsuriAndGoodsDto.numberOf50Yen = favoredNumberOf50Yen;
		} else if (favoredNumberOf50Yen <= moneyStock.numberOf50Yen) {
			otsuriAndGoodsDto.numberOf50Yen = otsuriKinkagakuExcept100Yen / 50;
			otsuriKinkagakuExcept50Yen = otsuriKinkagakuExcept100Yen - otsuriAndGoodsDto.numberOf50Yen * 50;
		} else {
			otsuriAndGoodsDto.numberOf50Yen = moneyStock.numberOf50Yen;
			otsuriKinkagakuExcept50Yen = otsuriKinkagakuExcept100Yen - otsuriAndGoodsDto.numberOf50Yen * 50;
		}
		
		otsuriAndGoodsDto.numberOf10Yen = otsuriKinkagakuExcept50Yen / 10;
		
		return otsuriAndGoodsDto;
	}
}