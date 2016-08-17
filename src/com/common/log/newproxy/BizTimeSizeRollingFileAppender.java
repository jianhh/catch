package com.common.log.newproxy;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.helpers.QuietWriter;
import org.apache.log4j.spi.LoggingEvent;


/**
 * <p> * Title: BizTimeSizeRollingFileAppender * </p>
 * <p> * Description: 业务日志追加实现类，实现按时间、大小分隔日志，并按时间格式产生对账文件 * </p>
 * <p> * Copyright: Copyright (c) 2012 * </p>
 * <p> * Company: Aspire Technologies * </p>
 * 
 * @author jimin
 * @version 1.0.0 history
 * 
 */

public class BizTimeSizeRollingFileAppender extends FileAppender
{

    static final int TOP_OF_TROUBLE=-1;
    static final int TOP_OF_MINUTE = 0;
    static final int TOP_OF_HOUR   = 1;
    static final int HALF_DAY      = 2;
    static final int TOP_OF_DAY    = 3;
    static final int TOP_OF_WEEK   = 4;
    static final int TOP_OF_MONTH  = 5;
    static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

    /**
     * 是否使用跟踪线程，以便尽快生成对帐文件
     */
    private String instantCreatChkfile = "false";
    
    /**
     * 日志文件中的日期格式定义:缺省是'.'yyyy-MM-dd
     */
    private String datePattern = "'.'yyyy-MM-dd";
    
    /**
     * 对帐文件中的日期格式定义:缺省是'.'yyyy-MM-dd'.999999'
     */
    private String stat_datepattern = "'.'yyyy-MM-dd'.999999'";
    
    /**
     * The default maximum file size is 10MB.
     */
    protected long maxFileSize = 10 * 1024 * 1024;
    
    //日志文件扩展名
    private static final String BACKUP_SUFFIX = ".bak";
    //对账文件临时扩展名
    private static final String CHECK_FILE_EXT=".chk";
    
    private static final String CHECK_FILE_SEPARATOR="|";
    private static final String NEXT_LINE=System.getProperty("line.separator");
    
    //日志文件相关变量
    int nextNum =0;                 //大小超过指定值的下标
    Date now = new Date();          //当前检查时间
    SimpleDateFormat sdf;
    RollingCalendar rc = new RollingCalendar();
    String scheduledFilename;       //下个日志文件文件名
    private long nextCheck = System.currentTimeMillis () - 1;       //下次检查时间
    
    //对账文件相关变量
    Date stat_now = new Date();     //当前检查时间
    SimpleDateFormat stat_sdf;
    RollingCalendar stat_rc = new RollingCalendar();
    String stat_fileName;           //当前对账文件文件名
    String stat_scheduledFilename;  //下个对账文件文件名
    private long stat_nextCheck = System.currentTimeMillis () - 1;  //下次检查时间
    protected QuietWriter stat_qw;
    
    /**
     * The default constructor does nothing.
     */

    public BizTimeSizeRollingFileAppender()
    {

    }

    public BizTimeSizeRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException
    {

        super(layout, filename, true);
        this.datePattern = datePattern;
        activateOptions();
    }
    
    /**
     * 设置相关参数
     */
    public void activateOptions()
    {
        super.activateOptions();
        
        // 初始化对账文件相关变量
        if (stat_datepattern != null && fileName != null)
        {
            stat_now.setTime(System.currentTimeMillis());
            stat_sdf = new SimpleDateFormat(stat_datepattern);
            int type = computeCheckPeriod(stat_datepattern);
            // printPeriodicity(type);
            stat_rc.setType(type);
            File file = new File(fileName);
            stat_scheduledFilename = this.getStatScheduledFilename(new Date(file.lastModified()));
            //stat_fileName = "stat_" + fileName+CHECK_FILE_EXT;
            resetStatQW();
        }
        else
        {
            LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
        }
        
        //初始化日志文件相关变量
        if (datePattern != null && fileName != null)
        {
            now.setTime(System.currentTimeMillis());
            sdf = new SimpleDateFormat(datePattern);
            int type = computeCheckPeriod(datePattern);
            // printPeriodicity(type);
            rc.setType(type);
            File file = new File(fileName);
            //得到大小限制的下标
            nextNum=getNextNum();
            scheduledFilename = this.getScheduledFilename(new Date(file.lastModified()));
        }
        else
        {
            LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
        }
        
        // 是否增加到侦听
        LogLog.debug("this.instantCreatChkfile:" + this.instantCreatChkfile);
        if ("true".equals(this.instantCreatChkfile))
        {
             ChkfileCreatorObserver.addToListen(this);
        }
    }

