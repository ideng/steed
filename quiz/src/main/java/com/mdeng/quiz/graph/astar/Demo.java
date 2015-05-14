package com.mdeng.quiz.graph.astar;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Demo {
  
  public static void h1(Demo demo,N start, N end) {

    Stopwatch stopwatch = new Stopwatch();
    stopwatch.start();
    demo.astar(start, end, new H() {

      @Override
      public int h(N a, N b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
      }

    });
    stopwatch.stop();
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    //demo.print(end);
    demo.reset();
  }

  public static void h2(Demo demo,N start, N end) {

    Stopwatch stopwatch = new Stopwatch();
    stopwatch.start();
    demo.astar(start, end, new H() {

      @Override
      public int h(N a, N b) {
        return 0;
      }

    });
    stopwatch.stop();
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    //demo.print(end);
    demo.reset();
  }

  public static void main(String[] args) throws InterruptedException {
    Demo demo = new Demo();
    N[][] g = demo.init();
    //h1(demo,g[0][0],g[499][499]);
    //TimeUnit.SECONDS.sleep(5);
    h2(demo,g[0][0],g[499][499]);
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
  int[][] dir = { {0, -1}, {0, 1}, {-1, 0}, {1, 0}};// up down left right
  //int[] cost = {1, 2, 3, 4};
  int X = 500;
  int Y = 500;
  N[][] v;
  int[][] e;

  public void astar(N start, N end, H h) {
    open.offer(start);
    while (!open.isEmpty()) {
      N n = open.poll();
      if (n == end) {
        break;
      }

      for (int i = 0; i < dir.length; i++) {
        int x = n.x + dir[i][0];
        int y = n.y + dir[i][1];
        if (isValid(x, y)) {
          N k = v[x][y];

          if (k.visited)
            continue;
          else if (open.contains(k)) {
            if (n.g + n.cost[i] < k.g) {
              k.g = n.g + n.cost[i];
              k.parent = n;
            }
          } else {
            k.h = h.h(k, end);
            k.g = n.g + n.cost[i];
            k.parent = n;
            open.offer(k);
          }
        }
      }
      n.visited = true;
    }
  }

  private boolean isValid(int x, int y) {
    return x >= 0 && x < X && y >= 0 && y < Y;
  }

  N[][] init() {
    v = new N[X][Y];

    Random random = new Random();
    for (int i = 0; i < X; i++) {
      for (int j = 0; j < Y; j++) {
        v[i][j] = new N();
        v[i][j].x = i;
        v[i][j].y = j;
        v[i][j].cost = new int[4];
        for (int k = 0; k < 4; k++) {
          int r = random.nextInt(11);
          r = r == 0 ? 1 : r;
          v[i][j].cost[k] = r;
        }
      }
    }
    return v;
  }

  int h(N a, N b) {
    // return 0;
    return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
  }

  void print(N end) {
    System.out.println("(" + end.x + "," + end.y + ")");
    if (end.parent != null) {
      print(end.parent);
    }
  }

  void reset() {
    open.clear();
    for (int i = 0; i < X; i++) {
      for (int j = 0; j < Y; j++) {
        v[i][j].g = 0;
        v[i][j].h = 0;
        v[i][j].parent = null;
        v[i][j].visited = false;
      }
    }
  }
}
