package com.mdeng.serank.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Single time execute strategy
 * 
 * @author Administrator
 *
 */
@Component
public class SingleTimeExecuteStrategy implements ExecuteStrategy {

  @Autowired
  private SERankExtractor executor;

  @Override
  public SERankExtractor getExecutor() {
    return executor;
  }

  @Override
  public void setExecutor(SERankExtractor executor) {
    this.executor = executor;
  }

  @Override
  public void execute() {
    executor.extract();
  }
}
