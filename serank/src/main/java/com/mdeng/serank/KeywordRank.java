package com.mdeng.serank;

import java.util.List;

import com.google.common.collect.Lists;
import com.mdeng.serank.spiders.GrabResult;

public class KeywordRank {
  private String keyword;
  private GrabResult result = GrabResult.EMPTY_PAGE;
  private List<RankInfo> rankInfos = Lists.newArrayList();

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

  public List<RankInfo> getRankInfos() {
    return rankInfos;
  }

  public void setRankInfos(List<RankInfo> rankInfos) {
    this.rankInfos = rankInfos;
  }

  public void addRankInfo(RankInfo rInfo) {
    this.rankInfos.add(rInfo);
  }
}
