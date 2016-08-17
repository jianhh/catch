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
 * �̼���Ϣbo
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
	 * ��ѯ���̻�����Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public ShopAliSchema getShopAliById(String shopId) throws Exception {
		return this.commodityDAO.getShopAliById(shopId);
	}

	/**
	 * ��ѯ������Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public ShopSchema getShopById(String shopId) throws Exception {
		return this.commodityDAO.getShopById(shopId);
	}

	/**
	 * �޸��̼���Ϣ
	 * 
	 * @param schema
	 *            �̼���Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean updateShopById(ShopSchema schema) throws Exception {
		return this.commodityDAO.updateShopById(schema);
	}

//	/**
//	 * �޸��̼���Ϣ
//	 * 
//	 * @param desc
//	 *            ������Ϣ
//	 * @param shopId
//	 *            ����ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateShopByDesc(String shopId, String desc)
//			throws Exception {
//		return this.commodityDAO.updateShopByDesc(shopId, desc);
//	}

	/**
	 * �޸��̼���Ϣ
	 * 
	 * @param htmlUrl
	 *            ������ҳ��ַ
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean updateShopByUrl(String shopId, String htmlUrl)
			throws Exception {
		return this.commodityDAO.updateShopByUrl(shopId, htmlUrl);
	}

	/**
	 * ���ݵ���id��ѯ
	 * 
	 * @param sellerid
	 *            ����id
	 * @return
	 * @throws Exception
	 */
	public ShopSchema getShopByShopId(String sellerid) throws Exception {
		return this.commodityDAO.getShopByShopId(sellerid);
	}

