package com.example.bingfa02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author: chenpiqian
 * @date: 2019/3/4 18:17
 * 线程池工具
 */
public final class ThreadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtil.class);
	private static ThreadPoolExecutor executor = null;
	private ThreadUtil() {}

	static{
		initExecutor();
	}

	/**
	 * 初始化
	 */
	private static void initExecutor(){
		int  cpuCores = Runtime.getRuntime().availableProcessors();
		cpuCores = cpuCores < 5 ? 5 : cpuCores;

		int temp = cpuCores + 2;
		int maxPool = ioMaximumPoolSize();
		maxPool = maxPool < temp ? temp : maxPool;

		LOGGER.info("初始化线程池 corePoolSize:{} , maximumPoolSize:{}" , cpuCores , maxPool);

		// 加上这个纯属为了解决Alibaba代码插件检测
		ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

		executor = new ThreadPoolExecutor(cpuCores , maxPool ,
				60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(10000),
				defaultThreadFactory,
				new CustomAbortPolicy());
	}

	/**
	 * 在公共线程池中执行任务
	 * 
	 * @param runnable 可运行对象
	 */
	public static void execute(Runnable runnable) {
		executor.execute(runnable);
	}

	/**
	 * IO密集型应用的最大线程数
	 * 公式： CPU期望利用率 * CPU核心数 * (1 + 线程平均等待时间 / 线程平均工作时间)
	 *
	 * 阻塞系数 = CPU期望利用率 * 阻塞时间 /（阻塞时间+计算时间）
	 * core / （1-阻塞系数） 和 core * ( 1 + w/c ) 这俩是同一公式
	 */
	private static int ioMaximumPoolSize() {
		// CPU期望利用率设置为100%，尽量利用CPU
		int cpuUsePercent = 1;

		// ava虚拟机可用的CPU核心数量
		int jvmAvailableProcessors = Runtime.getRuntime().availableProcessors();

		// 假设线程平均等待时间和线程平均工作时间的比值是10
		int blockTime = 10;
		int runTime = 1;

		int ioMaxCore = jvmAvailableProcessors * (1 + blockTime/runTime);
		return ioMaxCore;
	}


	/**
	 * 自定义程池饱和策略，模仿AbortPolicy（终止）。发送钉钉消息
	 *
	 * A handler for rejected tasks that throws a
	 * {@code RejectedExecutionException}.
	 */
	public static class CustomAbortPolicy implements RejectedExecutionHandler {
		/**
		 * Creates an {@code AbortPolicy}.
		 */
		public CustomAbortPolicy() { }

		/**
		 * Always throws RejectedExecutionException.
		 *
		 * @param r the runnable task requested to be executed
		 * @param e the executor attempting to execute this task
		 * @throws RejectedExecutionException always
		 */
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			String msg = String.format("线程池满了，抛出异常。\nTask: %s 。\nRejected from: %s",
					r.toString(), e.toString());
			RobotUtils.send(msg);

			throw new RejectedExecutionException("Task " + r.toString() +
					" rejected from " +
					e.toString());
		}
	}


	public static void main(String[] args) throws Exception{
		int num = 50;
		try {
			for (int i = 0; i < num; i++) {
				ThreadUtil.execute(()->{
					try {
						TimeUnit.MILLISECONDS.sleep(10);
					}catch (Exception e){
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName());
					System.out.println("@@@@"+executor);
				});
			}
		}catch (Exception e){
			System.out.println("EEEEEE  "+e);
		}

		TimeUnit.SECONDS.sleep(3);
		System.out.println("!!!!!"+executor);
		int n = 10;
		try {
			for (int i = 0; i < n; i++) {
				ThreadUtil.execute(()->{
					try {
						TimeUnit.MILLISECONDS.sleep(10);
					}catch (Exception e){
						e.printStackTrace();
					}

					System.out.println("####"+executor);
				});
			}
		}catch (Exception e){
			System.out.println("FFFFF  "+e);
		}

		TimeUnit.SECONDS.sleep(3);
		System.out.println("2222222!!!!!"+executor);

		try {
			for (int i = 0; i < n; i++) {
				ThreadUtil.execute(()->{
					try {
						TimeUnit.MILLISECONDS.sleep(10);
					}catch (Exception e){
						e.printStackTrace();
					}

					System.out.println("222222222####"+executor);
				});
			}
		}catch (Exception e){
			System.out.println("2222222!!!!!"+e);
		}

	}

}