    @Override
    protected void subAppend(LoggingEvent event)
    {

        long n = System.currentTimeMillis();
        if (n >= nextCheck)
        {
            now.setTime(n);
            nextCheck = rc.getNextCheckMillis(now);
            try
            {
                rollOverForTime();
            }
            catch (IOException ioe)
            {
                LogLog.error("rollOver() failed.", ioe);
            }
        }
        
        if(fileName != null && qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            if (size >= maxFileSize ) {
                try
                {
                    rollOverForSize();
                }
                catch (IOException ioe)
                {
                    LogLog.error("rollOver() failed.", ioe);
                }
            }
        }
        
        if(event != null){
            super.subAppend(event);    
        }
    }

    @Override
    protected void setQWForFiles(Writer writer) {
        this.qw = new CountingQuietWriter(writer, errorHandler);
    }
    
    @Override
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException
    {
        super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
        if (append)
        {
            File f = new File(fileName);
            (( CountingQuietWriter ) qw).setCount(f.length());
        }
    }
    @Override
    public void setFile(String file) {
        
        String val = file.trim();
        val = val.replace('/', File.separatorChar);
        fileName = LogHelper.getLogRootPath() + val;

        // create non-exist path
        LogLog.debug("fileName:" + fileName);

        int index = fileName.lastIndexOf(File.separatorChar);
        if (index > 0) {
            String sPath = fileName.substring(0, index);
            File path = new File(sPath);
            if (!path.exists()) {
                path.mkdirs();
            }
            
            String t_fileName = fileName.substring(index+1);
            stat_fileName = sPath+File.separatorChar+"stat_" + t_fileName+CHECK_FILE_EXT;
        }else{
            stat_fileName = "stat_" + fileName+CHECK_FILE_EXT;
        }

        LogLog.debug("stat_fileName:" + stat_fileName);
    }
    
    /**
     * 当大小超过限制时换文件名
     * @throws IOException
     */
    protected void rollOverForSize() throws IOException{
        this.nextNum++;
        rollOver(false);
        
    }
    /**
     * 当时间超过限制时换文件名
     * @throws IOException
     */
    protected void rollOverForTime() throws IOException{
        if (datePattern == null) {
            errorHandler.error("Missing DatePattern option in rollOver().");
            return;
          }

        rollOver(true);
          
    }

    /**
     * 换文件名，换完后需要写对账文件
     * @param isRollForTime
     * @throws IOException
     */
    void rollOver(boolean isRollForTime) throws IOException {
        
        String datedFilename = this.getScheduledFilename(now);
        if (scheduledFilename.equals(datedFilename)) {
          return;
        }
//      close current file, and rename it to datedFilename
        this.closeFile();

        File target  = new File(scheduledFilename);
        if (target.exists()) {
          target.delete();
        }

        File file = new File(fileName);
        boolean result = file.renameTo(target);
        if(result) {
          LogLog.debug(fileName +" -> "+ scheduledFilename);
        } else {
          LogLog.error("Failed to rename ["+fileName+"] to ["+scheduledFilename+"].");
        }

        try {
          // This will also close the file. This is OK since multiple
          // close operations are safe.
          this.setFile(fileName, true, this.bufferedIO, this.bufferSize);
        }
        catch(IOException e) {
          errorHandler.error("setFile("+fileName+", true) call failed.");
        }
        
        //记录对账文件
        appendStat(scheduledFilename);
        
        if(isRollForTime){
            this.nextNum=0;
        }
        scheduledFilename = this.getScheduledFilename(now);
    }
    
