package com.everwing.coreservice.common.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {

	private static ThreadPoolUtils utils = new ThreadPoolUtils();
	
	private static ExecutorService executors = Executors.newCachedThreadPool(); //可变缓存线程池
	
	
	public static ThreadPoolUtils getInstance(){
		return utils;
	}

	private ThreadPoolUtils(){}
	
	public void executeThread(Thread thread){
		executors.execute(thread);
	}
	
	public void executeThread(Runnable runnable){
		executors.execute(runnable);
	}
	
	
}
