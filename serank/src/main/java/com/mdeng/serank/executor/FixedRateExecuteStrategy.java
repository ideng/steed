package com.mdeng.serank.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Fixed rate execute strategy
 * 
 * @author Administrator
 *
 */
@Component
public class FixedRateExecuteStrategy implements ExecuteStrategy {

  private SERankExtractor executor;
  @Value("${serank.executor.frequency}")
  private int frequency;

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  @Override
  public void setExecutor(SERankExtractor executor) {
    this.executor = executor;
  }

  @Override
  public SERankExtractor getExecutor() {
    return executor;
  }

  @Override
  public void execute() {
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    ses.scheduleAtFixedRate(new Runnable() {

      @Override
      public void run() {
        executor.extract();
      }
    }, 0, frequency, TimeUnit.SECONDS);
  }

}
