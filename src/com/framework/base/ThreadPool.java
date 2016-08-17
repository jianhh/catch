package com.framework.base;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.StartContent;

/**
 * �����̳߳�
 * 
 * @author tangbiao
 * 
 */
public class ThreadPool {

	// �����߳�����������ʼ���̳߳ص������߳�����
	private int corePoolSize = 50;

	// ����߳�����
	private int maximumPoolSize = 300;

	// �̵߳Ĵ��ʱ�䣬�����������ÿ���ʹ��
	private int keepAliveTime = 60;

	// �ȴ����еĳ���
	private int workQueueSize = 200;

	private ThreadPoolExecutor exec = null;

	private static ThreadPool tp = null;

	static JLogger logger = LoggerFactory.getLogger(ThreadPool.class);

	/**
	 * ���췽��
	 */
	@SuppressWarnings("unchecked")
	private ThreadPool() {

		if (StartContent.getInstance().getDomainUrl().equals(
				"www.truedian.com")) {// ����
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
	 * ��ȡThreadPoolʵ��
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
