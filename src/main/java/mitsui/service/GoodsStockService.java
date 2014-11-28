package mitsui.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mitsui.path.FilePath;
import mitsui.util.FileUtil;

public class GoodsStockService {
	
	public static Map<String, String> findAll() throws Exception {
		
		File goodsStockFile = new File(FilePath.GODDS_STOCK_FILE);
		
		if (!FileUtil.canReadFile(goodsStockFile)) {
			throw new Exception("ファイルが存在しない、または読み込めません。");
		}
		
		String header = FileUtil.readFile(goodsStockFile).get(0);
		String[] goodsIdArray = header.split(",");
		
		String data = FileUtil.readFile(goodsStockFile).get(1);
		String[] goodsStockArray = data.split(",");
		
		Map<String, String> goodsStockMap = new HashMap<String, String>();
		for (int i = 0; i < goodsIdArray.length; i++) {
			goodsStockMap.put(goodsIdArray[i], goodsStockArray[i]);
		}
		
		return goodsStockMap;
	}
	
	public static void update(Map<String, String> goodsStockMap) throws Exception {
		if (goodsStockMap == null) {
			return;
		}
		
		File goodsStockFile = new File(FilePath.GODDS_STOCK_FILE);
		
		StringBuilder headerBuilder = new StringBuilder();
		StringBuilder dataBuilder = new StringBuilder();
		
		headerBuilder.append("1");
		dataBuilder.append(goodsStockMap.get("1"));
		
		for (int i = 2; i <= goodsStockMap.size(); i++) {
			headerBuilder.append(",").append(Integer.toString(i));
			dataBuilder.append(",").append(goodsStockMap.get(Integer.toString(i)));
		}
		if (!FileUtil.canWriteFile(goodsStockFile)) {
			throw new Exception("ファイルが存在しない、または書き込めません。");
		}
		FileUtil.writeFile(goodsStockFile, headerBuilder.toString(), dataBuilder.toString());
	}
}