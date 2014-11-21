package test.java;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import main.java.FilePath;
import main.java.FileUtil;
import main.java.GoodsPriceLogic;
import main.java.GoodsStockLogic;
import main.java.InsertMoney;
import main.java.Messages;
import main.java.MoneyStock;
import main.java.MoneyStockLogic;
import main.java.OtsuriAndGoodsDto;
import main.java.PurchaseGoods;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

// TODO パッケージを作る

// 動作確認のためのテスト
public class PurchaseGoodsTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	// TODO assertEqualsでオブジェクトを比較すると、ポインタが違うというエラーになる。
	// TODO 呼び出し先を掘っていくとboolean java.lang.Object.equals(Object
	// obj)メソッドにいっているが…。
	// assertEquals(expectedOtsuriAndGoodsDto, otsuriAndGoodsDto);
	// assertThat(otsuriAndGoodsDto, is(expectedOtsuriAndGoodsDto));
	
	@Test
	public void 商品Idが存在しない場合は購入できない() throws Exception {
		Map<String, String> goodsStockMap = new HashMap<String, String>();
		goodsStockMap.put("1", "1");
		GoodsStockLogic.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "100");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 1;
		moneyStock.numberOf50yen = 2;
		moneyStock.numberOf100yen = 3;
		moneyStock.numberOf500yen = 4;
		moneyStock.numberOf1000yen = 5;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.update(0);
		InsertMoney.insert100yen();
		
		expectedException.expect(Exception.class);
		expectedException.expectMessage(Messages.NO_INFO_ABOUT_GOODS);
		
		OtsuriAndGoodsDto otsuriAndGoodsDto = new OtsuriAndGoodsDto();
		try {
			otsuriAndGoodsDto = PurchaseGoods.purchase(0);
		} finally {
			// おつりと商品は得られない
			assert otsuriAndGoodsDto == null;
			
			// お金のストックは変わらない
			MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
			MoneyStock expectedMoneyStock = new MoneyStock();
			expectedMoneyStock.numberOf10yen = 1;
			expectedMoneyStock.numberOf50yen = 2;
			expectedMoneyStock.numberOf100yen = 4;
			expectedMoneyStock.numberOf500yen = 4;
			expectedMoneyStock.numberOf1000yen = 5;
			assert expectedMoneyStock.equals(actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "1");
			assert expectedGoodsStockMap.equals(actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertMoney.find();
			int expectedInsertedMoney = 100;
			assertEquals(expectedInsertedMoney, actualInsertedMoney);
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
			assert goodsPriceMap.equals(actualGoodsPriceMap);
			
			eraseText(FilePath.GOODS_PRICE_FILE);
			eraseText(FilePath.GODDS_STOCK_FILE);
			eraseText(FilePath.INSERTED_MONEY_FILE);
			eraseText(FilePath.MONEY_STOCK_FILE);
		}
	}
	
	@Test
	public void 売り切れ() throws Exception {
		Map<String, String> goodsStockMap = new HashMap<String, String>();
		goodsStockMap.put("1", "0");
		GoodsStockLogic.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "100");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 0;
		moneyStock.numberOf50yen = 0;
		moneyStock.numberOf100yen = 0;
		moneyStock.numberOf500yen = 0;
		moneyStock.numberOf1000yen = 0;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.update(0);
		InsertMoney.insert100yen();
		
		expectedException.expect(Exception.class);
		expectedException.expectMessage(Messages.SOLD_OUT);
		
		OtsuriAndGoodsDto otsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			otsuriAndGoodsDto = PurchaseGoods.purchase(1);
		} finally {
			// おつりと商品は得られない
			assert otsuriAndGoodsDto == null;
			
			// お金のストックは変わらない
			MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
			MoneyStock expectedMoneyStock = new MoneyStock();
			expectedMoneyStock.numberOf10yen = 0;
			expectedMoneyStock.numberOf50yen = 0;
			expectedMoneyStock.numberOf100yen = 1;
			expectedMoneyStock.numberOf500yen = 0;
			expectedMoneyStock.numberOf1000yen = 0;
			assert expectedMoneyStock.equals(actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "0");
			assert expectedGoodsStockMap.equals(actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertMoney.find();
			int expectedInsertedMoney = 100;
			assertEquals(expectedInsertedMoney, actualInsertedMoney);
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
			assert goodsPriceMap.equals(actualGoodsPriceMap);
			
			eraseText(FilePath.GOODS_PRICE_FILE);
			eraseText(FilePath.GODDS_STOCK_FILE);
			eraseText(FilePath.INSERTED_MONEY_FILE);
			eraseText(FilePath.MONEY_STOCK_FILE);
		}
	}
	
	@Test
	public void 投入金額が足りない場合は購入できない() throws Exception {
		Map<String, String> goodsStockMap = new HashMap<String, String>();
		goodsStockMap.put("1", "1");
		GoodsStockLogic.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "100");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 0;
		moneyStock.numberOf50yen = 0;
		moneyStock.numberOf100yen = 0;
		moneyStock.numberOf500yen = 0;
		moneyStock.numberOf1000yen = 0;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.update(0);
		InsertMoney.insert10yen();
		
		expectedException.expect(Exception.class);
		expectedException.expectMessage(Messages.LACK_INSERTED_MONEY);
		
		OtsuriAndGoodsDto otsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			otsuriAndGoodsDto = PurchaseGoods.purchase(1);
		} finally {
			// おつりと商品は得られない
			assert otsuriAndGoodsDto == null;
			
			// お金のストックは変わらない
			MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
			MoneyStock expectedMoneyStock = new MoneyStock();
			expectedMoneyStock.numberOf10yen = 1;
			expectedMoneyStock.numberOf50yen = 0;
			expectedMoneyStock.numberOf100yen = 0;
			expectedMoneyStock.numberOf500yen = 0;
			expectedMoneyStock.numberOf1000yen = 0;
			assert expectedMoneyStock.equals(actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "1");
			assert expectedGoodsStockMap.equals(actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertMoney.find();
			int expectedInsertedMoney = 10;
			assertEquals(expectedInsertedMoney, actualInsertedMoney);
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
			assert goodsPriceMap.equals(actualGoodsPriceMap);
			
			eraseText(FilePath.GOODS_PRICE_FILE);
			eraseText(FilePath.GODDS_STOCK_FILE);
			eraseText(FilePath.INSERTED_MONEY_FILE);
			eraseText(FilePath.MONEY_STOCK_FILE);
		}
	}
	
	@Test
	public void おつりを出せない() throws Exception {
		Map<String, String> goodsStockMap = new HashMap<String, String>();
		goodsStockMap.put("1", "1");
		GoodsStockLogic.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "120");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 7;
		moneyStock.numberOf50yen = 0;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 1;
		moneyStock.numberOf1000yen = 0;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.update(0);
		InsertMoney.insert1000yen();
		
		expectedException.expect(Exception.class);
		expectedException.expectMessage(Messages.LACK_MONEY_STOCK);
		
		OtsuriAndGoodsDto otsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			otsuriAndGoodsDto = PurchaseGoods.purchase(1);
		} finally {
			// おつりと商品は得られない
			assert otsuriAndGoodsDto == null;
			
			// お金のストックは変わらない
			MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
			MoneyStock expectedMoneyStock = new MoneyStock();
			expectedMoneyStock.numberOf10yen = 7;
			expectedMoneyStock.numberOf50yen = 0;
			expectedMoneyStock.numberOf100yen = 5;
			expectedMoneyStock.numberOf500yen = 1;
			expectedMoneyStock.numberOf1000yen = 1;
			assert expectedMoneyStock.equals(actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "1");
			assert expectedGoodsStockMap.equals(actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertMoney.find();
			int expectedInsertedMoney = 1000;
			assertEquals(expectedInsertedMoney, actualInsertedMoney);
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
			assert goodsPriceMap.equals(actualGoodsPriceMap);
			
			eraseText(FilePath.GOODS_PRICE_FILE);
			eraseText(FilePath.GODDS_STOCK_FILE);
			eraseText(FilePath.INSERTED_MONEY_FILE);
			eraseText(FilePath.MONEY_STOCK_FILE);
		}
	}
	
	@Test
	public void 購入できる() throws Exception {
		Map<String, String> goodsStockMap = new HashMap<String, String>();
		int goodsId = 1;
		goodsStockMap.put(Integer.toString(goodsId), "1");
		GoodsStockLogic.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "220");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 4;
		moneyStock.numberOf50yen = 3;
		moneyStock.numberOf100yen = 5;
		moneyStock.numberOf500yen = 6;
		moneyStock.numberOf1000yen = 0;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.update(0);
		InsertMoney.insert1000yen();
		InsertMoney.insert1000yen();
		
		OtsuriAndGoodsDto otsuriAndGoodsDto = PurchaseGoods.purchase(1);
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		expectedOtsuriAndGoodsDto.goodsId = goodsId;
		expectedOtsuriAndGoodsDto.numberOf10yen = 3;
		expectedOtsuriAndGoodsDto.numberOf50yen = 1;
		expectedOtsuriAndGoodsDto.numberOf100yen = 2;
		expectedOtsuriAndGoodsDto.numberOf500yen = 1;
		expectedOtsuriAndGoodsDto.numberOf1000yen = 1;
		
		// おつりと商品が得られる
		assert expectedOtsuriAndGoodsDto.equals(otsuriAndGoodsDto);
		
		// 投入されたお金が0になる
		assertEquals(0, InsertMoney.find());
		
		// お金のストックの枚数が変わる
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 2;
		expectedMoneyStock.numberOf100yen = 3;
		expectedMoneyStock.numberOf500yen = 5;
		expectedMoneyStock.numberOf1000yen = 1;
		assert expectedMoneyStock.equals(actualMoneyStock);
		
		// 商品のストックの個数が減る
		Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
		Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
		expectedGoodsStockMap.put("1", "0");
		assert expectedGoodsStockMap.equals(actualGoodsStockMap);
		
		// 商品価格は変わらない
		Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
		assert goodsPriceMap.equals(actualGoodsPriceMap);
		
		eraseText(FilePath.GOODS_PRICE_FILE);
		eraseText(FilePath.GODDS_STOCK_FILE);
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	@Test
	public void 購入できておつりの100円と10円の枚数が多い() throws Exception {
		Map<String, String> goodsStockMap = new HashMap<String, String>();
		int goodsId = 1;
		goodsStockMap.put(Integer.toString(goodsId), "1");
		GoodsStockLogic.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "220");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStock moneyStock = new MoneyStock();
		moneyStock.numberOf10yen = 14;
		moneyStock.numberOf50yen = 1;
		moneyStock.numberOf100yen = 6;
		moneyStock.numberOf500yen = 0;
		moneyStock.numberOf1000yen = 0;
		MoneyStockLogic.update(moneyStock);
		
		InsertMoney.update(0);
		InsertMoney.insert1000yen();
		
		OtsuriAndGoodsDto otsuriAndGoodsDto = PurchaseGoods.purchase(1);
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		expectedOtsuriAndGoodsDto.goodsId = goodsId;
		expectedOtsuriAndGoodsDto.numberOf10yen = 13;
		expectedOtsuriAndGoodsDto.numberOf50yen = 1;
		expectedOtsuriAndGoodsDto.numberOf100yen = 6;
		expectedOtsuriAndGoodsDto.numberOf500yen = 0;
		expectedOtsuriAndGoodsDto.numberOf1000yen = 0;
		
		// おつりと商品が得られる
		assert expectedOtsuriAndGoodsDto.equals(otsuriAndGoodsDto);
		
		// 投入されたお金が0になる
		assertEquals(0, InsertMoney.find());
		
		// お金のストックの枚数が変わる
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 2;
		expectedMoneyStock.numberOf100yen = 3;
		expectedMoneyStock.numberOf500yen = 5;
		expectedMoneyStock.numberOf1000yen = 1;
		assert expectedMoneyStock.equals(actualMoneyStock);
		
		// 商品のストックの個数が減る
		Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
		Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
		expectedGoodsStockMap.put("1", "0");
		assert expectedGoodsStockMap.equals(actualGoodsStockMap);
		
		// 商品価格は変わらない
		Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
		assert goodsPriceMap.equals(actualGoodsPriceMap);
		
		eraseText(FilePath.GOODS_PRICE_FILE);
		eraseText(FilePath.GODDS_STOCK_FILE);
		eraseText(FilePath.INSERTED_MONEY_FILE);
		eraseText(FilePath.MONEY_STOCK_FILE);
	}
	
	public static void updateGoodsPrice(Map<String, String> goodsPriceMap) throws Exception {
		if (goodsPriceMap == null) {
			return;
		}
		
		File goodsPriceFile = new File(FilePath.GOODS_PRICE_FILE);
		
		String header = "1";
		String data = goodsPriceMap.get(Integer.toString(1));
		for (int i = 2; i <= goodsPriceMap.size(); i++) {
			header = header + "," + Integer.toString(i);
			data = data + "," + goodsPriceMap.get(Integer.toString(i));
		}
		
		if (!FileUtil.canWriteFile(goodsPriceFile)) {
			throw new Exception("ファイルが存在しない、または書き込めません。");
		}
		
		FileUtil.writeFile(goodsPriceFile, header, data);
	}
	
	public static void eraseText(String filePath) throws Exception {
		File file = new File(filePath);
		
		if (!FileUtil.canWriteFile(file)) {
			throw new Exception("ファイルが存在しない、または書き込めません。");
		}
		FileUtil.writeFile(file, "");
	}
}