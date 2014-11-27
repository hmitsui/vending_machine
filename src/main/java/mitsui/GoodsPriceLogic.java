package mitsui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GoodsPriceLogic {
	
	public static Map<String, String> findAll() throws Exception {
		
		File goodsPriceFile = new File(FilePath.GOODS_PRICE_FILE);
		
		if (!FileUtil.canReadFile(goodsPriceFile)) {
			throw new Exception("ファイルが存在しない、または読み込めません。");
		}
		
		String header = FileUtil.readFile(goodsPriceFile).get(0);
		String[] goodsIdArray = header.split(",");
		
		String data = FileUtil.readFile(goodsPriceFile).get(1);
		String[] goodsPriceArray = data.split(",");
		
		Map<String, String> goodsPriceMap = new HashMap<String, String>();
		for (int i = 0; i < goodsIdArray.length; i++) {
			goodsPriceMap.put(goodsIdArray[i], goodsPriceArray[i]);
		}
		
		return goodsPriceMap;
	}
	
	public static int findPriceById(int goodsId) throws Exception {
		return Integer.parseInt(findAll().get(Integer.toString(goodsId)));
	}
}