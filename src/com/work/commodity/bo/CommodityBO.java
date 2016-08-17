package com.work.commodity.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.StringUtils;
import com.work.commodity.content.CommodityContent;
import com.work.commodity.dao.CommodityDAO;
import com.work.commodity.schema.CatchRecordSchema;
import com.work.commodity.schema.CfgSysCategorySchema;
import com.work.commodity.schema.CookieSchema;
import com.work.commodity.schema.GoodsDefaultExpressSchema;
import com.work.commodity.schema.GoodsExtendsPropertySchema;
import com.work.commodity.schema.GoodsImageSchema;
import com.work.commodity.schema.GoodsInfoImageSchema;
import com.work.commodity.schema.GoodsPriceSectionSchema;
import com.work.commodity.schema.GoodsSchema;
import com.work.commodity.schema.GoodsSkuInfoSchema;
import com.work.commodity.schema.GoodsSkuListSchema;
import com.work.commodity.schema.GoodsSyncRecordSchema;
import com.work.commodity.schema.ShopAliSchema;
import com.work.commodity.schema.ShopCfgSchema;
import com.work.commodity.schema.ShopSchema;

/**
 * 商家信息bo
 * 
 * @author tangbiao
 * 
 */
public class CommodityBO {

	static JLogger logger = LoggerFactory.getLogger(CommodityBO.class);

	private CommodityDAO commodityDAO;

	public CommodityBO() {

		this.commodityDAO = CommodityDAO.getInstance();
	}

	/**
	 * 查询店铺基本信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public ShopAliSchema getShopAliById(String shopId) throws Exception {
		return this.commodityDAO.getShopAliById(shopId);
	}

	/**
	 * 查询店铺信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public ShopSchema getShopById(String shopId) throws Exception {
		return this.commodityDAO.getShopById(shopId);
	}

	/**
	 * 修改商家信息
	 * 
	 * @param schema
	 *            商家信息
	 * @return
	 * @throws Exception
	 */
	public boolean updateShopById(ShopSchema schema) throws Exception {
		return this.commodityDAO.updateShopById(schema);
	}

//	/**
//	 * 修改商家信息
//	 * 
//	 * @param desc
//	 *            描述信息
//	 * @param shopId
//	 *            店铺ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateShopByDesc(String shopId, String desc)
//			throws Exception {
//		return this.commodityDAO.updateShopByDesc(shopId, desc);
//	}

