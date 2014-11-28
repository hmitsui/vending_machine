package mitsui;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;

import org.junit.Test;

// 動作確認のためのテスト
public class InsertMoneyTest {
	
	@Test
	public void 十円を投入する() throws Exception {
		InsertMoney.update(100);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert10yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 2;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 110;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 五十円を投入する() throws Exception {
		InsertMoney.update(100);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert50yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 4;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 150;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 百円を投入する() throws Exception {
		InsertMoney.update(100);
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert100yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 6;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 200;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 五百円を投入する() throws Exception {
		InsertMoney.update(100);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert500yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 8;
		expectedMoneyStock.numberOf1000yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 600;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 千円を投入する() throws Exception {
		InsertMoney.update(100);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert1000yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 10;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 1100;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 十円を2つ投入する() throws Exception {
		InsertMoney.update(100);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert10yen();
		InsertMoney.insert10yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 3;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 120;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 十円と五十円を投入する() throws Exception {
		InsertMoney.update(100);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert10yen();
		InsertMoney.insert50yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 2;
		expectedMoneyStock.numberOf50yen = 4;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 160;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	public static void eraseText(String filePath) throws Exception {
		File file = new File(filePath);
		
		if (!FileUtil.canWriteFile(file)) {
			throw new Exception("ファイルが存在しない、または書き込めません。");
		}
		FileUtil.writeFile(file, "");
	}
	
	public static void assertMoneyStockEquals(MoneyStock expectedMoneyStock, MoneyStock actualMoneyStock) {
		assertThat(actualMoneyStock.numberOf10yen, is(expectedMoneyStock.numberOf10yen));
		assertThat(actualMoneyStock.numberOf50yen, is(expectedMoneyStock.numberOf50yen));
		assertThat(actualMoneyStock.numberOf100yen, is(expectedMoneyStock.numberOf100yen));
		assertThat(actualMoneyStock.numberOf500yen, is(expectedMoneyStock.numberOf500yen));
		assertThat(actualMoneyStock.numberOf1000yen, is(expectedMoneyStock.numberOf1000yen));
	}
}