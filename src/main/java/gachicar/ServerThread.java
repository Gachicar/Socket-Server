package gachicar;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread implements Runnable{
    Socket child;
    BufferedReader ois;
    PrintWriter oos;
    HashMap<String, PrintWriter> hm;
    InetAddress ip;
    String nickname;

    public ServerThread(Socket s, HashMap<String, PrintWriter> h, String user_id) throws IOException {
        child = s;
        hm = h;

        try	{
            ois = new BufferedReader(new InputStreamReader(child.getInputStream()));
            oos = new PrintWriter(child.getOutputStream(), true);

            ip = child.getInetAddress();
            nickname = user_id;

            synchronized (hm) { //임계영역 설정
                hm.put(user_id, oos);
            }

            System.out.println(ip + "로부터 " + user_id + "님이 접속하였습니다.");

            String userCarName = "가치카";
            oos.println("사용자의 공유차량: " + userCarName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String inputLine;
        try {
            while ((inputLine = ois.readLine()) != null) {
                // 클라이언트 접속 종료 명령
                if (inputLine.equals("/quit")) {
                    // 주행 기록 요약
                    report(nickname);

                    synchronized(hm) {
                        hm.remove(nickname);
                    }
                    break; // 클라이언트와의 연결 종료
                }
                else if (inputLine.contains("집") && inputLine.contains("가")) {
                    speakToMe("네, 알겠습니다.");
                    checkRC("학교", "집", "시작");

                    // RC카에 명령 보내기

                }
                else {
                    System.out.println("Received from client: " + inputLine);
                    oos.println(inputLine);  // 클라이언트가 보낸 메시지 에코
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ois.close();
                oos.close();
                child.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // 전체 메시지
    public void broadcast(String message){
        synchronized(hm) {
            try {
                for (PrintWriter oos : hm.values( )){
                    oos.println(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 클라이언트 메시지에 대한 서버 응답 보내기
    public void sendMsgToMe(String message){
        hm.forEach((key, value) -> {
            if (key.equals(nickname)) {
                PrintWriter oos = hm.get(key);
                System.out.println(message);
                oos.println(message);
            }
        });
    }

    // 다른 클라이언트에 메시지 보내기
    public void sendMsgToOther(String message){
        hm.forEach((key, value) -> {
            if (!key.equals(nickname)) {
                PrintWriter oos = hm.get(key);
                oos.println(message);
            }
        });
    }

    // 사용자 명령 확인 메시지 생성
    public void checkRC(String departure, String destination, String Command) {
        // 원래는.. 사용자 id로 공유차량 정보 가져오기
        // + 사용자, 출발지, 목적지 => DB에 저장
        sendMsgToMe("r/- 출발지: " + departure +
                "\n- 목적지: " + destination +
                "\n- 명령어: " + "시작");
    }

    public void speakToMe(String message) {
        sendMsgToMe("spk/" + message);
    }

    public void report(String user_id) {
        // 사용자 정보로 공유차량 주행 기록 가져오기
        String msg = "[차량 주행 기록]" +
                "\n주행 거리: 10km" +
                "\n주유 상태: 보통" +
                "\n주행 시간: 30분";
        sendMsgToMe("report/" + msg);
    }
}