	/**
	 * 修改商家信息
	 * 
	 * @param htmlUrl
	 *            触店首页地址
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public boolean updateShopByUrl(String shopId, String htmlUrl)
			throws Exception {
		return this.commodityDAO.updateShopByUrl(shopId, htmlUrl);
	}

	/**
	 * 根据店铺id查询
	 * 
	 * @param sellerid
	 *            店铺id
	 * @return
	 * @throws Exception
	 */
	public ShopSchema getShopByShopId(String sellerid) throws Exception {
		return this.commodityDAO.getShopByShopId(sellerid);
	}

//	/**
//	 * 根据公司名称查询
//	 * 
//	 * @param companyName
//	 *            公司名称
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopByShopName(String companyName) throws Exception {
//		return this.commodityDAO.getShopByShopName(companyName);
//	}

//	/**
//	 * 根据店铺名称查询
//	 * 
//	 * @param cShopName
//	 *            店铺名称
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopBycShopName(String cShopName) throws Exception {
//		return this.commodityDAO.getShopBycShopName(cShopName);
//	}

	/**
	 * 增加商家商品信息
	 * 
	 * @param schema
	 *            商品信息
	 * @return
	 * @throws Exception
	 */
	public boolean addGoods(String shopId, GoodsSchema schema) throws Exception {
		if(StringUtils.isNotEmpty(shopId)){
			this.modifyPirce(shopId, schema, null, null);
		}
		return this.commodityDAO.addGoods(schema);
	}

	public boolean addGoodsList(String shopId, GoodsSchema goods,
			GoodsDefaultExpressSchema express,
			List<GoodsExtendsPropertySchema> propertyList,
			List<GoodsSkuInfoSchema> skuInfoList,
			List<GoodsPriceSectionSchema> sectionList,
			List<GoodsImageSchema> imageList,
			List<GoodsInfoImageSchema> infoImageList) throws Exception {
		if(StringUtils.isNotEmpty(shopId)){
			logger.debug("修改价格中。。。。。。");
			this.modifyPirce(shopId, goods, skuInfoList, sectionList);
		}
		return this.commodityDAO.addGoodsList(goods, express, propertyList,
				skuInfoList, sectionList, imageList, infoImageList);
	}

	public boolean updateGoodsList(String shopId, GoodsSchema goods,
			List<GoodsExtendsPropertySchema> propertyList,
			List<GoodsSkuInfoSchema> skuInfoList,
			List<GoodsSkuInfoSchema> skuInfoUpdateList,
			List<GoodsPriceSectionSchema> sectionList,
			List<GoodsImageSchema> imageList,
			List<GoodsInfoImageSchema> infoImageList) throws Exception {
		if(StringUtils.isNotEmpty(shopId)){
			this.modifyPirce(shopId, goods, skuInfoList, sectionList);
			this.modifyPirce(shopId, null, skuInfoUpdateList, null);
		}
		return this.commodityDAO.updateGoodsList(goods, propertyList,
				skuInfoList, skuInfoUpdateList, sectionList, imageList,
				infoImageList);
	}

	public boolean updateGoodsInfoImageList(GoodsSchema goods,
			List<GoodsInfoImageSchema> infoImageList) throws Exception {
		return this.commodityDAO.updateGoodsInfoImageList(goods, infoImageList);
	}

//	/**
//	 * 修改商家商品信息
//	 * 
//	 * @param goodsId
//	 *            商品ID
//	 * @param defaultPrice
//	 *            默认价格
//	 * @param totalStock
//	 *            总库存
//	 * @param price
//	 *            详情页展现的价格
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoods(String goodsId, String defaultPrice,
//			String totalStock, String price) throws Exception {
//		return this.commodityDAO.updateGoods(goodsId, defaultPrice, totalStock,
//				price);
//	}

//	/**
//	 * 修改商家商品信息(运费承担方式)
//	 * 
//	 * @param goodsId
//	 *            商品ID
//	 * @param payType
//	 *            运费承担方式：1卖家，2买家， 3自取
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsByPayType(String goodsId, String payType)
//			throws Exception {
//		return this.commodityDAO.updateGoodsByPayType(goodsId, payType);
//	}

//	/**
//	 * 修改商家商品信息
//	 * 
//	 * @param goodsId商品ID
//	 * @param name商品名称
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsName(String goodsId, String name, String weight)
//			throws Exception {
//		return this.commodityDAO.updateGoodsName(goodsId, name, weight);
//	}

	/**
	 * 根据店铺ID查询
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public List<GoodsSchema> getGoodsByshopId(String shopId) throws Exception {
		return this.commodityDAO.getGoodsByshopId(shopId);
	}

	/**
	 * 根据店铺ID查询
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public GoodsSchema getGoodsSchemaByshopId(String shopId) throws Exception {
		return this.commodityDAO.getGoodsSchemaByshopId(shopId);
	}

	/**
	 * 根据第三方平台商品ID查询
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param cThirdPlatformId
	 *            第三方平台商品ID
	 * @return
	 * @throws Exception
	 */
	public GoodsSchema getGoodsByformId(String shopId, String cThirdPlatformId)
			throws Exception {
		return this.commodityDAO.getGoodsByformId(shopId, cThirdPlatformId);
	}

//	/**
//	 * 增加默认物流表
//	 * 
//	 * @param schema
//	 *            默认物流信息
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsDefaultExpress(GoodsDefaultExpressSchema schema)
//			throws Exception {
//		return this.commodityDAO.addGoodsDefaultExpress(schema);
//	}

//	/**
//	 * 增加商品扩展属性
//	 * 
//	 * @param schema
//	 *            商品扩展属性信息
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsExtendsProperty(GoodsExtendsPropertySchema schema)
//			throws Exception {
//		this.commodityDAO.addGoodsExtendsProperty(schema);
//		if (schema.getC_prop_key_label().equals("货号")) {// 修改商品表的货号
//			this.commodityDAO.updateGoodsArtNo(schema.getN_goods_id(), schema
//					.getC_prop_value());
//		}
//		return true;
//	}

	/**
	 * 根据商品ID查询
	 * 
	 * @param goodsId
	 *            商品ID
	 * @return Map<sku_list_id,skuInfo对象>
	 * @throws Exception
	 */
	public Map<String, GoodsSkuInfoSchema> getGoodsSkuInfoBygoodsId(
			String goodsId) throws Exception {
		Map<String, GoodsSkuInfoSchema> map = new HashMap<String, GoodsSkuInfoSchema>();
		List<GoodsSkuInfoSchema> list = this.commodityDAO
				.getGoodsSkuInfoBygoodsId(goodsId);
		for (GoodsSkuInfoSchema skuInfo : list) {
			map.put(skuInfo.getC_sku_list_id(), skuInfo);
		}
		return map;
	}

	/**
	 * 根据商品ID查询
	 * 
	 * @param goodsId
	 *            商品ID
	 * @return Map<sku名称,skuId>
	 * @throws Exception
	 */
	public Map<String, String> getGoodsSkuListBygoodsId(String goodsId)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		List<GoodsSkuListSchema> list = this.commodityDAO
				.getGoodsSkuListBygId(goodsId);
		for (GoodsSkuListSchema skuList : list) {
			map.put(skuList.getC_sku_name(), skuList.getN_id());
		}
		return map;
	}

	/**
	 * 增加商品SKU列表信息
	 * 
	 * @param schema
	 *            商品SKU列表信息
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsSkuList(String goodsId, List<GoodsSkuListSchema> skuLists,
			Map<String, String> skuListMap) throws Exception {
		return this.commodityDAO.addGoodsSkuList(goodsId, skuLists, skuListMap);
	}

//	/**
//	 * 增加商品SKU详情信息
//	 * 
//	 * @param schema
//	 *            商品SKU详情信息
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsSkuInfo(GoodsSkuInfoSchema schema) {
//		try {
//			return this.commodityDAO.addGoodsSkuInfo(schema);
//		} catch (Exception e) {
//			logger.error("增加商品SKU详情信息异常：" + schema.getN_goods_id() + " "
//					+ schema.getN_shop_id() + " ", e);
//		}
//		return false;
//	}

//	/**
//	 * 修改商品SKU详情信息
//	 * 
//	 * @param stockNum库存
//	 * @param price价格
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsSkuInfoById(String stockNum, String price,
//			String id) throws Exception {
//		return this.commodityDAO.updateGoodsSkuInfoById(stockNum, price, id);
//	}

	/**
	 * 根据条件查询SKU列表信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @throws Exception
	 */
	public List<GoodsSkuListSchema> getGoodsSkuListBygId(String goodsId, int num) {
		try {
			return this.commodityDAO.getGoodsSkuListBygId(goodsId);
		} catch (Exception e) {
			logger.error("查询SKU列表信息异常：" + goodsId, e);
			num += 1;
			return this.getGoodsSkuListBygId(goodsId, num);
		}
	}

//	/**
//	 * 增加商品价格区间信息
//	 * 
//	 * @param schema
//	 *            商品价格区间信息
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsPriceSection(GoodsPriceSectionSchema schema)
//			throws Exception {
//		return this.commodityDAO.addGoodsPriceSection(schema);
//	}

//	/**
//	 * 
//	 * 增加商品图片信息
//	 * 
//	 * @param schema
//	 *            商品图片信息
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsImage(GoodsImageSchema schema) throws Exception {
//		return this.commodityDAO.addGoodsImage(schema);
//	}

	/**
	 * 删除商品及商品相关的信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @throws Exception
	 */
	public void deleteGoods(String shopId) throws Exception {
		this.commodityDAO.deleteGoods(shopId);// 删除商品信息
		this.commodityDAO.deleteGoodsDefaultExpress(shopId);// 删除默认物流信息
		this.commodityDAO.deleteGoodsExtendsProperty(shopId);// 删除商品扩展属性信息
		this.commodityDAO.deleteGoodsImage(shopId);// 删除商品图片信息
		this.commodityDAO.deleteGoodsPriceSection(shopId);// 删除商品价格区间信息
		this.commodityDAO.deleteGoodsSkuList(shopId);// 删除商品SKU列表信息
		this.commodityDAO.deleteGoodsSkuInfo(shopId);// 删除商品SKU详细信息
		this.commodityDAO.deleteGoodsInfoImage(shopId);// 删除商品详情图片信息
	}

	/**
	 * 删除商品及商品相关的信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
	 * @throws Exception
	 */
	public void deleteGoodsById(String goodsId, String shopId) throws Exception {
		this.commodityDAO.deleteGoodsById(goodsId, shopId);// 删除商品信息
		this.commodityDAO.deleteGoodsDefaultExpressById(goodsId, shopId);// 删除默认物流信息
		this.commodityDAO.deleteGoodsExtendsPropertyById(goodsId, shopId);// 删除商品扩展属性信息
		this.commodityDAO.deleteGoodsImageById(goodsId, shopId);// 删除商品图片信息
		this.commodityDAO.deleteGoodsPriceSectionById(goodsId, shopId);// 删除商品价格区间信息
		this.commodityDAO.deleteGoodsSkuListById(goodsId, shopId);// 删除商品SKU列表信息
		this.commodityDAO.deleteGoodsSkuInfoById(goodsId, shopId);// 删除商品SKU详细信息
		this.commodityDAO.deleteGoodsInfoImageById(goodsId, shopId);// 删除商品详情图片信息
	}

//	/**
//	 * 删除商品扩展属性信息
//	 * 
//	 * @param goodsId
//	 *            商品ID
//	 * @param shopId
//	 *            店铺ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean deleteGoodsExtendsPropertyById(String goodsId, String shopId)
//			throws Exception {
//		return this.commodityDAO
//				.deleteGoodsExtendsPropertyById(goodsId, shopId);// 删除商品扩展属性信息
//	}

	/**
	 * 删除商品详情图片信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsInfoImageById(String goodsId, String shopId)
			throws Exception {
		return this.commodityDAO.deleteGoodsInfoImageById(goodsId, shopId);// 删除商品详情图片信息
	}

//	/**
//	 * 删除商品价格区间信息
//	 * 
//	 * @param goodsId
//	 *            商品ID
//	 * @param shopId
//	 *            店铺ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean deleteGoodsPriceSectionById(String goodsId, String shopId)
//			throws Exception {
//		return this.commodityDAO.deleteGoodsPriceSectionById(goodsId, shopId);// 删除商品价格区间信息
//	}

//	/**
//	 * 删除商品图片信息
//	 * 
//	 * @param goodsId
//	 *            卖家ID
//	 * @param shopId
//	 *            店铺ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean deleteGoodsImageById(String goodsId, String shopId)
//			throws Exception {
//		return this.commodityDAO.deleteGoodsImageById(goodsId, shopId);// 删除商品图片信息
//	}

	/**
	 * 删除商品信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
	 * @throws Exception
	 */
	public void deleteGoodsId(String goodsId, String shopId) throws Exception {
		this.commodityDAO.deleteGoodsById(goodsId, shopId);// 删除商品信息
	}

	/**
	 * 增加商家基本信息
	 * 
	 * @param schema
	 *            商家基本信息
	 * @return
	 * @throws Exception
	 */
	public boolean addShopAli(String shopId, String url) throws Exception {
		return this.commodityDAO.addShopAli(shopId, url);
	}

	/**
	 * 修改商家基本信息
	 * 
	 * @param schema
	 *            商家基本信息
	 * @return
	 * @throws Exception
	 */
	public boolean updateShopAli(ShopAliSchema schema) throws Exception {
		return this.commodityDAO.updateShopAli(schema);
	}

	/**
	 * 修改商家商品信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param thirdTotalSell
	 *            销量
	 * @return
	 * @throws Exception
	 */
	public boolean updateGoodsSell(String goodsId, String thirdTotalSell)
			throws Exception {
		return this.commodityDAO.updateGoodsSell(goodsId, thirdTotalSell);
	}

	/**
	 * 
	 * 增加商品详情图片信息
	 * 
	 * @param schema
	 *            商品详情图片信息
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsInfoImage(GoodsInfoImageSchema schema)
			throws Exception {
		return this.commodityDAO.addGoodsInfoImage(schema);
	}

	/**
	 * 查询详情图片
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param url
	 *            图片地址
	 * @return
	 * @throws Exception
	 */
	public GoodsInfoImageSchema getGoodsInfoImageByUrl(String shopId,
			String imgType, String imgName) throws Exception {
		return this.commodityDAO.getGoodsInfoImageByUrl(shopId, imgType,
				imgName);
	}

	/**
	 * 查询详情图片
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param imgType
	 *            图片类型
	 * @return
	 * @throws Exception
	 */
	public List<GoodsInfoImageSchema> getGoodsInfoImageByshopId(String shopId,
			String imgType) throws Exception {
		return this.commodityDAO.getGoodsInfoImageByshopId(shopId, imgType);
	}

	/**
	 * 删除商品详情图片信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @param imgName
	 *            图片名称
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsInfoImageByUrl(String shopId, String imgName)
			throws Exception {
		return this.commodityDAO.deleteGoodsInfoImageByUrl(shopId, imgName);
	}

	/**
	 * 增加标准商品类目信息
	 * 
	 * @param schema
	 *            标准商品类目信息
	 * @return
	 * @throws Exception
	 */
	public boolean addCfgSysCategorySchema(CfgSysCategorySchema schema)
			throws Exception {
		return this.commodityDAO.addCfgSysCategorySchema(schema);
	}

	/**
	 * 标准商品类目信息
	 * 
	 * @param catId
	 *            分类ID
	 * @return
	 * @throws Exception
	 */
	public CfgSysCategorySchema getCfgSysCategorySchemaByCatId(String catId)
			throws Exception {
		return this.commodityDAO.getCfgSysCategorySchemaByCatId(catId);
	}

//	/**
//	 * 标准商品类目信息
//	 * 
//	 * @param pCatId
//	 *            父分类ID
//	 * @return
//	 * @throws Exception
//	 */
//	public CfgSysCategorySchema getCfgSysCategorySchemaByPcatId(String pCatId)
//			throws Exception {
//		return this.commodityDAO.getCfgSysCategorySchemaByPcatId(pCatId);
//	}

	/**
	 * 增加商品上新
	 * 
	 * @param schema
	 *            商品上新信息
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsSyncRecord(GoodsSyncRecordSchema schema)
			throws Exception {
		return this.commodityDAO.addGoodsSyncRecord(schema);
	}

	/**
	 * 商品上新总数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getGoodsNewNum(String taskId, int num) throws Exception {
		return this.commodityDAO.getGoodsNewNum(taskId, num);
	}

	/**
	 * 增加爬取任务
	 * 
	 * @param schema
	 *            爬取任务信息
	 * @return
	 * @throws Exception
	 */
	public boolean addCatchRecord(CatchRecordSchema schema) throws Exception {
		return this.commodityDAO.addCatchRecord(schema);
	}

	/**
	 * 删除爬取任务
	 * 
	 * @param schema
	 *            爬取任务信息
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCatchRecord(String task_id) throws Exception {
		return this.commodityDAO.deleteCatchRecord(task_id);
	}

	/**
	 * 根据条件查询爬取任务信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param status
	 *            任务状态
	 * @throws Exception
	 */
	public List<CatchRecordSchema> getCatchRecordByStatus(String status)
			throws Exception {
		return this.commodityDAO.getCatchRecordByStatus(status);
	}

	/**
	 * 查询所有店铺表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShopAliSchema> getShopAliSchema() throws Exception {
		return this.commodityDAO.getShopAliSchema();
	}

	/**
	 * 查询所有cookie
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CookieSchema> getCookieList() throws Exception {
		return this.commodityDAO.getCookieList();
	}
	
	/**
	 * 修改价格
	 * @param shopId
	 * @param schema
	 * @param skuInfoList
	 * @param sectionList
	 * @throws Exception
	 */
	private void modifyPirce(String shopId, GoodsSchema schema, List<GoodsSkuInfoSchema> skuInfoList, 
			List<GoodsPriceSectionSchema> sectionList) throws Exception{
		List<ShopCfgSchema> shopCfgList = this.commodityDAO.getShopCfgList(shopId);
		if(shopCfgList != null){
			Map<String, String> keyValue = new HashMap<String, String>();
			for(ShopCfgSchema shopCfg : shopCfgList){
				if(shopCfg != null){
					keyValue.put(shopCfg.getC_key(), shopCfg.getC_value());
				}
			}
			if(schema != null){//修改商品表中的价格，即tb_goods
				if(StringUtils.isNotEmpty(schema.getN_buy_price())){
					logger.debug("tb_goods表中的n_buy_price : " + schema.getN_buy_price());
					int buy_price = (int)Double.parseDouble(this.getPirce(schema.getN_buy_price(), keyValue));
					schema.setN_buy_price(buy_price+ "");
				}
				if(StringUtils.isNotEmpty(schema.getN_sell_price())){
					logger.debug("tb_goods表中的n_sell_price : " + schema.getN_sell_price());
					schema.setN_sell_price((int)Double.parseDouble(this.getPirce(schema.getN_sell_price(), keyValue))+ "");
					schema.setC_price(Double.parseDouble(schema.getN_sell_price())/100+"");
				}
			}
			if(skuInfoList != null){//修改商品SKU详情表中的价格，即tb_goods_sku_info
				for(GoodsSkuInfoSchema skuInfo : skuInfoList){
					if(skuInfo != null){
						if(StringUtils.isNotEmpty(skuInfo.getN_price()) && Integer.parseInt(skuInfo.getN_price()) != 0){
							logger.debug("tb_goods_sku_info表中的n_price : " + skuInfo.getN_price());
							int buy_price = (int)Double.parseDouble(this.getPirce(skuInfo.getN_price(), keyValue));
							if(buy_price != (int)Double.parseDouble(skuInfo.getN_price())){
								skuInfo.setN_price_set_flag("1");
							}
							skuInfo.setN_price(buy_price+ "");
						}
//						if(StringUtils.isNotEmpty(skuInfo.getN_discount_price())){
//							logger.debug("tb_goods_sku_info表中的n_discount_price() : " + skuInfo.getN_discount_price());
//							skuInfo.setN_discount_price((int)Double.parseDouble(this.getPirce(skuInfo.getN_discount_price(), keyValue))+ "");
//						}
					}
				}
			}
			if(sectionList != null){//修改商品价格区间配置表中的价格，即tb_goods_price_section
				for(GoodsPriceSectionSchema section : sectionList){
					if(section != null){
						if(StringUtils.isNotEmpty(section.getN_price()) && Integer.parseInt(section.getN_price()) != 0){
							logger.debug("tb_goods_price_section表中的n_price() : " + section.getN_price());
							int buy_price = (int)Double.parseDouble(this.getPirce(section.getN_price(), keyValue));
							if(buy_price != (int)Double.parseDouble(section.getN_price())){
								section.setN_price_set_flag("1");
							}
							section.setN_price(buy_price+ "");
						}
					}
				}
			}
		}
	}
	
	/**
	 * 根据配置条件修改价格
	 * @param price
	 * @param keyValue
	 * @return
	 */
	private String getPirce(String price, Map<String, String> keyValue){
		logger.debug(keyValue.containsKey(CommodityContent.KEY_PREFIX_PRICE_ALITAO+"_"+CommodityContent.IS_AUTO_PAY));
		if(keyValue.containsKey(CommodityContent.KEY_PREFIX_PRICE_ALITAO+"_"+CommodityContent.IS_AUTO_PAY) &&
				"1".equals(keyValue.get(CommodityContent.KEY_PREFIX_PRICE_ALITAO+"_"+CommodityContent.IS_AUTO_PAY))){
			if(keyValue.containsKey(CommodityContent.KEY_PREFIX_PRICE_ALITAO+"_"+CommodityContent.KEY_PRICE_SET_METHOD) &&
					keyValue.containsKey(CommodityContent.KEY_PREFIX_PRICE_ALITAO+"_"+CommodityContent.KEY_PRICE_ADD_OR_MINUS) &&
					keyValue.containsKey(CommodityContent.KEY_PREFIX_PRICE_ALITAO+"_"+CommodityContent.KEY_PRICE_SET_VALUE)){
				String priceSetMethod = keyValue.get(CommodityContent.KEY_PREFIX_PRICE_ALITAO+"_"+CommodityContent.KEY_PRICE_SET_METHOD);
				String priceAddOrMinus = keyValue.get(CommodityContent.KEY_PREFIX_PRICE_ALITAO+"_"+CommodityContent.KEY_PRICE_ADD_OR_MINUS);
				Double priceSetValue = Double.parseDouble(keyValue.get(CommodityContent.KEY_PREFIX_PRICE_ALITAO+"_"+CommodityContent.KEY_PRICE_SET_VALUE));
				if("1".equals(priceSetMethod) && "1".equals(priceAddOrMinus)){//按价格上涨
					price = Double.parseDouble(price) + priceSetValue * 100.00 + "";
				}else if("1".equals(priceSetMethod) && "2".equals(priceAddOrMinus)){//按价格下降
					if(priceSetValue * 100.00 < Double.parseDouble(price)){
						price = Double.parseDouble(price) - priceSetValue * 100.00 + "";
					}
				}else if("2".equals(priceSetMethod) && "1".equals(priceAddOrMinus)){//按比例上涨
					price = Double.parseDouble(price) * ((100.00 + priceSetValue) / 100.00) + "";
				}else if("2".equals(priceSetMethod) && "2".equals(priceAddOrMinus)){//按比例下降
					if(priceSetValue < 100.00){
						price = Double.parseDouble(price) * ((100.00 - priceSetValue) / 100.00) + "";
					}
				}
			}
		}
		logger.debug("当前价格price为：" + price);
		return price;
	}
}
