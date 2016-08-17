
package com.framework.page;

import java.util.ArrayList;
import java.util.List;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.aspire.dps.jvsc.Req;
import com.framework.util.CommonUtil;
import com.framework.util.StringUtils;

public class Page extends CachePageResultVO
{

    private static final JLogger logger = LoggerFactory.getLogger(Page.class);

    private int endNum = 0;

    private boolean isFromPage = false;

    private String reqCurrentSize = null;

    private String reqtotalRows = null;

    public Page()
    {

        this.currentPageNo = 1;
        this.pageSize = PageConstant.NOT_PAGINATION_PAGESIZE;
    }

    public Page(Req request, int pageSize)
    {

        if (pageSize < 0)
            pageSize = 10;
        this.pageSize = pageSize;
        getParamterFromReq(request);
        init(this.reqCurrentSize, this.reqtotalRows);
    }

    private void getParamterFromReq(Req request)
    {

        this.reqCurrentSize = CommonUtil.getParameter(request, "currentPage");
        this.reqtotalRows = CommonUtil.getParameter(request, "totalRows");
    }

    public Page(Req request)
    {

        getParamterFromReq(request);
    }

    public void setPageSize(int pageSize)
    {

        if (pageSize < 0)
            pageSize = 10;
        this.pageSize = pageSize;
        init(this.reqCurrentSize, this.reqtotalRows);
    }

    private void init(String current, String total)
    {

        if (logger.isDebugEnabled())
        {
            logger.debug("�����е�currentPage :" + current);
        }
        if (current != null && !"".equals(current))
        {
            try
            {
                this.totalRows = Integer.valueOf(total);
            }
            catch (Exception ex)
            {
                logger.debug("δ��ȡ������ͷ���total������Ĭ��ֵ");
                totalRows = 0;
            }
            countTotalPages();
            try
            {
                this.currentPageNo = Integer.valueOf(current);
                if (this.currentPageNo > this.totalPages)
                    this.currentPageNo = this.totalPages;
                if (this.currentPageNo <= 0)
                    this.currentPageNo = 1;
            }
            catch (Exception ex)
            {
                this.currentPageNo = 1;
            }
            isFromPage = true;
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("pageSize: " + this.pageSize);
            logger.debug("totalRows: " + this.totalRows);
            logger.debug("totalPage: " + this.totalPages);
            logger.debug("currentPage:" + this.currentPageNo);
        }
    }

    public void countTotalPages()
    {

        this.totalPages = (this.totalRows + this.pageSize - 1) / this.pageSize;
        if (totalPages <= 0)
            totalPages = 1;
    }

    public <T extends Object> void excute(String sql, String[] parameter,
                                          Class<T> cla, String dateVersionId,
                                          boolean isCache) throws Exception
    {

        PageDAO dao = new PageDAO();
        // ����������Է�ҳ����
        if (!isFromPage)
        {
            this.totalRows = dao.getCount(sql,
                                          parameter,
                                          dateVersionId,
                                          isCache);
            if (logger.isDebugEnabled())
            {
                logger.debug("��ȡ�����ܼ�¼��Ϊ:" + totalRows);
            }
            countTotalPages();
        }
        // �����¼������0
        if (totalRows > 0)
        {
            this.pageInfo = dao.queryDataFromDb(sql,
                                                cla,
                                                parameter,
                                                (this.currentPageNo - 1)
                                                                * pageSize,
                                                pageSize,
                                                dateVersionId,
                                                isCache);
        }
    }

    /**
     * ���з�ҳ��ѯ�����л���
     * 
     * @param <T>
     * @param sql ���ݲ�ѯsql
     * @param parameter ��ѯ����
     * @param cla ���ݶ�������
     * @param dataVersionId ���ݰ汾��ID
     * @throws Exception
     */
    public <T extends Object> void excuteWithCache(String sql,
                                                   String[] parameter,
                                                   Class<T> cla,
                                                   String dataVersionId)
                    throws Exception
    {

        if (StringUtils.isEmpty(dataVersionId))
        {
            excute(sql, parameter, cla, null, false);
        }
        else
        {
            excute(sql, parameter, cla, dataVersionId, true);
        }
    }

    /**
     * ��һ��list���뵽��ҳ�С�
     * 
     * @param allList List
     */
    public void excute(List allList)
    {

        if (allList == null || allList.size() == 0)
        {
            return;
        }
        excute(allList, allList.size(), true);
    }

    /**
     * ��һ���ֺ�ҳ��list�����ҳ��Ϣ��
     * 
     * @param pageList
     * @param totalRows
     */
    public void excute(List pageList, int totalRows)
    {

        excute(pageList, totalRows, false);
    }

    private void excute(List pageList, int totalRows, boolean isAllList)
    {

        pageInfo.clear();
        // �ܼ�¼��С��0��˵�����������µĲ�ѯ,���������Է�ҳ����
        if (0 >= this.totalRows)
        {
            this.totalRows = totalRows;
        }
        // ����ҳ����ż��㱾�����������ʼ��¼�ͽ�β��¼
        countTotalPages();
        this.startnum = this.pageSize * (this.currentPageNo - 1) + 1;
        if (!isAllList)
        {
            this.pageInfo = pageList;
            return;
        }
        this.endNum = startnum + this.pageSize - 1;
        this.endNum = (this.endNum >= totalRows ? totalRows : this.endNum);
        pageInfo = new ArrayList(pageList.subList(startnum - 1, endNum));
    }

    /**
     * getNextPageNo
     * 
     * @return int
     */
    public int getNextPageNo()
    {

        return (currentPageNo + 1 > totalPages) ? totalPages
                        : currentPageNo + 1;
    }

    /**
     * 
     * @return
     */
    public int getPrevPageNo()
    {

        return (currentPageNo - 1 < 1) ? 1 : currentPageNo - 1;
    }

}
