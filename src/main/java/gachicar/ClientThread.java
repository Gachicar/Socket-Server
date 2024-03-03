package gachicar;

import java.io.BufferedReader;
import java.net.Socket;

public class ClientThread implements Runnable {
    Socket client;
    BufferedReader ois;
    String receiveData;

    public ClientThread(Socket s, BufferedReader ois){
        client = s;
        this.ois = ois;
    }

    public void run() {
        try {
            while ((receiveData = ois.readLine()) != null)
                System.out.println(receiveData);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
