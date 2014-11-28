package mitsui;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;

import mitsui.entity.MoneyStockEntity;
import mitsui.logic.InsertMoneyLogic;
import mitsui.path.FilePath;
import mitsui.service.MoneyStockService;
import mitsui.util.FileUtil;

import org.junit.Test;

// 動作確認のためのテスト
public class InsertMoneyTest {
	
	@Test
	public void 十円を投入する() throws Exception {
		InsertMoneyLogic.update(100);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockService.update(moneyStock);
		
		InsertMoneyLogic.insert10Yen();
		
		MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
		MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
		expectedMoneyStock.numberOf10Yen = 2;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoneyLogic.find();
		int expectedInsertedMoney = 110;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 五十円を投入する() throws Exception {
		InsertMoneyLogic.update(100);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockService.update(moneyStock);
		
		InsertMoneyLogic.insert50Yen();
		
		MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
		MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 4;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoneyLogic.find();
		int expectedInsertedMoney = 150;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 百円を投入する() throws Exception {
		InsertMoneyLogic.update(100);
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockService.update(moneyStock);
		
		InsertMoneyLogic.insert100Yen();
		
		MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
		MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 6;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoneyLogic.find();
		int expectedInsertedMoney = 200;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 五百円を投入する() throws Exception {
		InsertMoneyLogic.update(100);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockService.update(moneyStock);
		
		InsertMoneyLogic.insert500Yen();
		
		MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
		MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 8;
		expectedMoneyStock.numberOf1000Yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoneyLogic.find();
		int expectedInsertedMoney = 600;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 千円を投入する() throws Exception {
		InsertMoneyLogic.update(100);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockService.update(moneyStock);
		
		InsertMoneyLogic.insert1000Yen();
		
		MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
		MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 10;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoneyLogic.find();
		int expectedInsertedMoney = 1100;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 十円を2つ投入する() throws Exception {
		InsertMoneyLogic.update(100);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockService.update(moneyStock);
		
		InsertMoneyLogic.insert10Yen();
		InsertMoneyLogic.insert10Yen();
		
		MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
		MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
		expectedMoneyStock.numberOf10Yen = 3;
		expectedMoneyStock.numberOf50Yen = 3;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoneyLogic.find();
		int expectedInsertedMoney = 120;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 十円と五十円を投入する() throws Exception {
		InsertMoneyLogic.update(100);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 7;
		moneyStock.numberOf1000Yen = 9;
		MoneyStockService.update(moneyStock);
		
		InsertMoneyLogic.insert10Yen();
		InsertMoneyLogic.insert50Yen();
		
		MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
		MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
		expectedMoneyStock.numberOf10Yen = 2;
		expectedMoneyStock.numberOf50Yen = 4;
		expectedMoneyStock.numberOf100Yen = 5;
		expectedMoneyStock.numberOf500Yen = 7;
		expectedMoneyStock.numberOf1000Yen = 9;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		int actualInsertedMoney = InsertMoneyLogic.find();
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
	
	public static void assertMoneyStockEquals(MoneyStockEntity expectedMoneyStock, MoneyStockEntity actualMoneyStock) {
		assertThat(actualMoneyStock.numberOf10Yen, is(expectedMoneyStock.numberOf10Yen));
		assertThat(actualMoneyStock.numberOf50Yen, is(expectedMoneyStock.numberOf50Yen));
		assertThat(actualMoneyStock.numberOf100Yen, is(expectedMoneyStock.numberOf100Yen));
		assertThat(actualMoneyStock.numberOf500Yen, is(expectedMoneyStock.numberOf500Yen));
		assertThat(actualMoneyStock.numberOf1000Yen, is(expectedMoneyStock.numberOf1000Yen));
	}
}