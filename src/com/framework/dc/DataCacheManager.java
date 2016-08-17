package com.framework.dc;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.ThreadPool;
import com.framework.dc.bo.DataLoadBO;
import com.framework.dc.vo.TableVO;
import com.framework.log.Util;
import com.framework.util.DateUtil;

/**
 * dc����ģ�������
 * 
 * @author rongcunhao
 * 
 */
public class DataCacheManager
{

    static JLogger logger = LoggerFactory.getLogger(DataCacheManager.class);

    static JLogger dataCacheManagerLog = LoggerFactory.getLogger("biz.datacache.manage");

    public static String date = Util.formatStrByLen(DateUtil.format.format(new Date()),
                                                    14);

    /**
     * ϵͳ������ʼ��
     */
    public void init()
    {

        dataCacheManagerLog.info(date
                                 + " ϵͳ��ʼ��-------------------------------------------��ʼ ");
        logger.debug("�����ݿ���ȡ��DC��");
        Config config = new Config();
        List<TableVO> tableList = config.getTableListConfig();
        for (int i = 0; i < tableList.size(); i++)
        {
            TableVO tableVO = ( TableVO ) tableList.get(i);
            String tableName = tableVO.getTableName();
            String tablePk = tableVO.getTablePk();
            String loadColumn = tableVO.getLoadColumn();

            StringBuffer sb = new StringBuffer();
            sb.append(Util.formatStrByLen(DateUtil.format.format(new Date()),
                                          14)).append("|");
            sb.append(Util.formatStrByLen(("Start load table " + tableName
                                           + " ,Primary key: " + tablePk), 75))
              .append("|");
            DataLoadBO bo = new DataLoadBO();
            // ��ȡ��ǰ������
            Map<String, Map> dcMap = bo.dataLoad(tableName, tablePk, loadColumn);
            // ����ǰ�����ݣ�����datacache��
            DataCache.addDataToCache(tableName, dcMap);
            sb.append("Table " + tableName + " load over");
            logger.debug(sb.toString());
            dataCacheManagerLog.info(sb.toString());
        }
        logger.debug("Initialization DC over��");
        dataCacheManagerLog.info(date
                                 + " ϵͳ��ʼ��-------------------------------------------���� ");
        refreshTimeLoad();
    }

    /**
     * �߳��Զ�����
     */
    public void refreshTimeLoad()
    {

        Config config = new Config();
        List tableList = config.getTableListConfig();
        for (int i = 0; i < tableList.size(); i++)
        {
            TableVO tableVO = ( TableVO ) tableList.get(i);
            String tableName = tableVO.getTableName();
            String tablePk = tableVO.getTablePk();
            String loadColumn = tableVO.getLoadColumn();
            Long sleepTime = config.getRefreshTime(tableName);

            dataCacheManagerLog.info(date + " Ϊ�� " + tableName + " ���������߳�");
            dataLoad(tableName, tablePk, loadColumn, sleepTime);
        }
    }

    /**
     * ���ݼ����߳�
     * 
     * @param tableName
     * @param tablePk
     * @param time
     */
    private void dataLoad(final String tableName, final String tablePk,
                          final String loadColumn, final Long sleepTime)
    {

        try
        {
            Callable call = new Callable()
            {

                public Object call() throws Exception
                {

                    while (true)
                    {
                        Thread.sleep(sleepTime);
                        StringBuffer sb = new StringBuffer();
                        sb.append(Util.formatStrByLen(DateUtil.format.format(new Date()),
                                                      14))
                          .append("|");
                        sb.append("�첽 Start load table " + tableName
                                  + " ,Primary key: " + tablePk).append("|");
                        sb.append("after " + sleepTime + " ms, load again")
                          .append("|");

                        DataLoadBO bo = new DataLoadBO();
                        // ��ȡ��ǰ������
                        Map<String, Map> dcTableMap = bo.dataLoad(tableName,
                                                                  tablePk,
                                                                  loadColumn);

                        // ����ǰ�����ݣ�����datacache�С�Ϊ�˷�ֹ��ѯ�쳣����dc����ֵ�滻
                        if (null != dcTableMap && dcTableMap.size() != 0)
                        {
                            DataCache.addDataToCache(tableName, dcTableMap);
                        }

                        sb.append("�첽  Load table " + tableName + " ,over!")
                          .append("|")
                          .append("Load record: " + dcTableMap.size() + " ��");

                        logger.debug(sb.toString());
                        dataCacheManagerLog.info(sb.toString());
                    }
                }
            };
            ThreadPoolExecutor executor = ThreadPool.getInstance().getThread();
            executor.execute(new java.util.concurrent.FutureTask(call));
        }
        catch (Exception e)
        {
            logger.error("�첽���ػ�������ݱ�" + tableName + "ʧ��", e);
            dataCacheManagerLog.info("�첽���ػ�������ݱ�" + tableName + "ʧ��", e);
        }
    }

}
