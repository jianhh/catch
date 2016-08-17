package com.common.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.common.db.ConnectionManager;
import com.common.db.SQLCode;
import com.common.log.newproxy.LogHelper;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.log.LogUtil;
import com.framework.util.StartContent;
import com.work.commodity.bo.CommodityBO;
import com.work.commodity.schema.CatchRecordSchema;
import com.work.commodity.schema.CookieSchema;
import com.work.commodity.util.CatchRecordTake;
import com.work.taobao.util.TBCatchRecordTake;
import com.work.util.JsoupUtil;

/**
 * 系统启动Servlet
 * 
 * @author tangbiao
 * 
 */

public class StartupServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private JLogger log = LoggerFactory.getLogger(StartupServlet.class);

	public final static String FS = System.getProperty("file.separator");

	protected String home = "";

	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		// todo.
		/** **系统名称***** */
		String systemName = config.getInitParameter("systemName");

		log.debug("SYSTEM is:" + systemName);

		ServerInfo.systemName = systemName;

		String path = null;
		File file = new File(".");
		try {
			path = file.getCanonicalPath();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error(ex);
		}

		home = path + FS + "servers" + FS + systemName;

		log.debug("home dir=" + home);
		ServerInfo.appRootPath = home;

		LogHelper.setLogRootPath(ServerInfo.appRootPath);

		// 加载系统配置项
		// this.initConfigFile(config, home);

		/** **初始化日志系统***** */
		// logConfig
		String logConfig = config.getInitParameter("logConfig");
		String errFile = home + config.getInitParameter("errmsg-config");
		LoggerFactory.loadErrorInfo(errFile);
		logConfig = home + logConfig;
		// 根据操作系统不同修改操作系统的目录符号
		/** logRefresh * */
		String logRefreshTime = config.getInitParameter("logRefresh");
		if (logRefreshTime == null)
			logRefreshTime = "60";
		LoggerFactory.configLog(logConfig, logRefreshTime, home);

		log.debug("Init log config finished!");

		String startFile = config.getInitParameter("start-config");
		startFile = home + startFile;
		String persistenceFile = config.getInitParameter("persistence-config");
		persistenceFile = home + persistenceFile;
		String sqlFile = config.getInitParameter("sql-config");
		sqlFile = home + sqlFile;
		CommodityBO bo = new CommodityBO();
		try {
			// DB.getInstance().init(persistenceFile, sqlFile); huangqinfang
			ConnectionManager.getInstance().init(persistenceFile);
			SQLCode.getInstance().init(sqlFile);
			StartContent.getInstance().init(startFile);

			log.debug("初始化cookie数据");
			List<CookieSchema> cookieList = bo.getCookieList();
			for (CookieSchema cookie : cookieList) {
				JsoupUtil.listCookie.add(cookie.getCookie());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

		log.debug("启动OK！");
		if (StartContent.getInstance().getDomainUrl()
				.equals("www.truedian.com")) {// 现网才需要做续爬
			try {
				List<CatchRecordSchema> catchRecordList = bo
						.getCatchRecordByStatus("0");// 查询任务状态为0（爬取开始），未完成的数据
				for (CatchRecordSchema schema : catchRecordList) {
					LogUtil
							.writeCommodityLog("开线程检查商品没爬取完的数据CatchRecordTake()...........start ");
					ThreadPoolExecutor executor = com.framework.base.ThreadPool
							.getInstance().getThread();
					if (schema.getC_url().indexOf("taobao.com")>-1){
						executor.execute(new TBCatchRecordTake(schema.getN_shop_id(),
								schema.getC_url(), schema.getN_sync_type(), schema
										.getN_task_id()));
					}else{
						executor.execute(new CatchRecordTake(schema.getN_shop_id(),
								schema.getC_url(), schema.getN_sync_type(), schema
										.getN_task_id()));
					}

					LogUtil
							.writeCommodityLog("开线程检查商品没爬取完的数据CatchRecordTake()...........end ");
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	/**
	 * @param config
	 *            ServletConfig
	 */
	private void initConfigFile(ServletConfig config, String home) {

		/** **系统配置文件***** */
		// System.out.print("init system config...") ;
		String configFileName = config.getInitParameter("configFile");

		String configFile = configFileName.replace('/', FS.charAt(0));
		if (!configFile.startsWith(FS)) {
			configFile = FS + configFile;
		}
		String fullFileName = home + configFile;

		log.debug("Config file is:" + fullFileName);
		ConfigFactory.init(fullFileName);
		SystemConfig systemConfig = ConfigFactory.getSystemConfig();

		systemConfig.load();

	}
}
