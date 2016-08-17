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
import com.work.admin.schema.SerieSchema;
import com.work.admin.schema.ShopSerieSchema;

/**
 * ������/��Ӧ��ͼ���б�
 * 
 * @author zhangwentao AND n_shop_type=1 GROUP BY DAY(t_join_time);
 */
public class ShopChartListService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(ShopChartListService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		AdminBO bo = new AdminBO();
		JSONArray jsonarray = null;
		try {
			// ��Ӧ��������:
			List<ShopSerieSchema> supplier = bo.countMDaySupplier("1");
			// ������������:
			List<ShopSerieSchema> trader = bo.countMDaySupplier("3");
			List<SerieSchema> series = new ArrayList<SerieSchema>();
			List<Integer> line1=new ArrayList<Integer>();
			List<String> day1=new ArrayList<String>();
			List<Integer> line2=new ArrayList<Integer>();
			List<String> day2=new ArrayList<String>();
			int i = 0;
			int j = 0;
			SerieSchema serie = new SerieSchema();
			// ����һ���¹�Ӧ�����������ÿ��
			for (ShopSerieSchema s : supplier) {
				line1.add(Integer.parseInt(s.getNum()));
				day1.add(s.getJoin_time());
				i++;
			}
			serie.setName("��Ӧ�̵���������");
			serie.setLine(line1);
			serie.setDay(day1);
			series.add(serie);
			serie = new SerieSchema();
			// ����һ���¹�Ӧ�����������ÿ��
			for (ShopSerieSchema ss : trader) {
				line2.add(Integer.parseInt(ss.getNum()));
				day2.add(ss.getJoin_time());
				j++;
			}
			serie.setName("�����̵���������");
			serie.setLine(line2);
			serie.setDay(day2);
			series.add(serie);
			jsonarray = JSONArray.fromObject(series);
		} catch (Exception e) {
			logger.error("ͼ���б����:", e);
			e.printStackTrace();
		}
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonarray.toString());
		response.setForwardId("success");

	}

	public String gets(String retention) {
		DecimalFormat df = new DecimalFormat("0.00");
		retention = df.format(Float.parseFloat(retention) * 100);
		if (retention.equals("0.0") || retention.equals("1")) {
			retention = "0";
		}
		return retention;
	}

	public float getUp(String sum, String total) {
		float retention =0;
		float f_retention = 0;
		float all_retention = 0;
		 DecimalFormat df = new DecimalFormat("0.00");
		if (StringUtils.isNotEmpty(sum)) {
			f_retention =Float.parseFloat(sum);
		}
		if (StringUtils.isNotEmpty(total)) {
			all_retention =Float.parseFloat(total);
		}

		if (f_retention == 0 || all_retention == 0) {
		} else {
			retention = (f_retention * 100 / all_retention);
		}
		return Float.parseFloat(df.format(retention));
	}
}
