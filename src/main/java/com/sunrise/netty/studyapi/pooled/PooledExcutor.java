package com.sunrise.netty.studyapi.pooled;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @version: 1.00
 * @author: lzhaoyang
 * @date: 2019/12/1 4:34 PM
 */
public class PooledExcutor {
    private ExecutorService executorService;

    public PooledExcutor(int maxPoolSize, int queueSize) {

        this.executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxPoolSize,120L,TimeUnit.SECONDS,new ArrayBlockingQueue<>(queueSize));

    }

    //执行
    public void excute(Runnable task){
        this.executorService.execute(task);
    }
}