    /**
     * 将文件名记录到对账文件
     * @param filename
     */
    protected void appendStat(String filename)
    {

        File file = new File(filename);
        if (file.exists())
        {
            StringBuffer sb = new StringBuffer();
            sb.append(file.getName()).append(CHECK_FILE_SEPARATOR);
            sb.append(file.length()).append(CHECK_FILE_SEPARATOR);
            try
            {
                sb.append(this.countLines(filename));
            }
            catch (IOException e)
            {
                LogLog.error("write to check file error! filename:" + fileName + ",stat_filename:" + stat_fileName, e);
            }
            sb.append(NEXT_LINE);
            // 将文件名写入对账文件
            this.stat_qw.write(sb.toString());
            this.stat_qw.flush();
        }
        else
        {
            LogLog.error("log file is not exists! filename:" + filename);
        }

        //检查是否需要更换对账文件的文件名
        long n = System.currentTimeMillis();
        if (n >= stat_nextCheck)
        {
            stat_now.setTime(n);
            stat_nextCheck = stat_rc.getNextCheckMillis(stat_now);
            try
            {
                rollOverStat();
            }
            catch (IOException ioe)
            {
                LogLog.error("rollOver() failed.", ioe);
            }
        }
    }
    /**
     * 修改对账文件文件名
     * @throws IOException 
     *
     */
    private void rollOverStat() throws IOException {
        /* Compute filename, but only if datePattern is specified */
        if (stat_datepattern == null) {
          errorHandler.error("Missing DatePattern option in rollOver().");
          return;
        }

        String datedFilename = this.getStatScheduledFilename(stat_now);
        // It is too early to roll over because we are still within the
        // bounds of the current interval. Rollover will occur once the
        // next interval is reached.
        if (stat_scheduledFilename.equals(datedFilename)) {
          return;
        }

        File target  = new File(stat_scheduledFilename);
        if (target.exists()) {
          target.delete();
        }

        //写入一行999999，并改名成目标对账文件
        File file = new File(stat_fileName);
        String line = "999999"+NEXT_LINE;
        this.stat_qw.write(line);
        this.stat_qw.flush();
        this.stat_qw.close();
        
        boolean result = file.renameTo(target);
        if(result) {
          LogLog.debug(stat_fileName +" -> "+ stat_scheduledFilename);
        } else {
          LogLog.error("Failed to rename ["+stat_fileName+"] to ["+stat_scheduledFilename+"].");
        }
        stat_scheduledFilename = datedFilename;
        this.resetStatQW();
    }


    /**
     * 重置对账文件的QuietWriter
     *
     */
    private void resetStatQW()
    {
        try
        {
            if (stat_qw != null)
            {
                stat_qw.close();
                stat_qw = null;
            }
            this.stat_qw = new QuietWriter(new FileWriter(stat_fileName, true), errorHandler);
        }
        catch (IOException e)
        {
            LogLog.error("reset stat qw error.",e);
        }
    }
    
    /**
     * 得到下一个日志文件的文件名
     * 如果大小限制的下标大于0则加下标
     * @param now
     * @return
     */
    private String getScheduledFilename(Date now){
        StringBuffer sb = new StringBuffer();
        sb.append(fileName).append(sdf.format(now));
        if(nextNum>0){
            sb.append(".").append(nextNum);
        }
        sb.append(BACKUP_SUFFIX);
        return sb.toString();
    }
    
    /**
     * 得到下个对账文件的文件名
     * @param now
     * @return
     */
    private String getStatScheduledFilename(Date now){
        StringBuffer sb = new StringBuffer();
        sb.append(fileName).append(stat_sdf.format(now));        
        return sb.toString();
    }
    
    /**
     * 得到文件名的下个序号
     * @return
     */
    private int getNextNum()
    {
        int ret = 0;
        // 先从对账文件中取，如果没有对账文件，则从日志目录中取，如果也没有，则返回0
        try
        {
            File stat_file = new File(stat_fileName);
            if (stat_file.exists())
            {
                List list = FileUtils.readLines(stat_file);
                if (list.size() > 0)
                {
                    String lastLine = ( String ) list.get(list.size() - 1);
                    String fileName = stat_file.getName() + sdf.format(stat_now);
                    String num = lastLine.substring(fileName.length() + 1, lastLine.indexOf(BACKUP_SUFFIX));
                    ret = Integer.valueOf(num) + 1;
                }
                return ret;
            }
            else
            {
                LogLog.debug("check file is not exists.");
            }
        }
        catch (Exception e)
        {
            //该异常不作记录
            //LogLog.error("get NextNum from check file error!", e);
        }
        
        try
        {
            File file = new File(fileName);
            String fileName = file.getName() + sdf.format(stat_now);
            String[] files = file.getParentFile().list();
            for (String s : files)
            {
                try
                {
                    if (s.startsWith(fileName)&&s.endsWith(BACKUP_SUFFIX))
                    {
                        String num = s.substring(fileName.length() + 1, s.indexOf(BACKUP_SUFFIX));
                        int i = Integer.valueOf(num).intValue()+1;
                        ret = ret < i?i:ret;
                    }
                }
                catch (Exception e)
                {
                    // 该异常不作记录
                }
            }
        }catch(Exception e){
            LogLog.error("get NextNum from log file error!", e);
        }
        return ret;
    }
    
    /**
     * 得到文件行数
     * @param filename
     * @return
     * @throws IOException
     */
    private int countLines(String filename) throws IOException
    {
        LineNumberReader reader = new LineNumberReader(new FileReader(filename));
        int cnt = 0;
        while ((reader.readLine()) != null)
        {
        }
        cnt = reader.getLineNumber();
        reader.close();
        return cnt;
    }  
    
