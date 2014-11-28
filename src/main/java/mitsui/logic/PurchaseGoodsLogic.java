package mitsui.logic;

import java.util.Map;

import mitsui.dto.OtsuriAndGoodsDto;
import mitsui.entity.MoneyStockEntity;
import mitsui.messages.Messages;
import mitsui.service.GoodsPriceService;
import mitsui.service.GoodsStockService;
import mitsui.service.InsertedMoneyService;
import mitsui.service.MoneyStockService;

public class PurchaseGoodsLogic {
	
	public static OtsuriAndGoodsDto purchase(int goodsId) throws Exception {
		
		// 商品のストックがあるか
		Map<String, String> goodsStockMap = GoodsStockService.findAll();
		if (goodsStockMap.get(Integer.toString(goodsId)) == null) {
			throw new Exception(Messages.NO_INFO_ABOUT_GOODS);
		}
		if (Integer.parseInt(goodsStockMap.get(Integer.toString(goodsId))) < 1) {
			throw new Exception(Messages.SOLD_OUT);
		}
		
		int goodsPrice = GoodsPriceService.findPriceById(goodsId);
		int totalInsertedMoney = InsertedMoneyService.find();
		
		// 投入されているお金は商品価格より上か
		if (totalInsertedMoney < goodsPrice) {
			throw new Exception(Messages.LACK_INSERTED_MONEY);
		}
		
		// おつりが出せるか
		int otsuri = totalInsertedMoney - goodsPrice;
		if (!canGiveOtsuri(otsuri)) {
			throw new Exception(Messages.LACK_MONEY_STOCK);
		}
		
		// 商品のストックが1減る
		int goodsStock = Integer.parseInt(goodsStockMap.get(Integer.toString(goodsId)));
		goodsStockMap.put(Integer.toString(goodsId), Integer.toString(goodsStock - 1));
		GoodsStockService.update(goodsStockMap);
		
		// 投入金額が0になる
		InsertedMoneyService.update(0);
		
		// おつりの各硬貨の枚数を返す。購入できた場合は商品IDも返す。商品が出てきて、差額がおつりとして出る
		OtsuriAndGoodsDto purchaseDto = new OtsuriAndGoodsDto();
		purchaseDto = computeNumberOfOtsuri(otsuri);
		purchaseDto.goodsId = goodsId;
		
		// 自動販売機内のお金の枚数が変わる
		MoneyStockEntity moneyStock = MoneyStockService.findAll();
		moneyStock.numberOf10Yen = moneyStock.numberOf10Yen - purchaseDto.numberOf10Yen;
		moneyStock.numberOf50Yen = moneyStock.numberOf50Yen - purchaseDto.numberOf50Yen;
		moneyStock.numberOf100Yen = moneyStock.numberOf100Yen - purchaseDto.numberOf100Yen;
		moneyStock.numberOf500Yen = moneyStock.numberOf500Yen - purchaseDto.numberOf500Yen;
		moneyStock.numberOf1000Yen = moneyStock.numberOf1000Yen - purchaseDto.numberOf1000Yen;
		
		MoneyStockService.update(moneyStock);
		
		return purchaseDto;
	}
	
	public static boolean canGiveOtsuri(int otsuriKingaku) throws Exception {
		MoneyStockEntity moneyStock = MoneyStockService.findAll();
		
		int favoredNumberOf1000Yen = otsuriKingaku / 1000;
		int otsuriKinkagakuExcept1000Yen;
		if (favoredNumberOf1000Yen == 0) {
			otsuriKinkagakuExcept1000Yen = otsuriKingaku;
		} else {
			int otsuriNumberOf1000Yen;
			if (favoredNumberOf1000Yen <= moneyStock.numberOf1000Yen) {
				otsuriNumberOf1000Yen = favoredNumberOf1000Yen;
			} else {
				otsuriNumberOf1000Yen = moneyStock.numberOf1000Yen;
			}
			otsuriKinkagakuExcept1000Yen = otsuriKingaku - otsuriNumberOf1000Yen * 1000;
		}
		
		int favoredNumberOf500Yen = otsuriKinkagakuExcept1000Yen / 500;
		int otsuriKingakuExcept500Yen;
		if (favoredNumberOf500Yen == 0) {
			otsuriKingakuExcept500Yen = otsuriKinkagakuExcept1000Yen;
		} else {
			int otsuriNumberOf500Yen;
			if (favoredNumberOf500Yen <= moneyStock.numberOf500Yen) {
				otsuriNumberOf500Yen = favoredNumberOf500Yen;
			} else {
				otsuriNumberOf500Yen = moneyStock.numberOf500Yen;
			}
			otsuriKingakuExcept500Yen = otsuriKinkagakuExcept1000Yen - otsuriNumberOf500Yen * 500;
		}
		
		int favoredNumberOf100Yen = otsuriKingakuExcept500Yen / 100;
		int otsuriKingakuExcept100Yen;
		if (favoredNumberOf100Yen == 0) {
			otsuriKingakuExcept100Yen = otsuriKingakuExcept500Yen;
		} else {
			int otsuriNumberOf100Yen;
			if (favoredNumberOf100Yen <= moneyStock.numberOf100Yen) {
				otsuriNumberOf100Yen = favoredNumberOf100Yen;
			} else {
				otsuriNumberOf100Yen = moneyStock.numberOf100Yen;
			}
			otsuriKingakuExcept100Yen = otsuriKingakuExcept500Yen - otsuriNumberOf100Yen * 100;
		}
		
		int favoredNumberOf50Yen = otsuriKingakuExcept100Yen / 50;
		int otsuriKingakuExcept50Yen;
		if (favoredNumberOf50Yen == 0) {
			otsuriKingakuExcept50Yen = otsuriKingakuExcept100Yen;
		} else {
			int otsuriNumberOf50Yen;
			if (favoredNumberOf50Yen <= moneyStock.numberOf50Yen) {
				otsuriNumberOf50Yen = favoredNumberOf50Yen;
			} else {
				otsuriNumberOf50Yen = moneyStock.numberOf50Yen;
			}
			otsuriKingakuExcept50Yen = otsuriKingakuExcept100Yen - otsuriNumberOf50Yen * 50;
		}
		
		int favoredNumberOf10Yen = otsuriKingakuExcept50Yen / 10;
		if (favoredNumberOf10Yen <= moneyStock.numberOf10Yen) {
			return true;
		} else {
			return false;
		}
	}
	
