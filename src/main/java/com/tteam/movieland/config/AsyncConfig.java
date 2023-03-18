package com.tteam.movieland.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    @Override
    public ExecutorService getAsyncExecutor() {
        return new ThreadPoolExecutor(
                10, // core pool size - minimum number of threads to keep in the pool
                Integer.MAX_VALUE, // maximum pool size - maximum number of threads to allow in the pool
                5L, // keep-alive time - time in seconds to keep an idle thread alive
                TimeUnit.SECONDS, // time unit for the keep-alive time
                new SynchronousQueue<>(),// the queue to use for holding tasks
                r -> {                   // ThreadFactory that converts all threads to daemons
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                }
        );
    }

    /*daemon threads can be killed on program termination*/
//    @Bean
//    @Override
//    public ExecutorService getAsyncExecutor() {
//        return Executors.newFixedThreadPool(Math.min(2, 100), r -> {
//            Thread t = new Thread(r);
//            t.setDaemon(true);
//            return t;
//        });
//    }
}
