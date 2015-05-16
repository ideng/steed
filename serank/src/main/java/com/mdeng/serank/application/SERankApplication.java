package com.mdeng.serank.application;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mdeng.serank.executor.SERankExecutor;

/**
 * SE Rank application main entry
 * 
 * @author Administrator
 *
 */
public class SERankApplication {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context = null;
    context = new AnnotationConfigApplicationContext(SERankConfig.class);
    SERankExecutor executor = context.getBean(SERankExecutor.class);

    ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
    ses.scheduleAtFixedRate(executor, 0, 5, TimeUnit.MINUTES);

    context.close();
  }

}
