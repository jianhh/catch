
package com.framework.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CachePageResultVO implements Serializable
{

   
    /**
	 * 
	 */
	private static final long serialVersionUID = 201111052;

	/**
	 * ÿҳ��¼��
	 */
	protected int pageSize = 12;

    /**
	 * ���������ļ�¼�����
	 */
    protected List pageInfo = new ArrayList();

    /**
     * ��ҳ��
     */
    protected int totalPages = 0;

    /**
     * �ܼ�¼��
     */
    protected int totalRows = 0;

    /**
     * ��ǰҳ��
     */
    protected int currentPageNo = 1;

    /**
     * ��ǰҳ�ļ�¼�Ŀ�ʼ���
     */
    protected int startnum = 1;

    /**
     * ��һҳ����
     */
    protected int nextPageNo = 0;

    /**
     * ��һҳ����
     */
    protected int prevPageNo = 0;

    /**
     * ��һҳ��
     */
    protected int firstPage = 1;

    /**
     * ���һҳ��
     */
    protected int lastPage = 1;

    public int getPageSize()
    {

        return pageSize;
    }

    public void setPageSize(int pageSize)
    {

        this.pageSize = pageSize;
    }

    public List getPageInfo()
    {

        return pageInfo;
    }

    public void setPageInfo(List pageInfo)
    {

        this.pageInfo = pageInfo;
    }

    public int getCurrentPageNo()
    {

        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo)
    {

        this.currentPageNo = currentPageNo;
    }

    public int getTotalRows()
    {

        return totalRows;
    }

    public void setTotalRows(int totalRows)
    {

        this.totalRows = totalRows;
    }

    public int getFirstPage()
    {

        return firstPage;
    }

    public int getLastPage()
    {

        return lastPage;
    }

    public int getNextPageNo()
    {

        return nextPageNo;
    }

    public int getPrevPageNo()
    {

        return prevPageNo;
    }

    public int getStartnum()
    {

        return startnum;
    }

    public void setTotalPages(int totalPages)
    {

        this.totalPages = totalPages;
    }
 
    public int getTotalPages()
    {
    
        return totalPages;
    };
 
}
