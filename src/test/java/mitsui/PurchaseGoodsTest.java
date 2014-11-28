package mitsui;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mitsui.dto.OtsuriAndGoodsDto;
import mitsui.entity.MoneyStockEntity;
import mitsui.logic.InsertMoneyLogic;
import mitsui.logic.PurchaseGoodsLogic;
import mitsui.messages.Messages;
import mitsui.path.FilePath;
import mitsui.service.GoodsPriceService;
import mitsui.service.GoodsStockService;
import mitsui.service.InsertedMoneyService;
import mitsui.service.MoneyStockService;
import mitsui.util.FileUtil;

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
		GoodsStockService.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "100");
		goodsPriceMap.put("2", "200");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 1;
		moneyStock.numberOf50Yen = 2;
		moneyStock.numberOf100Yen = 3;
		moneyStock.numberOf500Yen = 4;
		moneyStock.numberOf1000Yen = 5;
		MoneyStockService.update(moneyStock);
		
		InsertedMoneyService.update(0);
		InsertMoneyLogic.insert100Yen();
		
		expectedException.expect(Exception.class);
		expectedException.expectMessage(Messages.NO_INFO_ABOUT_GOODS);
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			actualOtsuriAndGoodsDto = PurchaseGoodsLogic.purchase(0);
		} finally {
			expectedOtsuriAndGoodsDto.goodsId = null;
			expectedOtsuriAndGoodsDto.numberOf10Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf50Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf100Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf500Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf1000Yen = 0;
			
			// おつりと商品は得られない
			assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
			
			// お金のストックは変わらない
			MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
			MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
			expectedMoneyStock.numberOf10Yen = 1;
			expectedMoneyStock.numberOf50Yen = 2;
			expectedMoneyStock.numberOf100Yen = 4;
			expectedMoneyStock.numberOf500Yen = 4;
			expectedMoneyStock.numberOf1000Yen = 5;
			assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockService.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "1");
			expectedGoodsStockMap.put("2", "5");
			assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertedMoneyService.find();
			int expectedInsertedMoney = 100;
			assertThat(actualInsertedMoney, is(expectedInsertedMoney));
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceService.findAll();
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
		GoodsStockService.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "100");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 0;
		moneyStock.numberOf50Yen = 0;
		moneyStock.numberOf100Yen = 0;
		moneyStock.numberOf500Yen = 0;
		moneyStock.numberOf1000Yen = 0;
		MoneyStockService.update(moneyStock);
		
		InsertedMoneyService.update(0);
		InsertMoneyLogic.insert100Yen();
		
		expectedException.expect(Exception.class);
		expectedException.expectMessage(Messages.SOLD_OUT);
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			actualOtsuriAndGoodsDto = PurchaseGoodsLogic.purchase(1);
		} finally {
			expectedOtsuriAndGoodsDto.goodsId = null;
			expectedOtsuriAndGoodsDto.numberOf10Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf50Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf100Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf500Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf1000Yen = 0;
			
			// おつりと商品は得られない
			assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
			
			// お金のストックは変わらない
			MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
			MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
			expectedMoneyStock.numberOf10Yen = 0;
			expectedMoneyStock.numberOf50Yen = 0;
			expectedMoneyStock.numberOf100Yen = 1;
			expectedMoneyStock.numberOf500Yen = 0;
			expectedMoneyStock.numberOf1000Yen = 0;
			assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockService.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "0");
			assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertedMoneyService.find();
			int expectedInsertedMoney = 100;
			assertThat(actualInsertedMoney, is(expectedInsertedMoney));
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceService.findAll();
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
		GoodsStockService.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "100");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 0;
		moneyStock.numberOf50Yen = 0;
		moneyStock.numberOf100Yen = 0;
		moneyStock.numberOf500Yen = 0;
		moneyStock.numberOf1000Yen = 0;
		MoneyStockService.update(moneyStock);
		
		InsertedMoneyService.update(0);
		InsertMoneyLogic.insert10Yen();
		
		expectedException.expect(Exception.class);
		expectedException.expectMessage(Messages.LACK_INSERTED_MONEY);
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			actualOtsuriAndGoodsDto = PurchaseGoodsLogic.purchase(1);
		} finally {
			expectedOtsuriAndGoodsDto.goodsId = null;
			expectedOtsuriAndGoodsDto.numberOf10Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf50Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf100Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf500Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf1000Yen = 0;
			
			// おつりと商品は得られない
			assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
			
			// お金のストックは変わらない
			MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
			MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
			expectedMoneyStock.numberOf10Yen = 1;
			expectedMoneyStock.numberOf50Yen = 0;
			expectedMoneyStock.numberOf100Yen = 0;
			expectedMoneyStock.numberOf500Yen = 0;
			expectedMoneyStock.numberOf1000Yen = 0;
			assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockService.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "1");
			assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertedMoneyService.find();
			int expectedInsertedMoney = 10;
			assertThat(actualInsertedMoney, is(expectedInsertedMoney));
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceService.findAll();
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
		GoodsStockService.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "120");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 7;
		moneyStock.numberOf50Yen = 0;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 1;
		moneyStock.numberOf1000Yen = 0;
		MoneyStockService.update(moneyStock);
		
		InsertedMoneyService.update(0);
		InsertMoneyLogic.insert1000Yen();
		
		expectedException.expect(Exception.class);
		expectedException.expectMessage(Messages.LACK_MONEY_STOCK);
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		try {
			actualOtsuriAndGoodsDto = PurchaseGoodsLogic.purchase(1);
		} finally {
			expectedOtsuriAndGoodsDto.goodsId = null;
			expectedOtsuriAndGoodsDto.numberOf10Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf50Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf100Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf500Yen = 0;
			expectedOtsuriAndGoodsDto.numberOf1000Yen = 0;
			
			// おつりと商品は得られない
			assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
			
			// お金のストックは変わらない
			MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
			MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
			expectedMoneyStock.numberOf10Yen = 7;
			expectedMoneyStock.numberOf50Yen = 0;
			expectedMoneyStock.numberOf100Yen = 5;
			expectedMoneyStock.numberOf500Yen = 1;
			expectedMoneyStock.numberOf1000Yen = 1;
			assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
			
			// 商品のストックは変わらない
			Map<String, String> actualGoodsStockMap = GoodsStockService.findAll();
			Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
			expectedGoodsStockMap.put("1", "1");
			assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
			
			// 投入されたお金の金額は変わらない
			int actualInsertedMoney = InsertedMoneyService.find();
			int expectedInsertedMoney = 1000;
			assertThat(actualInsertedMoney, is(expectedInsertedMoney));
			
			// 商品価格は変わらない
			Map<String, String> actualGoodsPriceMap = GoodsPriceService.findAll();
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
		GoodsStockService.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "220");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 4;
		moneyStock.numberOf50Yen = 3;
		moneyStock.numberOf100Yen = 5;
		moneyStock.numberOf500Yen = 6;
		moneyStock.numberOf1000Yen = 0;
		MoneyStockService.update(moneyStock);
		
		InsertedMoneyService.update(0);
		InsertMoneyLogic.insert1000Yen();
		InsertMoneyLogic.insert1000Yen();
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = PurchaseGoodsLogic.purchase(1);
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		expectedOtsuriAndGoodsDto.goodsId = goodsId;
		expectedOtsuriAndGoodsDto.numberOf10Yen = 3;
		expectedOtsuriAndGoodsDto.numberOf50Yen = 1;
		expectedOtsuriAndGoodsDto.numberOf100Yen = 2;
		expectedOtsuriAndGoodsDto.numberOf500Yen = 1;
		expectedOtsuriAndGoodsDto.numberOf1000Yen = 1;
		
		// おつりと商品が得られる
		assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
		
		// 投入されたお金が0になる
		int actualInsertedMoney = InsertedMoneyService.find();
		int expectedInsertedMoney = 0;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		// お金のストックの枚数が変わる
		MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
		MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 2;
		expectedMoneyStock.numberOf100Yen = 3;
		expectedMoneyStock.numberOf500Yen = 5;
		expectedMoneyStock.numberOf1000Yen = 1;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		// 商品のストックの個数が減る
		Map<String, String> actualGoodsStockMap = GoodsStockService.findAll();
		Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
		expectedGoodsStockMap.put("1", "0");
		assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
		
		// 商品価格は変わらない
		Map<String, String> actualGoodsPriceMap = GoodsPriceService.findAll();
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
		GoodsStockService.update(goodsStockMap);
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		goodsPriceMap.put("1", "220");
		updateGoodsPrice(goodsPriceMap);
		
		MoneyStockEntity moneyStock = new MoneyStockEntity();
		moneyStock.numberOf10Yen = 14;
		moneyStock.numberOf50Yen = 1;
		moneyStock.numberOf100Yen = 6;
		moneyStock.numberOf500Yen = 0;
		moneyStock.numberOf1000Yen = 0;
		MoneyStockService.update(moneyStock);
		
		InsertedMoneyService.update(0);
		InsertMoneyLogic.insert1000Yen();
		
		OtsuriAndGoodsDto actualOtsuriAndGoodsDto = PurchaseGoodsLogic.purchase(1);
		OtsuriAndGoodsDto expectedOtsuriAndGoodsDto = new OtsuriAndGoodsDto();
		expectedOtsuriAndGoodsDto.goodsId = goodsId;
		expectedOtsuriAndGoodsDto.numberOf10Yen = 13;
		expectedOtsuriAndGoodsDto.numberOf50Yen = 1;
		expectedOtsuriAndGoodsDto.numberOf100Yen = 6;
		expectedOtsuriAndGoodsDto.numberOf500Yen = 0;
		expectedOtsuriAndGoodsDto.numberOf1000Yen = 0;
		
		// おつりと商品が得られる
		assertOtsuriAndGoodsDtoEquals(expectedOtsuriAndGoodsDto, actualOtsuriAndGoodsDto);
		
		// 投入されたお金が0になる
		int actualInsertedMoney = InsertedMoneyService.find();
		int expectedInsertedMoney = 0;
		assertThat(actualInsertedMoney, is(expectedInsertedMoney));
		
		// お金のストックの枚数が変わる
		MoneyStockEntity actualMoneyStock = MoneyStockService.findAll();
		MoneyStockEntity expectedMoneyStock = new MoneyStockEntity();
		expectedMoneyStock.numberOf10Yen = 1;
		expectedMoneyStock.numberOf50Yen = 0;
		expectedMoneyStock.numberOf100Yen = 0;
		expectedMoneyStock.numberOf500Yen = 0;
		expectedMoneyStock.numberOf1000Yen = 1;
		assertMoneyStockEquals(expectedMoneyStock, actualMoneyStock);
		
		// 商品のストックの個数が減る
		Map<String, String> actualGoodsStockMap = GoodsStockService.findAll();
		Map<String, String> expectedGoodsStockMap = new HashMap<String, String>();
		expectedGoodsStockMap.put("1", "0");
		assertGoodsStockMapEquals(expectedGoodsStockMap, actualGoodsStockMap);
		
		// 商品価格は変わらない
		Map<String, String> actualGoodsPriceMap = GoodsPriceService.findAll();
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
	
	public static void assertMoneyStockEquals(MoneyStockEntity expected, MoneyStockEntity actual) {
		assertThat(actual.numberOf10Yen, is(expected.numberOf10Yen));
		assertThat(actual.numberOf50Yen, is(expected.numberOf50Yen));
		assertThat(actual.numberOf100Yen, is(expected.numberOf100Yen));
		assertThat(actual.numberOf500Yen, is(expected.numberOf500Yen));
		assertThat(actual.numberOf1000Yen, is(expected.numberOf1000Yen));
	}
	
	public static void assertOtsuriAndGoodsDtoEquals(OtsuriAndGoodsDto expected, OtsuriAndGoodsDto actual) {
		assertThat(actual.goodsId, is(expected.goodsId));
		assertThat(actual.numberOf10Yen, is(expected.numberOf10Yen));
		assertThat(actual.numberOf50Yen, is(expected.numberOf50Yen));
		assertThat(actual.numberOf100Yen, is(expected.numberOf100Yen));
		assertThat(actual.numberOf500Yen, is(expected.numberOf500Yen));
		assertThat(actual.numberOf1000Yen, is(expected.numberOf1000Yen));
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