import java.util.HashMap;
import java.util.Map;

public class PurchaseGoods {
	
	public static OtsuriAndGoodsDto purchase(int goodsId) throws Exception {
		
		// 引数のIDの商品のストックがあるかをファイルから読み込む
		Map<String, String> goodsStockMap = new HashMap<String, String>();
		goodsStockMap = GoodsStockLogic.findAll();
		
		// 判定 商品のストックがあるか
		if (goodsStockMap.get(Integer.toString(goodsId)) == null) {
			throw new Exception(Messages.NO_INFO_ABOUT_GOODS);
		}
		if (Integer.parseInt(goodsStockMap.get(Integer.toString(goodsId))) < 1) {
			throw new Exception(Messages.SOLD_OUT);
		}
		
		// 引数のIDの商品の価格をファイルから読み込む
		int goodsPrice = GoodsPriceLogic.findPriceById(goodsId);
		
		// 判定 投入されているお金は商品価格より上か
		int totalInsertedMoney = InsertMoney.find();
		if (totalInsertedMoney < goodsPrice) {
			throw new Exception(Messages.LACK_INSERTED_MONEY);
		}
		
		int otsuri = totalInsertedMoney - goodsPrice;
		// 判定 おつりが出せるか
		if (!MoneyStockLogic.canGiveOtsuri(otsuri)) {
			throw new Exception(Messages.LACK_MONEY_STOCK);
		}
		
		// 商品のストックが1減る
		int goodsStock = Integer.parseInt(goodsStockMap.get(Integer.toString(goodsId)));
		goodsStockMap.put(Integer.toString(goodsId), Integer.toString(goodsStock - 1));
		
		// 投入金額が0になる
		InsertMoney.update(0);
		
		// おつりの各硬貨の枚数を返す。購入できた場合は商品IDも返す。商品が出てきて、差額がおつりとして出る
		OtsuriAndGoodsDto purchaseDto = new OtsuriAndGoodsDto();
		purchaseDto = MoneyStockLogic.computeNumberOfOtsuri(otsuri);
		purchaseDto.goodsId = goodsId;
		
		// 自動販売機内のお金の枚数が変わる
		MoneyStock moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf10yen = moneyStock.numberOf10yen - purchaseDto.numberOf10yen;
		moneyStock.numberOf50yen = moneyStock.numberOf50yen - purchaseDto.numberOf50yen;
		moneyStock.numberOf100yen = moneyStock.numberOf100yen - purchaseDto.numberOf100yen;
		moneyStock.numberOf500yen = moneyStock.numberOf500yen - purchaseDto.numberOf500yen;
		moneyStock.numberOf1000yen = moneyStock.numberOf1000yen - purchaseDto.numberOf1000yen;
		
		return purchaseDto;
	}
}
