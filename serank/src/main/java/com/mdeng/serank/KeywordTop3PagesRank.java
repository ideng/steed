package com.mdeng.serank;

import java.util.List;

import com.google.common.collect.Lists;

public class KeywordTop3PagesRank extends KeywordRank {
  private List<RankInfo> ranks = Lists.newArrayList();

  public List<RankInfo> getRanks() {
    return ranks;
  }

  public void setRanks(List<RankInfo> ranks) {
    this.ranks = ranks;
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
