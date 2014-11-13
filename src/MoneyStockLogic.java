import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
		if (favoredNumberOf1000yen != 0) {
			int otsuriNumberOf1000yen;
			if (favoredNumberOf1000yen <= moneyStock.numberOf1000yen) {
				otsuriNumberOf1000yen = favoredNumberOf1000yen;
			} else {
				otsuriNumberOf1000yen = moneyStock.numberOf1000yen;
			}
			otsuriKingaku = otsuriKingaku - otsuriNumberOf1000yen * 1000;
		}
		
		int favoredNumberOf500yen = otsuriKingaku / 500;
		if (favoredNumberOf500yen != 0) {
			int otsuriNumberOf500yen;
			if (favoredNumberOf500yen <= moneyStock.numberOf500yen) {
				otsuriNumberOf500yen = favoredNumberOf500yen;
			} else {
				otsuriNumberOf500yen = moneyStock.numberOf500yen;
			}
			otsuriKingaku = otsuriKingaku - otsuriNumberOf500yen * 500;
		}
		
		int favoredNumberOf100yen = otsuriKingaku / 100;
		if (favoredNumberOf100yen != 0) {
			int otsuriNumberOf100yen;
			if (favoredNumberOf100yen <= moneyStock.numberOf100yen) {
				otsuriNumberOf100yen = favoredNumberOf100yen;
			} else {
				otsuriNumberOf100yen = moneyStock.numberOf100yen;
			}
			otsuriKingaku = otsuriKingaku - otsuriNumberOf100yen * 100;
		}
		
		int favoredNumberOf50yen = otsuriKingaku / 50;
		if (favoredNumberOf50yen != 0) {
			int otsuriNumberOf50yen;
			if (favoredNumberOf50yen <= moneyStock.numberOf50yen) {
				otsuriNumberOf50yen = favoredNumberOf50yen;
			} else {
				otsuriNumberOf50yen = moneyStock.numberOf50yen;
			}
			otsuriKingaku = otsuriKingaku - otsuriNumberOf50yen * 50;
		}
		
		int favoredNumberOf10yen = otsuriKingaku / 10;
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
		if (favoredNumberOf1000yen == 0) {
			otsuriAndGoodsDto.numberOf1000yen = 0;
		} else if (favoredNumberOf1000yen <= moneyStock.numberOf1000yen) {
			otsuriAndGoodsDto.numberOf1000yen = favoredNumberOf1000yen;
			otsuriKingaku = otsuriKingaku - otsuriAndGoodsDto.numberOf1000yen * 1000;
		} else {
			otsuriAndGoodsDto.numberOf1000yen = moneyStock.numberOf1000yen;
			otsuriKingaku = otsuriKingaku - otsuriAndGoodsDto.numberOf1000yen * 1000;
		}
		
		int favoredNumberOf500yen = otsuriKingaku / 500;
		if (favoredNumberOf500yen == 0) {
			otsuriAndGoodsDto.numberOf500yen = favoredNumberOf500yen;
		} else if (favoredNumberOf500yen <= moneyStock.numberOf500yen) {
			otsuriAndGoodsDto.numberOf500yen = favoredNumberOf500yen;
			otsuriKingaku = otsuriKingaku - otsuriAndGoodsDto.numberOf500yen * 500;
		} else {
			otsuriAndGoodsDto.numberOf500yen = moneyStock.numberOf500yen;
			otsuriKingaku = otsuriKingaku - otsuriAndGoodsDto.numberOf500yen * 500;
		}
		
		int favoredNumberOf100yen = otsuriKingaku / 100;
		if (favoredNumberOf100yen == 0) {
			otsuriAndGoodsDto.numberOf100yen = favoredNumberOf100yen;
		} else if (otsuriKingaku / 100 <= moneyStock.numberOf100yen) {
			otsuriAndGoodsDto.numberOf100yen = otsuriKingaku / 100;
			otsuriKingaku = otsuriKingaku - otsuriAndGoodsDto.numberOf100yen * 100;
		} else {
			otsuriAndGoodsDto.numberOf100yen = moneyStock.numberOf100yen;
			otsuriKingaku = otsuriKingaku - otsuriAndGoodsDto.numberOf100yen * 100;
		}
		
		int favoredNumberOf50yen = otsuriKingaku / 50;
		if (favoredNumberOf50yen == 0) {
			otsuriAndGoodsDto.numberOf50yen = favoredNumberOf50yen;
		} else if (otsuriKingaku / 50 <= moneyStock.numberOf50yen) {
			otsuriAndGoodsDto.numberOf50yen = otsuriKingaku / 50;
			otsuriKingaku = otsuriKingaku - otsuriAndGoodsDto.numberOf50yen * 50;
		} else {
			otsuriAndGoodsDto.numberOf50yen = moneyStock.numberOf50yen;
			otsuriKingaku = otsuriKingaku - otsuriAndGoodsDto.numberOf50yen * 50;
		}
		
		otsuriAndGoodsDto.numberOf10yen = otsuriKingaku / 10;
		
		return otsuriAndGoodsDto;
	}
}