//	/**
//	 * ���ݹ�˾���Ʋ�ѯ
//	 * 
//	 * @param companyName
//	 *            ��˾����
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopByShopName(String companyName) throws Exception {
//		return this.commodityDAO.getShopByShopName(companyName);
//	}

//	/**
//	 * ���ݵ������Ʋ�ѯ
//	 * 
//	 * @param cShopName
//	 *            ��������
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopBycShopName(String cShopName) throws Exception {
//		return this.commodityDAO.getShopBycShopName(cShopName);
//	}

	/**
	 * �����̼���Ʒ��Ϣ
	 * 
	 * @param schema
	 *            ��Ʒ��Ϣ
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
			logger.debug("�޸ļ۸��С�����������");
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
//	 * �޸��̼���Ʒ��Ϣ
//	 * 
//	 * @param goodsId
//	 *            ��ƷID
//	 * @param defaultPrice
//	 *            Ĭ�ϼ۸�
//	 * @param totalStock
//	 *            �ܿ��
//	 * @param price
//	 *            ����ҳչ�ֵļ۸�
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoods(String goodsId, String defaultPrice,
//			String totalStock, String price) throws Exception {
//		return this.commodityDAO.updateGoods(goodsId, defaultPrice, totalStock,
//				price);
//	}

//	/**
//	 * �޸��̼���Ʒ��Ϣ(�˷ѳе���ʽ)
//	 * 
//	 * @param goodsId
//	 *            ��ƷID
//	 * @param payType
//	 *            �˷ѳе���ʽ��1���ң�2��ң� 3��ȡ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsByPayType(String goodsId, String payType)
//			throws Exception {
//		return this.commodityDAO.updateGoodsByPayType(goodsId, payType);
//	}

//	/**
//	 * �޸��̼���Ʒ��Ϣ
//	 * 
//	 * @param goodsId��ƷID
//	 * @param name��Ʒ����
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsName(String goodsId, String name, String weight)
//			throws Exception {
//		return this.commodityDAO.updateGoodsName(goodsId, name, weight);
//	}

	/**
	 * ���ݵ���ID��ѯ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public List<GoodsSchema> getGoodsByshopId(String shopId) throws Exception {
		return this.commodityDAO.getGoodsByshopId(shopId);
	}

	/**
	 * ���ݵ���ID��ѯ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public GoodsSchema getGoodsSchemaByshopId(String shopId) throws Exception {
		return this.commodityDAO.getGoodsSchemaByshopId(shopId);
	}

	/**
	 * ���ݵ�����ƽ̨��ƷID��ѯ
	 * 
	 * @param shopId
	 *            ����ID
	 * @param cThirdPlatformId
	 *            ������ƽ̨��ƷID
	 * @return
	 * @throws Exception
	 */
	public GoodsSchema getGoodsByformId(String shopId, String cThirdPlatformId)
			throws Exception {
		return this.commodityDAO.getGoodsByformId(shopId, cThirdPlatformId);
	}

//	/**
//	 * ����Ĭ��������
//	 * 
//	 * @param schema
//	 *            Ĭ��������Ϣ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsDefaultExpress(GoodsDefaultExpressSchema schema)
//			throws Exception {
//		return this.commodityDAO.addGoodsDefaultExpress(schema);
//	}

//	/**
//	 * ������Ʒ��չ����
//	 * 
//	 * @param schema
//	 *            ��Ʒ��չ������Ϣ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsExtendsProperty(GoodsExtendsPropertySchema schema)
//			throws Exception {
//		this.commodityDAO.addGoodsExtendsProperty(schema);
//		if (schema.getC_prop_key_label().equals("����")) {// �޸���Ʒ��Ļ���
//			this.commodityDAO.updateGoodsArtNo(schema.getN_goods_id(), schema
//					.getC_prop_value());
//		}
//		return true;
//	}

	/**
	 * ������ƷID��ѯ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @return Map<sku_list_id,skuInfo����>
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
	 * ������ƷID��ѯ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @return Map<sku����,skuId>
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
	 * ������ƷSKU�б���Ϣ
	 * 
	 * @param schema
	 *            ��ƷSKU�б���Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsSkuList(String goodsId, List<GoodsSkuListSchema> skuLists,
			Map<String, String> skuListMap) throws Exception {
		return this.commodityDAO.addGoodsSkuList(goodsId, skuLists, skuListMap);
	}

//	/**
//	 * ������ƷSKU������Ϣ
//	 * 
//	 * @param schema
//	 *            ��ƷSKU������Ϣ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsSkuInfo(GoodsSkuInfoSchema schema) {
//		try {
//			return this.commodityDAO.addGoodsSkuInfo(schema);
//		} catch (Exception e) {
//			logger.error("������ƷSKU������Ϣ�쳣��" + schema.getN_goods_id() + " "
//					+ schema.getN_shop_id() + " ", e);
//		}
//		return false;
//	}

//	/**
//	 * �޸���ƷSKU������Ϣ
//	 * 
//	 * @param stockNum���
//	 * @param price�۸�
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsSkuInfoById(String stockNum, String price,
//			String id) throws Exception {
//		return this.commodityDAO.updateGoodsSkuInfoById(stockNum, price, id);
//	}

	/**
	 * ����������ѯSKU�б���Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @throws Exception
	 */
	public List<GoodsSkuListSchema> getGoodsSkuListBygId(String goodsId, int num) {
		try {
			return this.commodityDAO.getGoodsSkuListBygId(goodsId);
		} catch (Exception e) {
			logger.error("��ѯSKU�б���Ϣ�쳣��" + goodsId, e);
			num += 1;
			return this.getGoodsSkuListBygId(goodsId, num);
		}
	}

//	/**
//	 * ������Ʒ�۸�������Ϣ
//	 * 
//	 * @param schema
//	 *            ��Ʒ�۸�������Ϣ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsPriceSection(GoodsPriceSectionSchema schema)
//			throws Exception {
//		return this.commodityDAO.addGoodsPriceSection(schema);
//	}

//	/**
//	 * 
//	 * ������ƷͼƬ��Ϣ
//	 * 
//	 * @param schema
//	 *            ��ƷͼƬ��Ϣ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsImage(GoodsImageSchema schema) throws Exception {
//		return this.commodityDAO.addGoodsImage(schema);
//	}

	/**
	 * ɾ����Ʒ����Ʒ��ص���Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @throws Exception
	 */
	public void deleteGoods(String shopId) throws Exception {
		this.commodityDAO.deleteGoods(shopId);// ɾ����Ʒ��Ϣ
		this.commodityDAO.deleteGoodsDefaultExpress(shopId);// ɾ��Ĭ��������Ϣ
		this.commodityDAO.deleteGoodsExtendsProperty(shopId);// ɾ����Ʒ��չ������Ϣ
		this.commodityDAO.deleteGoodsImage(shopId);// ɾ����ƷͼƬ��Ϣ
		this.commodityDAO.deleteGoodsPriceSection(shopId);// ɾ����Ʒ�۸�������Ϣ
		this.commodityDAO.deleteGoodsSkuList(shopId);// ɾ����ƷSKU�б���Ϣ
		this.commodityDAO.deleteGoodsSkuInfo(shopId);// ɾ����ƷSKU��ϸ��Ϣ
		this.commodityDAO.deleteGoodsInfoImage(shopId);// ɾ����Ʒ����ͼƬ��Ϣ
	}

	/**
	 * ɾ����Ʒ����Ʒ��ص���Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param shopId
	 *            ����ID
	 * @throws Exception
	 */
	public void deleteGoodsById(String goodsId, String shopId) throws Exception {
		this.commodityDAO.deleteGoodsById(goodsId, shopId);// ɾ����Ʒ��Ϣ
		this.commodityDAO.deleteGoodsDefaultExpressById(goodsId, shopId);// ɾ��Ĭ��������Ϣ
		this.commodityDAO.deleteGoodsExtendsPropertyById(goodsId, shopId);// ɾ����Ʒ��չ������Ϣ
		this.commodityDAO.deleteGoodsImageById(goodsId, shopId);// ɾ����ƷͼƬ��Ϣ
		this.commodityDAO.deleteGoodsPriceSectionById(goodsId, shopId);// ɾ����Ʒ�۸�������Ϣ
		this.commodityDAO.deleteGoodsSkuListById(goodsId, shopId);// ɾ����ƷSKU�б���Ϣ
		this.commodityDAO.deleteGoodsSkuInfoById(goodsId, shopId);// ɾ����ƷSKU��ϸ��Ϣ
		this.commodityDAO.deleteGoodsInfoImageById(goodsId, shopId);// ɾ����Ʒ����ͼƬ��Ϣ
	}

//	/**
//	 * ɾ����Ʒ��չ������Ϣ
//	 * 
//	 * @param goodsId
//	 *            ��ƷID
//	 * @param shopId
//	 *            ����ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean deleteGoodsExtendsPropertyById(String goodsId, String shopId)
//			throws Exception {
//		return this.commodityDAO
//				.deleteGoodsExtendsPropertyById(goodsId, shopId);// ɾ����Ʒ��չ������Ϣ
//	}

	/**
	 * ɾ����Ʒ����ͼƬ��Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsInfoImageById(String goodsId, String shopId)
			throws Exception {
		return this.commodityDAO.deleteGoodsInfoImageById(goodsId, shopId);// ɾ����Ʒ����ͼƬ��Ϣ
	}

//	/**
//	 * ɾ����Ʒ�۸�������Ϣ
//	 * 
//	 * @param goodsId
//	 *            ��ƷID
//	 * @param shopId
//	 *            ����ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean deleteGoodsPriceSectionById(String goodsId, String shopId)
//			throws Exception {
//		return this.commodityDAO.deleteGoodsPriceSectionById(goodsId, shopId);// ɾ����Ʒ�۸�������Ϣ
//	}

//	/**
//	 * ɾ����ƷͼƬ��Ϣ
//	 * 
//	 * @param goodsId
//	 *            ����ID
//	 * @param shopId
//	 *            ����ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean deleteGoodsImageById(String goodsId, String shopId)
//			throws Exception {
//		return this.commodityDAO.deleteGoodsImageById(goodsId, shopId);// ɾ����ƷͼƬ��Ϣ
//	}

	/**
	 * ɾ����Ʒ��Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param shopId
	 *            ����ID
	 * @throws Exception
	 */
	public void deleteGoodsId(String goodsId, String shopId) throws Exception {
		this.commodityDAO.deleteGoodsById(goodsId, shopId);// ɾ����Ʒ��Ϣ
	}

	/**
	 * �����̼һ�����Ϣ
	 * 
	 * @param schema
	 *            �̼һ�����Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addShopAli(String shopId, String url) throws Exception {
		return this.commodityDAO.addShopAli(shopId, url);
	}

	/**
	 * �޸��̼һ�����Ϣ
	 * 
	 * @param schema
	 *            �̼һ�����Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean updateShopAli(ShopAliSchema schema) throws Exception {
		return this.commodityDAO.updateShopAli(schema);
	}

	/**
	 * �޸��̼���Ʒ��Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param thirdTotalSell
	 *            ����
	 * @return
	 * @throws Exception
	 */
	public boolean updateGoodsSell(String goodsId, String thirdTotalSell)
			throws Exception {
		return this.commodityDAO.updateGoodsSell(goodsId, thirdTotalSell);
	}

	/**
	 * 
	 * ������Ʒ����ͼƬ��Ϣ
	 * 
	 * @param schema
	 *            ��Ʒ����ͼƬ��Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsInfoImage(GoodsInfoImageSchema schema)
			throws Exception {
		return this.commodityDAO.addGoodsInfoImage(schema);
	}

	/**
	 * ��ѯ����ͼƬ
	 * 
	 * @param shopId
	 *            ����ID
	 * @param url
	 *            ͼƬ��ַ
	 * @return
	 * @throws Exception
	 */
	public GoodsInfoImageSchema getGoodsInfoImageByUrl(String shopId,
			String imgType, String imgName) throws Exception {
		return this.commodityDAO.getGoodsInfoImageByUrl(shopId, imgType,
				imgName);
	}

	/**
	 * ��ѯ����ͼƬ
	 * 
	 * @param shopId
	 *            ����ID
	 * @param imgType
	 *            ͼƬ����
	 * @return
	 * @throws Exception
	 */
	public List<GoodsInfoImageSchema> getGoodsInfoImageByshopId(String shopId,
			String imgType) throws Exception {
		return this.commodityDAO.getGoodsInfoImageByshopId(shopId, imgType);
	}

	/**
	 * ɾ����Ʒ����ͼƬ��Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @param imgName
	 *            ͼƬ����
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsInfoImageByUrl(String shopId, String imgName)
			throws Exception {
		return this.commodityDAO.deleteGoodsInfoImageByUrl(shopId, imgName);
	}

	/**
	 * ���ӱ�׼��Ʒ��Ŀ��Ϣ
	 * 
	 * @param schema
	 *            ��׼��Ʒ��Ŀ��Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addCfgSysCategorySchema(CfgSysCategorySchema schema)
			throws Exception {
		return this.commodityDAO.addCfgSysCategorySchema(schema);
	}

	/**
	 * ��׼��Ʒ��Ŀ��Ϣ
	 * 
	 * @param catId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public CfgSysCategorySchema getCfgSysCategorySchemaByCatId(String catId)
			throws Exception {
		return this.commodityDAO.getCfgSysCategorySchemaByCatId(catId);
	}

//	/**
//	 * ��׼��Ʒ��Ŀ��Ϣ
//	 * 
//	 * @param pCatId
//	 *            ������ID
//	 * @return
//	 * @throws Exception
//	 */
//	public CfgSysCategorySchema getCfgSysCategorySchemaByPcatId(String pCatId)
//			throws Exception {
//		return this.commodityDAO.getCfgSysCategorySchemaByPcatId(pCatId);
//	}

	/**
	 * ������Ʒ����
	 * 
	 * @param schema
	 *            ��Ʒ������Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsSyncRecord(GoodsSyncRecordSchema schema)
			throws Exception {
		return this.commodityDAO.addGoodsSyncRecord(schema);
	}

	/**
	 * ��Ʒ��������
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getGoodsNewNum(String taskId, int num) throws Exception {
		return this.commodityDAO.getGoodsNewNum(taskId, num);
	}

	/**
	 * ������ȡ����
	 * 
	 * @param schema
	 *            ��ȡ������Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addCatchRecord(CatchRecordSchema schema) throws Exception {
		return this.commodityDAO.addCatchRecord(schema);
	}

	/**
	 * ɾ����ȡ����
	 * 
	 * @param schema
	 *            ��ȡ������Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCatchRecord(String task_id) throws Exception {
		return this.commodityDAO.deleteCatchRecord(task_id);
	}

	/**
	 * ����������ѯ��ȡ������Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @param status
	 *            ����״̬
	 * @throws Exception
	 */
	public List<CatchRecordSchema> getCatchRecordByStatus(String status)
			throws Exception {
		return this.commodityDAO.getCatchRecordByStatus(status);
	}

	/**
	 * ��ѯ���е��̱�
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShopAliSchema> getShopAliSchema() throws Exception {
		return this.commodityDAO.getShopAliSchema();
	}

	/**
	 * ��ѯ����cookie
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CookieSchema> getCookieList() throws Exception {
		return this.commodityDAO.getCookieList();
	}
	
	/**
	 * �޸ļ۸�
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
			if(schema != null){//�޸���Ʒ���еļ۸񣬼�tb_goods
				if(StringUtils.isNotEmpty(schema.getN_buy_price())){
					logger.debug("tb_goods���е�n_buy_price : " + schema.getN_buy_price());
					int buy_price = (int)Double.parseDouble(this.getPirce(schema.getN_buy_price(), keyValue));
					schema.setN_buy_price(buy_price+ "");
				}
				if(StringUtils.isNotEmpty(schema.getN_sell_price())){
					logger.debug("tb_goods���е�n_sell_price : " + schema.getN_sell_price());
					schema.setN_sell_price((int)Double.parseDouble(this.getPirce(schema.getN_sell_price(), keyValue))+ "");
					schema.setC_price(Double.parseDouble(schema.getN_sell_price())/100+"");
				}
			}
			if(skuInfoList != null){//�޸���ƷSKU������еļ۸񣬼�tb_goods_sku_info
				for(GoodsSkuInfoSchema skuInfo : skuInfoList){
					if(skuInfo != null){
						if(StringUtils.isNotEmpty(skuInfo.getN_price()) && Integer.parseInt(skuInfo.getN_price()) != 0){
							logger.debug("tb_goods_sku_info���е�n_price : " + skuInfo.getN_price());
							int buy_price = (int)Double.parseDouble(this.getPirce(skuInfo.getN_price(), keyValue));
							if(buy_price != (int)Double.parseDouble(skuInfo.getN_price())){
								skuInfo.setN_price_set_flag("1");
							}
							skuInfo.setN_price(buy_price+ "");
						}
//						if(StringUtils.isNotEmpty(skuInfo.getN_discount_price())){
//							logger.debug("tb_goods_sku_info���е�n_discount_price() : " + skuInfo.getN_discount_price());
//							skuInfo.setN_discount_price((int)Double.parseDouble(this.getPirce(skuInfo.getN_discount_price(), keyValue))+ "");
//						}
					}
				}
			}
			if(sectionList != null){//�޸���Ʒ�۸��������ñ��еļ۸񣬼�tb_goods_price_section
				for(GoodsPriceSectionSchema section : sectionList){
					if(section != null){
						if(StringUtils.isNotEmpty(section.getN_price()) && Integer.parseInt(section.getN_price()) != 0){
							logger.debug("tb_goods_price_section���е�n_price() : " + section.getN_price());
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
	 * �������������޸ļ۸�
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
				if("1".equals(priceSetMethod) && "1".equals(priceAddOrMinus)){//���۸�����
					price = Double.parseDouble(price) + priceSetValue * 100.00 + "";
				}else if("1".equals(priceSetMethod) && "2".equals(priceAddOrMinus)){//���۸��½�
					if(priceSetValue * 100.00 < Double.parseDouble(price)){
						price = Double.parseDouble(price) - priceSetValue * 100.00 + "";
					}
				}else if("2".equals(priceSetMethod) && "1".equals(priceAddOrMinus)){//����������
					price = Double.parseDouble(price) * ((100.00 + priceSetValue) / 100.00) + "";
				}else if("2".equals(priceSetMethod) && "2".equals(priceAddOrMinus)){//�������½�
					if(priceSetValue < 100.00){
						price = Double.parseDouble(price) * ((100.00 - priceSetValue) / 100.00) + "";
					}
				}
			}
		}
		logger.debug("��ǰ�۸�priceΪ��" + price);
		return price;
	}
}
