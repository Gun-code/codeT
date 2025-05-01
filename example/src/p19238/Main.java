package p19238;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

  static class Node {
    int x, y;
    int dist;
    boolean isVisited;
    boolean isCustomer;

    Node(int x, int y, int dist) {
      this.x = x;
      this.y = y;
      this.dist = dist;
      this.isVisited = false;
    }
    Node(int x, int y, int dist, boolean isVisited) {
      this.x = x;
      this.y = y;
      this.dist = dist;
      this.isVisited = isVisited;
    }
    
  }

  public static class Graph {
    Node[][] node;
    Graph(int size) {
      node = new Node[size][size];
    }
    public void addNode(int x, int y, int dist, boolean isVisited) {
      node[x][y] = new Node(x, y, dist, isVisited);
    }
    public void addNode(int x, int y, int dist) {
      node[x][y] = new Node(x, y, dist);
    }
    public void setCustomer(int x, int y) {
      node[x][y].isCustomer = true;
    }
    public boolean isVisited(int x, int y) {
      return node[x][y].isVisited;
    }
    public void setVisited(int x, int y, int dist) {
      node[x][y].isVisited = true;
      node[x][y].dist = dist;
    }
  }

  static class Taxi {
    int x, y;
    int gas;
    Taxi(int x, int y, int gas) {
      this.x = x;
      this.y = y;
      this.gas = gas;
    }
    public void move(int x, int y, int dist) {
      this.x = x;
      this.y = y;
      this.gas -= dist;
    }
    public void getGas(int dist) {
      this.gas += 2 * dist;
    }
    public boolean isGasEnough(int dist) {
      return this.gas >= dist;
    }
    public Node searchCustomer(Graph graph) {// 손님 찾기 BFS
      if (x < 0 || x >= graph.node.length || y < 0 || y >= graph.node[0].length) {
        return null;
      }
      Queue<Node> queue = new LinkedList<>();
      queue.add(graph.node[x][y]);
      int dist = 0;
      int size = graph.node.length;
      int minDist = Integer.MAX_VALUE;
      ArrayList<Node> candidates = new ArrayList<>();

      while(!queue.isEmpty()) {
        Node current = queue.poll();
        if(current.isVisited) continue;
        current.isVisited = true;
        // 최소거리의 손님이 여러명일 경우를 대비하여 최소거리의 손님을 모두 저장
        if(current.isCustomer && current.dist <= minDist) {
          current.dist += dist;
          graph.setVisited(current.x, current.y, current.dist);
          minDist = current.dist;
          candidates.add(current);
        }
        // 여러명의 손님을 행,열 오름차순으로 정렬
        if(current.dist > minDist){
          candidates.sort((a, b) -> {
            if(a.x == b.x) return a.y - b.y;
            return a.x - b.x;
          });
          return candidates.get(0);
        }

        // 현재 위치에서 상하좌우 탐색
        dist += 1; // 거리 증가

        // size 범위 체크
        if(current.x + 1 < size && current.y >= 0 && current.y < size && !graph.isVisited(current.x + 1, current.y)){
          queue.add(graph.node[current.x + 1][current.y]);
          graph.setVisited(current.x + 1, current.y, dist);
        }
        if(current.x - 1 >= 0 && current.y >= 0 && current.y < size && !graph.isVisited(current.x - 1, current.y)){
          queue.add(graph.node[current.x - 1][current.y]);
          graph.setVisited(current.x - 1, current.y, dist);
        }
        if(current.y + 1 < size && current.x >= 0 && current.x < size && !graph.isVisited(current.x, current.y + 1)){
          queue.add(graph.node[current.x][current.y + 1]);
          graph.setVisited(current.x, current.y + 1, dist);
        }
        if(current.y - 1 >= 0 && current.x >= 0 && current.x < size && !graph.isVisited(current.x, current.y - 1)){
          queue.add(graph.node[current.x][current.y - 1]);
          graph.setVisited(current.x, current.y - 1, dist);
        }
      } 
      return null;
    }

  }

  static class Customer {
    int x, y;
    int destX, destY;
    int dist;
    Customer(int x, int y, int destX, int destY) {
      this.x = x;
      this.y = y;
      this.destX = destX;
      this.destY = destY;
    }
    public Node calcDist(Graph graph) { // 손님 찾기 후 목적지까지의 거리 계산 BFS
      Queue<Node> queue = new LinkedList<>();
      queue.add(graph.node[x][y]);
      int dist = 0;
      int size = graph.node.length;
      Node destination = graph.node[destX][destY];

      while(!queue.isEmpty()) {
        Node current = queue.poll();
        if(current.isVisited) continue;
        current.isVisited = true;
        
        if(current.x == destination.x && current.y == destination.y) {
          current.dist += dist;
          graph.setVisited(current.x, current.y, current.dist);
          return graph.node[current.x][current.y];
        }

        // 현재 위치에서 상하좌우 탐색
        dist += 1; // 거리 증가

        // size 범위 체크
        if(current.x + 1 < size && current.y >= 0 && current.y < size && !graph.isVisited(current.x + 1, current.y)){
          queue.add(graph.node[current.x + 1][current.y]);
          graph.setVisited(current.x + 1, current.y, dist);
        }
        if(current.x - 1 >= 0 && current.y >= 0 && current.y < size && !graph.isVisited(current.x - 1, current.y)){
          queue.add(graph.node[current.x - 1][current.y]);
          graph.setVisited(current.x - 1, current.y, dist);
        }
        if(current.y + 1 < size && current.x >= 0 && current.x < size && !graph.isVisited(current.x, current.y + 1)){
          queue.add(graph.node[current.x][current.y + 1]);
          graph.setVisited(current.x, current.y + 1, dist);
        }
        if(current.y - 1 >= 0 && current.x >= 0 && current.x < size && !graph.isVisited(current.x, current.y - 1)){
          queue.add(graph.node[current.x][current.y - 1]);
          graph.setVisited(current.x, current.y - 1, dist);
        }
      } 
      return null;
    }
  }


  public static void main(String[] args) throws IOException {
    System.setIn(new FileInputStream("src/p19238/sample1.txt"));
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    // 맵 크기, 손님 수, 초기 연료
    int size = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());
    int gas = Integer.parseInt(st.nextToken());

    // 맵 입력
    Graph graph = new Graph(size);
    for(int i = 0; i < size; i++) {
      st = new StringTokenizer(br.readLine());
      for(int j = 0; j < size; j++) {
        if(st.nextToken().equals("1")) {
          graph.addNode(i, j, 0, false);
        }else {
          graph.addNode(i, j, 0);
        }
      }
    }
    Graph snapShot = graph;
    
    // 택시 초기 위치
    st = new StringTokenizer(br.readLine());
    int taxiX = Integer.parseInt(st.nextToken());
    int taxiY = Integer.parseInt(st.nextToken());
    Taxi taxi = new Taxi(taxiX, taxiY, gas);

    // 손님 정보
    Customer[] customers = new Customer[M];
    for(int i = 0; i < M; i++) {
      st = new StringTokenizer(br.readLine());
      int x = Integer.parseInt(st.nextToken());
      int y = Integer.parseInt(st.nextToken());
      int destX = Integer.parseInt(st.nextToken());
      int destY = Integer.parseInt(st.nextToken());
      graph.setCustomer(x, y);
      customers[i] = new Customer(x, y, destX, destY);
    }

    while(M > 0) {
      Node customerNode = taxi.searchCustomer(graph);
      
      if (customerNode == null) {
        System.out.println(-1);
        return;
      }

      if(taxi.isGasEnough(customerNode.dist)) {
        taxi.move(customerNode.x, customerNode.y, customerNode.dist);
        graph = snapShot;
        Node destination = null;
        int destDist = 0;
        for(int i = 0; i < M; i++) {
          if(customers[i].x == customerNode.x && customers[i].y == customerNode.y) {
            destination = customers[i].calcDist(graph);
            destDist = destination.dist;
            break;
          }
        }
        if(taxi.isGasEnough(destDist)) {
          taxi.move(destination.x, destination.y, destDist);
          graph = snapShot;
          taxi.getGas(destDist);
        }
      }else {
        System.out.println(-1);
        return;
      }
      M--;
    }
  }
}
