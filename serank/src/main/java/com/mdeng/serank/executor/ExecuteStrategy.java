package com.mdeng.serank.executor;

/**
 * Strategy to run SERank application
 * 
 * @author Administrator
 *
 */
public interface ExecuteStrategy {

  public abstract void setExecutor(SERankExtractor executor);

  public abstract SERankExtractor getExecutor();

  public abstract void execute();

}
