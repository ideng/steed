package com.mdeng.quiz.graph.astar;

public class N implements Comparable<N>{
  int x;
  int y;
  int[] cost;
  
  int g;
  int h;
  N parent;
  boolean visited;
  
  @Override
  public int compareTo(N o) {
    return Integer.valueOf(h).compareTo(o.h);
  }
}