    /**
     * 根据日期格式返回日期类型
     * 
     * @param pattern
     * @return
     */
    int computeCheckPeriod(String pattern) {
        RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.getDefault());
        // set sate to 1970-01-01 00:00:00 GMT
        Date epoch = new Date(0);
        if(pattern != null) {
          for(int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(gmtTimeZone); // do all date formatting in GMT
        String r0 = simpleDateFormat.format(epoch);
        rollingCalendar.setType(i);
        Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
        String r1 =  simpleDateFormat.format(next);
        //System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
        if(r0 != null && r1 != null && !r0.equals(r1)) {
          return i;
        }
          }
        }
        return TOP_OF_TROUBLE; // Deliberately head for trouble...
      }
    
    public void setMaxFileSize(String value) {
        maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);
        // maxFileSize=value;
    }

    public void setMaximumFileSize(long value) {
        // maxFileSize=OptionConverter.toFileSize(value,maxFileSize+1);
        maxFileSize = value;
    }
    public String getDatePattern()
    {
        return datePattern;
    }
    
    public void setDatePattern(String datePattern)
    {
        this.datePattern = datePattern;
    }
    
    public String getInstantCreatChkfile()
    {
        return instantCreatChkfile;
    }

    public void setInstantCreatChkfile(String instantCreatChkfile)
    {
        this.instantCreatChkfile = instantCreatChkfile;
    }
    
    public String getStat_datepattern()
    {
        return stat_datepattern;
    }

    public void setStat_datepattern(String stat_datepattern)
    {
        this.stat_datepattern = stat_datepattern;
    }
    

    /**
     *  RollingCalendar is a helper class to DailyRollingFileAppender.
     *  Given a periodicity type and the current time, it computes the
     *  start of the next interval.  
     * */
    class RollingCalendar extends GregorianCalendar {
      private static final long serialVersionUID = -3560331770601814177L;

      int type = TOP_OF_TROUBLE;

      RollingCalendar() {
        super();
      }  

      RollingCalendar(TimeZone tz, Locale locale) {
        super(tz, locale);
      }  

      void setType(int type) {
        this.type = type;
      }

      public long getNextCheckMillis(Date now) {
        return getNextCheckDate(now).getTime();
      }

      public Date getNextCheckDate(Date now) {
        this.setTime(now);

        switch(type) {
        case TOP_OF_MINUTE:
        this.set(Calendar.SECOND, 0);
        this.set(Calendar.MILLISECOND, 0);
        this.add(Calendar.MINUTE, 1);
        break;
        case TOP_OF_HOUR:
        this.set(Calendar.MINUTE, 0);
        this.set(Calendar.SECOND, 0);
        this.set(Calendar.MILLISECOND, 0);
        this.add(Calendar.HOUR_OF_DAY, 1);
        break;
        case HALF_DAY:
        this.set(Calendar.MINUTE, 0);
        this.set(Calendar.SECOND, 0);
        this.set(Calendar.MILLISECOND, 0);
        int hour = get(Calendar.HOUR_OF_DAY);
        if(hour < 12) {
          this.set(Calendar.HOUR_OF_DAY, 12);
        } else {
          this.set(Calendar.HOUR_OF_DAY, 0);
          this.add(Calendar.DAY_OF_MONTH, 1);
        }
        break;
        case TOP_OF_DAY:
        this.set(Calendar.HOUR_OF_DAY, 0);
        this.set(Calendar.MINUTE, 0);
        this.set(Calendar.SECOND, 0);
        this.set(Calendar.MILLISECOND, 0);
        this.add(Calendar.DATE, 1);
        break;
        case TOP_OF_WEEK:
        this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
        this.set(Calendar.HOUR_OF_DAY, 0);
        this.set(Calendar.MINUTE, 0);
        this.set(Calendar.SECOND, 0);
        this.set(Calendar.MILLISECOND, 0);
        this.add(Calendar.WEEK_OF_YEAR, 1);
        break;
        case TOP_OF_MONTH:
        this.set(Calendar.DATE, 1);
        this.set(Calendar.HOUR_OF_DAY, 0);
        this.set(Calendar.MINUTE, 0);
        this.set(Calendar.SECOND, 0);
        this.set(Calendar.MILLISECOND, 0);
        this.add(Calendar.MONTH, 1);
        break;
        default:
        throw new IllegalStateException("Unknown periodicity type.");
        }
        return getTime();
      }
    }
}
