package com.framework.base;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.StartContent;

/**
 * 创建线程池
 * 
 * @author tangbiao
 * 
 */
public class ThreadPool {

	// 核心线程数量，即初始化线程池的启动线程数量
	private int corePoolSize = 50;

	// 最大线程数量
	private int maximumPoolSize = 300;

	// 线程的存活时间，即完成任务后多久可再使用
	private int keepAliveTime = 60;

	// 等待队列的长度
	private int workQueueSize = 200;

	private ThreadPoolExecutor exec = null;

	private static ThreadPool tp = null;

	static JLogger logger = LoggerFactory.getLogger(ThreadPool.class);

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	private ThreadPool() {

		if (StartContent.getInstance().getDomainUrl().equals(
				"www.truedian.com")) {// 现网
			exec = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
					keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(
							workQueueSize));
		}else{
			corePoolSize = 2;
			maximumPoolSize = 10;
			exec = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
					keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(
							workQueueSize));
		}
	}

	/**
	 * 获取ThreadPool实例
	 * 
	 * @return ThreadPool
	 */
	public static synchronized ThreadPool getInstance() {

		if (tp == null) {
			tp = new ThreadPool();
		}
		return tp;
	}

	public ThreadPoolExecutor getThread() {

		return exec;
	}

}
