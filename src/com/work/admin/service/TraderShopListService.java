package com.work.admin.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.util.StringUtils;
import com.work.admin.bo.AdminBO;
import com.work.admin.content.AdminContent;
import com.work.admin.schema.SerieSchema;
import com.work.admin.schema.ShopSerieSchema;

/**
 * 分销商/供应商图形列表
 * 
 * @author zhangwentao AND n_shop_type=1 GROUP BY DAY(t_join_time);
 */
public class TraderShopListService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(TraderShopListService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		StringBuilder json = new StringBuilder();
		AdminBO bo = new AdminBO();
		JSONArray jsonarray = null;
		try {
			String shopId = request.getParameter("shopId");
			// 供应商的分销商周留存率:AND tb_trader_mapping.n_parent_shop_id = a.n_shop_id
			// 总分销商数
			// 活跃用户
			String traderActive1 = bo.countTraderActive("1",shopId);
			String allTrader1 = bo.countAllTrader("1",shopId);
			String traderActive2 = bo.countTraderActive("2",shopId);
			String allTrader2 = bo.countAllTrader("2",shopId);
			String traderActive3 = bo.countTraderActive("3",shopId);
			String allTrader3 = bo.countAllTrader("3",shopId);
			String traderActive4 = bo.countTraderActive("4",shopId);
			String allTrader4 = bo.countAllTrader("4",shopId);
			List<SerieSchema> series = new ArrayList<SerieSchema>();
			float[]  line = new float[4];
			SerieSchema serie = new SerieSchema();
			line[3]=getUp(traderActive1, allTrader1);
			line[2]=getUp(traderActive2, allTrader2);
			line[1]=getUp(traderActive3, allTrader3);
			line[0]=getUp(traderActive4, allTrader4);
			serie.setName("供应商的分销商周留存率");
			serie.setData(line);
			series.add(serie);
			serie = new SerieSchema();
			jsonarray = JSONArray.fromObject(series);
		} catch (Exception e) {
			logger.error("图形列表错误:", e);
			e.printStackTrace();
		}
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonarray.toString());
		System.out.println("json.toString():" + jsonarray.toString());
		response.setForwardId("success");

	}
	public float getUp(String traderActive, String allTrader) {
		float retention =0;
		float f_retention = 0;
		float all_retention = 0;
		 DecimalFormat df = new DecimalFormat("0.00");
		if (StringUtils.isNotEmpty(traderActive)) {
			f_retention =Float.parseFloat(traderActive);
		}
		if (StringUtils.isNotEmpty(allTrader)) {
			all_retention =Float.parseFloat(allTrader);
		}

		if (f_retention == 0 || all_retention == 0) {
		} else {
			retention = (f_retention * 100 / all_retention);
		}
		return Float.parseFloat(df.format(retention));
	}
}
