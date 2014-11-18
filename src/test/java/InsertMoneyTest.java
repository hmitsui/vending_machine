import org.junit.Test;

import static org.junit.Assert.assertEquals;

// 動作確認のためのテスト
public class InsertMoneyTest {
	
	@Test
	public void 十円を投入する() throws Exception {
		InsertMoney.update(100);
		InsertMoney.insert10yen();
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 2;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assert expectedMoneyStock.equals(actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 110;
		assertEquals(expectedInsertedMoney, actualInsertedMoney);
	}
	
	@Test
	public void 五十円を投入する() throws Exception {
		InsertMoney.update(100);
		InsertMoney.insert50yen();
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 4;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assert expectedMoneyStock.equals(actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 150;
		assertEquals(expectedInsertedMoney, actualInsertedMoney);
	}
	
	@Test
	public void 百円を投入する() throws Exception {
		InsertMoney.update(100);
		InsertMoney.insert100yen();
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 6;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assert expectedMoneyStock.equals(actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 200;
		assertEquals(expectedInsertedMoney, actualInsertedMoney);
	}
	
	@Test
	public void 五百円を投入する() throws Exception {
		InsertMoney.update(100);
		InsertMoney.insert500yen();
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 8;
		expectedMoneyStock.numberOf1000yen = 9;
		assert expectedMoneyStock.equals(actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 600;
		assertEquals(expectedInsertedMoney, actualInsertedMoney);
	}
	
	@Test
	public void 千円を投入する() throws Exception {
		InsertMoney.update(100);
		InsertMoney.insert1000yen();
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 10;
		assert expectedMoneyStock.equals(actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 1100;
		assertEquals(expectedInsertedMoney, actualInsertedMoney);
	}
	
	@Test
	public void 十円を2つ投入する() throws Exception {
		InsertMoney.update(100);
		InsertMoney.insert10yen();
		InsertMoney.insert10yen();
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 3;
		expectedMoneyStock.numberOf50yen = 3;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assert expectedMoneyStock.equals(actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 120;
		assertEquals(expectedInsertedMoney, actualInsertedMoney);
	}
	
	@Test
	public void 十円と五十円を投入する() throws Exception {
		InsertMoney.update(100);
		InsertMoney.insert10yen();
		InsertMoney.insert50yen();
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 7;
		moneyStock.numberOf1000yen = 9;
		MoneyStockLogic.update(moneyStock);
		
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 2;
		expectedMoneyStock.numberOf50yen = 4;
		expectedMoneyStock.numberOf100yen = 5;
		expectedMoneyStock.numberOf500yen = 7;
		expectedMoneyStock.numberOf1000yen = 9;
		assert expectedMoneyStock.equals(actualMoneyStock);
		
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 160;
		assertEquals(expectedInsertedMoney, actualInsertedMoney);
	}
}