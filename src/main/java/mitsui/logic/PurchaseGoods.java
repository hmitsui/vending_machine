package mitsui.logic;

import java.util.Map;

import mitsui.dto.OtsuriAndGoodsDto;
import mitsui.entity.MoneyStockEntity;
import mitsui.messages.Messages;
import mitsui.service.GoodsPriceLogic;
import mitsui.service.GoodsStockLogic;
import mitsui.service.MoneyStockLogic;

public class PurchaseGoods {
	
	public static OtsuriAndGoodsDto purchase(int goodsId) throws Exception {
		
		// 商品のストックがあるか
		Map<String, String> goodsStockMap = GoodsStockLogic.findAll();
		if (goodsStockMap.get(Integer.toString(goodsId)) == null) {
			throw new Exception(Messages.NO_INFO_ABOUT_GOODS);
		}
		if (Integer.parseInt(goodsStockMap.get(Integer.toString(goodsId))) < 1) {
			throw new Exception(Messages.SOLD_OUT);
		}
		
		int goodsPrice = GoodsPriceLogic.findPriceById(goodsId);
		int totalInsertedMoney = InsertMoney.find();
		
		// 投入されているお金は商品価格より上か
		if (totalInsertedMoney < goodsPrice) {
			throw new Exception(Messages.LACK_INSERTED_MONEY);
		}
		
		// おつりが出せるか
		int otsuri = totalInsertedMoney - goodsPrice;
		if (!MoneyStockLogic.canGiveOtsuri(otsuri)) {
			throw new Exception(Messages.LACK_MONEY_STOCK);
		}
		
		// 商品のストックが1減る
		int goodsStock = Integer.parseInt(goodsStockMap.get(Integer.toString(goodsId)));
		goodsStockMap.put(Integer.toString(goodsId), Integer.toString(goodsStock - 1));
		GoodsStockLogic.update(goodsStockMap);
		
		// 投入金額が0になる
		InsertMoney.update(0);
		
		// おつりの各硬貨の枚数を返す。購入できた場合は商品IDも返す。商品が出てきて、差額がおつりとして出る
		OtsuriAndGoodsDto purchaseDto = new OtsuriAndGoodsDto();
		purchaseDto = MoneyStockLogic.computeNumberOfOtsuri(otsuri);
		purchaseDto.goodsId = goodsId;
		
		// 自動販売機内のお金の枚数が変わる
		MoneyStockEntity moneyStock = MoneyStockLogic.findAll();
		moneyStock.numberOf10Yen = moneyStock.numberOf10Yen - purchaseDto.numberOf10Yen;
		moneyStock.numberOf50Yen = moneyStock.numberOf50Yen - purchaseDto.numberOf50Yen;
		moneyStock.numberOf100Yen = moneyStock.numberOf100Yen - purchaseDto.numberOf100Yen;
		moneyStock.numberOf500Yen = moneyStock.numberOf500Yen - purchaseDto.numberOf500Yen;
		moneyStock.numberOf1000Yen = moneyStock.numberOf1000Yen - purchaseDto.numberOf1000Yen;
		
		MoneyStockLogic.update(moneyStock);
		
		return purchaseDto;
	}
}
