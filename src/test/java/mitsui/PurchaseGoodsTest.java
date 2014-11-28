package mitsui;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

// TODO パッケージを作る

// 動作確認のためのテスト
public class PurchaseGoodsTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void 商品Idが存在しない場合は購入できない() throws Exception {
		Map<String, String> goodsStockMap = new HashMap<String, String>();
		goodsStockMap.put("1", "1");
		goodsStockMap.put("2", "5");
		GoodsStockLogic.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "100");
		goodsPriceMap.put("2", "200");
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
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			actualOtsuriAndGoodsDto = PurchaseGoods.purchase(0);
			expectedOtsuriAndGoodsDto.goodsId = 0;
			expectedOtsuriAndGoodsDto.numberOf10yen = 0;
			expectedOtsuriAndGoodsDto.numberOf50yen = 0;
			expectedOtsuriAndGoodsDto.numberOf100yen = 0;
			expectedOtsuriAndGoodsDto.numberOf500yen = 0;
			expectedOtsuriAndGoodsDto.numberOf1000yen = 0;
		} finally {
			// おつりと商品は得られない
			assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
			
			// お金のストックは変わらない
			MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
			MoneyStock expectedMoneyStock = new MoneyStock();
			expectedMoneyStock.numberOf10yen = 1;
			expectedMoneyStock.numberOf50yen = 2;
			expectedMoneyStock.numberOf100yen = 4;
			expectedMoneyStock.numberOf500yen = 4;
			expectedMoneyStock.numberOf1000yen = 5;
			assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "1");
			expectedGoodsStockMap.put("2", "5");
			assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertMoney.find();
			int expectedInsertedMoney = 100;
			assertThat(actualInsertedMoney, is(expectedInsertedMoney));
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
			Map<String, String> expectedGoodsPriceMap = new HashMap<String, String>();
			expectedGoodsPriceMap.put("1", "100");
			expectedGoodsPriceMap.put("2", "200");
			assertGoodsPriceMapEquals(expectedGoodsPriceMap, actualGoodsPriceMap);
			
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
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			actualOtsuriAndGoodsDto = PurchaseGoods.purchase(1);
			expectedOtsuriAndGoodsDto.goodsId = 0;
			expectedOtsuriAndGoodsDto.numberOf10yen = 0;
			expectedOtsuriAndGoodsDto.numberOf50yen = 0;
			expectedOtsuriAndGoodsDto.numberOf100yen = 0;
			expectedOtsuriAndGoodsDto.numberOf500yen = 0;
			expectedOtsuriAndGoodsDto.numberOf1000yen = 0;
		} finally {
			// おつりと商品は得られない
			assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
			
			// お金のストックは変わらない
			MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
			MoneyStock expectedMoneyStock = new MoneyStock();
			expectedMoneyStock.numberOf10yen = 0;
			expectedMoneyStock.numberOf50yen = 0;
			expectedMoneyStock.numberOf100yen = 1;
			expectedMoneyStock.numberOf500yen = 0;
			expectedMoneyStock.numberOf1000yen = 0;
			assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "0");
			assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertMoney.find();
			int expectedInsertedMoney = 100;
			assertThat(actualInsertedMoney, is(expectedInsertedMoney));
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
			Map<String, String> expectedGoodsPriceMap = new HashMap<String, String>();
			expectedGoodsPriceMap.put("1", "100");
			assertGoodsPriceMapEquals(expectedGoodsPriceMap, actualGoodsPriceMap);
			
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
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			actualOtsuriAndGoodsDto = PurchaseGoods.purchase(1);
			expectedOtsuriAndGoodsDto.goodsId = 0;
			expectedOtsuriAndGoodsDto.numberOf10yen = 0;
			expectedOtsuriAndGoodsDto.numberOf50yen = 0;
			expectedOtsuriAndGoodsDto.numberOf100yen = 0;
			expectedOtsuriAndGoodsDto.numberOf500yen = 0;
			expectedOtsuriAndGoodsDto.numberOf1000yen = 0;
		} finally {
			// おつりと商品は得られない
			assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
			
			// お金のストックは変わらない
			MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
			MoneyStock expectedMoneyStock = new MoneyStock();
			expectedMoneyStock.numberOf10yen = 1;
			expectedMoneyStock.numberOf50yen = 0;
			expectedMoneyStock.numberOf100yen = 0;
			expectedMoneyStock.numberOf500yen = 0;
			expectedMoneyStock.numberOf1000yen = 0;
			assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "1");
			assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertMoney.find();
			int expectedInsertedMoney = 10;
			assertThat(actualInsertedMoney, is(expectedInsertedMoney));
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
			Map<String, String> expectedGoodsPriceMap = new HashMap<String, String>();
			expectedGoodsPriceMap.put("1", "100");
			assertGoodsPriceMapEquals(expectedGoodsPriceMap, actualGoodsPriceMap);
			
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
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			actualOtsuriAndGoodsDto = PurchaseGoods.purchase(1);
			expectedOtsuriAndGoodsDto.goodsId = 0;
			expectedOtsuriAndGoodsDto.numberOf10yen = 0;
			expectedOtsuriAndGoodsDto.numberOf50yen = 0;
			expectedOtsuriAndGoodsDto.numberOf100yen = 0;
			expectedOtsuriAndGoodsDto.numberOf500yen = 0;
			expectedOtsuriAndGoodsDto.numberOf1000yen = 0;
		} finally {
			// おつりと商品は得られない
			assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
			
			// お金のストックは変わらない
			MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
			MoneyStock expectedMoneyStock = new MoneyStock();
			expectedMoneyStock.numberOf10yen = 7;
			expectedMoneyStock.numberOf50yen = 0;
			expectedMoneyStock.numberOf100yen = 5;
			expectedMoneyStock.numberOf500yen = 1;
			expectedMoneyStock.numberOf1000yen = 1;
			assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "1");
			assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertMoney.find();
			int expectedInsertedMoney = 1000;
			assertThat(actualInsertedMoney, is(expectedInsertedMoney));
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
			Map<String, String> expectedGoodsPriceMap = new HashMap<String, String>();
			expectedGoodsPriceMap.put("1", "120");
			assertGoodsPriceMapEquals(expectedGoodsPriceMap, actualGoodsPriceMap);
			
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
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = PurchaseGoods.purchase(1);
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		expectedOtsuriAndGoodsDto.goodsId = goodsId;
		expectedOtsuriAndGoodsDto.numberOf10yen = 3;
		expectedOtsuriAndGoodsDto.numberOf50yen = 1;
		expectedOtsuriAndGoodsDto.numberOf100yen = 2;
		expectedOtsuriAndGoodsDto.numberOf500yen = 1;
		expectedOtsuriAndGoodsDto.numberOf1000yen = 1;
		
		// おつりと商品が得られる
		assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
		
		// 投入されたお金が0になる
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 0;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		// お金のストックの枚数が変わる
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 2;
		expectedMoneyStock.numberOf100yen = 3;
		expectedMoneyStock.numberOf500yen = 5;
		expectedMoneyStock.numberOf1000yen = 1;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		// 商品のストックの個数が減る
		Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
		Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
		expectedGoodsStockMap.put("1", "0");
		assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
		
		// 商品価格は変わらない
		Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
		Map<String, String> expectedGoodsPriceMap = new HashMap<String, String>();
		expectedGoodsPriceMap.put("1", "220");
		assertGoodsPriceMapEquals(expectedGoodsPriceMap, actualGoodsPriceMap);
		
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
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = PurchaseGoods.purchase(1);
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		expectedOtsuriAndGoodsDto.goodsId = goodsId;
		expectedOtsuriAndGoodsDto.numberOf10yen = 13;
		expectedOtsuriAndGoodsDto.numberOf50yen = 1;
		expectedOtsuriAndGoodsDto.numberOf100yen = 6;
		expectedOtsuriAndGoodsDto.numberOf500yen = 0;
		expectedOtsuriAndGoodsDto.numberOf1000yen = 0;
		
		// おつりと商品が得られる
		assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
		
		// 投入されたお金が0になる
		int actualInsertedMoney = InsertMoney.find();
		int expectedInsertedMoney = 0;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		// お金のストックの枚数が変わる
		MoneyStock actualMoneyStock = MoneyStockLogic.findAll();
		MoneyStock expectedMoneyStock = new MoneyStock();
		expectedMoneyStock.numberOf10yen = 1;
		expectedMoneyStock.numberOf50yen = 0;
		expectedMoneyStock.numberOf100yen = 0;
		expectedMoneyStock.numberOf500yen = 0;
		expectedMoneyStock.numberOf1000yen = 1;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		// 商品のストックの個数が減る
		Map<String, String> actualGoodsStockMap = GoodsStockLogic.findAll();
		Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
		expectedGoodsStockMap.put("1", "0");
		assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
		
		// 商品価格は変わらない
		Map<String, String> actualGoodsPriceMap = GoodsPriceLogic.findAll();
		Map<String, String> expectedGoodsPriceMap = new HashMap<String, String>();
		expectedGoodsPriceMap.put("1", "220");
		assertGoodsPriceMapEquals(expectedGoodsPriceMap, actualGoodsPriceMap);
		
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
	
	public static void assertMoneyStockEquals(MoneyStock expected, MoneyStock actual) {
		assertThat(actual.numberOf10yen, is(expected.numberOf10yen));
		assertThat(actual.numberOf50yen, is(expected.numberOf50yen));
		assertThat(actual.numberOf100yen, is(expected.numberOf100yen));
		assertThat(actual.numberOf500yen, is(expected.numberOf500yen));
		assertThat(actual.numberOf1000yen, is(expected.numberOf1000yen));
	}
	
	public static void assertOtsuriAndGoodsDtoEquals(OtsuriAndGoodsDto expected, OtsuriAndGoodsDto actual) {
		assertThat(actual.goodsId, is(expected.goodsId));
		assertThat(actual.numberOf10yen, is(expected.numberOf10yen));
		assertThat(actual.numberOf50yen, is(expected.numberOf50yen));
		assertThat(actual.numberOf100yen, is(expected.numberOf100yen));
		assertThat(actual.numberOf500yen, is(expected.numberOf500yen));
		assertThat(actual.numberOf1000yen, is(expected.numberOf1000yen));
	}
	
	public static void assertGoodsStockMapEquals(Map<String, String> expected, Map<String, String> actual) {
		assertThat(actual.size(), is(expected.size()));
		for (int i = 1; i <= expected.size(); i++) {
			String expectedValue = expected.get(Integer.toString(i));
			String actualValue = actual.get(Integer.toString(i));
			assertThat(actualValue, is(expectedValue));
		}
	}
	
	public static void assertGoodsPriceMapEquals(Map<String, String> expected, Map<String, String> actual) {
		assertThat(actual.size(), is(expected.size()));
		for (int i = 1; i <= expected.size(); i++) {
			String expectedValue = expected.get(Integer.toString(i));
			String actualValue = actual.get(Integer.toString(i));
			assertThat(actualValue, is(expectedValue));
		}
	}
	
}