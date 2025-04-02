package case1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
      // 현재 클래스 파일의 위치를 기준으로 sample.txt 파일을 찾습니다
      String filePath = Main.class.getResource("").getPath() + "sample.txt";
      File file = new File(filePath);
      BufferedReader br = new BufferedReader(new FileReader(file));
      for(int i = 0; i < 20; i++){
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int w = Integer.parseInt(st.nextToken());
        int num = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        int result = Integer.parseInt(st.nextToken());
        Solution solution = new Solution();
        int answer = solution.solution(n, w, num);
        if(answer == result){

          System.out.println("===============================================" +i + "===============================================");
          System.out.println("answer : " + answer + " result : " + result);
          System.out.println("정답");
        }else{
          System.out.println("===============================================" +i + "===============================================");
          System.out.println("answer : " + answer + " result : " + result);
          System.out.println("오답");
        }
      }
      br.close();
    }
    static class Solution {
      public int solution(int n, int w, int num) {
        // 총 층수
        int totalFloor = n/w;
        if (n%w != 0) totalFloor++;
        // 목표 층수
        int targetFloor = num/w;
        if (num%w != 0) targetFloor++;
        
        if (totalFloor % 2 == 0){
            // 마지막 층 상자 여부
            int diff = num - (w * totalFloor);
            if(diff >= 0) return totalFloor - targetFloor + 1;
            else return totalFloor - targetFloor;
        }else{
            // 마지막 층 상자 여부
            int diff = (w * totalFloor) - num;
            if(diff >= 0) return totalFloor - targetFloor;
            else return totalFloor - targetFloor + 1;
        }   
      }
  }
} 