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
import com.work.admin.bo.AdminBO;
import com.work.admin.content.AdminContent;
import com.work.admin.schema.SerieSchema;

/**
 * 图形列表
 * 
 * @author zhangwentao
 * AND n_shop_type=1 GROUP BY DAY(t_join_time);
 */
public class ChartListService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(ChartListService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		StringBuilder json = new StringBuilder();  
		AdminBO bo = new AdminBO();
		String jsonStr = AdminContent.S_ZORE;
		//供应商周留存率:
		String retentionG1="0";
		String retentionG2="0";
		String retentionG3="0";
		String retentionG4="0";
		//分销商周留存率:
		String retentionF1="0";
		String retentionF2="0";
		String retentionF3="0";
		String retentionF4="0";
		try {
			//获取一个月供应商留存的数据每周
			retentionG1=bo.countRetentionG("1");
			retentionG2=bo.countRetentionG("2");
			retentionG3=bo.countRetentionG("3");
			retentionG4=bo.countRetentionG("4");
			//获取一个月分销商留存的数据每周
			retentionF1=bo.countRetentionF("1");
			retentionF2=bo.countRetentionF("2");
			retentionF3=bo.countRetentionF("3");
			retentionF4=bo.countRetentionF("4");
		} catch (Exception e) {
			logger.error("图形列表错误:", e);
			e.printStackTrace();
		}
		List<SerieSchema> series = new ArrayList<SerieSchema>();
		float line[]={1,1,1,1};;
		 SerieSchema serie = new SerieSchema();  
		 line[0]=gets(retentionG1);
		 line[1]=gets(retentionG2);
		 line[2]=gets(retentionG3);
		 line[3]=gets(retentionG4);
		 serie.setName("供应商的留存率");
		 serie.setData(line);
		 series.add(serie);
		 float lin[]={1,1,1,1};
		 serie = new SerieSchema();  
		 lin[0]=gets(retentionF1);
		 lin[1]=gets(retentionF2);
		 lin[2]=gets(retentionF3);
		 lin[3]=gets(retentionF4);
		 serie.setName("分销商的留存率");
		 serie.setData(lin);
		 series.add(serie);
		JSONArray  jsonarray = JSONArray .fromObject(series);
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonarray.toString());
		response.setForwardId("success");
		
	}
	public  float  gets(String retention){
	    DecimalFormat df = new DecimalFormat("0.00");
		retention = df.format(Float.parseFloat(retention)*100);
		return Float.parseFloat(retention);
	}
}
