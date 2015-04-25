package com.mdeng.serank;

import com.mdeng.serank.spiders.GrabResult;

public abstract class KeywordRank {
  private String keyword;
  private GrabResult result = GrabResult.FAILED;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public GrabResult getResult() {
    return result;
  }

  public void setResult(GrabResult result) {
    this.result = result;
  }

}
