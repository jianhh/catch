
package com.framework.base;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.aspire.dps.boprovide.BOProvideService;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;

abstract public class BaseListener extends BOProvideService
{

    final private static JLogger logger = LoggerFactory.getLogger(BaseListener.class);
    
    public void excute(Req request, Resp response)
    {

        try
        {
            doProcess(request, response);
        }
        catch (Exception e)
        {
            response.setCaseType(Resp.CASETYPE_REDIRECT);
            response.setRedirectUrl("/defaultSite/warning.html");
        }

    }

    private void doProcess(Req request, Resp response) throws Exception
    {

        doPerform(request, response); // ////Ö´ÐÐÒµÎñ
    }
    
    abstract public void doPerform(Req request, Resp response) throws Exception;

}
