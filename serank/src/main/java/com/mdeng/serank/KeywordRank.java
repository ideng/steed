package com.mdeng.serank;

import java.util.List;

import com.google.common.collect.Lists;
import com.mdeng.serank.spiders.GrabResult;

public class KeywordRank {
  private String keyword;
  private List<RankInfo> ranks = Lists.newArrayList();
  private GrabResult result = GrabResult.FAILED;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public List<RankInfo> getRanks() {
    return ranks;
  }

  public void setRanks(List<RankInfo> ranks) {
    this.ranks = ranks;
  }

  public GrabResult getResult() {
    return result;
  }

  public void setResult(GrabResult result) {
    this.result = result;
  }

  class RankInfo {
    private String host;
    private String address;
    private int rank;

    public String getHost() {
      return host;
    }

    public void setHost(String host) {
      this.host = host;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public int getRank() {
      return rank;
    }

    public void setRank(int rank) {
      this.rank = rank;
    }

  }
}
