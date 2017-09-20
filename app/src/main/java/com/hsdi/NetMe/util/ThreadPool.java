package com.hsdi.NetMe.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huohong.yi on 2017/7/18.
 */

public class ThreadPool {
    static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void doTask(Runnable task){
        threadPool.execute(task);
    }

}
