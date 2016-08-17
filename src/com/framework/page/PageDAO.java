
package com.framework.page;

import java.util.List;

import com.framework.base.QueryTemplate;

public class PageDAO extends QueryTemplate
{

    protected PageDAO()
    {

    };

    /**
     * 获取总记录数
     * 
     * @param sql
     * @param obj
     * @return
     */
    public int getCount(String sql, String[] obj, String dataVersion,
                        boolean isCache) throws Exception
    {

        String sqlcount = sql.toLowerCase();
        sqlcount = "select count(1) "
                   + sqlcount.substring(sqlcount.indexOf("from"),
                                        sqlcount.length());

        if (isCache)
            return Integer.valueOf(this.queryStrBySqlWithCache(sqlcount,
                                                               dataVersion,
                                                               obj));
        else
            return Integer.valueOf(this.queryStrBySql(sqlcount, obj));
    }

    /**
     * 从数据库中获取数据
     * 
     * @param <T>
     * @param sql
     * @param cla
     * @param obj
     * @param currentPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public <T extends Object> List<T> queryDataFromDb(String sql, Class<T> cla,
                                                      String[] parameters,
                                                      int currentPageNum,
                                                      int pageSize,
                                                      String dataVersionId,
                                                      boolean isCache)
                    throws Exception
    {

        String pagesql = "";
        if (sql.indexOf("where") > 0)
        {
            pagesql = sql + " LIMIT " + currentPageNum + ", " + pageSize;
        }
        else
        {
            pagesql = sql + " where 1=1 LIMIT " + currentPageNum + ", "
                      + pageSize;
        }

        if (isCache)
        {
            return this.pageQueryWithCache(sql,
                                           currentPageNum,
                                           pageSize,
                                           dataVersionId,
                                           parameters,
                                           cla);
        }
        else
        {
            return this.queryListBySql(pagesql, cla, parameters);
        }
    }
}
