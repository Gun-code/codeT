package p19238;

import java.io.BufferedReader;
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
    public void setCustomer(int x, int y) {
      node[x][y].isCustomer = true;
    }
    public void delCustomer(int x, int y) {
      node[x][y].isCustomer = false;
    }
    public boolean isVisited(int x, int y) {
      return node[x][y].isVisited;
    }
    public void setVisited(int x, int y, int dist) {
      node[x][y].isVisited = true;
      node[x][y].dist = dist;
    }
    public void setVisited(int x, int y, int dist, boolean isVisited) {
      node[x][y].isVisited = isVisited;
      node[x][y].dist = dist;
    }
    public void setTaxi(int x, int y, boolean isVisited) {
      node[x][y].isVisited = isVisited;
    }
    public void copyFrom(Graph other) {
      for(int i = 0; i < node.length; i++) {
        for(int j = 0; j < node.length; j++) {
          if(node[i][j] != null && other.node[i][j] != null) {
            node[i][j].dist = other.node[i][j].dist;
            node[i][j].isVisited = other.node[i][j].isVisited;
            node[i][j].isCustomer = other.node[i][j].isCustomer;
          }
        }
      }
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
      
      if(graph.node[x][y].isCustomer) return graph.node[x][y];
      
      graph.setTaxi(x, y, true);
      Queue<Node> queue = new LinkedList<>();
      queue.add(graph.node[x][y]);
      int size = graph.node.length;
      int minDist = Integer.MAX_VALUE;
      boolean isShortcut = false;
      ArrayList<Node> candidates = new ArrayList<>();

      while(!queue.isEmpty()) {
        Node current = queue.poll();


        // 최소거리의 손님이 여러명일 경우를 대비하여 최소거리의 손님을 모두 저장
        if(current.isCustomer && current.dist <= minDist) {
          graph.setVisited(current.x, current.y, current.dist);
          candidates.add(current);
          if(!isShortcut) {
            minDist = current.dist;
            isShortcut = true;
          }
        }
        // 여러명의 손님을 행,열 오름차순으로 정렬
        if(current.dist > minDist){
          candidates.sort((a, b) -> {
            if(a.x == b.x) return a.y - b.y;
            return a.x - b.x;
          });
          return candidates.get(0);
        }
        if(current.isCustomer) continue;

        // 현재 위치에서 상하좌우 탐색
        int nextDist = current.dist + 1;

        // size 범위 체크
        if(current.x + 1 < size && current.y >= 0 && current.y < size && !graph.isVisited(current.x + 1, current.y)){
          Node next = graph.node[current.x + 1][current.y];
          next.dist = nextDist;
          queue.add(next);
          graph.setVisited(current.x + 1, current.y, nextDist);
        }
        if(current.x - 1 >= 0 && current.y >= 0 && current.y < size && !graph.isVisited(current.x - 1, current.y)){
          Node next = graph.node[current.x - 1][current.y];
          next.dist = nextDist;
          queue.add(next);
          graph.setVisited(current.x - 1, current.y, nextDist);
        }
        if(current.y + 1 < size && current.x >= 0 && current.x < size && !graph.isVisited(current.x, current.y + 1)){
          Node next = graph.node[current.x][current.y + 1];
          next.dist = nextDist;
          queue.add(next);
          graph.setVisited(current.x, current.y + 1, nextDist);
        }
        if(current.y - 1 >= 0 && current.x >= 0 && current.x < size && !graph.isVisited(current.x, current.y - 1)){
          Node next = graph.node[current.x][current.y - 1];
          next.dist = nextDist;
          queue.add(next);
          graph.setVisited(current.x, current.y - 1, nextDist);
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
      
      if(destX == x && destY == y) return graph.node[destX][destY];
      Queue<Node> queue = new LinkedList<>();
      queue.add(graph.node[x][y]);
      int size = graph.node.length;
      graph.setVisited(x, y, 0, true);

      while(!queue.isEmpty()) {
        Node current = queue.poll();
        current.isVisited = true;

        if(current.x == destX && current.y == destY)  return graph.node[destX][destY];

        // 현재 위치에서 상하좌우 탐색
        int nextDist = current.dist + 1;
        // size 범위 체크
        if(current.x + 1 < size && current.y >= 0 && current.y < size && !graph.isVisited(current.x + 1, current.y)){
          Node next = graph.node[current.x + 1][current.y];
          next.dist = nextDist;
          queue.add(next);
          graph.setVisited(current.x + 1, current.y, nextDist);
        }
        if(current.x - 1 >= 0 && current.y >= 0 && current.y < size && !graph.isVisited(current.x - 1, current.y)){
          Node next = graph.node[current.x - 1][current.y];
          next.dist = nextDist;
          queue.add(next);
          graph.setVisited(current.x - 1, current.y, nextDist);
        }
        if(current.y + 1 < size && current.x >= 0 && current.x < size && !graph.isVisited(current.x, current.y + 1)){
          Node next = graph.node[current.x][current.y + 1];
          next.dist = nextDist;
          queue.add(next);
          graph.setVisited(current.x, current.y + 1, nextDist);
        }
        if(current.y - 1 >= 0 && current.x >= 0 && current.x < size && !graph.isVisited(current.x, current.y - 1)){
          Node next = graph.node[current.x][current.y - 1];
          next.dist = nextDist;
          queue.add(next);
          graph.setVisited(current.x, current.y - 1, nextDist);
        }
      } 
      return null;
    }
  }


  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    // 맵 크기, 손님 수, 초기 연료
    int size = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());
    int gas = Integer.parseInt(st.nextToken());

    // 맵 입력
    Graph graph = new Graph(size);
    Graph snapShot = new Graph(size);
    for(int i = 0; i < size; i++) {
      st = new StringTokenizer(br.readLine());
      for(int j = 0; j < size; j++) {
        if(st.nextToken().equals("1")) {
          graph.addNode(i, j, 0, true);
          snapShot.addNode(i, j, 0, true);
        }else {
          graph.addNode(i, j, 0, false);
          snapShot.addNode(i, j, 0, false);
        }
      }
    }
    
    
    // 택시 초기 위치
    st = new StringTokenizer(br.readLine());
    int taxiX = Integer.parseInt(st.nextToken()) - 1;
    int taxiY = Integer.parseInt(st.nextToken()) - 1;
    Taxi taxi = new Taxi(taxiX, taxiY, gas);

    // 손님 정보
    ArrayList<Customer> customers = new ArrayList<>();
    for(int i = 0; i < M; i++) {
      st = new StringTokenizer(br.readLine());
      int x = Integer.parseInt(st.nextToken()) - 1;
      int y = Integer.parseInt(st.nextToken()) - 1;
      int destX = Integer.parseInt(st.nextToken()) - 1;
      int destY = Integer.parseInt(st.nextToken()) - 1;
      graph.setCustomer(x, y);
      snapShot.setCustomer(x, y);
      customers.add(new Customer(x, y, destX, destY));
    }
    while(M > 0) {
      Node customerNode = taxi.searchCustomer(graph);
      if (customerNode == null) {
        System.out.println(-1);
        return;
      }

      System.out.println("손님좌표 : " + customerNode.x + " " + customerNode.y);
      if(taxi.isGasEnough(customerNode.dist)) {
        taxi.move(customerNode.x, customerNode.y, customerNode.dist);
        snapShot.delCustomer(customerNode.x, customerNode.y);
        graph.copyFrom(snapShot);
        Node destination = null;
        int destDist = 0;
  
        for(int i = 0; i < customers.size(); i++) {
          if(customers.get(i).x == customerNode.x && customers.get(i).y == customerNode.y) {
            destination = customers.get(i).calcDist(graph);
            System.out.println("목적지좌표 : " + destination.x + " " + destination.y);
            destDist = destination.dist;
            customers.remove(i);
            break;
          }
        }
        if(taxi.isGasEnough(destDist)) {
          taxi.move(destination.x, destination.y, destDist);
          graph.copyFrom(snapShot);
          taxi.getGas(destDist);
        }else{
          System.out.println(-1);
          return;
        }
      }else {
        System.out.println(-1);
        return;
      }
      M--;
    }
    System.out.println(taxi.gas);
  }
}