	public static OtsuriAndGoodsDto computeNumberOfOtsuri(int otsuriKingaku) throws Exception {
		MoneyStockEntity moneyStock = MoneyStockService.findAll();
		OtsuriAndGoodsDto otsuriAndGoodsDto = new OtsuriAndGoodsDto();
		
		int favoredNumberOf1000Yen = otsuriKingaku / 1000;
		int otsuriKinkagakuExcept1000Yen;
		if (favoredNumberOf1000Yen == 0) {
			otsuriKinkagakuExcept1000Yen = otsuriKingaku;
			otsuriAndGoodsDto.numberOf1000Yen = 0;
		} else if (favoredNumberOf1000Yen <= moneyStock.numberOf1000Yen) {
			otsuriAndGoodsDto.numberOf1000Yen = favoredNumberOf1000Yen;
			otsuriKinkagakuExcept1000Yen = otsuriKingaku - otsuriAndGoodsDto.numberOf1000Yen * 1000;
		} else {
			otsuriAndGoodsDto.numberOf1000Yen = moneyStock.numberOf1000Yen;
			otsuriKinkagakuExcept1000Yen = otsuriKingaku - otsuriAndGoodsDto.numberOf1000Yen * 1000;
		}
		
		int favoredNumberOf500Yen = otsuriKinkagakuExcept1000Yen / 500;
		int otsuriKinkagakuExcept500Yen;
		if (favoredNumberOf500Yen == 0) {
			otsuriKinkagakuExcept500Yen = otsuriKinkagakuExcept1000Yen;
			otsuriAndGoodsDto.numberOf500Yen = favoredNumberOf500Yen;
		} else if (favoredNumberOf500Yen <= moneyStock.numberOf500Yen) {
			otsuriAndGoodsDto.numberOf500Yen = favoredNumberOf500Yen;
			otsuriKinkagakuExcept500Yen = otsuriKinkagakuExcept1000Yen - otsuriAndGoodsDto.numberOf500Yen * 500;
		} else {
			otsuriAndGoodsDto.numberOf500Yen = moneyStock.numberOf500Yen;
			otsuriKinkagakuExcept500Yen = otsuriKinkagakuExcept1000Yen - otsuriAndGoodsDto.numberOf500Yen * 500;
		}
		
		int favoredNumberOf100Yen = otsuriKinkagakuExcept500Yen / 100;
		int otsuriKinkagakuExcept100Yen;
		if (favoredNumberOf100Yen == 0) {
			otsuriKinkagakuExcept100Yen = otsuriKinkagakuExcept500Yen;
			otsuriAndGoodsDto.numberOf100Yen = favoredNumberOf100Yen;
		} else if (favoredNumberOf100Yen <= moneyStock.numberOf100Yen) {
			otsuriAndGoodsDto.numberOf100Yen = otsuriKinkagakuExcept500Yen / 100;
			otsuriKinkagakuExcept100Yen = otsuriKinkagakuExcept500Yen - otsuriAndGoodsDto.numberOf100Yen * 100;
		} else {
			otsuriAndGoodsDto.numberOf100Yen = moneyStock.numberOf100Yen;
			otsuriKinkagakuExcept100Yen = otsuriKinkagakuExcept500Yen - otsuriAndGoodsDto.numberOf100Yen * 100;
		}
		
		int favoredNumberOf50Yen = otsuriKinkagakuExcept100Yen / 50;
		int otsuriKinkagakuExcept50Yen;
		if (favoredNumberOf50Yen == 0) {
			otsuriKinkagakuExcept50Yen = otsuriKinkagakuExcept100Yen;
			otsuriAndGoodsDto.numberOf50Yen = favoredNumberOf50Yen;
		} else if (favoredNumberOf50Yen <= moneyStock.numberOf50Yen) {
			otsuriAndGoodsDto.numberOf50Yen = otsuriKinkagakuExcept100Yen / 50;
			otsuriKinkagakuExcept50Yen = otsuriKinkagakuExcept100Yen - otsuriAndGoodsDto.numberOf50Yen * 50;
		} else {
			otsuriAndGoodsDto.numberOf50Yen = moneyStock.numberOf50Yen;
			otsuriKinkagakuExcept50Yen = otsuriKinkagakuExcept100Yen - otsuriAndGoodsDto.numberOf50Yen * 50;
		}
		
		otsuriAndGoodsDto.numberOf10Yen = otsuriKinkagakuExcept50Yen / 10;
		
		return otsuriAndGoodsDto;
	}
}
