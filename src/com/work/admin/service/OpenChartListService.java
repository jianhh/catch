package com.work.admin.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.work.admin.bo.AdminBO;
import com.work.admin.schema.SerieSchema;
import com.work.admin.schema.ShopSerieSchema;

/**
 * 绑定之后图形列表图形列表
 * 
 * @author zhangwentao AND n_shop_type=1 GROUP BY DAY(t_join_time);
 */
public class OpenChartListService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(OpenChartListService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		AdminBO bo = new AdminBO();
		JSONArray jsonarray = null;
		try {
			// 供应商日增长:
			List<ShopSerieSchema> supplier = bo.countOpenAccount();
			List<SerieSchema> series = new ArrayList<SerieSchema>();
			List<Integer> line=new ArrayList<Integer>();
			List<String> day=new ArrayList<String>();
			int i = 0;
			SerieSchema serie = new SerieSchema();
			// 计算一个月供应商留存的数据每周
			for (ShopSerieSchema s : supplier) {
				line.add(Integer.parseInt(s.getNum()));
				day.add(s.getJoin_time());
				i++;
			}
			serie.setName("绑定公众号日增长率");
			//serie.setData(line1);
			//serie.setCategories(day1);
			serie.setLine(line);
			serie.setDay(day);
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
}
