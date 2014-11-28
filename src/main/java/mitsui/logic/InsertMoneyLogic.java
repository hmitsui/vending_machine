package mitsui.logic;

import mitsui.entity.MoneyStockEntity;
import mitsui.service.InsertedMoneyService;
import mitsui.service.MoneyStockService;

public class InsertMoneyLogic {
	
	public static void insert10Yen() throws Exception {
		int alreadyInsertedMoney = InsertedMoneyService.find();
		int totalInsertedMoney = alreadyInsertedMoney + 10;
		InsertedMoneyService.update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockService.findAll();
		moneyStock.numberOf10Yen = moneyStock.numberOf10Yen + 1;
		MoneyStockService.update(moneyStock);
	}
	
	public static void insert50Yen() throws Exception {
		int alreadyInsertedMoney = InsertedMoneyService.find();
		int totalInsertedMoney = alreadyInsertedMoney + 50;
		InsertedMoneyService.update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockService.findAll();
		moneyStock.numberOf50Yen = moneyStock.numberOf50Yen + 1;
		MoneyStockService.update(moneyStock);
	}
	
	public static void insert100Yen() throws Exception {
		int alreadyInsertedMoney = InsertedMoneyService.find();
		int totalInsertedMoney = alreadyInsertedMoney + 100;
		InsertedMoneyService.update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockService.findAll();
		moneyStock.numberOf100Yen = moneyStock.numberOf100Yen + 1;
		MoneyStockService.update(moneyStock);
	}
	
	public static void insert500Yen() throws Exception {
		int alreadyInsertedMoney = InsertedMoneyService.find();
		int totalInsertedMoney = alreadyInsertedMoney + 500;
		InsertedMoneyService.update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockService.findAll();
		moneyStock.numberOf500Yen = moneyStock.numberOf500Yen + 1;
		MoneyStockService.update(moneyStock);
	}
	
	public static void insert1000Yen() throws Exception {
		int alreadyInsertedMoney = InsertedMoneyService.find();
		int totalInsertedMoney = alreadyInsertedMoney + 1000;
		InsertedMoneyService.update(totalInsertedMoney);
		
		MoneyStockEntity moneyStock = MoneyStockService.findAll();
		moneyStock.numberOf1000Yen = moneyStock.numberOf1000Yen + 1;
		MoneyStockService.update(moneyStock);
	}
}
