package gachicar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    String ipAddress;
    static final int port = 9595;
    Socket client = null;
    BufferedReader read;
    PrintWriter oos;
    BufferedReader ois;
    Long user_id;
    String sendData;
    ClientThread rt;

    boolean endflag = false;

    public SocketClient(Long id, String ip) {
        user_id = id;
        ipAddress = ip;

        try {
            client = new Socket(ip, port);

            read = new BufferedReader(new InputStreamReader(System.in));
            ois = new BufferedReader(new InputStreamReader(client.getInputStream()));
            oos = new PrintWriter(client.getOutputStream(), true);

            // 사용자 아이디를 먼저 보냄.
//            oos.println(user_id);

            rt = new ClientThread(client, ois);
            Thread t = new Thread(rt);
            t.start();

            while (true) {
                sendData = read.readLine();
                // 사용자가 보낸 메시지 출력
                System.out.println(user_id + ": " + sendData);
                oos.println(sendData);  // 메시지 전송

                if (sendData.equals("/quit")) {
                    System.out.println("클라 quit");
                    endflag = true;
                    break;
                }
            }
            System.out.print("클라이트의 접속을 종료합니다.");
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
                oos.close();
                client.close();
                System.exit(0);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
//        System.out.print("아이디를 입력하세요: ");
//        Long id = sc.nextLong();

        new SocketClient(1L, "localhost");
    }
}
