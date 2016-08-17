
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
            logger.debug("请求中的currentPage :" + current);
        }
        if (current != null && !"".equals(current))
        {
            try
            {
                this.totalRows = Integer.valueOf(total);
            }
            catch (Exception ex)
            {
                logger.debug("未获取到请求头里的total，设置默认值");
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
        // 如果不是来自翻页请求
        if (!isFromPage)
        {
            this.totalRows = dao.getCount(sql,
                                          parameter,
                                          dateVersionId,
                                          isCache);
            if (logger.isDebugEnabled())
            {
                logger.debug("获取到的总记录数为:" + totalRows);
            }
            countTotalPages();
        }
        // 如果记录数大于0
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
     * 进行分页查询并进行缓存
     * 
     * @param <T>
     * @param sql 数据查询sql
     * @param parameter 查询参数
     * @param cla 数据对象类型
     * @param dataVersionId 数据版本号ID
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
     * 把一个list加入到分页中。
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
     * 将一个分好页的list放入分页信息中
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
        // 总记录数小于0，说明请求来自新的查询,否则是来自翻页请求
        if (0 >= this.totalRows)
        {
            this.totalRows = totalRows;
        }
        // 根据页面序号计算本次请求的其起始记录和结尾记录
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
