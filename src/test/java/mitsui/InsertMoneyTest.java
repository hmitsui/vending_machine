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
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert10Yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10Yen = 2;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
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
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert50Yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 4;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
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
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert100Yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 6;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
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
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert500Yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 8;
		expectedMoneyStock.numberOf1000Yen = 9;
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
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert1000Yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 10;
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
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert10Yen();
		InsertMoney.insert10Yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10Yen = 3;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
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
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.insert10Yen();
		InsertMoney.insert50Yen();
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10Yen = 2;
		expectedMoneyStock.numberOf50Yen = 4;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
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
		assertThat(actualMoneyStock.numberOf10Yen, is(expectedMoneyStock.numberOf10Yen));
		assertThat(actualMoneyStock.numberOf50Yen, is(expectedMoneyStock.numberOf50Yen));
		assertThat(actualMoneyStock.numberOf100Yen, is(expectedMoneyStock.numberOf100Yen));
		assertThat(actualMoneyStock.numberOf500Yen, is(expectedMoneyStock.numberOf500Yen));
		assertThat(actualMoneyStock.numberOf1000Yen, is(expectedMoneyStock.numberOf1000Yen));
	}
}