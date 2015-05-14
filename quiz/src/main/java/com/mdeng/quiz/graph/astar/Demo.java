package com.mdeng.quiz.graph.astar;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

public class Demo {

  public static void main(String[] args) {
    Demo demo = new Demo();
    N[][] graph = demo.init();
    System.out.println(System.currentTimeMillis());
    demo.astar(graph[0][3], graph[430][28],new H(){

      @Override
      public int h(N a, N b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
      }
      
    });
    System.out.println(System.currentTimeMillis());
    
    //demo.print(graph[43][28]);
    demo.reset();
    
    System.out.println(System.currentTimeMillis());
    demo.astar(graph[0][3], graph[430][28],new H(){

      @Override
      public int h(N a, N b) {
        return 0;
      }
      
    });
    System.out.println(System.currentTimeMillis());
    
    System.exit(0);
  }

  // astar(start, end) {
  // OPEN <- start, CLOSED <- empty
  // while(OPEN!=empty) {
  // get n from OPEN with min h
  // if(neighbor == end) break;
  // for(neighbor in n.neighbors) {
  // if(neighbor in CLOSED) continue;
  // else {
  // if(neighbor in OPEN) {
  // if(n.g + cost(n,neighbor)<neighbor.g) {
  // neighbor.g = n.g + cost(n,neighbor);
  // neighbor.parent = n;
  // }
  // } else {
  // OPEN <- neighbor;
  // neighbor.h = computeH(neighbor);
  // neighbout.g = n.g + cost(n,neighbor);
  // neighbor.parent = n;
  // }
  // }
  // }
  // CLOSED <- n
  // }

  PriorityQueue<N> open = new PriorityQueue<N>();
  int[][] dir = { {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
  int m = 1000;
  N[][] graph;

  public void astar(N start, N end, H h) {
    open.offer(start);
    while (!open.isEmpty()) {
      N n = open.poll();
      if (n == end) break;

      for (int i = 0; i < dir.length; i++) {
        int x = n.x + dir[i][0];
        int y = n.y + dir[i][1];
        if (isValid(x, y)) {
          N k = graph[x][y];

          int cost = n.cost[i];
          if (k.visited)
            continue;
          else if (open.contains(k)) {
            if (n.g + cost < k.g) {
              k.g = n.g + cost;
              k.parent = n;
            }
          } else {
            open.offer(k);
            k.h = h.h(k, end);
            k.g = n.g + cost;
            k.parent = n;
          }
        }
      }
      n.visited = true;
    }
  }

  private boolean isValid(int x,int y) {
    return x>=0 && x<m && y>=0 && y<m;
  }

  N[][] init() {
    Random random = new Random();
    graph = new N[m][m];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < m; j++) {
        graph[i][j] = new N();
        graph[i][j].x = i;
        graph[i][j].y = j;
        graph[i][j].cost = new int[4];
        for (int k = 0; k < graph[i][j].cost.length; k++) {
          int c = random.nextInt(11);
          if (c == 0) c = 1;
          graph[i][j].cost[k] = c;
        }
      }
    }

    return graph;
  }

  int h(N a, N b) {
    //return 0;
    return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
  }
  
  void print(N end) {
    System.out.println("("+end.x+","+end.y+")" );
    if (end.parent!=null) {
      print(end.parent);
    }
  }
  
  void reset() {
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < m; j++) {
        graph[i][j].g=0;
        graph[i][j].h=0;
        graph[i][j].parent=null;
        graph[i][j].visited=false;
      }
    }
  }
}
