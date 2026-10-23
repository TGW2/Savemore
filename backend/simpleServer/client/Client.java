package simpleServer.client;

import java.io.*;
import java.net.*;

public class Client {
    Socket connection;
    PrintWriter pr;
    /**
     * telling port 9901 i'm making connection
     */
    public void makePortConnection(){
        try{
            connection = new Socket("127.0.0.1",9901);
            pr = new PrintWriter(connection.getOutputStream(),true);
        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(IOException m){
            m.printStackTrace();
        }
    }

    /**
     * send info through 'infoWriting' and pass the information to server
     * @param info
     */
    public void infoWriting(String info) {
        pr.println(info);
        if (pr.checkError()) {
            System.out.println("Message sending failed");
        }
    }

    /**
     * 关闭当前线程的输入，关闭建立的连接
     */
    public void close(){
        try{
            connection.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
