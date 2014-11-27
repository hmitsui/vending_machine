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
		moneyStock.numberOf10yen = Integer.parseInt(moneyStockMap.get("10"));
		moneyStock.numberOf50yen = Integer.parseInt(moneyStockMap.get("50"));
		moneyStock.numberOf100yen = Integer.parseInt(moneyStockMap.get("100"));
		moneyStock.numberOf500yen = Integer.parseInt(moneyStockMap.get("500"));
		moneyStock.numberOf1000yen = Integer.parseInt(moneyStockMap.get("1000"));
		
		return moneyStock;
	}
	
	public static void update(MoneyStock moneyStock) throws Exception {
		if (moneyStock == null) {
			return;
		}
		
		File vendingMachineMoneyFile = new File(FilePath.MONEY_STOCK_FILE);
		
		String header = "10,50,100,500,1000";
		String data = moneyStock.numberOf10yen + "," + moneyStock.numberOf50yen + "," + moneyStock.numberOf100yen + ","
				+ moneyStock.numberOf500yen + "," + moneyStock.numberOf1000yen;
		
		if (!FileUtil.canWriteFile(vendingMachineMoneyFile)) {
			throw new Exception("ファイルが存在しない、または書き込めません。");
		}
		FileUtil.writeFile(vendingMachineMoneyFile, header, data);
	}
	
	public static boolean canGiveOtsuri(int otsuriKingaku) throws Exception {
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		
		int favoredNumberOf1000yen = otsuriKingaku / 1000;
		int otsuriKinkagakuExcept1000yen;
		if (favoredNumberOf1000yen == 0) {
			otsuriKinkagakuExcept1000yen = otsuriKingaku;
		} else {
			int otsuriNumberOf1000yen;
			if (favoredNumberOf1000yen <= moneyStock.numberOf1000yen) {
				otsuriNumberOf1000yen = favoredNumberOf1000yen;
			} else {
				otsuriNumberOf1000yen = moneyStock.numberOf1000yen;
			}
			otsuriKinkagakuExcept1000yen = otsuriKingaku - otsuriNumberOf1000yen * 1000;
		}
		
		int favoredNumberOf500yen = otsuriKinkagakuExcept1000yen / 500;
		int otsuriKingakuExcept500yen;
		if (favoredNumberOf500yen == 0) {
			otsuriKingakuExcept500yen = otsuriKinkagakuExcept1000yen;
		} else {
			int otsuriNumberOf500yen;
			if (favoredNumberOf500yen <= moneyStock.numberOf500yen) {
				otsuriNumberOf500yen = favoredNumberOf500yen;
			} else {
				otsuriNumberOf500yen = moneyStock.numberOf500yen;
			}
			otsuriKingakuExcept500yen = otsuriKinkagakuExcept1000yen - otsuriNumberOf500yen * 500;
		}
		
		int favoredNumberOf100yen = otsuriKingakuExcept500yen / 100;
		int otsuriKingakuExcept100yen;
		if (favoredNumberOf100yen == 0) {
			otsuriKingakuExcept100yen = otsuriKingakuExcept500yen;
		} else {
			int otsuriNumberOf100yen;
			if (favoredNumberOf100yen <= moneyStock.numberOf100yen) {
				otsuriNumberOf100yen = favoredNumberOf100yen;
			} else {
				otsuriNumberOf100yen = moneyStock.numberOf100yen;
			}
			otsuriKingakuExcept100yen = otsuriKingakuExcept500yen - otsuriNumberOf100yen * 100;
		}
		
		int favoredNumberOf50yen = otsuriKingakuExcept100yen / 50;
		int otsuriKingakuExcept50yen;
		if (favoredNumberOf50yen == 0) {
			otsuriKingakuExcept50yen = otsuriKingakuExcept100yen;
		} else {
			int otsuriNumberOf50yen;
			if (favoredNumberOf50yen <= moneyStock.numberOf50yen) {
				otsuriNumberOf50yen = favoredNumberOf50yen;
			} else {
				otsuriNumberOf50yen = moneyStock.numberOf50yen;
			}
			otsuriKingakuExcept50yen = otsuriKingakuExcept100yen - otsuriNumberOf50yen * 50;
		}
		
		int favoredNumberOf10yen = otsuriKingakuExcept50yen / 10;
		if (favoredNumberOf10yen <= moneyStock.numberOf10yen) {
			return true;
		} else {
			return false;
		}
	}
	
	public static OtsuriAndGoodsDto computeNumberOfOtsuri(int otsuriKingaku) throws Exception {
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		OtsuriAndGoodsDto otsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		int favoredNumberOf1000yen = otsuriKingaku / 1000;
		int otsuriKinkagakuExcept1000yen;
		if (favoredNumberOf1000yen == 0) {
			otsuriKinkagakuExcept1000yen = otsuriKingaku;
			otsuriAndGoodsDto.numberOf1000yen = 0;
		} else if (favoredNumberOf1000yen <= moneyStock.numberOf1000yen) {
			otsuriAndGoodsDto.numberOf1000yen = favoredNumberOf1000yen;
			otsuriKinkagakuExcept1000yen = otsuriKingaku - otsuriAndGoodsDto.numberOf1000yen * 1000;
		} else {
			otsuriAndGoodsDto.numberOf1000yen = moneyStock.numberOf1000yen;
			otsuriKinkagakuExcept1000yen = otsuriKingaku - otsuriAndGoodsDto.numberOf1000yen * 1000;
		}
		
		int favoredNumberOf500yen = otsuriKinkagakuExcept1000yen / 500;
		int otsuriKinkagakuExcept500yen;
		if (favoredNumberOf500yen == 0) {
			otsuriKinkagakuExcept500yen = otsuriKinkagakuExcept1000yen;
			otsuriAndGoodsDto.numberOf500yen = favoredNumberOf500yen;
		} else if (favoredNumberOf500yen <= moneyStock.numberOf500yen) {
			otsuriAndGoodsDto.numberOf500yen = favoredNumberOf500yen;
			otsuriKinkagakuExcept500yen = otsuriKinkagakuExcept1000yen - otsuriAndGoodsDto.numberOf500yen * 500;
		} else {
			otsuriAndGoodsDto.numberOf500yen = moneyStock.numberOf500yen;
			otsuriKinkagakuExcept500yen = otsuriKinkagakuExcept1000yen - otsuriAndGoodsDto.numberOf500yen * 500;
		}
		
		int favoredNumberOf100yen = otsuriKinkagakuExcept500yen / 100;
		int otsuriKinkagakuExcept100yen;
		if (favoredNumberOf100yen == 0) {
			otsuriKinkagakuExcept100yen = otsuriKinkagakuExcept500yen;
			otsuriAndGoodsDto.numberOf100yen = favoredNumberOf100yen;
		} else if (favoredNumberOf100yen <= moneyStock.numberOf100yen) {
			otsuriAndGoodsDto.numberOf100yen = otsuriKinkagakuExcept500yen / 100;
			otsuriKinkagakuExcept100yen = otsuriKinkagakuExcept500yen - otsuriAndGoodsDto.numberOf100yen * 100;
		} else {
			otsuriAndGoodsDto.numberOf100yen = moneyStock.numberOf100yen;
			otsuriKinkagakuExcept100yen = otsuriKinkagakuExcept500yen - otsuriAndGoodsDto.numberOf100yen * 100;
		}
		
		int favoredNumberOf50yen = otsuriKinkagakuExcept100yen / 50;
		int otsuriKinkagakuExcept50yen;
		if (favoredNumberOf50yen == 0) {
			otsuriKinkagakuExcept50yen = otsuriKinkagakuExcept100yen;
			otsuriAndGoodsDto.numberOf50yen = favoredNumberOf50yen;
		} else if (favoredNumberOf50yen <= moneyStock.numberOf50yen) {
			otsuriAndGoodsDto.numberOf50yen = otsuriKinkagakuExcept100yen / 50;
			otsuriKinkagakuExcept50yen = otsuriKinkagakuExcept100yen - otsuriAndGoodsDto.numberOf50yen * 50;
		} else {
			otsuriAndGoodsDto.numberOf50yen = moneyStock.numberOf50yen;
			otsuriKinkagakuExcept50yen = otsuriKinkagakuExcept100yen - otsuriAndGoodsDto.numberOf50yen * 50;
		}
		
		otsuriAndGoodsDto.numberOf10yen = otsuriKinkagakuExcept50yen / 10;
		
		return otsuriAndGoodsDto;
	}
}